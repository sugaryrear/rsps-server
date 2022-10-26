package com.ferox.game.task.impl;


import com.ferox.game.content.TopPkersWeeklyEvent;

import com.ferox.game.task.Task;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.util.Color;
import com.ferox.util.Utils;
import org.apache.commons.text.WordUtils;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WeeklyRewards extends Task {

    /**
     * The amount of time in game cycles (600ms) that the event pulses at
     */
    private static final int INTERVAL = Utils.toCyclesOrDefault(1, 1, TimeUnit.HOURS);


    /**
     * Creates a new event to cycle through messages for the entirety of the runtime
     */
    public WeeklyRewards() {
        super("WeeklyRewards", INTERVAL);
    }

    @Override
    public void execute() {
        TopPkersWeeklyEvent.handleTopPkersRewards();
    }

}
