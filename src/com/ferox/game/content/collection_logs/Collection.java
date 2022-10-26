package com.ferox.game.content.collection_logs;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.items.Item;
import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.NpcIdentifiers;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.ferox.game.content.collection_logs.CollectionLog.RAIDS_KEY;
import static com.ferox.util.CustomItemIdentifiers.BARRELCHEST_PET;
import static com.ferox.util.CustomItemIdentifiers.NIFFLER;
import static com.ferox.util.CustomItemIdentifiers.WAMPA;
import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.CustomNpcIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;
import static com.ferox.util.NpcIdentifiers.THE_NIGHTMARE_9430;

/**
 * @author PVE
 * @Since juli 15, 2020
 */
public enum Collection {
    // bosses
        //Drops

    BARROWS(AttributeKey.BARROWS_CHESTS_OPENED, LogType.BOSSES, "Barrows", new int[] {CollectionLog.BARROWS_KEY}, AttributeKey.BARROWS_KEY_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX,3) },
        //Drops
        new Item(4708), new Item(4710), new Item(4712), new Item(4714),
        new Item(4716), new Item(4718), new Item(4720), new Item(4722),
            new Item(4724), new Item(4726), new Item(4728), new Item(4730),
        new Item(4732), new Item(4734), new Item(4736), new Item(4738), new Item(4740,1),
        new Item(4724), new Item(4726), new Item(4728), new Item(4730),
        new Item(4745), new Item(4747), new Item(4749), new Item(4751),
        new Item(4753), new Item(4755), new Item(4757), new Item(4759)),

    CALLISTO(AttributeKey.CALLISTOS_KILLED, LogType.BOSSES, "Callisto", new int[] {NpcIdentifiers.CALLISTO, NpcIdentifiers.CALLISTO_6609}, AttributeKey.CALLISTO_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX,3) },
        //Drops
        new Item(CALLISTO_CUB), new Item(DRAGON_PICKAXE), new Item(TYRANNICAL_RING),new Item(DRAGON_2H_SWORD)),

            COMMANDER_ZILYANA(AttributeKey.COMMANDER_ZILYANA_KILLED, LogType.BOSSES, "Commander Zilyana", new int[] {2205, 6493 }, AttributeKey.ZILYANA_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX,3) },

                //Drops
        new Item(PET_ZILYANA), new Item(11818), new Item(11820),new Item(11822),
            new Item(13256), new Item(11838), new Item(11785),new Item(11814)),
    GENERAL_GRAARDOR(AttributeKey.GENERAL_GRAARDOR_KILLED, LogType.BOSSES, "General Graardor", new int[] {2215, 6494 }, AttributeKey.GRAARDOR_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX,3) },

        //Drops
        new Item(12650), new Item(11832), new Item(11834),new Item(11836),
        new Item(11812), new Item(11818), new Item(11820),new Item(11822)),
    KREEARA(AttributeKey.KREE_ARRA_KILLED, LogType.BOSSES, "Kree'arra", new int[] {3162, 6492 }, AttributeKey.KREEARRA_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX,3) },

        //Drops
        new Item(12649), new Item(11826), new Item(11828),new Item(11830),
        new Item(ARMADYL_HILT),
        new Item(11812), new Item(11818), new Item(11820),new Item(11822)),
    KRILTSUTSAROTH(AttributeKey.KRIL_TSUTSAROTHS_KILLED, LogType.BOSSES, "K'ril Tsutsaroth", new int[] {3129, 6495 }, AttributeKey.KRILTSUTSAROTH_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX,3) },

        //Drops
        new Item(12652), new Item(11791), new Item(11824),new Item(12795),
        new Item(ZAMORAK_HILT),
 new Item(11818), new Item(11820),new Item(11822)),
    NEX(AttributeKey.NEX_KILLED, LogType.BOSSES, "Nex", new int[] {11278}, AttributeKey.NEX_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX,3) },

        //Drops
        new Item(26348), new Item(26370), new Item(26372),new Item(26235),
        new Item(26376),
        new Item(26378), new Item(26380),new Item(26231, 1)),

    CERBERUS(AttributeKey.CERBERUS_KILLED, LogType.BOSSES,"Cerberus", new int[]{NpcIdentifiers.CERBERUS}, AttributeKey.CERBERUS_LOG_CLAIMED, new Item[] { new Item(PRIMORDIAL_BOOTS), new Item(PEGASIAN_BOOTS), new Item(ETERNAL_BOOTS) },
        //Drops
        new Item(HELLPUPPY), new Item(PRIMORDIAL_CRYSTAL), new Item(PEGASIAN_CRYSTAL), new Item(ETERNAL_CRYSTAL), new Item(SMOULDERING_STONE), new Item(JAR_OF_SOULS)),

    ABYSSAL_SIRE(AttributeKey.KC_ABYSSALSIRE, LogType.BOSSES,"Abyssal sire", new int[]{5890}, AttributeKey.ABYSSAL_SIRE_LOG_CLAIMED, new Item[] { new Item(PRIMORDIAL_BOOTS), new Item(PEGASIAN_BOOTS), new Item(ETERNAL_BOOTS) },
        //Drops
        new Item(ABYSSAL_ORPHAN), new Item(UNSIRED), new Item(ABYSSAL_HEAD), new Item(BLUDGEON_SPINE), new Item(BLUDGEON_CLAW), new Item(BLUDGEON_AXON),
        new Item(JAR_OF_MIASMA),     new Item(ABYSSAL_DAGGER),     new Item(ABYSSAL_WHIP)),

    CHAOS_ELEMENTAL(AttributeKey.CHAOS_ELEMENTALS_KILLED, LogType.BOSSES, "Chaos Elemental", new int[]{NpcIdentifiers.CHAOS_ELEMENTAL}, AttributeKey.CHAOS_ELEMENTAL_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX) },
        //Drops
        new Item(PET_CHAOS_ELEMENTAL), new Item(DRAGON_2H_SWORD), new Item(DRAGON_PICKAXE)),



    CHAOS_FANATIC(AttributeKey.CHAOS_FANATICS_KILLED, LogType.BOSSES, "Chaos Fanatic", new int[] {NpcIdentifiers.CHAOS_FANATIC}, AttributeKey.CHAOS_FANATIC_LOG_CLAIMED, new Item[] { new Item(BLOOD_MONEY_CASKET,2) },
        //Drops
        new Item(PET_CHAOS_ELEMENTAL), new Item(ODIUM_SHARD_1), new Item(MALEDICTION_SHARD_1)),

    CORPOREAL_BEAST(AttributeKey.CORPOREAL_BEASTS_KILLED, LogType.BOSSES, "Corporeal Beast", new int[]{NpcIdentifiers.CORPOREAL_BEAST}, AttributeKey.CORPOREAL_BEAST_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX,3), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX,2) },
        //Drops
        new Item(PET_DARK_CORE), new Item(ELYSIAN_SIGIL), new Item(SPECTRAL_SIGIL), new Item(ARCANE_SIGIL), new Item(SPIRIT_SHIELD), new Item(JAR_OF_SPIRITS), new Item(HOLY_ELIXIR)),

    CRAZY_ARCHAEOLOGIST(AttributeKey.CRAZY_ARCHAEOLOGISTS_KILLED, LogType.BOSSES, "Crazy Archaeologist", new int[]{NpcIdentifiers.CRAZY_ARCHAEOLOGIST}, AttributeKey.CRAZY_ARCHAEOLOGIST_LOG_CLAIMED, new Item[] { new Item(ODIUM_WARD), new Item(MALEDICTION_WARD) },
        //Drops
        new Item(ODIUM_SHARD_2), new Item(MALEDICTION_SHARD_2), new Item(FEDORA)),


    KING_BLACK_DRAGON(AttributeKey.KING_BLACK_DRAGONS_KILLED, LogType.BOSSES, "King Black Dragon", new int[]{NpcIdentifiers.KING_BLACK_DRAGON}, AttributeKey.KING_BLACK_DRAGON_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX,3), new Item(DRAGONFIRE_SHIELD,2) },
        //Drops
        new Item(PRINCE_BLACK_DRAGON), new Item(KBD_HEADS), new Item(DRAGON_PICKAXE), new Item(DRACONIC_VISAGE)),

    KRAKEN(AttributeKey.KRAKENS_KILLED, LogType.BOSSES, "Kraken", new int[]{NpcIdentifiers.KRAKEN}, AttributeKey.KRAKEN_LOG_CLAIMED, new Item[] { new Item(ANCIENT_WYVERN_SHIELD), new Item(DRAGONFIRE_WARD) },
        //Drops
        new Item(PET_KRAKEN), new Item(KRAKEN_TENTACLE), new Item(TRIDENT_OF_THE_SEAS), new Item(JAR_OF_DIRT)),


    LIZARDMAN_SHAMAN(AttributeKey.LIZARDMAN_SHAMANS_KILLED, LogType.BOSSES, "Lizardman Shaman", new int[] {NpcIdentifiers.LIZARDMAN_SHAMAN, NpcIdentifiers.LIZARDMAN_SHAMAN_6767}, AttributeKey.LIZARDMAN_SHAMAN_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX,5) },
        //Drops
        new Item(DRAGON_WARHAMMER)),

    SCORPIA(AttributeKey.SCORPIAS_KILLED, LogType.BOSSES, "Scorpia", new int[] {NpcIdentifiers.SCORPIA}, AttributeKey.SCORPIA_LOG_CLAIMED, new Item[] { new Item(OCCULT_NECKLACE_OR) },
        //Drops
        new Item(SCORPIAS_OFFSPRING), new Item(ODIUM_SHARD_3), new Item(MALEDICTION_SHARD_3)),

    THERMONUCLEAR_SMOKE_DEVIL(AttributeKey.THERMONUCLEAR_SMOKE_DEVILS_KILLED, LogType.BOSSES, "Thermonuclear Smoke Devil", new int[] {NpcIdentifiers.THERMONUCLEAR_SMOKE_DEVIL}, AttributeKey.THERMONUCLEAR_SMOKE_DEVIL_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX,3) },
        //Drops
        new Item(PET_SMOKE_DEVIL), new Item(OCCULT_NECKLACE), new Item(DRAGON_CHAINBODY), new Item(JAR_OF_SMOKE), new Item(SMOKE_BATTLESTAFF)),

    TZTOK_JAD(AttributeKey.JADS_KILLED, LogType.BOSSES, "The Fight Caves", new int[] {NpcIdentifiers.TZTOKJAD}, AttributeKey.TZTOK_JAD_LOG_CLAIMED, new Item[] { new Item(DRAGON_CLAWS) },
        //Drops
        new Item(TZREKJAD), new Item(FIRE_CAPE)),
   KALPHITE_QUEEN(AttributeKey.KQ_KILLED, LogType.BOSSES, "Kalphite Queen", new int[] {963, 965, 4303, 4304, 6500, 6501}, AttributeKey.KQ_LOG_CLAIMED, new Item[] { new Item(DRAGON_CLAWS) },
        //Drops
        new Item(KALPHITE_PRINCESS), new Item(JAR_OF_SAND), new Item(DRAGON_CHAINBODY), new Item(DRAGON_2H_SWORD),new Item(7981)),
    VENENATIS(AttributeKey.VENENATIS_KILLED, LogType.BOSSES, "Venenatis", new int[] {NpcIdentifiers.VENENATIS, NpcIdentifiers.VENENATIS_6610}, AttributeKey.VENENATIS_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX,5) },
        //Drops
        new Item(VENENATIS_SPIDERLING), new Item(TREASONOUS_RING), new Item(DRAGON_PICKAXE)),

    VETION(AttributeKey.VETIONS_KILLED, LogType.BOSSES, "Vet'ion", new int[]{NpcIdentifiers.VETION_REBORN}, AttributeKey.VETION_LOG_CLAIMED, new Item[] { new Item(KEY_OF_DROPS) },
        //Drops
        new Item(VETION_JR), new Item(DRAGON_PICKAXE), new Item(RING_OF_THE_GODS)),

    VORKATH(AttributeKey.VORKATHS_KILLED, LogType.BOSSES, "Vorkath", new int[]{NpcIdentifiers.VORKATH}, AttributeKey.VORKATH_LOG_CLAIMED, new Item[] { new Item(VORKATHS_HEAD_21907), new Item(DRAGON_HUNTER_CROSSBOW_T) },
        //Drops
        new Item(VORKI), new Item(VORKATHS_HEAD),new Item(DRACONIC_VISAGE), new Item(SKELETAL_VISAGE), new Item(22111), new Item(JAR_OF_DECAY), new Item(DRAGONBONE_NECKLACE)),


    SKOTIZO(AttributeKey.SKOTIZOS_KILLED, LogType.BOSSES, "Skotizo", new int[] {NpcIdentifiers.SKOTIZO}, AttributeKey.SKOTIZO_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX) },
        //Dropscustom
        new Item(SKOTOS), new Item(DARK_CLAW), new Item(JAR_OF_DARKNESS), new Item(DARK_TOTEM), new Item(UNCUT_ONYX), new Item(ANCIENT_SHARD)),

    ZULRAH(AttributeKey.ZULRAHS_KILLED, LogType.BOSSES, "Zulrah", new int[]{NpcIdentifiers.ZULRAH, NpcIdentifiers.ZULRAH_2044, NpcIdentifiers.ZULRAH_2043}, AttributeKey.ZULRAH_LOG_CLAIMED, new Item[] { new Item(TOXIC_BLOWPIPE), new Item(TRIDENT_OF_THE_SWAMP), new Item(TOXIC_STAFF_OF_THE_DEAD) },
        //Drops
        new Item(PET_SNAKELING), new Item(TANZANITE_HELM), new Item(MAGMA_HELM), new Item(JAR_OF_SWAMP), new Item(TRIDENT_OF_THE_SWAMP), new Item(SERPENTINE_HELM), new Item(TOXIC_STAFF_OF_THE_DEAD), new Item(TOXIC_BLOWPIPE), new Item(ZULANDRA_TELEPORT), new Item(UNCUT_ONYX), new Item(ZULRAHS_SCALES)),

    ALCHEMICAL_HYDRA(AttributeKey.ALCHY_KILLED, LogType.BOSSES, "Alchemical Hydra", new int[]{NpcIdentifiers.ALCHEMICAL_HYDRA, NpcIdentifiers.ALCHEMICAL_HYDRA_8616, NpcIdentifiers.ALCHEMICAL_HYDRA_8617, NpcIdentifiers.ALCHEMICAL_HYDRA_8618, NpcIdentifiers.ALCHEMICAL_HYDRA_8619, NpcIdentifiers.ALCHEMICAL_HYDRA_8620, NpcIdentifiers.ALCHEMICAL_HYDRA_8621, NpcIdentifiers.ALCHEMICAL_HYDRA_8622}, AttributeKey.ALCHEMICAL_HYDRA_LOG_CLAIMED, new Item[] { new Item(KEY_OF_DROPS) },
        //Drops
        new Item(IKKLE_HYDRA), new Item(HYDRAS_CLAW), new Item(HYDRAS_FANG),new Item(HYDRAS_EYE), new Item(ALCHEMICAL_HYDRA_HEADS), new Item(HYDRA_TAIL), new Item(HYDRA_LEATHER), new Item(HYDRAS_HEART), new Item(DRAGON_KNIFE), new Item(DRAGON_THROWNAXE), new Item(JAR_OF_CHEMICALS), new Item(ALCHEMICAL_HYDRA_HEADS)),


    GIANT_MOLE(AttributeKey.KC_GIANTMOLE, LogType.BOSSES, "Giant Mole", new int[]{NpcIdentifiers.GIANT_MOLE}, AttributeKey.GIANT_MOLE_LOG_CLAIMED, new Item[] { new Item(DRAGON_CLAWS) },
        //Drops
        new Item(NIFFLER), new Item(MOLE_SKIN), new Item(MOLE_CLAW)),


    THE_NIGHTMARE(AttributeKey.THE_NIGHTMARE_KC, LogType.BOSSES, "The nightmare", new int[]{THE_NIGHTMARE_9430}, AttributeKey.THE_NIGTHMARE_LOG_CLAIMED, new Item[] { new Item(SHADOW_INQUISITOR_ORNAMENT_KIT), new Item(INQUISITORS_MACE_ORNAMENT_KIT) },
        //Drops
        new Item(LITTLE_NIGHTMARE), new Item(ItemIdentifiers.CRYSTAL_KEY), new Item(BLOOD_MONEY_CASKET), new Item(ABYSSAL_BLUDGEON), new Item(ARMADYL_GODSWORD), new Item(DRAGON_CLAWS), new Item(DRAGON_WARHAMMER), new Item(INQUISITORS_MACE), new Item(INQUISITORS_GREAT_HELM), new Item(INQUISITORS_HAUBERK), new Item(INQUISITORS_PLATESKIRT), new Item(NIGHTMARE_STAFF), new Item(ELDRITCH_ORB), new Item(HARMONISED_ORB), new Item(VOLATILE_ORB), new Item(SHADOW_INQUISITOR_ORNAMENT_KIT), new Item(INQUISITORS_MACE_ORNAMENT_KIT)),

    THE_GAUNTLET(AttributeKey.CORRUPTED_HUNLEFFS_KILLED, LogType.BOSSES, "The Gauntlet", new int[] {NpcIdentifiers.CORRUPTED_HUNLLEF, NpcIdentifiers.CORRUPTED_HUNLLEF_9036, NpcIdentifiers.CORRUPTED_HUNLLEF_9037}, AttributeKey.CORRUPTED_HUNLEFF_LOG_CLAIMED, new Item[] { new Item(KEY_OF_DROPS) },
        //Drops
        new Item(YOUNGLLEF), new Item(23956), new Item(4207), new Item(25859), new Item(23859)
    ),


    // mboxes
    DONATOR_MYSTERY_BOX(AttributeKey.DONATOR_MYSTERY_BOXES_OPENED, LogType.MYSTERY_BOX, "Donator mystery box", new int[] {CustomItemIdentifiers.DONATOR_MYSTERY_BOX}, AttributeKey.DONATOR_MYSTERY_BOX_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX,5) },
        new Item(ELYSIAN_SPIRIT_SHIELD),
        new Item(NIFFLER),
        new Item(CustomItemIdentifiers.FAWKES),
        new Item(CustomItemIdentifiers.GRIM_REAPER_PET),
        new Item(BARRELCHEST_PET),
        new Item(_3RD_AGE_LONGSWORD),
        new Item(_3RD_AGE_BOW),
        new Item(_3RD_AGE_WAND),
        new Item(ANCESTRAL_HAT),
        new Item(ANCESTRAL_ROBE_TOP),
        new Item(ANCESTRAL_ROBE_BOTTOM),
        new Item(VESTAS_CHAINBODY),
        new Item(VESTAS_PLATESKIRT),
        new Item(VESTAS_LONGSWORD),
        new Item(MORRIGANS_COIF),
        new Item(MORRIGANS_LEATHER_BODY),
        new Item(MORRIGANS_LEATHER_CHAPS),
        new Item(ZURIELS_HOOD),
        new Item(ZURIELS_ROBE_TOP),
        new Item(ZURIELS_ROBE_BOTTOM),
        new Item(VESTAS_SPEAR),
        new Item(STATIUSS_FULL_HELM),
        new Item(STATIUSS_PLATEBODY),
        new Item(STATIUSS_PLATELEGS),
        new Item(STATIUSS_WARHAMMER),
        new Item(SANGUINESTI_STAFF),
        new Item(ELDER_MAUL),
        new Item(GHRAZI_RAPIER),
        new Item(DRAGON_CLAWS),
        new Item(ARCANE_SPIRIT_SHIELD),
        new Item(TOXIC_BLOWPIPE),
        new Item(DRAGON_WARHAMMER),
        new Item(NEITIZNOT_FACEGUARD),
        new Item(DINHS_BULWARK),
        new Item(BLOOD_MONEY,500),
        new Item(HEAVY_BALLISTA),
        new Item(ARMADYL_CROSSBOW),
        new Item(MORRIGANS_JAVELIN, 75),
        new Item(MORRIGANS_THROWING_AXE,75),
        new Item(MAGMA_HELM),
        new Item(TANZANITE_HELM),
        new Item(ARMADYL_GODSWORD),
        new Item(BRIMSTONE_RING),
        new Item(AMULET_OF_TORTURE),
        new Item(BANDOS_GODSWORD),
        new Item(SARADOMIN_GODSWORD),
        new Item(ZAMORAK_GODSWORD),
        new Item(ARMADYL_CHAINSKIRT),
        new Item(BANDOS_CHESTPLATE),
        new Item(BANDOS_TASSETS),
        new Item(DRAGONFIRE_WARD),
        new Item(SERPENTINE_HELM),
        new Item(ANCIENT_WYVERN_SHIELD),
        new Item(DRAGONFIRE_SHIELD),
        new Item(TOXIC_STAFF_OF_THE_DEAD),
        new Item(SPECTRAL_SPIRIT_SHIELD),
        new Item(ABYSSAL_DAGGER_P_13271),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(DRAGON_CROSSBOW),
        new Item(PRIMORDIAL_BOOTS),
        new Item(PEGASIAN_BOOTS),
        new Item(ETERNAL_BOOTS),
        new Item(LIGHT_BALLISTA),
        new Item(AMULET_OF_FURY + 1, 10),
        new Item(DRAGON_BOOTS + 1, 15),
        new Item(ARMADYL_CROSSBOW),
        new Item(DRAGON_HUNTER_CROSSBOW)
    ),

    ARMOUR_MYSTERY_BOX(AttributeKey.ARMOUR_MYSTERY_BOXES_OPENED, LogType.MYSTERY_BOX, "Armour mystery box", new int[] {CustomItemIdentifiers.ARMOUR_MYSTERY_BOX}, AttributeKey.ARMOUR_MYSTERY_BOX_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX,5) },
        new Item(VESTAS_CHAINBODY),
        new Item(VESTAS_PLATESKIRT),
        new Item(STATIUSS_FULL_HELM),
        new Item(STATIUSS_PLATEBODY),
        new Item(STATIUSS_PLATELEGS),
        new Item(ANCESTRAL_HAT),
        new Item(ANCESTRAL_ROBE_TOP),
        new Item(ANCESTRAL_ROBE_BOTTOM),
        new Item(JUSTICIAR_FACEGUARD),
        new Item(JUSTICIAR_CHESTGUARD),
        new Item(JUSTICIAR_LEGGUARDS),
        new Item(MORRIGANS_COIF),
        new Item(MORRIGANS_LEATHER_BODY),
        new Item(MORRIGANS_LEATHER_CHAPS),
        new Item(ZURIELS_HOOD),
        new Item(ZURIELS_ROBE_TOP),
        new Item(ZURIELS_ROBE_BOTTOM),
        new Item(ARCANE_SPIRIT_SHIELD),
        new Item(SPECTRAL_SPIRIT_SHIELD),
        new Item(NEITIZNOT_FACEGUARD),
        new Item(GUARDIAN_BOOTS),
        new Item(TWISTED_BUCKLER),
        new Item(MAGMA_HELM),
        new Item(SERPENTINE_HELM),
        new Item(TANZANITE_HELM),
        new Item(PRIMORDIAL_BOOTS),
        new Item(PEGASIAN_BOOTS),
        new Item(ETERNAL_BOOTS),
        new Item(BANDOS_CHESTPLATE),
        new Item(BANDOS_TASSETS),
        new Item(ARMADYL_HELMET),
        new Item(ARMADYL_CHESTPLATE),
        new Item(ARMADYL_CHAINSKIRT),
        new Item(ANCIENT_WYVERN_SHIELD),
        new Item(DRAGONFIRE_SHIELD),
        new Item(DRAGONFIRE_WARD),
        new Item(DAGONHAI_HAT),
        new Item(DAGONHAI_ROBE_TOP),
        new Item(DAGONHAI_ROBE_BOTTOM),
        new Item(DRAGON_DEFENDER),
        new Item(FIGHTER_TORSO),
        new Item(FIRE_CAPE),
        new Item(SPIRIT_SHIELD),
        new Item(BLESSED_SPIRIT_SHIELD),
        new Item(ROBIN_HOOD_HAT),
        new Item(DRAGON_BOOTS),
        new Item(RANGER_BOOTS),
        new Item(INFINITY_BOOTS),
        new Item(OBSIDIAN_HELMET),
        new Item(OBSIDIAN_PLATEBODY),
        new Item(OBSIDIAN_PLATELEGS),
        new Item(MAGES_BOOK),
        new Item(ODIUM_WARD),
        new Item(MALEDICTION_WARD),
        new Item(DHAROKS_ARMOUR_SET),
        new Item(KARILS_ARMOUR_SET),
        new Item(AHRIMS_ARMOUR_SET),
        new Item(VERACS_ARMOUR_SET),
        new Item(TORAGS_ARMOUR_SET),
        new Item(GUTHANS_ARMOUR_SET),
        new Item(DRAGON_FULL_HELM),
        new Item(DRAGON_PLATEBODY)
    ),

    WEAPON_MYSTERY_BOX(AttributeKey.WEAPON_MYSTERY_BOXES_OPENED, LogType.MYSTERY_BOX, "Weapon mystery box", new int[] {CustomItemIdentifiers.WEAPON_MYSTERY_BOX}, AttributeKey.WEAPON_MYSTERY_BOX_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX,5) },
        new Item(NIGHTMARE_STAFF),
        new Item(SANGUINESTI_STAFF),
        new Item(VESTAS_LONGSWORD),
        new Item(STATIUSS_WARHAMMER),
        new Item(DRAGON_CLAWS_OR),
        new Item(ARMADYL_GODSWORD_OR),
        new Item(KODAI_WAND),
        new Item(DINHS_BULWARK),
        new Item(ARMADYL_GODSWORD),
        new Item(DRAGON_CLAWS),
        new Item(BANDOS_GODSWORD_OR),
        new Item(SARADOMIN_GODSWORD_OR),
        new Item(ZAMORAK_GODSWORD_OR),
        new Item(ZURIELS_STAFF),
        new Item(VESTAS_SPEAR),
        new Item(TOXIC_BLOWPIPE),
        new Item(TOXIC_STAFF_OF_THE_DEAD),
        new Item(DRAGON_HUNTER_CROSSBOW),
        new Item(DRAGON_HUNTER_LANCE),
        new Item(BLADE_OF_SAELDOR),
        new Item(GHRAZI_RAPIER),
        new Item(INQUISITORS_MACE),
        new Item(VOLATILE_ORB),
        new Item(HARMONISED_ORB),
        new Item(ELDRITCH_ORB),
        new Item(ELDER_MAUL),
        new Item(MORRIGANS_JAVELIN, 100),
        new Item(MORRIGANS_THROWING_AXE, 100),
        new Item(ABYSSAL_DAGGER),
        new Item(ABYSSAL_DAGGER_P_13271),
        new Item(ZAMORAKIAN_SPEAR),
        new Item(ZAMORAKIAN_HASTA),
        new Item(BARRELCHEST_ANCHOR),
        new Item(HEAVY_BALLISTA),
        new Item(ARMADYL_CROSSBOW),
        new Item(SARADOMINS_BLESSED_SWORD),
        new Item(LIGHT_BALLISTA),
        new Item(DRAGON_CROSSBOW),
        new Item(ABYSSAL_TENTACLE),
        new Item(STAFF_OF_LIGHT),
        new Item(STAFF_OF_THE_DEAD),
        new Item(STAFF_OF_BALANCE),
        new Item(BANDOS_GODSWORD),
        new Item(SARADOMIN_GODSWORD),
        new Item(ZAMORAK_GODSWORD),
        new Item(GRANITE_MAUL_24225),
        new Item(DARK_BOW),
        new Item(DRAGON_JAVELIN, 100)
    ),

    LEGENDARY_MYSTERY_BOX(AttributeKey.LEGENDARY_MYSTERY_BOXES_OPENED, LogType.MYSTERY_BOX, "Legendary mystery box", new int[] {CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX}, AttributeKey.LEGENDARY_MYSTERY_BOX_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX,5) },
        new Item(TWISTED_BOW),
        new Item(SCYTHE_OF_VITUR),
        new Item(ELYSIAN_SPIRIT_SHIELD),
        new Item(NIFFLER),
        new Item(CustomItemIdentifiers.FAWKES),
        new Item(CustomItemIdentifiers.GRIM_REAPER_PET),
        new Item(BARRELCHEST_PET),
        new Item(WAMPA),
        new Item(VOLATILE_NIGHTMARE_STAFF),
        new Item(HARMONISED_NIGHTMARE_STAFF),
        new Item(ELDRITCH_NIGHTMARE_STAFF),
        new Item(DRAGON_CLAWS_OR),
        new Item(ARMADYL_GODSWORD_OR),
        new Item(JUSTICIAR_FACEGUARD),
        new Item(JUSTICIAR_CHESTGUARD),
        new Item(JUSTICIAR_LEGGUARDS),
        new Item(ANCESTRAL_HAT),
        new Item(ANCESTRAL_ROBE_TOP),
        new Item(ANCESTRAL_ROBE_BOTTOM),
        new Item(INFERNAL_CAPE),
        new Item(RING_OF_MANHUNTING),
        new Item(NIGHTMARE_STAFF),
        new Item(SANGUINESTI_STAFF),
        new Item(ELDER_MAUL),
        new Item(KODAI_WAND),
        new Item(GHRAZI_RAPIER),
        new Item(BLADE_OF_SAELDOR),
        new Item(INQUISITORS_MACE),
        new Item(BANDOS_GODSWORD_OR),
        new Item(SARADOMIN_GODSWORD_OR),
        new Item(ZAMORAK_GODSWORD_OR),
        new Item(INQUISITORS_GREAT_HELM),
        new Item(INQUISITORS_HAUBERK),
        new Item(INQUISITORS_PLATESKIRT),
        new Item(AMULET_OF_TORTURE_OR),
        new Item(NECKLACE_OF_ANGUISH_OR),
        new Item(TORMENTED_BRACELET_OR),
        new Item(TOXIC_BLOWPIPE),
        new Item(ARMADYL_GODSWORD),
        new Item(DRAGON_CLAWS),
        new Item(HARMONISED_ORB),
        new Item(ELDRITCH_ORB),
        new Item(VOLATILE_ORB),
        new Item(HEAVY_BALLISTA),
        new Item(DINHS_BULWARK),
        new Item(SARADOMINS_BLESSED_SWORD),
        new Item(DRAGON_HUNTER_CROSSBOW),
        new Item(DRAGON_HUNTER_LANCE),
        new Item(AMULET_OF_FURY_OR),
        new Item(OCCULT_NECKLACE_OR),
        new Item(SPECTRAL_SPIRIT_SHIELD),
        new Item(ARCANE_SPIRIT_SHIELD),
        new Item(DRAGON_WARHAMMER),
        new Item(TOXIC_STAFF_OF_THE_DEAD)
    ),

    EPIC_PET_MYSTERY_BOX(AttributeKey.EPIC_PET_MYSTERY_BOXES_OPENED, LogType.MYSTERY_BOX, "Epic pet mystery box", new int[] {EPIC_PET_BOX}, AttributeKey.EPIC_PET_MYSTERY_BOX_LOG_CLAIMED, new Item[] { new Item(EPIC_PET_BOX,3) },
        new Item(ItemIdentifiers.JALNIBREK),
        new Item(ItemIdentifiers.LITTLE_NIGHTMARE),
        new Item(CustomItemIdentifiers.RING_OF_ELYSIAN),
        new Item(CustomItemIdentifiers.BLOOD_MONEY_PET),
        new Item(CustomItemIdentifiers.KERBEROS_PET),
        new Item(CustomItemIdentifiers.SKORPIOS_PET),
        new Item(CustomItemIdentifiers.ARACHNE_PET),
        new Item(CustomItemIdentifiers.ARTIO_PET),
        new Item(CustomItemIdentifiers.JAWA_PET),
        new Item(CustomItemIdentifiers.DEMENTOR_PET),
        new Item(CustomItemIdentifiers.FENRIR_GREYBACK_JR),
        new Item(CustomItemIdentifiers.FLUFFY_JR),
        new Item(CustomItemIdentifiers.ANCIENT_KING_BLACK_DRAGON_PET),
        new Item(CustomItemIdentifiers.ANCIENT_CHAOS_ELEMENTAL_PET),
        new Item(CustomItemIdentifiers.ANCIENT_BARRELCHEST_PET),
        new Item(CustomItemIdentifiers.ZRIAWK),
        new Item(CustomItemIdentifiers.FAWKES),
        new Item(CustomItemIdentifiers.NIFFLER),
        new Item(CustomItemIdentifiers.BARRELCHEST_PET),
        new Item(CustomItemIdentifiers.WAMPA),
        new Item(CustomItemIdentifiers.BABY_ARAGOG),
        new Item(CustomItemIdentifiers.FOUNDER_IMP),
        new Item(CustomItemIdentifiers.CENTAUR_FEMALE),
        new Item(CustomItemIdentifiers.CENTAUR_MALE),
        new Item(CustomItemIdentifiers.BABY_LAVA_DRAGON),
        new Item(CustomItemIdentifiers.JALTOK_JAD),
        new Item(CustomItemIdentifiers.MINI_NECROMANCER),
        new Item(CustomItemIdentifiers.PET_CORRUPTED_NECHRYARCH),
        new Item(ItemIdentifiers.TZREKZUK),
        new Item(CustomItemIdentifiers.GRIM_REAPER_PET),
        new Item(CustomItemIdentifiers.GENIE_PET),
        new Item(CustomItemIdentifiers.DHAROK_PET),
        new Item(CustomItemIdentifiers.PET_ZOMBIES_CHAMPION),
        new Item(CustomItemIdentifiers.BABY_ABYSSAL_DEMON),
        new Item(CustomItemIdentifiers.BABY_DARK_BEAST_EGG),
        new Item(CustomItemIdentifiers.BABY_SQUIRT)
    ),

    RAIDS_MYSTERY_BOX(AttributeKey.RAIDS_MYSTERY_BOXES_OPENED, LogType.MYSTERY_BOX, "Raids mystery box", new int[] {CustomItemIdentifiers.RAIDS_MYSTERY_BOX}, AttributeKey.RAIDS_MYSTERY_BOXES_OPENED, new Item[] { new Item(FIFTY_DOLLAR_BOND), new Item(CustomItemIdentifiers.RAIDS_MYSTERY_BOX,3) },
        new Item(CustomItemIdentifiers.SANGUINE_SCYTHE_OF_VITUR),
        new Item(CustomItemIdentifiers.SANGUINE_TWISTED_BOW),
        new Item(CustomItemIdentifiers.HOLY_GHRAZI_RAPIER),
        new Item(CustomItemIdentifiers.HOLY_SANGUINESTI_STAFF),
        new Item(CustomItemIdentifiers.HOLY_SCYTHE_OF_VITUR),
        new Item(CustomItemIdentifiers.TWISTED_BOW_I),
        new Item(ItemIdentifiers.TWISTED_BOW),
        new Item(ItemIdentifiers.SCYTHE_OF_VITUR),
        new Item(ItemIdentifiers.GHRAZI_RAPIER),
        new Item(ItemIdentifiers.ANCESTRAL_ROBE_TOP),
        new Item(ItemIdentifiers.ANCESTRAL_ROBE_BOTTOM),
        new Item(ItemIdentifiers.DRAGON_CLAWS),
        new Item(ItemIdentifiers.KODAI_WAND),
        new Item(ELDER_MAUL),
        new Item(ItemIdentifiers.ANCESTRAL_HAT),
        new Item(ItemIdentifiers.SANGUINESTI_STAFF)
    ),



    // keys
    CRYSTAL_KEY(AttributeKey.CRYSTAL_KEYS_OPENED, LogType.KEYS, "Crystal key", new int[] {ItemIdentifiers.CRYSTAL_KEY}, AttributeKey.CRYSTAL_KEY_LOG_CLAIMED, new Item[] { new Item(ItemIdentifiers.CRYSTAL_KEY+1,10) },
        //Drops
        new Item(ItemIdentifiers.RING_OF_RECOIL + 1), new Item(ItemIdentifiers.GUTHIX_REST4 + 1), new Item(ItemIdentifiers.ANGLERFISH + 1), new Item(ItemIdentifiers.DRAGON_JAVELIN), new Item(ItemIdentifiers.DRAGON_KNIFE), new Item(ItemIdentifiers.DRAGON_DART), new Item(ItemIdentifiers.DRAGON_BOOTS + 1), new Item(ItemIdentifiers.STAMINA_POTION4 + 1), new Item(ItemIdentifiers.DARK_BOW), new Item(ItemIdentifiers.AMULET_OF_FURY), new Item(ItemIdentifiers.MAGES_BOOK), new Item(ItemIdentifiers.MASTER_WAND), new Item(ItemIdentifiers.DRAGONFIRE_SHIELD)),

    WILDERNESS_KEY(AttributeKey.WILDY_KEYS_OPENED, LogType.KEYS, "Wilderness key", new int[] {CustomItemIdentifiers.WILDERNESS_KEY}, AttributeKey.WILDERNESS_KEY_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.WILDERNESS_KEY,5) },
        //Common
        new Item(SERPENTINE_HELM),
        new Item(MAGMA_HELM),
        new Item(TANZANITE_HELM),
        new Item(NEITIZNOT_FACEGUARD),
        new Item(TOXIC_STAFF_OF_THE_DEAD),
        new Item(DINHS_BULWARK),
        new Item(DRAGONFIRE_SHIELD),
        new Item(ANCIENT_WYVERN_SHIELD),
        new Item(DRAGONFIRE_WARD),
        new Item(ANKOU_GLOVES),
        new Item(ANKOU_SOCKS),
        new Item(TWISTED_ANCESTRAL_COLOUR_KIT),
        new Item(ARMADYL_GODSWORD),
        new Item(STATIUSS_WARHAMMER),
        //Uncommon
        new Item(ARMADYL_GODSWORD),
        new Item(DRAGON_CLAWS),
        new Item(TOXIC_BLOWPIPE),
        new Item(TOXIC_STAFF_OF_THE_DEAD),
        new Item(MORRIGANS_COIF),
        new Item(MORRIGANS_LEATHER_BODY),
        new Item(MORRIGANS_LEATHER_CHAPS),
        new Item(ZURIELS_HOOD),
        new Item(ZURIELS_ROBE_TOP),
        new Item(ZURIELS_ROBE_BOTTOM),
        new Item(VOLATILE_ORB),
        new Item(HARMONISED_ORB),
        new Item(ELDRITCH_ORB),
        new Item(SANTA_HAT),
        new Item(ANKOU_MASK),
        new Item(DINHS_BULWARK),
        //Rare
        new Item(VESTAS_LONGSWORD),
        new Item(VESTAS_CHAINBODY),
        new Item(VESTAS_PLATESKIRT),
        new Item(STATIUSS_FULL_HELM),
        new Item(STATIUSS_PLATEBODY),
        new Item(STATIUSS_PLATELEGS),
        new Item(STATIUSS_WARHAMMER),
        new Item(GHRAZI_RAPIER),
        new Item(ELDER_MAUL),
        new Item(NIGHTMARE_STAFF),
        new Item(ANCESTRAL_HAT),
        new Item(ANCESTRAL_ROBE_TOP),
        new Item(ANCESTRAL_ROBE_BOTTOM),
        new Item(JUSTICIAR_FACEGUARD),
        new Item(JUSTICIAR_CHESTGUARD),
        new Item(JUSTICIAR_LEGGUARDS),
        new Item(KODAI_WAND),
        new Item(BLADE_OF_SAELDOR),
        new Item(INVERTED_SANTA_HAT),
        new Item(ANKOU_TOP),
        new Item(ANKOUS_LEGGINGS),
        new Item(DRAGON_CLAWS),
        //Ext rare
        new Item(BOW_OF_FAERDHINEN_3),
        new Item(ANCESTRAL_HAT),
        new Item(ANCESTRAL_ROBE_TOP),
        new Item(ANCESTRAL_ROBE_BOTTOM),
        new Item(JUSTICIAR_FACEGUARD),
        new Item(JUSTICIAR_CHESTGUARD),
        new Item(JUSTICIAR_LEGGUARDS)
        ),
    LARRANS_KEY_I(AttributeKey.LARRANS_KEYS_TIER_ONE_USED, LogType.KEYS, "Larran's key tier I", new int[] {LARRANS_KEY_TIER_I}, AttributeKey.LARRANS_KEY_TIER_I_LOG_CLAIMED, new Item[] { new Item(LARRANS_KEY_TIER_I,5) },
        //Drops
        new Item(BLOOD_MONEY),
        new Item(DRAGON_DART),
        new Item(DRAGON_KNIFE),
        new Item(DRAGON_JAVELIN),
        new Item(DRAGON_THROWNAXE),
        new Item(ANTIVENOM4+1),
        new Item(GUTHIX_REST4+1),
        new Item(OBSIDIAN_HELMET),
        new Item(OBSIDIAN_PLATEBODY),
        new Item(OBSIDIAN_PLATELEGS),
        new Item(RANGERS_TUNIC),
        new Item(REGEN_BRACELET),
        new Item(GRANITE_MAUL_24225),
        new Item(BERSERKER_RING_I),
        new Item(ARCHERS_RING_I),
        new Item(SEERS_RING_I),
        new Item(WARRIOR_RING_I),
        new Item(PRIMORDIAL_BOOTS),
        new Item(PEGASIAN_BOOTS),
        new Item(ETERNAL_BOOTS),
        new Item(ABYSSAL_TENTACLE),
        new Item(BANDOS_CHESTPLATE),
        new Item(BANDOS_TASSETS),
        new Item(BLADE_OF_SAELDOR),
        new Item(BANDOS_GODSWORD),
        new Item(SARADOMIN_GODSWORD),
        new Item(ZAMORAK_GODSWORD),
        new Item(ARMADYL_CHAINSKIRT),
        new Item(ARMADYL_CHESTPLATE),
        new Item(ARMADYL_HELMET),
        new Item(SERPENTINE_HELM),
        new Item(ZAMORAKIAN_HASTA),
        new Item(FREMENNIK_KILT),
        new Item(DRAGON_CROSSBOW),
        new Item(OPAL_DRAGON_BOLTS_E),
        new Item(DIAMOND_DRAGON_BOLTS_E),
        new Item(DRAGONSTONE_DRAGON_BOLTS_E),
        new Item(ONYX_DRAGON_BOLTS_E),
        new Item(DRAGON_SCIMITAR_OR),
        new Item(ARMADYL_GODSWORD),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(TORMENTED_BRACELET),
        new Item(ABYSSAL_DAGGER),
        new Item(DRAGON_CLAWS),
        new Item(TOXIC_BLOWPIPE),
        new Item(TOXIC_STAFF_OF_THE_DEAD),
        new Item(UNCHARGED_TOXIC_TRIDENT),
        new Item(ABYSSAL_BLUDGEON),
        new Item(VESTAS_LONGSWORD),
        new Item(STATIUSS_WARHAMMER),
        new Item(DRAGON_WARHAMMER),
        new Item(ELDER_MAUL),
        new Item(DINHS_BULWARK),
        new Item(BOW_OF_FAERDHINEN_3)
    ),

    LARRANS_KEY_II(AttributeKey.LARRANS_KEYS_TIER_TWO_USED, LogType.KEYS, "Larran's key tier II", new int[] {LARRANS_KEY_TIER_II}, AttributeKey.LARRANS_KEY_TIER_II_LOG_CLAIMED, new Item[] { new Item(LARRANS_KEY_TIER_II,5) },
        //Drops
        new Item(PRIMORDIAL_BOOTS),
        new Item(PEGASIAN_BOOTS),
        new Item(ETERNAL_BOOTS),
        new Item(ABYSSAL_TENTACLE),
        new Item(BANDOS_CHESTPLATE),
        new Item(BANDOS_TASSETS),
        new Item(BLADE_OF_SAELDOR),
        new Item(BANDOS_GODSWORD),
        new Item(SARADOMIN_GODSWORD),
        new Item(ZAMORAK_GODSWORD),
        new Item(ARMADYL_CHAINSKIRT),
        new Item(ARMADYL_CHESTPLATE),
        new Item(ARMADYL_HELMET),
        new Item(BLOOD_MONEY),
        new Item(SERPENTINE_HELM),
        new Item(ZAMORAKIAN_HASTA),
        new Item(FREMENNIK_KILT),
        new Item(DRAGON_CROSSBOW),
        new Item(OPAL_DRAGON_BOLTS_E),
        new Item(DIAMOND_DRAGON_BOLTS_E),
        new Item(DRAGONSTONE_DRAGON_BOLTS_E),
        new Item(ONYX_DRAGON_BOLTS_E),
        new Item(DRAGON_SCIMITAR_OR),
        new Item(ARMADYL_GODSWORD),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(TORMENTED_BRACELET),
        new Item(ABYSSAL_DAGGER),
        new Item(DRAGON_CLAWS),
        new Item(TOXIC_BLOWPIPE),
        new Item(TOXIC_STAFF_OF_THE_DEAD),
        new Item(UNCHARGED_TOXIC_TRIDENT),
        new Item(ABYSSAL_BLUDGEON),
        new Item(MORRIGANS_COIF),
        new Item(MORRIGANS_LEATHER_BODY),
        new Item(MORRIGANS_LEATHER_CHAPS),
        new Item(ZURIELS_HOOD),
        new Item(ZURIELS_ROBE_TOP),
        new Item(ZURIELS_ROBE_BOTTOM),
        new Item(ZURIELS_STAFF),
        new Item(DINHS_BULWARK),
        new Item(VESTAS_LONGSWORD),
        new Item(STATIUSS_WARHAMMER),
        new Item(DRAGON_WARHAMMER),
        new Item(ELDER_MAUL),
        new Item(GHRAZI_RAPIER),
        new Item(KODAI_WAND),
        new Item(VOLATILE_ORB),
        new Item(ELDRITCH_ORB),
        new Item(HARMONISED_ORB),
        new Item(ARMADYL_GODSWORD_OR)
    ),

    LARRANS_KEY_III(AttributeKey.LARRANS_KEYS_TIER_THREE_USED, LogType.KEYS, "Larran's key tier III", new int[] {LARRANS_KEY_TIER_III}, AttributeKey.LARRANS_KEY_TIER_III_LOG_CLAIMED, new Item[] { new Item(LARRANS_KEY_TIER_III,5) },
        //Drops
        new Item(ARMADYL_GODSWORD),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(TORMENTED_BRACELET),
        new Item(ABYSSAL_DAGGER),
        new Item(DRAGON_CLAWS),
        new Item(TOXIC_BLOWPIPE),
        new Item(TOXIC_STAFF_OF_THE_DEAD),
        new Item(UNCHARGED_TOXIC_TRIDENT),
        new Item(ABYSSAL_BLUDGEON),
        new Item(DINHS_BULWARK),
        new Item(MORRIGANS_COIF),
        new Item(MORRIGANS_LEATHER_BODY),
        new Item(MORRIGANS_LEATHER_CHAPS),
        new Item(ZURIELS_STAFF),
        new Item(ZURIELS_HOOD),
        new Item(ZURIELS_ROBE_TOP),
        new Item(ZURIELS_ROBE_BOTTOM),
        new Item(STATIUSS_FULL_HELM),
        new Item(STATIUSS_PLATEBODY),
        new Item(STATIUSS_PLATELEGS),
        new Item(STATIUSS_WARHAMMER),
        new Item(VESTAS_CHAINBODY),
        new Item(VESTAS_PLATESKIRT),
        new Item(VESTAS_LONGSWORD),
        new Item(VESTAS_LONGSWORD),
        new Item(STATIUSS_WARHAMMER),
        new Item(DRAGON_WARHAMMER),
        new Item(ELDER_MAUL),
        new Item(GHRAZI_RAPIER),
        new Item(KODAI_WAND),
        new Item(VOLATILE_ORB),
        new Item(ELDRITCH_ORB),
        new Item(HARMONISED_ORB),
        new Item(ARMADYL_GODSWORD_OR),
        new Item(NIGHTMARE_STAFF),
        new Item(DRAGON_CLAWS_OR),
        new Item(FEROX_COINS),
        new Item(BARRELCHEST_PET),
        new Item(SANGUINESTI_STAFF)
    ),

    SLAYER_KEY(AttributeKey.SLAYER_KEYS_OPENED, LogType.KEYS, "Slayer key", new int[] {CustomItemIdentifiers.SLAYER_KEY}, AttributeKey.SLAYER_KEY_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.SLAYER_KEY,5) },
        new Item(DIVINE_SUPER_COMBAT_POTION4 + 1, 10),
        new Item(STAMINA_POTION4 + 1, 10),
        new Item(ANTIVENOM4 + 1, 10),
        new Item(SUPER_ANTIFIRE_POTION4 + 1, 10),
        new Item(REGEN_BRACELET + 1, 1),
        new Item(WARRIOR_RING + 1, 1),
        new Item(ARCHERS_RING + 1, 1),
        new Item(BERSERKER_RING + 1, 1),
        new Item(SEERS_RING + 1, 1),
        new Item(BLOOD_MONEY),
        new Item(AMULET_OF_FURY),
        new Item(ABYSSAL_TENTACLE),
        new Item(RANGER_BOOTS),
        new Item(ROBIN_HOOD_HAT),
        new Item(DIVINE_SUPER_COMBAT_POTION4 + 1, 50),
        new Item(STAMINA_POTION4 + 1, 25),
        new Item(SUPER_ANTIFIRE_POTION4 + 1, 25),
        new Item(DRAGON_CROSSBOW),
        new Item(ARMADYL_HELMET),
        new Item(ABYSSAL_BLUDGEON),
        new Item(BANDOS_GODSWORD),
        new Item(ZAMORAK_GODSWORD),
        new Item(SARADOMIN_GODSWORD),
        new Item(DRAGONFIRE_SHIELD),
        new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX),
        new Item(ARMADYL_GODSWORD),
        new Item(BANDOS_TASSETS),
        new Item(BANDOS_CHESTPLATE),
        new Item(ARMADYL_CHAINSKIRT),
        new Item(ARMADYL_CHESTPLATE),
        new Item(ARMADYL_CROSSBOW),
        new Item(SERPENTINE_HELM),
        new Item(TOXIC_STAFF_OF_THE_DEAD),
        new Item(ETERNAL_BOOTS),
        new Item(PEGASIAN_BOOTS),
        new Item(PRIMORDIAL_BOOTS)
    ),
    CHAMBERS_OF_XERIC(AttributeKey.CHAMBERS_OF_XERIC, LogType.RAIDS, "Chambers of Xeric", new int[] {RAIDS_KEY}, AttributeKey.CHAMBERS_OF_XERIC_LOG_CLAIMED, new Item[] { new Item(CustomItemIdentifiers.SLAYER_KEY,1) },
        new Item(OLMLET),
        new Item(METAMORPHIC_DUST),
        new Item(TWISTED_BOW),
        new Item(ELDER_MAUL),
        new Item(KODAI_INSIGNIA),
        new Item(DRAGON_CLAWS),
        new Item(ANCESTRAL_HAT),
        new Item(ANCESTRAL_ROBE_TOP),
        new Item(ANCESTRAL_ROBE_BOTTOM),
        new Item(DINHS_BULWARK),
        new Item(DEXTEROUS_PRAYER_SCROLL),
        new Item(ARCANE_PRAYER_SCROLL),
        new Item(DRAGON_HUNTER_CROSSBOW),
        new Item(TWISTED_BUCKLER),
        new Item(TORN_PRAYER_SCROLL),
        new Item(DARK_RELIC),
        new Item(ONYX),
        new Item(TWISTED_ANCESTRAL_COLOUR_KIT),
        new Item(XERICS_GUARD),
        new Item(XERICS_WARRIOR),
        new Item(XERICS_SENTINEL),
        new Item(XERICS_GENERAL),
        new Item(XERICS_CHAMPION)
    ),

    REVENANTS(AttributeKey.REVENANTS_KILLED, LogType.OTHER, "Revenants", new int[] {NpcIdentifiers.REVENANT_IMP, NpcIdentifiers.REVENANT_CYCLOPS, NpcIdentifiers.REVENANT_DARK_BEAST, NpcIdentifiers.REVENANT_DEMON, NpcIdentifiers.REVENANT_DRAGON, NpcIdentifiers.REVENANT_GOBLIN, NpcIdentifiers.REVENANT_HELLHOUND, NpcIdentifiers.REVENANT_HOBGOBLIN, NpcIdentifiers.REVENANT_KNIGHT, NpcIdentifiers.REVENANT_ORK, NpcIdentifiers.REVENANT_PYREFIEND}, AttributeKey.REVENANTS_LOG_CLAIMED, new Item[] { new Item(CRAWS_BOW), new Item(VIGGORAS_CHAINMACE), new Item(THAMMARONS_SCEPTRE) },
        //Drops
        new Item(VESTAS_SPEAR), new Item(VESTAS_LONGSWORD), new Item(VESTAS_CHAINBODY), new Item(VESTAS_PLATESKIRT), new Item(STATIUSS_WARHAMMER), new Item(STATIUSS_FULL_HELM), new Item(STATIUSS_PLATEBODY), new Item(STATIUSS_PLATELEGS), new Item(ZURIELS_STAFF), new Item(ZURIELS_HOOD), new Item(ZURIELS_ROBE_TOP), new Item(ZURIELS_ROBE_BOTTOM), new Item(MORRIGANS_COIF), new Item(MORRIGANS_LEATHER_BODY), new Item(MORRIGANS_LEATHER_CHAPS), new Item(MORRIGANS_JAVELIN), new Item(MORRIGANS_THROWING_AXE), new Item(VIGGORAS_CHAINMACE), new Item(CRAWS_BOW), new Item(THAMMARONS_SCEPTRE), new Item(AMULET_OF_AVARICE), new Item(BRACELET_OF_ETHEREUM_UNCHARGED), new Item(ANCIENT_RELIC), new Item(ANCIENT_EFFIGY), new Item(ANCIENT_MEDALLION), new Item(ItemIdentifiers.ANCIENT_STATUETTE), new Item(ANCIENT_TOTEM), new Item(ANCIENT_EMBLEM), new Item(REVENANT_CAVE_TELEPORT), new Item(REVENANT_ETHER)
    ),

    ANCIENT_REVENANTS(AttributeKey.ANCIENT_REVENANTS_KILLED, LogType.OTHER, "Ancient Revenants", new int[] {ANCIENT_REVENANT_DARK_BEAST, ANCIENT_REVENANT_ORK, ANCIENT_REVENANT_CYCLOPS, ANCIENT_REVENANT_DRAGON, ANCIENT_REVENANT_KNIGHT}, AttributeKey.ANCIENT_REVENANTS_LOG_CLAIMED, new Item[] { new Item(ANCIENT_VESTAS_LONGSWORD), new Item(ANCIENT_STATIUSS_WARHAMMER) },
        //Drops
        new Item(VESTAS_SPEAR), new Item(VESTAS_LONGSWORD), new Item(VESTAS_CHAINBODY), new Item(VESTAS_PLATESKIRT), new Item(STATIUSS_WARHAMMER), new Item(STATIUSS_FULL_HELM), new Item(STATIUSS_PLATEBODY), new Item(STATIUSS_PLATELEGS), new Item(ZURIELS_STAFF), new Item(ZURIELS_HOOD), new Item(ZURIELS_ROBE_TOP), new Item(ZURIELS_ROBE_BOTTOM), new Item(MORRIGANS_COIF), new Item(MORRIGANS_LEATHER_BODY), new Item(MORRIGANS_LEATHER_CHAPS), new Item(MORRIGANS_JAVELIN), new Item(MORRIGANS_THROWING_AXE), new Item(VIGGORAS_CHAINMACE), new Item(CRAWS_BOW), new Item(THAMMARONS_SCEPTRE), new Item(AMULET_OF_AVARICE), new Item(DARK_ANCIENT_EMBLEM), new Item(DARK_ANCIENT_RELIC), new Item(DARK_ANCIENT_EFFIGY), new Item(DARK_ANCIENT_MEDALLION), new Item(DARK_ANCIENT_STATUETTE), new Item(DARK_ANCIENT_TOTEM), new Item(DARK_ANCIENT_EMBLEM), new Item(ANCIENT_VESTAS_LONGSWORD), new Item(ANCIENT_STATIUSS_WARHAMMER)
    ),

    SLAYER(null, LogType.OTHER, "Slayer",
        //Npcs that drop these items
        new int[] {NpcIdentifiers.CRAWLING_HAND_448, NpcIdentifiers.CRAWLING_HAND_449, NpcIdentifiers.CRAWLING_HAND_450, NpcIdentifiers.CRAWLING_HAND_451, NpcIdentifiers.CRAWLING_HAND_452, NpcIdentifiers.CRAWLING_HAND_453, NpcIdentifiers.CRAWLING_HAND_454, NpcIdentifiers.CRAWLING_HAND_455, NpcIdentifiers.CRAWLING_HAND_456, NpcIdentifiers.CRAWLING_HAND_457, NpcIdentifiers.CRUSHING_HAND,
        NpcIdentifiers.COCKATRICE_419, NpcIdentifiers.COCKATRICE_420, NpcIdentifiers.COCKATHRICE, NpcIdentifiers.BASILISK_417, NpcIdentifiers.BASILISK_418, NpcIdentifiers.BASILISK_9283, NpcIdentifiers.BASILISK_9284, NpcIdentifiers.BASILISK_9285, NpcIdentifiers.BASILISK_9286, NpcIdentifiers.BASILISK_KNIGHT, NpcIdentifiers.BASILISK_SENTINEL, NpcIdentifiers.BASILISK_YOUNGLING, NpcIdentifiers.MONSTROUS_BASILISK, NpcIdentifiers.MONSTROUS_BASILISK_9287, NpcIdentifiers.MONSTROUS_BASILISK_9288,
        NpcIdentifiers.KURASK_410, NpcIdentifiers.KURASK_411, NpcIdentifiers.KING_KURASK, NpcIdentifiers.ABYSSAL_DEMON_415, NpcIdentifiers.ABYSSAL_DEMON_416, NpcIdentifiers.ABYSSAL_DEMON_7241, NpcIdentifiers.GREATER_ABYSSAL_DEMON, NpcIdentifiers.ABYSSAL_SIRE, NpcIdentifiers.ABYSSAL_SIRE_5887, NpcIdentifiers.ABYSSAL_SIRE_5888, NpcIdentifiers.ABYSSAL_SIRE_5889, NpcIdentifiers.ABYSSAL_SIRE_5890, NpcIdentifiers.ABYSSAL_SIRE_5891, NpcIdentifiers.ABYSSAL_SIRE_5908,
        NpcIdentifiers.GARGOYLE, NpcIdentifiers.GARGOYLE_1543, NpcIdentifiers.MARBLE_GARGOYLE_7408, NpcIdentifiers.TUROTH, NpcIdentifiers.TUROTH_427, NpcIdentifiers.TUROTH_428, NpcIdentifiers.TUROTH_429, NpcIdentifiers.TUROTH_430, NpcIdentifiers.TUROTH_431, NpcIdentifiers.TUROTH_432, NpcIdentifiers.CAVE_HORROR, NpcIdentifiers.CAVE_HORROR_1048, NpcIdentifiers.CAVE_HORROR_1049, NpcIdentifiers.CAVE_HORROR_1050, NpcIdentifiers.CAVE_HORROR_1051, NpcIdentifiers.CAVE_ABOMINATION,
        NpcIdentifiers.TALONED_WYVERN, NpcIdentifiers.SPITTING_WYVERN, NpcIdentifiers.LONGTAILED_WYVERN, NpcIdentifiers.ANCIENT_WYVERN, NpcIdentifiers.KING_BLACK_DRAGON, NpcIdentifiers.KING_BLACK_DRAGON_6502, NpcIdentifiers.BLACK_DRAGON, NpcIdentifiers.BLACK_DRAGON_253, NpcIdentifiers.BLACK_DRAGON_254, NpcIdentifiers.BLACK_DRAGON_255, NpcIdentifiers.BLACK_DRAGON_256, NpcIdentifiers.BLACK_DRAGON_257, NpcIdentifiers.BLACK_DRAGON_258, NpcIdentifiers.BLACK_DRAGON_259, NpcIdentifiers.BLACK_DRAGON_7861, NpcIdentifiers.BLACK_DRAGON_7862, NpcIdentifiers.BLACK_DRAGON_7863, NpcIdentifiers.BLACK_DRAGON_8084, NpcIdentifiers.BLACK_DRAGON_8085, NpcIdentifiers.BRUTAL_BLACK_DRAGON, NpcIdentifiers.BRUTAL_BLACK_DRAGON_8092, NpcIdentifiers.BRUTAL_BLACK_DRAGON_8092,
        NpcIdentifiers.VORKATH_8061, NpcIdentifiers.ADAMANT_DRAGON, NpcIdentifiers.ADAMANT_DRAGON_8090, NpcIdentifiers.RUNE_DRAGON, NpcIdentifiers.RUNE_DRAGON_8031, NpcIdentifiers.RUNE_DRAGON_8091, NpcIdentifiers.LAVA_DRAGON, NpcIdentifiers.MITHRIL_DRAGON, NpcIdentifiers.MITHRIL_DRAGON_8088, NpcIdentifiers.MITHRIL_DRAGON_8089, NpcIdentifiers.SKELETAL_WYVERN_466, NpcIdentifiers.SKELETAL_WYVERN_467, NpcIdentifiers.SKELETAL_WYVERN_468, NpcIdentifiers.SPIRITUAL_MAGE, NpcIdentifiers.SPIRITUAL_MAGE_2244, NpcIdentifiers.SPIRITUAL_MAGE_3161, NpcIdentifiers.SPIRITUAL_MAGE_3168,
        NpcIdentifiers.KRAKEN, NpcIdentifiers.DARK_BEAST, NpcIdentifiers.DARK_BEAST_7250, NpcIdentifiers.NIGHT_BEAST, NpcIdentifiers.SMOKE_DEVIL, NpcIdentifiers.SMOKE_DEVIL_6639, NpcIdentifiers.SMOKE_DEVIL_6655, NpcIdentifiers.SMOKE_DEVIL_8482, NpcIdentifiers.SMOKE_DEVIL_8483, NpcIdentifiers.NUCLEAR_SMOKE_DEVIL, NpcIdentifiers.THERMONUCLEAR_SMOKE_DEVIL, NpcIdentifiers.KALPHITE_QUEEN_6500, NpcIdentifiers.KALPHITE_QUEEN_6501, NpcIdentifiers.WYRM, NpcIdentifiers.WYRM_8611, NpcIdentifiers.DRAKE_8612, NpcIdentifiers.DRAKE_8613, NpcIdentifiers.HYDRA, NpcIdentifiers.ALCHEMICAL_HYDRA, NpcIdentifiers.ALCHEMICAL_HYDRA_8616, NpcIdentifiers.ALCHEMICAL_HYDRA_8617, NpcIdentifiers.ALCHEMICAL_HYDRA_8618, NpcIdentifiers.ALCHEMICAL_HYDRA_8619, NpcIdentifiers.ALCHEMICAL_HYDRA_8620, NpcIdentifiers.ALCHEMICAL_HYDRA_8621, NpcIdentifiers.ALCHEMICAL_HYDRA_8622, NpcIdentifiers.ALCHEMICAL_HYDRA_8634,
        NpcIdentifiers.BASILISK_KNIGHT, NpcIdentifiers.BASILISK_SENTINEL}, AttributeKey.SLAYER_LOG_CLAIMED, new Item[] { new Item(ABYSSAL_TENTACLE_24948), new Item(GRANITE_MAUL_24944) },
        //Drops
        new Item(CRAWLING_HAND_7975), new Item(COCKATRICE_HEAD), new Item(BASILISK_HEAD), new Item(KURASK_HEAD), new Item(ABYSSAL_HEAD), new Item(IMBUED_HEART), new Item(ETERNAL_GEM), new Item(DUST_BATTLESTAFF), new Item(MIST_BATTLESTAFF), new Item(ABYSSAL_WHIP), new Item(GRANITE_MAUL_24225), new Item(LEAFBLADED_SWORD), new Item(LEAFBLADED_BATTLEAXE), new Item(BLACK_MASK), new Item(GRANITE_LONGSWORD), new Item(WYVERN_VISAGE), new Item(DRACONIC_VISAGE),
        new Item(DRAGON_BOOTS), new Item(ABYSSAL_DAGGER), new Item(TRIDENT_OF_THE_SEAS), new Item(KRAKEN_TENTACLE), new Item(DARK_BOW), new Item(OCCULT_NECKLACE), new Item(DRAGON_CHAINBODY_3140), new Item(DRAGON_THROWNAXE), new Item(DRAGON_HARPOON), new Item(DRAGON_SWORD), new Item(DRAGON_KNIFE), new Item(DRAKES_TOOTH), new Item(DRAKES_CLAW), new Item(HYDRA_TAIL), new Item(HYDRAS_FANG), new Item(HYDRAS_EYE), new Item(HYDRAS_HEART), new Item(BASILISK_JAW)),

