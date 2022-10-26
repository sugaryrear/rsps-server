package com.ferox.net.packet.incoming_packets;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.combat.magic.MagicClickSpells;
import com.ferox.game.world.entity.mob.player.Player;
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
 * @author Patrick van Elderen | March, 17, 2021, 15:28
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class MagicOnObjectPacketListener implements PacketListener {

    private static final Logger logger = LogManager.getLogger(MagicOnObjectPacketListener.class);

    @Override
    public void handleMessage(Player player, Packet packet) {
        int x = packet.readLEShort();
        int spell_id = packet.readShortA();
        int y = packet.readShortA();
        int object_id = packet.readLEShort();

        Tile tile = new Tile(x, y, player.tile().getZ());
        Optional<GameObject> object = MapObjects.get(object_id, tile);

        //Fix object not found for instances
        if (object.isEmpty() && tile.getLevel() > 3) {
            tile = new Tile(x, y, player.tile().getLevel() % 4);
            object = MapObjects.get(object_id, tile);
            player.debugMessage(String.format("found real mapobj %s from %s", object.orElse(null), player.tile().level));
        }

        object.ifPresent(gameObject -> player.debugMessage("Magic on object " + gameObject.toString()));

        //Make sure the object actually exists in the region...
        if (object.isEmpty()) {
            //logger.info("Object op1 with id {} does not exist for player " + player.toString() + " !", box(object_id));
            //Utils.sendDiscordInfoLog("Object op1 with id " + object_id + " does not exist for player " + player.toString() + "!");
            return;
        }

        final GameObject gameObject = object.get();

        if (gameObject.definition() == null) {
            logger.error("ObjectDefinition for object {} is null for player " + player.toString() + ".", box(object_id));
            return;
        }

        if (player.locked() || player.dead()) {
            return;
        }

        player.stopActions(false);
        player.putAttrib(AttributeKey.INTERACTION_OBJECT, gameObject);

        //Do actions...
        player.faceObj(gameObject);

        if(MagicClickSpells.handleSpellOnObject(player, gameObject, tile, spell_id)) {
            return;
        }

        player.message("Nothing interesting happens.");
    }
}
