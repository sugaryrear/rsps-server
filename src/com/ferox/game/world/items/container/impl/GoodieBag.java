package com.ferox.game.world.items.container.impl;

import com.ferox.game.world.InterfaceConstants;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.container.ItemContainer;
import com.ferox.game.world.items.container.ItemContainerAdapter;
import com.ferox.game.world.items.container.price_checker.PriceChecker;
import com.ferox.util.Utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Optional;

public class GoodieBag extends ItemContainer {



    /** The player instance. */
    public Player player;

    /** The item being searched. */
    public Item searchedItem;

    /** Creates a new <code>PriceChecker<code>. */
    public GoodieBag(Player player) {
        super(28, StackPolicy.STANDARD);
        this.player = player;
        addListener(new GoodieBagListener());
    }

    /** Closes the price checker interface. */
    public void close() {
        player.getGoodieBag().withdrawAll();
        player.getGoodieBag().searchedItem = null;
        player.putAttrib(AttributeKey.GOODIE_BAG_CHECKING, false);
    }

    /** Opens the price checker interface. */
    public void open() {
        refresh();
        player.putAttrib(AttributeKey.GOODIE_BAG_CHECKING, true);
        player.getInterfaceManager().openInventory(InterfaceConstants.GOODIE_BAG, 5063);
    }

    /** Sets the calculating value of the price checker. */
    public void setValue() {
        refresh();
    }

    /** Deposits an item into the price checker. */
    public void deposit(int slot, int amount) {
        Item item = player.inventory().get(slot);
        if (item == null)
            return;

        if (!item.rawtradable()) {
            player.message("This item is untradeable!");
            return;
        }

        int id = item.getId();

        int invAmount = player.inventory().count(id);

        if (invAmount < amount) {
            amount = invAmount;
        }

        setFiringEvents(false);
        add(id, amount);
        player.inventory().remove(item.getId(), amount);
        setFiringEvents(true);
        refresh();
    }
    public void listofitems() {
        ArrayList<Item> theitems = getValidItems();
        for(Item item : theitems){
            player.message("You have: "+item.getId()+" in the goodie bag.");
        }
    }
    public void disperse() {
        ArrayList<Item> theitems = getValidItems();

        if(theitems.size() == 0){
            player.message("There are no items in the goodie bag to give out.");
            return;
        }
      //  if (!player.getPlayerRights().isAdminOrGreater(player)) {
            World.getWorld().givegoodiebag(theitems);
        //}

        for (Item item : getItems()) {
            if (item != null) {
                if (this.remove(item)) {

                }
            }
        }
        refresh();

    }

    /** Withdraws an item from the price checker. */
    public void delete(int itemId, int amount) {
        int slot = getSlot(itemId);
        if (itemId < 0)
            return;

        Item item = get(slot);
        if (item == null || itemId != item.getId()) {
            return;
        }

        int contains = count(itemId);

        if (contains < amount) {
            amount = contains;
        }

        int id = item.getId();
        setFiringEvents(false);
        if (!new Item(id).stackable() && amount > player.inventory().getFreeSlots()) {
            amount = player.inventory().getFreeSlots();
        }

        int slotId = player.inventory().getSlot(id);
        if (slotId != -1) {
            Item i = player.inventory().get(slotId);
            if (Integer.MAX_VALUE - i.getAmount() < amount) {
                amount = Integer.MAX_VALUE - i.getAmount();
                player.message("Your inventory didn't have enough space to withdraw all that!");
            }
        }

        if (remove(item.getId(), amount)) {
            player.inventory().add(id, amount);
            shift();
        }

        setFiringEvents(true);
        refresh();
    }
    /** Withdraws an item from the price checker. */
    public void withdraw(int itemId, int amount) {
        int slot = getSlot(itemId);
        if (itemId < 0)
            return;

        Item item = get(slot);
        if (item == null || itemId != item.getId()) {
            return;
        }

        int contains = count(itemId);

        if (contains < amount) {
            amount = contains;
        }

        int id = item.getId();
        setFiringEvents(false);
        if (!new Item(id).stackable() && amount > player.inventory().getFreeSlots()) {
            amount = player.inventory().getFreeSlots();
        }

        int slotId = player.inventory().getSlot(id);
        if (slotId != -1) {
            Item i = player.inventory().get(slotId);
            if (Integer.MAX_VALUE - i.getAmount() < amount) {
                amount = Integer.MAX_VALUE - i.getAmount();
                player.message("Your inventory didn't have enough space to withdraw all that!");
            }
        }

        if (remove(item.getId(), amount)) {
            player.inventory().add(id, amount);
            shift();
        }

        setFiringEvents(true);
        refresh();
    }

    /** Deposits all the items into the price checker. */
    public void depositAll() {
        Item[] items = player.inventory().toArray();
        for (int slot = 0; slot < items.length; slot++) {
            Item item = items[slot];
            if (item == null) {
                continue;
            }

            deposit(slot, item.getAmount());
        }
        refresh();
    }

    /** Withdraw all the items from the price checker. */
    public void withdrawAll() {
        for (Item item : getItems()) {
            if (item != null) {
                if (this.remove(item)) {
                    player.inventory().add(item, -1, false);
                }
            }
        }
        refresh();
    }

    public boolean buttonActions(int button) {
        switch (button) {
            //Close
            case 79502 -> {
                player.getInterfaceManager().close();
                return true;
            }
            //Deposit all
            case 79509 -> {
                withdrawAll();
                return true;
            }
            case 79505 -> {
                disperse();
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public void sync() {
        refresh(player, InterfaceConstants.GOODIE_BAG_DISPLAY_ID);
    }

    @Override
    public void onRefresh() {
        player.inventory().refresh();
        player.getPacketSender().sendItemOnInterface(InterfaceConstants.INVENTORY_STORE, player.inventory().toArray());

    }

    private final class GoodieBagListener extends ItemContainerAdapter {

        GoodieBagListener() {
            super(player);
        }

        @Override
        public int getWidgetId() {
            return 5063;
        }

        @Override
        public String getCapacityExceededMsg() {
            return "Your goodie bag is currently full!";
        }

        @Override
        public void itemUpdated(ItemContainer container, Optional<Item> oldItem, Optional<Item> newItem, int index, boolean refresh) {
        }

        @Override
        public void bulkItemsUpdated(ItemContainer container) {
            refresh();
        }
    }
}