;

    private final AttributeKey attributeKey;
    private final LogType logType;
    private final String name;
    private final int[] key;
    private final AttributeKey rewardClaimed;
    private final Item[] reward;
    private final Item[] obtainables;

    Collection(AttributeKey attributeKey, LogType logType, String name, int[] key, AttributeKey rewardClaimed, Item[] reward, Item... obtainables) {
        this.attributeKey = attributeKey;
        this.logType = logType;
        this.name = name;
        this.key = key;
        this.rewardClaimed = rewardClaimed;
        this.reward = reward;
        this.obtainables = obtainables;
    }

    public AttributeKey getAttributeKey() {
        return attributeKey;
    }

    public String getName() {
        return name;
    }

    public int[] getKey() {
        return key;
    }

    public AttributeKey getRewardClaimedKey() {
        return rewardClaimed;
    }

    public Item[] getReward() {
        return reward;
    }

    public Item[] getObtainables() {
        return obtainables;
    }

    public LogType getLogType() {
        return logType;
    }

    /**
     * The amount of items we can obtain.
     */
    public int totalCollectables() {
        return obtainables.length;
    }

    /**
     * Gets all the data for a specific type.
     * @param logType the log type that is being sorted at alphabetical order
     */
    public static List<Collection> getAsList(LogType logType) {
        return Arrays.stream(values()).filter(type -> type.getLogType() == logType).sorted(Comparator.comparing(Enum::name)).collect(Collectors.toList());
    }
}
