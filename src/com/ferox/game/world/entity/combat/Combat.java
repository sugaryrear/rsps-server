package com.ferox.game.world.entity.combat;

import com.google.common.base.Stopwatch;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.formula.AccuracyFormula;
import com.ferox.game.world.entity.combat.formula.CombatFormula;
import com.ferox.game.world.entity.combat.hit.HitDamageCache;
import com.ferox.game.world.entity.combat.hit.HitQueue;
import com.ferox.game.world.entity.combat.magic.CombatSpell;
import com.ferox.game.world.entity.combat.magic.CombatSpells;
import com.ferox.game.world.entity.combat.method.CombatMethod;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.combat.method.impl.specials.melee.GraniteMaul;
import com.ferox.game.world.entity.combat.ranged.RangedData.RangedWeapon;
import com.ferox.game.world.entity.combat.skull.SkullType;
import com.ferox.game.world.entity.combat.skull.Skulling;
import com.ferox.game.world.entity.combat.weapon.FightType;
import com.ferox.game.world.entity.combat.weapon.WeaponType;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.game.world.route.RouteMisc;
import com.ferox.game.world.route.routes.DumbRoute;
import com.ferox.game.world.route.routes.TargetRoute;
import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.Debugs;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.Utils;
import com.ferox.util.timers.TimerKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.IntStream;

import static com.ferox.game.content.daily_tasks.DailyTaskUtility.DAILY_TASK_MANAGER_INTERFACE;
import static com.ferox.util.NpcIdentifiers.*;

/**
 * My entity-based combat system. The main class of the system.
 *
 * @author Swiffy
 */

public class Combat {

    private static final Logger logger = LogManager.getLogger(Combat.class);

    public boolean attemptHit(Mob me, Mob target, CombatType style) {
        return AccuracyFormula.doesHit(me, target, style);
    }

    public CombatSpell[] AUTOCAST_SPELLS = {
        CombatSpells.TRIDENT_OF_THE_SEAS.getSpell(),
        CombatSpells.TRIDENT_OF_THE_SWAMP.getSpell(),
        CombatSpells.SANGUINESTI_STAFF.getSpell(),
        CombatSpells.CRUCIATUS_CURSE.getSpell(),
        CombatSpells.PETRIFICUS_TOTALUS.getSpell(),
        CombatSpells.AVADA_KEDAVRA.getSpell(),
        CombatSpells.EXPELLIARMUS.getSpell(),
        CombatSpells.SECTUMSEMPRA.getSpell()
    };

    public static CombatSpell[] ELDER_WAND_SPELLS = {
        CombatSpells.CRUCIATUS_CURSE.getSpell(),
        CombatSpells.PETRIFICUS_TOTALUS.getSpell(),
        CombatSpells.AVADA_KEDAVRA.getSpell(),
        CombatSpells.EXPELLIARMUS.getSpell(),
        CombatSpells.SECTUMSEMPRA.getSpell()
    };

    // The user's damage map
    private Map<Mob, HitDamageCache> damageMap;

    public Map<Mob, HitDamageCache> getDamageMap() {
        if (damageMap == null)
            damageMap = new HashMap<>(); // only create when code needs it!
        return damageMap;
    }

    public void setDamageMap(Map<Mob, HitDamageCache> damageMap) {
        this.damageMap = damageMap;
    }

    public void clearDamagers() {
        if (damageMap == null) return;
        damageMap.clear();
    }

    // Ranged data
    public RangedWeapon rangedWeapon;
    // The user's HitQueue
    private final HitQueue hitQueue;
    // The mob
    private final Mob mob;
    // The mob's current target
    private Mob target;
    // The last combat method used
    private CombatMethod method;
    // Fight type
    private FightType fightType = FightType.UNARMED_KICK;
    // WeaponInterface
    private WeaponType weapon;
    // Autoretaliate
    private boolean autoRetaliate;
    // Magic data
    private CombatSpell castSpell;
    private CombatSpell autoCastSpell;

