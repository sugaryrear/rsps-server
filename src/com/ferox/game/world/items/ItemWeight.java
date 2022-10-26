package com.ferox.game.world.items;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Situations on 12/31/2015.
 */
public class ItemWeight {

    private static final Logger logger = LogManager.getLogger(ItemWeight.class);
    public static final transient int MAX_SIZE = 13352;
    private static final int[] WEIGHT_REDUCERS = {};
    private static Map<Integer, Double> itemWeight = new HashMap<>();

    private static Map<Integer, Double> equipweightOverrides = new HashMap<Integer, Double>() {{
        put(11850, -3.0); // Graceful hood
        put(11852, -4.0); // Graceful cape
        put(11854, -5.0); // Graceful top
        put(11856, -6.0); // Graceful legs
        put(11858, -3.0); // Graceful gloves
        put(11860, -4.0); // Graceful boots
    }};

    public static void init() {
        itemWeight.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader("./data/list/item_weight.txt"))) {
            long start = System.currentTimeMillis();
            while (true) {
                String file = reader.readLine();
                if (file == null || file.trim().startsWith("//")) {
                    break;
                }

                String[] values = file.split(":");
                itemWeight.put(Integer.valueOf(values[0]), Double.parseDouble(values[1]));
            }
            long elapsed = System.currentTimeMillis() - start;
            //logger.info("Loaded " + itemWeight.size() + " item weights.");
            logger.info("Loaded definitions for ./data/list/item_weight.txt. It took " + elapsed + "ms.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double calculateWeight(Player player) {
        double weight = 0;

        for (Item item : player.inventory()) {
            if (item != null) {
                weight += getWeight(item.getId());
            }
        }

        for (Item item : player.getEquipment()) {
            if (item != null) {
                if (equipweightOverrides.containsKey(item.getId())) {
                    weight += equipweightOverrides.get(item.getId());
                } else {
                    weight += getWeight(item.getId());
                }
            }
        }

        player.setWeight(weight);
        player.getPacketSender().sendWeight(weight);
        player.getPacketSender().sendString(15122, Utils.format((int) weight) + " kg");
        return weight;
    }

    private static double getWeight(int itemId) {
        return itemWeight.getOrDefault(itemId, 0.0);
    }

}
