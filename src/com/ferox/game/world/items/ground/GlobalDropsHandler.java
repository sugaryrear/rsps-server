package com.ferox.game.world.items.ground;

import com.ferox.game.task.Task;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.combat.bountyhunter.emblem.BountyHunterEmblem;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Color;
import com.ferox.util.Utils;
import org.apache.commons.text.WordUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GlobalDropsHandler extends Task {

    /**
     * The amount of time in game cycles (600ms) that the event pulses at
     */
    private static final int INTERVAL = Utils.toCyclesOrDefault(1, 1, TimeUnit.MINUTES);

    public static Optional<GlobalDrop> getGlobalItem(int id, int x, int y) {
        List<GlobalDrop> globalitems = globalDrops.stream().filter(finditem(id,x,y)).collect(Collectors.toList());
        if (globalitems.isEmpty()) {
            return Optional.empty();
        }

        return globalitems.stream().findFirst();
   }
    private static Predicate<GlobalDrop> finditem(int id, int x, int y) {
        return emblem -> emblem.getId() == id && emblem.getX() == x && emblem.getY() == y;
    }
    /**
     * Creates a new event to cycle through messages for the entirety of the runtime
     */
    public GlobalDropsHandler() {
        super("globaldropshandler", INTERVAL);
    }

    @Override
    public void execute() {

        for (GlobalDrop drop : globalDrops) {
            drop.setTaken(false);
            for (Player client : World.getWorld().getPlayers()) {

                if (client != null) {
                    GroundItem groundItem;
               //     client.message("here1");
                    if (client.distanceToPoint(drop.getX(), drop.getY()) <= 60) {
                        Optional<GroundItem> item = GroundItemHandler.getUnownedGroundItem(drop.getId(), new Tile(drop.getX(), drop.getY(), drop.getH()));
                        if (item == null || !item.isPresent()) {
                            groundItem = new GroundItem(new Item(drop.getId(), drop.getAmount()), new Tile(drop.getX(), drop.getY(), drop.getH()));
                            groundItem.setState(GroundItem.State.SEEN_BY_EVERYONE);
                            GroundItemHandler.createGroundItem(groundItem);
                       //     client.message("here2");
                        }


                    }
                }
            }

        }
    }
    /**
     * Loads all the items when a player changes region
     */
    public static void load(Player client) {
        GroundItem groundItem;
        for(GlobalDrop drop : globalDrops) {
            if(!drop.isTaken()) {

                if (client.distanceToPoint(drop.getX(), drop.getY()) <= 60) {
                    Optional<GroundItem> item = GroundItemHandler.getUnownedGroundItem(drop.getId(), new Tile(drop.getX(), drop.getY(), drop.getH()));
                    if (item == null ||     !item.isPresent()) {
                        groundItem = new GroundItem(new Item(drop.getId(),drop.getAmount()),  new Tile(drop.getX(), drop.getY(), drop.getH()));
                        groundItem.setState(GroundItem.State.SEEN_BY_EVERYONE);
                        GroundItemHandler.createGroundItem(groundItem);
                    }
                }
            }
        }
    }
    /**
     * holds all the objects
     */
    public static List<GlobalDrop> globalDrops = new ArrayList<GlobalDrop>();



    /**
     * loads the items
     */
    public static void initialize() {
       // ObjectManager.addObj(new GameObject(31858, new Tile(3126,3635), 10, 1));

        String Data;
        BufferedReader Checker = null;
        try {
            Checker = new BufferedReader(new FileReader("data/map/globaldrops.txt"));
            while ((Data = Checker.readLine()) != null) {
                if (Data.startsWith("#"))
                    continue;
                String[] args = Data.split(":");
                globalDrops.add(new GlobalDrop(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]),  Integer.parseInt(args[4])));
            }
            Checker.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Loaded " + globalDrops.size() + " global drops.");

    }

    /**
     * Holds each drops data
     * @author Stuart
     *
     */
    static class GlobalDrop {
        /**
         * cord x
         */
        int x;
        /**
         * cord y
         */
        int y;
        /**
         * item id
         */
        int h;
        int id;
        /**
         * item amount
         */
        int amount;
        /**
         * has the item been taken
         */
        boolean taken = false;
        /**
         * Time it was taken at
         */
        long takenAt;

        /**
         * Sets the drop arguments
         * @param a item id
         * @param b item amount
         * @param c cord x
         * @param d cord y
         */
        public GlobalDrop(int a, int b, int c, int d, int h) {
            this.id = a;
            this.amount = b;
            this.x = c;
            this.y = d;
            this.h = h;
        }

        /**
         * get cord x
         * @return
         */
        public int getX() {
            return this.x;
        }

        /**
         * get cord x
         * @return
         */
        public int getY() {
            return this.y;
        }

        /**
         * get cord h
         * @return
         */
        public int getH() {
            return this.h;
        }


        /**
         * get the item id
         * @return
         */
        public int getId() {
            return this.id;
        }

        /**
         * get the item amount
         * @return
         */
        public int getAmount() {
            return this.amount;
        }

        /**
         * has the drop already been taken?
         * @return
         */
        public boolean isTaken() {
            return this.taken;
        }

        /**
         * set if or not the drop has been taken
         * @param a true yes false no
         */
        public void setTaken(boolean a) {
            this.taken = a;
        }

        /**
         * set the time it was picked up
         * @param a
         */
        public void setTakenAt(long a) {
            this.takenAt = a;
        }

        /**
         * get the time it was taken at
         * @return
         */
        public long getTakenAt() {
            return this.takenAt;
        }

    }
}
