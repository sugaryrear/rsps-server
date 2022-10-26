package com.ferox.game.world.entity.mob.player.commands.impl.staff.admin;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.util.Utils;

import java.util.Optional;

public class GetIpCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
       // Known exploit
        if (command.contains("\r") || command.contains("\n")) {
            return;
        }
        String[] pieces = command.split(" ");
        final String player2 = Utils.formatText(command.substring(parts[0].length() + 1));
        Optional<Player> plr = World.getWorld().getPlayerByName(player2);

        if (plr.isPresent()) {
            player.message("The IP address of " + player2 + " is " + plr.get().getHostAddress());
        } else {
            player.message("The player " + player2 + " is not online.");
        }

    }

    @Override
    public boolean canUse(Player player) {
        return (player.getUsername().equalsIgnoreCase("Patrick") || player.getUsername().equalsIgnoreCase("Matthew"));
    }
}
