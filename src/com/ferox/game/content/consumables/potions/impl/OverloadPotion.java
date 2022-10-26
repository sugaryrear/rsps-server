package com.ferox.game.content.consumables.potions.impl;

import com.ferox.game.content.EffectTimer;
import com.ferox.game.task.Task;
import com.ferox.game.task.TaskManager;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.util.Color;

/**
 * @author Patrick van Elderen | May, 24, 2021, 15:38
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class OverloadPotion {

    public static void apply(Player player) {
        player.putAttrib(AttributeKey.OVERLOAD_TASK_RUNNING, true);
        TaskManager.submit(new Task("OverloadTask", 1, false) {

            @Override
            protected void execute() {
                int ticks = player.getAttribOr(AttributeKey.OVERLOAD_POTION, 0);

                if (!player.isRegistered() || player.dead() || ticks == 0 || WildernessArea.inWilderness(player.tile())) {
                    stop();
                    player.clearAttrib(AttributeKey.OVERLOAD_TASK_RUNNING);
                    return;
                }

                if (ticks > 0) {
                    ticks -= 1;

                    //Every 15 seconds apply effect
                    if(ticks % 25 == 0) {
                        player.skills().overloadPlusBoost(Skills.ATTACK);
                        player.skills().overloadPlusBoost(Skills.STRENGTH);
                        player.skills().overloadPlusBoost(Skills.DEFENCE);
                        player.skills().overloadPlusBoost(Skills.RANGED);
                        player.skills().overloadPlusBoost(Skills.MAGIC);
                    }

                    if (ticks == 0) {
                        player.message(Color.RED.tag() + "Your overload potion has expired.");
                        player.putAttrib(AttributeKey.OVERLOAD_POTION, 0);
                        player.clearAttrib(AttributeKey.OVERLOAD_TASK_RUNNING);
                        player.getPacketSender().sendEffectTimer(0, EffectTimer.OVERLOAD);
                        player.hp(Math.max(player.skills().level(Skills.HITPOINTS), 50), 20);
                        player.skills().replenishStats();
                        player.healPlayer();
                        stop();
                    } else {
                        player.putAttrib(AttributeKey.OVERLOAD_POTION, ticks);
                        if (ticks == 3) {
                            player.message(Color.RED.tag() + "Your overload potion is about to expire.");
                        }
                    }
                }
            }
        });
    }
}
