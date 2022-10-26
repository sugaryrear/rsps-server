package com.ferox.game.content.seasonal_events.rewards;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.items.Item;
import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.ItemIdentifiers;

import static com.ferox.game.world.entity.AttributeKey.*;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 14, 2021
 */
public enum EventRewards {

    EVENT_REWARD_1(new Item(ItemIdentifiers.CRYSTAL_KEY), 210, EVENT_REWARD_1_CLAIMED),
    EVENT_REWARD_2(new Item(CustomItemIdentifiers.SLAYER_KEY), 205, EVENT_REWARD_2_CLAIMED),
    EVENT_REWARD_3(new Item(CustomItemIdentifiers.DOUBLE_DROPS_LAMP), 200, EVENT_REWARD_3_CLAIMED),
    EVENT_REWARD_4(new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), 195, EVENT_REWARD_4_CLAIMED),
    EVENT_REWARD_5(new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX), 190, EVENT_REWARD_5_CLAIMED),
    EVENT_REWARD_6(new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX), 185, EVENT_REWARD_6_CLAIMED),
    EVENT_REWARD_7(new Item(ItemIdentifiers.RANGER_BOOTS), 180, EVENT_REWARD_7_CLAIMED),
    EVENT_REWARD_8(new Item(ItemIdentifiers.DRAGON_BOOTS), 175, EVENT_REWARD_8_CLAIMED),
    EVENT_REWARD_9(new Item(ItemIdentifiers.AMULET_OF_FURY), 170, EVENT_REWARD_9_CLAIMED),
    EVENT_REWARD_10(new Item(ItemIdentifiers.INFINITY_BOOTS), 165, EVENT_REWARD_10_CLAIMED),
    EVENT_REWARD_11(new Item(ItemIdentifiers.MASTER_WAND), 160, EVENT_REWARD_11_CLAIMED),
    EVENT_REWARD_12(new Item(ItemIdentifiers.MAGES_BOOK), 155, EVENT_REWARD_12_CLAIMED),
    EVENT_REWARD_13(new Item(ItemIdentifiers.RING_OF_THE_GODS), 150, EVENT_REWARD_13_CLAIMED),
    EVENT_REWARD_14(new Item(ItemIdentifiers.TREASONOUS_RING), 145, EVENT_REWARD_14_CLAIMED),
    EVENT_REWARD_15(new Item(ItemIdentifiers.TYRANNICAL_RING), 140, EVENT_REWARD_15_CLAIMED),
    EVENT_REWARD_16(new Item(ItemIdentifiers.ABYSSAL_DAGGER_P_13271), 135, EVENT_REWARD_16_CLAIMED),
    EVENT_REWARD_17(new Item(ItemIdentifiers.SPIKED_MANACLES), 130, EVENT_REWARD_17_CLAIMED),
    EVENT_REWARD_18(new Item(ItemIdentifiers.BANDOS_GODSWORD), 125, EVENT_REWARD_18_CLAIMED),
    EVENT_REWARD_19(new Item(ItemIdentifiers.SARADOMIN_GODSWORD), 120, EVENT_REWARD_19_CLAIMED),
    EVENT_REWARD_20(new Item(ItemIdentifiers.ZAMORAK_GODSWORD), 115, EVENT_REWARD_20_CLAIMED),
    EVENT_REWARD_21(new Item(ItemIdentifiers.ABYSSAL_BLUDGEON), 110, EVENT_REWARD_21_CLAIMED),
    EVENT_REWARD_22(new Item(ItemIdentifiers.BRIMSTONE_RING), 105, EVENT_REWARD_22_CLAIMED),
    EVENT_REWARD_23(new Item(CustomItemIdentifiers.PET_MYSTERY_BOX), 100, EVENT_REWARD_23_CLAIMED),
    EVENT_REWARD_24(new Item(ItemIdentifiers.FREMENNIK_KILT), 95, EVENT_REWARD_24_CLAIMED),
    EVENT_REWARD_25(new Item(CustomItemIdentifiers.BLOOD_MONEY_CASKET), 90, EVENT_REWARD_25_CLAIMED),
    EVENT_REWARD_26(new Item(ItemIdentifiers.ARMADYL_GODSWORD), 85, EVENT_REWARD_26_CLAIMED),
    EVENT_REWARD_27(new Item(CustomItemIdentifiers.LARRANS_KEY_TIER_II), 80, EVENT_REWARD_27_CLAIMED),
    EVENT_REWARD_28(new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), 75, EVENT_REWARD_28_CLAIMED),
    EVENT_REWARD_29(new Item(CustomItemIdentifiers.ZENYTE_MYSTERY_BOX), 70, EVENT_REWARD_29_CLAIMED),
    EVENT_REWARD_30(new Item(CustomItemIdentifiers.RAIDS_MYSTERY_BOX), 65, EVENT_REWARD_30_CLAIMED),
    EVENT_REWARD_31(new Item(CustomItemIdentifiers.MYSTERY_TICKET), 60, EVENT_REWARD_31_CLAIMED),
    EVENT_REWARD_32(new Item(CustomItemIdentifiers.KEY_OF_DROPS), 55, EVENT_REWARD_32_CLAIMED),
    EVENT_REWARD_33(new Item(CustomItemIdentifiers.REVENANT_MYSTER_BOX), 50, EVENT_REWARD_33_CLAIMED),
    EVENT_REWARD_34(new Item(ItemIdentifiers.CHRISTMAS_CRACKER), 45, EVENT_REWARD_34_CLAIMED),
    EVENT_REWARD_35(new Item(CustomItemIdentifiers.MYSTERY_TICKET), 40, EVENT_REWARD_35_CLAIMED),
    EVENT_REWARD_36(new Item(ItemIdentifiers.PRIMORDIAL_BOOTS), 35, EVENT_REWARD_36_CLAIMED),
    EVENT_REWARD_37(new Item(ItemIdentifiers.PEGASIAN_BOOTS), 30, EVENT_REWARD_37_CLAIMED),
    EVENT_REWARD_38(new Item(CustomItemIdentifiers.MYSTERY_TICKET), 25, EVENT_REWARD_38_CLAIMED),
    EVENT_REWARD_39(new Item(CustomItemIdentifiers.KEY_OF_DROPS), 20, EVENT_REWARD_39_CLAIMED),
    EVENT_REWARD_40(new Item(ItemIdentifiers.ETERNAL_BOOTS), 15, EVENT_REWARD_40_CLAIMED),
    EVENT_REWARD_41(new Item(CustomItemIdentifiers.MYSTERY_TICKET), 10, EVENT_REWARD_41_CLAIMED),
    EVENT_REWARD_42(new Item(ItemIdentifiers.RING_OF_SUFFERING), 5, EVENT_REWARD_42_CLAIMED),
    EVENT_REWARD_43(new Item(CustomItemIdentifiers.MYSTERY_TICKET), 2, EVENT_REWARD_43_CLAIMED),
    EVENT_REWARD_44(new Item(CustomItemIdentifiers.HWEEN_ITEM_CHEST), 0, EVENT_REWARD_44_CLAIMED)//Contains a random cosmetic
    ;

    public final Item reward;
    public final int chance;
    public final AttributeKey key;

    EventRewards(Item reward, int chance, AttributeKey key) {
        this.reward = reward;
        this.chance = chance;
        this.key = key;
    }
}
