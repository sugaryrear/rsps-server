package com.ferox.game.content.packet_actions.interactions.items;

import com.ferox.game.content.items.BraceletOfEthereum;
import com.ferox.game.content.items.MagicCape;
import com.ferox.game.content.items.RingOfWealth;
import com.ferox.game.content.items.RockCake;
import com.ferox.game.content.items.teleport.ArdyCape;
import com.ferox.game.content.skill.impl.slayer.content.SlayerRing;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteractionManager;

import static com.ferox.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * juni 04, 2020
 */
public class ItemActionThree {

    public static void click(Player player, Item item) {
        int id = item.getId();

        if (PacketInteractionManager.checkItemInteraction(player, item, 3)) {
            return;
        }

        ArdyCape.onItemOption3(player, item);
        if(player.getRunePouch().quickFill(item.getId())) {
            return;
        }
if(RingOfWealth.onItemOption3(player,item)){
    return;
}
        if(BraceletOfEthereum.onItemOption3(player,item)){
            return;
        }
        if(MagicCape.onItemOption3(player,item)){
            return;
        }
        if(SlayerRing.onItemOption3(player, item)) {
            return;
        }

        if(RockCake.onItemOption3(player, item)) {
            return;
        }

        switch (id) {
            case LOOTING_BAG, LOOTING_BAG_22586 -> player.getLootingBag().depositWidget();
        }
    }
}
