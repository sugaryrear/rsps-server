package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.game.content.packet_actions.interactions.container.AllButOneAction;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;

/**
 * @author PVE
 * @Since augustus 26, 2020
 */
public class WithdrawAllButOnePacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        final int slot = packet.readShortA();
        final int interfaceId = packet.readShort();
        final int id = packet.readShortA();

        if (player == null || player.dead()) {
            return;
        }

        if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
            player.getBankPin().openIfNot();
            return;
        }

        if(player.askForAccountPin()) {
            player.sendAccountPinMessage();
            return;
        }

        player.debugMessage(String.format("all but one packet, interfaceId: %d slot %d id %d", interfaceId, slot, id));

        AllButOneAction.allButOne(player, slot, interfaceId, id);
    }
}
