package com.ferox.game.content.diary.wilderness;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public enum WildernessDiaryEntry {
    //Easy
    LOW_ALCH("Cast low alchemy at the fountain of rune"),
    WILDERNESS_LEVER("Enter the wilderness from the Edgeville lever"),
    WILDERNESS_ALTAR("Pray at the chaos altar in level 38 wilderness"),
    KILL_EARTH_WARRIOR("Kill some earth warriors: %totalstage", 50),
    DEMONIC_RUINS("Drink a prayer potion at the Demonic ruins"),
    KBD_LAIR("Enter the king black dragon lair"),
    MINE_IRON_WILD("Mine some iron ore"),

    //Medium
    MINE_MITHRIL_WILD("Mine some mithril ore"),
    WILDERNESS_GODWARS("Enter the wilderness god wars dungeon"),
    KILL_GREEN_DRAGON("Kill some green dragons: %totalstage", 128),
    KILL_BLOODVELD("Kill a bloodveld in the wilderness god wars dungeon"),
    MYSTERIOUS_EMBLEM("Sell a mysterious emblem to the emblem trader"),
    SMITH_MITHRIL_AXE("Smith an mithril axe in resource area"),
    WILDERNESS_AGILITY("Complete wilderness agility course: %totalstage", 50),

    //Hard
    CLAWS_OF_GUTHIX("Cast Claws of Guthix on another player"),
    SMITH_ADAMANT_SCIMITAR("Smith an adamant scimitar in the resource area"),
    CHAOS_ELEMENTAL("Kill Chaos Elemental: %totalstage", 30),
    CRAZY_ARCHAEOLOGIST("Kill Crazy Archaeologist: %totalstage", 30),
    CHAOS_FANATIC("Kill Chaos Fanatic: %totalstage", 30),
    SCORPIA("Kill Scorpia: %totalstage", 30),
    SPIRITUAL_WARRIOR("Kill a spiritual warrior in the wilderness god wars dungeon"),

    //Elite
    CALLISTO("Kill Callisto: %totalstage", 20),
    VENENATIS("Kill Venenatis: %totalstage", 20),
    VETION("Kill Vet'ion: %totalstage", 20),
    GHORROCK("Teleport to Ghorrock using the Ancient spellbook"),
    DARK_CRAB("Cook some dark crab at resource area: %totalstage", 350),
    SMITH_RUNE_SCIM_WILD("Smith a rune scimitar in resource area"),
    SPIRITUAL_MAGE("Kill a spiritual mage in the wilderness god wars dungeon"),
    MAGIC_LOG_WILD("Cut some magic logs in resource area: %totalstage", 200);

    private final String description;

    private final int maximumStages;

    public static final Set<WildernessDiaryEntry> SET = EnumSet.allOf(WildernessDiaryEntry.class);

    WildernessDiaryEntry(String description) {
        this(description, -1);
    }

    WildernessDiaryEntry(String description, int maximumStages) {
        this.description = description;
        this.maximumStages = maximumStages;
    }

    public final String getDescription() {
        return description;
    }

    public static final Optional<WildernessDiaryEntry> fromName(String name) {
        return SET.stream().filter(entry -> entry.name().equalsIgnoreCase(name)).findAny();
    }

    public int getMaximumStages() {
        return maximumStages;
    }
}
