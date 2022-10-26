package com.ferox.game.world.entity.combat.formula;

import com.ferox.game.content.skill.impl.slayer.Slayer;
import com.ferox.game.content.skill.impl.slayer.SlayerConstants;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.npcs.bosses.abyssalsire.AbyssalSireState;
import com.ferox.game.world.entity.combat.prayer.default_prayer.DefaultPrayers;
import com.ferox.game.world.entity.combat.weapon.AttackType;
import com.ferox.game.world.entity.combat.weapon.FightStyle;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.container.ItemContainer;
import com.ferox.game.world.items.container.equipment.Equipment;
import com.ferox.game.world.items.container.equipment.EquipmentInfo;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.NpcIdentifiers;
import com.ferox.util.timers.TimerKey;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;

/**
 * Created by Bart on 8/15/2015.
 */
public class CombatFormula {

    /**
     * Checks if the Npc is a demon
     *
     * @param name The npc's name
     * @return true if the npc is in fact a demon, false otherwise.
     */
    public static boolean isDemon(String name) {
        return name.equalsIgnoreCase("Imp") || name.equalsIgnoreCase("Imp Champion") || name.equalsIgnoreCase("Lesser demon") || name.equalsIgnoreCase("Lesser Demon Champion") || name.equalsIgnoreCase("Greater demon") || name.equalsIgnoreCase("Black demon") || name.equalsIgnoreCase("Abyssal demon") || name.equalsIgnoreCase("Greater abyssal demon") || name.equalsIgnoreCase("Ice demon") || name.equalsIgnoreCase("Bloodveld") || name.equalsIgnoreCase("Insatiable Bloodveld") || name.equalsIgnoreCase("Mutated Bloodveld") || name.equalsIgnoreCase("Insatiable Mutated Bloodveld") || name.equalsIgnoreCase("Demonic gorilla") || name.equalsIgnoreCase("hellhound") || name.equalsIgnoreCase("Skeleton Hellhound") || name.equalsIgnoreCase("Greater Skeleton Hellhound") || name.equalsIgnoreCase("Nechryael") || name.equalsIgnoreCase("Death spawn") || name.equalsIgnoreCase("Greater Nechryael") || name.equalsIgnoreCase("Nechryarch") || name.equalsIgnoreCase("Chaotic death spawn");
    }

    /**
     * Checks if the Npc is a dragon.
     * @param name The npcs name
     * @return returns true if the npc is a dragon, false otherwise.
     */
    public static boolean isDragon(String name) {
        boolean exceptions = name.contains("Elvarg") || name.contains("Revenant dragon");
        return name.contains("Hungarian horntail") || name.contains("Wyvern") || name.contains("Basilisk (Right claw)") || name.contains("Basilisk (Left claw)") || name.contains("Basilisk") || name.contains("Great Olm") || name.contains("Wyrm") || name.contains("Drake") || name.contains("Hydra") || name.contains("Vorkath") || name.contains("Galvek") || name.contains("dragon") || name.contains("Dragon") && !exceptions;
    }

    public static boolean wearingTorags(Player player) {
        return player.getEquipment().containsAll(TORAGS_HELM, TORAGS_PLATEBODY, TORAGS_PLATELEGS);
    }

    public static boolean wearingFullDharoks(Player player) {
        return player.getEquipment().containsAll(DHAROKS_HELM, DHAROKS_PLATEBODY, DHAROKS_PLATELEGS, DHAROKS_GREATAXE);
    }

    public static boolean wearingDharoksArmour(Player player) {
        return player.getEquipment().containsAll(DHAROKS_HELM, DHAROKS_PLATEBODY, DHAROKS_PLATELEGS);
    }

    public static boolean hasAntiFireShield(Player player) {
        return (player.getEquipment().hasAt(EquipSlot.SHIELD, ANTIDRAGON_SHIELD) || player.getEquipment().hasAt(EquipSlot.SHIELD, DRAGONFIRE_SHIELD) || player.getEquipment().hasAt(EquipSlot.SHIELD, DRAGONFIRE_WARD) || player.getEquipment().hasAt(EquipSlot.SHIELD, ANCIENT_WYVERN_SHIELD));
    }

    public static boolean obbyArmour(Player player) {
        ItemContainer eq = player.getEquipment();
        return ((eq.hasAt(EquipSlot.HEAD, 21298) && eq.hasAt(EquipSlot.BODY, 21301) && eq.hasAt(EquipSlot.LEGS, 21304)));
    }

    public static boolean hasViggorasChainMace(Player player) {
        return ((player.getEquipment().hasAt(EquipSlot.WEAPON, VIGGORAS_CHAINMACE) || player.getEquipment().hasAt(EquipSlot.WEAPON, BEGINNER_CHAINMACE) || player.getEquipment().hasAt(EquipSlot.WEAPON, HWEEN_CHAINMACE)) && WildernessArea.inWilderness(player.tile()));
    }

