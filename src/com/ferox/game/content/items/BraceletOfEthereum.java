package com.ferox.game.content.items;

import com.ferox.game.content.DropsDisplay;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.npc.pets.Pet;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.ItemIdentifiers;

import static com.ferox.game.world.entity.AttributeKey.*;
import static com.ferox.util.ItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.INFERNAL_CAPE;

public class BraceletOfEthereum extends PacketInteraction {

    public static boolean onItemOption3(Player player, Item item) {
        if (item.getId() == 21816) {
            displayOptions(player);
            return true;
        }
        if (item.getId() == 21817) {
            toggle_absorption(player);
            return true;
        }
        return false;
    }
    public static boolean onItemOption2(Player player, Item item) {
        if (item.getId() == 21816) {
            toggle_absorption(player);
            return true;
        }
        return false;
    }
    public static void toggle_absorption(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION,"Toggle revenant ether absorption?", "Yes", "No");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(option == 1) {
                  player.message("You will now absorb revenant ether into your bracelet.");
                    player.putAttrib(BRACELET_OF_ETHEREUM_ABSORPTION,true);

                    stop();
                } else if(option == 2) {
                    player.message("Your will no longer absorb revenant ether into your bracelet.");
                    player.putAttrib(BRACELET_OF_ETHEREUM_ABSORPTION,false);
                    stop();
                }
            }
        });
    }
    public static boolean onItemOption4(Player player, Item item) {
        int amtofcharges = player.getAttribOr(BRACELET_OF_ETHEREUM_CHARGES, 0);

        if (item.getId() == ItemIdentifiers.BRACELET_OF_ETHEREUM) {
            player.inventory().remove(new Item(ItemIdentifiers.BRACELET_OF_ETHEREUM,1), true);
            player.inventory().add(new Item(ItemIdentifiers.BRACELET_OF_ETHEREUM_UNCHARGED));
            player.inventory().add(new Item(ItemIdentifiers.REVENANT_ETHER,amtofcharges));

            player.message("You uncharge your bracelet of ethereum.");
           return true;
            }

        return false;
    }
    public static void displayOptions(Player player) {
        int amtofcharges = player.getAttribOr(BRACELET_OF_ETHEREUM_CHARGES, 0);
player.message("Your bracelet of ethereum has "+amtofcharges+" charges.");
    }
    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        int amtofcharges = player.getAttribOr(BRACELET_OF_ETHEREUM_CHARGES, 0);
        int howmanyether = player.inventory().count(ItemIdentifiers.REVENANT_ETHER);
        if ((use.getId() == ItemIdentifiers.REVENANT_ETHER && usedWith.getId() == 21816) || (use.getId() == 21816 && usedWith.getId() == ItemIdentifiers.REVENANT_ETHER)) {
            player.inventory().remove(new Item(ItemIdentifiers.REVENANT_ETHER,howmanyether), true);
            player.putAttrib(BRACELET_OF_ETHEREUM_CHARGES,amtofcharges+howmanyether);
            player.message("You add "+howmanyether+" charges into your bracelet of ethereum.");
            return true;
        }
        if ((use.getId() == ItemIdentifiers.REVENANT_ETHER && usedWith.getId() == 21817) || (use.getId() == 21817 && usedWith.getId() == ItemIdentifiers.REVENANT_ETHER)) {
            if(howmanyether < 250){
                player.message("You need at least 250 ether to charge the bracelet of ethereum.");
                return false;
            }
            player.inventory().remove(new Item(ItemIdentifiers.REVENANT_ETHER,howmanyether), true);
            player.inventory().remove(new Item(21817,1), true);
            player.inventory().add(new Item(ItemIdentifiers.BRACELET_OF_ETHEREUM));
            player.putAttrib(BRACELET_OF_ETHEREUM_CHARGES,howmanyether);
            player.message("You add "+howmanyether+" charges into your bracelet of ethereum.");
            return true;
        }
        return false;
    }

}
