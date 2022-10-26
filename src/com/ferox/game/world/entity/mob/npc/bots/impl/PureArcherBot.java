package com.ferox.game.world.entity.mob.npc.bots.impl;

import com.ferox.game.content.consumables.FoodConsumable;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatSpecial;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.method.CombatMethod;
import com.ferox.game.world.entity.combat.prayer.default_prayer.DefaultPrayers;
import com.ferox.game.world.entity.combat.ranged.RangedData;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.npc.bots.NPCBotHandler;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.ground.GroundItem;
import com.ferox.game.world.items.ground.GroundItemHandler;
import com.ferox.util.Utils;

/**
 * @author Patrick van Elderen | June, 17, 2021, 17:13
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class PureArcherBot extends NPCBotHandler implements CombatMethod {
    
    /**
     * The default npc.
     */
    private static final int DEFAULT_BOT_ID = NPCBotHandler.PURE_ARCHER_BOT_ID;
    /**
     * The npc which has
     * a special attack weapon equipped.
     */
    private static final int SPEC_BOT_ID = NPCBotHandler.PURE_ARCHER_BOT_ID + 1;

    public PureArcherBot(Npc npc) {
        super(npc);
        npc.clearAttrib(AttributeKey.VENGEANCE_ACTIVE);
        npc.getCombat().setRangedWeapon(RangedData.RangedWeapon.MAGIC_SHORTBOW);
        npc.combatInfo().projectile = 15;
    }

    @Override
    public void process() {

        //Check if npc is in combat..
        if (CombatFactory.inCombat(npc)) {

            //Make sure our opponent is valid..
            Player opponent = getOpponent();
            if (opponent == null) {
                return;
            }
            //If the opponent hasn't teleported away (12 is probably a safe distance to consider teled out), reset combat... This can take a few ticks (I believe up to 9). Might need this reset for non-bot NPCs too although probably not.
            if (npc.tile().distance(opponent.tile()) >= 12) {
                //npc.forceChat("Nice tele.");
                //System.out.println("Nice tele.");
                npc.setEntityInteraction(null);
                npc.getCombat().reset();
                npc.getMovementQueue().clear();
                return;
            }
            //Are we in distance to the opponent?
            final boolean inDistance = (npc.tile().distance(opponent.tile()) <= getMethod().getAttackDistance(npc));

            //Activate prayers..
            DefaultPrayers.activatePrayer(npc, DefaultPrayers.EAGLE_EYE);
            DefaultPrayers.activatePrayer(npc, DefaultPrayers.STEEL_SKIN);

            //Activate any overheads..
            int overhead = getOverheadPrayer(opponent, inDistance);
            if (overhead != -1) {

                //Activate overhead!
                DefaultPrayers.activatePrayer(npc, overhead);

            } else {

                //We shouldn't be using any overhead.
                //Make sure to turn off any headicons.
                if (npc.getPKBotHeadIcon() != -1) {
                    npc.setPKBotHeadIcon(-1);
                }
                resetOverheadPrayers(npc);
            }

            //Eat whenever we need to.
            if (npc.hp() > 0) {

                if (npc.hp() < 30 + Utils.getRandom(10)) {

                    if (getEatCounter() < 28) {
                        super.eat(FoodConsumable.Food.SHARK, 1100);
                    }
                }

                //Sometimes we spec
                //if (inDistance && npc.getSpecialPercentage() > CombatSpecial.DRAGON_DAGGER.getDrainAmount() && opponent.getHitpoints() <= 45) {
                //Since we switch to Ballista, it doesn't actually use spec.
                if (inDistance && opponent.hp() <= 45) {
                    if (Utils.getRandom(10) == 1) {
                        npc.setSpecialActivated(true);
                    }
                }
                //Randomly turn it off..
                if (!inDistance || Utils.getRandom(8) == 1) {
                    //System.out.println("Deactivated spec");
                    npc.setSpecialActivated(false);
                }
                //Update npc depending on the special attack state
                if (!npc.isSpecialActivated()) {
                    //System.out.println("We are not spec.");
                    transform(DEFAULT_BOT_ID);
                    npc.combatInfo().maxhit = 21;
                    //Lower attack speed is faster.
                    npc.combatInfo().attackspeed = 3;
                    npc.getCombat().setRangedWeapon(RangedData.RangedWeapon.MAGIC_SHORTBOW);
                    npc.combatInfo().projectile = 15;
                } else {
                    npc.combatInfo().maxhit = 45;
                    //Lower attack speed is faster.
                    npc.combatInfo().attackspeed = 5;
                    npc.getCombat().setRangedWeapon(RangedData.RangedWeapon.BALLISTA);
                    npc.combatInfo().projectile = 1301;
                    //System.out.println("We are spec.");
                    npc.setSpecialAttackPercentage(npc.getSpecialAttackPercentage() - CombatSpecial.DRAGON_DAGGER.getDrainAmount()); //use dds spec amount (25%) for ballista spec
                    transform(SPEC_BOT_ID);
                }

            } else {
                npc.forceChat("Ggwp");
            }

        } else {

            //Turn off prayers
            DefaultPrayers.closeAllPrayers(npc);

            //Reset weapon
            transform(DEFAULT_BOT_ID);
            npc.getCombat().setRangedWeapon(RangedData.RangedWeapon.MAGIC_SHORTBOW);
            //Reset all attributes
            super.reset();
        }
    }

    @Override
    public void onDeath(Player killer) {
        int botKills = killer.getAttribOr(AttributeKey.BOT_KILLS, 0);
        botKills++;
        killer.putAttrib(AttributeKey.BOT_KILLS, botKills);
        GroundItemHandler.createGroundItem(new GroundItem(new Item(13307, World.getWorld().random(25)), npc.tile(),killer));
        GroundItemHandler.createGroundItem(new GroundItem(new Item(526,1),npc.tile(),killer));    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 6;
    }

    @Override
    public CombatMethod getMethod() {
        return CombatFactory.RANGED_COMBAT;
    }

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED),1, CombatType.RANGED).checkAccuracy();
        hit.submit();
    }
}
