package com.ferox.game.content.skill.impl.smithing;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.ItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.ABYSSAL_TENTACLE;

public class MithGrapple extends PacketInteraction {
    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == 9416 || usedWith.getId() == 9142) && (use.getId() == 9142 || usedWith.getId() == 9416)) {
           // if (player.inventory().count(9416) > 0 && player.inventory().count(9142) > 0) {

                player.inventory().remove(new Item(9142));
                player.inventory().remove(new Item(9416));
                player.inventory().add(new Item(9418));
                player.message("You create a mithril grapple. It still needs a rope attached to it.");
          //  }
            return true;
        }
        if ((use.getId() == 954 || usedWith.getId() == 9418) && (use.getId() == 9418 || usedWith.getId() == 954)) {
       //     if (player.inventory().count(9418) > 0 && player.inventory().count(954) > 0) {

                player.inventory().remove(new Item(9418));
                player.inventory().remove(new Item(954));
                player.inventory().add(new Item(9419));
                player.message("You create a mithril grapple.");
         //   }
            return true;
        }
        return false;
    }
}
