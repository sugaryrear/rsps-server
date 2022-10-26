package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.game.content.items.RottenPotato;
import com.ferox.game.content.packet_actions.interactions.items.ItemActionOne;
import com.ferox.game.world.InterfaceConstants;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;

import static com.ferox.util.ItemIdentifiers.ROTTEN_POTATO;

/**
 * @author PVE
 * @Since augustus 27, 2020
 */
public class ItemActionOnePacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        final int interfaceId = packet.readUnsignedShort();
        final int id = packet.readShort();
        final int slot = packet.readShort();

        player.debugMessage(String.format("First item action, interface: %d id: %d slot: %d", interfaceId, id, slot));

        if (slot < 0 || slot > 27)
            return;

        final Item item = player.inventory().get(slot);

        if (item != null && item.getId() == id) {

            if(item.getId() == ROTTEN_POTATO) {
                RottenPotato.onItemOption1(player);
                //System.out.println("Block because of Rotten Potato.");
                return;
            }

            if (player.locked() || player.dead()) {
                //System.out.println("Player locked or dead.");
                return;
            }

            if (player.busy()) {
                //System.out.println("Player is busy.");
                return;
            }

            if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
                player.getBankPin().openIfNot();
                //System.out.println("Bank pin.");
                return;
            }

            if(player.askForAccountPin()) {
                player.sendAccountPinMessage();
                return;
            }

            player.afkTimer.reset();

            if (interfaceId == InterfaceConstants.INVENTORY_INTERFACE) {
                player.stopActions(false);
                player.putAttrib(AttributeKey.ITEM_SLOT, slot);
                player.putAttrib(AttributeKey.FROM_ITEM, player.inventory().get(slot));
                player.putAttrib(AttributeKey.ITEM_ID, item.getId());

                ItemActionOne.click(player, item);
            }
        }
    }
}
