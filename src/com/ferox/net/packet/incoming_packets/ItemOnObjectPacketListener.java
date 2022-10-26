package com.ferox.net.packet.incoming_packets;

import com.ferox.game.content.items.RottenPotato;
import com.ferox.game.content.packet_actions.interactions.items.ItemOnObject;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.MapObjects;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static org.apache.logging.log4j.util.Unbox.box;

/**
 * @author PVE
 * @Since augustus 24, 2020
 */
public class ItemOnObjectPacketListener implements PacketListener {

    private static final Logger logger = LogManager.getLogger(ItemOnObjectPacketListener.class);

    @Override
    public void handleMessage(Player player, Packet packet) {
        int interfaceType = packet.readShort();
        final int objectId = packet.readUnsignedShort();
        final int objectY = packet.readLEShortA();
        final int slot = packet.readLEShort();
        final int objectX = packet.readLEShortA();
        final int itemId = packet.readShort();

        Tile tile = new Tile(objectX, objectY, player.tile().getLevel());
        Optional<GameObject> object = MapObjects.get(objectId, tile);
        int osize = object.map(GameObject::getSize).orElse(1);

        if (object.isEmpty() && tile.getLevel() > 3) {
            tile = new Tile(objectX, objectY, player.tile().getLevel() % 4);
            object = MapObjects.get(objectId, tile);
            player.debugMessage(String.format("found real mapobj %s from %s", object.orElse(null), player.tile().level));
        }

        //Make sure the object actually exists in the region...
        if (object.isEmpty()) {
           // logger.info("Object with id {} does not exist for player " + player.toString() + "!", box(objectId));
            //Utils.sendDiscordErrorLog("Object with id " + objectId + " does not exist for player " + player.toString() + "!");
            return;
        }

        // Check item
        Item item = player.inventory().get(slot);
        if (item == null || item.getId() != itemId) {
            return;
        }

        final GameObject gameObject = object.get();
        if (!player.locked() && !player.dead()) {
            player.stopActions(true);
            player.putAttrib(AttributeKey.INTERACTION_OBJECT, gameObject);
            player.putAttrib(AttributeKey.INTERACTION_OPTION, -1); // Special code
            player.putAttrib(AttributeKey.ITEM_SLOT, slot);
            player.putAttrib(AttributeKey.ITEM_ID, itemId);
            player.putAttrib(AttributeKey.FROM_ITEM, item);


            //Handle actions...
            player.faceObj(gameObject);// Always face what you interacted with.

            // Cannot interact with objects when speared. #OSSRV-100
            if (player.stunned()) {
                return;
            }

            if (gameObject.interactAble()) {
                boolean reachable = false;

                if (player.getPlayerRights().isDeveloperOrGreater(player)) {
                    if (itemId == 5733) {
                        RottenPotato.used_on_object(player);
                        return;
                    }
                }

                if (gameObject.getId() == 11663) {
                    ItemOnObject.itemOnObject(player, item, gameObject);
                    return;
                }

                //occult altar
                if (gameObject.getId() == 29150 && player.tile().distance(gameObject.tile()) <= 2) {
                    ItemOnObject.itemOnObject(player, item, gameObject);
                }

                if ((gameObject.getType() == 10 && gameObject.tile() == player.tile())) {// We are ontop of it
                    ItemOnObject.itemOnObject(player, item, gameObject);
                } else {
                    player.getRouteFinder().routeObject(gameObject, () -> {
                        ItemOnObject.itemOnObject(player, item, gameObject);
                    });

                }
            }
        }
    }
}
