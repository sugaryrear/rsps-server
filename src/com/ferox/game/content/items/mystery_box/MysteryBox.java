package com.ferox.game.content.items.mystery_box;

import com.ferox.game.content.items.mystery_box.impl.*;
import com.ferox.game.world.entity.AttributeKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An abstract system for mystery boxes.
 *
 * @author Patrick van Elderen | 27-8-2018 : 13:58
 * @see <a href="https://www.rune-server.ee/members/_Patrick_/">Rune-Server
 * profile</a>
 */
public abstract class MysteryBox {

    /**
     * Map of all the mystery boxes.
     */
    private static final Map<Integer, MysteryBox> MYSTERY_BOXES = new HashMap<>();

    /**
     * Handles loading the mystery boxes.
     */
    public static void load() {
        MysteryBox MYSTERY_BOX = new RegularMysteryBox();
        MysteryBox WEAPON_MYSTERY_BOX = new WeaponMysteryBox();
        MysteryBox ARMOUR_MYSTERY_BOX = new ArmourMysteryBox();
        MysteryBox DONATOR_MYSTERY_BOX = new DonatorMysteryBox();
        MysteryBox HALLOWEEN_MYSTERY_BOX = new HalloweenMysteryBox();
        MysteryBox LEGENDARY_MYSTERY_BOX = new LegendaryMysteryBox();

        MYSTERY_BOXES.put(MYSTERY_BOX.mysteryBoxId(), MYSTERY_BOX);
        MYSTERY_BOXES.put(WEAPON_MYSTERY_BOX.mysteryBoxId(), WEAPON_MYSTERY_BOX);
        MYSTERY_BOXES.put(ARMOUR_MYSTERY_BOX.mysteryBoxId(), ARMOUR_MYSTERY_BOX);
        MYSTERY_BOXES.put(DONATOR_MYSTERY_BOX.mysteryBoxId(), DONATOR_MYSTERY_BOX);
        MYSTERY_BOXES.put(HALLOWEEN_MYSTERY_BOX.mysteryBoxId(), HALLOWEEN_MYSTERY_BOX);
        MYSTERY_BOXES.put(LEGENDARY_MYSTERY_BOX.mysteryBoxId(), LEGENDARY_MYSTERY_BOX);
    }

    /** Handles getting the mystery box. */
    public static Optional<MysteryBox> getMysteryBox(int item) {
        return MYSTERY_BOXES.containsKey(item) ? Optional.of(MYSTERY_BOXES.get(item)) : Optional.empty();
    }

    /** The name of the mystery box. */
    protected abstract String name();

    /** The item identification of the mystery box. */
    protected abstract int mysteryBoxId();

    /**
     * Roll chances and return a reward
     */
    public abstract MboxItem rollReward(boolean keyOfDrops);

    /**
     * Collect mutliple tiers of rares/common arrays into one array
     */
    public abstract MboxItem[] allPossibleRewards();

    public abstract AttributeKey key();
}
