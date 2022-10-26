package com.ferox.game.world.items.container.shop;

import com.ferox.GameServer;
import com.ferox.game.GameConstants;
import com.ferox.game.content.syntax.EnterSyntax;
import com.ferox.game.task.TaskManager;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.npc.pets.Pet;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.container.ItemContainer;
import com.ferox.game.world.items.container.shop.currency.CurrencyType;
import com.ferox.util.Color;
import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.Utils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.IntStream;

import static com.ferox.util.CustomItemIdentifiers.BIG_CHEST;
import static com.ferox.util.CustomItemIdentifiers.BLOOD_MONEY_CASKET;
import static com.ferox.util.ItemIdentifiers.*;

/**
 * The container that represents a shop players can buy and sell items from.
 *
 * @author lare96 <http://github.com/lare96>
 */
public abstract class Shop {

    private static final Logger shopLogs = LogManager.getLogger("ShopsLogs");
    private static final Level SHOPS_LEVEL;

    static {
        SHOPS_LEVEL = Level.getLevel("SHOPS");
    }

    AddStockTask addStockTask;
    RemoveStockTask removeStockTask;

    /**
     * The id of this shop.
     */
    public int shopId;

    /**
     * The name of this shop.
     */
    public String name;

    /**
     * Can ironman access this shop.
     */
    public boolean noiron;

    /**
     * The current item container which contains the current items from this
     * shop.
     */
    public ItemContainer container;

    /**
     * The currency for this shop.
     */
    public CurrencyType currencyType;

    /**
     * The map of cached shop item identifications and their amounts.
     */
    public Map<Integer, Integer> itemCache;

    /**
     * The set of players that are currently viewing this shop.
     */
    public final Set<Player> players = new HashSet<>();

    public Shop(int shopId, String name, boolean noiron, ItemContainer.StackPolicy policy, CurrencyType currencyType, int capacity) {
        this.shopId = shopId;
        this.name = name;
        this.noiron = noiron;
        this.currencyType = currencyType;
        this.container = new ItemContainer(capacity, policy, new StoreItem[capacity]);
        this.itemCache = new HashMap<>(container.capacity());
    }

    public static void closeShop(Player player) {
        if (!player.getInterfaceManager().isInterfaceOpen(ShopUtility.SHOP_INTERFACE)) {
            return;
        }

        int id = player.getAttribOr(AttributeKey.SHOP, -1);

        Shop store = World.getWorld().shop(id);

        if (store == null) {
            return;
        }

        player.debugMessage("Closing store for " + player.toString() + ".");
        store.close(player);
    }

    public static void exchange(Player player, int id, int slot, int action, boolean purchase) {
        if (!player.getInterfaceManager().isInterfaceOpen(ShopUtility.SHOP_INTERFACE) && !player.getInterfaceManager().isInterfaceOpen(ShopUtility.SLAYER_SHOP_INTERFACE)) {
            return;
        }

        int shop = player.getAttribOr(AttributeKey.SHOP, -1);

        Shop store = World.getWorld().shop(shop);

        if (store == null) {
            return;
        }

        store.itemContainerAction(player, id, slot, action, purchase);

        if (action == 5 && shop != 7) {
            player.setEnterSyntax(new EnterSyntax() {
                @Override
                public void handleSyntax(Player player, long input) {
                    player.putAttrib(AttributeKey.STORE_X, (int) input);
                    store.itemContainerAction(player, id, slot, action, purchase);
                }
            });
            String plural = purchase ? "buy" : "sell";
            player.getPacketSender().sendEnterAmountPrompt("How many would you like to " + plural + "?");
        }
    }

    public abstract void itemContainerAction(Player player, int id, int slot, int action, boolean purchase);

