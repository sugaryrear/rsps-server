package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.GameServer;
import com.ferox.db.transactions.CollectVotes;
import com.ferox.game.content.raids.party.Party;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

public class TimeRaids implements Command {



    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getChamberOfSecrets().gettime();
    }

    @Override
    public boolean canUse(Player player) {
        Party party = player.raidsParty;
        if (party == null)
            return false;
        return true;
    }
}
