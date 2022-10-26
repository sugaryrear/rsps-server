package com.ferox.game.content;

import com.ferox.game.GameEngine;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.save.PlayerSave;
import com.ferox.game.world.items.Item;
import com.ferox.util.Utils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static com.ferox.game.world.entity.AttributeKey.*;
import static com.ferox.util.CustomItemIdentifiers.SLAYER_KEY;
import static com.ferox.util.ItemIdentifiers.MYSTERY_BOX;

public class TopPkersWeeklyEvent {
public static void setdate(){//only change this
    TopPkersWeekly wogw = World.getServerData().getTopPkersWeekly();
    Calendar calendar = World.getWorld().getCalendar().getInstance();

    // Date date = calendar.getTime();
    Date date = new GregorianCalendar(World.getWorld().getCalendar().getInstance().get(Calendar.YEAR), Calendar.AUGUST, 14, 22, 59).getTime();
    wogw.setDate(date);
    System.out.println(wogw.getDate().toString());
    World.getServerData().setTopPkersWeekly(wogw);
    World.getServerData().processQueue();

}

public static int TOP_PKERS_REWARD_ITEM = MYSTERY_BOX;
    public static int TOP_BOSS_POINTS_REWARD_ITEM = MYSTERY_BOX;

    public static int TOP_XP_REWARD_ITEM = MYSTERY_BOX;
    public static void handleTopPkersRewards() {
        TopPkersWeekly wogw = World.getServerData().getTopPkersWeekly();
        TopBossPointsWeekly bosspointsweekly = World.getServerData().getTopBossPointsWeekly();
        TopXPWeekly topxpweekly = World.getServerData().getTopXPWeekly();
        Calendar calendar = World.getWorld().getCalendar().getInstance();
        Date date = calendar.getTime();
        Item itemfortoppkers = new Item(TOP_PKERS_REWARD_ITEM);
        Item itemfortopbosspoints = new Item(TOP_PKERS_REWARD_ITEM);
        Item itemfortopxp = new Item(TOP_XP_REWARD_ITEM);
        if (calendar.getTime().after(wogw.getDate())) {
            date = DateUtils.addDays(date, 7);
            wogw.setDate(date);
        //    System.out.println(wogw.getDate().toString());
            ArrayList<Map.Entry<String, Integer>> winners = wogw.getSortedResultsForWinners();
            ArrayList<Map.Entry<String, Integer>> winnersforbosspoints = bosspointsweekly.getSortedResultsForWinners();
            ArrayList<Map.Entry<String, Long>> winnersfortopxp = topxpweekly.getSortedResultsForWinners();


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
                bosspointsweekly.setWinners(winnerMapforbosspoints);
                topxpweekly.setWinners(winnerMapfortopxp);
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

                    }
                try(FileWriter fw = new FileWriter("data/weeklyrewardswinner/toppkersrewards.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {
                    out.println("winner: "+s);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                World.getWorld().sendWorldMessage("<col=004f00>Broadcast:</col>Congratulations to "+ s +" on winning a "+itemfortoppkers.unnote().name()+" for being the top weekly pker!");

              //  }
            }

            if (winnersforbosspoints.size() == 0) { // no winners
                bosspointsweekly.setWinners(winnerMapforbosspoints);
            } else {
                bosspointsweekly.setWinners(winnerMapforbosspoints);
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

                }
                try(FileWriter fw = new FileWriter("data/weeklyrewardswinner/topbosspointsrewards.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {
                    out.println("winner: "+s);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                World.getWorld().sendWorldMessage("<col=004f00>Broadcast:</col>Congratulations to "+ s +" on winning a "+itemfortopbosspoints.unnote().name()+" for having the top weekly boss points!");

                //  }
            }

            if (winnersfortopxp.size() == 0) { // no winners
                topxpweekly.setWinners(winnerMapfortopxp);
            } else {
                topxpweekly.setWinners(winnerMapfortopxp);
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
                                    player.inventory().addOrBank(new Item(TOP_XP_REWARD_ITEM));
                                    PlayerSave.save(player);
                                } else {
                                    System.out.println("Something went wrong offline reward for offline Player " + player.getUsername());
                                }
                            } catch (Exception e) {

                            }
                        });
                        continue;
                    }

                    playerToGive.get().inventory().addOrBank(new Item(TOP_XP_REWARD_ITEM));

                }
                try(FileWriter fw = new FileWriter("data/weeklyrewardswinner/topxprewards.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {
                    out.println("winner: "+s);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                World.getWorld().sendWorldMessage("<col=004f00>Broadcast:</col>Congratulations to "+ s +" on winning a "+itemfortopxp.unnote().name()+" for having the top weekly XP!");

                //  }
            }
            wogw.clear();
            World.getServerData().setTopPkersWeekly(wogw);
            World.getServerData().processQueue();
            bosspointsweekly.clear();
            World.getServerData().setTopBossPointsWeekly(bosspointsweekly);
            World.getServerData().processQueue();
           topxpweekly.clear();


            World.getServerData().setTopXPWeekly(topxpweekly);
            World.getServerData().processQueue();
        } else {
            System.out.println("The week didn't finish yet.");
        }
    }
    }

