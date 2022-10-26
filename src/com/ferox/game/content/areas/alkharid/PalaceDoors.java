package com.ferox.game.content.areas.alkharid;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;

/**
 * @author Patrick van Elderen | April, 14, 2021, 18:18
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class PalaceDoors extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            if (obj.getId() == 1511 || obj.getId() == 1513) {
                if (obj.tile().equals(3293, 3167) || obj.tile().equals(3292, 3167)) {
                    openGate();
                    return true;
                }
            }

            if (obj.getId() == 1516 || obj.getId() == 1512) {
                if (obj.tile().equals(3292, 3167) || obj.tile().equals(3293, 3167)) {
                    closeGate();
                    return true;
                }
            }
        }
        return false;
    }

    private void openGate() {
        ObjectManager.removeObj(new GameObject(1513, new Tile(3293,3167),0,3));
        ObjectManager.removeObj(new GameObject(1511, new Tile(3292,3167),0,0));

        ObjectManager.addObj(new GameObject(1516, new Tile(3293,3167),0,2));
        ObjectManager.addObj(new GameObject(1512, new Tile(3292,3167),0,0));
    }

    private void closeGate() {
        ObjectManager.removeObj(new GameObject(1516, new Tile(3293,3167),0,2));
        ObjectManager.removeObj(new GameObject(1512, new Tile(3292,3167),0,0));

        ObjectManager.addObj(new GameObject(1513, new Tile(3293,3167),0,3));
        ObjectManager.addObj(new GameObject(1511, new Tile(3292,3167),0,3));
    }
}
