package com.ferox.game.content.areas.wilderness.content.todays_top_pkers;

import com.ferox.game.GameEngine;
import com.ferox.game.content.*;
import com.ferox.game.task.Task;
import com.ferox.game.task.TaskManager;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.save.PlayerSave;
import com.ferox.game.world.items.Item;
import com.ferox.util.Color;
import com.ferox.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.*;

import static com.ferox.game.world.entity.AttributeKey.*;
import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.NpcIdentifiers.CHAOS_ELEMENTAL;

/**
 * Created by Kaleem on 24/11/2017.
 */
public final class TopPkers {


    public void handleBosses(Player killer, int id){
        int bossrating = 0;
        if(id == CHAOS_ELEMENTAL){
            bossrating += 1;
        }
        World.getServerData().getTopBossPoints().update(killer.getUsername(), bossrating);
        World.getServerData().getTopBossPointsWeekly().update(killer.getUsername(), bossrating);
        World.getServerData().getTopBossPointsDaily().update(killer.getUsername(), bossrating);


        if(bossrating >0)
            killer.message("You gain "+bossrating+" boss rating from killing the boss!");
    }

    private static final Logger logger = LoggerFactory.getLogger(TopPkers.class);

    public static final TopPkers SINGLETON = new TopPkers();

    private static final LocalTime ANNOUNCEMENT_TIME = LocalTime.of(22, 0, 0); //10PM

    private static final int ANNOUNCEMENT_AMOUNT = 3;

    private final Map<String, Integer> totalKills = new HashMap<>();

    public final Task announcementTask = new Task("TopPkersAnnouncementTask") {
        private boolean announced = false;

        @Override
        protected void execute() {
            LocalTime currentTime = LocalTime.now();
            if (currentTime.isAfter(ANNOUNCEMENT_TIME)) {
                if (!announced) {
                    announce();
                    announced = true;
                }
            } else {
                announced = false;
            }
        }
    };

    public void init() {
        TaskManager.submit(announcementTask);
    }

    public void increase(String username) {
        totalKills.merge(username, 1, Integer::sum);
    }

    public void announce() {
        broadcast("<sprite=780> <col=800000>Today's top PKers are now being announced:");

        for (int i = 0; i < ANNOUNCEMENT_AMOUNT; i++) {
            var entry = getAndTakeTop();
            var position = i + 1;
            var details = "Nobody";

            var sprite = position == 1 ? 203 : position == 2 ? 202 : 201;

            if (entry != null) {
                var reward = position == 1 ? new Item(BLOOD_MONEY_CASKET) : position == 2 ? new Item(14524) : new Item(7956);
                logger.trace("{} was selected as todays {} best PK-er, being awarded with a {}.", entry.getUsername(), position, reward.name());
                String rewardMsg = "who has been awarded with a " + reward.name() + "!";
                details = Color.BLUE.tag() + "" + entry.getUsername() + " " + Color.BLACK.tag() + "with " + entry.getKills() + " kills - " + rewardMsg;
                give(entry, position, reward);
            }

            broadcast("<sprite=" + sprite + ">" + position + getSuffix(position) + ": " + details);
        }

        totalKills.clear();
    }

    private void broadcast(String message) {
        World.getWorld().getPlayers().forEach(player -> player.message(message));
    }

    private void give(KillEntry entry, int position, Item reward) {
        String playerName = entry.getUsername();
        Optional<Player> playerToGive = World.getWorld().getPlayerByName(playerName);
        if (playerToGive.isEmpty()) {
            Player player = new Player();
            player.setUsername(playerName);
            GameEngine.getInstance().submitLowPriority(() -> {
                try {
                    if (PlayerSave.loadOfflineWithoutPassword(player)) {
                        player.putAttrib(TOP_PKER_REWARD_UNCLAIMED, true);
                        player.putAttrib(TOP_PKER_POSITION, position);
                        player.putAttrib(TOP_PKER_REWARD, reward);
                        PlayerSave.save(player);
                    } else {
                        logger.error("Something went wrong offline reward for offline Player " + player.getUsername());
                    }
                } catch (Exception e) {
                    logger.error("Something went wrong offline reward for offline Player " + player.getUsername());
                    logger.error("TopPkers give() error: ", e);
                }
            });
            return;
        }
        playerToGive.get().inventory().addOrBank(reward);
        playerToGive.get().message("Congratulations, you finished " + position + getSuffix(position) + " in the top PKers!");
    }

