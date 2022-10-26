package com.ferox.game.content.areas.dungeons;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.chainedwork.Chain;

import static com.ferox.util.ObjectIdentifiers.BIG_DOOR;
import static com.ferox.util.ObjectIdentifiers.ICE_BRIDGE;

public class WyvernCave extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            if(obj.getId() == 31481) {
                player.teleport(3597,10292,0);
                return true;
            }

            if(obj.getId() == 30844) {
                player.teleport(3716,3815,0);
                return true;
            }
            if(obj.getId() == 31485) {
                if(player.tile().x < 3605)
                player.teleport(3607,10290,0);
                else
                    player.teleport(3603,10291,0);

                return true;
            }
        }
        return false;
    }
}
