package com.ferox.game.content.items.mystery_box.impl;

import com.ferox.GameServer;
import com.ferox.game.content.items.mystery_box.MboxItem;
import com.ferox.game.content.items.mystery_box.MysteryBox;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import static com.ferox.util.CustomItemIdentifiers.DRAGON_CLAWS_OR;
import static com.ferox.util.ItemIdentifiers.*;

public class WeaponMysteryBox extends MysteryBox {

    public static final int WEAPON_MYSTERY_BOX = 16451;

    @Override
    protected String name() {
        return "Weapon mystery box";
    }

    @Override
    public int mysteryBoxId() {
        return WEAPON_MYSTERY_BOX;
    }

    private static final int EXTREME_ROLL = 28;
    private static final int RARE_ROLL = 14;
    private static final int UNCOMMON_ROLL = 7;

    private static final MboxItem[] EXTREMELY_RARE = new MboxItem[]{
        new MboxItem(SANGUINESTI_STAFF).broadcastWorldMessage(true),
        new MboxItem(STATIUSS_WARHAMMER).broadcastWorldMessage(true),
        new MboxItem(TOXIC_STAFF_OF_THE_DEAD),
        new MboxItem(GHRAZI_RAPIER),
        new MboxItem(ARMADYL_GODSWORD_OR).broadcastWorldMessage(true),
        new MboxItem(DRAGON_CLAWS_OR).broadcastWorldMessage(true),
    };

    private static final MboxItem[] RARE = new MboxItem[]{
        new MboxItem(DRAGON_CLAWS).broadcastWorldMessage(true),
        new MboxItem(DINHS_BULWARK),
        new MboxItem(ARMADYL_GODSWORD),
        new MboxItem(ZURIELS_STAFF),
        new MboxItem(VESTAS_SPEAR),
        new MboxItem(DRAGON_HUNTER_CROSSBOW),
        new MboxItem(DRAGON_HUNTER_LANCE),
        new MboxItem(BLADE_OF_SAELDOR),
        new MboxItem(INQUISITORS_MACE),
        new MboxItem(VOLATILE_ORB).broadcastWorldMessage(true),
        new MboxItem(HARMONISED_ORB).broadcastWorldMessage(true),
        new MboxItem(ELDRITCH_ORB).broadcastWorldMessage(true),
    };

    private static final MboxItem[] UNCOMMON = new MboxItem[]{
        new MboxItem(MORRIGANS_JAVELIN, 100),
        new MboxItem(MORRIGANS_THROWING_AXE, 100),
        new MboxItem(ABYSSAL_DAGGER),
        new MboxItem(ABYSSAL_DAGGER_P_13271),
        new MboxItem(ZAMORAKIAN_SPEAR),
        new MboxItem(ZAMORAKIAN_HASTA),
        new MboxItem(BARRELCHEST_ANCHOR),
        new MboxItem(HEAVY_BALLISTA),
        new MboxItem(SARADOMINS_BLESSED_SWORD),
    };

    private static final MboxItem[] COMMON = new MboxItem[]{
        new MboxItem(LIGHT_BALLISTA),
        new MboxItem(DRAGON_CROSSBOW),
        new MboxItem(ABYSSAL_TENTACLE),
        new MboxItem(STAFF_OF_BALANCE),
        new MboxItem(BANDOS_GODSWORD),
        new MboxItem(SARADOMIN_GODSWORD),
        new MboxItem(ZAMORAK_GODSWORD),
        new MboxItem(GRANITE_MAUL_24225),
        new MboxItem(DARK_BOW),
        new MboxItem(DRAGON_JAVELIN, 100),
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
        return AttributeKey.WEAPON_MYSTERY_BOXES_OPENED;
    }

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
