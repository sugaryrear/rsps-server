package com.ferox.game.content;

import com.ferox.game.GameEngine;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.save.PlayerSave;
import com.ferox.game.world.items.Item;
import org.apache.commons.lang3.time.DateUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static com.ferox.util.ItemIdentifiers.MYSTERY_BOX;

public class dailyRewardsHandout {
    public static void setdate(){//only change this
        TopPkersDaily wogw = World.getServerData().getTopPkersDaily();

        // Date date = calendar.getTime();
        Date date = new GregorianCalendar(World.getWorld().getCalendar().getInstance().get(Calendar.YEAR), Calendar.AUGUST, 13, 22, 59).getTime();
        wogw.setDate(date);
        System.out.println(wogw.getDate().toString());
        World.getServerData().setTopPkersDaily(wogw);
        World.getServerData().processQueue();

    }
    public static int TOP_PKERS_REWARD_ITEM = MYSTERY_BOX;
    public static int TOP_BOSS_POINTS_REWARD_ITEM = MYSTERY_BOX;
    public static int TOP_XP_REWARD_ITEM = MYSTERY_BOX;


    public static void handleRewards() {
        TopPkersDaily wogw = World.getServerData().getTopPkersDaily();
        TopBossPointsDaily bosspointsdaily = World.getServerData().getTopBossPointsDaily();
        TopXPDaily topxpdaily = World.getServerData().getTopXPDaily();


        Calendar calendar = World.getWorld().getCalendar().getInstance();
        Date date = calendar.getTime();
        Item itemfortoppkers = new Item(TOP_PKERS_REWARD_ITEM);
        Item itemfortopbosspoints = new Item(TOP_PKERS_REWARD_ITEM);
        Item itemfortopxp = new Item(TOP_XP_REWARD_ITEM);
        if (calendar.getTime().after(wogw.getDate())) {
            date = DateUtils.addDays(date, 1);
            wogw.setDate(date);
            System.out.println(wogw.getDate().toString());

            ArrayList<Map.Entry<String, Integer>> winners = wogw.getSortedResultsForWinners();

            if(winners.size() == 0){
                return;
            }
            ArrayList<Map.Entry<String, Integer>> winnersforbosspoints = bosspointsdaily.getSortedResultsForWinners();
            ArrayList<Map.Entry<String, Long>> winnersfortopxp = topxpdaily.getSortedResultsForWinners();



            Map<String, Integer> winnerMap = new HashMap<>();
            winners.forEach(entry -> winnerMap.put(entry.getKey(), entry.getValue()));

            Map<String, Integer> winnerMapforbosspoints = new HashMap<>();
            winnersforbosspoints.forEach(entry -> winnerMapforbosspoints.put(entry.getKey(), entry.getValue()));

            Map<String, Long> winnerMapfortopxp = new HashMap<>();
            winnersfortopxp.forEach(entry -> winnerMapfortopxp.put(entry.getKey(), entry.getValue()));




            if (winners.size() == 0) { // no winners
                wogw.setWinners(winnerMap);
            } else {
                wogw.setWinners(winnerMap);
                bosspointsdaily.setWinners(winnerMapforbosspoints);
                StringBuilder s = new StringBuilder();
                for (Map.Entry<String, Integer> entry : winners) {
                    Optional<Player> playerToGive = World.getWorld().getPlayerByName(entry.getKey());
                    s.append(entry.getKey());



                    if (playerToGive.isEmpty()) { // if player is offline add the item to his bank
                        Player player = new Player();
                        player.setUsername(entry.getKey());
                        GameEngine.getInstance().submitLowPriority(() -> {
                            try {
                                if (PlayerSave.loadOfflineWithoutPassword(player)) {
                                    player.inventory().addOrBank(new Item(TOP_PKERS_REWARD_ITEM));
                                    PlayerSave.save(player);
                                } else {
                                    System.out.println("Something went wrong offline reward for offline Player " + player.getUsername());
                                }
                            } catch (Exception e) {

                            }
                        });
                        continue;
                    }

                    playerToGive.get().inventory().addOrBank(new Item(TOP_PKERS_REWARD_ITEM));
                    playerToGive.get().message("Congratulations you are the top pker of the day!");

                }

                World.getWorld().sendWorldMessage("<col=004f00>Broadcast:</col>Congratulations to "+ s +" on winning a "+itemfortoppkers.unnote().name()+" for being the top daily");
                World.getWorld().sendWorldMessage("<col=004f00>Broadcast:</col>pker!");

            }

            if (winnersforbosspoints.size() == 0) { // no winners
                bosspointsdaily.setWinners(winnerMapforbosspoints);
            } else {
                bosspointsdaily.setWinners(winnerMapforbosspoints);
                StringBuilder s = new StringBuilder();
                for (Map.Entry<String, Integer> entry : winnersforbosspoints) {
                    Optional<Player> playerToGive = World.getWorld().getPlayerByName(entry.getKey());
                    s.append(entry.getKey());



                    if (playerToGive.isEmpty()) { // if player is offline add the item to his bank
                        Player player = new Player();
                        player.setUsername(entry.getKey());
                        GameEngine.getInstance().submitLowPriority(() -> {
                            try {
                                if (PlayerSave.loadOfflineWithoutPassword(player)) {
                                    player.inventory().addOrBank(new Item(TOP_BOSS_POINTS_REWARD_ITEM));
                                    PlayerSave.save(player);
                                } else {
                                    System.out.println("Something went wrong offline reward for offline Player " + player.getUsername());
                                }
                            } catch (Exception e) {

                            }
                        });
                        continue;
                    }

                    playerToGive.get().inventory().addOrBank(new Item(TOP_BOSS_POINTS_REWARD_ITEM));
                    playerToGive.get().message("Congratulations you have the top boss kills of the day!");

                }

                World.getWorld().sendWorldMessage("<col=004f00>Broadcast:</col>Congratulations to "+ s +" on winning a "+itemfortopxp.unnote().name()+" for having the top");
                World.getWorld().sendWorldMessage("<col=004f00>Broadcast:</col>daily boss points!");

                //  }
            }
            if (winnersfortopxp.size() == 0) { // no winners
                topxpdaily.setWinners(winnerMapfortopxp);
            } else {
                topxpdaily.setWinners(winnerMapfortopxp);
                StringBuilder s = new StringBuilder();
                for (Map.Entry<String, Long> entry : winnersfortopxp) {
                    Optional<Player> playerToGive = World.getWorld().getPlayerByName(entry.getKey());
                    s.append(entry.getKey());



                    if (playerToGive.isEmpty()) { // if player is offline add the item to his bank
                        Player player = new Player();
                        player.setUsername(entry.getKey());
                        GameEngine.getInstance().submitLowPriority(() -> {
                            try {
                                if (PlayerSave.loadOfflineWithoutPassword(player)) {
                                    player.inventory().addOrBank(new Item(TOP_BOSS_POINTS_REWARD_ITEM));
                                    PlayerSave.save(player);
                                } else {
                                    System.out.println("Something went wrong offline reward for offline Player " + player.getUsername());
                                }
                            } catch (Exception e) {

                            }
                        });
                        continue;
                    }

                    playerToGive.get().inventory().addOrBank(new Item(TOP_BOSS_POINTS_REWARD_ITEM));
                    playerToGive.get().message("Congratulations you have the top xp of the day!");

                }

                World.getWorld().sendWorldMessage("<col=004f00>Broadcast:</col>Congratulations to "+ s +" on winning a "+itemfortopbosspoints.unnote().name()+" for having the top daily xp!");

                //  }
            }

            wogw.clear();
            World.getServerData().setTopPkersDaily(wogw);
            World.getServerData().processQueue();
            bosspointsdaily.clear();

            World.getServerData().setTopBossPointsDaily(bosspointsdaily);
            World.getServerData().processQueue();
            topxpdaily.clear();
            World.getServerData().setTopXPDaily(topxpdaily);
            World.getServerData().processQueue();
        } else {
           // System.out.println("The day hasn't finished yet.");
        }
    }
}
