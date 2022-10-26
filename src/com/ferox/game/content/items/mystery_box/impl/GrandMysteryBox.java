package com.ferox.game.content.items.mystery_box.impl;

import com.ferox.GameServer;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.Utils;

import java.util.Arrays;

import static com.ferox.game.content.items.mystery_box.impl.DonatorMysteryBox.DONATOR_MYSTERY_BOX;
import static com.ferox.game.content.items.mystery_box.impl.LegendaryMysteryBox.LEGENDARY_MYSTERY_BOX;
import static com.ferox.util.CustomItemIdentifiers.EPIC_PET_BOX;
import static com.ferox.util.ItemIdentifiers.MYSTERY_BOX;

/**
 * @author Patrick van Elderen | Zerikoth | PVE
 * @date februari 08, 2020 13:01
 */
public class GrandMysteryBox extends PacketInteraction {

    public static final int GRAND_MYSTERY_BOX = 16455;

    /**
     * This option gives 1 legendary mystery box and 2 donator mystery box.
     * There is a 75% chance that you get this option.
     */
    private static final Item[] OPTION_ONE = {new Item(LEGENDARY_MYSTERY_BOX), new Item(DONATOR_MYSTERY_BOX), new Item(DONATOR_MYSTERY_BOX)};

    /**
     * This option gives 1 legendary mystery box and 3 donator mystery box
     * There is a 25% chance that you get this option.
     */
    private static final Item[] OPTION_TWO = {new Item(LEGENDARY_MYSTERY_BOX), new Item(DONATOR_MYSTERY_BOX), new Item(DONATOR_MYSTERY_BOX), new Item(DONATOR_MYSTERY_BOX)};

    /**
     * 1 legendary mystery box and one epic pet box
     * There is a 10% chance that you get this option.
     */
    private static final Item[] OPTION_THREE = {new Item(LEGENDARY_MYSTERY_BOX), new Item(EPIC_PET_BOX)};

    /**
     * This option gives you a mystery chest promo I
     * There is a slim chance that you get this option.
     */
    private static final Item[] OPTION_FOUR = {new Item(CustomItemIdentifiers.MYSTERY_CHEST, 1)};

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 1) {
            if(item.getId() == GRAND_MYSTERY_BOX) {
                open(player);
                return true;
            }
        }
        return false;
    }

    /**
     * The grand mystery box opens three mystery boxes
     */
    private void open(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.STATEMENT, "<col=FF0000>75% chance to get</col> 1 Legendary and 2 Donator box.", "<col=FF0000>50% chance to get</col> 1 Legendary and 3 Donator box.", "<col=FF0000>25% chance to get</col> 1 Legendary and 1 Epic pet box.", "<col=FF0000>EXTREME RARE: 1 Mystery chest.</col>");
                setPhase(1);
            }

            @Override
            protected void next() {
                if(isPhase(1)) {
                    send(DialogueType.OPTION, "Open the Grand Mystery Box?", "Yes.", "No.");
                    setPhase(2);
                }
            }

            @Override
            protected void select(int option) {
                if(isPhase(2)) {
                    if(option == 1) {
                        if(!player.inventory().contains(GRAND_MYSTERY_BOX)) {
                            stop();
                            return;
                        }
                        reward(player);
                    }
                    stop();
                }
            }
        });
    }

    public static void reward(Player player) {
        if (!player.inventory().contains(GRAND_MYSTERY_BOX))
            return;

        int avail = player.inventory().getFreeSlots();

        if(avail > 5) {
            player.inventory().remove(new Item(GRAND_MYSTERY_BOX),true);

            if (Utils.rollDie(GameServer.properties().nerfDropRateBoxes ? 50 : 15, 1)) {
                player.inventory().addAll(OPTION_FOUR);
                //The user box test doesn't yell.
                if(player.getUsername().equalsIgnoreCase("Box test")) {
                    return;
                }
                Utils.sendDiscordInfoLog("Player " + player.getUsername() + " received "+ Arrays.toString(OPTION_FOUR) +" from a mystery box.", "box_and_tickets");
                String worldMessage = "<img=505>[<col=" + Color.MEDRED.getColorValue() + ">Grand Mystery Box</col>]:<col=1e44b3> " + player.getUsername() + ""+Color.HOTPINK.tag()+" just received a Mystery chest promo I" + Color.BLACK.tag() + "!";
                World.getWorld().sendWorldMessage(worldMessage);
            } else if (Utils.rollDie(7, 1)) {
                Utils.sendDiscordInfoLog("Player " + player.getUsername() + " received "+ Arrays.toString(OPTION_THREE) +" from a mystery box.", "box_and_tickets");
                player.inventory().addAll(OPTION_THREE);
            } else if (Utils.rollDie(5, 1)) {
                Utils.sendDiscordInfoLog("Player " + player.getUsername() + " received "+ Arrays.toString(OPTION_TWO) +" from a mystery box.", "box_and_tickets");
                player.inventory().addAll(OPTION_TWO);
            } else {
                Utils.sendDiscordInfoLog("Player " + player.getUsername() + " received "+ Arrays.toString(OPTION_ONE) +" from a mystery box.", "box_and_tickets");
                player.inventory().addAll(OPTION_ONE);
            }
        } else {
            player.message("You need at least 6 free inventory slots.");
        }
    }

}