    public static boolean hasThammaronSceptre(Player player) {
        ItemContainer eq = player.getEquipment();
        return (eq.hasAt(EquipSlot.WEAPON, 22555) && (WildernessArea.inWilderness(player.tile())));
    }

    public static boolean hasCrawsBow(Player player) {
        return ((player.getEquipment().hasAt(EquipSlot.WEAPON, CRAWS_BOW) || player.getEquipment().hasAt(EquipSlot.WEAPON, BEGINNER_CRAWS_BOW) || player.getEquipment().hasAt(EquipSlot.WEAPON, HWEEN_CRAWS_BOW)) && WildernessArea.inWilderness(player.tile()));
    }

    public static boolean hasAmuletOfAvarice(Player player) {
        ItemContainer eq = player.getEquipment();
        return (eq.hasAt(EquipSlot.WEAPON, 22557) && WildernessArea.inWilderness(player.tile()));
    }

    public static boolean berserkerNecklace(Player player) {
        return player.getEquipment().hasAt(EquipSlot.AMULET, 11128) && player.getEquipment().containsAny(6523, 6525, 6527, 6528);
    }

    public static boolean hasObbyWeapon(Player player) {
        ItemContainer eq = player.getEquipment();
        int[] weaponry = new int[]{6528, 6523, 6525};
        return ((eq.hasAt(EquipSlot.WEAPON, weaponry[0]) || (eq.hasAt(EquipSlot.WEAPON, weaponry[1]) || (eq.hasAt(EquipSlot.WEAPON, weaponry[2])))));
    }

    public static boolean voidBase(Player player) {
        return ((player.getEquipment().hasAt(EquipSlot.BODY, 8839) && player.getEquipment().hasAt(EquipSlot.LEGS, 8840)) || (player.getEquipment().hasAt(EquipSlot.BODY, 13072) && player.getEquipment().hasAt(EquipSlot.LEGS, 13073))) && player.getEquipment().hasAt(EquipSlot.HANDS, 8842);
    }

    public static boolean voidCustomBase(Player player) {
        return (player.getEquipment().hasAt(EquipSlot.BODY, ELITE_VOID_TOP_24943) && player.getEquipment().hasAt(EquipSlot.LEGS, ELITE_VOID_ROBE_24942) && player.getEquipment().hasAt(EquipSlot.HANDS, VOID_KNIGHT_GLOVES_24938));
    }

    public static boolean voidCustomRanger(Player player) {
        return player.getEquipment().hasAt(EquipSlot.HEAD, VOID_RANGER_HELM_24939) && voidCustomBase(player);
    }

    public static boolean voidCustomMelee(Player player) {
        return player.getEquipment().hasAt(EquipSlot.HEAD, VOID_MELEE_HELM_24941) && voidCustomBase(player);
    }

    public static boolean voidCustomMagic(Player player) {
        return player.getEquipment().hasAt(EquipSlot.HEAD, VOID_MAGE_HELM_24940) && voidCustomBase(player);
    }

    public static boolean voidRanger(Player player) {
        return player.getEquipment().hasAt(EquipSlot.HEAD, 11664) && voidBase(player);
    }

    public static boolean voidMelee(Player player) {
        return player.getEquipment().hasAt(EquipSlot.HEAD, 11665) && voidBase(player);
    }

    public static boolean voidMagic(Player player) {
        return player.getEquipment().hasAt(EquipSlot.HEAD, 11663) && voidBase(player);
    }

    public static boolean wearingEliteVoid(Player p) {
        return (p.getEquipment().contains(11665) || p.getEquipment().contains(11664) || p.getEquipment().contains(11663)) && p.getEquipment().hasAt(EquipSlot.BODY,13072) && p.getEquipment().hasAt(EquipSlot.LEGS,13073) && p.getEquipment().hasAt(EquipSlot.HANDS,8842);
    }

    public static int maximumMeleeHit(Player player, boolean special) {
        return maximumMeleeHit(player,special,true);
    }

