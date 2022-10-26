package com.ferox.game.content.mechanics;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;

/**
 * @author Patrick van Elderen | May, 26, 2021, 08:50
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class Flax extends PacketInteraction {

    private static final int FLAX = 14896;

    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if(object.getId() == FLAX) {
            if (player.getInventory().isFull()) {
                player.message("You can't carry any more flax.");
            } else {
                player.lock();
                player.animate(827);
                Chain.bound(player).name("Flax1Task").runFn(1, () -> {
                    player.getInventory().add(new Item(1779));
                    player.message("You pick some flax.");

                    // Prepare despawn & respawn
                    if (Utils.random(6) == 1) {
                        ObjectManager.removeObj(object);
                        Chain.bound(player).name("Flax2Task").runFn(10, () -> ObjectManager.addObj(object));
                    }
                    player.unlock();
                });
            }
            return true;
        }
        return false;
    }
}
