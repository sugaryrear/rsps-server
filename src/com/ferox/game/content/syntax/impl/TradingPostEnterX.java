package com.ferox.game.content.syntax.impl;

import com.ferox.game.content.syntax.EnterSyntax;
import com.ferox.game.content.tradingpost.TradingPost;
import com.ferox.game.world.entity.mob.player.Player;

public class TradingPostEnterX implements EnterSyntax {

    private int id, slot;

    public TradingPostEnterX(Player player, String message, int id, int slot) {
        this.id = id;
        this.slot = slot;
        send(player, message);
    }

    public void send(Player player, String message) {
        player.getPacketSender().sendEnterAmountPrompt(message);
    }

    @Override
    public void handleSyntax(Player player, long amount) {
        TradingPost.handleSellX(player, id, (int) amount);
    }
}
