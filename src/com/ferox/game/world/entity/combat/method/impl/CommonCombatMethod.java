package com.ferox.game.world.entity.combat.method.impl;

import com.ferox.GameServer;
import com.ferox.game.content.duel.DuelRule;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.magic.CombatSpell;
import com.ferox.game.world.entity.combat.method.CombatMethod;
import com.ferox.game.world.entity.dialogue.DialogueManager;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.game.world.route.routes.DumbRoute;
import com.ferox.util.Debugs;

import static com.ferox.game.world.entity.combat.method.impl.npcs.bosses.CorporealBeast.CORPOREAL_BEAST_AREA;
import static com.ferox.util.CustomItemIdentifiers.ELDER_WAND;
import static com.ferox.util.NpcIdentifiers.*;

/**
 * reduce code replication for the 90+ npc classes
 *
 * @author Jak Shadowrs tardisfan121@gmail.com
 */
public abstract class CommonCombatMethod implements CombatMethod {

    public Mob mob, target;

    public void set(Mob mob, Mob target) {
        this.mob = mob;
        this.target = target;
    }

    /**
     * npc only
     */
    public void doFollowLogic() {
        DumbRoute.step(mob, target, getAttackDistance(mob));
    }

    /**
     * npc only
     */
    public boolean inAttackRange() {
        boolean instance = mob.tile().getZ() > 4;
        // just a hard limit. might need to replace/override with special cases
        //System.out.println(mob.tile().distance(target.tile()));
        if(mob.isNpc() && mob.getAsNpc().id() == CORPOREAL_BEAST) {
            if(!target.tile().inArea(CORPOREAL_BEAST_AREA)) {
                mob.getCombat().reset();//Target out of distance reset combat
            }
        }
        if (mob.tile().distance(target.tile()) >= 16 && !instance) {
            mob.getCombat().reset();//Target out of distance reset combat
            return false;
        }
        return DumbRoute.withinDistance(mob, target, getAttackDistance(mob));
    }

    /**
     * npc only
     */
    public void onDeath() {

    }

    /**
     * player only
     */
    public void postAttack() {

    }

    public boolean canAttackStyle(Mob entity, Mob other, CombatType type) {

        if (entity.isPlayer()) {
            Player player = (Player) entity;
            boolean magicOnly = player.getAttribOr(AttributeKey.MAGEBANK_MAGIC_ONLY, false);
            CombatSpell spell = player.getCombat().getCastSpell() != null ? player.getCombat().getCastSpell() : player.getCombat().getAutoCastSpell();

            // If you're in the mage arena, where it is magic only.
            if (type != CombatType.MAGIC && magicOnly) {
                player.message("You can only use magic inside the arena!");
                player.getCombat().reset();
                return false;
            }

            if (type == CombatType.MAGIC) {
                if (spell != null && !spell.canCast(player, other, false)) {
                    player.getCombat().reset();//We can't cast this spell reset combat
                    player.getCombat().setCastSpell(null);
                    Debugs.CMB.debug(entity, "spell !cancast.", other, true);
                    return false;
                }
                if (other.isNpc()) {
                    int id = other.getAsNpc().id();

                    //ranged and melee vanguards can't be attacked with melee
                    if( id == VANGUARD_7527){
                        entity.message("This vanguard can only be attacked using melee!");
                        entity.getCombat().reset();
                        return false;
                    }
                    if( id == VANGUARD_7528){
                        entity.message("This vanguard can only be attacked using ranged!");
                        entity.getCombat().reset();
                        return false;
                    }
                }
                // Duel, disabled magic?
                if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_MAGIC.ordinal()]) {
                    DialogueManager.sendStatement(player, "Magic has been disabled in this duel!");
                    player.getCombat().reset();
                    Debugs.CMB.debug(entity, "no magic in duel.", other, true);
                    return false;
                }
            } else if (type == CombatType.RANGED) {
                // Duel, disabled ranged?
                if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_RANGED.ordinal()]) {
                    DialogueManager.sendStatement(player, "Ranged has been disabled in this duel!");
                    player.getCombat().reset();//Ranged attacks disabled, stop combat
                    Debugs.CMB.debug(entity, "no range in duel.", other, true);
                    return false;
                }
                if (other.isNpc()) {
                    int id = other.getAsNpc().id();

                    //ranged and melee vanguards can't be attacked with melee
                    if( id == VANGUARD_7527){
                        entity.message("This vanguard can only be attacked using melee!");
                        entity.getCombat().reset();
                        return false;
                    }
                    if( id == VANGUARD_7529){
                        entity.message("This vanguard can only be attacked using magic!");
                        entity.getCombat().reset();
                        return false;
                    }
                }
                // Check that we have the ammo required
                if (!CombatFactory.checkAmmo(player)) {
                    Debugs.CMB.debug(entity, "no ammo", other, true);
                    player.getCombat().reset();//Out of ammo, stop combat
                    return false;
                }
            } else if (type == CombatType.MELEE) {
                if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_MELEE.ordinal()]) {
                    DialogueManager.sendStatement(player, "Melee has been disabled in this duel!");
                    player.getCombat().reset();//Melee attacks disabled, stop combat
                    Debugs.CMB.debug(entity, "no melee in duel.", other, true);
                    return false;
                }
                //Att acking Aviansie with melee.
                if (other.isNpc()) {
                    int id = other.getAsNpc().id();

                    //ranged and melee vanguards can't be attacked with melee
                    if( id == VANGUARD_7528){
                        entity.message("This vanguard can only be attacked using ranged!");
                        entity.getCombat().reset();
                        return false;
                    }
                    if( id == VANGUARD_7529){
                        entity.message("This vanguard can only be attacked using magic!");
                        entity.getCombat().reset();
                        return false;
                    }
                    if (id == 3166 || id == 3167 || id == 3168 || id == 3169 || id == 3170 || id == 3171
                        || id == 3172 || id == 3173 || id == 3174 || id == 3175 || id == 3176 || id == 3177
                        || id == 3178 || id == 3179 || id == 3180 || id == 3181 || id == 3182 || id == 3183) {
                        entity.message("The Aviansie is flying too high for you to attack using melee.");
                        entity.getCombat().reset();//Aviansie out of range, stop combat
                        return false;
                    } else if (id >= 3162 && id <= 3165 || id == 15016) {
                        entity.message("It's flying too high for you to attack using melee.");
                        entity.getCombat().reset();//Monster out of range, stop combat
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public CombatType styleOf() {
        if (!mob.isPlayer()) // this mtd is players only
            return null;
        if (this instanceof MagicCombatMethod)
            return CombatType.MAGIC;
        if (this instanceof RangedCombatMethod)
            return CombatType.RANGED;
        if (this instanceof MeleeCombatMethod)
            return CombatType.MELEE;
        if (this.getClass().getPackageName().contains("magic"))
            return CombatType.MAGIC;
        if (this.getClass().getPackageName().contains("melee"))
            return CombatType.MELEE;
        if (this.getClass().getPackageName().contains("range"))
            return CombatType.RANGED;
        if (mob.getAsPlayer().getEquipment().hasAt(EquipSlot.WEAPON, ELDER_WAND))
            return CombatType.MAGIC;
        System.err.println("unknown player styleOf combat script: " + this + " wep " + mob.getAsPlayer().getEquipment().getId(3));
        return null;
    }
}