    public Combat(Mob mob) {
        this.mob = mob;
        this.hitQueue = new HitQueue();
    }

    public CombatType combatType() {
        CombatType combatType = null;
        if (method instanceof CommonCombatMethod) {
            CommonCombatMethod commonCombatMethod = (CommonCombatMethod) method;
            combatType = commonCombatMethod.styleOf();
        }
        return combatType;
    }

    public void setMethod(CombatMethod method) {
        this.method = method;
    }

    public void delayAttack(int ticks) {
        mob.getTimers().extendOrRegister(TimerKey.COMBAT_ATTACK, ticks);
    }

    public int maximumMagicHit() {
        if (mob.isNpc()) {
            return mob.getAsNpc().combatInfo() == null ? 0 : mob.getAsNpc().combatInfo().maxhit;
        }
        Player player = mob.getAsPlayer();
        CombatSpell spell = player.getCombat().getCastSpell() != null ? player.getCombat().getCastSpell() : player.getCombat().getAutoCastSpell();
        if (spell == null) {
            //System.out.println("maximumMagicHit: Spell is null.");
            Utils.sendDiscordInfoLog("maximumMagicHit: Spell is null.");
            return 0;
        }
        int maxhit = spell.baseMaxHit();
        String name = spell.name();

        if (mob.isPlayer() && target.isNpc() && target.getAsNpc().id() == UNDEAD_COMBAT_DUMMY) {
            return CombatFormula.modifyMagicDamage(player, maxhit, name, false);
        }
        return CombatFormula.modifyMagicDamage(player, maxhit, name);
    }

    public int maximumMeleeHit() {
        if (mob.isNpc()) {
            return mob.getAsNpc().combatInfo() == null ? 0 : mob.getAsNpc().combatInfo().maxhit;
        }
        if (mob.isPlayer() && target != null && target.isNpc() && target.getAsNpc().id() == UNDEAD_COMBAT_DUMMY) {
            return CombatFormula.maximumMeleeHit(mob.getAsPlayer(), mob.getAsPlayer().isSpecialActivated(), false);
        }
        return CombatFormula.maximumMeleeHit(mob.getAsPlayer(), mob.getAsPlayer().isSpecialActivated());
    }

    /**
     * The maximum range hit
     *
     * @param ignoreArrowRangeStr Checks if we are ignoring arrows equipment
     * @return The max hit
     */
    public int maximumRangedHit(boolean ignoreArrowRangeStr) {
        if (mob.isNpc()) {
            return mob.getAsNpc().combatInfo() == null ? 0 : mob.getAsNpc().combatInfo().maxhit;
        }
        if (mob.isPlayer() && target.isNpc() && target.getAsNpc().id() == UNDEAD_COMBAT_DUMMY) {
            return CombatFormula.maximumRangedHit(mob.getAsPlayer(), mob.getAsPlayer().getCombat().target, mob.getAsPlayer().isSpecialActivated(), ignoreArrowRangeStr, false);
        }
        return CombatFormula.maximumRangedHit(mob.getAsPlayer(), mob.getAsPlayer().getCombat().target, mob.getAsPlayer().isSpecialActivated(), ignoreArrowRangeStr);
    }

