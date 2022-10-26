package com.ferox.net.packet.incoming_packets;

import com.ferox.game.content.skill.impl.firemaking.LogLighting;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.ground.GroundItem;
import com.ferox.game.world.items.ground.GroundItemHandler;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;

import java.util.Optional;

/**
 * @author PVE
 * @Since augustus 24, 2020
 */
public class ItemOnGroundItemPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int interfaceId = packet.readLEShort();
        int itemUsedId = packet.readShortA();
        int gItem = packet.readShort();
        int y = packet.readShortA();
        int slot = packet.readLEShortA();
        int x = packet.readShort();

        player.debugMessage(String.format("itemOnGroundItem, interface: %d slot: %d itemUsedId: %d gItem: %d x: %d y: %d", interfaceId, slot, itemUsedId, gItem, x, y));

        Optional<GroundItem> groundItem = GroundItemHandler.getGroundItem(gItem, new Tile(x, y), player);

        if (!player.locked() || !player.dead()) {
            Item item = player.inventory().get(slot);
            if (item == null)
                return;

            player.stopActions(true);
            player.putAttrib(AttributeKey.FROM_ITEM, item);
            player.putAttrib(AttributeKey.ITEM_SLOT, slot);
            player.putAttrib(AttributeKey.INTERACTED_GROUNDITEM, groundItem.get());
            player.putAttrib(AttributeKey.INTERACTION_OPTION, -1);

            player.getRouteFinder().routeGroundItem(groundItem.get(), distance -> {
                //Face...
                player.face(groundItem.get().getTile());


                //Handle used item..
                LogLighting.onInvitemOnGrounditem(player, item);
            });
        }
    }
}
