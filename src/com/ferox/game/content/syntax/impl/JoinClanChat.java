package com.ferox.game.content.syntax.impl;

import com.ferox.game.content.clan.ClanManager;
import com.ferox.game.content.syntax.EnterSyntax;
import com.ferox.game.world.entity.mob.player.Player;

public class JoinClanChat implements EnterSyntax {

    @Override
    public void handleSyntax(Player player, String input) {
        ClanManager.join(player, input);
    }

    @Override
    public void handleSyntax(Player player, long input) {

    }
}
