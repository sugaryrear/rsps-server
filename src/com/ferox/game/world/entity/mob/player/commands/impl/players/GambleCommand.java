package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.content.teleport.TeleportType;
import com.ferox.game.content.teleport.Teleports;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.position.areas.impl.WildernessArea;

public class GambleCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        Tile tile = new Tile(3048, 3381);

        if(WildernessArea.inWilderness(player.tile()) && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            player.message("You can't use this command here.");
            return;
        }

        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC) || !Teleports.pkTeleportOk(player, tile)) {
            return;
        }

        Teleports.basicTeleport(player, tile);
        player.message("You have been teleported to the gambling area.");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
