package com.ferox.game.world.entity.mob.player.commands.impl.staff.admin;

import com.ferox.game.GameEngine;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.entity.mob.player.save.PlayerSave;
import com.ferox.util.PlayerPunishment;
import com.ferox.util.Utils;

/**
 * @author Patrick van Elderen | January, 16, 2021, 11:13
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class UnMacBanCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (command.length() <= 9)
            return;
        String username = Utils.formatText(command.substring(9)); // after "unmacban "

        if (username.isEmpty()) {
            player.message("You must enter a valid username.");
            return;
        }

        Player offlinePlayer = new Player();
        offlinePlayer.setUsername(Utils.formatText(username.substring(0, 1).toUpperCase() + username.substring(1)));

        GameEngine.getInstance().submitLowPriority(() -> {
            try {
                if (PlayerSave.loadOfflineWithoutPassword(offlinePlayer)) {
                    GameEngine.getInstance().addSyncTask(() -> {
                        if (!PlayerSave.playerExists(offlinePlayer.getUsername())) {
                            player.message("No such player profile..");
                            return;
                        }

                        String MACToRemove = offlinePlayer.getAttribOr(AttributeKey.MAC_ADDRESS, "invalid");

                        if (!PlayerPunishment.macBanned(MACToRemove)) {
                            player.message("This MAC address is not listed as MAC banned");
                            return;
                        }

                        PlayerPunishment.removeMacBan(MACToRemove);
                        if (PlayerPunishment.banned(offlinePlayer.getUsername())) {
                            PlayerPunishment.unban(offlinePlayer.getUsername());
                        }
                        player.message("Player " + offlinePlayer.getUsername() + " was successfully un MAC banned.");
                        Utils.sendDiscordInfoLog("Player " + offlinePlayer.getUsername() + " was successfully un MAC banned. was unbanned by " + player.getUsername(), "sanctions");
                    });
                } else {
                    player.message("Something went wrong trying to unmacban " + offlinePlayer.getUsername());
                }
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
