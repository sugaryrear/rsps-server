package com.ferox.game.content.items.combine;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;

import java.util.Arrays;
import java.util.List;

import static com.ferox.util.ItemIdentifiers.*;

public class BrimstoneRing extends PacketInteraction {

    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        List<Item> parts = Arrays.asList(new Item(HYDRAS_HEART), new Item(HYDRAS_FANG), new Item(HYDRAS_EYE));
        for(Item id : parts) {
            if((use.getId() == id.getId() || usedWith.getId() == id.getId())) {
                if(player.inventory().containsAll(parts)) {
                    player.confirmDialogue(new String[]{"Are you sure you wish to combine the" + Color.RED.tag() + " Hydra Heart, the Hydra", "Eye, and Hydra Fang into the Brimstone Ring? This can not be,", Color.RED.tag() + "Reversed.</col>"}, "", "Proceed with the combination.", "Cancel.", () -> {
                        if(!player.inventory().containsAll(parts)) {
                            return;
                        }
                        player.animate(4462);
                        player.graphic(759,15,0);
                        player.inventory().removeAll(parts);
                        player.inventory().add(new Item(BRIMSTONE_RING));
                        player.message("You successfully combine the Hydra Heart, the Hydra Eye, and the Hydra Fang to create");
                        player.message("the Brimstone Ring.");
                        player.itemDialogue("You successfully combine the Hydra Heart, the Hydra<br>Eye, and the Hydra Fang into the Brimstone Ring.", BRIMSTONE_RING);
                    });
                } else {
                    player.message("Nothing interesting happens.");
                }
                return true;
            }
        }
        return false;
    }
}
