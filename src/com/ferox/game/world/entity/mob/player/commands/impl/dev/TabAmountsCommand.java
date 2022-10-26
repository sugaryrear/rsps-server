package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

import java.util.Arrays;
import java.util.stream.IntStream;

public class TabAmountsCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.message("Tab amounts: " + Arrays.toString(player.getBank().tabAmounts));
        player.message("Used slots: " + (player.getBank().capacity() - player.getBank().getFreeSlots()));
        player.message((IntStream.of(player.getBank().tabAmounts).sum() == (player.getBank().capacity() - player.getBank().getFreeSlots())) ? "Bank tabAmounts equals used slots." : "<col=ca0d0d>Bank tabAmounts does not equal used slots.");
    }

    @Override
    public boolean canUse(Player player) {

        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
