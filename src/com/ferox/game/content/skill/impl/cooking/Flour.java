package com.ferox.game.content.skill.impl.cooking;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.ItemIdentifiers;

import static com.ferox.util.ItemIdentifiers.CRYSTAL_KEY;
import static com.ferox.util.ItemIdentifiers.POT_OF_FLOUR;

public class Flour extends PacketInteraction {

    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() ==ItemIdentifiers.GRAIN && usedWith.getId() == ItemIdentifiers.POT) || (use.getId() == ItemIdentifiers.POT&& usedWith.getId() == ItemIdentifiers.GRAIN)) {
            player.inventory().remove(new Item(ItemIdentifiers.POT), true);
            player.inventory().remove(new Item(ItemIdentifiers.GRAIN), true);
            player.inventory().add(new Item(POT_OF_FLOUR), true);
            return true;
        }
        return false;
    }
}