    public void checkForReward(Player player) {
        var position = player.<Integer>getAttribOr(TOP_PKER_POSITION, 0);
        var rewardUnclaimed = player.<Boolean>getAttribOr(TOP_PKER_REWARD_UNCLAIMED, false);

        //Player is infact a top pker
        if (position > 0) {
         //   System.out.println("enter");
            //Reward wasn't claimed yet, lets claim.
            if (rewardUnclaimed) {
              //  System.out.println("enter2");
                Item reward = player.getAttribOr(TOP_PKER_REWARD, null);
                if (reward != null) {
                 //   System.out.println("enter3");
                    player.inventory().addOrBank(reward);
                    player.message("Congratulations, you finished " + position + getSuffix(position) + " in todays top PKers!");
                    player.clearAttrib(TOP_PKER_POSITION);
                    player.clearAttrib(TOP_PKER_REWARD_UNCLAIMED);
                    player.clearAttrib(TOP_PKER_REWARD);
                }
            }
        }
    }

    public KillEntry getAndTakeTop() {
        KillEntry top = getTop();
        if (top != null) {
            totalKills.remove(top.getUsername());
        }
        return top;
    }

    public KillEntry getTop() {
        Optional<Map.Entry<String, Integer>> entry = totalKills.entrySet().stream().max(Map.Entry.comparingByValue());
        return entry.map(stringIntegerEntry -> new KillEntry(stringIntegerEntry.getKey(), stringIntegerEntry.getValue())).orElse(null);
    }


