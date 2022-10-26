package com.ferox.game.content.items.mystery_box.impl;

import com.ferox.GameServer;
import com.ferox.game.content.items.mystery_box.MboxItem;
import com.ferox.game.content.items.mystery_box.MysteryBox;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import static com.ferox.util.ItemIdentifiers.*;

public class DonatorMysteryBox extends MysteryBox {

    public static final int DONATOR_MYSTERY_BOX = 30185;

    @Override
    protected String name() {
        return "Donator mystery box";
    }

    @Override
    public int mysteryBoxId() {
        return DONATOR_MYSTERY_BOX;
    }

    private static final int EXTREME_ROLL = 28;
    private static final int RARE_ROLL = 14;
    private static final int UNCOMMON_ROLL = 7;

    private static final MboxItem[] EXTREMELY_RARE = new MboxItem[] {
        new MboxItem(CustomItemIdentifiers.GRIM_REAPER_PET).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.BARRELCHEST_PET).broadcastWorldMessage(true),
        new MboxItem(ANCESTRAL_HAT).broadcastWorldMessage(true),
        new MboxItem(ANCESTRAL_ROBE_TOP).broadcastWorldMessage(true),
        new MboxItem(ANCESTRAL_ROBE_BOTTOM).broadcastWorldMessage(true),
        new MboxItem(VOLATILE_NIGHTMARE_STAFF).broadcastWorldMessage(true),
        new MboxItem(HARMONISED_NIGHTMARE_STAFF).broadcastWorldMessage(true),
        new MboxItem(ELDRITCH_NIGHTMARE_STAFF).broadcastWorldMessage(true),
        new MboxItem(STATIUSS_PLATEBODY),
        new MboxItem(STATIUSS_PLATELEGS),
        new MboxItem(MORRIGANS_LEATHER_BODY),
        new MboxItem(MORRIGANS_LEATHER_CHAPS),
        new MboxItem(ARCANE_SPIRIT_SHIELD),
        new MboxItem(ELDER_MAUL),
    };

    private static final MboxItem[] RARE = new MboxItem[] {
        new MboxItem(MORRIGANS_COIF),
        new MboxItem(_3RD_AGE_LONGSWORD).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_BOW).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_WAND).broadcastWorldMessage(true),
        new MboxItem(ZURIELS_ROBE_TOP),
        new MboxItem(ZURIELS_ROBE_BOTTOM),
        new MboxItem(VESTAS_SPEAR),
        new MboxItem(STATIUSS_FULL_HELM),
        new MboxItem(STATIUSS_WARHAMMER),
        new MboxItem(SANGUINESTI_STAFF),
        new MboxItem(GHRAZI_RAPIER),
        new MboxItem(DRAGON_CLAWS),
        new MboxItem(TOXIC_BLOWPIPE),
        new MboxItem(NEITIZNOT_FACEGUARD),
    };

    private static final MboxItem[] UNCOMMON = new MboxItem[] {
        new MboxItem(HEAVY_BALLISTA),
        new MboxItem(ARMADYL_CROSSBOW),
        new MboxItem(MAGMA_HELM),
        new MboxItem(TANZANITE_HELM),
        new MboxItem(ARMADYL_GODSWORD),
        new MboxItem(BRIMSTONE_RING),
        new MboxItem(TOXIC_STAFF_OF_THE_DEAD),
        new MboxItem(BANDOS_CHESTPLATE),
        new MboxItem(BANDOS_TASSETS),
        new MboxItem(DRAGON_WARHAMMER),
    };

    private static final MboxItem[] COMMON = new MboxItem[] {
        new MboxItem(BANDOS_GODSWORD),
        new MboxItem(SARADOMIN_GODSWORD),
        new MboxItem(ZAMORAK_GODSWORD),
        new MboxItem(ARMADYL_CHAINSKIRT),
        new MboxItem(ARMADYL_CHESTPLATE),
        new MboxItem(MORRIGANS_JAVELIN, 75),
        new MboxItem(MORRIGANS_THROWING_AXE,75),
        new MboxItem(DRAGONFIRE_WARD),
        new MboxItem(SERPENTINE_HELM),
        new MboxItem(ANCIENT_WYVERN_SHIELD),
        new MboxItem(DRAGONFIRE_SHIELD),
        new MboxItem(BLOOD_MONEY,500),
        new MboxItem(DINHS_BULWARK),
    };

    private MboxItem[] allRewardsCached;
    public MboxItem[] allPossibleRewards() {
        if (allRewardsCached == null) {
            ArrayList<MboxItem> mboxItems = new ArrayList<>();
            mboxItems.addAll(Arrays.asList(EXTREMELY_RARE));
            mboxItems.addAll(Arrays.asList(RARE));
            mboxItems.addAll(Arrays.asList(UNCOMMON));
            mboxItems.addAll(Arrays.asList(COMMON));
            allRewardsCached = mboxItems.toArray(new MboxItem[0]);
        }
        return allRewardsCached;
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.DONATOR_MYSTERY_BOXES_OPENED;
    }

    @Override
    public MboxItem rollReward(boolean keyOfDrops) {
        if(keyOfDrops) {
            if (Utils.rollDie(GameServer.properties().nerfDropRateBoxes ? 10 : 5, 1)) {
                return Utils.randomElement(EXTREMELY_RARE);
            } else {
                return Utils.randomElement(RARE);
            }
        } else {
            if (Utils.rollDie(EXTREME_ROLL, 1)) {
                return Utils.randomElement(EXTREMELY_RARE);
            } else if (Utils.rollDie(RARE_ROLL, 1)) {
                return Utils.randomElement(RARE);
            } else if (Utils.rollDie(UNCOMMON_ROLL, 1)) {
                return Utils.randomElement(UNCOMMON);
            } else {
                return Utils.randomElement(COMMON);
            }
        }
    }
}
