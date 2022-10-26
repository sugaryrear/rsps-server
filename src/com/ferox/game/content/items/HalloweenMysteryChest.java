package com.ferox.game.content.items;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import com.ferox.util.Utils;

import java.util.Arrays;
import java.util.List;

import static com.ferox.game.content.collection_logs.LogType.MYSTERY_BOX;
import static com.ferox.game.world.entity.AttributeKey.MYSTERY_CHESTS_OPENED;
import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 13, 2021
 */
public class HalloweenMysteryChest extends PacketInteraction {

    private final List<Item> rewards = Arrays.asList(
        new Item(SANGUINE_SCYTHE_OF_VITUR), new Item(SANGUINE_TWISTED_BOW), new Item(ELYSIAN_SPIRIT_SHIELD), new Item(TWISTED_BOW), new Item(SCYTHE_OF_VITUR), new Item(ANCIENT_WARRIOR_AXE_C), new Item(ANCIENT_WARRIOR_MAUL_C), new Item(ANCIENT_WARRIOR_SWORD_C),
        new Item(SALAZAR_SLYTHERINS_LOCKET), new Item(SWORD_OF_GRYFFINDOR), new Item(TALONHAWK_CROSSBOW), new Item(ANCESTRAL_HAT_I), new Item(ANCESTRAL_ROBE_BOTTOM_I), new Item(ANCESTRAL_ROBE_TOP_I), new Item(LAVA_DHIDE_BODY), new Item(LAVA_DHIDE_CHAPS), new Item(LAVA_DHIDE_COIF), new Item(CORRUPTED_BOOTS), new Item(RING_OF_TRINITY),
        new Item(CRAWS_BOW_C), new Item(VIGGORAS_CHAINMACE_C), new Item(BOW_OF_FAERDHINEN_3), new Item(DARK_ARMADYL_HELMET), new Item(DARK_ARMADYL_CHESTPLATE), new Item(DARK_ARMADYL_CHAINSKIRT));

    private void rollForReward(Player player) {
        //Safety make sure people aren't spoofing using cheat clients
        if (player.inventory().contains(HWEEN_MYSTERY_CHEST)) {

            player.inventory().remove(new Item(HWEEN_MYSTERY_CHEST), true);

            Item reward;
            //Roll for a random item
            reward = Utils.randomElement(rewards);
           // MYSTERY_BOX.log(player, MYSTERY_CHEST, reward);
            player.inventory().addOrBank(reward);

            String chest = "H'ween mystery chest";

            if (reward != null) {
                Utils.sendDiscordInfoLog("Player " + player.getUsername() + " received a "+reward.name()+" from a H'ween mystery chest.", "box_and_tickets");
                var timesOpened = player.<Integer>getAttribOr(MYSTERY_CHESTS_OPENED, 0) + 1;
                player.putAttrib(MYSTERY_CHESTS_OPENED, timesOpened);
                //The user box test doesn't yell.
                if(player.getUsername().equalsIgnoreCase("Box test")) {
                    return;
                }
                String worldMessage = "<img=505><shad=0>[<col=" + Color.MEDRED.getColorValue() + ">" + chest + "</col>]</shad>:<col=AD800F> " + player.getUsername() + " received a <shad=0>" + reward.name() + "</shad>!";
                World.getWorld().sendWorldMessage(worldMessage);
            }
        }
    }

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if (option == 1) {
            if (item.getId() == HWEEN_MYSTERY_CHEST) {
                rollForReward(player);
                return true;
            }
        }
        return false;
    }
}
