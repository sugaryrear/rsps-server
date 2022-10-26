package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.content.teleport.TeleportType;
import com.ferox.game.content.teleport.Teleports;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.position.Tile;

/**
 * @author Patrick van Elderen | January, 11, 2021, 18:06
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 * @Chase - very sexy boi
 */
public class ZulrahCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if(!player.getMemberRights().isRegularMemberOrGreater(player)) {
            player.message("You have to be at least a Member to use this command.");
            return;
        }

        Tile tile = new Tile(2201, 3057, 0);

        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
            return;
        }

        Teleports.basicTeleport(player, tile);
        player.message("You have been teleported to Zulrah!");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
