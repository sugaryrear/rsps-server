package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.content.tournaments.TournamentManager;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

/**
 * @author Shadowrs (tardisfan121@gmail.com)
 */
public class LeaveTournamentCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (player.isInTournamentLobby()) {
            TournamentManager.leaveLobby(player);
        }
    }

    @Override
    public boolean canUse(Player player) {
        return player.isInTournamentLobby();
    }

}
