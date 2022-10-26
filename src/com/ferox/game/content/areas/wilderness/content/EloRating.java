package com.ferox.game.content.areas.wilderness.content;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.QuestTab;
import com.ferox.util.Utils;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 12, 2020
 */
public class EloRating {

    public static int DEFAULT_ELO_RATING = 1300;
    public static int MAX_ELO_RATING = 4000;

    public static void modify(Player player, Player target) {

        int currentEloRating = player.getAttribOr(AttributeKey.ELO_RATING, DEFAULT_ELO_RATING);
        int targetEloRating = target.getAttribOr(AttributeKey.ELO_RATING, DEFAULT_ELO_RATING);
        int gain;
        if(target.skills().combatLevel() < player.skills().combatLevel() || targetEloRating < currentEloRating) {
            gain = Utils.random(10, 15);
        } else  {
            gain = Utils.random(20, 25);
        }

        if (currentEloRating + gain > MAX_ELO_RATING) {
            currentEloRating = MAX_ELO_RATING;
        } else {
            currentEloRating += gain;
        }

        if (targetEloRating - gain < 0) {
            targetEloRating = 0;
        } else {
            targetEloRating -= gain;
        }

        player.putAttrib(AttributeKey.ELO_RATING, currentEloRating);
        target.putAttrib(AttributeKey.ELO_RATING, targetEloRating);
        player.getPacketSender().sendString(QuestTab.InfoTab.ELO_RATING.childId, QuestTab.InfoTab.INFO_TAB.get(QuestTab.InfoTab.ELO_RATING.childId).fetchLineData(player));
        target.getPacketSender().sendString(QuestTab.InfoTab.ELO_RATING.childId, QuestTab.InfoTab.INFO_TAB.get(QuestTab.InfoTab.ELO_RATING.childId).fetchLineData(player));
        player.message("<col=ca0d0d>You have gained +"+gain+" to your elo rating.");
        target.message("<col=ca0d0d>You have lost -"+gain+" to your elo rating.");
    }
}
