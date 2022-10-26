package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.content.tournaments.TournamentManager;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

public class ExitCommand implements Command {

    public void execute(Player player, String command, String[] parts) {
        //remove from tourny
        TournamentManager.leaveTourny(player, false,true);
        player.message("You have left the tournament.");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
