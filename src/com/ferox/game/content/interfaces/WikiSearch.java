package com.ferox.game.content.interfaces;

import com.ferox.game.content.areas.edgevile.BobBarter;
import com.ferox.game.content.syntax.impl.ChangePasswordSyntax;
import com.ferox.game.content.syntax.impl.WikiSearchSyntax;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import com.ferox.util.SecondsTimer;

public class WikiSearch extends PacketInteraction {

    private final SecondsTimer button_delay = new SecondsTimer();

    @Override
    public boolean handleButtonInteraction(Player player, int button) {
        if (button == 1513) {
            player.setEnterSyntax(new WikiSearchSyntax());
            player.getPacketSender().sendEnterInputPrompt("Search the OSRS Wiki:");
            return true;
        }

        return false;
    }
}

