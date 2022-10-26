package com.ferox.game.content.mechanics.item_dispenser;

import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.ItemIdentifiers;

import java.util.Optional;

/**
 * This enum will hold all the prices and data for the item dispenser.
 *
 * @author Patrick van Elderen | February, 14, 2021, 23:12
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public enum Cart {

    JAWA_PET(CustomItemIdentifiers.JAWA_PET,600),
    CENTAUR_MALE(CustomItemIdentifiers.CENTAUR_MALE,800),
    CENTAUR_FEMALE(CustomItemIdentifiers.CENTAUR_FEMALE,800),
    DEMENTOR_PET(CustomItemIdentifiers.DEMENTOR_PET,1250),
    FENRIR_GREYBACK_JR(CustomItemIdentifiers.FENRIR_GREYBACK_JR,350),
    FLUFFY_JR(CustomItemIdentifiers.FLUFFY_JR,350),
    ANCIENT_BARRELCHEST_PET(CustomItemIdentifiers.ANCIENT_BARRELCHEST_PET,350),
    ANCIENT_CHAOS_ELEMENTAL_PET(CustomItemIdentifiers.ANCIENT_CHAOS_ELEMENTAL_PET,750),
    ANCIENT_KING_BLACK_DRAGON_PET(CustomItemIdentifiers.ANCIENT_KING_BLACK_DRAGON_PET,350),
    ZRIAWK(CustomItemIdentifiers.ZRIAWK,2000),
    FAWKES(CustomItemIdentifiers.FAWKES,1500),
    BARRELCHEST_PET(CustomItemIdentifiers.BARRELCHEST_PET,350),
    WAMPA(CustomItemIdentifiers.WAMPA,1000),
    NIFFLER(CustomItemIdentifiers.NIFFLER,750),
    BABY_ARAGOG(CustomItemIdentifiers.BABY_ARAGOG,1250),
    FOUNDER_IMP(CustomItemIdentifiers.FOUNDER_IMP,850),
    BABY_LAVA_DRAGON(CustomItemIdentifiers.BABY_LAVA_DRAGON,800),
    MINI_NECROMANCER(CustomItemIdentifiers.MINI_NECROMANCER,1500),
    PET_CORRUPTED_NECHRYARCH(CustomItemIdentifiers.PET_CORRUPTED_NECHRYARCH,1500),
    TZREKZUK(ItemIdentifiers.TZREKZUK,500),
    GRIM_REAPER_PET(CustomItemIdentifiers.GRIM_REAPER_PET,750),
    GENIE_PET(CustomItemIdentifiers.GENIE_PET,500),
    DHAROK_PET(CustomItemIdentifiers.DHAROK_PET,350),
    PET_ZOMBIES_CHAMPION(CustomItemIdentifiers.PET_ZOMBIES_CHAMPION,350),
    BABY_ABYSSAL_DEMON(CustomItemIdentifiers.BABY_ABYSSAL_DEMON,500),
    BABY_SQUIRT(CustomItemIdentifiers.BABY_SQUIRT,500),
    BABY_DARK_BEAST_EGG(CustomItemIdentifiers.BABY_DARK_BEAST_EGG,500),
    ELEMENTAL_BOW(CustomItemIdentifiers.ELEMENTAL_BOW,1000),
    HEAVY_BALLISTA(ItemIdentifiers.HEAVY_BALLISTA,75),
    DINHS_BULWARK(ItemIdentifiers.DINHS_BULWARK,75),
    ARMADYL_GODSWORD(ItemIdentifiers.ARMADYL_GODSWORD,50),
    BLESSED_SARADOMIN_SWORD(ItemIdentifiers.SARADOMINS_BLESSED_SWORD,75),
    FEROCIOUS_GLOVES(ItemIdentifiers.FEROCIOUS_GLOVES, 1750),
    ANCIENT_VESTAS_LONGSWORD(CustomItemIdentifiers.ANCIENT_VESTAS_LONGSWORD,750),
    ANCIENT_STATIUSS_WARHAMMER(CustomItemIdentifiers.ANCIENT_STATIUSS_WARHAMMER,750),
    ANCIENT_WARRIOR_AXE_C(CustomItemIdentifiers.ANCIENT_WARRIOR_AXE_C,2500),
    ANCIENT_WARRIOR_MAUL_C(CustomItemIdentifiers.ANCIENT_WARRIOR_MAUL_C,2500),
    ANCIENT_WARRIOR_SWORD_C(CustomItemIdentifiers.ANCIENT_WARRIOR_SWORD_C,2500),
    ANCIENT_FACEGAURD(CustomItemIdentifiers.ANCIENT_FACEGAURD,1250),
    NEITIZNOT_FACEGUARD(ItemIdentifiers.NEITIZNOT_FACEGUARD,500),
    ANCIENT_WARRIOR_AXE(CustomItemIdentifiers.ANCIENT_WARRIOR_AXE,750),
    ANCIENT_WARRIOR_MAUL(CustomItemIdentifiers.ANCIENT_WARRIOR_MAUL,750),
    ANCIENT_WARRIOR_SWORD(CustomItemIdentifiers.ANCIENT_WARRIOR_SWORD,750),
    DRAGON_HUNTER_CROSSBOW(ItemIdentifiers.DRAGON_HUNTER_CROSSBOW,125),
    ELDER_MAUL(ItemIdentifiers.ELDER_MAUL,750),
    MAGMA_HELM(ItemIdentifiers.MAGMA_HELM,75),
    TANZANITE_HELM(ItemIdentifiers.TANZANITE_HELM,75),
    HOLY_GHRAZI_RAPIER(CustomItemIdentifiers.HOLY_GHRAZI_RAPIER,1500),
    HOLY_SANGUINESTI_STAFF(CustomItemIdentifiers.HOLY_SANGUINESTI_STAFF,1500),
    HOLY_SCYTHE_OF_VITUR(CustomItemIdentifiers.HOLY_SCYTHE_OF_VITUR,12000),
    SANGUINE_SCYTHE_OF_VITUR(CustomItemIdentifiers.SANGUINE_SCYTHE_OF_VITUR,35000),
    SANGUINE_TWISTED_BOW(CustomItemIdentifiers.SANGUINE_TWISTED_BOW,35000),
    PRIMORDIAL_BOOTS_OR(CustomItemIdentifiers.PRIMORDIAL_BOOTS_OR,1500),
    PEGASIAN_BOOTS_OR(CustomItemIdentifiers.PEGASIAN_BOOTS_OR,1500),
    ETERNAL_BOOTS_OR(CustomItemIdentifiers.ETERNAL_BOOTS_OR,1500),
    BLOOD_MONEY_CHEST(CustomItemIdentifiers.BLOOD_MONEY_CASKET,125),
    ANCIENT_WYVERN_SHIELD(ItemIdentifiers.ANCIENT_WYVERN_SHIELD,350),
    DRAGONFRIE_WARD(ItemIdentifiers.DRAGONFIRE_WARD,250),
    SERPENTINE_HELM(ItemIdentifiers.SERPENTINE_HELM,50),
    TRIDENT_OF_THE_SWAMP(ItemIdentifiers.TRIDENT_OF_THE_SWAMP,250),
    TOXIC_BLOWPIPE(ItemIdentifiers.TOXIC_BLOWPIPE,400),
    TOXIC_STAFF_OF_THE_DEAD(ItemIdentifiers.TOXIC_STAFF_OF_THE_DEAD,150),
    ANKOU_GLOVES(ItemIdentifiers.ANKOU_GLOVES,1000),
    ANKOU_MASK(ItemIdentifiers.ANKOU_MASK,1000),
    ANKOU_SOCKS(ItemIdentifiers.ANKOU_SOCKS,1000),
    ANKOU_TOP(ItemIdentifiers.ANKOU_TOP,1000),
    ANKOUS_LEGGINGS(ItemIdentifiers.ANKOUS_LEGGINGS,1000),
    DEXTEROUS_PRAYER_SCROLL(ItemIdentifiers.DEXTEROUS_PRAYER_SCROLL,1250),
    ARCANE_PRAYER_SCROLL(ItemIdentifiers.ARCANE_PRAYER_SCROLL,1250),
    INQUISITORS_GREAT_HELM(ItemIdentifiers.INQUISITORS_GREAT_HELM,400),
    INQUISITORS_HAUBERK(ItemIdentifiers.INQUISITORS_HAUBERK,1000),
    INQUISITORS_PLATESKIRT(ItemIdentifiers.INQUISITORS_PLATESKIRT,1000),
    SPECTRAL_SPIRIT_SHIELD(ItemIdentifiers.SPECTRAL_SPIRIT_SHIELD,75),
    NECKLACE_OF_ANGUISH(ItemIdentifiers.NECKLACE_OF_ANGUISH,250),
    AMULET_OF_TORTURE(ItemIdentifiers.AMULET_OF_TORTURE,250),
    TORMENTED_BRACELET(ItemIdentifiers.TORMENTED_BRACELET,250),
    RING_OF_SUFFERING(ItemIdentifiers.RING_OF_SUFFERING,250),
    DRAGON_WARHAMMER(ItemIdentifiers.DRAGON_WARHAMMER,125),
    BLADE_OF_SAELDOR(ItemIdentifiers.BLADE_OF_SAELDOR,300),
    DRAGON_HUNTER_LANCE(ItemIdentifiers.DRAGON_HUNTER_LANCE,125),
    ARMADYL_GODSWORD_OR(ItemIdentifiers.ARMADYL_GODSWORD_OR,800),
    BANDOS_GODSWORD_OR(ItemIdentifiers.BANDOS_GODSWORD_OR,100),
    SARADOMIN_GODSWORD_OR(ItemIdentifiers.SARADOMIN_GODSWORD_OR,100),
    ZAMORAK_GODSWORD_OR(ItemIdentifiers.ZAMORAK_GODSWORD_OR,100),
    DRAGON_CLAWS(ItemIdentifiers.DRAGON_CLAWS,200),
    DRAGON_CLAWS_OR(CustomItemIdentifiers.DRAGON_CLAWS_OR,1000),
    TWISTED_BOW(ItemIdentifiers.TWISTED_BOW,5000),
    SCYTHE_OF_VITUR(ItemIdentifiers.SCYTHE_OF_VITUR,5000),
    ELYSIAN_SPIRIT_SHIELD(ItemIdentifiers.ELYSIAN_SPIRIT_SHIELD,7500),
    VESTAS_CHAINBODY(ItemIdentifiers.VESTAS_CHAINBODY,400),
    VESTAS_PLATESKIRT(ItemIdentifiers.VESTAS_PLATESKIRT,400),
    VESTAS_LONGSWORD(ItemIdentifiers.VESTAS_LONGSWORD,400),
    VESTAS_SPEAR(ItemIdentifiers.VESTAS_SPEAR,250),
    STATIUSS_FULL_HELM(ItemIdentifiers.STATIUSS_FULL_HELM,250),
    STATIUSS_PLATEBODY(ItemIdentifiers.STATIUSS_PLATEBODY,250),
    STATIUSS_PLATELEGS(ItemIdentifiers.STATIUSS_PLATELEGS,250),
    STATIUSS_WARHAMMER(ItemIdentifiers.STATIUSS_WARHAMMER,250),
    MORRIGANS_COIF(ItemIdentifiers.MORRIGANS_COIF,250),
    MORRIGANS_LEATHER_BODY(ItemIdentifiers.MORRIGANS_LEATHER_BODY,250),
    MORRIGANS_LEATHER_CHAPS(ItemIdentifiers.MORRIGANS_LEATHER_CHAPS,250),
    ZURIEL_STAFF(ItemIdentifiers.ZURIELS_STAFF,250),
    ZURIELS_HOOD(ItemIdentifiers.ZURIELS_HOOD,250),
    ZURIELS_ROBE_TOP(ItemIdentifiers.ZURIELS_ROBE_TOP,250),
    ZURIELS_ROBE_BOTTOM(ItemIdentifiers.ZURIELS_ROBE_BOTTOM,250),
    AVERNIC_DEFENDER(ItemIdentifiers.AVERNIC_DEFENDER,1250),
    INFERNAL_CAPE(ItemIdentifiers.INFERNAL_CAPE,1000),
    ARCANE_SPIRIT_SHIELD(ItemIdentifiers.ARCANE_SPIRIT_SHIELD,850),
    ANCESTRAL_HAT(ItemIdentifiers.ANCESTRAL_HAT,600),
    ANCESTRAL_ROBE_TOP(ItemIdentifiers.ANCESTRAL_ROBE_TOP,1000),
    ANCESTRAL_ROBE_BOTTOM(ItemIdentifiers.ANCESTRAL_ROBE_BOTTOM,1000),
    TWISTED_ANCESTRAL_HAT(ItemIdentifiers.TWISTED_ANCESTRAL_HAT,800),
    TWISTED_ANCESTRAL_ROBE_TOP(ItemIdentifiers.TWISTED_ANCESTRAL_ROBE_TOP,1500),
    TWISTED_ANCESTRAL_ROBE_BOTTOM(ItemIdentifiers.TWISTED_ANCESTRAL_ROBE_BOTTOM,1500),
    JUSTICIAR_FACEGUARD(ItemIdentifiers.JUSTICIAR_FACEGUARD,400),
    JUSTICIAR_CHESTGUARD(ItemIdentifiers.JUSTICIAR_CHESTGUARD,500),
    JUSTICIAR_LEGGUARDS(ItemIdentifiers.JUSTICIAR_LEGGUARDS,500),
    VOLATILE_ORB(ItemIdentifiers.VOLATILE_ORB,1200),
    HARMONISED_ORB(ItemIdentifiers.HARMONISED_ORB,1200),
    ELDRITCH_ORB(ItemIdentifiers.ELDRITCH_ORB,400),
    NIGHTMARE_STAFF(ItemIdentifiers.NIGHTMARE_STAFF,1500),
    VIGGORAS_CHAINMACE(ItemIdentifiers.VIGGORAS_CHAINMACE,250),
    CRAWS_BOW(ItemIdentifiers.CRAWS_BOW,300),
    THAMMARONS_SCEPTRE(ItemIdentifiers.THAMMARONS_SCEPTRE,250),
    AMULET_OF_AVARICE(ItemIdentifiers.AMULET_OF_AVARICE,250),
    KODAI_WAND(ItemIdentifiers.KODAI_WAND,500),
    GHRAZI_RAPIER(ItemIdentifiers.GHRAZI_RAPIER,500),
    DARK_ELDER_MAUL(CustomItemIdentifiers.DARK_ELDER_MAUL,30000),//van onder na boven
    ;

    Cart(int item, int value) {
        this.item = item;
        this.value = value;
    }

    /**
     * The item that will be dispensed into Ferox coins.
     */
    public final int item;

    /**
     * The amount of coins we get in return.
     */
    public final int value;

    /**
     * A getter to gather enum info for specific items such as the value.
     * @param itemId The item to check
     * @return The info
     */
    public static Optional<Cart> get(int itemId) {
        for (Cart cart : Cart.values()) {
            if (cart.item == itemId) {
                return Optional.of(cart);
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "Cart{" +
            "item=" + item +
            ", value=" + value +
            '}';
    }
}
