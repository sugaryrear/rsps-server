package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.GameServer;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.util.Utils;

/**
 * To use this command:
 * 3 lines displaying for 30 seconds:
 * ::msg Line 1%%Line 2%%Line 3&&30
 * 1 line displaying for 10 seconds:
 * ::msg Line 1&&10
 */
public class MsgCommand implements Command {
    @Override
    public void execute(Player player, String command, String[] parts) {
       if (GameServer.isUpdating())  {
           player.message("Cannot send message, the server is currently updating.");
           return;
       }
       String msg = "";
        for (int i = 1; i < parts.length; i++) {
            msg += parts[i] + " ";
        }
        if (msg.contains("&&")) {
            String[] splitTime = msg.split("&&");
            int time = Integer.parseInt(splitTime[1].trim());
            if (time < 10 || time > 120) {
                player.message("The duration for yellow messages must be between 10 and 120 seconds.");
                player.message("Use ::msg hello there&&15 for 15 seconds.");
                return;
            }
        } else {
            player.message("The duration for yellow messages must be between 10 and 120 seconds.");
            player.message("Use ::msg hello there&&15 for 15 seconds.");
            return;
        }
        //Message without Name:
        World.getWorld().sendWorldMessage("yellowAnnouncement##" + Utils.capitalizeFirst(msg));
        //Message with Name:
        //World.sendMessage("Yellow##" + player.getUsername() + ": " + Misc.capitalize(msg));
        World.getWorld().sendWorldMessage("<col=004f00>Broadcast:</col>" + Utils.capitalizeFirst(msg.replaceAll("\\P{L}", " ").replace("  ", ". ").replace(". .", ".")));
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
