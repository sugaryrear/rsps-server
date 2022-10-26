package com.ferox.game.content.mechanics;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;

public class Cabbage extends PacketInteraction {

    private static final int CABBAGE = 1161;

    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if(object.getId() == CABBAGE) {
            if (player.getInventory().isFull()) {
                player.message("You can't carry any more cabbage.");
            } else {
                player.lock();
                player.animate(827);
                Chain.bound(player).name("Cabbage1Task").runFn(1, () -> {
                    player.getInventory().add(new Item(ItemIdentifiers.CABBAGE));
                    player.message("You pick some cabbage.");

                    // Prepare despawn & respawn

                        ObjectManager.removeObj(object);
                        Chain.bound(player).name("Cabbage2Task").runFn(10, () -> ObjectManager.addObj(object));

                    player.unlock();
                });
            }
            return true;
        }
        return false;
    }
}
