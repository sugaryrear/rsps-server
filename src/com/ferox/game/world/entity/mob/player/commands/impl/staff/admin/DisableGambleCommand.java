package com.ferox.game.world.entity.mob.player.commands.impl.staff.admin;

import com.ferox.game.content.gambling.GamblingSession;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

public class DisableGambleCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        GamblingSession.ENABLED =! GamblingSession.ENABLED;
        String msg = GamblingSession.ENABLED ? "Enabled" : "Disabled";
        player.message("Gambling is now "+msg+".");
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isAdminOrGreater(player);
    }
}
