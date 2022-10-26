package com.ferox.game.content.raids.chamber_of_secrets;

import com.ferox.game.content.raids.party.Party;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.npc.pets.Pet;
import com.ferox.game.world.entity.mob.npc.pets.PetAI;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.loot.LootItem;
import com.ferox.game.world.items.loot.LootTable;
import com.ferox.util.Color;
import com.ferox.util.Utils;

import static com.ferox.game.content.collection_logs.CollectionLog.RAIDS_KEY;
import static com.ferox.game.content.collection_logs.LogType.*;
import static com.ferox.game.content.raids.chamber_of_secrets.ChamberOfSecrets.REWARD_WIDGET;
import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | May, 13, 2021, 12:29
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class ChamberOfSecretsReward {

    public static void unlockOlmlet(Player player) {
        RAIDS.log(player, RAIDS_KEY, new Item(Pet.OLMLET.item));
        if (!PetAI.hasUnlocked(player, Pet.OLMLET)) {
            // Unlock the varbit. Just do it, rather safe than sorry.
            player.addUnlockedPet(Pet.OLMLET.varbit);

            // RS tries to add it as follower first. That only works if you don't have one.
            var currentPet = player.pet();
            if (currentPet == null) {
                player.message("You have a funny feeling like you're being followed.");
                PetAI.spawnPet(player, Pet.OLMLET, false);
            } else {
                // Sneak it into their inventory. If that fails, fuck you, no pet for you!
                if (player.inventory().add(new Item(Pet.OLMLET.item), true)) {
                    player.message("You feel something weird sneaking into your backpack.");
                } else {
                    player.message("Speak to Probita to claim your pet!");
                }
            }

            World.getWorld().sendWorldMessage("<img=1081>" + player.getUsername() + " has unlocked the pet: <col=" + Color.HOTPINK.getColorValue() + ">" + new Item(Pet.OLMLET.item).name() + "</col>.");
        } else {
            player.message("You have a funny feeling like you would have been followed...");
        }
    }

    public static void withdrawReward(Player player) {
        var rareReward = player.<Item>getAttribOr(AttributeKey.RAIDS_RARE_ITEM,null);
        if(rareReward != null) {
            String worldMessage = "<img=1081>[<col=" + Color.RAID_PURPLE.getColorValue() + ">Chambers of Secrets</col>]</shad></col>: " + Color.BLUE.wrap(player.getUsername()) + " received " + Utils.getAOrAn(rareReward.unnote().name()) + " <shad=0><col=AD800F>" + rareReward.unnote().name() + "</shad>!";
          //  World.getWorld().sendWorldMessage(worldMessage);
        }

        player.inventory().addOrBank(player.getRaidRewards().getItems());
        for (Item item : player.getRaidRewards().getItems()) {
            if(item == null)
                continue;
            if (ChamberOfSecretsReward.uniqueTable.allItems().stream().anyMatch(i -> item.matchesId(item.getId()))) {
                Utils.sendDiscordInfoLog("Rare drop collected: " + player.getUsername() + " withdrew " + item.unnote().name() + " ", "raids");
            }
        }

        player.putAttrib(AttributeKey.RAIDS_RARE_ITEM,null);
        player.getRaidRewards().clear();

        //Roll for pet
        if (World.getWorld().rollDie(650, 1)) {
            unlockOlmlet(player);
        }
    }

    public static void displayRewards(Player player) { // shows
        int totalRewards = player.getRaidRewards().getItems().length;

        // clear slots
        player.getPacketSender().sendItemOnInterfaceSlot(12022, null, 0);
        player.getPacketSender().sendItemOnInterfaceSlot(12023, null, 0);
        player.getPacketSender().sendItemOnInterfaceSlot(12024, null, 0);

        player.getInterfaceManager().open(REWARD_WIDGET);

        if (totalRewards >= 1) {
            Item reward1 = player.getRaidRewards().getItems()[0];
            player.getPacketSender().sendItemOnInterfaceSlot(12022, reward1, 0);
        }

        if (totalRewards >= 2) {
            Item reward2 = player.getRaidRewards().getItems()[1];
            player.getPacketSender().sendItemOnInterfaceSlot(12023, reward2, 0);
        }

       // player.getPacketSender().sendItemOnInterfaceSlot(12024, new Item(DARK_JOURNAL), 0);
    }

    private static final LootTable uniqueTable = new LootTable()
        .addTable(1,
            new LootItem(DRAGON_CLAWS, 1, 6),
            new LootItem(ARCANE_PRAYER_SCROLL, 1, 7),
            new LootItem(TWISTED_BUCKLER, 1, 8),
            new LootItem(DRAGON_HUNTER_CROSSBOW, 1, 7),
            new LootItem(DINHS_BULWARK, 1, 6),
            new LootItem(ANCESTRAL_HAT, 1, 6),
            new LootItem(KODAI_INSIGNIA, 1, 6),
            new LootItem(DEXTEROUS_PRAYER_SCROLL, 1, 7),
            new LootItem(TORN_PRAYER_SCROLL, 1, 3),
            new LootItem(ELDER_MAUL, 1, 4),
            new LootItem(ANCESTRAL_ROBE_TOP, 1, 5),
            new LootItem(ANCESTRAL_ROBE_BOTTOM, 1, 5),
            new LootItem(TWISTED_BOW_KIT, 1, 2),
            new LootItem(TWISTED_BOW, 1, 1),
            new LootItem(TWISTED_ANCESTRAL_COLOUR_KIT, 1, 8),
                        new LootItem(METAMORPHIC_DUST, 1, 6)

        );

    private static final LootTable regularTable = new LootTable()
        .addTable(1,

            new LootItem(ONYX, 1, 6),

            new LootItem(DEATH_RUNE, World.getWorld().random(1, 3640), 6),
            new LootItem(BLOOD_RUNE, World.getWorld().random(1, 3500), 6),
            new LootItem(WRATH_RUNE, World.getWorld().random(1, 3500), 6),
            new LootItem(SOUL_RUNE, World.getWorld().random(1, 6553), 6),
            new LootItem(RUNE_ARROW, World.getWorld().random(1, 3500), 6),
            new LootItem(DRAGON_ARROW, World.getWorld().random(1, 648), 6),
            new LootItem(DARK_RELIC, 1, 6),
            new LootItem(BLOOD_MONEY, World.getWorld().random(15000, 35000), 6),
            new LootItem(DRAGON_CROSSBOW, 1, 6),
            new LootItem(DRAGON_THROWNAXE, World.getWorld().random(125, 250), 5),
            new LootItem(DRAGON_KNIFE, World.getWorld().random(125, 250), 5)

        );

    public static void giveRewards(Player player) {
        //uniques
        int personalPoints = player.getAttribOr(AttributeKey.PERSONAL_POINTS,0);

        if (personalPoints <= 10_000) {//Can't get any loot if below 10,000 points
            player.message("You need at least 10k points to get a drop from Raids.");
            return;
        }

        if(personalPoints > 100_000) {
            personalPoints = 100_000;
        }

        Party party = player.raidsParty;
        if (party == null)
            return;
        double chance = (float) personalPoints / 100 / 100.0;
        if(party.lastboss){
            chance = ((float) personalPoints / 100 / 100.0 ) * 0.3;
        }

        //System.out.println(chance);
        Player rare = null;

        if (Utils.percentageChance((int) chance)) {
            Item item = rollUnique();
            boolean added = player.getRaidRewards().add(item);
            RAIDS.log(player, RAIDS_KEY, item);
            player.putAttrib(AttributeKey.RAIDS_RARE_ITEM, item);
            Utils.sendDiscordInfoLog("Rare drop: " + player.getUsername() + " Has just received " + item.unnote().name() + " from Chambers of Xeric! Party Points: " + Utils.formatNumber(personalPoints)+" [debug: added="+added+"]", "raids");
            rare = player;
        }

        //Only give normal drops when you did not receive any rares.
        //regular drops
        if(player != rare) {
            Item item = rollRegular();
            player.getRaidRewards().add(item);
            Utils.sendDiscordInfoLog("Regular Drop: " + player.getUsername() + " Has just received " + item.unnote().name() + " from Chambers of Secrets! Personal Points: " + Utils.formatNumber(personalPoints), "raids");
        }
    }

    private static Item rollRegular() {
        return regularTable.rollItem();
    }

    private static Item rollUnique() {
        return uniqueTable.rollItem();
    }
}
