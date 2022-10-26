package com.ferox.game.content.diary.falador;


import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public enum FaladorDiaryEntry {
    //Easy
    CHOP_WILLOW("Chop willow logs: %totalstage", 85),
    PICK_FLAX("Pick flax from the garden: %totalstage", 85),
    PICKPOCKET_MAN("Pickpocket a man for some money: %totalstage", 50),
    COMPOST_BUCKET("Fill buckets with compost: %totalstage", 85),
    GRAPPLE_NORTH_WALL("Grapple the north wall shortcut"),
    TELEPORT_TO_FALADOR("Teleport to Falador using the proper runes"),

    //Medium
    KILL_WHITE_KNIGHT("Kill white knights: %totalstage", 35),
    RECOLOR_GRACEFUL("Recolor a graceful piece (Trade Grace)"),
    CHOP_YEW("Chop yew logs: %totalstage", 100),
    PICKPOCKET_MASTER_FARMER_FAL("Pickpocket a master farmer for some seeds: %totalstage", 250),
    ALTAR_OF_GUTHIX("Pray at the Guthix altar in Taverly wearing full Initiate"),
    CRAFT_MIND("Craft more than 100 mind runes in one go: %totalstage", 30),

    //Hard
    PICK_POSION_BERRY("Pick poison berries: %totalstage", 30),
    KILL_GIANT_MOLE("Kill Giant moles under the garden (Dig): %totalstage", 45),
    MINE_MITHRIL("Mine some mithril ore at the Crafting Guild: %totalstage", 84),

    //Elite
    HARVEST_TORSTOL("Harvest torstols: %totalstage", 200),
    MINE_GEM_FAL("Mine some gems from a gem rock in the Crafting Guild: %totalstage", 300);

    private final String description;

    private final int maximumStages;

    public static final Set<FaladorDiaryEntry> SET = EnumSet.allOf(FaladorDiaryEntry.class);

    FaladorDiaryEntry(String description) {
        this(description, -1);
    }

    FaladorDiaryEntry(String description, int maximumStages) {
        this.description = description;
        this.maximumStages = maximumStages;
    }

    public final String getDescription() {
        return description;
    }

    public static final Optional<FaladorDiaryEntry> fromName(String name) {
        return SET.stream().filter(entry -> entry.name().equalsIgnoreCase(name)).findAny();
    }

    public int getMaximumStages() {
        return maximumStages;
    }
}
