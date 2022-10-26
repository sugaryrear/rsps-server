package com.ferox.game.content.item_forging;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import com.ferox.util.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.ferox.game.world.entity.AttributeKey.LAST_ENCHANT_SELECTED;
import static com.ferox.util.ItemIdentifiers.BLOOD_MONEY;

/**
 * This class represents the item forging table.
 *
 * @author Patrick van Elderen | 16 okt. 2019 : 09:49
 * @see <a href="https://github.com/Patrick9-10-1995">Github profile</a>
 */
public class ItemForgingTable extends PacketInteraction {

    private static final int INTERFACE = 69000;
    private static final int REQUIRED_ITEMS_CONTAINER = 69016;
    private static final int SUCCESS_RATE_STRING = 69008;
    private static final int ENCHANTED_ITEM_REWARD = 69009;
    public static final int START_OF_FORGE_LIST = 69021;
    private static final int ITEM_SCROLL_ID = 69020;

    private void clear(Player player) {
        //Clear out old text
        for (int index = 0; index < 50; index++) {
            player.getPacketSender().sendString(START_OF_FORGE_LIST + index, "");
        }
    }

    public void open(Player player, ItemForgingCategory tier) {
        player.getInterfaceManager().open(INTERFACE);

        //We're viewing the items for tier...
        player.putAttrib(AttributeKey.VIEWING_FORGING_CATEGORY, tier);

        //Clear previous data
        clear(player);

        final List<ItemForgement> items = ItemForgement.sortByTier(tier);
        final int size = items.size();

        player.getPacketSender().sendScrollbarHeight(ITEM_SCROLL_ID, size * 14 + 5);

        for (int index = 0; index < size; index++) {
            final ItemForgement itemForgement = items.get(index);
            player.getPacketSender().sendString(START_OF_FORGE_LIST + index, itemForgement.name);
        }

        //Open first enchantment in the list
        loadInfo(player, ItemForgement.GRANITE_MAUL);
        player.getPacketSender().setClickedText(START_OF_FORGE_LIST,true);
    }

    private void loadInfo(Player player, ItemForgement itemForgement) {
        //Write the success rate
        String rate = "Success rate: "+itemForgement.successRate+"%";
        player.getPacketSender().sendString(SUCCESS_RATE_STRING, rate);

        //Write the required items
        player.getPacketSender().sendItemOnInterface(REQUIRED_ITEMS_CONTAINER, itemForgement.requiredItems);

        //Write the reward
        player.getPacketSender().sendItemOnInterfaceSlot(ENCHANTED_ITEM_REWARD, itemForgement.enchantedItem, 0);
    }

    private void forge(Player player, ItemForgement itemForgement) {
        boolean[] missingItems = new boolean[1];
        AtomicReference<String> itemsMissing = new AtomicReference<>("");
        Arrays.stream(itemForgement.requiredItems).forEach(requiredItem -> {
            if (!player.inventory().containsAll(requiredItem)) {
                //System.out.println("missing items ye");
                itemsMissing.set(requiredItem.unnote().name());
                missingItems[0] = true;
            }
        });

        //Check if we actually have the required items to enchant.
        if (missingItems[0]) {
            player.message(Color.RED.tag() + "You do not have the required items to attempt this enchantment.");
            player.message("Required item: " + itemsMissing);
            return;
        }
        if(!player.inventory().contains(BLOOD_MONEY, 10_000)){
            player.message("You need at least 10,000 blood money to forge an item.");
            return;
        }
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... options) {
                send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Attempt to forge item?.", "Nevermind.");
                setPhase(0);
            }