    public static int maximumMeleeHit(Player player, boolean special, boolean includeNpcMax) {
        EquipmentInfo.Bonuses bonuses = EquipmentInfo.totalBonuses(player, World.getWorld().equipmentInfo());

        int maxHit;
        double specialMultiplier = 1;
        double prayerMultiplier = 1;
        double otherBonusMultiplier = 1;
        int strengthLevel = player.skills().level(Skills.STRENGTH);
        int combatStyleBonus = 0;

        Item helm = player.getEquipment().get(EquipSlot.HEAD);

        boolean special_slayer_helmet_i = helm != null && (helm.getId() == RED_SLAYER_HELMET_I || helm.getId() == TWISTED_SLAYER_HELMET_I || helm.getId() == TWISTED_SLAYER_HELMET_I_KBD_HEADS|| helm.getId() == TWISTED_SLAYER_HELMET_I_CORP_HEART  || helm.getId() == PURPLE_SLAYER_HELMET_I || helm.getId() == HYDRA_SLAYER_HELMET_I || helm.getId() == TWISTED_SLAYER_HELMET_I_JAD || helm.getId() == TWISTED_SLAYER_HELMET_I_INFERNAL_CAPE || helm.getId() == TWISTED_SLAYER_HELMET_I_VAMP_DUST);

        // Prayer additions
        if (DefaultPrayers.usingPrayer(player, DefaultPrayers.BURST_OF_STRENGTH)) {
            prayerMultiplier = 1.05;
        } else if (DefaultPrayers.usingPrayer(player, DefaultPrayers.SUPERHUMAN_STRENGTH)) {
            prayerMultiplier = 1.10;
        } else if (DefaultPrayers.usingPrayer(player, DefaultPrayers.ULTIMATE_STRENGTH)) {
            prayerMultiplier = 1.15;
        } else if (DefaultPrayers.usingPrayer(player, DefaultPrayers.CHIVALRY)) {
            prayerMultiplier = 1.18;
        } else if (DefaultPrayers.usingPrayer(player, DefaultPrayers.PIETY)) {
            prayerMultiplier = 1.23;
        }

        switch(player.getCombat().getFightType().getStyle()) {
            case AGGRESSIVE:
                combatStyleBonus = 3;
                break;
            case CONTROLLED:
                combatStyleBonus = 1;
                break;
            default:
                break;
        }

        Mob target = player.getCombat().getTarget();

        if (voidMelee(player) || wearingEliteVoid(player) || voidCustomMelee(player))
            otherBonusMultiplier += 0.1;

        Item weapon = player.getEquipment().get(EquipSlot.WEAPON);

        if(player.hasPetOut("Olmlet") && target != null && target.isNpc() && includeNpcMax) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonusMultiplier += 0.1;
            }
            if (player.isInsideRaids())
                otherBonusMultiplier += 0.1;
        }

        if(player.hasPetOut("Pet zombies champion") && target != null && target.isNpc() && target.getAsNpc().isWorldBoss() && includeNpcMax) {
            otherBonusMultiplier += 0.1;
        }

        if (hasViggorasChainMace(player) && target != null && target.isNpc() && includeNpcMax) {
            otherBonusMultiplier += 0.5;
        }

        if (player.getEquipment().hasAt(EquipSlot.WEAPON, VIGGORAS_CHAINMACE_C) && target != null && target.isNpc() && includeNpcMax) {
            otherBonusMultiplier += 0.5;
        }

        if (player.getEquipment().hasAt(EquipSlot.WEAPON, ARCLIGHT) && target != null && target.isNpc() && target.getAsNpc().def() != null && target.getAsNpc().def().name != null && isDemon(target.getAsNpc().def().name) && includeNpcMax) {
            otherBonusMultiplier += 0.7;
        }

        //Dragon hunter lance
        if (player.getEquipment().hasAt(EquipSlot.WEAPON, DRAGON_HUNTER_LANCE) && target != null && target.isNpc() && includeNpcMax) {
            Npc npc = (Npc) target;
            if (npc.def() != null && npc.def().name != null && CombatFormula.isDragon(npc.def().name)) {
                otherBonusMultiplier += 0.3;
            }
        }

        boolean ancientKingBlackDragonPet = player.hasPetOut("Ancient king black dragon");
        if(ancientKingBlackDragonPet && target != null && target.isNpc() && includeNpcMax) {
            Npc npc = (Npc) target;
            if (npc.def() != null && npc.def().name != null && CombatFormula.isDragon(npc.def().name)) {
                otherBonusMultiplier += 0.25;
            }
        }

        //The sword of gryffindor gives a 25% damage boost vs monsters
        if(player.getEquipment().hasAt(EquipSlot.WEAPON, CustomItemIdentifiers.SWORD_OF_GRYFFINDOR) && target != null && target.isNpc() && includeNpcMax) {
            otherBonusMultiplier += 0.25;
        }

        if (berserkerNecklace(player)) {
            otherBonusMultiplier += 0.2;
        }

        if (obbyArmour(player) && hasObbyWeapon(player)) {
            otherBonusMultiplier += 0.1;
        }

        if (special_slayer_helmet_i && target != null && target.isNpc() && includeNpcMax) {
            otherBonusMultiplier += 0.1;
        }

        if (player.getEquipment().contains(ItemIdentifiers.SALVE_AMULET) && target != null && target.isNpc() && includeNpcMax) {
            otherBonusMultiplier += 0.15;
        }

        if ((player.getEquipment().contains(ItemIdentifiers.SALVE_AMULETI) || player.getEquipment().contains(SALVE_AMULET_E) || player.getEquipment().contains(ItemIdentifiers.SALVE_AMULETEI)) && target != null && target.isNpc() && includeNpcMax) {
            otherBonusMultiplier += 0.2;
        }

        if (player.getEquipment().hasAt(EquipSlot.HEAD, INQUISITORS_GREAT_HELM) || player.getEquipment().hasAt(EquipSlot.BODY, INQUISITORS_HAUBERK) || player.getEquipment().hasAt(EquipSlot.LEGS, INQUISITORS_PLATESKIRT)) {
            otherBonusMultiplier += 0.02;//2% damage boost
        }

        if (player.hasPetOut("Skeleton hellhound pet")) {
            otherBonusMultiplier += 0.05;
        }

        //Custom effect not from OSRS, OSRS is 2.5% this is 5%
        if (player.getCombat().getFightType().getAttackType() == AttackType.CRUSH) {
            if (player.getEquipment().hasAt(EquipSlot.HEAD, INQUISITORS_GREAT_HELM)) {
                otherBonusMultiplier += 0.01;//1.0% damage boost
            }

            if (player.getEquipment().hasAt(EquipSlot.BODY, INQUISITORS_HAUBERK)) {
                otherBonusMultiplier += 0.01;//1.0% damage boost
            }

            if (player.getEquipment().hasAt(EquipSlot.LEGS, INQUISITORS_PLATESKIRT)) {
                otherBonusMultiplier += 0.01;//1.0% damage boost
            }

            if (player.getEquipment().hasAt(EquipSlot.HEAD, INQUISITORS_GREAT_HELM) || player.getEquipment().hasAt(EquipSlot.BODY, INQUISITORS_HAUBERK) || player.getEquipment().hasAt(EquipSlot.LEGS, INQUISITORS_PLATESKIRT)) {
                otherBonusMultiplier += 0.02;//2% damage boost
            }
        }

        if (target != null && target.isNpc() && includeNpcMax) {
            //VS combat dummy show regardless
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                if(player.getEquipment().wearingSlayerHelm()) {
                    otherBonusMultiplier *= 1.15;
                }
            }

            if(player.getEquipment().wearingSlayerHelm() && target.isNpc() && Slayer.creatureMatches(player, target.getAsNpc().id())) {
                otherBonusMultiplier *= 1.15;
            }
        }

        if (target != null && target.isNpc() && includeNpcMax) {
            if(target.isNpc() && player.tile().memberCave()) {
                otherBonusMultiplier *= 1.10;
            }
        }

        var weakSpot = player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.WEAK_SPOT);

        if(weakSpot && target != null && target.isNpc()) {
            if(Slayer.creatureMatches(player, target.getAsNpc().id())) {
                otherBonusMultiplier *= 1.10;
            }
        }

        int effectiveStrengthDamage = (int) ((strengthLevel * prayerMultiplier * otherBonusMultiplier) + combatStyleBonus);
        double baseDamage = 1.3 + (effectiveStrengthDamage / 10d) + (bonuses.str / 80d) + ((effectiveStrengthDamage * bonuses.str) / 640d);

        //System.out.println("basedmg "+baseDamage);

        // Special effects also affect maxhit
        if (special) {
            specialMultiplier = player.getCombatSpecial().getSpecialMultiplier();
            //System.out.println("special modifier: " + specialMultiplier + " received " + player.getCombatSpecial().getSpecialMultiplier());
        }

        maxHit = (int) (Math.floor(baseDamage) * specialMultiplier);

        List<Integer> increaseMaxHitbyOne = new ArrayList<>(List.of(GRANITE_MAUL_12848, ARMADYL_GODSWORD_OR, ANCIENT_GODSWORD, BANDOS_GODSWORD_OR, SARADOMIN_GODSWORD_OR, ZAMORAK_GODSWORD_OR, DRAGON_CLAWS_OR));

        if (increaseMaxHitbyOne.stream().anyMatch(w -> player.getEquipment().hasAt(EquipSlot.WEAPON, w))) {
            maxHit += 1;
        }

        //Calculate pet bonus
        if(player.hasPetOut("Baby Barrelchest") || player.hasPetOut("Ancient barrelchest")) {
            maxHit += 1;
        }

        if(player.hasPetOut("Corrupted nechryarch pet")) {
            maxHit += 2;
        }

        if(player.hasPetOut("Youngllef pet")) {
            maxHit += 1;
        }

        if(player.hasPetOut("Corrupted Youngllef pet")) {
            maxHit += 2;
        }

        if(player.getEquipment().hasAt(EquipSlot.AMULET, AMULET_OF_TORTURE_OR) || player.getEquipment().hasAt(EquipSlot.AMULET, AMULET_OF_FURY_OR) || player.getEquipment().hasAt(EquipSlot.AMULET, BERSERKER_NECKLACE_OR)) {
            maxHit += 1;
        }

        // Dharoks effect
        if (CombatFactory.fullDharoks(player)) {
            int hitpoints = player.hp();
            double max = player.maxHp();
            double mult = Math.max(0, ((max - (double) hitpoints) / max) * 100d) + 100d;
            maxHit *= (mult / 100);
        }

        //System.out.println("maxhit: "+maxHit);

        return maxHit;
    }

    public static int maximumRangedHit(Mob mob, Mob target, boolean special, boolean factorInAmmoRangeStr) {
        return maximumRangedHit(mob, target, special, factorInAmmoRangeStr,true);
    }

    public static int maximumRangedHit(Mob mob, Mob target, boolean special, boolean factorInAmmoRangeStr, boolean includeNpcMax) {
        int maxHit;
        Player player = (Player) mob;

        EquipmentInfo.Bonuses bonuses = EquipmentInfo.totalBonuses(player, World.getWorld().equipmentInfo(), !factorInAmmoRangeStr);

        double prayerBonus = 1;
        double extraBonus = 1;

        // Prayer additions
        if (DefaultPrayers.usingPrayer(player, DefaultPrayers.SHARP_EYE)) {
            prayerBonus = 1.05;
        } else if (DefaultPrayers.usingPrayer(player, DefaultPrayers.HAWK_EYE)) {
            prayerBonus = 1.10;
        } else if (DefaultPrayers.usingPrayer(player, DefaultPrayers.EAGLE_EYE)) {
            prayerBonus = 1.15;
        } else if (DefaultPrayers.usingPrayer(player, DefaultPrayers.RIGOUR)) {
            prayerBonus = 1.23;
        }

        Item weapon = player.getEquipment().get(EquipSlot.WEAPON);
        Item helm = player.getEquipment().get(EquipSlot.HEAD);
        Item amulet = player.getEquipment().get(EquipSlot.AMULET);

        boolean special_slayer_helmet_i = helm != null && (helm.getId() == RED_SLAYER_HELMET_I || helm.getId() == TWISTED_SLAYER_HELMET_I || helm.getId() == TWISTED_SLAYER_HELMET_I_KBD_HEADS|| helm.getId() == TWISTED_SLAYER_HELMET_I_CORP_HEART || helm.getId() == TWISTED_SLAYER_HELMET_I_JAD || helm.getId() == TWISTED_SLAYER_HELMET_I_INFERNAL_CAPE || helm.getId() == TWISTED_SLAYER_HELMET_I_VAMP_DUST || helm.getId() == PURPLE_SLAYER_HELMET_I || helm.getId() == HYDRA_SLAYER_HELMET_I);
        int[] black_mask_i = {ItemIdentifiers.BLACK_MASK_I, ItemIdentifiers.SLAYER_HELMET_I, ItemIdentifiers.BLACK_SLAYER_HELMET_I, RED_SLAYER_HELMET_I, TWISTED_SLAYER_HELMET_I, TWISTED_SLAYER_HELMET_I_KBD_HEADS, PURPLE_SLAYER_HELMET_I, HYDRA_SLAYER_HELMET_I, GREEN_SLAYER_HELMET_I,TWISTED_SLAYER_HELMET_I_CORP_HEART, TWISTED_SLAYER_HELMET_I_JAD, TWISTED_SLAYER_HELMET_I_INFERNAL_CAPE, TWISTED_SLAYER_HELMET_I_VAMP_DUST, PURPLE_SLAYER_HELMET_I, HYDRA_SLAYER_HELMET_I};

        //Readability
        boolean wearing_black_mask = helm != null && Arrays.stream(black_mask_i).anyMatch(mask_id -> mask_id == helm.getId());

        // The magic shortbow spec ignores bonus like prayers, void etc...
        boolean ignoreMSBBonus = weapon != null && (weapon.getId() == ItemIdentifiers.MAGIC_SHORTBOW || weapon.getId() == ItemIdentifiers.MAGIC_SHORTBOW_I && special);

        int effectiveLevel = ignoreMSBBonus ? player.skills().level(Skills.RANGED) : (int) Math.floor(player.skills().level(Skills.RANGED) * prayerBonus * extraBonus);

        // Accurate mode gives you 3 extra levels in the algorithm.
        if (player.getCombat().getFightType().getStyle().equals(FightStyle.ACCURATE)) {
            effectiveLevel += 3;
        }

        effectiveLevel += 8;

        // Void effect adds 10%.
        if (voidRanger(player)) {
            effectiveLevel += effectiveLevel / 10;
        }

        // Elite Void effect adds extra 2.5%.
        if (wearingEliteVoid(player) || voidCustomRanger(player)) {
            effectiveLevel += effectiveLevel * 0.025;
        }

        //Onl apply to npc combat
        if(target != null && target.isNpc() && includeNpcMax) {
             if (special_slayer_helmet_i) {
                effectiveLevel += effectiveLevel / 10; //+10%
            }

            if (player.getEquipment().contains(ItemIdentifiers.SALVE_AMULET)) {
                effectiveLevel += effectiveLevel / 15;
            }

            if (player.getEquipment().contains(ItemIdentifiers.SALVE_AMULETI) || player.getEquipment().contains(SALVE_AMULET_E) || player.getEquipment().contains(ItemIdentifiers.SALVE_AMULETEI)) {
                effectiveLevel += effectiveLevel / 20;
            }
        }

        int baseDamage = (int) (0.5 + effectiveLevel * (bonuses.rangestr + 64d) / 640);
        var off_additional_bonus = 1.0;

        if(player.hasPetOut("Baby Aragog")) {
            var percentage = target != null && target.isNpc() && includeNpcMax ? 0.10 : 0.05;
            off_additional_bonus += percentage;
        }

        if(player.hasPetOut("Olmlet") && target != null && target.isNpc()) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY && includeNpcMax) {
                off_additional_bonus += 0.10;
            }
            if (player.isInsideRaids())
                off_additional_bonus += 0.10;
        }

        if (player.hasPetOut("Skeleton hellhound pet")) {
            off_additional_bonus += 0.05;
        }

        var crystalArmourMultiplier = 1.0;

        if (player.getEquipment().hasAt(EquipSlot.HEAD, CRYSTAL_HELM)) {
            crystalArmourMultiplier += 0.025;//2.5% damage boost
        }

        if (player.getEquipment().hasAt(EquipSlot.BODY, CRYSTAL_BODY)) {
            crystalArmourMultiplier += 0.075;//7.5% damage boost
        }

        if (player.getEquipment().hasAt(EquipSlot.LEGS, CRYSTAL_LEGS)) {
            crystalArmourMultiplier += 0.05;//5.0% damage boost
        }

        // Append the Twisted bow computation, if we have enough data..
        if (weapon != null && (weapon.getId() == TWISTED_BOW || weapon.getId() == TWISTED_BOW_I) && target != null && target.isNpc() && includeNpcMax) {
            int magicLevel = 0;

            if (((Npc) target).combatInfo() != null && ((Npc) target).combatInfo().stats != null)
                magicLevel = ((Npc) target).combatInfo().stats.magic;

            double damage = 265D + ((3 * magicLevel - 14D) / 100D) - (Math.pow(3 * magicLevel / 10.0 - 140.0, 2) / 100D);
            damage = Math.min(265D, damage);
            baseDamage *= Math.min(2D, 1D + damage);
            return baseDamage;
        }

        // Append the Twisted bow computation, if we have enough data..
        if (weapon != null && weapon.getId() == SANGUINE_TWISTED_BOW && target != null && target.isNpc() && includeNpcMax) {
            int magicLevel = 0;

            if (((Npc) target).combatInfo() != null && ((Npc) target).combatInfo().stats != null)
                magicLevel = ((Npc) target).combatInfo().stats.magic;

            double damage = 280D + ((3 * magicLevel - 14D) / 100D) - (Math.pow(3 * magicLevel / 10.0 - 140.0, 2) / 100D);
            damage = Math.min(280, damage);
            baseDamage *= Math.min(2D, 1D + damage);
            return baseDamage;
        }

        //Dragon hunter crossbow
        if (weapon != null && (weapon.getId() == DRAGON_HUNTER_CROSSBOW || weapon.getId() == DRAGON_HUNTER_CROSSBOW_T) && target != null && target.isNpc() && includeNpcMax) {
            if (((Npc) target).def() != null && ((Npc) target).def().name != null && CombatFormula.isDragon(((Npc) target).def().name))
                off_additional_bonus += 0.30;
        }

        var weakSpot = player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.WEAK_SPOT);

        if(weakSpot && target != null && target.isNpc()) {
            if(Slayer.creatureMatches(player, target.getAsNpc().id())) {
                off_additional_bonus += 0.10;
            }
        }

        //Craws Bow
        if (hasCrawsBow(player) && target != null && target.isNpc() && includeNpcMax) {
            off_additional_bonus += 0.50;
        }

        if (player.getEquipment().hasAt(EquipSlot.WEAPON, CRAWS_BOW_C) && target != null && target.isNpc() && includeNpcMax) {
            off_additional_bonus += 0.50;
        }

        double specialMultiplier = 1;

        if (special) {
            specialMultiplier = player.getCombatSpecial().getSpecialMultiplier();
        }

        //System.out.println("Regular hit "+baseDamage+" times specialMultiplier "+specialMultiplier);

        maxHit = (int) Math.floor(baseDamage * off_additional_bonus * crystalArmourMultiplier * specialMultiplier);

        if(player.hasPetOut("Little Nightmare")) {
            maxHit += 1;
        }

        if(player.getEquipment().hasAt(EquipSlot.AMULET, NECKLACE_OF_ANGUISH_OR)) {
            maxHit += 1;
        }

        if(player.getEquipment().hasAt(EquipSlot.WEAPON, MAGMA_BLOWPIPE)) {
            maxHit += 3;
        }

        if(player.hasPetOut("Youngllef pet")) {
            maxHit += 1;
        }

        if(player.hasPetOut("Corrupted Youngllef pet")) {
            maxHit += 2;
        }

        //System.out.printf("range max hit %d %n", maxHit);
        return maxHit;
    }

    public static int modifyMagicDamage(Player player, int spell_maxhit, String spell_name) {
        return modifyMagicDamage(player, spell_maxhit, spell_name,true);
    }

    public static int modifyMagicDamage(Player player, int spell_maxhit, String spell_name, boolean includeNpcMax) {
        EquipmentInfo.Bonuses b = EquipmentInfo.totalBonuses(player, World.getWorld().equipmentInfo());
        double multi = 1 + ((b.magestr > 0 ? b.magestr : 1.0) / 100);
        boolean hasTomeOfFire = player.getEquipment().hasAt(EquipSlot.SHIELD, TOME_OF_FIRE);
        Mob target = player.getCombat().getTarget();

        //Custom calculations
        if(spell_name.equals("Volatile spell")) {
            int baseLevel = player.skills().xpLevel(Skills.MAGIC);
            if(baseLevel > 99) //Magic potion doesn't increase max hit when already over 99 magic
                baseLevel = 99;

            //The base damage for Immolate is scaled based on the player's Magic level, ranging from 50 at level 75 to 66 at level 99
            double levelTimes = 0.67;
            spell_maxhit = (int) (baseLevel * levelTimes);//The base max hit
            multi -= 0.15;//Don't calculate the staff multiplier in the base hit
        }

        if(player.hasPetOut("Baby lava dragon pet")) {
            var percentage = target != null && target.isNpc() && includeNpcMax ? 1.10 : 1.05;
            spell_maxhit *= percentage;
        }

        if(player.hasPetOut("Mini necromancer pet")) {
            var increaseBy = target != null && target.isNpc() && includeNpcMax ? 5 : 1;
            spell_maxhit += increaseBy;
        }

        if (spell_name.toLowerCase().contains("fire") && hasTomeOfFire) {
            spell_maxhit *= 1.50;
        }

        if (spell_name.equals("Saradomin Strike") || spell_name.equals("Claws of Guthix") || spell_name.equals("Flames of Zamorak")) {
            if (player.getTimers().has(TimerKey.CHARGE_SPELL)) {
                spell_maxhit = 30;
            }
        }

        int weapon = player.getEquipment().get(3) == null ? -1 : player.getEquipment().get(3).getId();

        if(spell_name.equals("Sanguinesti spell")) {
            boolean holy_staff = weapon == HOLY_SANGUINESTI_STAFF;
            if(holy_staff) {
                spell_maxhit += 10;
            }
        }

        // 10% of level
        if (spell_name.equals("Magic dart")) {
            boolean staff_e = weapon == 21255;
            if (staff_e) { // Slayer's staff (e)
                spell_maxhit = 13;
            }
            spell_maxhit += (player.skills().level(Skills.MAGIC) / (staff_e ? 16.6 : 10.0));
        }

        // Ammy of damned, 25% chance to deal 30% extra damage! Must be hardcoded
        if (Equipment.hasAmmyOfDamned(player) && Equipment.fullAhrim(player) && World.getWorld().rollDie(100, 25)) {
            multi += 0.30;
        }

        if(player.hasPetOut("Olmlet") && target != null && target.isNpc() && includeNpcMax) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                multi += 0.10;
            }
            if (player.isInsideRaids())
                multi += 0.10;
        }

        if (player.hasPetOut("Skeleton hellhound pet")) {
            multi += 0.05;
        }

        //Thammaron Sceptre
        if (CombatFormula.hasThammaronSceptre(player) && target != null && target.isNpc() && includeNpcMax) {
            multi += 0.25;
        }

        if (player.getEquipment().hasAt(EquipSlot.WEAPON, THAMMARONS_STAFF_C) && target != null && target.isNpc() && includeNpcMax) {
            multi += 0.25;
        }

        var weakSpot = player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.WEAK_SPOT);

        if(weakSpot && target != null && target.isNpc()) {
            if(Slayer.creatureMatches(player, target.getAsNpc().id())) {
                multi += 0.10;
            }
        }

        // Occult necklace gives 10% bonus and Staff of the Death (+Toxic) gives 15% damage boost - done via equip bonuses on OSRS

        if (player.getEquipment().containsAny(11998, 12000)) { // Smoke staffs provide 10% extra dmg + accuracy
            multi += 0.10;
        } else if (player.getEquipment().hasAt(EquipSlot.WEAPON, 12899) && spell_name.contains("trident")) {
            // Toxic trident base damage is +3
            spell_maxhit += 3;
        }

        // Slayer helm and corp adjustments.
        multi *= multi(player, ((WeakReference<Mob>) player.getAttribOr(AttributeKey.TARGET, new WeakReference<Mob>(null))).get(), CombatType.MAGIC, false);

        System.out.println(spell_maxhit);
        System.out.println(multi);
        int maxHit = (int) (spell_maxhit * multi);

        if(player.getEquipment().hasAt(EquipSlot.AMULET, OCCULT_NECKLACE_OR) || player.getEquipment().hasAt(EquipSlot.HANDS, TORMENTED_BRACELET_OR)) {
            maxHit += 1;
        }

        if(player.hasPetOut("Youngllef pet")) {
            maxHit += 1;
        }

        if(player.hasPetOut("Corrupted Youngllef pet")) {
            maxHit += 2;
        }

        if(player.hasPetOut("Little Nightmare")) {
            maxHit += 1;
        }

        if(spell_name.equals("Petrificus Totalus") && target != null && target.isPlayer()) {
            maxHit = 40;
        }

        if(spell_name.equals("Cruciatus Curse") && target != null && target.isPlayer()) {
            maxHit = 41;
        }

        if(spell_name.equals("Expelliarmus") && target != null && target.isPlayer()) {
            maxHit = 50;
        }

        if(spell_name.equals("Sectumsempra") && target != null && target.isPlayer()) {
            maxHit = 55;
        }

        if(spell_name.equals("Avada Kedavra") && target != null && target.isPlayer()) {
            maxHit = 82;
        }

        /*if(spell_name.equals("Volatile spell")) {
            maxHit = 80;
        }*/
        //System.out.println("magic max hit: "+maxHit+" multi: "+multi+" spell maxhit: "+spell_maxhit);
        return maxHit;
    }

    /**
     * Calculates a Multipler to adjust our max hit. Considers the Slayer helm, your Slayer Target,
     * the Corp Beast (50% dmg reduction vs non-Spear weapons) etc.
     */
    private static double multi(Player player, Mob target, CombatType style, boolean ignoreCorp) {
        var mult = 1.0;
        if (target != null) {
            if (Equipment.targetIsSlayerTask(player, target)) {
                var headItem = player.getEquipment().get(EquipSlot.HEAD);
                // Black mask (0) and (10) and normal slayer helm.
                if (headItem != null) {
                    if (Arrays.asList(BLACK_MASK_10, BLACK_MASK).contains(headItem.getId()) || headItem.getId() == SLAYER_HELMET || headItem.getId() == RED_SLAYER_HELMET || headItem.getId() == GREEN_SLAYER_HELMET || headItem.getId() == BLACK_SLAYER_HELMET || headItem.getId() == PURPLE_SLAYER_HELMET || headItem.getId() == TURQUOISE_SLAYER_HELMET || headItem.getId() == HYDRA_SLAYER_HELMET_I || headItem.getId() == TWISTED_SLAYER_HELMET) { // black mask / normal slayer helm
                        if (style == CombatType.MELEE) {
                            mult += 0.166; // 16.6%
                        }
                        // mask/normal helm do not give range/mage bonuses
                    } else if (Arrays.asList(BLACK_MASK_10_I, BLACK_MASK_I).contains(headItem.getId())) { // black mask (i) gives 15% mage/range
                        if (style == CombatType.MELEE) {
                            mult += 0.166; // 16.6%
                        } else {
                            mult += 0.15; // other 2 styles are only 15% (ash confirmed on twitter)
                        }
                    } else if (Arrays.asList(SLAYER_HELMET_I, BLACK_SLAYER_HELMET_I, GREEN_SLAYER_HELMET_I, RED_SLAYER_HELMET_I, PURPLE_SLAYER_HELMET_I, TURQUOISE_SLAYER_HELMET_I, HYDRA_SLAYER_HELMET_I, TWISTED_SLAYER_HELMET_I, TWISTED_SLAYER_HELMET_I_KBD_HEADS,TWISTED_SLAYER_HELMET_I_CORP_HEART, TWISTED_SLAYER_HELMET_I_JAD, TWISTED_SLAYER_HELMET_I_INFERNAL_CAPE, TWISTED_SLAYER_HELMET_I_VAMP_DUST, PURPLE_SLAYER_HELMET_I, HYDRA_SLAYER_HELMET_I).contains(headItem.getId())) { // helms (i)
                        if (style != CombatType.MELEE) {
                            mult += 0.3; // 15% (normal helm) + 15% (for the imbue) totalling 30% bonus for magic + range damage
                        } else {
                            mult += 0.166; // the normal 16.6%
                        }
                    }
                }
            }
            if (target.isNpc()) {
                var npc = target.getAsNpc();

                //If we're attacking the Abyssal Sire's Respiratory system and it's not disoriented, reduce damage by 95%..
                if (npc.id() == 5914) {
                    var combatState = npc.<AbyssalSireState>getAttribOr(AttributeKey.ABYSSAL_SIRE_STATE, AbyssalSireState.STASIS);
                    if (combatState == AbyssalSireState.COMBAT) {
                        if (World.getWorld().rollDie(2, 1))
                            player.message("You can't deal much damage with those tentacles getting in the way.");
                        mult -= 0.9;
                    }
                }
            }
        }
        return mult;
    }
}
