package com.ferox.game.world.entity.combat.bountyhunter;

import com.ferox.GameServer;
import com.ferox.game.GameConstants;
import com.ferox.game.content.achievements.Achievements;
import com.ferox.game.content.achievements.AchievementsManager;
import com.ferox.game.content.areas.riskzone.RiskFightArea;
import com.ferox.game.content.areas.wilderness.content.PlayerKillingRewards;
import com.ferox.game.content.areas.wilderness.content.todays_top_pkers.TopPkers;
import com.ferox.game.content.daily_tasks.DailyTaskManager;
import com.ferox.game.content.daily_tasks.DailyTasks;
import com.ferox.game.content.items_kept_on_death.ItemsKeptOnDeath;
import com.ferox.game.content.mechanics.Death;
import com.ferox.game.task.Task;
import com.ferox.game.task.TaskManager;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.combat.bountyhunter.emblem.BountyHunterEmblem;
import com.ferox.game.world.entity.mob.player.GameMode;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.QuestTab;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.util.Color;
import com.ferox.util.ItemIdentifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ferox.game.world.entity.AttributeKey.EMBLEM_WEALTH;
import static com.ferox.util.ItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.WARRIOR_GUILD_TOKEN;
import static com.ferox.util.Utils.formatNumber;

/**
 * The bounty hunter minigame as of 4 November 2019.
 * 
 * @author Professor Oak
 * @author Zerikoth
 */
public class BountyHunter {

    /**
     * All players currently in the wilderness.
     */
    public static final List<Player> PLAYERS_IN_WILD = new ArrayList<>();

    /**
     * Target pairs.
     */
    private static final List<TargetPair> TARGET_PAIRS = new ArrayList<>();