    public void purchase(Player player, Item item, int slot) {
        if (!Item.valid(item)) {
            return;
        }

        player.clearAttrib(AttributeKey.STORE_X);

        Optional<Item> find = container.retrieve(slot);

        if (find.isEmpty()) {
            return;
        }

        Item found = find.get();

        if (!(found instanceof StoreItem)) {
            return;
        }

        if (!found.matchesId(item.getId())) {
            player.message("Something went wrong.");
            return;
        }

        StoreItem storeItem = (StoreItem) find.get();

        if (petAlreadyUnlocked(player, item)) {
            player.message("You already have this pet unlocked.");
            return;
        }

        if (storeItem.getAmount() < 1) {
            player.message("There is none of this item left in stock!");
            return;
        }

        if (item.getAmount() > storeItem.getAmount() && shopId != 7)
            item.setAmount(storeItem.getAmount());

        if (!player.inventory().hasCapacityFor(item)) {
            item.setAmount(player.inventory().remaining());

            if (item.getAmount() == 0) {
                player.message("You don't have enough space in your inventory to buy this item!");
                return;
            }
        }

        int value = storeItem.getShopValue();
        if (IntStream.of(GameConstants.BLOOD_MONEY_SHOPS).anyMatch(id -> shopId == id))
            value = storeItem.bloodmoneyvalue();
        Item itemminusone = new Item(item.getId()-1);
        int coinAmountToGive = (int) Math.floor(itemminusone.definition(World.getWorld()).highAlchValue() * 1.1);
        if(item.noted()){
            value = coinAmountToGive;
        }
        long safetyCost = (1L * value * item.getAmount());
        if (safetyCost > Integer.MAX_VALUE) {
            int safeAmtToBuy = Math.max(0, (Integer.MAX_VALUE / value) - 1);
            item.setAmount(safeAmtToBuy);
        }
        int cost = (value * item.getAmount());
        if (!(currencyType.currency.currencyAmount(player, cost) >= cost)) {
            player.message("You don't have enough " + currencyType.toString() + " to buy this item.");
            return;
        }

        if (player.getInventory().remaining() >= item.getAmount() && !item.stackable()
            || player.getInventory().remaining() >= 1 && item.stackable()
            || player.getInventory().contains(item.getId()) && item.stackable()) {
            boolean canNote = item.noteable();
            int giveNotedItemOrUnnoted = canNote && item.getAmount() > 1 ? item.note().getId() : item.getId();

            if (GameServer.properties().pvpMode) {
                item = new Item(giveNotedItemOrUnnoted, item.getAmount());
            } else {
                item = new Item(item.getId(), item.getAmount());
            }

            if (value > 0 && !currencyType.currency.takeCurrency(player, item.getAmount() * value)) {
                return;
            }

            boolean ignore_amount = (GameServer.properties().pvpMode || shopId == 2) && shopId != 16;

            if (itemCache.containsKey(item.getId()) && container.retrieve(slot).isPresent() && !ignore_amount) {
                if (decrementStock()) {
                    container.retrieve(slot).get().decrementAmountBy(item.getAmount());
                }
            } else if (!itemCache.containsKey(item.getId())) {
                if (decrementStock()) {
                    container.remove(item);
                }
            }
        } else {
            player.message("You don't have enough space in your inventory.");
            return;
        }
        if(shopId == 4) {
         //   System.out.println("here");
//            if (item.getId() != 15300 || item.getId() != 379) {
//                item.setId(item.getId() + 1);
//               // item.setAmount(100);
//            }
           // item.setId(item.getId()+ 1 );
            //  item.setAmount(item.getAmount());
        }
        onPurchase(player, item);


        player.inventory().addOrBank(item);
        if (player.getInterfaceManager().isInterfaceOpen(ShopUtility.SLAYER_SHOP_INTERFACE)) {
            int slayerRewardPoints = player.getAttribOr(AttributeKey.SLAYER_REWARD_POINTS, 0);
            player.getPacketSender().sendString(64014, "Reward Points: " + Utils.formatNumber(slayerRewardPoints));
        }

        shopLogs.log(SHOPS_LEVEL, player.getUsername() + " has bought " + item.unnote().name() + " from a shop for " + Utils.formatNumber((long) item.getAmount() * value) + " " + currencyType.currency.toString());
        Utils.sendDiscordInfoLog(player.getUsername() + " has bought " + item.unnote().name() + " from a shop for " + Utils.formatNumber((long) item.getAmount() * value) + " " + currencyType.currency.toString(), "shops");

        //Don't refresh the shop for one player, refresh it for all players.
        for (Player player1 : this.players) {
            refresh(player1, false);
        }
    }

    public boolean petAlreadyUnlocked(Player player, Item item) {
        Pet pet = Pet.getPetByItem(item.getId());
        return pet != null && player.isPetUnlocked(pet.varbit) && pet.varbit != -1;
    }

    public void onPurchase(Player player, Item item) {
        if (item.getId() == ItemIdentifiers.BABY_CHINCHOMPA_13326) {
            if (!player.isPetUnlocked(Pet.BABY_CHINCHOMPA_YELLOW.varbit)) {
                player.addUnlockedPet(Pet.BABY_CHINCHOMPA_YELLOW.varbit);
            }
        }

        if (item.getId() == ItemIdentifiers.PET_SMOKE_DEVIL) {
            if (!player.isPetUnlocked(Pet.PET_SMOKE_DEVIL.varbit)) {
                player.addUnlockedPet(Pet.PET_SMOKE_DEVIL.varbit);
            }
        }

        if (item.getId() == ItemIdentifiers.HERB_BOX) {
            player.putAttrib(AttributeKey.HERB_BOX_CHARGES,10);
        }

        if(shopId == 4) {
//            if (item.getId() != 15300 || item.getId() != 379) {
//                item.setId(item.getId() + 1);
//               // item.setAmount(100);
//            }
           // item.setId(item.getId()+ 1 );
          //  item.setAmount(item.getAmount());
        }
        if(shopId == 47) {
            if (item.getId() == CANNONBALL) {
                item.setAmount(1000);
            }
        }

        for (SkillcapeHoods skillcapeHoods : SkillcapeHoods.values()) {
            //Check if the item being purchased is a cape with hood.
            if (Arrays.stream(skillcapeHoods.getCapes()).anyMatch(id -> id == item.getId())) {
                //Add the hood in the players inventory if there is space, otherwise bank.
                player.inventory().addOrBank(new Item(skillcapeHoods.getHood()));
            }
        }
    }

