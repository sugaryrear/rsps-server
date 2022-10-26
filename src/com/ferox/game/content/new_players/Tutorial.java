package com.ferox.game.content.new_players;

import com.ferox.GameServer;
import com.ferox.game.GameConstants;
import com.ferox.game.world.InterfaceConstants;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.*;
import com.ferox.game.world.entity.mob.Flag;
import com.ferox.game.world.entity.mob.player.GameMode;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.entity.mob.player.rights.PlayerRights;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Color;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.NpcIdentifiers;

import static com.ferox.game.GameConstants.*;
import static com.ferox.util.CustomItemIdentifiers.BEGINNER_WEAPON_PACK;
import static com.ferox.util.ItemIdentifiers.*;

public class Tutorial extends Dialogue {

    GameMode accountType = GameMode.TRAINED_ACCOUNT;

    public static void start(Player player) {
//        player.lock();
//        player.looks().hide(true);
//        player.teleport(GameServer.properties().defaultTile);
//        player.getDialogueManager().start(new Tutorial());
    }

    @Override
    protected void start(Object... parameters) {


        send(DialogueType.NPC_STATEMENT, NpcIdentifiers.HANS, Expression.HAPPY, "Welcome to " + GameConstants.SERVER_NAME + "!");
        setPhase(1);
        //System.out.println(getPhase());
    }

