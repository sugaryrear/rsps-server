package com.ferox.game.content.areas.lumbridge.dialogue;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.player.Player;

public class Playtime extends Dialogue {

    @Override
    protected void start(Object... parameters) {
        send(DialogueType.STATEMENT, "Your current play time is: " + getTime());
        setPhase(0);
    }

    @Override
    protected void next() {
        if (isPhase(0)) {
            stop();
        }
    }

    private String getTime() {
        int gameTime = player.getAttribOr(AttributeKey.GAME_TIME, 0);
        int time = (int) (gameTime * 0.6);
        int days = (time / 86400);
        int hours = (time / 3600) - (days * 24);
        int minutes = (time / 60) - (days * 1440) - (hours * 60);
        String minute = minutes > 1 ? "Minutes" : "Minute";
        String hour = hours > 1 ? "Hours" : "Hour";
        String day = days > 1 ? "Days" : "Day";
        return "" + days + " " + day + " " + hours + " " + hour + " " + minutes + " " + minute + ".";
    }

    public static String getTimeDHS(Player player) {
        int gameTime = player.getAttribOr(AttributeKey.GAME_TIME, 0);
        int time = (int) (gameTime * 0.6);
        int days = (time / 86400);
        int hours = (time / 3600) - (days * 24);
        int minutes = (time / 60) - (days * 1440) - (hours * 60);
        String m = "";
        if (days > 0)
            m += "" + days + "d";
        if (hours > 0)
            m += "" + hours + "h";
        if (days < 0 && hours < 0)
            m += "" + minutes + "m";
        return m;
    }
}
