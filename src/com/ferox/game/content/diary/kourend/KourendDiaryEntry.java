package com.ferox.game.content.diary.kourend;


import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public enum KourendDiaryEntry {
    //Easy
    MINE_ORE("Mine coal in Mount Karuulm Mine"),
    KILL_SANDCRAB("Kill 25 sand crabs: %totalstage", 25),

    //Medium
    KILL_A_LIZARDMAN("Kill 5 lizardman brutes: %totalstage", 5),
    CATCH_A_CHINCHOMPA("Catch a red chinchompa: %totalstage", 10),

    //Hard
    KILL_A_LIZARDMANSHAMAN("Kill a lizardman shaman"),
    ENTER_WC_GUILD("Enter the Woodcutting guild"),


    //Elite
    KILL_A_HYDRA("Kill a hydra in the Karuulm dungeon"),
    COMPLETE_A_RAID("Complete a Chambers of Xeric raid");


    private final String description;

    private final int maximumStages;

    public static final Set<KourendDiaryEntry> SET = EnumSet.allOf(KourendDiaryEntry.class);

    KourendDiaryEntry(String description) {
        this(description, -1);
    }

    KourendDiaryEntry(String description, int maximumStages) {
        this.description = description;
        this.maximumStages = maximumStages;
    }

    public final String getDescription() {
        return description;
    }

    public static final Optional<KourendDiaryEntry> fromName(String name) {
        return SET.stream().filter(entry -> entry.name().equalsIgnoreCase(name)).findAny();
    }

    public int getMaximumStages() {
        return maximumStages;
    }
}
