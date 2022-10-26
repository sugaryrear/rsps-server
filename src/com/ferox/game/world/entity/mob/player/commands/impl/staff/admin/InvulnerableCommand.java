package com.ferox.game.world.entity.mob.player.commands.impl.staff.admin;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

public class InvulnerableCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (parts.length > 1) {
            player.setInvulnerable(parts[1].equalsIgnoreCase("true") || parts[1].equalsIgnoreCase("on"));
        } else {
            player.setInvulnerable(!player.isInvulnerable());
        }
        player.message("Infinite HP is now " + (player.isInvulnerable() ? "enabled" : "disabled") + ".");
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }
}