    /**
     * Attacks an entity by updating our current target.
     *
     * @param target The target to attack.
     */
    public void attack(Mob target) {
        //When certain conditions are met you can no longer attack.
        if (mob.dead() || target.dead() || mob.isNoAttackLocked()) {
            return;
        }

        if (mob.isPlayer()) {
            Player player = mob.getAsPlayer();

            if (player.locked()) {
                return;
            }
            player.action.reset();
            //Also close dialogues when attacking.
            player.getInterfaceManager().closeDialogue();
            player.getRunePouch().close();
            player.action.clearNonWalkableActions();
            if (!player.getInterfaceManager().isMainClear()) {
                boolean ignore = player.getInterfaceManager().isInterfaceOpen(DAILY_TASK_MANAGER_INTERFACE) || player.getInterfaceManager().isInterfaceOpen(29050) || player.getInterfaceManager().isInterfaceOpen(55140);
                if(!ignore) {
                    //player.debugMessage("walkable interface is: " + player.getInterfaceManager().getWalkable());
                    player.getInterfaceManager().close(false);
                }
            }
        }

        if (target.isPlayer()) {
            target.getAsPlayer().getRunePouch().close();
        }

        //Set new target
        setTarget(target);

        // Set facing
        if (mob.getInteractingEntity() != target) {
            mob.setEntityInteraction(target);
        }

        CombatFactory.unfreezeWhenOutOfRange(target);
        // maybe that should be for players only then
        // in 317s the Movement packet is sent when you click a entity, we discard the walk data
        // because we use serverside pf instead
        // a path is calculated later
        if (mob.isPlayer())
            mob.getMovementQueue().clear();

        Debugs.CMB.debug(mob, "Attack", target, true);
    }

    /**
     * Processes combat.
     */
    public static final int[] DO_NOT_DRAW_HP = {2668,7413};
    public void process() {
        // Process the hit queue
        hitQueue.process(mob);

        // Handle attacking
        performNewAttack();

        if (mob.isPlayer() && target != null) {

            if (!target.getAsNpc().isCombatDummy())//don't draw the entity hp name/bar for combat dummies
            mob.getAsPlayer().getPacketSender().sendEntityFeed(target.getMobName(), target.hp(), target.maxHp());
        } else if (mob.isPlayer() && target == null) {
            mob.getAsPlayer().getPacketSender().resetEntityFeed();

            //No target found reset fight time
            if (fightTimer.isRunning()) {
                fightTimer.reset();
            }
        }
    }

    /**
     * Attempts to attack the target.
     */
    public void performNewAttack() {
        try {
            performNewAttack0();
        } catch (Exception e) {
            // log the combat state
            logger.error("performNewAttack ex on " + mob.getMobName());
            logger.error("perfNewAttack", e);
            StringBuilder sb = new StringBuilder();
            sb.append("combat state: ");
            sb.append(this);
            logger.error(sb.toString());
            e.printStackTrace();
            throw e; // send it up the callstack
        }
    }

