package com.ferox.game.content.items.keys;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.combat.skull.SkullType;
import com.ferox.game.world.entity.combat.skull.Skulling;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import com.ferox.util.Utils;

import static com.ferox.game.content.collection_logs.LogType.KEYS;
import static com.ferox.util.CustomItemIdentifiers.WILDERNESS_KEY;
import static com.ferox.util.ItemIdentifiers.*;
import static com.ferox.util.ObjectIdentifiers.DEADMAN_CHEST_27290;

/**
 * The wilderness Key and its rewards.
 * @author Malefique
 * @Since december 01, 2020
 */
public class WildyKey extends PacketInteraction {

    private static final Item[] EXTREMELY_RARE = new Item[]{
        new Item(SERPENTINE_HELM), new Item(TOXIC_STAFF_OF_THE_DEAD), new Item(DRAGON_CLAWS), new Item(BANDOS_TASSETS), new Item(BANDOS_CHESTPLATE), new Item(ARMADYL_GODSWORD), new Item(ARMADYL_CROSSBOW)
    };

    private static final Item[] RARE = new Item[]{
        new Item(ANCIENT_WYVERN_SHIELD), new Item(DRAGONFIRE_WARD), new Item(DINHS_BULWARK), new Item(ELDRITCH_ORB), new Item(STAFF_OF_THE_DEAD), new Item(STAFF_OF_THE_DEAD), new Item(ODIUM_WARD), new Item(MALEDICTION_WARD),
    };

    private static final Item[] UNCOMMON = new Item[]{
        new Item(FREMENNIK_KILT), new Item(BERSERKER_RING), new Item(ARCHERS_RING), new Item(SEERS_RING), new Item(DRAGON_CROSSBOW), new Item(ZAMORAK_GODSWORD), new Item(BANDOS_GODSWORD), new Item(SARADOMIN_GODSWORD)
    };

    private static final Item[] COMMON = new Item[]{
        new Item(DARK_BOW), new Item(DHAROKS_ARMOUR_SET), new Item(KARILS_ARMOUR_SET), new Item(AHRIMS_ARMOUR_SET), new Item(INFINITY_BOOTS), new Item(AMULET_OF_FURY), new Item(OCCULT_NECKLACE), new Item(RANGER_BOOTS), new Item(MAGES_BOOK), new Item(DRAGON_BOOTS), new Item(TOME_OF_FIRE)
    };

    public Item rollReward() {
        var roll = World.getWorld().random(100);
        //Reward rarity to base the server message on
        if (roll >= 90 && roll <= 100) {
            return Utils.randomElement(EXTREMELY_RARE);
        } else if (roll >= 75 && roll <= 89) {
            return Utils.randomElement(RARE);
        } else if (roll >= 50 && roll <= 74) {
            return Utils.randomElement(UNCOMMON);
        } else {
            return Utils.randomElement(COMMON);
        }
    }

    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        //Only perform actions if the object is a deadman chest and the option is 1.
        if (option == 1 && object.getId() == 27269 || object.getId() == DEADMAN_CHEST_27290) {
            //Do stuff here
            player.faceObj(object);

            if (!player.inventory().contains(WILDERNESS_KEY)) {
                player.message("You need a wilderness key to open this chest.");
                return true;
            }

            //Important to lock the player during the action
            player.lock();

            Item rewardOne = rollReward();
            Item rewardTwo = rollReward();

            player.runFn(1, () -> {
                //Generate reward
                if(object.getId() == DEADMAN_CHEST_27290 && object.tile().equals(2337, 9904,1)) {//wildy version of it
                    player.confirmDialogue(new String[]{"Are you sure you wish to open the chest?"}, "", "proceed to open the chest.", "Nevermind.", () -> {
                        if(!player.inventory().contains(WILDERNESS_KEY)) {
                            return;
                        }
                        open(player, rewardOne, rewardTwo,true);
                    });
                    return;
                }

                open(player, rewardOne,null,false);
            });
            return true;
        }
        return false;
    }

    private void open(Player player, Item rewardOne, Item rewardTwo, boolean wilderness) {
        player.message("You unlock the chest with your key.");
        player.sound(51);
        player.animate(536);
        player.inventory().remove(new Item(WILDERNESS_KEY));

        if(wilderness) {
            player.teleblock(250,true);
            Skulling.assignSkullState(player, SkullType.RED_SKULL);
        }

        int keysUsed = player.<Integer>getAttribOr(AttributeKey.WILDY_KEYS_OPENED, 0) + 1;
        player.putAttrib(AttributeKey.WILDY_KEYS_OPENED, keysUsed);

        if(rewardOne != null) {
            boolean amOverOne = rewardOne.getAmount() > 1;
            String amtString = amOverOne ? "x " + Utils.format(rewardOne.getAmount()) + "" : Utils.getAOrAn(rewardOne.name());
            String openedAt = wilderness ? "inside the member zone" : "at home";

            if(!player.getUsername().equalsIgnoreCase("Box test")) {
                String msg = "<img=506><shad=0>[<col=" + Color.MEDRED.getColorValue() + ">Wildy key</col>]</shad>: " + "<col=AD800F>" + player.getUsername() + " has received " + amtString + " " + rewardOne.name() + " " + openedAt + "!";
                World.getWorld().sendWorldMessage(msg);
            }

            //Check if item exists in collection log items
            KEYS.log(player, WILDERNESS_KEY, rewardOne);

            if(wilderness) {
                player.inventory().addOrDrop(rewardOne);
            } else {
                player.inventory().addOrBank(rewardOne);
            }
        }

        if(rewardTwo != null) {
            boolean amOverOne = rewardTwo.getAmount() > 1;
            String amtString = amOverOne ? "x " + Utils.format(rewardTwo.getAmount()) + "" : Utils.getAOrAn(rewardTwo.name());
            String openedAt = wilderness ? "inside the member zone" : "at home";

            //The user box test doesn't yell.
            if(!player.getUsername().equalsIgnoreCase("Box test")) {
                String msg = "<img=506><shad=0>[<col=" + Color.MEDRED.getColorValue() + ">Wildy key</col>]</shad>: " + "<col=AD800F>" + player.getUsername() + " has received " + amtString + " " + rewardTwo.name() + " " + openedAt + "!";
                World.getWorld().sendWorldMessage(msg);
            }

            //Check if item exists in collection log items
            KEYS.log(player, WILDERNESS_KEY, rewardTwo);

            if(wilderness) {
                player.inventory().addOrDrop(rewardTwo);
            } else {
                player.inventory().addOrBank(rewardTwo);
            }
        }

        //And unlock the player
        player.unlock();
    }
}
