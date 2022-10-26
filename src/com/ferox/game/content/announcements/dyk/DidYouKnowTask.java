package com.ferox.game.content.announcements.dyk;

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

public class DidYouKnowTask extends Task {

    /**
     * The amount of time in game cycles (600ms) that the event pulses at
     */
    private static final int INTERVAL = Utils.toCyclesOrDefault(10, 1, TimeUnit.MINUTES);

    /**
     * A {@link java.util.Collection} of messages that are to be displayed
     */
    public static final List<String> MESSAGES = Utils.jsonArrayToList(Paths.get("data", "def", "server_announcements/", "did_you_know.json"), String[].class);

    /**
     * The index or position in the list that we're currently at
     */
    private int position;

    /**
     * Creates a new event to cycle through messages for the entirety of the runtime
     */
    public DidYouKnowTask() {
        super("DidYouKnowTask", INTERVAL);
    }

    @Override
    public void execute() {
        //We read the messages top to bottom, so most important messages should go at the top of did_you_know.json
        position++;
        if(MESSAGES == null) {
            stop();
            return;
        }
        if (position >= MESSAGES.size()) {
            position = 0;
        }
        
        List<String> messages = Arrays.asList(WordUtils.wrap(MESSAGES.get(position), 200).split("<br>"));
        messages.set(0, "<img=505> <col="+Color.PURPLE.getColorValue()+">" + messages.get(0) + (messages.size() > 1 ? "..." : "")); //Maroon and Purple are the best colours for this type of message

        for (Player player : World.getWorld().getPlayers()) {
            if (player != null) {
                boolean didYouKnowActivated = player.getAttribOr(AttributeKey.DID_YOU_KNOW, true);
                if (didYouKnowActivated)
                    messages.forEach(player::message);
            }
        }
    }

}
