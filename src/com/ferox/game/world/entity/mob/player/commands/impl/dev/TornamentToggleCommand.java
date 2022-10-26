package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.game.content.tournaments.TournamentManager;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

import static java.lang.String.format;

public class TornamentToggleCommand implements Command {
    @Override
    public void execute(Player player, String command, String[] parts) {
        if (parts[0].equalsIgnoreCase("torntoggle")) {
            TournamentManager.setTournamentsDisabled(!TournamentManager.isTournamentsDisabled());
        }
        else if (parts[0].equalsIgnoreCase("tornon")) {
            TournamentManager.setTournamentsDisabled(false);
        }
        else if (parts[0].equalsIgnoreCase("tornoff")) {
            TournamentManager.setTournamentsDisabled(true);
        }
        player.message(format("The tournament system is now %s",
            TournamentManager.isTournamentsDisabled() ? "disabled." : "enabled."));
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isDeveloperOrGreater(player);
    }
}
