package com.ferox.game.content.areas.dungeons.tzhaar;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.ObjectIdentifiers.CAVE_EXIT_11836;

public class CaveEntrance extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            if(obj.getId() == CAVE_EXIT_11836) {
                player.teleport(new Tile(2862, 9572));
                return true;
            }
        }
        return false;
    }
}