    @Override
    protected void next() {
        //   System.out.println(getPhase());
        if (isPhase(1)) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Start the tutorial", "Skip");
            setPhase(2);
        } else if (isPhase(7)) {

            // player.looks().hide(true);
            player.lock();
            player.teleport(new Tile(3096, 3510));
            send(DialogueType.NPC_STATEMENT, NpcIdentifiers.HANS, Expression.CALM_TALK, "All the combat gear you will need", "in your leveling journey can be found here", "no matter where you are in the world");
            setPhase(8);
        } else if (isPhase(8)) {
            send(DialogueType.NPC_STATEMENT, NpcIdentifiers.HANS, Expression.CALM_TALK, "come back here to purchase upgrades","so you don't get owned");
            setPhase(9);
        } else if (isPhase(9)) {
            send(DialogueType.NPC_STATEMENT, NpcIdentifiers.HANS, Expression.CALM_TALK, "After that there are two very effective ways to make money", "early on. Slayer and revenants both are " + Color.RED.wrap("(dangerous)") + ".", "Both money makers are in the wilderness.");
            setPhase(10);
        } else if (isPhase(10)) {
            player.teleport(new Tile(3110,3516));
            send(DialogueType.NPC_STATEMENT, NpcIdentifiers.HANS, Expression.CALM_TALK, "You can find the slayer master here.");
            setPhase(11);
        } else if (isPhase(11)) {
            player.teleport(new Tile(3106,3497));
            send(DialogueType.NPC_STATEMENT, NpcIdentifiers.HANS, Expression.CALM_TALK, "The forging table allows you to craft", "gilded or ornamented version of your favorite items", "with a random chance of failure");
            setPhase(12);
        } else if (isPhase(12)) {
            player.teleport(new Tile(3166,3489));
            send(DialogueType.NPC_STATEMENT, NpcIdentifiers.HANS, Expression.CALM_TALK, "Here you can find the trading post");
            setPhase(13);
        } else if (isPhase(13)) {
            player.teleport(new Tile(3135,3629));
            send(DialogueType.NPC_STATEMENT, NpcIdentifiers.HANS, Expression.CALM_TALK, "Last man standing is the central hub for", "wilderness activity. There are many useful amenities","such as the occult altar.");
            setPhase(14);
        } else if (isPhase(14)) {
            player.teleport(new Tile(3092,3489));
            send(DialogueType.NPC_STATEMENT, NpcIdentifiers.HANS, Expression.CALM_TALK, "Visit any voting booth in a bank", "to access the vote store or claim your votes");
            setPhase(15);
        } else if (isPhase(15)) {
            player.teleport(new Tile(2507,4720));
            send(DialogueType.NPC_STATEMENT, NpcIdentifiers.HANS, Expression.CALM_TALK, "Looking for something from OSRS such as god capes?");
            setPhase(16);
        } else if (isPhase(16)) {
            player.teleport(new Tile(3015,3448));
            send(DialogueType.NPC_STATEMENT, NpcIdentifiers.HANS, Expression.CALM_TALK, "Or maybe dwarf cannon parts?", "");
            setPhase(17);
        } else if (isPhase(17)) {
            send(DialogueType.NPC_STATEMENT, NpcIdentifiers.HANS, Expression.CALM_TALK, "We try to emulate OSRS in order to keep the world lively", "without the boring grind!");
            setPhase(18);//
        } else if (isPhase(18)) {
            send(DialogueType.NPC_STATEMENT, NpcIdentifiers.HANS, Expression.CALM_TALK, "You can find teleports to different locations","by right clicking the minigames tab");
            setPhase(19);//
        } else if (isPhase(19)) {

            send(DialogueType.NPC_STATEMENT, NpcIdentifiers.HANS, Expression.CALM_TALK, "Talk to me in Lumbridge if you need to", "replay this tutorial");


            setPhase(20);//
        } else if (isPhase(20)) {

            player.teleport(GameServer.properties().defaultTile);
            stop();
            player.unlock();
            //   player.looks().hide(false);
            player.getInterfaceManager().close();

        }
    }

    @Override
    protected void select(int option) {
        // System.out.println(getPhase());
        if (getPhase() == 2) {
            if (option == 1) {
                player.teleport(new Tile(3096, 3510));
                player.looks().hide(true);
                player.getPacketSender().disableTabs();

                send(DialogueType.NPC_STATEMENT, NpcIdentifiers.HANS, Expression.CALM_TALK, "All the combat gear you will need", "in your leveling journey can be found here", "no matter where you are in the world");
                setPhase(8);

            } else if (option == 2) {
                player.getInterfaceManager().close();
                stop();
                player.unlock();
                player.looks().hide(false);
            }
        } else if (getPhase() == 6) {
            if (option == 1) {
                if (accountType == GameMode.DARK_LORD) {
                    player.mode(GameMode.DARK_LORD);
                    player.setPlayerRights(PlayerRights.DARK_LORD);
                    player.getPacketSender().sendRights();
                    player.getUpdateFlag().flag(Flag.APPEARANCE);
                    player.putAttrib(AttributeKey.DARK_LORD_LIVES,3);
                } else if(accountType == GameMode.TRAINED_ACCOUNT) {
                    player.mode(GameMode.TRAINED_ACCOUNT);
                    StarterBox.claimStarterBox(player);
                } else {
                    player.mode(GameMode.INSTANT_PKER);
                    StarterBox.claimStarterBox(player);
                    //Max out combat
                    for (int skill = 0; skill < 7; skill++) {
                        player.skills().setXp(skill, Skills.levelToXp(99));
                        player.skills().update();
                        player.skills().recalculateCombat();
                    }
                }

                if(accountType == GameMode.DARK_LORD) {
                    player.putAttrib(AttributeKey.DARK_LORD_MELEE_TIER,1);
                    player.putAttrib(AttributeKey.DARK_LORD_RANGE_TIER,1);
                    player.putAttrib(AttributeKey.DARK_LORD_MAGE_TIER,1);
                    player.getEquipment().manualWear(new Item(IRONMAN_HELM),true);
                    player.getEquipment().manualWear(new Item(IRONMAN_PLATEBODY),true);
                    player.getEquipment().manualWear(new Item(IRONMAN_PLATELEGS),true);
                }

                if(accountType != GameMode.INSTANT_PKER) {
                    player.message("You have been given some training equipment.");
                    Item[] training_equipment = {
                        new Item(ItemIdentifiers.BRONZE_ARROW, 10_000),
                        new Item(ItemIdentifiers.IRON_KNIFE, 10_000),
                        new Item(ItemIdentifiers.AIR_RUNE, 10_000),
                        new Item(ItemIdentifiers.MIND_RUNE, 10_000),
                        new Item(ItemIdentifiers.CHAOS_RUNE, 10_000),
                        new Item(ItemIdentifiers.WATER_RUNE, 10_000),
                        new Item(ItemIdentifiers.EARTH_RUNE, 10_000),
                        new Item(ItemIdentifiers.FIRE_RUNE, 10_000),
                        new Item(ItemIdentifiers.LOBSTER+1, 10_000),
                        new Item(ItemIdentifiers.STAFF_OF_AIR, 1),
                        new Item(ItemIdentifiers.SHORTBOW, 1),
                        new Item(ItemIdentifiers.IRON_SCIMITAR, 1),
                        new Item(ItemIdentifiers.IRON_FULL_HELM, 1),
                        new Item(ItemIdentifiers.IRON_PLATEBODY, 1),
                        new Item(ItemIdentifiers.IRON_PLATELEGS, 1),
                        new Item(ItemIdentifiers.CLIMBING_BOOTS, 1),
                        new Item(ItemIdentifiers.BLUE_WIZARD_HAT, 1),
                        new Item(ItemIdentifiers.BLUE_WIZARD_ROBE, 1),
                        new Item(ItemIdentifiers.BLUE_SKIRT, 1),
                        new Item(ItemIdentifiers.LEATHER_BODY, 1),
                        new Item(ItemIdentifiers.LEATHER_CHAPS, 1),
                        new Item(ItemIdentifiers.STAFF_OF_AIR, 1),
                    };
                    player.getInventory().addAll(training_equipment);
                }

                player.getBank().addAll(BANK_ITEMS);
                System.arraycopy(TAB_AMOUNT, 0, player.getBank().tabAmounts, 0, TAB_AMOUNT.length);
                player.getBank().shift();

                send(DialogueType.NPC_STATEMENT, NpcIdentifiers.COMBAT_INSTRUCTOR, Expression.HAPPY, "Let me show you how to get started in " + GameConstants.SERVER_NAME + ".");
                setPhase(7);
            } else {
                send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "I'd like to take my time and earn benefits of the <col=" + Color.BLUE.getColorValue() + ">Trained account</col>.", "I'd like to take my time and earn benefits of the <col=" + Color.BLUE.getColorValue() + ">Dark lord account</col>.", "I want to go straight to action with a <col=" + Color.BLUE.getColorValue() + ">PK account</col>.", "What's the difference between the three?");
                setPhase(2);
            }
        } else if(isPhase(8)) {
            if(option == 1) {
                accountType = GameMode.DARK_LORD;
                send(DialogueType.NPC_STATEMENT, NpcIdentifiers.COMBAT_INSTRUCTOR, Expression.DEFAULT, "Are you sure you wish to play as a Dark Lord (3 lives)?");
                setPhase(5);
            }
        }
    }
}
