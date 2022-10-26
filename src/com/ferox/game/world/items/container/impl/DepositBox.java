package com.ferox.game.world.items.container.impl;

import com.ferox.game.world.InterfaceConstants;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.container.ItemContainer;
import com.ferox.game.world.items.container.ItemContainerAdapter;
import com.ferox.util.Utils;

import java.text.NumberFormat;
import java.util.Optional;

public class DepositBox extends ItemContainer {

    /** Holds all the string identifications */
    private final int[] STRINGS = { 49550, 49551, 49552, 49553, 49554, 49555, 49556, 49557, 49558, 49559, 49560, 49561,
        49562, 49563, 49564, 49565, 49566, 49567, 49568, 49569, 49570, 49571, 49572, 49573, 49574, 49575, 49576,
        49577, };

    /** The player instance. */
    public Player player;

    /** The item being searched. */
    public Item searchedItem;

    /** Creates a new <code>DepositBox<code>. */
    public DepositBox(Player player) {
        super(28, StackPolicy.STANDARD);
        this.player = player;
        addListener(new DepositBoxListener());
    }

    /** Closes the deposit box interface. */
    public void close() {
      //  player.getInterfaceManager().close();
        player.getDepositBox().searchedItem = null;
        player.putAttrib(AttributeKey.DEPOSIT_BOXING, false);
    }

    /** Opens the deposit box interface. */
    public void open() {
        clear();
        refresh();
        player.putAttrib(AttributeKey.DEPOSIT_BOXING, true);
        player.getInterfaceManager().openInventory(InterfaceConstants.DEPOSIT_BOX, 5063);
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



        int id = item.getId();

        int invAmount = player.inventory().count(id);

        if (invAmount < amount) {
            amount = invAmount;
        }

        setFiringEvents(false);
        add(id, amount);
        player.inventory().remove(item.getId(), amount);
        player.getBank().depositFromNothing(new Item(item, amount));
        setFiringEvents(true);
        refresh();
    }
    public void depositLootingBag(int slot, int amount) {
        Item item = player.getLootingBag().get(slot);
        if (item == null)
            return;



        int id = item.getId();

        int invAmount = player.getLootingBag().count(id);

        if (invAmount < amount) {
            amount = invAmount;
        }

        setFiringEvents(false);
        add(id, amount);
        setFiringEvents(true);
        refresh();
    }
    /** Deposits an item into the price checker. */
    public void depositeq(int slot, int amount) {
        Item item = player.getEquipment().get(slot);
        if (item == null)
            return;

        if (!item.rawtradable()) {
            player.message("This is item is untradeable!");
            return;
        }

        int id = item.getId();

        int invAmount = player.getEquipment().count(id);

        if (invAmount < amount) {
            amount = invAmount;
        }

        setFiringEvents(false);
        add(id, amount);
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
    public void depositToLootingBag() {
        if (!player.inventory().contains(11941))
            return;
        Item[] items = player.getLootingBag().toArray();
        for (int slot = 0; slot < items.length; slot++) {
            Item item = items[slot];
            if (item == null) {
                continue;
            }

            depositLootingBag(slot, item.getAmount());
        }
        refresh();
        for (int i = 0; i <= 27; i++) {
            var itemAt = player.getLootingBag().get(i);
            if(itemAt == null)
                continue; // Get item or continue
            player.getBank().deposit(i, itemAt.getAmount(), player.getLootingBag());
        }
    }

    public void depositEquipment() {

    Item[] items = player.getEquipment().toArray();

    for (int slot = 0; slot < items.length; slot++) {
        Item item = items[slot];
        if (item == null) {
            continue;
        }

        depositeq(slot, item.getAmount());
    }
    player.getBank().depositeEquipment();
    refresh();

}
    public boolean buttonActions(int button) {
        switch (button) {
            //Close
            case 35004 -> {
            close();
                return true;
            }
            //Deposit all
            case 42109-> {
                depositAll();
                return true;
            }
            case 42110-> {

                depositEquipment();
                return true;
            }
            case 42111-> {

                depositToLootingBag();
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public void sync() {
        refresh(player, InterfaceConstants.DEPOSIT_BOX_DISPLAY_ID);
    }

    @Override
    public void onRefresh() {
        player.getPacketSender().sendString(35005,"" + (size()) + "/28");


        player.inventory().refresh();

        player.getPacketSender().sendItemOnInterface(InterfaceConstants.INVENTORY_STORE, player.inventory().toArray());

    }

    private final class DepositBoxListener extends ItemContainerAdapter {

        DepositBoxListener() {
            super(player);
        }

        @Override
        public int getWidgetId() {
            return 5063;
        }

        @Override
        public String getCapacityExceededMsg() {
            return "Your deposit box is currently full!";
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
