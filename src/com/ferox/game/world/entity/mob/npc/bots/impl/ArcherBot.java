package com.ferox.game.world.entity.mob.npc.bots.impl;

import com.ferox.game.content.consumables.FoodConsumable;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
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
 * Represents a simple archer bot.
 * @author Professor Oak
 */
public class ArcherBot extends NPCBotHandler implements CombatMethod {

    private boolean ramboMode;
    private int ramboShots;
    
    public ArcherBot(Npc npc) {
        super(npc);
        npc.putAttrib(AttributeKey.VENGEANCE_ACTIVE, true);
        npc.getCombat().setRangedWeapon(RangedData.RangedWeapon.BALLISTA);
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

                if (npc.hp() < 40 + Utils.getRandom(15)) {

                    if (getEatCounter() < 28) {
                        super.eat(FoodConsumable.Food.SHARK, 1100);
                    }
                }

                //Cast vengeance when ever we can.
                super.castVengeance();

                //Sometimes go nuts
                if (Utils.getRandom(20) == 1) {
                    ramboMode = true;
                }
                if (ramboMode) {

                    npc.forceChat("Raaaaaarrrrgggghhhhhh!");

                    if (ramboShots++ >= 1) {
                        ramboShots = 0;
                        ramboMode = false;
                    }
                }

            } else {
                npc.forceChat("Gg");
            }

        } else {

            //Turn off prayers
            DefaultPrayers.closeAllPrayers(npc);

            //Reset all attributes
            super.reset();
        }
    }

    @Override
    public void onDeath(Player killer) {
        int botKills = killer.getAttribOr(AttributeKey.BOT_KILLS, 0);
        botKills++;
        killer.putAttrib(AttributeKey.BOT_KILLS, botKills);
        GroundItemHandler.createGroundItem(new GroundItem(new Item(13307, World.getWorld().random(25)), killer.tile(),killer));
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

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 7;
    }
}
