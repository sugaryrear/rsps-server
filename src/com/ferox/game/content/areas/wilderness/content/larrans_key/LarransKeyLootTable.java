package com.ferox.game.content.areas.wilderness.content.larrans_key;

import com.ferox.game.world.items.Item;
import com.ferox.util.Utils;

import java.util.Arrays;
import java.util.List;

import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | February, 17, 2021, 14:19
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class LarransKeyLootTable {

    private static final List<Item> COMMON_TABLE_TIER_I = Arrays.asList(
        new Item(BLOOD_MONEY, 1500),
        new Item(DRAGON_DART, 25),
        new Item(DRAGON_KNIFE, 15),
        new Item(DRAGON_JAVELIN, 25),
        new Item(DRAGON_THROWNAXE, 25),
        new Item(ANTIVENOM4+1, 5),
        new Item(GUTHIX_REST4+1, 5),
        new Item(OBSIDIAN_HELMET, 1),
        new Item(OBSIDIAN_PLATEBODY, 1),
        new Item(OBSIDIAN_PLATELEGS, 1),
        new Item(RANGERS_TUNIC, 1),
        new Item(REGEN_BRACELET, 1),
        new Item(GRANITE_MAUL_24225, 1),
        new Item(OPAL_DRAGON_BOLTS_E, 25),
        new Item(DIAMOND_DRAGON_BOLTS_E, 25),
        new Item(DRAGONSTONE_DRAGON_BOLTS_E, 25),
        new Item(ONYX_DRAGON_BOLTS_E, 25)
        );

    private static final List<Item> UNCOMMON_TABLE_TIER_I = Arrays.asList(


        new Item(BLOOD_MONEY, 15000),
        new Item(ZAMORAKIAN_HASTA),
        new Item(DRAGON_CROSSBOW),
        new Item(DRAGON_SCIMITAR_OR),
        new Item(ABYSSAL_DAGGER)
    );

    private static final List<Item> RARE_TABLE_TIER_I = Arrays.asList(
        new Item(STATIUSS_FULL_HELM),
        new Item(STATIUSS_WARHAMMER),
        new Item(DRAGON_WARHAMMER),
        new Item(BLOOD_MONEY, 15000),
        new Item(MORRIGANS_LEATHER_BODY),
        new Item(MORRIGANS_LEATHER_CHAPS),
        new Item(ZURIELS_ROBE_TOP),
        new Item(BLOOD_MONEY, 15000),
        new Item(ZAMORAKIAN_HASTA),
        new Item(DRAGON_CROSSBOW),
        new Item(DRAGON_SCIMITAR_OR),
        new Item(ABYSSAL_DAGGER),        new Item(BLOOD_MONEY, 15000),
        new Item(ZAMORAKIAN_HASTA),
        new Item(DRAGON_CROSSBOW),
        new Item(DRAGON_SCIMITAR_OR),
        new Item(ABYSSAL_DAGGER),
        new Item(ZURIELS_ROBE_BOTTOM),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(TORMENTED_BRACELET)
    );

    private static final List<Item> EXTREMELY_RARE_TABLE_TIER_I = Arrays.asList(
        new Item(STATIUSS_FULL_HELM),
        new Item(STATIUSS_WARHAMMER),
        new Item(DRAGON_WARHAMMER),        new Item(BLOOD_MONEY, 15000),
        new Item(ZAMORAKIAN_HASTA),
        new Item(DRAGON_CROSSBOW),
        new Item(DRAGON_SCIMITAR_OR),
        new Item(ABYSSAL_DAGGER),
        new Item(MORRIGANS_LEATHER_BODY),
        new Item(MORRIGANS_LEATHER_CHAPS),
        new Item(ZURIELS_ROBE_TOP),
        new Item(ZURIELS_ROBE_BOTTOM),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(TORMENTED_BRACELET)
        );

    private static final List<Item> COMMON_TABLE_TIER_II = Arrays.asList(
        new Item(STATIUSS_FULL_HELM),
        new Item(STATIUSS_WARHAMMER),
        new Item(DRAGON_WARHAMMER),
        new Item(MORRIGANS_LEATHER_BODY),
        new Item(MORRIGANS_LEATHER_CHAPS),
        new Item(ZURIELS_ROBE_TOP),        new Item(BLOOD_MONEY, 15000),
        new Item(ZAMORAKIAN_HASTA),
        new Item(DRAGON_CROSSBOW),
        new Item(DRAGON_SCIMITAR_OR),
        new Item(ABYSSAL_DAGGER),
        new Item(ZURIELS_ROBE_BOTTOM),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(TORMENTED_BRACELET)
        );

    private static final List<Item> UNCOMMON_TABLE_TIER_II = Arrays.asList(
        new Item(STATIUSS_FULL_HELM),
        new Item(STATIUSS_WARHAMMER),
        new Item(DRAGON_WARHAMMER),
        new Item(MORRIGANS_LEATHER_BODY),        new Item(BLOOD_MONEY, 15000),
        new Item(ZAMORAKIAN_HASTA),
        new Item(DRAGON_CROSSBOW),
        new Item(DRAGON_SCIMITAR_OR),
        new Item(ABYSSAL_DAGGER),
        new Item(MORRIGANS_LEATHER_CHAPS),
        new Item(ZURIELS_ROBE_TOP),
        new Item(ZURIELS_ROBE_BOTTOM),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(TORMENTED_BRACELET)
    );

    private static final List<Item> RARE_TABLE_TIER_II = Arrays.asList(
        new Item(STATIUSS_FULL_HELM),
        new Item(STATIUSS_WARHAMMER),
        new Item(DRAGON_WARHAMMER),
        new Item(MORRIGANS_LEATHER_BODY),
        new Item(MORRIGANS_LEATHER_CHAPS),
        new Item(ZURIELS_ROBE_TOP),        new Item(BLOOD_MONEY, 15000),
        new Item(ZAMORAKIAN_HASTA),
        new Item(DRAGON_CROSSBOW),
        new Item(DRAGON_SCIMITAR_OR),
        new Item(ABYSSAL_DAGGER),
        new Item(ZURIELS_ROBE_BOTTOM),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(TORMENTED_BRACELET)
    );

    private static final List<Item> EXTREMELY_RARE_TABLE_TIER_II = Arrays.asList(
        new Item(STATIUSS_FULL_HELM),
        new Item(STATIUSS_WARHAMMER),
        new Item(DRAGON_WARHAMMER),
        new Item(MORRIGANS_LEATHER_BODY),
        new Item(MORRIGANS_LEATHER_CHAPS),
        new Item(ZURIELS_ROBE_TOP),
        new Item(ZURIELS_ROBE_BOTTOM),        new Item(BLOOD_MONEY, 15000),
        new Item(ZAMORAKIAN_HASTA),
        new Item(DRAGON_CROSSBOW),
        new Item(DRAGON_SCIMITAR_OR),
        new Item(ABYSSAL_DAGGER),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(TORMENTED_BRACELET)
    );

    private static final List<Item> COMMON_TABLE_TIER_III = Arrays.asList(
        new Item(STATIUSS_FULL_HELM),
        new Item(STATIUSS_WARHAMMER),
        new Item(DRAGON_WARHAMMER),
        new Item(MORRIGANS_LEATHER_BODY),
        new Item(MORRIGANS_LEATHER_CHAPS),
        new Item(ZURIELS_ROBE_TOP),        new Item(BLOOD_MONEY, 15000),
        new Item(ZAMORAKIAN_HASTA),
        new Item(DRAGON_CROSSBOW),
        new Item(DRAGON_SCIMITAR_OR),
        new Item(ABYSSAL_DAGGER),
        new Item(ZURIELS_ROBE_BOTTOM),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(TORMENTED_BRACELET)
        );

    private static final List<Item> UNCOMMON_TABLE_TIER_III = Arrays.asList(
        new Item(STATIUSS_FULL_HELM),
        new Item(STATIUSS_WARHAMMER),
        new Item(DRAGON_WARHAMMER),
        new Item(MORRIGANS_LEATHER_BODY),
        new Item(MORRIGANS_LEATHER_CHAPS),        new Item(BLOOD_MONEY, 15000),
        new Item(ZAMORAKIAN_HASTA),
        new Item(DRAGON_CROSSBOW),
        new Item(DRAGON_SCIMITAR_OR),
        new Item(ABYSSAL_DAGGER),
        new Item(ZURIELS_ROBE_TOP),
        new Item(ZURIELS_ROBE_BOTTOM),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(TORMENTED_BRACELET)
    );

    private static final List<Item> RARE_TABLE_TIER_III = Arrays.asList(
        new Item(STATIUSS_FULL_HELM),
        new Item(STATIUSS_WARHAMMER),
        new Item(DRAGON_WARHAMMER),
        new Item(MORRIGANS_LEATHER_BODY),
        new Item(MORRIGANS_LEATHER_CHAPS),        new Item(BLOOD_MONEY, 15000),
        new Item(ZAMORAKIAN_HASTA),
        new Item(DRAGON_CROSSBOW),
        new Item(DRAGON_SCIMITAR_OR),
        new Item(ABYSSAL_DAGGER),
        new Item(ZURIELS_ROBE_TOP),
        new Item(ZURIELS_ROBE_BOTTOM),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(TORMENTED_BRACELET)
    );

    private static final List<Item> EXTREMELY_RARE_TABLE_TIER_III = Arrays.asList(
        new Item(STATIUSS_FULL_HELM),
        new Item(STATIUSS_WARHAMMER),
        new Item(DRAGON_WARHAMMER),
        new Item(MORRIGANS_LEATHER_BODY),
        new Item(MORRIGANS_LEATHER_CHAPS),        new Item(BLOOD_MONEY, 15000),
        new Item(ZAMORAKIAN_HASTA),
        new Item(DRAGON_CROSSBOW),
        new Item(DRAGON_SCIMITAR_OR),
        new Item(ABYSSAL_DAGGER),
        new Item(ZURIELS_ROBE_TOP),
        new Item(ZURIELS_ROBE_BOTTOM),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(TORMENTED_BRACELET)
        );

    public static Item rewardTables(int key) {
        List<Item> items = null;
        if(key == LARRANS_KEY_TIER_I) {
            if(Utils.rollDie(150, 1)) {
                items = EXTREMELY_RARE_TABLE_TIER_I;
            } else if (Utils.rollDie(30, 1)) {
                items = RARE_TABLE_TIER_I;
            } else if (Utils.rollDie(7, 1)) {
                items = UNCOMMON_TABLE_TIER_I;
            } else {
                items = COMMON_TABLE_TIER_I;
            }
        } else if(key == LARRANS_KEY_TIER_II) {
            if(Utils.rollDie(150, 1)) {
                items = EXTREMELY_RARE_TABLE_TIER_II;
            } else if (Utils.rollDie(30, 1)) {
                items = RARE_TABLE_TIER_II;
            } else if (Utils.rollDie(7, 1)) {
                items = UNCOMMON_TABLE_TIER_II;
            } else {
                items = COMMON_TABLE_TIER_II;
            }
        } else if(key == LARRANS_KEY_TIER_III) {
            if(Utils.rollDie(150, 1)) {
                items = EXTREMELY_RARE_TABLE_TIER_III;
            } else if (Utils.rollDie(30, 1)) {
                items = RARE_TABLE_TIER_III;
            } else if (Utils.rollDie(7, 1)) {
                items = UNCOMMON_TABLE_TIER_III;
            } else {
                items = COMMON_TABLE_TIER_III;
            }
        }
        return Utils.randomElement(items);
    }
}
