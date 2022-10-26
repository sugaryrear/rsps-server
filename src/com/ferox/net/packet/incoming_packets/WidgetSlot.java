package com.ferox.net.packet.incoming_packets;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;

public class WidgetSlot implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int slot = packet.readUnsignedByte();

        if (player.getTrading().getInteract() != null) {
            player.getTrading().getInteract().getPacketSender().sendModified(slot);
        }
    }
}