    /**
     *
     * initially opening the leaderboard shows all time pkers
     * @param player
     */
    public void openLeaderboard(Player player, String which) {
//        if (totalKills.isEmpty()) {
//            player.sendScroll("<col=800000>Today's Top Pkers", "Nobody");
//            return;
//        }
//
//        //System.out.println(totalKills.toString());
//        List<String> info = new ArrayList<>();
//
//        totalKills.forEach((name, killCount) -> info.add(name + " Kills - " + killCount));
//
//        player.sendScroll("<col=800000>Today's Top Pkers", info.toArray(new String[0]));

        player.getPacketSender().sendFrame126("", 50193);

        //reset all buttons
        for(int i = 0; i < 10; i++){
            player.getPacketSender().sendConfig(1800+i, 0);
        }
        TopPkersWeekly wogw = World.getServerData().getTopPkersWeekly();
        TopPkersDaily dailyend = World.getServerData().getTopPkersDaily();

       Date enddate_week =  wogw.getDate();
        Date enddate_daily =  dailyend.getDate();

        Date date = new Date();
        String result = Utils.get_duration(date, enddate_week);
        String result_daily = Utils.get_duration(date, enddate_daily);
        player.getPacketSender().sendString(50103, "#1 in each daily category will receive a mystery box!");
        player.getPacketSender().sendString(50193, "");
        player.getInterfaceManager().open(50190);
        for (int i = 0; i < 10; i++) {
            player.getPacketSender().sendFrame126("", 50200 + i);
            player.getPacketSender().sendFrame126("", 50210 + i);
            player.getPacketSender().sendFrame126("", 50220 + i);
        }
        player.getPacketSender().sendString(50104, "");

switch (which){


    case "alltimepkers":
        player.getPacketSender().sendFrame126("All Time Leaderboard", 50193);
        int rank = 0;
        player.getPacketSender().sendConfig(1801, 1);
//        World.getServerData().setTopPkers(World.getServerData().getTopPkers());
//        World.getServerData().processQueue();

        player.getPacketSender().sendFrame126("Kills", 50101);
        com.ferox.game.content.TopPkers toppkers = World.getServerData().getTopPkers();
        ArrayList<Map.Entry<String, Integer>> winners = toppkers.getSortedResults();

        Map<String, Integer> winnerMap = new HashMap<>();
        winners.forEach(entry -> winnerMap.put(entry.getKey(), entry.getValue()));


        for(int i = 0 ; i < winners.size(); i++){
            if(player.getUsername().equals(winners.get(i).getKey())){
                rank = i + 1;
            }
        }

        int killcount = player.getAttribOr(AttributeKey.PLAYER_KILLS, 0);
        player.getPacketSender().sendString(50104, "Your current kills: @red@"+killcount+"@lre@ / "+(rank == 0? "unranked" : "rank #"+rank+"")+"");



        int i = 1;

        for (Map.Entry<String, Integer> entry : winners) {
            player.getPacketSender().sendFrame126("#"+i+"", 50199 + i);
            player.getPacketSender().sendFrame126(entry.getKey(), 50209 + i);
            player.getPacketSender().sendFrame126(entry.getValue()+"", 50219 + i);
            i++;
        }

        break;

    case "dailypkers":
        player.getPacketSender().sendFrame126("@blu@Current day ends in : @gre@" +result_daily, 50193);

        rank = 0;
        player.getPacketSender().sendConfig(1806, 1);
//        World.getServerData().setTopPkersDaily(World.getServerData().getTopPkersDaily());
//        World.getServerData().processQueue();

        player.getPacketSender().sendFrame126("Kills", 50101);
        TopPkersDaily toppkersdaily= World.getServerData().getTopPkersDaily();
        ArrayList<Map.Entry<String, Integer>> winnersdailypkers = toppkersdaily.getSortedResults();

        Map<String, Integer> winnerMapdailypkers = new HashMap<>();
        winnersdailypkers.forEach(entry -> winnerMapdailypkers.put(entry.getKey(), entry.getValue()));

        int amountofdailykills = 0;
        for(int i4 = 0 ; i4 < winnersdailypkers.size(); i4++){
            if(player.getUsername().equals(winnersdailypkers.get(i4).getKey())){
                rank = i4 + 1;
                amountofdailykills = winnersdailypkers.get(i4).getValue();
            }
        }


        player.getPacketSender().sendString(50104, "Your current daily kills: @red@"+amountofdailykills+"@lre@ / "+(rank == 0? "unranked" : "rank #"+rank+"")+"");



        i = 1;

        for (Map.Entry<String, Integer> entry : winnersdailypkers) {
            player.getPacketSender().sendFrame126("#"+i+"", 50199 + i);
            player.getPacketSender().sendFrame126(entry.getKey(), 50209 + i);
            player.getPacketSender().sendFrame126(entry.getValue()+"", 50219 + i);
            i++;
        }

        break;

    case "weeklybosspoints":
        player.getPacketSender().sendFrame126("@blu@Current week ends in :  @gre@"+result, 50193);
    rank = 0;
        player.getPacketSender().sendConfig(1802, 1);
//        World.getServerData().setTopBossPointsWeekly(World.getServerData().getTopBossPointsWeekly());
//        World.getServerData().processQueue();

        player.getPacketSender().sendFrame126("Most Rating Boss kills", 50101);
        TopBossPointsWeekly toppbosspointsweekly = World.getServerData().getTopBossPointsWeekly();
        ArrayList<Map.Entry<String, Integer>> winnersbosspointsweekly = toppbosspointsweekly.getSortedResults();

        Map<String, Integer> winnerMapbosspointsweekly = new HashMap<>();
        winnersbosspointsweekly.forEach(entry -> winnerMapbosspointsweekly.put(entry.getKey(), entry.getValue()));

            int amountofbosspointsweekly = 0;
        for(int i2 = 0 ; i2 < winnersbosspointsweekly.size(); i2++){
            if(player.getUsername().equals(winnersbosspointsweekly.get(i2).getKey())){
                rank = i2 + 1;
                amountofbosspointsweekly = winnersbosspointsweekly.get(i2).getValue();
            }
        }


        player.getPacketSender().sendString(50104, "Your current weekly boss rating: @red@"+amountofbosspointsweekly+"@lre@ / "+(rank == 0? "unranked" : "rank #"+rank+"")+"");



        i = 1;

        for (Map.Entry<String, Integer> entry : winnersbosspointsweekly) {
            player.getPacketSender().sendFrame126("#"+i+"", 50199 + i);
            player.getPacketSender().sendFrame126(entry.getKey(), 50209 + i);
            player.getPacketSender().sendFrame126(entry.getValue()+"", 50219 + i);
            i++;
        }

        break;
    case "dailybosspoints":
        player.getPacketSender().sendFrame126("@blu@Current day ends in :  @gre@"+result_daily, 50193);
        rank = 0;
        player.getPacketSender().sendConfig(1807, 1);
//        World.getServerData().setTopBossPointsDaily(World.getServerData().getTopBossPointsDaily());
//        World.getServerData().processQueue();

        player.getPacketSender().sendFrame126("Most Rating Boss kills", 50101);
        TopBossPointsDaily toppbosspointsdaily = World.getServerData().getTopBossPointsDaily();
        ArrayList<Map.Entry<String, Integer>> winnersbosspointsdaily = toppbosspointsdaily.getSortedResults();

        Map<String, Integer> winnerMapbosspointsdaily = new HashMap<>();
        winnersbosspointsdaily.forEach(entry -> winnerMapbosspointsdaily.put(entry.getKey(), entry.getValue()));

        int amountofbosspointsdaily = 0;
        for(int i5 = 0 ; i5 < winnersbosspointsdaily.size(); i5++){
            if(player.getUsername().equals(winnersbosspointsdaily.get(i5).getKey())){
                rank = i5 + 1;
                amountofbosspointsdaily = winnersbosspointsdaily.get(i5).getValue();
            }
        }


        player.getPacketSender().sendString(50104, "Your current daily boss rating: @red@"+amountofbosspointsdaily+"@lre@ / "+(rank == 0? "unranked" : "rank #"+rank+"")+"");



        i = 1;

        for (Map.Entry<String, Integer> entry : winnersbosspointsdaily) {
            player.getPacketSender().sendFrame126("#"+i+"", 50199 + i);
            player.getPacketSender().sendFrame126(entry.getKey(), 50209 + i);
            player.getPacketSender().sendFrame126(entry.getValue()+"", 50219 + i);
            i++;
        }

        break;
    case "weeklypkers":
        player.getPacketSender().sendFrame126("@blu@Current day ends in :  @gre@"+result, 50193);
        rank = 0;
        player.getPacketSender().sendConfig(1800, 1);
//        World.getServerData().setTopPkersWeekly(World.getServerData().getTopPkersWeekly());
//        World.getServerData().processQueue();

        player.getPacketSender().sendFrame126("Kills", 50101);
        TopPkersWeekly toppkersweekly = World.getServerData().getTopPkersWeekly();
        ArrayList<Map.Entry<String, Integer>> winnersweekly = toppkersweekly.getSortedResults();

        Map<String, Integer> winnerMapweekly = new HashMap<>();
        winnersweekly.forEach(entry -> winnerMapweekly.put(entry.getKey(), entry.getValue()));

        int amountofpkersweekly = 0;
        for(int i2 = 0 ; i2 < winnersweekly.size(); i2++){
            if(player.getUsername().equals(winnersweekly.get(i2).getKey())){
                rank = i2 + 1;
                amountofpkersweekly = winnersweekly.get(i2).getValue();
            }
        }


        player.getPacketSender().sendString(50104, "Your current weekly kills: @red@"+amountofpkersweekly+"@lre@ / "+(rank == 0? "unranked" : "rank #"+rank+"")+"");



        i = 1;

        for (Map.Entry<String, Integer> entry : winnersweekly) {
            player.getPacketSender().sendFrame126("#"+i+"", 50199 + i);
            player.getPacketSender().sendFrame126(entry.getKey(), 50209 + i);
            player.getPacketSender().sendFrame126(entry.getValue()+"", 50219 + i);
            i++;
        }

        break;
    case "alltimebosspoints":
        player.getPacketSender().sendFrame126("All Time Leaderboard", 50193);

        rank = 0;
        player.getPacketSender().sendConfig(1803, 1);
//        World.getServerData().setTopBossPoints(World.getServerData().getTopBossPoints());
//        World.getServerData().processQueue();

        player.getPacketSender().sendFrame126("Most Rating Boss kills", 50101);
        TopBossPoints topbosspoints = World.getServerData().getTopBossPoints();
        ArrayList<Map.Entry<String, Integer>> winnersfortopbosspoints = topbosspoints.getSortedResults();

        Map<String, Integer> winnerMapfortopbosspoints = new HashMap<>();
        winnersfortopbosspoints.forEach(entry -> winnerMapfortopbosspoints.put(entry.getKey(), entry.getValue()));
        int amountofbosspoints = 0;

        for(int i3 = 0 ; i3 < winnersfortopbosspoints.size(); i3++){
            if(player.getUsername().equals(winnersfortopbosspoints.get(i3).getKey())){
                rank = i3 + 1;
                amountofbosspoints= winnersfortopbosspoints.get(i3).getValue();
            }
        }


        player.getPacketSender().sendString(50104, "Your current boss rating: @red@"+amountofbosspoints+"@lre@ / "+(rank == 0? "unranked" : "rank #"+rank+"")+"");



        i = 1;

        for (Map.Entry<String, Integer> entry : winnersfortopbosspoints) {
            player.getPacketSender().sendFrame126("#"+i+"", 50199 + i);
            player.getPacketSender().sendFrame126(entry.getKey(), 50209 + i);
            player.getPacketSender().sendFrame126(entry.getValue()+"", 50219 + i);
            i++;
        }

        break;
    case "alltimexp":
        player.getPacketSender().sendFrame126("All Time Leaderboard", 50193);
        rank = 0;
        player.getPacketSender().sendConfig(1805, 1);
//        World.getServerData().setTopXP(World.getServerData().getTopXP());
//        World.getServerData().processQueue();

        player.getPacketSender().sendFrame126("Total XP", 50101);
        TopXP topxp = World.getServerData().getTopXP();
        ArrayList<Map.Entry<String, Long>> topxpwinners = topxp.getSortedResults();

        Map<String, Long> winnerMaptopxp = new HashMap<>();
        topxpwinners.forEach(entry -> winnerMaptopxp.put(entry.getKey(), entry.getValue()));

        long thepersonstopxp = 0;
        for(int i6 = 0 ; i6 < topxpwinners.size(); i6++){
            if(player.getUsername().equals(topxpwinners.get(i6).getKey())){
                rank = i6 + 1;
                thepersonstopxp = topxpwinners.get(i6).getValue();
            }
        }


        player.getPacketSender().sendString(50104, "Your current XP: @red@"+Utils.insertCommasToNumber(String.valueOf(thepersonstopxp))+"@lre@ / "+(rank == 0? "unranked" : "rank #"+rank+"")+"");



     i = 1;

        for (Map.Entry<String, Long> entry : topxpwinners) {
            player.getPacketSender().sendFrame126("#"+i+"", 50199 + i);
            player.getPacketSender().sendFrame126(entry.getKey(), 50209 + i);
            player.getPacketSender().sendFrame126(Utils.insertCommasToNumber(String.valueOf(entry.getValue()))+"", 50219 + i);
            i++;
        }

        break;

    case "weeklyxp":
        player.getPacketSender().sendFrame126("@blu@Current week ends in :  @gre@"+result, 50193);
        rank = 0;
        player.getPacketSender().sendConfig(1804, 1);
//        World.getServerData().setTopXPWeekly(World.getServerData().getTopXPWeekly());
//        World.getServerData().processQueue();

        player.getPacketSender().sendFrame126("Total XP", 50101);
        TopXPWeekly topxpweekly = World.getServerData().getTopXPWeekly();
        ArrayList<Map.Entry<String, Long>> topxpwinnersweekly = topxpweekly.getSortedResults();

        Map<String, Long> winnerMaptopxpweekly = new HashMap<>();
        topxpwinnersweekly.forEach(entry -> winnerMaptopxpweekly.put(entry.getKey(), entry.getValue()));

     thepersonstopxp = 0;
        for(int i6 = 0 ; i6 < topxpwinnersweekly.size(); i6++){
            if(player.getUsername().equals(topxpwinnersweekly.get(i6).getKey())){
                rank = i6 + 1;
                thepersonstopxp = topxpwinnersweekly.get(i6).getValue();
            }
        }


        player.getPacketSender().sendString(50104, "Your weekly top XP: @red@"+Utils.insertCommasToNumber(String.valueOf(thepersonstopxp))+"@lre@ / "+(rank == 0? "unranked" : "rank #"+rank+"")+"");



        i = 1;

        for (Map.Entry<String, Long> entry : topxpwinnersweekly) {
            player.getPacketSender().sendFrame126("#"+i+"", 50199 + i);
            player.getPacketSender().sendFrame126(entry.getKey(), 50209 + i);
            player.getPacketSender().sendFrame126(Utils.insertCommasToNumber(String.valueOf(entry.getValue()))+"", 50219 + i);
            i++;
        }

        break;
    case "dailyxp":
        player.getPacketSender().sendFrame126("@blu@Current day ends in : @gre@" +result_daily, 50193);

        rank = 0;
        player.getPacketSender().sendConfig(1808, 1);
//        World.getServerData().setTopXPDaily(World.getServerData().getTopXPDaily());
//        World.getServerData().processQueue();

        player.getPacketSender().sendFrame126("Total XP", 50101);
        TopXPDaily topxpdaily= World.getServerData().getTopXPDaily();
        ArrayList<Map.Entry<String, Long>> winnersdailyxp = topxpdaily.getSortedResults();

        Map<String, Long> winnerMapdailyxp = new HashMap<>();
        winnersdailyxp.forEach(entry -> winnerMapdailyxp.put(entry.getKey(), entry.getValue()));

       long amountoftotalxp = 0;
        for(int i6 = 0 ; i6 < winnersdailyxp.size(); i6++){
            if(player.getUsername().equals(winnersdailyxp.get(i6).getKey())){
                rank = i6 + 1;
                amountoftotalxp = winnersdailyxp.get(i6).getValue();
            }
        }


        player.getPacketSender().sendString(50104, "Your current daily XP: @red@"+Utils.insertCommasToNumber(String.valueOf(amountoftotalxp))+"@lre@ / "+(rank == 0? "unranked" : "rank #"+rank+"")+"");



        i = 1;

        for (Map.Entry<String, Long> entry : winnersdailyxp) {
            player.getPacketSender().sendFrame126("#"+i+"", 50199 + i);
            player.getPacketSender().sendFrame126(entry.getKey(), 50209 + i);
            player.getPacketSender().sendFrame126(Utils.insertCommasToNumber(String.valueOf(entry.getValue()))+"", 50219 + i);
            i++;
        }

        break;


}

    }


    private static String getSuffix(int position) {
        if (position == 1) {
            return "st";
        } else if (position == 2) {
            return "nd";
        } else if (position == 3) {
            return "rd";
        }
        return "th";
    }

}
