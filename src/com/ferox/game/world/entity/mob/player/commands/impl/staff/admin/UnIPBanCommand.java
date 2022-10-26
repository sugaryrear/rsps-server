package com.ferox.game.world.entity.mob.player.commands.impl.staff.admin;

import com.ferox.game.GameEngine;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.util.PlayerPunishment;
import com.ferox.util.Utils;

import java.io.IOException;

/**
 * @author Patrick van Elderen | January, 16, 2021, 11:09
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class UnIPBanCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (command.length() <= 8)
            return;
        String IPToRemove = Utils.formatText(command.substring(8)); // after "unipban "

        if (IPToRemove.isEmpty()) {
            player.message("You must enter a valid username.");
            return;
        }

        GameEngine.getInstance().submitLowPriority(() -> {
            try {
                GameEngine.getInstance().addSyncTask(() -> {
                    if (!PlayerPunishment.ipBanned(IPToRemove)) {
                        player.message("This IP address is not listed as IP banned");
                        return;
                    }

                    try {
                        PlayerPunishment.removeIpBan(IPToRemove);
                        PlayerPunishment.removeFromIPBanList(IPToRemove);
                        player.message("Player " + IPToRemove + " was successfully un IP banned.");
                        Utils.sendDiscordInfoLog("Player " + IPToRemove + " was successfully un IP banned. was unbanned by " + player.getUsername(), "sanctions");
                    } catch (IOException e) {
                        player.message("The IP could not be successfully removed from the file.");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isAdminOrGreater(player));
    }

}