    public static void resetInterface(Player player){
        player.getPacketSender().sendString(53723, "Target:");
        player.getPacketSender().sendString(53724, "None");
        player.getPacketSender().sendString(53725, "@yel@------");
        player.getPacketSender().sendString(53726, "---");


        for (int i = 53730; i <= 53740; i += 2) {
            player.getPacketSender().sendInterfaceDisplayState(i, true);
        }
        player.getPacketSender().sendInterfaceDisplayState(53730,false);
    }
    /**
     * Processes the bounty hunter system for the specified
     * player.
     */
    public static void sequence(Player player) {
        if (GameConstants.production) {
            //Get our target..
            Optional<Player> target = getTargetfor(player);
//System.out.println("sequence in BountyHunter.java");
            String color = null;
            String display = null;
            if (!target.isEmpty()) {
                int distance = player.tile().distance(target.get().tile());
                color = distance < 15 ? "gre" : distance >= 15 && distance < 45 ? "blu" : "red";
                display = WildernessArea.wildernessLevel(target.get().tile()) > 0 ? "Lvl " + WildernessArea.wildernessLevel(target.get().tile()) + "-" + (WildernessArea.wildernessLevel(target.get().tile()) + 3) : "Safe";
                updateSkull(player, target.get());
                for (int i = 53730; i <= 53740; i += 2) {
                    if (i == player.skull.getFrameId()) {
                        player.getPacketSender().sendInterfaceDisplayState(i, false);
                    } else {
                        player.getPacketSender().sendInterfaceDisplayState(i, true);
                    }
                }
            }

            String finalColor = color;
            String finalDisplay = display;
            target.ifPresent(value -> player.getPacketSender().sendString(53724, value.getUsername()));
            target.ifPresent(value -> player.getPacketSender().sendString(53725, "@" + finalColor + "@" + finalDisplay + ", Cmb " + target.get().skills().combatLevel()));

            target.ifPresent(value -> player.getPacketSender().sendString(53726, "Wealth: " + player.skull.getRepresentation()));

            //Is player in the wilderness?
            if (WildernessArea.inWilderness(player.tile())) {
                //Check if the player has a target.
                //If not, search for a new one.
                if (target.isEmpty()) {
                    resetInterface(player);
                    //Only search for a target every {@code TARGET_DELAY_SECONDS}.
                    if (!player.getTargetSearchTimer().active()) {
                        //Make sure we're a valid target..
                        if (!validTargetContester(player)) {
                            return;
                        }

                        //Search for a new target for the player..
                        for (final Player potential : PLAYERS_IN_WILD) {
                            //Check if player2 is a valid target..
                            if (validTargetContester(potential)) {
                                //Check other stuff...

//                            if(potential.getHostAddress().equalsIgnoreCase(player.getHostAddress())) {
//                                continue;
//                            }

                                if (potential.looks().hidden()) {
                                    continue;
                                }

                                //Can't get this user as target
                                if (potential.getUsername().equalsIgnoreCase("TRY CATCH ME")) {
                                    continue;
                                }

                                boolean riskZone = potential.tile().inArea(RiskFightArea.NH_AREA) || potential.tile().inArea(RiskFightArea.ONE_V_ONE_1) || potential.tile().inArea(RiskFightArea.ONE_V_ONE_2) || potential.tile().inArea(RiskFightArea.ONE_V_ONE_3);

                                //Skip risk arena
                                if (riskZone) {
                                    continue;
                                }

                                //Check if we aren't looping ourselves..
                                if (player.equals(potential)) {
                                    continue;
                                }

//                            //Check that we haven't killed this player before..
//                            if (player.getRecentKills().contains(potential.getHostAddress()) && GameServer.properties().production) {
//                                continue;
//                            }

                                //Skip clan mates
                                if (player.getClanChat().equalsIgnoreCase(potential.getClanChat())) {
                                    continue;
                                }

                                if (Math.abs(player.skills().combatLevel() - potential.skills().combatLevel()) > 5) {
                                    continue;
                                }

                                assign(player, potential);
                                break;
                            }
                        }
                        player.getTargetSearchTimer().start(TARGET_SEARCH_DELAY_SECONDS);
                    }
                }
            } else {
                //Player leaves wilderness remove target, otherwise it takes to long to assign new target.
                if (target.isPresent()) {
                    unassign(player);
                    target.get().message("You have lost your target and will be given a new one shortly.");
                }
            }
        }
    }
    /**
     * Assign a new {@link TargetPair} of the two specified players.
     */
    public static void assign(Player player, Player target) {
        if(GameConstants.production) {
            if (getPairFor(player).isEmpty() && getPairFor(target).isEmpty()) {

                //Create a new pair..
                final TargetPair pair = new TargetPair(player, target);

                //Add the pair to our list..
                TARGET_PAIRS.add(pair);


                int distance = player.tile().distance(target.tile());
                String color = distance < 15 ? "gre" : distance >= 15 && distance < 45 ? "blu" : "red";
                String display = WildernessArea.wildernessLevel(target.tile()) > 0 ? "Lvl " + WildernessArea.wildernessLevel(target.tile()) + "-" + (WildernessArea.wildernessLevel(target.tile()) + 3) : "Safe";


                //Send messages..
                updateSkull(player, target);


                for (int i = 53730; i <= 53740; i += 2) {
                    if (i == player.skull.getFrameId()) {
                        player.getPacketSender().sendInterfaceDisplayState(i, false);
                    } else {
                        player.getPacketSender().sendInterfaceDisplayState(i, true);
                    }
                }
                player.getPacketSender().sendMessage("You've been assigned " + target.getUsername() + " as your target!");


                player.getPacketSender().sendString(53724, target.getUsername());
                player.getPacketSender().sendString(53725, "@" + color + "@" + display + ", Cmb " + target.skills().combatLevel());
                player.getPacketSender().sendString(53726, "Wealth: " + player.skull.getRepresentation());


                target.getPacketSender().sendMessage("You've been assigned " + player.getUsername() + " as your target!");
                target.getPacketSender().sendString(53724, player.getUsername());


                //Send hints..
                player.getPacketSender().sendEntityHint(target);
                target.getPacketSender().sendEntityHint(player);

                //Reset attributes
                player.clearAttrib(AttributeKey.SPECIAL_ATTACK_USED);
                target.clearAttrib(AttributeKey.SPECIAL_ATTACK_USED);
            }
        }
    }

    /**
     * The players bounty hunter skull. This determines their wealth.
     */

    public static void updateSkull(Player player, Player target) {
        ItemsKeptOnDeath.clearAndRecalc(target);


        //Get risked value.
        long risked = ItemsKeptOnDeath.getLostItemsValue();
        player.skull = BountyHunterSkull.getSkull(risked);
    }
    /**
     * Unassign an existing {@link TargetPair}.
     */
    public static void unassign(Player player) {
        final Optional<TargetPair> pair = getPairFor(player);
        if (pair.isPresent()) {

            TARGET_PAIRS.remove(pair.get());

            final Player p1 = pair.get().getPlayer1();
            final Player p2 = pair.get().getPlayer2();

            //Reset hints..
            p1.getPacketSender().sendEntityHintRemoval(true);
            p2.getPacketSender().sendEntityHintRemoval(true);


            //Reset name
            p2.getPacketSender().sendString(53723, "Target: <col=65280>None");
            p1.getPacketSender().sendString(53723, "Target: <col=65280>None");

            resetInterface(p1);
            resetInterface(p2);

            //Set timers
            p2.getTargetSearchTimer().start(TARGET_SEARCH_DELAY_SECONDS);
            p1.getTargetSearchTimer().start(TARGET_SEARCH_DELAY_SECONDS);
        }
    }

