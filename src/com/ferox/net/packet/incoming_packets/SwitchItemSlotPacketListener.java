package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.game.world.InterfaceConstants;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.PlayerStatus;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;

/**
 * This packet listener is called when an item is dragged onto another slot.
 * 
 * @author relex lawl
 */

public class SwitchItemSlotPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        final int interfaceId = packet.readInt();
        final int inserting = packet.readByteC();
        final int fromSlot = packet.readLEShortA();
        final int toSlot = packet.readLEShort();

        if (player == null || player.dead()) {
            return;
        }

        player.afkTimer.reset();

        if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
            player.getBankPin().openIfNot();
            return;
        }

        if(player.askForAccountPin()) {
            player.sendAccountPinMessage();
            return;
        }

        if(player.getStatus() == PlayerStatus.TRADING || player.getStatus() == PlayerStatus.DUELING || player.getStatus() == PlayerStatus.GAMBLING) {
            return;
        }

        switch (interfaceId) {
            case 3214, InterfaceConstants.INVENTORY_STORE -> player.inventory().swap(fromSlot, toSlot);
            case InterfaceConstants.WITHDRAW_BANK -> player.getBank().moveItem(inserting, fromSlot, toSlot);
        }
    }
}