    /**
     * the real method, without try-catch wrapped around it
     */
    private void performNewAttack0() {
        if (target == null) {
            //if (mob.isNpc() && mob.getAsNpc().id() == SKOTIZO)
            //System.out.println("no targ");
            return;
        }

        // Fetch the combat method the mob will be attacking with
        method = CombatFactory.getMethod(mob);

        if (method instanceof CommonCombatMethod) {
            ((CommonCombatMethod) method).set(mob, target);
        }
        updateLastTarget(target);

        // runite: player reach checks are done before hand, so we can nicely just check targetRoute.withinDistance
        if (mob.isPlayer() && mob.getRouteFinder() != null && mob.getRouteFinder().targetRoute != null && !mob.getRouteFinder().targetRoute.withinDistance) {
            //System.out.println("can't find?");
            return;
        }

        // runite npc reached check delegated to factory.canReach > CommonCombatMethod.inAttackRange
        if (mob.isPlayer()) {
            Debugs.CMB.debug(mob, "mtd " + method + " vs " + target);
        }

        if (!CombatFactory.canReach(mob, method, target)) {
          //  System.out.println("cant reach?");
            return;
        }

        if (target.isPlayer()) {
            Player player = target.getAsPlayer();

            if (!player.getInterfaceManager().isMainClear()) {
                boolean ignore = player.getInterfaceManager().isInterfaceOpen(DAILY_TASK_MANAGER_INTERFACE) || player.getInterfaceManager().isInterfaceOpen(29050) || player.getInterfaceManager().isInterfaceOpen(55140);
                if(!ignore) {
                    //player.debugMessage("walkable interface is: " + player.getInterfaceManager().getWalkable());
                    player.getInterfaceManager().close(false);
                }
            }
        }

        // Check if the mob can perform the attack
        if (!CombatFactory.canAttack(mob, method, target)) {
            mob.getCombat().reset();//We can't attack our target, reset combat
            //System.out.println("reeee?");
            return;
        }

        if (mob.isPlayer()) {
            // TODO you have to know the intended CombatStyle to be able to do canAttack
            if (method instanceof CommonCombatMethod) { // should be the base class of all scripts now
                CommonCombatMethod commonCombatMethod = (CommonCombatMethod) method;
                if (!commonCombatMethod.canAttackStyle(mob, target, commonCombatMethod.styleOf())) {
                    //System.out.println("wut?");
                    return;
                }
            }
        }

        int combatAttackTicksRemaining = mob.getTimers().left(TimerKey.COMBAT_ATTACK);

        //System.out.println(combatAttackTicksRemaining);

        boolean graniteMaulSpecial = (method instanceof GraniteMaul);

        // gmaul triggers when you've hit someone in the last 2 ticks.
        if (graniteMaulSpecial && specialGraniteMaul()) {
            return;
        }

        // temp fix due to a rogue prepareAttack seemingly setting target to null
        final Mob targ = target;

        // Make sure attack timer is <= 0
        if (combatAttackTicksRemaining <= 0) {
            // Face target
            mob.face(targ.getCentrePosition());
            if (mob.getInteractingEntity() != targ) {
                mob.setEntityInteraction(targ);
            }

            // Perform the abstract method "prepareAttack" before adding the hit for the target
            method.prepareAttack(mob, targ);

            /*if (target == null) {
                logger.error("Warning (not error)- target is null is middle of cb. wtf");
                StringBuilder sb = new StringBuilder();
                sb.append("combat state: ");
                sb.append(this);
                logger.error(sb.toString());
            }*/

            //Check for skulling context.
            if (mob.isPlayer() && targ.isPlayer()) { // Check if the player should be skulled for making this attack..
                Player player = mob.getAsPlayer();
                if (WildernessArea.inWilderness(player.tile())) {
                    Skulling.skull(player, targ, SkullType.WHITE_SKULL);
                }
            }

            // Flag the targ as under attack at this moment to factor in delayed combat styles.
            targ.putAttrib(AttributeKey.LAST_DAMAGER, mob);
            targ.putAttrib(AttributeKey.LAST_WAS_ATTACKED_TIME, System.currentTimeMillis());
            targ.getTimers().register(TimerKey.COMBAT_LOGOUT, 16);
            mob.putAttrib(AttributeKey.LAST_ATTACK_TIME, System.currentTimeMillis());
            mob.putAttrib(AttributeKey.LAST_TARGET, targ);
            mob.getTimers().register(TimerKey.COMBAT_LOGOUT, 16);

            final int attackSpeed = method.getAttackSpeed(mob);

            // Reset attack timer
            if (!graniteMaulSpecial) {
                //System.out.println("set timer");
                mob.getTimers().register(TimerKey.COMBAT_ATTACK, attackSpeed);
            }

            // combat is complete, clear the cast spell. this stops the spell from repeating.
            // do NOT clear spell before attackSpeed is set, otherwise it'll forget the magic.
            if (mob.isPlayer() && method == CombatFactory.MAGIC_COMBAT) {
                if (method instanceof CommonCombatMethod) {
                    CommonCombatMethod o = (CommonCombatMethod) method;
                    o.postAttack();
                }
            }
        }
    }

    static final List<Integer> gmauls = new ArrayList<>(List.of(ItemIdentifiers.GRANITE_MAUL, CustomItemIdentifiers.GRANITE_MAUL_24944, ItemIdentifiers.GRANITE_MAUL_12848, ItemIdentifiers.GRANITE_MAUL_24225, CustomItemIdentifiers.GRANITE_MAUL_24944));

