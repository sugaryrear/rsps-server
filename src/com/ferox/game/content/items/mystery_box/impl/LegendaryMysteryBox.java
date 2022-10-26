package com.ferox.game.content.items.mystery_box.impl;

import com.ferox.GameServer;
import com.ferox.game.content.items.mystery_box.MboxItem;
import com.ferox.game.content.items.mystery_box.MysteryBox;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;

public class LegendaryMysteryBox extends MysteryBox {

    public static final int LEGENDARY_MYSTERY_BOX = 16454;

    @Override
    protected String name() {
        return "Legendary mystery box";
    }

    @Override
    public int mysteryBoxId() {
        return LEGENDARY_MYSTERY_BOX;
    }

    private static final int EXTREME_RARE_ROLL = 15;
    private static final int RARE_ROLL = 10;
    private static final int UNCOMMON_ROLL = 5;

    private static final MboxItem[] EXTREMELY_RARE = new MboxItem[] {
        new MboxItem(TWISTED_BOW).broadcastWorldMessage(true),
        new MboxItem(SCYTHE_OF_VITUR).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.NIFFLER).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.FAWKES).broadcastWorldMessage(true),
        new MboxItem(WAMPA).broadcastWorldMessage(true),
        new MboxItem(ANCESTRAL_HAT).broadcastWorldMessage(true),
        new MboxItem(ANCESTRAL_ROBE_TOP).broadcastWorldMessage(true),
        new MboxItem(ANCESTRAL_ROBE_BOTTOM).broadcastWorldMessage(true),
        new MboxItem(VESTAS_CHAINBODY).broadcastWorldMessage(true),
        new MboxItem(VESTAS_PLATESKIRT).broadcastWorldMessage(true),
    };

    private static final MboxItem[] RARE = new MboxItem[] {
        new MboxItem(DRAGON_CLAWS_OR).broadcastWorldMessage(true),
        new MboxItem(ARMADYL_GODSWORD_OR).broadcastWorldMessage(true),
        new MboxItem(JUSTICIAR_FACEGUARD).broadcastWorldMessage(true),
        new MboxItem(JUSTICIAR_CHESTGUARD).broadcastWorldMessage(true),
        new MboxItem(JUSTICIAR_LEGGUARDS).broadcastWorldMessage(true),
        new MboxItem(AMULET_OF_TORTURE_OR),
        new MboxItem(NECKLACE_OF_ANGUISH_OR),
        new MboxItem(TORMENTED_BRACELET_OR),
        new MboxItem(ELDER_MAUL),
        new MboxItem(ARCANE_SPIRIT_SHIELD),
        new MboxItem(CustomItemIdentifiers.GRIM_REAPER_PET).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.BARRELCHEST_PET).broadcastWorldMessage(true),
    };

    private static final MboxItem[] UNCOMMON = new MboxItem[] {
        new MboxItem(SANGUINESTI_STAFF),
        new MboxItem(KODAI_WAND),
        new MboxItem(GHRAZI_RAPIER),
        new MboxItem(BLADE_OF_SAELDOR),
        new MboxItem(INQUISITORS_MACE),
        new MboxItem(BANDOS_GODSWORD_OR),
        new MboxItem(SARADOMIN_GODSWORD_OR),
        new MboxItem(ZAMORAK_GODSWORD_OR),
        new MboxItem(INQUISITORS_GREAT_HELM),
        new MboxItem(INQUISITORS_HAUBERK),
        new MboxItem(INQUISITORS_PLATESKIRT),

    };

    private static final MboxItem[] COMMON = new MboxItem[] {
        new MboxItem(ARMADYL_GODSWORD),
        new MboxItem(DRAGON_CLAWS),
        new MboxItem(HEAVY_BALLISTA),
        new MboxItem(DINHS_BULWARK),
        new MboxItem(SARADOMINS_BLESSED_SWORD),
        new MboxItem(DRAGON_HUNTER_CROSSBOW),
        new MboxItem(DRAGON_HUNTER_LANCE),
        new MboxItem(SPECTRAL_SPIRIT_SHIELD),
        new MboxItem(DRAGON_WARHAMMER),
        new MboxItem(TOXIC_STAFF_OF_THE_DEAD),
        new MboxItem(TOXIC_BLOWPIPE),
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
        return AttributeKey.LEGENDARY_MYSTERY_BOXES_OPENED;
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
            if (Utils.rollDie(EXTREME_RARE_ROLL, 1)) {
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
