package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.game.content.interfaces.BonusesInterface;
import com.ferox.game.content.items.Ornatejewelrybox;
import com.ferox.game.content.mechanics.Transmogrify;
import com.ferox.game.content.mechanics.item_simulator.ItemSimulatorUtility;
import com.ferox.game.world.InterfaceConstants;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.container.equipment.EquipmentInfo;
import com.ferox.game.world.items.container.looting_bag.LootingBag;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;
import com.ferox.net.packet.interaction.PacketInteractionManager;
import com.ferox.util.ItemIdentifiers;

/**
 * This packet listener manages the equip action a player
 * executes when wielding or equipping an item.
 *
 * @author relex lawl
 */

public class EquipPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int id = packet.readShort();
        int slot = packet.readShortA();
        int interfaceId = packet.readShortA();

        boolean newAccount = player.getAttribOr(AttributeKey.NEW_ACCOUNT, false);

        if (newAccount) {
            player.message("You have to select your game mode before you can continue.");
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

        if (slot < 0 || slot > 27)
            return;
        Item item = player.inventory().get(slot);
        if (item != null && item.getId() == id && !player.locked() && !player.dead()) {
            if(player.getInterfaceManager().isInterfaceOpen(ItemSimulatorUtility.WIDGET_ID)) {
                player.message("Close this interface before trying to equip your "+item.unnote().name()+".");
                return;
            }

            //Close all other interfaces except for the {@code Equipment.EQUIPMENT_SCREEN_INTERFACE_ID} one..
            if (!player.getInterfaceManager().isClear() && !player.getInterfaceManager().isInterfaceOpen(InterfaceConstants.EQUIPMENT_SCREEN_INTERFACE_ID)) {
                player.getInterfaceManager().close(false);
            }

            if (item.getId() == ItemIdentifiers.LOOTING_BAG || item.getId() == LootingBag.OPEN_LOOTING_BAG) {
                player.getLootingBag().open();
                return;
            }

            if (interfaceId == InterfaceConstants.INVENTORY_INTERFACE) {
                player.debugMessage("Equip ItemId=" + id + " Slot=" + slot + " InterfaceId=" + interfaceId);

                //Stop skilling..
                player.skills().stopSkillable();
                if(Ornatejewelrybox.onItemOption1(player, item)) {
                    return;
                }

                Transmogrify.onItemEquip(player, item);

                if(PacketInteractionManager.onEquipItem(player, item)) {
                    return;
                }

                EquipmentInfo info = World.getWorld().equipmentInfo();
                if(info != null) {
                    player.getEquipment().equip(slot);
                    BonusesInterface.sendBonuses(player);
                }
            }
        }
    }
}