    /**
     * Gets the {@link Player} target for the specified player.
     */
    public static Optional<Player> getTargetfor(Player player) {
        Optional<TargetPair> pair = getPairFor(player);
        if (pair.isPresent()) {

            //Check if player 1 in the pair is us.
            //If so, return the other player.
            if (pair.get().getPlayer1().equals(player)) {
                return Optional.of(pair.get().getPlayer2());
            }

            //Check if player 2 in the pair is us.
            //If so, return the other player.
            if (pair.get().getPlayer2().equals(player)) {
                return Optional.of(pair.get().getPlayer1());
            }
        }
        return Optional.empty();
    }

    /**
     * Gets the {@link TargetPair} for the specfied player.
     */
    public static Optional<TargetPair> getPairFor(final Player p) {
        for (TargetPair pair : TARGET_PAIRS) {
            if (p.equals(pair.getPlayer1()) ||
                    p.equals(pair.getPlayer2())) {
                return Optional.of(pair);
            }
        }
        return Optional.empty();
    }



    /**
     * Handles death for a player.
     * Rewards the killer.
     */
    public static void onDeath(Player killer, Player killed) {

        var safepk = killer.tile().inArea(2434,3079, 2444,3100) ||
            killer.tile().inArea(3329,3204, 3391,3261);

        if(safepk){
            return;
        }
        //Remove first index if we've killed 1
        if (killer.getRecentKills().size() >= 2) {
            killer.getRecentKills().remove(0);
        }

        //Should the player be rewarded for this kill?
        boolean rewardPlayer = true;
if(!killer.listofkills.containsKey(killed.getUsername())){
    killer.listofkills.put(killed.getUsername(),true);
}

        if(killer.getRecentKills().contains(killed.getHostAddress())) {
          //  rewardPlayer = false;
            //System.out.println("Let's not reward the player, already killed before.");
        } else if (killer.getHostAddress().equals(killed.getHostAddress())) {
         //   rewardPlayer = false;
            //System.out.println("Let's not reward the player, same IP.");
        } else if (!WildernessArea.inWilderness(killer.tile())) {
            rewardPlayer = false;
            //System.out.println("Let's not reward the player, not in wild.");
        } else {
            //System.out.println("Let's reward the player");
            //Clear out the old kills so the player can kill other players, since it is recent kills, not lifetime kills.
            killer.getRecentKills().clear();
            killer.getRecentKills().add(killed.getHostAddress());
        }

        Optional<Player> target = getTargetfor(killer);

//        if(killer.getPlayerRights().isDeveloperOrGreater(killer)) {
//            rewardPlayer = true;
//        }

        if (rewardPlayer) {
            TopPkers.SINGLETON.increase(killer.getUsername());

            //Other rewards
            if(WildernessArea.inWilderness(killer.tile())) { // Only reward if in wild
                PlayerKillingRewards.reward(killer, killed,true);
            }
        } else {
            killer.message("You don't get any rewards for that kill.");
            //Player is probably farming kills.
        }

        //Check if the player killed was our target..
        if (target.isPresent() && target.get().equals(killed)) {
            if (!rewardPlayer) {
                killed.message("Being killed by targets on the same address does not count.");
                killer.message("Killing targets on the same address does not count towards rewards.");
            }

            //Reset targets
            unassign(killer);

            //If player isn't farming kills..
            if (rewardPlayer) {
                //Send messages
                killed.getPacketSender().sendMessage("You were defeated by your target!");

                int targetKills = (Integer) killer.getAttribOr(AttributeKey.TARGET_KILLS, 0) + 1;
                killer.putAttrib(AttributeKey.TARGET_KILLS, targetKills);




                var increaseBy = 4;
                var targetPoints = killer.<Integer>getAttribOr(AttributeKey.TARGET_POINTS, 0) + increaseBy;
                killer.putAttrib(AttributeKey.TARGET_POINTS, targetPoints);
                killer.message("(" + Color.RED.tag() + "+1</col>) You receive 4 target points for killing your assigned target!");
                killer.message("You now have " + Color.BLUE.tag() + "" + targetPoints + "</col> target points. (" + Color.RED.tag() + "+" + increaseBy + "</col>)");
                killer.getPacketSender().sendString(QuestTab.InfoTab.TARGET_KILLS.childId, QuestTab.InfoTab.INFO_TAB.get(QuestTab.InfoTab.TARGET_KILLS.childId).fetchLineData(killer));

                Optional<BountyHunterEmblem> emblem = BountyHunterEmblem.getBest(killer, true);

                if (emblem.isPresent()) {
                    killer.inventory().remove(new Item(emblem.get().getItemId()));
                    killer.inventory().add(new Item(emblem.get().getNextOrLast().getItemId()));

                    boolean tierTen = emblem.get().getNextOrLast().getItemId() == ItemIdentifiers.ANTIQUE_EMBLEM_TIER_10;
                    if(tierTen) {
                        DailyTaskManager.increase(DailyTasks.TIER_UPGRADE, killer);
                    }
                } else {
                    killer.inventory().addOrBank(new Item(ANTIQUE_EMBLEM_TIER_1));
                }

                AchievementsManager.activate(killer, Achievements.BOUNTY_HUNTER_I, 1);
                AchievementsManager.activate(killer, Achievements.BOUNTY_HUNTER_II, 1);
                AchievementsManager.activate(killer, Achievements.BOUNTY_HUNTER_III, 1);
            }
        } else {

            var increaseBy = 3;
            var targetPoints = killer.<Integer>getAttribOr(AttributeKey.TARGET_POINTS, 0) + increaseBy;
            killer.putAttrib(AttributeKey.TARGET_POINTS, targetPoints);
            killer.message("(" + Color.RED.tag() + "+1</col>) This was not your assigned target. You only receive 3 target points.");
            killer.message("You now have " + Color.BLUE.tag() + "" + targetPoints + "</col> target points. (" + Color.RED.tag() + "+" + increaseBy + "</col>)");
        }

    //    killer.message(String.format(Death.randomKillMessage(), killed.getUsername()));
    }

