package com.ferox.game.content;


import com.ferox.game.world.World;
import com.ferox.util.Utils;

import java.io.Serializable;
import java.util.*;



public class TopBossPointsWeekly implements Serializable {

    private static final long serialVersionUID = -3335621107999346853L;

    /**
     * The total number of winners possible for each draw
     */
    private static final int MAXIMUM_WINNERS = 2;
    private static final int TopPkersOnline = 10;
    /**
     * Compares the value of an entry
     */
    private static final Comparator<Map.Entry<String, Integer>> HIGHEST_VALUE = Map.Entry.comparingByValue();

    /**
     * Contains all of the entries for the week
     */
    private Map<String, Integer> entries = new HashMap<>();

    /**
     * Winners of the previous week
     */
    private Map<String, Integer> winners;

    /**
     * The date the week is over. The initial date is set.
     */
    private Date date = Utils.getFutureDate(2022, Calendar.AUGUST, 11, 10, 59, 50);

    /**
     * Requests that an update be made on the entries variable.
     *
     * @param player the key being updated
     * @param amount the amount being added - ADDED IMPORTANT
     */
    public void update(String player, Integer amount) {
        if (!entries.containsKey(player)) {
            entries.put(player, amount);
        } else {
            int oldValue = entries.get(player);
            entries.replace(player, oldValue, oldValue + amount);
        }
        World.getServerData().setTopBossPointsWeekly(World.getServerData().getTopBossPointsWeekly());
        World.getServerData().processQueue();
        //World.getServerData().setTopPkers(this);
    }

    /**
     * Clears all entries in the map
     */
    public void clear() {
        entries.clear();
    }

    /**
     * Determines the weekly winner based on their contributions
     *
     * @return the winner
     */
    public ArrayList<Map.Entry<String, Integer>> getSortedResults() {//the total number to list
        List<Map.Entry<String, Integer>> list = new ArrayList<>(entries.entrySet());

        list.sort(HIGHEST_VALUE);
        Collections.reverse(list);
        return new ArrayList<>(list.subList(0, list.size()));
    }

    public ArrayList<Map.Entry<String, Integer>> getSortedResultsForWinners() {//the actual number of winners (1)
        List<Map.Entry<String, Integer>> list = new ArrayList<>(entries.entrySet());

        list.sort(HIGHEST_VALUE);
        Collections.reverse(list);
        return new ArrayList<>(list.subList(0, 1));
    }


    /**
     * The map containing all of the entries for the week
     *
     * @return the entries
     */
    public Map<String, Integer> getEntries() {
        return entries;
    }

    /**
     * The winner of the previous week
     *
     * @return the winner
     */
    public Map<String, Integer> getWinners() {
        return winners;
    }

    /**
     * Sets the new winner of the week
     *
     * @param winner the winner
     */
    public void setWinners(Map<String, Integer> winners) {
        this.winners = winners;
    }

    /**
     * The date the week started
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date of the week the event starts on
     *
     * @param date the date
     */
    public void setDate(Date date) {
        this.date = date;
    }
}
