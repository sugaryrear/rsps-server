package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.game.content.packet_actions.interactions.container.SecondContainerAction;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;

/**
 * @author PVE
 * @Since augustus 26, 2020
 */
public class SecondItemContainerActionPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int interfaceId = packet.readInt();
        int id = packet.readLEShortA();
        int slot = packet.readLEShort();

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

        player.debugMessage(String.format("ItemContainerAction: second action, container: %d slot: %d id %d", interfaceId, slot, id));
        player.putAttrib(AttributeKey.ITEM_SLOT, slot);
        player.putAttrib(AttributeKey.ITEM_ID, id);

        SecondContainerAction.secondAction(player, interfaceId, slot, id);
    }
}
