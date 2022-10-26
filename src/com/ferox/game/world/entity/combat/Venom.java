package com.ferox.game.world.entity.combat;

import com.ferox.game.task.Task;
import com.ferox.game.task.TaskManager;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.hit.SplatType;
import com.ferox.game.world.entity.mob.Flag;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.InfectionType;
import com.ferox.game.world.entity.mob.player.Player;

import static com.ferox.game.world.entity.AttributeKey.VENOMED_BY;
import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;

/**
 * Created by Jak on 16/08/2016.
 *
 * Starting cycles is 8.
 * 8 'cycles' which execute every 34 game ticks (20.4 seconds)
 * Cycles go down -- per 34 ticks to 0 then continue forever.
 * Each cycle +2 more damage is dealt:
 * 8=6
 * 7=8
 * 6=10
 * 5=12
 * 4=14
 * 3=16
 * 2=18
 * 1=20 .. then on 1 it never reduces to 0, unless you die/take antivenom potion
 */
public class Venom {

    public static void onLogin(Player me) {
        setTimer(me);
        int ticks = me.getAttribOr(AttributeKey.VENOM_TICKS, 0);
        if(ticks > 0) {
            me.setInfection(InfectionType.VENOM_INFECTION);
        }
    }

    public static void setTimer(Mob mob) {
        if (mob.getAttribOr(AttributeKey.VENOM_TASK_RUNNING, false))
            return;
        mob.putAttrib(AttributeKey.VENOM_TASK_RUNNING, true);
        TaskManager.submit(new Task("VenomTask", 34, false) {//Every 20 seconds

            @Override
            protected void execute() {
                int ticks = mob.getAttribOr(AttributeKey.VENOM_TICKS, 0);

                if(!mob.isRegistered() || mob.dead()) {
                    stop();
                    mob.clearAttrib(AttributeKey.VENOM_TASK_RUNNING);
                    return;
                }

                if (ticks > 0) {
                    //I think I got it perhaps I need to set a extra param in this method to reference attacker
                    mob.putAttrib(AttributeKey.VENOM_TICKS, Math.max(1, ticks - 1));
                    Mob attacker = mob.getAttribOr(AttributeKey.VENOMED_BY,null);
                    if(attacker != null) {
                        mob.hit(attacker, calcHit(ticks), SplatType.VENOM_HITSPLAT);
                    }
                    //System.out.println("current tick: "+ticks);
                } else if (ticks < 0) {
                    // Negative value of ticks means we're immune. Increase up until 0 where the venom ends.
                    mob.putAttrib(AttributeKey.VENOM_TICKS, ticks + 1);
                }
            }
        });
    }

    private static int calcHit(int ticks) {
        return Math.max(6, Math.min(20, 6 + ((8 - ticks) * 2)));
    }

    public static void cure(int type, Mob e) {
        cure(type, e, true);
    }

    public static void cure(int type, Mob e, boolean msg) {
        int venomVal = e.getAttribOr(AttributeKey.VENOM_TICKS, 0);
        Player player = (Player) e;
        if (type == 1) { // normal poison cure.
            if (venomVal > 0 && msg) {
                player.message("<col=145A32>The potion cures the venom, however you are still poisoned.");
            }
            player.putAttrib(AttributeKey.VENOM_TICKS, -1);
            player.setInfection(InfectionType.HEALTHY);// Reset and then send poison after
            player.getUpdateFlag().flag(Flag.APPEARANCE);
            player.poison(6, msg);
        } else if (type == 2) { // totally removes it, no poison
            if (venomVal > 0 && msg) {
                player.message("<col=145A32>The potion fully cures the venom.");
            }
            player.putAttrib(AttributeKey.VENOM_TICKS, 0);
            player.setInfection(InfectionType.HEALTHY);// Healthy
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        } else if (type == 3) { // totally removes, plus immunity
            if (venomVal > 0 && msg) {
                player.message("<col=145A32>The potion cures the venom and provides you with 3 minutes of immunity.");
            } else if (msg) {
                player.message("<col=145A32>It grants you 3 minutes of immunity to venom.");
            }
            player.putAttrib(AttributeKey.VENOM_TICKS, -9); // 3 minutes, aka 9 venom cycles of immunity
            player.setInfection(InfectionType.HEALTHY);// Healthy
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }
        //Regardless of what venom cure you take the venom attacker attribute is reset regardless.
        player.clearAttrib(VENOMED_BY);
        player.message("clearpoisons:-1");
    }

    public static boolean venomed(Mob e) {
        return (int) e.getAttribOr(AttributeKey.VENOM_TICKS, 0) > 0;
    }

    /**
     * Will reduce the charges/scales/darts in toxic items and convert to uncharged when it runs out, with a notification.
     * Does not account for if target has a Serp helm or is a Npc Boss. Entity#venom checks that.
     *
     * @return True if venom can be applied to the victim.
     */
    public static boolean attempt(Mob source, Mob target, CombatType type, boolean success) {
        // Npcs don't use this method.
        if (source.isNpc())
            return false;

        var wep = ((Player)source).getEquipment().get(EquipSlot.WEAPON);
        if(wep == null) return false;

        // Only venom weps for venom. Blowpipe charged, toxic trident used, toxic staff of the dead charged.
        if (wep.getId() != TOXIC_BLOWPIPE && wep.getId() != MAGMA_BLOWPIPE && wep.getId() != HWEEN_BLOWPIPE && wep.getId() != TRIDENT_OF_THE_SWAMP && wep.getId() != TOXIC_STAFF_OF_THE_DEAD && wep.getId() != TOXIC_STAFF_OF_THE_DEAD_C) {
            return false;
        }

        return success && World.getWorld().rollDie(4, 1);
    }
}
