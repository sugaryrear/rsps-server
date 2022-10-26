package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.game.content.tradingpost.TradingPost;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

/**
 * @author Patrick van Elderen | April, 14, 2021, 13:24
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class TradingPostCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        TradingPost.open(player);
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isDeveloperOrGreater(player);
    }
}
