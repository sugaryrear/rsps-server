package com.ferox.game.world.entity.mob.player.commands.impl.staff.admin;

import com.ferox.GameServer;
import com.ferox.db.transactions.UnbanPlayerDatabaseTransaction;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.entity.mob.player.save.PlayerSave;
import com.ferox.util.PlayerPunishment;
import com.ferox.util.Utils;

public class UnBanPlayerCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (command.length() <= 6)
            return;
        String username = Utils.formatText(command.substring(6)); // after "unban "

        if (GameServer.properties().enableSql && GameServer.properties().punishmentsToDatabase) {
            GameServer.getDatabaseService().submit(new UnbanPlayerDatabaseTransaction(username));
        }

        if(GameServer.properties().punishmentsToLocalFile) {
            if (!PlayerSave.playerExists(username)) {
                player.message("Player " + username + " does not exist.");
                return;
            }

            //Remove from regular ban list
            if(PlayerPunishment.banned(username)) {
                PlayerPunishment.unban(username);
            }
        }
        player.message("Player " + username + " was successfully unbanned.");
        Utils.sendDiscordInfoLog("Player " + username + " was unbanned by " + player.getUsername(), "sanctions");
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isModeratorOrGreater(player));
    }

}