    private boolean specialGraniteMaul() {
        var graniteMaulSpecials = mob.<Integer>getAttribOr(AttributeKey.GRANITE_MAUL_SPECIALS, 0);
        if (graniteMaulSpecials == 0)
            return false;

        if (mob.isPlayer()) {
            Player player = mob.getAsPlayer();
            boolean isGmaul = gmauls.stream().anyMatch(granite_maul -> player.getEquipment().hasAt(EquipSlot.WEAPON, granite_maul));
            if (!isGmaul)
                return false;
        }

        mob.putAttrib(AttributeKey.GRANITE_MAUL_SPECIALS, 0);

        if (graniteMaulSpecials > 2)
            graniteMaulSpecials = 2;

        for (int i = 0; i < graniteMaulSpecials; i++) {
            mob.getCombat().method.prepareAttack(mob, target);
        }

        // any gmaul spec pushes the next weapon attack to the next tick
        mob.getTimers().extendOrRegister(TimerKey.COMBAT_ATTACK, 1);
        return true;
    }

    public int magicSpellDelay(Mob target) {
        var tileDist = mob.tile().distance(target.tile());
        var delay = 1.5 + Math.floor(tileDist) / 2.0;

        if (castSpell != null && (castSpell.spellId() == 1 || castSpell.spellId() == 2 || castSpell.spellId() == 3)) // Tridents
            delay -= 1; // 1 less tick to make it faster

        if (castSpell != null && castSpell.name().equalsIgnoreCase("Crumble undead")) {
            delay = 0;
        }

        // 1: Always a minimum delay before impact.
        // maximum of 4 otherwise barrage will stack up with pidded melee hit and you cannot out-eat it.
        delay = Math.min(Math.max(1.0, delay), 5.0);

        return (int) delay;
    }

    /**
     * Resets combat for the {@link Mob}.
     */
    public void reset() {
        target = null;
        lastTarget = null;

        mob.clearAttrib(AttributeKey.TARGET);
        mob.getMovementQueue().resetFollowing();
        mob.setEntityInteraction(null);
        TargetRoute.reset(mob);
    }

    /**
     * Adds damage to the damage map, as long as the argued amount of damage is
     * above 0 and the argued entity is a player.
     *
     * @param entity the entity to add damage for.
     * @param amount the amount of damage to add for the argued entity.
     */
    public void addDamage(Mob entity, int amount) {

        if (amount <= 0 || isNonCombatNpc(this.mob)) { // damage on npcs not tracked! makes sense for non-cb npcs,
            // wil also be memory intensive unless we lazy-init (only create the new Map<> when actuall yneeded)
            //System.out.println("yeet this guy "+entity.getMobName()+" by "+amount);
            return;
        }
        //System.out.println(entity.getMobName()+" hit "+mob.getMobName()+" for "+amount);
        getDamageMap(); // make sure it exists

        if (damageMap.containsKey(entity)) {
            damageMap.get(entity).incrementDamage(amount);
            return;
        }

        damageMap.put(entity, new HitDamageCache(amount));
    }

    private boolean isNonCombatNpc(Mob entity) {
        if (!entity.isNpc()) return false;
        return entity.isNpc() && entity.getAsNpc().combatInfo() != null && entity.getAsNpc().combatInfo().unattackable;
    }

    /**
     * Performs a search on the <code>damageMap</code> to find which {@link Player}
     * dealt the most damage on this controller.
     *
     * @return the player who killed this entity, or <code>null</code> if an npc or
     * something else killed this entity.
     */
    public Optional<Player> getKiller() {

        // Return null if no players killed this entity.
        if (damageMap == null || damageMap.size() == 0) {
            return Optional.empty();
        }

        // The damage and killer placeholders.
        int damage = 0;
        Optional<Player> killer = Optional.empty();

        for (Entry<Mob, HitDamageCache> entry : damageMap.entrySet()) {

            // Check if this entry is valid.
            if (entry == null) {
                continue;
            }

            // Check if the cached time is valid.
            long timeout = entry.getValue().getStopwatch().elapsed();
            if (timeout > CombatConstants.DAMAGE_CACHE_TIMEOUT) {
                continue;
            }

            // Check if the key for this entry has logged out.
            if (entry.getKey().isPlayer()) {
                Player player = (Player) entry.getKey();
                if (!player.isRegistered()) {
                    continue;
                }

                // If their damage is above the placeholder value, they become the
                // new 'placeholder'.
                if (entry.getValue().getDamage() > damage) {
                    damage = entry.getValue().getDamage();
                    killer = Optional.of((Player) entry.getKey());
                }
            }
        }

        // Return the killer placeholder.
        return killer;
    }

