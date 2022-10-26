package com.ferox.game.content.packet_actions.interactions.items;

import com.ferox.game.content.items.BraceletOfEthereum;
import com.ferox.game.world.entity.mob.npc.pets.PetAI;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * juni 24, 2020
 */
public class ItemActionFour {

    public static boolean click(Player player, Item item) {

        if(PetAI.onItemOption4(player, item)) {
            return true;
        }
        if(BraceletOfEthereum.onItemOption4(player, item)) {
            return true;
        }
        return false;
    }
}
