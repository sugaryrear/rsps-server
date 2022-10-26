package com.ferox.game.world.entity.mob.player.commands.impl.staff.moderator;

import com.ferox.GameServer;
import com.ferox.db.transactions.UnmutePlayerDatabaseTransaction;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.util.PlayerPunishment;
import com.ferox.util.Utils;

import java.util.Optional;

public class UnMutePlayerCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (command.length() <= 7)
            return;
        String username = Utils.formatText(command.substring(7)); // after "unmute "

        Optional<Player> plr = World.getWorld().getPlayerByName(username);
        if (GameServer.properties().enableSql && GameServer.properties().punishmentsToDatabase) {
            plr.ifPresent(value -> value.putAttrib(AttributeKey.MUTED,false));
            GameServer.getDatabaseService().submit(new UnmutePlayerDatabaseTransaction(username));
            player.message("Player " + username + " was successfully unmuted.");
        }

        if (GameServer.properties().punishmentsToLocalFile) {
            plr.ifPresent(value -> value.putAttrib(AttributeKey.MUTED,false));

            if (!PlayerPunishment.muted(username)) {
                player.message("Player " + username + " does not have an active mute.");
                return;
            }

            //Remove from regular mute list
            if (PlayerPunishment.muted(username)) {
                PlayerPunishment.unmute(username);
            }

            //And from regular the IP mute list
            if (PlayerPunishment.IPmuted(username)) {
                PlayerPunishment.unIPMuteUser(username);
            }

            player.message("Player " + username + " (" + username + ") was successfully unmuted.");
        }
        Utils.sendDiscordInfoLog("Player " + username + " was unmuted by " + player.getUsername(), "sanctions");
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isModeratorOrGreater(player));
    }

}
