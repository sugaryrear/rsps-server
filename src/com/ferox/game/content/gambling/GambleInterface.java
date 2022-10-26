package com.ferox.game.content.gambling;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.interaction.PacketInteraction;

public class GambleInterface extends PacketInteraction {

    @Override
    public boolean handleButtonInteraction(Player player, int button) {
        return player.getGamblingSession() != null && player.getGamblingSession().handleButton(button);
    }
}
