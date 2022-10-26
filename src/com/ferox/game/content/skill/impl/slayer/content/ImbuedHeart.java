package com.ferox.game.content.skill.impl.slayer.content;

import com.ferox.game.content.EffectTimer;
import com.ferox.game.content.duel.DuelRule;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.util.Color;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.timers.TimerKey;

public class ImbuedHeart {

    public static void activate(Player player) {
        if(!player.inventory().contains(ItemIdentifiers.IMBUED_HEART)) {
            return;
        }

        if (player.getTimers().has(TimerKey.IMBUED_HEART_COOLDOWN)) {
            int ticks = player.getTimers().left(TimerKey.IMBUED_HEART_COOLDOWN);

            if (ticks >= 100) {
                int minutes = ticks / 100;
                player.message("The heart is still drained of its power. Judging by how it feels, it will be ready");
                player.message("in around "+minutes+" minutes.");
            } else {
                int seconds = ticks / 10 * 6;
                player.message("The heart is still drained of its power. Judging by how it feels, it will be ready");
                player.message("in around "+seconds+" seconds.");
            }
        } else {
            if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_POTIONS.ordinal()]) {
                player.message("Stat-boosting items are disabled for this duel.");
            } else {
                player.message("<col="+Color.RED.getColorValue()+">Your imbued heart has regained its magical power.");
                player.graphic(1316);
                player.getTimers().register(TimerKey.IMBUED_HEART_COOLDOWN, 700);
                int seconds = 700 / 10 * 6;//7 minutes
                player.getPacketSender().sendEffectTimer(seconds, EffectTimer.IMBUED_HEART);

                // This boost will only increment.
                int boost = 1 + (player.skills().xpLevel(Skills.MAGIC) / 10);
                if (player.skills().levels()[Skills.MAGIC] == player.skills().xpLevel(Skills.MAGIC)) {
                    player.skills().alterSkill(Skills.MAGIC, boost);
                }
            }
        }
    }
}
