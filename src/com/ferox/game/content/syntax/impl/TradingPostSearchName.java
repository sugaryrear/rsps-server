package com.ferox.game.content.syntax.impl;

import com.ferox.game.content.syntax.EnterSyntax;
import com.ferox.game.content.tradingpost.TradingPost;
import com.ferox.game.world.entity.mob.player.Player;

public class TradingPostSearchName implements EnterSyntax {

    public TradingPostSearchName(Player p, String message) {
        p.getPacketSender().sendEnterInputPrompt(message);
    }

    @Override
    public void handleSyntax(Player player, String string) {
        TradingPost.searchByUsername(player, string, false);
    }
}