    public Mob getTargetRef() {
        var ref = mob.<WeakReference<Mob>>getAttribOr(AttributeKey.TARGET, new WeakReference<Mob>(null));
        if (ref == null) return null;
        var target = ref.get();

        if (target != null)
            mob.face(target.tile());

        // If these conditions fail, we can't attack
        if (target != null && !target.dead() && !mob.dead() && !target.finished()) {
            return target;
        }

        return null;
    }

    public Mob refreshTarget() {
        var target = getTarget();
        var npc = mob.getAsNpc();

        // If these conditions fail, we can't attack
        if (target != null && !target.dead() && !npc.dead() && !npc.finished() && !target.finished()) {
            return target;
        }

        return null;
    }

    public boolean damageMapContains(Player player) {
        return damageMap.containsKey(player);
    }

    /**
     * Getters and setters
     **/

    public Mob getMob() {
        return mob;
    }

    /**
     * Return the player's combat target. This is not the bounty target.
     */
    public Mob getTarget() {
        return target;
    }

    /**
     * Set the player's combat target. This is not the bounty target.
     */
    public void setTarget(Mob target) {
        updateLastTarget(target);
        this.target = target;
        mob.putAttrib(AttributeKey.TARGET, new WeakReference<Mob>(target));
    }

    public Mob lastTarget;

    private int lastTargetTimeoutTicks;

    private void updateLastTarget(Mob target) {
        if (target == null) // dont cancel this field
            return;
        lastTarget = target;
        lastTargetTimeoutTicks = 10;
    }

    private void checkLastTarget() {
        //System.out.println("lastTargetTimeoutTicks "+lastTargetTimeoutTicks+" lastTargetTimeoutTicks "+lastTargetTimeoutTicks);
        if (lastTargetTimeoutTicks > 0 && --lastTargetTimeoutTicks == 0) {
            lastTarget = null;
        }
    }

    public HitQueue getHitQueue() {
        return hitQueue;
    }

    public CombatSpell getCastSpell() {
        return castSpell;
    }

    /**
     * the next spell to cast example from {@link com.ferox.net.packet.incoming_packets.MagicOnPlayerPacketListener}
     */
    public void setCastSpell(CombatSpell castSpell) {
        this.castSpell = castSpell;
    }

    public CombatSpell getAutoCastSpell() {
        return autoCastSpell;
    }

    public void setAutoCastSpell(CombatSpell autoCastSpell) {
        this.autoCastSpell = autoCastSpell;
    }

    public RangedWeapon getRangedWeapon() {
        return rangedWeapon;
    }

    public void setRangedWeapon(RangedWeapon rangedWeapon) {
        //System.out.printf("%s wep updated %s%n", mob, rangedWeapon);
        this.rangedWeapon = rangedWeapon;
    }

    public WeaponType getWeaponInterface() {
        return weapon;
    }

    public void setWeapon(WeaponType weapon) {
        this.weapon = weapon;
    }

    public FightType getFightType() {
        return fightType;
    }

    public void setFightType(FightType fightType) {
        this.fightType = fightType;
    }

    public boolean autoRetaliate() {
        return autoRetaliate;
    }

    public void setAutoRetaliate(boolean autoRetaliate) {
        this.autoRetaliate = autoRetaliate;
    }

