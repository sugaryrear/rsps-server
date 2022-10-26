package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.game.content.packet_actions.interactions.container.ThirdContainerAction;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;

/**
 * @author PVE
 * @Since augustus 26, 2020
 */
public class ThirdItemContainerActionPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int interfaceId = packet.readInt();
        int id = packet.readUnsignedShortA();
        int slot = packet.readUnsignedShortA();

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

        player.debugMessage(String.format("third action, container: %d slot: %d id %d", interfaceId, slot, id));

        ThirdContainerAction.thirdAction(player, interfaceId, slot, id);
    }
}
