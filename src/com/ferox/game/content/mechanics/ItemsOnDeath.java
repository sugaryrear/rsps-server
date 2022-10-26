package com.ferox.game.content.mechanics;

import com.ferox.GameServer;
import com.ferox.fs.ItemDefinition;
import com.ferox.game.content.areas.wilderness.content.key.WildernessKeyPlugin;
import com.ferox.game.content.areas.wilderness.content.revenant_caves.AncientArtifacts;
import com.ferox.game.content.items_kept_on_death.ItemsKeptOnDeath;
import com.ferox.game.content.mechanics.break_items.BrokenItem;
import com.ferox.game.content.minigames.MinigameManager;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.bountyhunter.emblem.BountyHunterEmblem;
import com.ferox.game.world.entity.combat.method.impl.npcs.bosses.zulrah.Zulrah;
import com.ferox.game.world.entity.combat.method.impl.npcs.godwars.zaros.Nex;
import com.ferox.game.world.entity.combat.prayer.default_prayer.DefaultPrayers;
import com.ferox.game.world.entity.combat.skull.SkullType;
import com.ferox.game.world.entity.combat.skull.Skulling;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.npc.pets.Pet;
import com.ferox.game.world.entity.mob.player.GameMode;
import com.ferox.game.world.entity.mob.player.IronMode;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.rights.PlayerRights;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.ground.GroundItem;
import com.ferox.game.world.items.ground.GroundItemHandler;
import com.ferox.game.world.position.Boundary;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.net.packet.incoming_packets.PickupItemPacketListener;
import com.ferox.test.unit.IKODTest;
import com.ferox.test.unit.PlayerDeathConvertResult;
import com.ferox.test.unit.PlayerDeathDropResult;
import com.ferox.util.Color;
import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.CustomNpcIdentifiers;
import com.ferox.util.Utils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ferox.game.world.entity.AttributeKey.BRACELET_OF_ETHEREUM_CHARGES;
import static com.ferox.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | June, 27, 2021, 12:56
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class ItemsOnDeath {

    private static final Logger playerDeathLogs = LogManager.getLogger("PlayerDeathsLogs");
    private static final Level PLAYER_DEATHS;

    static {
        PLAYER_DEATHS = Level.getLevel("PLAYER_DEATHS");
    }

    /**
     * The items the Player lost.
     */
    private static final List<Item> lostItems = new ArrayList<>();

    /**
     * If our account has the ability for the custom Pet Shout mechanic - where when you kill someone
     * your pet will shout something.
     */
    public static boolean hasShoutAbility(Player player) {
        // Are we a user with the mechanic enabled
        return player.getAttribOr(AttributeKey.PET_SHOUT_ABILITY, false);
    }

    /**
     * Calculates and drops all of the items from {@code player} for {@code killer}.
     * @return
     */
    public static PlayerDeathDropResult droplootToKiller(Player player, Mob killer) {
        var in_tournament = player.inActiveTournament() || player.isInTournamentLobby();
        var donator_zone = player.tile().region() == 13462;
        var vorkath_area = player.tile().region() == 9023;
        var hydra_area = player.tile().region() == 5536;
//        var zulrah_area = player.tile().region() == 9007 || player.tile().region() == 9008;
        var safe_accounts = player.getUsername().equalsIgnoreCase("Box test");
        var duel_arena = player.getDueling().inDuel() || player.getDueling().endingDuel();
        var pest_control = player.tile().region() == 10536;
        var nex = Nex.getENCAMPMENT().contains(player);
        var zulrah = player.tile().region() == 9007 || player.tile().region() == 9008;
        var raids_area = player.isInsideRaids();
        var minigame_safe_death = player.getMinigame() != null && player.getMinigame().getType().equals(MinigameManager.ItemType.SAFE);
        var hunleff_area = player.tile().region() == 6810;
var safepk = player.tile().inArea(2434,3079, 2444,3100);
        if(player.mode() == GameMode.DARK_LORD) {
            stripDarkLordRank(player);
        }

        // If we're in FFA clan wars, don't drop our items.
        // Have these safe area checks before we do some expensive code ... looking for who killed us.
        if (donator_zone || safepk || vorkath_area/* || zulrah_area*/ /*|| hydra_area*/ || safe_accounts || duel_arena || pest_control || raids_area || in_tournament || minigame_safe_death || hunleff_area) {
            playerDeathLogs.log(PLAYER_DEATHS, "Player: "+ player.getUsername() + " died in a safe area " + (killer != null && killer.isPlayer() ? " to " + killer.toString() : ""));
            Utils.sendDiscordInfoLog("Player: "+ player.getUsername() + " died in a safe area " + (killer != null && killer.isPlayer() ? " to " + killer.toString() : ""), "playerdeaths");
            Utils.sendDiscordInfoLog("Safe deaths activated for: "+ player.getUsername() + "" + (killer != null && killer.isPlayer() ? " to " + killer.toString() : ""+" donator_zone: "+donator_zone+" vorkath_area: "+vorkath_area+" hydra_area: "+hydra_area+"in safe_accounts: "+safe_accounts+" duel_arena: "+duel_arena+" pest_control: "+pest_control+" raids_area: "+raids_area+" in_tournament: "+in_tournament+" minigame_safe_death: "+minigame_safe_death+" hunleff_area: "+hunleff_area), "playerdeaths");
            return null;
        }

        // If it's not a safe death, turn a Hardcore Ironman into a regular.
        if (player.ironMode() == IronMode.HARDCORE) {
            stripHardcoreRank(player);
        }

        // Past this point.. we're in a dangerous zone! Drop our items....

        Player theKiller = killer == null || killer.isNpc() ? player : killer.getAsPlayer();

        final Tile tile = player.tile();

        // Game Lists
        LinkedList<Item> toDrop = new LinkedList<>();
        List<Item> keep = new LinkedList<>();
        List<Item> toDropPre = new LinkedList<>();

        // Unit Testing Lists
        List<Item> outputDrop = new ArrayList<>(toDrop.size());
        List<Item> outputKept = new ArrayList<>(1);
        List<Item> outputDeleted = new ArrayList<>(0);
        List<PlayerDeathConvertResult> outputConverted = new ArrayList<>(0);



player.getEquipment().stream().filter(e-> e != null && e.rawtradable() && e.getId() != -1).forEach(toDropPre::add);//only adds toDropPre if its tradeable
//for(Item item : toDropPre){
//    player.message(item.getId()+"");
//}

        player.inventory().forEach(item -> {
            if (!item.matchesId(LOOTING_BAG) && !item.matchesId(LOOTING_BAG_22586) && !item.matchesId(RUNE_POUCH)) { // always lost
                toDropPre.add(item);
            } else {
                outputDeleted.add(item); // looting bag goes into deleted
            }
        });
//        player.getEquipment().clear(); // everything gets cleared no matter what
        player.getEquipment().cleartradeable();
        player.inventory().clear();

        toDrop.addAll(toDropPre);

        // Any extra custom logic here for alwaysKept under special circumstances
        List<Item> keptPets = toDrop.stream().filter(i -> {
            Pet petByItem = Pet.getPetByItem(i.getId());
            if (petByItem == null)
                return false;
            boolean canTransfer = petByItem.varbit == -1;
            boolean loseByTransfer = player.getSkullType() == SkullType.RED_SKULL;
            return !(canTransfer && loseByTransfer); // this will be lost on death
        }).collect(Collectors.toList());
        keep.addAll(keptPets);
        for (Item keptPet : keptPets) {
            toDrop.remove(keptPet);
        }

        //System.out.println("Dropping: " + Arrays.toString(toDrop.toArray()));

        // remove always kept before calculating kept-3 by value
        List<Item> alwaysKept = toDrop.stream().filter(ItemsKeptOnDeath::alwaysKept).collect(Collectors.toList());
        IKODTest.debug("death alwaysKept list : " + Arrays.toString(alwaysKept.stream().map(Item::toShortValueString).toArray()));
        keep.addAll(alwaysKept);
        toDrop.removeIf(ItemsKeptOnDeath::alwaysKept);
        int amtofcharges = player.getAttribOr(BRACELET_OF_ETHEREUM_CHARGES, 0);
        // custom always lost
        final List<Item> alwaysLostSpecial = toDrop.stream().filter(i -> i.getId() == RUNE_POUCH || i.getId() == BRACELET_OF_ETHEREUM|| i.getId() == LOOTING_BAG || i.getId() == LOOTING_BAG_22586).collect(Collectors.toList());
        for (Item item : alwaysLostSpecial) {
            toDrop.remove(item); // not included in kept-3 if unskulled
            Item currency;
            if(GameServer.properties().pvpMode) {
                currency = new Item(BLOOD_MONEY, item.getId() == LOOTING_BAG || item.getId() == LOOTING_BAG_22586 ? 1250 : 2500);
            } else {
                currency = new Item(BLOOD_MONEY, item.getId() == LOOTING_BAG || item.getId() == LOOTING_BAG_22586 ? 2500 : 2500);//blood money value? on death
            }
            if(item.getId() == BRACELET_OF_ETHEREUM)
                currency = new Item(REVENANT_ETHER, amtofcharges);

            outputDrop.add(currency); // this list isn't whats dropped its for logging
            GroundItemHandler.createGroundItem(new GroundItem(currency, player.tile(), theKiller)); // manually drop it here
        }

        // Sort remaining lost items by value.
        toDrop.sort((o1, o2) -> {
            o1 = o1.unnote();
            o2 = o2.unnote();

            ItemDefinition def = o1.definition(World.getWorld());
            ItemDefinition def2 = o2.definition(World.getWorld());

            int v1 = 0;
            int v2 = 0;

            if (def != null) {
                v1 = o1.getValue();
                if (v1 <= 0 && !GameServer.properties().pvpMode) {
            //        v1 = o1.getBloodMoneyPrice().value();
                    v1 = o1.getValue();
                }
            }
            if (def2 != null) {
                v2 = o2.getValue();
                if (v2 <= 0 && !GameServer.properties().pvpMode) {
 //                   v2 = o2.getBloodMoneyPrice().value();
                    v2 = o2.getValue();


                }
            }

            return Integer.compare(v2, v1);
        });
        int keptItems = (Skulling.skulled(player) ? 0 : 3);

        // On Ultimate Iron Man, you drop everything!
        if (player.ironMode() == IronMode.ULTIMATE) {
            keptItems = 0;
        }

        boolean protection_prayer = DefaultPrayers.usingPrayer(player, DefaultPrayers.PROTECT_ITEM);
        if (protection_prayer) {
            keptItems++;
        }

        //#Update as of 16/02/2021 when smited you're actually smited the pet effect will not work!
        var reaper = player.hasPetOut("Grim Reaper pet") || player.hasPetOut("Blood Reaper pet");
        if (reaper && protection_prayer) {
            keptItems++;
        }
        if (player.getSkullType().equals(SkullType.RED_SKULL) || player.mode().isDarklord()) {
            keptItems = 0;
        }
        IKODTest.debug("keeping " + keptItems + " items");

        while (keptItems-- > 0 && !toDrop.isEmpty()) {
            Item head = toDrop.peek();
            if (head == null) {
                keptItems++;
                toDrop.poll();
                continue;
            }
            keep.add(new Item(head.getId(), 1));

            //Always drop wildy keys
            if(head.getId() == CustomItemIdentifiers.WILDERNESS_KEY) {
                if (WildernessArea.inWilderness(player.tile())) {
                    player.inventory().remove(CustomItemIdentifiers.WILDERNESS_KEY, Integer.MAX_VALUE);
                    PickupItemPacketListener.respawn(Item.of(CustomItemIdentifiers.WILDERNESS_KEY), tile, 3);
                    WildernessKeyPlugin.announceKeyDrop(player, tile);
                    keep.remove(head);
                }
            }

            if (head.getAmount() == 1) { // Amount 1? Remove the item entirely.
                Item delete = toDrop.poll();
                IKODTest.debug("kept " + delete.toShortString());
            } else { // Amount more? Subtract one amount.
                int index = toDrop.indexOf(head);
                toDrop.set(index, new Item(head, head.getAmount() - 1));
                IKODTest.debug("kept " + toDrop.get(index).toShortString());
            }
        }
        for (Item item : keep) {
          //  if(GameServer.properties().pvpMode) {//Only in PvP worlds
                // Handle item breaking..
                BrokenItem brokenItem = BrokenItem.get(item.getId());
                if (brokenItem != null) {
                    player.getPacketSender().sendMessage("Your " + item.unnote().name() + " has been broken. You can fix it by talking to").sendMessage("Perdu who is located in Lumbridge.");
                    item.setId(brokenItem.brokenItem);

                    //Drop bm for the killer
                    GroundItem groundItem = new GroundItem(new Item(BLOOD_MONEY, (int) brokenItem.bmDrop), player.tile(), theKiller);
                    GroundItemHandler.createGroundItem(groundItem);
                }
          //  }
            player.inventory().add(item, true);
        }

        // Looting bag items are NOT in top-3 kept from prot item/unskulled. Always lost.
        if (outputDeleted.stream().anyMatch(i -> i.getId() == LOOTING_BAG || i.getId() == LOOTING_BAG_22586)) {
            Item[] lootingBag = player.getLootingBag().toNonNullArray(); // bypass check if carrying bag since inv is cleared above
            toDrop.addAll(Arrays.asList(lootingBag));
            playerDeathLogs.log(PLAYER_DEATHS,  player.getUsername() + " (Skulled: " + Skulling.skulled(player) + ") looting bag lost items: " + Arrays.toString(Arrays.asList(lootingBag).toArray()) + (killer != null && killer.isPlayer() ? " to " + killer.getMobName() : ""));
            Utils.sendDiscordInfoLog(player.getUsername() + " (Skulled: " + Skulling.skulled(player) + ") looting bag lost items: " + Arrays.toString(Arrays.asList(lootingBag).toArray()) + (killer != null && killer.isPlayer() ? " to " + killer.getMobName() : ""), "playerdeaths");

            player.getLootingBag().clear();
            IKODTest.debug("looting bag had now: " + Arrays.toString(Arrays.asList(lootingBag).toArray()));
        }

        // Rune pouch items are NOT in top-3 kept from prot item/unskulled. Always lost.
        Item[] runePouch = player.getRunePouch().toNonNullArray(); // bypass check if carrying pouch since inv is cleared above
        toDrop.addAll(Arrays.asList(runePouch));
        player.getRunePouch().clear();
        IKODTest.debug("rune pouch had now: " + Arrays.toString(Arrays.asList(runePouch).toArray()));

        if (player.hasPetOut("Niffler")) {
            //Get the current stored item list
            var nifflerItemsStored = player.<ArrayList<Item>>getAttribOr(AttributeKey.NIFFLER_ITEMS_STORED, new ArrayList<Item>());
            if (nifflerItemsStored != null) {
                toDrop.addAll(nifflerItemsStored);
                playerDeathLogs.log(PLAYER_DEATHS,  player.getUsername() + " (Skulled: " + Skulling.skulled(player) + ") niffler lost items: " + Arrays.toString(nifflerItemsStored.toArray()) + (killer != null && killer.isPlayer() ? " to " + killer.getMobName() : ""));
                Utils.sendDiscordInfoLog(player.getUsername() + " (Skulled: " + Skulling.skulled(player) + ") niffler lost items: " + Arrays.toString(nifflerItemsStored.toArray()) + (killer != null && killer.isPlayer() ? " to " + killer.getMobName() : ""), "playerdeaths");

                nifflerItemsStored.clear();
                IKODTest.debug("niffler had now: " + Arrays.toString(nifflerItemsStored.toArray()));
            }
        }

        lostItems.clear();
        IKODTest.debug("Dropping now: " + Arrays.toString(toDrop.stream().map(Item::toShortString).toArray()));

        outputKept.addAll(keep);
        IKODTest.debug("Kept-3: " + Arrays.toString(keep.stream().map(Item::toShortString).toArray()));

        Mob lastAttacker = player.getAttribOr(AttributeKey.LAST_DAMAGER,null);
        final boolean npcFlag = lastAttacker != null && lastAttacker.isNpc() && lastAttacker.getAsNpc().getBotHandler() != null;

        LinkedList<Item> toDropConverted = new LinkedList<>();

        toDrop.forEach(item -> {
            if(item.getId() == CustomItemIdentifiers.WILDERNESS_KEY) {
                if (WildernessArea.inWilderness(player.tile())) {
                    player.inventory().remove(CustomItemIdentifiers.WILDERNESS_KEY, Integer.MAX_VALUE);
                    PickupItemPacketListener.respawn(Item.of(CustomItemIdentifiers.WILDERNESS_KEY), tile, 3);
                    WildernessKeyPlugin.announceKeyDrop(player, tile);
                    return;
                }
            }



            if (item.getId() == AncientArtifacts.ANCIENT_EFFIGY.getItemId()
                || item.getId() == AncientArtifacts.ANCIENT_EMBLEM.getItemId()
                || item.getId() == AncientArtifacts.ANCIENT_MEDALLION.getItemId()
                || item.getId() == AncientArtifacts.ANCIENT_RELIC.getItemId()
                || item.getId() == AncientArtifacts.ANCIENT_STATUETTE.getItemId()
                || item.getId() == AncientArtifacts.ANCIENT_TOTEM.getItemId()) {
                GroundItemHandler.createGroundItem(new GroundItem(new Item(item.getId()), player.tile(), theKiller));
                outputDrop.add(new Item(item.getId()));
                // dont add to toDropConverted, we're manually dropping it
                return;
            }

            //Drop emblems but downgrade them a tier.
            if (item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_1.getItemId()
                || item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_2.getItemId() ||
                item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_3.getItemId() ||
                item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_4.getItemId() ||
                item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_5.getItemId() ||
                item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_6.getItemId() ||
                item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_7.getItemId() ||
                item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_8.getItemId() ||
                item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_9.getItemId() ||
                item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_10.getItemId()) {

                //Tier 1 shouldnt be dropped cause it cant be downgraded
                if (item.matchesId(BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_1.getItemId())) {
                    return;
                }

                final int lowerEmblem = item.getId() - 2;

                ItemDefinition def = World.getWorld().definitions().get(ItemDefinition.class, lowerEmblem);
                GroundItemHandler.createGroundItem(new GroundItem(new Item(lowerEmblem), player.tile(), theKiller));
                theKiller.message("<col=ca0d0d>" + player.getUsername() + " dropped a " + def.name + "!");
                outputDrop.add(new Item(lowerEmblem));
                // dont add to toDropConverted, we're manually dropping it
                return;
            }

            // IKODTest.debug("dc2: "+item.toShortString());

            // if we've got to here, add the original or changed SINGLE item to the newer list
            toDropConverted.add(item);
        });
        // replace the original list with the newer list which reflects changes
        toDrop = toDropConverted;

        // Dropping in-game the finalized items list on death here
        toDrop.forEach(item -> {
            //   IKODTest.debug("dropping check: "+item.toShortString());

            if (ItemsKeptOnDeath.alwaysKept(item)) {
                //System.out.println("Autokeep");
                //QOL OSRS doesn't drop them anymore but spawns in inventory.
                player.inventory().add(item);
                outputKept.add(item);
                return;
            }

            //Drop item
            //System.out.println("Creating ground item " + item.getId());
            //Add the items to the lost list regardless of if the player died to a bot.
            lostItems.add(item);

            boolean diedToSelf = theKiller == player;
            //System.out.println("died to npc "+npcFlag+" or died to self "+diedToSelf);
            boolean nifflerShouldLoot = !diedToSelf && !npcFlag;
            //System.out.println("nifflerShouldLoot "+nifflerShouldLoot);

            //Niffler should only pick up items of monsters and players that you've killed.
            if(theKiller.nifflerPetOut() && theKiller.nifflerCanStore() && nifflerShouldLoot) {
                if (item.getValue() > 0) {
                    theKiller.nifflerStore(item);
                }
            } else {
                if(!nex && !zulrah) {
                    GroundItem g = new GroundItem(item, player.tile(), theKiller);

                        GroundItemHandler.createGroundItem(g);
                        g.pkedFrom(player.getUsername()); // Mark item as from PvP to avoid ironmen picking it up.

                }
            }

            outputDrop.add(item);

        });
//        for (Item item : outputDrop){
//            player.message("item: "+item.getId());
//        }
        if(nex)
            player.getNexLostItems().store(outputDrop);
        if(zulrah)
            player.getZulrahLostItems().store(outputDrop);

        GroundItemHandler.createGroundItem(new GroundItem(new Item(BONES), player.tile(), theKiller));
        outputDrop.add(new Item(BONES));
        playerDeathLogs.log(PLAYER_DEATHS,  player.getUsername() + " (Skulled: " + Skulling.skulled(player) + ") lost items: " + Arrays.toString(lostItems.stream().map(Item::toShortString).toArray()) + (killer != null && killer.isPlayer() ? " to " + killer.getMobName() : ""));
        Utils.sendDiscordInfoLog(player.getUsername() + " (Skulled: " + Skulling.skulled(player) + ") lost items: " + Arrays.toString(lostItems.stream().map(Item::toShortString).toArray()) + (killer != null && killer.isPlayer() ? " to " + killer.getMobName() : ""), "playerdeaths");
        //Reset last attacked by, since we already handled it above, and the player is already dead.
        player.clearAttrib(AttributeKey.LAST_DAMAGER);
        return new PlayerDeathDropResult(theKiller, outputDrop, outputKept, outputDeleted, outputConverted);
    }

    private static void stripHardcoreRank(Player player) {
        player.ironMode(IronMode.REGULAR); // Revert mode
        if(!player.getPlayerRights().isStaffMemberOrYoutuber(player)) {
            player.setPlayerRights(PlayerRights.IRON_MAN);
            player.getPacketSender().sendRights();
        }
        World.getWorld().sendWorldMessage("<img=504>"+Color.RED.wrap("[Hardcore fallen]:")+" "+Color.BLUE.wrap(player.getUsername())+" has fallen as a Hardcore Iron Man!");
        player.message("You have fallen as a Hardcore Iron Man', your Hardcore status has been revoked.");
    }

    private static void stripDarkLordRank(Player player) {
        var lives = player.<Integer>getAttribOr(AttributeKey.DARK_LORD_LIVES,3) - 1;
        player.putAttrib(AttributeKey.DARK_LORD_LIVES, lives);
        if(lives == 0) {
            if (!player.getPlayerRights().isStaffMemberOrYoutuber(player)) {
                player.setPlayerRights(PlayerRights.PLAYER);
                player.getPacketSender().sendRights();
            }
            player.mode(GameMode.TRAINED_ACCOUNT);
            player.message("You have fallen as a Dark Lord', your status has been revoked.");
            World.getWorld().sendWorldMessage("<img=1081>" + Color.PURPLE.wrap(player.getUsername()) + Color.RED.wrap("has fallen as a Dark Lord!"));
        }
    }
}
