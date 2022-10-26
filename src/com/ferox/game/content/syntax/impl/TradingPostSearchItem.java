package com.ferox.game.content.syntax.impl;

import com.ferox.game.content.syntax.EnterSyntax;
import com.ferox.game.content.tradingpost.TradingPost;
import com.ferox.game.world.entity.mob.player.Player;
import org.jetbrains.annotations.NotNull;

public class TradingPostSearchItem implements EnterSyntax {

    public TradingPostSearchItem(Player p, String message) {
        p.getPacketSender().sendEnterInputPrompt(message);
    }

    @Override
    public void handleSyntax(Player player, @NotNull String string) {
        TradingPost.searchByItemName(player, string, false);
    }
}