    /**
     * Gets the amount of value for a player's emblems.
     */
    public static int exchange(Player player, boolean performSale) {
        ArrayList<BountyHunterEmblem> list = new ArrayList<>();
        for(BountyHunterEmblem emblem : BountyHunterEmblem.values()) {
            if(player.inventory().contains(emblem.getItemId())) {
                list.add(emblem);
            }
        }

        if(list.isEmpty()) {
            return 0;
        }

        int value = 0;
        int targetPoints = 0;

        for(BountyHunterEmblem emblem : list) {
            int amount = player.inventory().count(emblem.getItemId());
            if(amount > 0) {
                targetPoints += (emblem.getTargetPoints() * amount);
                value += (emblem.getBm() * amount);
                player.putAttrib(EMBLEM_WEALTH,formatNumber(value)+" BM and "+formatNumber(targetPoints)+" target points");

                if(performSale) {
                    if(!player.inventory().contains(emblem.getItemId())) {
                        return 0;
                    }
                    player.inventory().remove(emblem.getItemId(), amount);
                    var increaseTargetPointsBy = player.<Integer>getAttribOr(AttributeKey.TARGET_POINTS,0) + targetPoints;
                    player.putAttrib(AttributeKey.TARGET_POINTS, increaseTargetPointsBy);
                    var blood_reaper = player.hasPetOut("Blood Reaper pet");
                    if(blood_reaper) {
                        int extraBM = value * 10 / 100;
                        value += extraBM;
                    }
                    player.inventory().add(new Item(BLOOD_MONEY, value));
                    player.clearAttrib(EMBLEM_WEALTH);
                }
            }
        }
        return value;
    }

    /***
     * Checks if the specified player is in a state of being able
     * to receive/be set a target.
     */
    private static boolean validTargetContester(Player p) {
        //A minimum of 30 combat is now required to receive targets in the minigame.
        if (p.skills().combatLevel() < 30) {
            return false;
        }

        return !(!p.isRegistered() || !(WildernessArea.inWilderness(p.tile())) || WildernessArea.wildernessLevel(p.tile()) <= 0 || p.isNullifyDamageLock() || p.dead() || p.isNeedsPlacement() || getPairFor(p).isPresent());
    }

    /**
     * The delay between each search for a new target.
     */
    private static final int TARGET_SEARCH_DELAY_SECONDS = GameServer.properties().production ? 30 : 5;

}
