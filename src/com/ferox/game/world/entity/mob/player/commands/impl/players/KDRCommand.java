package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.GameServer;
import com.ferox.db.transactions.UpdateKdrDatabaseTransaction;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

public class KDRCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        int deaths = player.getAttribOr(AttributeKey.PLAYER_DEATHS, 0);
        int kills = player.getAttribOr(AttributeKey.PLAYER_KILLS, 0);
        int killstreak = player.getAttribOr(AttributeKey.KILLSTREAK, 0);
        player.forceChat(String.format("I currently have %d kills, %d deaths, a killstreak of %d and my kdr is %s!", kills, deaths, killstreak, player.getKillDeathRatio()));
        GameServer.getDatabaseService().submit(new UpdateKdrDatabaseTransaction(Double.parseDouble(player.getKillDeathRatio()), player.getUsername()));
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
