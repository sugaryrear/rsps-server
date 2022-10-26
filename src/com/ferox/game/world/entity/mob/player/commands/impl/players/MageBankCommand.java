package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.content.teleport.TeleportType;
import com.ferox.game.content.teleport.Teleports;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.position.Tile;

/**
 * @author Patrick van Elderen | January, 11, 2021, 18:09
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class MageBankCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        Tile tile = new Tile(2539, 4716);

        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC) || !Teleports.pkTeleportOk(player, tile)) {
            return;
        }

        Teleports.basicTeleport(player, tile);
        player.message("You have been teleported to the mage bank.");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
