package com.ferox.game.content.areas.edgevile;

import com.ferox.game.content.areas.edgevile.dialogue.WildernessStatBoardDialogue;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Utils;

/**
 * @author Patrick van Elderen | December, 09, 2020, 14:11
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class WildernessStatisticsBoard extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if(option == 1) {
            if(object.getId() == 267565) {
                int kills = player.getAttribOr(AttributeKey.PLAYER_KILLS, 0);
                int deaths = player.getAttribOr(AttributeKey.BOT_KILLS, 0);
                int targetKills = player.getAttribOr(AttributeKey.TARGET_KILLS, 0);
                player.message("You have " + targetKills + " target " + Utils.pluralOrNot("kill", kills) + ", " + kills + " player " + Utils.pluralOrNot("kill", kills) + ", and " + deaths + " " + Utils.pluralOrNot("death", deaths) + ". Your KD ratio is " + player.getKillDeathRatio() + ".");
                int killsteak = player.getAttribOr(AttributeKey.KILLSTREAK, 0);
                int record = player.getAttribOr(AttributeKey.KILLSTREAK_RECORD, 0);
                if (killsteak > 0) {
                    player.message("You are currently on a " + killsteak + " and your highest kill streak is " + record + ".");
                } else {
                    player.message("You are not currently on a kill streak and your highest kill streak is " + record + ".");
                }
                return true;
            }
        } else if(option == 2) {
            if(object.getId() == 26756) {
                player.getDialogueManager().start(new WildernessStatBoardDialogue());
                return true;
            }
        }
        return false;
    }
}
