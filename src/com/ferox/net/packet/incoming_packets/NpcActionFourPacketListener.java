package com.ferox.net.packet.incoming_packets;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;

/**
 * @author PVE
 * @Since augustus 27, 2020
 */
public class NpcActionFourPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int index = packet.readLEShort();
        Npc npc = World.getWorld().getNpcs().get(index);

        NpcActionOnePacketListener.handleNpcClicks(player, npc, 4);
    }
}