    protected final void sell(Player player, Item item, int slot) {
        if (!Item.valid(item)) {
            return;
        }
Item itemminusone = new Item(item.getId()-1);
        final Item inventoryItem = player.inventory().get(slot);

        player.clearAttrib(AttributeKey.STORE_X);

        if (inventoryItem == null) {
            player.message("This item does not exist.");
            return;
        }

        /*if (!item.rawtradable()) {
            player.message("This item can't be sold to shops.");
            return;
        }*/

        if (sellType() == SellType.NONE) {
            player.message("You can't sell items to this shop.");
            return;
        }

        if (item.getId() == BLOOD_MONEY_CASKET || item.getId() == BIG_CHEST || item.getId() == COINS_995 || item.getId() == PLATINUM_TOKEN || item.getId() == BLOOD_MONEY || item.getId() == CustomItemIdentifiers.FEROX_COINS) {
            player.message("You can't sell this item.");
            return;
        }

        for (Item bankItem : GameConstants.BANK_ITEMS) {
            if (bankItem.note().getId() == item.getId()) {
                player.message("You can't sell this item.");
                return;
            }
            if (bankItem.getId() == item.getId()) {
                player.message("You can't sell this item.");
                return;
            }
        }

        if (item.unnote().definition(World.getWorld()).pvpAllowed) {
            player.message("You can't trade spawnable items.");
            return;
        }

        if (Arrays.stream(GameConstants.DONATOR_ITEMS).anyMatch(id -> id == item.getId())) {
            player.message("You can't sell this item.");
            return;
        }

        if (sellType() == SellType.ANY && name.equalsIgnoreCase("General store")) {
            if (item.getValue() <= 0) {
                player.message("You can't sell items to this shop that have no value.");
                return;
            }
        }

        final boolean contains = container.contains(item.unnote().getId());

        if (!contains && sellType() == SellType.CONTAINS) {
            player.message("You can't sell " + item.unnote().name() + " to this shop.");
            return;
        }

        if (!container.hasCapacityFor(item.unnote())) {
            player.message("There is no room in this store for the item you are trying to sell!");
            return;
        }

        if (player.inventory().remaining() == 0 && !currencyType.currency.canRecieveCurrency(player)
            && inventoryItem.getAmount() > 1) {
            player.message("You do not have enough space in your inventory to sell this item!");
            return;
        }

        if (CurrencyType.isCurrency(item.getId())) {
            player.message("You can not sell currency to this shop!");
            return;
        }

        Optional<Item> find = container.search(item.getId());
        int sellValue;
        sellValue = item.getId() == 619 ? 1 : item.getSellValue();

        if(item.noted()){
            sellValue = itemminusone.getSellValue();
        }

        final int amount = player.inventory().count(item.getId());

        if (item.getAmount() > amount && !item.stackable()) {
            item.setAmount(amount);
        } else if (item.getAmount() > inventoryItem.getAmount() && item.stackable()) {
            item.setAmount(inventoryItem.getAmount());
        }

        //Safety
        if (player.inventory().count(item.getId()) < 1) {
            return;
        }

        player.inventory().remove(item, slot);

        if (sellValue > 0) {
            currencyType.currency.recieveCurrency(player, item.getAmount() * sellValue);
            player.message("You sold your " + item.unnote().name() + " for " + Utils.formatNumber((long) item.getAmount() * sellValue) + " " + currencyType.currency.toString() + ".");
            shopLogs.log(SHOPS_LEVEL, player.getUsername() + " has sold " + item.unnote().name() + " for " + Utils.formatNumber((long) item.getAmount() * sellValue) + " " + currencyType.currency.toString() + ".");
            Utils.sendDiscordInfoLog(player.getUsername() + " has sold " + item.unnote().name() + " for " + Utils.formatNumber((long) item.getAmount() * sellValue) + " " + currencyType.currency.toString() + ".", "shops");
        }
        StoreItem converted = new StoreItem(item.getId(), item.getAmount());

        boolean dontAddToContainer = GameServer.properties().pvpMode && shopId != 16;

        if (!dontAddToContainer) {
            if (find.isPresent()) {
                Item found = find.get();
                found.setAmount(found.getAmount() + item.getAmount());
            } else {
                container.add(converted);
            }
        }

        //Don't refresh the shop for one player, refresh it for all players.
        //refresh(player);
        for (Player player1 : this.players) {
            refresh(player1, false);
        }
    }

