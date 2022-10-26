package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.GameServer;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

public class FloodCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        int amt = Integer.parseInt(parts[1]);
        GameServer.getFlooder().login(amt);
    }

    @Override
    public boolean canUse(Player player) {

        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
