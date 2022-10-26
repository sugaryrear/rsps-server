package com.ferox.game.content.mechanics;

import com.ferox.game.GameConstants;
import com.ferox.game.task.Task;
import com.ferox.game.task.TaskManager;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.Venom;
import com.ferox.game.world.entity.combat.hit.SplatType;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.InfectionType;
import com.ferox.game.world.entity.mob.player.Player;

/**
 * Created by Bart on 11/18/2015.
 */
public class Poison {

    private static int determineHit(int poisonticks) {
        return poisonticks / 5 + 1;
    }

    public static int ticksForDamage(int damage) {
        return damage * 5 - 4;
    }

    public static boolean poisoned(Mob e) {
        return (int) e.getAttribOr(AttributeKey.POISON_TICKS, 0) > 0;
    }

    public static void cure(Player player) {
        player.putAttrib(AttributeKey.POISON_TICKS,0);
        player.setInfection(InfectionType.HEALTHY);
        player.message("clearpoisons:-1");
    }

    public static void cureAndImmune(Player player, int immunityTicks) {
        player.setInfection(InfectionType.HEALTHY);
        player.putAttrib(AttributeKey.POISON_TICKS, -immunityTicks);
        player.message("clearpoisons:-1");

    }

    public static void onLogin(Player me) {
        setTimer(me);
        int ticks = me.getAttribOr(AttributeKey.POISON_TICKS, 0);
        if(ticks > 0) {
            me.setInfection(InfectionType.POISON_INFECTION);
        }
    }

    public static void setTimer(Mob mob) {
        if (mob.getAttribOr(AttributeKey.POISON_TASK_RUNNING, false))
            return;
        mob.putAttrib(AttributeKey.POISON_TASK_RUNNING, true);
        TaskManager.submit(new Task("PoisonTask", 30, false) {//Every 18 seconds

            @Override
            protected void execute() {
//                System.out.println("here");
                if(!mob.isRegistered() || mob.dead()) {
                    stop();
                    mob.clearAttrib(AttributeKey.POISON_TASK_RUNNING);
                    return;
                }

                if (Venom.venomed(mob))
                    return;

                if (mob.isPlayer()) {
                    Player player = (Player) mob;

                    // Grab value. More than 0 means we're poisoned for X ticks, lower than X means we're immune.
                    var poisonTicks = player.<Integer>getAttribOr(AttributeKey.POISON_TICKS, 0);
                    Mob poisonedBy = player; // TODO add poisonedBy attrib, default to self player
                //    player.message(poisonTicks+" poison ticks");
                    if (poisonTicks > 0) {
                        player.hit(poisonedBy, Math.min(20, determineHit(poisonTicks)), SplatType.POISON_HITSPLAT);
                        //player.hit(new PoisonOrigin(), Math.min(20, determineHit(poisonTicks)), Hitsplat.POISON_HITSPLAT);
                        player.putAttrib(AttributeKey.POISON_TICKS, poisonTicks - 1); // reduce as normal
                    } else if (poisonTicks < 0) {
                        player.putAttrib(AttributeKey.POISON_TICKS, poisonTicks + 1); // increment it back to 0 from negative (from being immune)
                    } else {
                        if(GameConstants.production)
                        player.message("clearpoisons:-1");
                    }
                } else {
                    Npc npc = (Npc) mob;
                    // Grab value. More than 0 means we're poisoned for X ticks, lower than X means we're immune.
                    var poisonTicks = npc.<Integer>getAttribOr(AttributeKey.POISON_TICKS, 0);

                    if (poisonTicks > 0) {
                        npc.hit(null, Math.min(20, determineHit(poisonTicks)), SplatType.POISON_HITSPLAT);
                        //npc.hit(npc, Math.min(20, determineHit(poisonTicks)), Hitsplat.POISON_HITSPLAT);
                        npc.putAttrib(AttributeKey.POISON_TICKS, poisonTicks - 1);
                    } else if (poisonTicks < 0) {
                        npc.putAttrib(AttributeKey.POISON_TICKS, poisonTicks + 1);
                    }
                }
            }
        });
    }
}
