package com.ferox.game.task.impl;

import com.ferox.game.content.TopPkersWeeklyEvent;
import com.ferox.game.content.dailyRewardsHandout;
import com.ferox.game.task.Task;
import com.ferox.util.Utils;

import java.util.concurrent.TimeUnit;

public class dailyRewards extends Task {

    /**
     * The amount of time in game cycles (600ms) that the event pulses at
     */
    private static final int INTERVAL = Utils.toCyclesOrDefault(1, 1, TimeUnit.MINUTES);


    /**
     * Creates a new event to cycle through messages for the entirety of the runtime
     */
    public dailyRewards() {
        super("dailyRewards", INTERVAL);
    }

    @Override
    public void execute() {
        dailyRewardsHandout.handleRewards();
    }

}
