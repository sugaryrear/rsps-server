package com.ferox.game.content.seasonal_events.halloween;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import com.ferox.util.Utils;

import java.util.Arrays;
import java.util.List;

import static com.ferox.util.CustomItemIdentifiers.*;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 14, 2021
 */
public class HweenItemCasket extends PacketInteraction {

    private final List<Item> items = Arrays.asList(
        new Item(HWEEN_ARMADYL_GODSWORD),
        new Item(HWEEN_BLOWPIPE),
        new Item(HWEEN_DRAGON_CLAWS),
        new Item(HWEEN_CRAWS_BOW),
        new Item(HWEEN_CHAINMACE),
        new Item(HWEEN_GRANITE_MAUL),
        new Item(HAUNTED_SLED),
        new Item(HAUNTED_CROSSBOW),
        new Item(HAUNTED_DRAGONFIRE_SHIELD),
        new Item(GRIM_HWEEN_MASK),
        new Item(GRIM_PARTYHAT),
        new Item(GRIM_SCYTHE)
    );

    private Item reward() {
       return Utils.randomElement(items);
    }

    private void openCasket(Player player) {
        if(player.inventory().contains(HWEEN_ITEM_CHEST)) {
            player.inventory().remove(HWEEN_ITEM_CHEST);
            Item reward = reward();
            player.inventory().add(reward);
            World.getWorld().sendWorldMessage("<img=505><shad=0>"+ Color.MEDRED.wrap("[News]:")+"</shad> "+Color.PURPLE.wrap(player.getUsername()) + " received "+Color.HOTPINK.wrap(reward.unnote().name())+" from the Event Item Casket!");
        }
    }

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 1) {
            if(item.getId() == HWEEN_ITEM_CHEST) {
                openCasket(player);
                return true;
            }
        }
        return false;
    }
}