    public abstract void refresh(Player player, boolean redrawStrings);

    public void startAddStock() {
        if (addStockTask == null) {
            addStockTask = new AddStockTask(this);
            TaskManager.submit(addStockTask);
        }
    }

    public void startRemoveStock() {
        if (removeStockTask == null) {
            removeStockTask = new RemoveStockTask(this);
            TaskManager.submit(removeStockTask);
        }
    }

    protected final void sendSellValue(Player player, int slot) {
        Item item = player.inventory().get(slot);
Item itemminusone = new Item(item.getId()-1);
        if (item == null) {
            return;
        }

        /*if (!item.rawtradable()) {
            player.message("This item can't be sold to shops.");
            return;
        }*/

        if (item.getId() == BLOOD_MONEY_CASKET || item.getId() == BIG_CHEST || item.getId() == COINS_995 || item.getId() == PLATINUM_TOKEN || item.getId() == BLOOD_MONEY || item.getId() == CustomItemIdentifiers.FEROX_COINS) {
            player.message("This item can't be sold to shops.");
            return;
        }

        for (Item bankItem : GameConstants.BANK_ITEMS) {
            if (bankItem.note().getId() == item.getId()) {
                player.message("You can't sell this item.");
                return;
            }
            if (bankItem.getId() == item.getId()) {
                player.message("You can't sell this item.");
                return;
            }
        }

        if (item.unnote().definition(World.getWorld()).pvpAllowed) {
            player.message("You can't trade spawnable items.");
            return;
        }

        if (item.getValue() <= 0) {
            player.message("You can't sell items to this shop that have no value.");
            return;
        }

        if (Arrays.stream(GameConstants.DONATOR_ITEMS).anyMatch(id -> id == item.getId())) {
            player.message("This item can't be sold to shops.");
            return;
        }

        if (CurrencyType.isCurrency(item.getId())) {
            player.message("You can not sell currency to this shop!");
            return;
        }

        final boolean contains = container.contains(item.getId());

        if (!contains && sellType() == SellType.CONTAINS) {
            player.message("You can't sell " + item.unnote().name() + " to this shop.");
            return;
        }

        int value = item.getId() == 619 ? 1 : item.getSellValue();
if(item.noted()){
    value = itemminusone.getSellValue();
}
//        if (value <= 0) {
//            if (this.sellType() != SellType.NONE) {
//                player.message(String.format("%s will buy %s for free!", name, item.unnote().name()));
//            } else {
//                player.message(String.format("%s will not buy any items.", name));
//            }
//            return;
//        }

        final String message = this.sellType() != SellType.NONE ? String.format("%s will buy %s for %s %s.", name,
            item.unnote().name(), Utils.formatNumber(value), currencyType.toString())
            : String.format("%s will not buy any items.", name);
        player.message(message);
    }

    protected void sendPurchaseValue(Player player, int slot) {
        Optional<Item> find = container.retrieve(slot);

        if (find.isEmpty()) {
            return;
        }

        Item item = find.get();


        if (item instanceof StoreItem) {
            int itematthatslot = item.getId();
            StoreItem storeItem = (StoreItem) item;
            Item itemminusone = new Item(itematthatslot-1);
            int coinAmountToGive = (int) Math.floor(itemminusone.definition(World.getWorld()).highAlchValue() * 1.1);
            int value = storeItem.getShopValue();
            if(item.noted()){
                value = coinAmountToGive;

            }
            if (IntStream.of(GameConstants.BLOOD_MONEY_SHOPS).anyMatch(id -> shopId == id))

                value = storeItem.bloodmoneyvalue();


            String message = "The shop will sell " + item.unnote().name() + " for " + (value <= 0 ? "free!" : Utils.formatValue(value) + storeItem.getShopCurrency(this).toString() + ".");
            if (shopId == 47) {
                message ="The shop will sell " + item.unnote().name() + " for " + (value <= 0 ? "free!" : Utils.formatValue(value) + storeItem.getShopCurrency(this).toString() + ".");//Override message
            }
            player.message(message);
        }
    }

    public abstract void open(Player player);

    public abstract void close(Player player);

    public abstract SellType sellType();

    public boolean decrementStock() {
        return true;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Shop))
            return false;
        Shop other = (Shop) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
