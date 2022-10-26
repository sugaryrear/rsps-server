package com.ferox.game.world.definition.loader.impl;

import com.ferox.GameServer;
import com.ferox.fs.ItemDefinition;
import com.ferox.game.world.World;
import com.ferox.game.world.definition.BloodMoneyPrices;
import com.ferox.game.world.definition.loader.DefinitionLoader;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;

public class BloodMoneyPriceLoader extends DefinitionLoader {
    private static final Logger logger = LogManager.getLogger(BloodMoneyPriceLoader.class);

    @Override
    public void load() throws Exception {
        try (FileReader reader = new FileReader(file())) {
            BloodMoneyPrices[] defs = new Gson().fromJson(reader, BloodMoneyPrices[].class);
            StringBuilder duplicates = new StringBuilder();
            int duplicateCount = 0;
            for (BloodMoneyPrices def : defs) {
                if (BloodMoneyPrices.definitions.containsKey(def.id())) {
                    duplicates.append(def.id()).append(", ");
                    duplicateCount++;
                }
                BloodMoneyPrices.definitions.put(def.id(), def);
                World.getWorld().definitions().get(ItemDefinition.class, def.id()).bm = def;
            }
            if (duplicateCount > 0) {
                logger.error("There are " + duplicateCount + " duplicate Blood Money Price JSON entries");
                logger.error(duplicates + " have duplicate Blood Money Price JSON values.");
            }
        }
    }

    @Override
    public String file() {
        return GameServer.properties().definitionsDirectory + "blood_money_prices.json";
    }
}
