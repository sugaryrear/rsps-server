package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

public class InterfaceCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getInterfaceManager().open(Integer.parseInt(parts[1]));
    }

    @Override
    public boolean canUse(Player player) {

        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
