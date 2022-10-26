package com.ferox.game.world.definition.loader.impl;

import com.ferox.GameServer;
import com.ferox.fs.ItemDefinition;
import com.ferox.game.world.World;
import com.ferox.game.world.definition.loader.DefinitionLoader;
import com.ferox.game.world.items.container.shop.SellType;
import com.ferox.game.world.items.container.shop.StoreItem;
import com.ferox.game.world.items.container.shop.currency.CurrencyType;
import com.ferox.game.world.items.container.shop.impl.DefaultShop;
import com.google.gson.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.util.*;

public class ShopLoader extends DefinitionLoader {

    private static final Logger logger = LogManager.getLogger(ShopLoader.class);

    @Override
    public void load() throws Exception {
        try (FileReader in = new FileReader(file())) {
            JsonArray array = (JsonArray) JsonParser.parseReader(in);
            Gson builder = new GsonBuilder().create();
            for (int index = 0; index < array.size(); index++) {
                JsonObject reader = (JsonObject) array.get(index);
                final int shopId = Objects.requireNonNull(reader.get("shopId")).getAsInt();
                final String name = Objects.requireNonNull(reader.get("name").getAsString());
                final boolean noiron = reader.get("noiron").getAsBoolean();
                final CurrencyType currency = builder.fromJson(reader.get("currency"), CurrencyType.class);
                final boolean restock = reader.get("restock").getAsBoolean();
                final int scroll = reader.get("scroll").getAsInt();
                final String sellType = reader.get("sellType").getAsString().toUpperCase();
                final LoadedItem[] loadedItems = builder.fromJson(reader.get("items"), LoadedItem[].class);

                final List<StoreItem> storeItems = new ArrayList<>(loadedItems.length);

                for (LoadedItem loadedItem : loadedItems) {
                  //  ItemDefinition def = World.getWorld().definitions().get(ItemDefinition.class, itemid);

//                    OptionalInt value = loadedItem.value == 0 ? OptionalInt.empty() : OptionalInt.of(loadedItem.value);
                    OptionalInt value = loadedItem == null ? OptionalInt.of(1) :loadedItem.value == 0 ? OptionalInt.of(World.getWorld().definitions().get(ItemDefinition.class, loadedItem.id).cost) : OptionalInt.of(loadedItem.value);
if(loadedItem == null)
    storeItems.add(new StoreItem(1,1,OptionalInt.of(1), Optional.ofNullable(CurrencyType.DONATOR_TICKETS)));
else
                    storeItems.add(new StoreItem(loadedItem.id, loadedItem.amount, value, Optional.ofNullable(loadedItem.type)));
                }

                StoreItem[] items = storeItems.toArray(new StoreItem[0]);
                World.getWorld().shops.put(shopId, new DefaultShop(items, shopId, name, noiron, SellType.valueOf(sellType), scroll, restock, currency));
            }
        }
    }
    
    @Override
    public String file() {
        return GameServer.properties().definitionsDirectory + "shops.json";
    }
    
    private static final class LoadedItem {

        private final int id;

        private final int amount;

        private final int value;

        private final CurrencyType type;

        public LoadedItem(int id, int amount, int value, CurrencyType type) {
            this.id = id;
            this.amount = amount;
            this.value = value;
            this.type = type;
        }
    }

}
