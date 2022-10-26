package com.ferox.game.world.entity.mob.player.commands.impl.staff.admin;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

public class DismissBroadcastCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        World.getWorld().sendWorldMessage("dismissbroadcast##");
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isDeveloperOrGreater(player);
    }
}
