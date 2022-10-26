package com.ferox.net.packet.incoming_packets;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.ClientToServerPackets;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;

/**
 * Represents a packet used for handling dialogues.
 * This specific packet currently handles the action
 * for clicking the "next" option during a dialogue_old.
 * 
 * @author Professor Oak
 */

public class DialoguePacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {

        if (player == null || player.dead()) {
            return;
        }
        player.afkTimer.reset();

        if (packet.getOpcode() == ClientToServerPackets.DIALOGUE_OPCODE) {
            if (player.getDialogueManager().isActive()) {
                if (player.getDialogueManager().next()) {
                    return;
                }
            }
        }
    }
}