    private void checkGraniteMaul() {
        var graniteMaulTimeoutTicks = mob.<Integer>getAttribOr(AttributeKey.GRANITE_MAUL_TIMEOUT_TICKS, 0);
        if (graniteMaulTimeoutTicks > 0) {
            mob.putAttrib(AttributeKey.GRANITE_MAUL_TIMEOUT_TICKS, graniteMaulTimeoutTicks - 1);
            if (mob.<Integer>getAttribOr(AttributeKey.GRANITE_MAUL_TIMEOUT_TICKS, 0) == 0) {
                mob.putAttrib(AttributeKey.GRANITE_MAUL_SPECIALS, 0);
            } else if (graniteMaulTimeoutTicks == 4)
                //1 tick less than 5 because it was subtracted
                autoAttackGraniteMaul();
        }
    }

    /**
     * when in range of 1x1 target, re-focus the previous target
     */
    private void autoAttackGraniteMaul() {
        // Define our target as last entity we attacked
        if (target != null || lastTarget == null)
            return;
        if (mob.getZ() != lastTarget.getZ())
            return;
        int x = mob.getAbsX();
        int y = mob.getAbsY();
        if (lastTarget.getSize() == 1) {
            int targetX = lastTarget.getAbsX();
            int targetY = lastTarget.getAbsY();
            int diffX = Math.abs(x - targetX);
            int diffY = Math.abs(y - targetY);
            if ((diffX + diffY) != 1)
                return;
        } else {
            Tile closestPos = RouteMisc.getClosestPosition(mob, lastTarget);
            int targetX = closestPos.getX();
            int targetY = closestPos.getY();
            int diffX = Math.abs(x - targetX);
            int diffY = Math.abs(y - targetY);
            if (diffX > 1 || diffY > 1)
                return;
        }
        mob.setEntityInteraction(lastTarget);
        setTarget(lastTarget);
    }

    /**
     * aka NPCCombat.follow0/follow in Runite
     */
    public void preAttack() {
        method = CombatFactory.getMethod(mob);
        checkLastTarget();
        checkGraniteMaul();

        // players use normal code -> TargetRoute.set
        if (target != null && (mob.isPlayer() || (mob.isNpc() && mob.getAsNpc().id() == ZOMBIFIED_SPAWN_8063))) {
            TargetRoute.set(mob, target, method.getAttackDistance(mob));
        }

        // npcs can have overridable logic
        if (target != null && mob.isNpc()) {
            // delegate into a method you can override for npcs for special cases
            if (method instanceof CommonCombatMethod) {
                CommonCombatMethod commonCombatMethod = (CommonCombatMethod) method;
                commonCombatMethod.set(mob, target);
                commonCombatMethod.doFollowLogic();
            } else {
                // the normal code for all mobs.
                DumbRoute.step(mob, target, method.getAttackDistance(mob));
            }
        }
    }

    @Override
    public String toString() {
        return "Combat{" +
            "damageMap=" + (damageMap == null ? "?" : damageMap.size()) +
            ", rangedWeapon=" + (rangedWeapon == null ? "None" : rangedWeapon) +
            ", hitQueue=" + (hitQueue == null ? "?" : hitQueue.size()) +
            ", mob=" + (mob == null ? "null" : mob) +
            ", target=" + (target == null ? "null" : target) +
            ", method=" + (method == null ? "null" : method) +
            ", fightType=" + (fightType == null ? "null" : fightType) +
            ", weapon=" + (weapon == null ? "?" : weapon.name()) +
            ", autoRetaliate=" + autoRetaliate +
            ", castSpell=" + (castSpell == null ? "none" : castSpell.name()) +
            ", weps: " + (mob != null && mob.isPlayer() ? ("wepid: " + mob.getAsPlayer().getEquipment().getId(EquipSlot.WEAPON) + " ammo=" +
            mob.getAsPlayer().getEquipment().getId(EquipSlot.AMMO)) : " useless:" + mob) +
            '}';
    }

    private final Stopwatch fightTimer = Stopwatch.createUnstarted();

    public Stopwatch getFightTimer() {
        return fightTimer;
    }

    public boolean inCombat() {
        return CombatFactory.inCombat(mob);
    }
}
