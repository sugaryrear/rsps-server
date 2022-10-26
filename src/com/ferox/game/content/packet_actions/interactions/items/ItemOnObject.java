package com.ferox.game.content.packet_actions.interactions.items;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.net.packet.interaction.PacketInteractionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.apache.logging.log4j.util.Unbox.box;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * mei 05, 2020
 */
public class ItemOnObject {

    private static final Logger logger = LogManager.getLogger(ItemOnObject.class);

    public static void itemOnObject(Player player, Item item, GameObject object) {
        //If the object doesn't exist, we probably shouldn't do anything about it.
        if (object == null) {
            return;
        }

        if (object.definition() == null) {
            logger.error("ObjectDefinition for object {} is null for player " + player.toString() + ".", box(object.getId()));
            return;
        }

        if (player.farming().handleItemOnObjectInteraction(object.getId(), item.getId(), object.getX(), object.getY())) {
            return;
        }

        if (PacketInteractionManager.checkItemOnObjectInteraction(player, item, object)) {
            return;
        }

        player.message("Nothing interesting happens.");
    }
}