            @Override
            public void select(int option) {
                if (isPhase(0)) {
                    if (option == 1) {
                        if(!player.inventory().containsAll(itemForgement.requiredItems)) {
                            stop();
                            return;
                        }

                        player.message("You attempt the enchantment...");

                        //Calculate the chance for succeeding
                        int chance = itemForgement.successRate;

                        Arrays.stream(itemForgement.requiredItems).forEach(item -> player.inventory().removeAll(item));

                        var attempts = player.<Integer>getAttribOr(itemForgement.attempts, 0) + 1;
                        player.putAttrib(itemForgement.attempts, attempts);

                        //Roll
                        if (Utils.percentageChance(chance)) {
                            //Succesfully enchanted

                            //Add enchanted item to inventory or bank
                            player.inventory().addOrBank(itemForgement.enchantedItem);

                            //Send message
                            var totalAttempts = player.<Integer>getAttribOr(itemForgement.attempts, 0);
                            player.message("<col=" + Color.GREEN.getColorValue() + ">You successfully enchanted your item.");
                            World.getWorld().sendWorldMessage("[<col=" + Color.MEDRED.getColorValue() + ">Item Forging</col>]: " + Color.BLUE.tag() + "" + player.getUsername() + "</col> successfully forged  " + Color.HOTPINK.tag() + "" + itemForgement.enchantedItem.name() + "</col> on their "+totalAttempts+""+(totalAttempts <=1 ? "st" : totalAttempts == 2 ? "nd" : totalAttempts == 3 ? "rd" : "th" )+" try!");
                        //    World.getWorld().sendWorldMessage("[<col=" + Color.MEDRED.getColorValue() + ">Item enchantment</col>]: " + Color.BLUE.tag() + "" + player.getUsername() + "</col> successfully forged  " + Color.HOTPINK.tag() + "" + itemForgement.enchantedItem.name() + "</col>! (Try : " + totalAttempts + ")");

                        } else {
                            //Failed
                            player.message("<col=" + Color.MEDRED.getColorValue() + ">You tried to forge your item but sadly failed. Try again next time.");
                        }
                        stop();
                    } else if (option == 2) {
                        stop();
                    }
                }
            }
        });
    }

    @Override
    public boolean handleButtonInteraction(Player player, int button) {
        if (button == 69006) {
            ItemForgement itemForgement = null;
            button = player.getAttribOr(LAST_ENCHANT_SELECTED, -1); //Override button
            if (player.getAttribOr(AttributeKey.VIEWING_FORGING_CATEGORY, null) == ItemForgingCategory.WEAPON) {
                itemForgement = WEAPON_CATEGORY_BUTTONS.get(button);
            }

            if (player.getAttribOr(AttributeKey.VIEWING_FORGING_CATEGORY, null) == ItemForgingCategory.ARMOUR) {
                itemForgement = ARMOUR_CATEGORY_BUTTONS.get(button);
            }

            if (player.getAttribOr(AttributeKey.VIEWING_FORGING_CATEGORY, null) == ItemForgingCategory.MISC) {
                itemForgement = MISC_CATEGORY_BUTTONS.get(button);
            }

            if (itemForgement != null) {
                forge(player, itemForgement);
            }
            return true;
        }

        if (button == 69010) {
            open(player, ItemForgingCategory.WEAPON);
            loadInfo(player, ItemForgement.DRAGON_CLAWS);
            player.getPacketSender().setClickedText(START_OF_FORGE_LIST, true);
            player.getPacketSender().sendString(68004, "Tier I");
            return true;
        }

        if (button == 69011) {
            open(player, ItemForgingCategory.ARMOUR);
            loadInfo(player, ItemForgement.AMULET_OF_FURY);
            player.getPacketSender().setClickedText(START_OF_FORGE_LIST, true);
            player.getPacketSender().sendString(68004, "Tier II");
            return true;
        }

        if (button == 69012) {
            open(player, ItemForgingCategory.MISC);
            loadInfo(player, ItemForgement.LARRANS_KEY_TIER_II);
            player.getPacketSender().setClickedText(START_OF_FORGE_LIST, true);
            player.getPacketSender().sendString(68004, "Tier III");
            return true;
        }

        if (player.getAttribOr(AttributeKey.VIEWING_FORGING_CATEGORY, null) == ItemForgingCategory.WEAPON && WEAPON_CATEGORY_BUTTONS.containsKey(button)) {
            loadInfo(player, WEAPON_CATEGORY_BUTTONS.get(button));
            player.putAttrib(LAST_ENCHANT_SELECTED, button);
            player.getPacketSender().setClickedText(button, true);
            return true;
        }

        if (player.getAttribOr(AttributeKey.VIEWING_FORGING_CATEGORY, null) == ItemForgingCategory.ARMOUR && ARMOUR_CATEGORY_BUTTONS.containsKey(button)) {
            loadInfo(player, ARMOUR_CATEGORY_BUTTONS.get(button));
            player.putAttrib(LAST_ENCHANT_SELECTED, button);
            player.getPacketSender().setClickedText(button, true);
            return true;
        }

        if (player.getAttribOr(AttributeKey.VIEWING_FORGING_CATEGORY, null) == ItemForgingCategory.MISC && MISC_CATEGORY_BUTTONS.containsKey(button)) {
            loadInfo(player, MISC_CATEGORY_BUTTONS.get(button));
            player.putAttrib(LAST_ENCHANT_SELECTED, button);
            player.getPacketSender().setClickedText(button, true);
            return true;
        }
        return false;
    }

    private static final HashMap<Integer, ItemForgement> WEAPON_CATEGORY_BUTTONS = new HashMap<>();
    private static final HashMap<Integer, ItemForgement> ARMOUR_CATEGORY_BUTTONS = new HashMap<>();
    private static final HashMap<Integer, ItemForgement> MISC_CATEGORY_BUTTONS = new HashMap<>();

    //Store all buttons in a hashmap
    static {
        int button;
        button = START_OF_FORGE_LIST;

        for (final ItemForgement itemForgement : ItemForgement.sortByTier(ItemForgingCategory.WEAPON)) {
            WEAPON_CATEGORY_BUTTONS.put(button++, itemForgement);
        }
        button = START_OF_FORGE_LIST;
        for (final ItemForgement itemForgement : ItemForgement.sortByTier(ItemForgingCategory.ARMOUR)) {
            ARMOUR_CATEGORY_BUTTONS.put(button++, itemForgement);
        }
        button = START_OF_FORGE_LIST;
        for (final ItemForgement itemForgement : ItemForgement.sortByTier(ItemForgingCategory.MISC)) {
            MISC_CATEGORY_BUTTONS.put(button++, itemForgement);
        }
    }
}
