package com.ferox.game.content.items;

import com.ferox.fs.ItemDefinition;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.loot.LootItem;
import com.ferox.game.world.items.loot.LootTable;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | April, 02, 2021, 12:04
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class HerbBox extends PacketInteraction {

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 1) {
            if(item.getId() == HERB_BOX) {
                if (player.inventory().isFull()) {
                    player.message("You need at least one inventory space to take a herb from your box.");
                    return true;
                }
                var herbBoxCharges = player.<Integer>getAttribOr(AttributeKey.HERB_BOX_CHARGES,10);
                if (herbBoxCharges == 0) {
                    player.putAttrib(AttributeKey.HERB_BOX_CHARGES,10);
                } else {
                    int herb = table.rollItem().getId();
                    player.inventory().add(herb, 1);
                    player.message("You open the herb box and find " + World.getWorld().definitions().get(ItemDefinition.class, herb).name + ".");
                    if (herbBoxCharges - 1 <= 0) {
                        player.inventory().remove(item,true);
                    }
                }
                return true;
            }
        }
        if(option == 2) {
            if(item.getId() == HERB_BOX) {
                check(player);
                return true;
            }
        }
        if(option == 3) {
            if(item.getId() == HERB_BOX) {
                open(player, item);
                return true;
            }
        }
        return false;
    }

    private void open(Player player, Item herbBox) {
        int currentCharges = player.<Integer>getAttribOr(AttributeKey.HERB_BOX_CHARGES,10);
        int amtToAdd = currentCharges == -1 || currentCharges == 0 ? 10 : currentCharges;
        for (int i = 0; i < amtToAdd; i++)
            player.getBank().add(table.rollItem().getId(), 1);

        player.inventory().remove(herbBox,true);
        player.message(amtToAdd + " herb" + (amtToAdd == 1 ? " has" : "s have") + " been deposited into your bank.");
    }

    private void check(Player player) {
        int charges = player.<Integer>getAttribOr(AttributeKey.HERB_BOX_CHARGES,10);
        if(charges == 0)
            player.putAttrib(AttributeKey.HERB_BOX_CHARGES,10);
        charges = player.<Integer>getAttribOr(AttributeKey.HERB_BOX_CHARGES,10);
        player.message("Your box has " + charges + " herb" + (charges == 1 ? "" : "s") + " left.");
    }

    private static final LootTable table = new LootTable().addTable(1,
        new LootItem(GRIMY_GUAM_LEAF, World.getWorld().random(1, 5), 25),
        new LootItem(GRIMY_MARRENTILL, World.getWorld().random(1, 5), 24),
        new LootItem(GRIMY_TARROMIN, World.getWorld().random(1, 5), 23),
        new LootItem(GRIMY_HARRALANDER, World.getWorld().random(1, 5), 22),
        new LootItem(GRIMY_RANARR_WEED, World.getWorld().random(1, 5), 21),
        new LootItem(GRIMY_IRIT_LEAF, World.getWorld().random(1, 5), 20),
        new LootItem(GRIMY_AVANTOE, World.getWorld().random(1, 5), 19),
        new LootItem(GRIMY_KWUARM, World.getWorld().random(1, 5), 18),
        new LootItem(GRIMY_CADANTINE, World.getWorld().random(1, 5), 17),
        new LootItem(GRIMY_LANTADYME, World.getWorld().random(1, 5), 16),
        new LootItem(GRIMY_DWARF_WEED, World.getWorld().random(1, 5), 15),
        new LootItem(GRIMY_SNAPDRAGON, World.getWorld().random(1, 5), 14)
    );
}
