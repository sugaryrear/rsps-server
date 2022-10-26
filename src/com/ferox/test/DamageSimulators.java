package com.ferox.test;

import com.ferox.GameServer;
import com.ferox.game.content.presets.PresetManager;
import com.ferox.game.content.skill.impl.slayer.Slayer;
import com.ferox.game.content.tournaments.Tournament;
import com.ferox.game.content.tournaments.TournamentManager;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatSpecial;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.formula.CombatFormula;
import com.ferox.game.world.entity.combat.magic.CombatSpell;
import com.ferox.game.world.entity.combat.magic.CombatSpells;
import com.ferox.game.world.entity.combat.method.CombatMethod;
import com.ferox.game.world.entity.combat.prayer.default_prayer.DefaultPrayerData;
import com.ferox.game.world.entity.combat.prayer.default_prayer.DefaultPrayers;
import com.ferox.game.world.entity.combat.weapon.AttackType;
import com.ferox.game.world.entity.combat.weapon.FightStyle;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.entity.mob.player.commands.CommandManager;
import com.ferox.game.world.entity.mob.player.rights.PlayerRights;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.container.equipment.Equipment;
import com.ferox.game.world.items.container.equipment.EquipmentInfo;
import com.ferox.net.PlayerSession;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.Utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.ferox.util.ItemIdentifiers.*;
import static com.ferox.util.NpcIdentifiers.LAVA_DRAGON;

/**
 * Example output:
 * <br>
 * <br>damage map is:
 * <br>DMG		OCCURANCE
 * <br>0			18 times (1.7999999999999998%)
 * <br>1			22 times (2.1999999999999997%)
 * <br>2			20 times (2.0%)
 * <br>
 * <br>
 * @author Shadowrs tardisfan121@gmail.com
 */
public class DamageSimulators {

    public static class HitSim {

        public ArrayList<Integer> hits = new ArrayList<>();

        /**
         * a map of damage and how many times that damage value appeared:
         * 1 : 100
         * 2 : 100 (hit 2 dmg 100 times)
         * 30 : 10 (hit 30 dmg 10 times)
         */
        public HashMap<Integer, Integer> commonality() {
            HashMap<Integer, Integer> integers = new HashMap();
            hits.forEach(i -> {
                integers.compute(i, (k, v) -> (v == null) ? 1 : v + 1);
            });
            return integers;
        }

        public String printAnalysis() {
            // print analysis

            Stream<Map.Entry<Integer, Integer>> sort = commonality().entrySet().stream().sorted(new Comparator<Map.Entry<Integer, Integer>>() {
                @Override
                public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                    return Integer.compare(o1.getKey(), o2.getKey());
                }
            });
            String data = Arrays.toString(sort.map(e -> e.getKey()+"\t\t\t"+e.getValue()+" times ("+((double)e.getValue() / (double)hits.size() * 100.0)+"%)\n").toArray());
            data = data.substring(1, data.length() - 2).replace(", ", "");// remove arrays.tostring wrappers
            String out = String.format("damage map is:%n" +
                "DMG\t\tOCCURANCE%n" +
                "%s" +
                "%n", data);
           // System.out.println(out);
            return out;
        }
    }

    public static int blyat = 1;

    public static void main(String[] args) {
        GameServer.properties().gamePort = 39999;
        GameServer.main(new String[]{});

        // run sims
        runMaxMeleeDhPeitySim();
        runMaxMeleeDhNoPraySim();
        nakedMaxMainsBoxPeity();
        nakedMaxMainsBoxNoPrayer();
        nakedBoxMaxMainsWith1def();
        runMaxMeleeDhTurmoil();
        runRangePresetSim();
        runMagicPresetSim();

        System.exit(0);
    }

    public static Player makebot() {
        Player player = new Player(new PlayerSession(null));
        player.setUsername("bot").setLongUsername(Utils.stringToLong("bot")).setHostAddress("127.0.0.1");
        player.putAttrib(AttributeKey.MAC_ADDRESS,"OMEGALUL");
        player.onLogin();
        player.setIndex(1); // presets need to be fuckin registered user
        player.setPlayerRights(PlayerRights.DEVELOPER);
        return player;
    }

    private static void runMagicPresetSim() {
        Player p1 = makebot();
        Player p2 = makebot();

        //setup p1
        CommandManager.commands.get("master").execute(p1, "master", null);

        // setup p2
        CommandManager.commands.get("master").execute(p2, "master", null);

        p1.getPresetManager().load(PresetManager.GLOBAL_PRESETS[2]); // tribird
        p2.getPresetManager().load(PresetManager.GLOBAL_PRESETS[2]); // tribird

        assert p1.skills().level(5) == 99; // 99 prayer
        assert p1.getEquipment().get(EquipSlot.WEAPON).matchesId(4675); // staff

        DefaultPrayers.togglePrayer(p1, DefaultPrayerData.MYSTIC_MIGHT.getButtonId());
        DefaultPrayers.togglePrayer(p2, DefaultPrayerData.MYSTIC_MIGHT.getButtonId());
        assert p1.getPrayerActive()[DefaultPrayerData.MYSTIC_MIGHT.ordinal()]; // activated

        applyMagicSim(p1, p2, "runMagicPresetSim", 94, CombatSpells.ICE_BARRAGE.getSpell());
    }

    private static void runRangePresetSim() {
        Player p1 = makebot();
        Player p2 = makebot();

        //setup p1
        CommandManager.commands.get("master").execute(p1, "master", null);

        // setup p2
        CommandManager.commands.get("master").execute(p2, "master", null);

        p1.getPresetManager().load(PresetManager.GLOBAL_PRESETS[2]); // tribird
        p2.getPresetManager().load(PresetManager.GLOBAL_PRESETS[2]); // tribird
        p1.inventory().remove(385, 10);
        p2.inventory().remove(385, 10);
        p1.getEquipment().manualWear(new Item(9185), true);
        p1.getEquipment().manualWear(new Item(2503), true);

        assert p1.skills().level(5) == 99; // 99 prayer
        assert p1.getEquipment().get(EquipSlot.WEAPON) != null && p1.getEquipment().get(EquipSlot.WEAPON).matchesId(9185) : "what the fuck"; // bow
        System.out.println("range wep "+p1.getEquipment().get(3));

        DefaultPrayers.togglePrayer(p1, DefaultPrayerData.EAGLE_EYE.getButtonId());
        DefaultPrayers.togglePrayer(p2, DefaultPrayerData.EAGLE_EYE.getButtonId());
        assert p1.getPrayerActive()[DefaultPrayerData.EAGLE_EYE.ordinal()]; // activated

        applyRangeSim(p1, p2, "runRangePresetSim");
    }

    private static void runMaxMeleeDhPeitySim() {
        Player p1 = makebot();
        Player p2 = makebot();

        //setup p1
        CommandManager.commands.get("master").execute(p1, "master", null);

        // setup p2
        CommandManager.commands.get("master").execute(p2, "master", null);

        Tournament dh = new Tournament(Arrays.stream(TournamentManager.settings.getTornConfigs()).filter(c -> c.key.equalsIgnoreCase("dharok")).findFirst().get());
        dh.setLoadoutOnPlayer(p1);
        dh.setLoadoutOnPlayer(p2);

        assert p2.skills().level(5) == 99; // 99 prayer
        assert p2.getEquipment().get(EquipSlot.WEAPON).matchesId(12006); // tent

        DefaultPrayers.togglePrayer(p1, DefaultPrayerData.PIETY.getButtonId());
        DefaultPrayers.togglePrayer(p2, DefaultPrayerData.PIETY.getButtonId());
        assert p1.getPrayerActive()[DefaultPrayerData.PIETY.ordinal()]; // activated

        applyMeleeSim(p1, p2, "runMaxMeleeDhPeitySim");
    }


    private static void runMaxMeleeDhTurmoil() {
        Player p1 = makebot();
        Player p2 = makebot();

        //setup p1
        CommandManager.commands.get("master").execute(p1, "master", null);

        // setup p2
        CommandManager.commands.get("master").execute(p2, "master", null);

        Tournament dh = new Tournament(Arrays.stream(TournamentManager.settings.getTornConfigs()).filter(c -> c.key.equalsIgnoreCase("dharok")).findFirst().get());
        dh.setLoadoutOnPlayer(p1);
        dh.setLoadoutOnPlayer(p2);

        assert p2.skills().level(5) == 99; // 99 prayer
        assert p2.getEquipment().get(EquipSlot.WEAPON).matchesId(12006); // tent

        applyMeleeSim(p1, p2, "runMaxMeleeDhTurmoil");
    }

    private static void runMaxMeleeDhNoPraySim() {
        Player p1 = makebot();
        Player p2 = makebot();

        Tournament dh = new Tournament(Arrays.stream(TournamentManager.settings.getTornConfigs()).filter(c -> c.key.equalsIgnoreCase("dharok")).findFirst().get());
        dh.setLoadoutOnPlayer(p1);
        dh.setLoadoutOnPlayer(p2);

        assert p2.skills().level(5) == 99; // 99 prayer
        assert p2.getEquipment().get(EquipSlot.WEAPON).matchesId(12006); // tent

        assert !p1.getPrayerActive()[DefaultPrayerData.PIETY.ordinal()];

        applyMeleeSim(p1, p2, "runMaxMeleeDhNoPraySim");
    }

    private static void nakedMaxMainsBoxPeity() {
        Player p1 = makebot();
        Player p2 = makebot();

        //setup p1
        CommandManager.commands.get("master").execute(p1, "master", null);

        // setup p2
        CommandManager.commands.get("master").execute(p2, "master", null);

        assert p2.skills().level(5) == 99; // 99 prayer
        assert p2.getEquipment().get(EquipSlot.WEAPON) == null; // tent

        DefaultPrayers.togglePrayer(p1, DefaultPrayerData.PIETY.getButtonId());
        DefaultPrayers.togglePrayer(p2, DefaultPrayerData.PIETY.getButtonId());
        assert p1.getPrayerActive()[DefaultPrayerData.PIETY.ordinal()]; // activated

        applyMeleeSim(p1, p2, "nakedMaxMainsBoxPeity");
    }
    private static void nakedMaxMainsBoxNoPrayer() {
        Player p1 = makebot();
        Player p2 = makebot();

        //setup p1
        CommandManager.commands.get("master").execute(p1, "master", null);

        // setup p2
        CommandManager.commands.get("master").execute(p2, "master", null);

        assert p2.skills().level(5) == 99; // 99 prayer
        assert p2.getEquipment().get(EquipSlot.WEAPON) == null; // tent

        applyMeleeSim(p1, p2, "nakedMaxMainsBoxNoPrayer");
    }

    private static void nakedBoxMaxMainsWith1def() {
        Player p1 = makebot();
        Player p2 = makebot();

        //setup p1
        CommandManager.commands.get("master").execute(p1, "master", "master".split(" "));

        // setup p2
        CommandManager.commands.get("master").execute(p2, "master", "master".split(" "));

        assert p2.skills().level(5) == 99; // 99 prayer
        assert p2.getEquipment().get(EquipSlot.WEAPON) == null; // tent

        CommandManager.commands.get("setlevel").execute(p1, "setlevel 5 1", "setlevel 5 1".split(" "));
        CommandManager.commands.get("setlevel").execute(p2, "setlevel 5 1", "setlevel 5 1".split(" "));

        assert p2.skills().level(1) == 1; // 1 def

        applyMeleeSim(p1, p2, "nakedBoxMaxMainsWith1def");
    }

    private static void applyMeleeSim(Player p1, Player p2, String setupname) {
        applySim(p1, p2, setupname, CombatFactory.MELEE_COMBAT, CombatType.MELEE);
    }
    private static void applyMagicSim(Player p1, Player p2, String setupname, int spell, CombatSpell com) {
        applySim(p1, p2, setupname, CombatFactory.MAGIC_COMBAT, CombatType.MAGIC, spell, com);
    }
    private static void applyRangeSim(Player p1, Player p2, String setupname) {
        applySim(p1, p2, setupname, CombatFactory.RANGED_COMBAT, CombatType.RANGED);
    }

    private static void applySim(Player p1, Player p2, String setupname, CombatMethod mtd, CombatType type) {
        applySim(p1, p2, setupname, mtd, type, 1, null);
    }
    private static void applySim(Player p1, Player p2, String setupname, CombatMethod mtd, CombatType type, int spell, CombatSpell combatSpell) {
        CombatFactory.getMethod(p1);
        CombatFactory.getMethod(p2);
        // sim and record
        HitSim hitSim = new HitSim();
        int simCount = 1_00;
        CombatMethod method = mtd;
        Mob mob = p1;
        Mob target = p2;
        p1.getCombat().setCastSpell(combatSpell);
        String acc = doesHit(p1, target, type, spell, 1.0); // print to console for now
        for (int i = 0; i < simCount; i++) {
            p1.getCombat().setCastSpell(combatSpell);

            /**
             * from {@link com.ferox.game.world.entity.combat.Combat#performNewAttack(boolean)}
             */
            method.prepareAttack(mob, target);
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
        }

        String breakdown = hitSim.printAnalysis();
        String out =
            "\n"+setupname +
            "\np1 equip: "+Arrays.toString(p1.getEquipment().getValidItems().stream().map(i -> i.name()).toArray()) +
                "\np2 equip: "+Arrays.toString(p2.getEquipment().getValidItems().stream().map(i -> i.name()).toArray())+
            "\n" + acc +
            "\n"+breakdown;
        try {
            Files.write(Paths.get("combat-sims.txt"), "\n".getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
            Files.write(Paths.get("combat-sims.txt"), out.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final SecureRandom srand = new SecureRandom();

    public static String doesHit(Mob entity, Mob enemy, CombatType type, int spellLevel, double multiplier) {
        StringBuilder sb = new StringBuilder();
        EquipmentInfo.Bonuses playerBonuses = EquipmentInfo.totalBonuses(entity, World.getWorld().equipmentInfo());
        EquipmentInfo.Bonuses targetBonuses = EquipmentInfo.totalBonuses(enemy, World.getWorld().equipmentInfo());
        /*
            S E T T I N G S

            S T A R T
        */

        //attack stances
        int off_stance_bonus = 0;
        int def_stance_bonus = 0;
        if (entity.isPlayer()) {
            final FightStyle attackStyle = entity.getCombat().getFightType().getStyle();
            off_stance_bonus = attackStyle.equals(FightStyle.ACCURATE) ? 3 : attackStyle.equals(FightStyle.CONTROLLED) ? 1 : 0; //accurate, aggressive, controlled, defensive
            def_stance_bonus = attackStyle.equals(FightStyle.DEFENSIVE) ? 3 : attackStyle.equals(FightStyle.CONTROLLED) ? 1 : 0; //accurate, aggressive, controlled, defensive
        }

        //requirements
        int off_weapon_requirement = 1; //weapon attack level requirement
        int off_spell_requirement = spellLevel; //spell magic level requirement

        //base levels
        int off_base_attack_level = 1;
        int off_base_ranged_level = 1;
        int off_base_magic_level = 1;
        int attackerWeaponId = -1;
        double twistedBowMultiplier = 1.0; // Defaults to no change (x1)
        double other_bonuses = 1.0;

        if (entity.isPlayer()) {
            Player player = ((Player) entity);
            final Item weapon = player.getEquipment().get(EquipSlot.WEAPON);

            if (weapon != null) {
                attackerWeaponId = weapon.getId(); // Used below in Twisted bow computation.

                final Map<Integer, Integer> requiredLevels = World.getWorld().equipmentInfo().requirementsFor(weapon.getId());
                if (requiredLevels != null) {
                    final Integer requiredLevel = requiredLevels.get(Skills.ATTACK);
                    if (requiredLevel != null)
                        off_weapon_requirement = requiredLevel;
                }
            }

            off_base_attack_level = (int) (player.skills().xpLevel(Skills.ATTACK)) + ((player.skills().xpLevel(Skills.ATTACK)) / 3);
            off_base_ranged_level = player.skills().xpLevel(Skills.RANGED);
            off_base_magic_level = player.skills().xpLevel(Skills.MAGIC);
        } else {
            Npc npc = ((Npc) entity);
            if (npc.combatInfo() != null && npc.combatInfo().stats != null) {
                off_base_attack_level = (int) (npc.combatInfo().stats.attack);
                off_base_ranged_level = npc.combatInfo().stats.ranged;
                off_base_magic_level = npc.combatInfo().stats.magic;
            } else {
                //
            }
        }

        //current levels
        double off_current_attack_level = 1;
        double off_current_ranged_level = 1;
        double off_current_magic_level = 1;

        if (entity.isPlayer()) {
            Player player = ((Player) entity);
            off_current_attack_level = player.skills().level(Skills.ATTACK);
            off_current_ranged_level = player.skills().level(Skills.RANGED);
            off_current_magic_level = player.skills().level(Skills.MAGIC);
        } else {
            Npc npc = ((Npc) entity);
            if (npc.combatInfo() != null && npc.combatInfo().stats != null) {
                off_current_attack_level = (int) (npc.combatInfo().stats.attack);
                off_current_ranged_level = npc.combatInfo().stats.ranged;
                off_current_magic_level = npc.combatInfo().stats.magic;
            }
        }

        // whats with this boost?
        off_current_attack_level *= 1.1;
        off_current_ranged_level *= 1.15;
        off_current_magic_level *= 1.15;

        double def_current_defence_level = 1;
        double def_current_magic_level = 1;

        if (enemy.isPlayer()) {
            Player opp = (Player) enemy;
            def_current_defence_level = opp.skills().level(Skills.DEFENCE);
            def_current_magic_level = opp.skills().level(Skills.MAGIC);

            int hpmissing = opp.maxHp() - opp.hp();
            if (hpmissing > 0 && Equipment.hasAmmyOfDamned(opp) && Equipment.fullTorag(opp)) {
                // Calc % increase. 1% per 1hp missing.
                double multi = 0.01D * hpmissing;
                def_current_defence_level += def_current_defence_level * multi;
            }
        } else {
            Npc npc = ((Npc) enemy);
            if (npc.combatInfo() != null && npc.combatInfo().stats != null) {
                def_current_defence_level = npc.combatInfo().stats.defence;
                def_current_magic_level = npc.combatInfo().stats.magic;
            }
        }

        //prayer bonuses
        double off_attack_prayer_bonus = 1.0;
        double off_ranged_prayer_bonus = 1.0;
        double off_magic_prayer_bonus = 1.0;
        double def_defence_prayer_bonus = 1.0;

        // Prayers
        if (entity.isPlayer()) {
            Player p = (Player) entity;

            if (DefaultPrayers.usingPrayer(p, DefaultPrayers.CLARITY_OF_THOUGHT))
                off_attack_prayer_bonus += 0.05; // 5% attack level boost
            else if (DefaultPrayers.usingPrayer(p, DefaultPrayers.IMPROVED_REFLEXES))
                off_attack_prayer_bonus += 0.10; // 10% attack level boost
            else if (DefaultPrayers.usingPrayer(p, DefaultPrayers.INCREDIBLE_REFLEXES))
                off_attack_prayer_bonus += 0.15; // 15% attack level boost
            else if (DefaultPrayers.usingPrayer(p, DefaultPrayers.CHIVALRY))
                off_attack_prayer_bonus += 0.15; // 15% attack level boost
            else if (DefaultPrayers.usingPrayer(p, DefaultPrayers.PIETY))
                off_attack_prayer_bonus += 0.20; // 20% attack level boost

            // System.out.println("attk bonus "+off_attack_prayer_bonus);

            if (DefaultPrayers.usingPrayer(p, DefaultPrayers.SHARP_EYE))
                off_ranged_prayer_bonus += 0.05; // 5% range level boost
            else if (DefaultPrayers.usingPrayer(p, DefaultPrayers.HAWK_EYE))
                off_ranged_prayer_bonus += 0.10; // 10% range level boost
            else if (DefaultPrayers.usingPrayer(p, DefaultPrayers.EAGLE_EYE))
                off_ranged_prayer_bonus += 0.15; // 15% range level boost
            else if (DefaultPrayers.usingPrayer(p, DefaultPrayers.RIGOUR))
                off_ranged_prayer_bonus += 0.20; // 20% range level boost

            if (DefaultPrayers.usingPrayer(p, DefaultPrayers.MYSTIC_WILL))
                off_magic_prayer_bonus += 0.05; // 5% magic level boost
            else if (DefaultPrayers.usingPrayer(p, DefaultPrayers.MYSTIC_LORE))
                off_magic_prayer_bonus += 0.10; // 10% magic level boost
            else if (DefaultPrayers.usingPrayer(p, DefaultPrayers.MYSTIC_MIGHT))
                off_magic_prayer_bonus += 0.15; // 15% magic level boost
            else if (DefaultPrayers.usingPrayer(p, DefaultPrayers.AUGURY))
                off_magic_prayer_bonus += 0.25; // 25% magic level boost
        }

        if (enemy.isPlayer()) {
            Player p = (Player) enemy;

            if (DefaultPrayers.usingPrayer(p, DefaultPrayers.THICK_SKIN))
                def_defence_prayer_bonus += 0.05; // 5% def level boost
            else if (DefaultPrayers.usingPrayer(p, DefaultPrayers.ROCK_SKIN))
                def_defence_prayer_bonus += 0.10; // 10% def level boost
            else if (DefaultPrayers.usingPrayer(p, DefaultPrayers.STEEL_SKIN))
                def_defence_prayer_bonus += 0.15; // 15% def level boost
            if (DefaultPrayers.usingPrayer(p, DefaultPrayers.CHIVALRY))
                def_defence_prayer_bonus += 0.20; // 20% def level boost
            else if (DefaultPrayers.usingPrayer(p, DefaultPrayers.PIETY))
                def_defence_prayer_bonus += 0.25; // 25% def level boost
            else if (DefaultPrayers.usingPrayer(p, DefaultPrayers.RIGOUR))
                def_defence_prayer_bonus += 0.25; // 25% def level boost
            else if (DefaultPrayers.usingPrayer(p, DefaultPrayers.AUGURY))
                def_defence_prayer_bonus += 0.25; // 25% def level boost

            //System.out.println("def bonus "+def_defence_prayer_bonus);
        }

        //additional bonus
        double off_additional_bonus = 1.0;

        if (entity.isPlayer()) {
            final Player player = (Player) entity;
            if (player.isSpecialActivated()) {
                if (true) {

                    // Dark bow special should always hit at least 8's.
                    if (player.getCombatSpecial() == CombatSpecial.DARK_BOW) {
                        //return true;
                    }

                    off_additional_bonus = player.getCombatSpecial().getAccuracyMultiplier();

                    //Hardcode extra accuracy
                    if(player.getEquipment().contains(ItemIdentifiers.HEAVY_BALLISTA) || player.getEquipment().contains(ItemIdentifiers.HEAVY_BALLISTA)) {
                        off_additional_bonus += 2.0;
                    }

                    //Pet accuracy bonus
                    if(player.hasPetOut("Baby Barrelchest")) {
                        off_additional_bonus += 0.10;
                    }

                    if(player.hasPetOut("Ancient barrelchest")) {
                        off_additional_bonus += 0.20;
                    }

                    //Custom effect not from OSRS, OSRS is 2.5% this is 5%
                    if (player.getCombat().getFightType().getAttackType() == AttackType.CRUSH) {
                        if (player.getEquipment().hasAt(EquipSlot.HEAD, INQUISITORS_GREAT_HELM)) {
                            off_additional_bonus += 0.01;//1% accuracy boost
                        }

                        if (player.getEquipment().hasAt(EquipSlot.BODY, INQUISITORS_HAUBERK)) {
                            off_additional_bonus += 0.01;//1% accuracy boost
                        }

                        if (player.getEquipment().hasAt(EquipSlot.LEGS, INQUISITORS_PLATESKIRT)) {
                            off_additional_bonus += 0.01;//1% accuracy boost
                        }

                        if (player.getEquipment().hasAt(EquipSlot.HEAD, INQUISITORS_GREAT_HELM) || player.getEquipment().hasAt(EquipSlot.BODY, INQUISITORS_HAUBERK) || player.getEquipment().hasAt(EquipSlot.LEGS, INQUISITORS_PLATESKIRT)) {
                            off_additional_bonus += 0.02;//2% accuracy boost
                        }
                    }

                    //System.out.println("off_additional_bonus: "+off_additional_bonus+ " spec accuracy: "+player.getCombatSpecial().getAccuracyMultiplier());
                }
            }
        }

        //if the player is using a slayer helm
        if (entity.isPlayer() && enemy.isNpc()) {
            final Npc npc = (Npc) enemy;
            final Player player = (Player) entity;
            final Item helm = player.getEquipment().get(EquipSlot.HEAD);
            final Item weapon = player.getEquipment().get(EquipSlot.WEAPON);
            if (helm != null && Slayer.creatureMatches(player, npc.id())) {
                if (player.getEquipment().wearingSlayerHelm() || (IntStream.range(8901, 8921).anyMatch(id -> id == helm.getId()))) {
                    off_additional_bonus += 0.125;
                }
            }

            if (weapon != null && weapon.getId() == TWISTED_BOW) {
               // twistedBowMultiplier = twistedBowAccuracyMultiplier((int) def_current_magic_level);
            }

            if (CombatFormula.obbyArmour(player) && CombatFormula.hasObbyWeapon(player)) {
                off_additional_bonus += 0.1;
            }

            //Arclight
            if (weapon != null && player.getEquipment().get(EquipSlot.WEAPON).getId() == ARCLIGHT) {
                if (npc.def().name.contains("demon") || npc.def().name.contains("skotizo") || npc.def().name.contains("demonic")) {
                    off_additional_bonus += 0.7;
                }
            }

            //Dragon hunter crossbow
            if (weapon != null && player.getEquipment().get(EquipSlot.WEAPON).getId() == DRAGON_HUNTER_CROSSBOW) {
                if (npc.def().name.contains("dragon") || npc.def().name.contains("great olm")) {
                    off_additional_bonus += 0.3;
                }
            }

            //Dragon hunter lance
            if (weapon != null && player.getEquipment().get(EquipSlot.WEAPON).getId() == DRAGON_HUNTER_LANCE) {
                if (npc.def().name.contains("dragon") || npc.def().name.contains("great olm")) {
                    off_additional_bonus += 0.3;
                }
            }

            //Craws Bow
            if (weapon != null && (CombatFormula.hasCrawsBow(player) || CombatFormula.hasViggorasChainMace(player))) {
                off_additional_bonus += 0.5;
            }

            //Thammaron Sceptre
            if (weapon != null && CombatFormula.hasThammaronSceptre(player)) {
                off_additional_bonus += 1.00;
            }

            //Magic on lava dragons
            if (npc.id() == LAVA_DRAGON && type.equals(CombatType.MAGIC)) {
                off_additional_bonus += 0.5;
            }

            //Arclight
            if (player.getEquipment().hasAt(EquipSlot.WEAPON, ARCLIGHT)) {
                if (npc.def() != null && npc.def().name != null && CombatFormula.isDemon(npc.def().name)) {
                    off_additional_bonus += 0.7;
                }
            }
        }

        //if the player is using a smoke battlestaff
        if (entity.isPlayer()) {
            final Player player = (Player) entity;
            if (player.getEquipment().containsAny(SMOKE_BATTLESTAFF, MYSTIC_SMOKE_STAFF) && type.equals(CombatType.MAGIC)) {
                off_additional_bonus += 0.10;
            }
        }

        //equipment bonuses
        int off_equipment_stab_attack = playerBonuses.stab;
        int off_equipment_slash_attack = playerBonuses.slash;
        int off_equipment_crush_attack = playerBonuses.crush;
        int off_equipment_ranged_attack = playerBonuses.range;
        int off_equipment_magic_attack = playerBonuses.mage;

        int def_equipment_stab_defence = targetBonuses.stabdef;
        int def_equipment_slash_defence = targetBonuses.slashdef;
        int def_equipment_crush_defence = targetBonuses.crushdef;
        int def_equipment_ranged_defence = targetBonuses.rangedef;
        int def_equipment_magic_defence = targetBonuses.magedef;

        if (enemy.isNpc()) {
            Npc npc = (Npc) enemy;
            if (npc.combatInfo() != null && npc.combatInfo().stats != null && npc.combatInfo().boss) {
                def_equipment_ranged_defence -= (def_current_defence_level * 0.50); //I don't like this solution but this formula is fucked.
            }
        }

        //protect from * prayers
        boolean def_protect_from_melee = false;
        boolean def_protect_from_ranged = false;
        boolean def_protect_from_magic = false;

        if (entity.isNpc() && enemy.isPlayer()) {
            Player player = ((Player) enemy);
            def_protect_from_melee = DefaultPrayers.usingPrayer(player, DefaultPrayers.PROTECT_FROM_MELEE);
            def_protect_from_ranged = DefaultPrayers.usingPrayer(player, DefaultPrayers.PROTECT_FROM_MISSILES);
            def_protect_from_magic = DefaultPrayers.usingPrayer(player, DefaultPrayers.PROTECT_FROM_MAGIC);
        }

        //chance bonuses
        double off_special_attack_bonus = 1.0;
        double off_void_bonus = 1.0;

        if (entity.isPlayer()) {
            final Player player = (Player) entity;
            if (type.equals(CombatType.MELEE) && (CombatFormula.voidMelee(player) || CombatFormula.voidCustomMelee(player)))
                off_void_bonus = 1.10;
            else if (type.equals(CombatType.RANGED) && (CombatFormula.voidRanger(player) || CombatFormula.voidCustomRanger(player)))
                off_void_bonus = 1.10;
            else if (type.equals(CombatType.MAGIC) && (CombatFormula.voidMagic(player) || CombatFormula.voidCustomMagic(player)))
                off_void_bonus = 1.30;
        }

        /*
            S E T T I N G S

            E N D
        */

        /*
            C A L C U L A T E D
            V A R I A B L E S

            S T A R T
        */

        //experience bonuses
        double off_spell_bonus = 0;
        double off_weapon_bonus = 0;

        //effective levels
        double effective_attack = 0;
        double effective_magic = 0;
        double effective_defence = 0;

        //relevent equipment bonuses
        int off_equipment_bonus = 0;
        int def_equipment_bonus = 0;

        //augmented levels
        double augmented_attack = 0;
        double augmented_defence = 0;

        //hit chances
        double hit_chance = 0;
        double off_hit_chance = 0;
        double def_block_chance = 0;

        /*
            C A L C U L A T E D
            V A R I A B L E S

            E N D
        */

        AttackType off_style = null;
        if (entity.isPlayer()) {
            off_style = entity.getCombat().getFightType().getAttackType();
        }
        double baseBonus = 64.; // TODO custom: made this 1/4th of original - 16 instead of 64

        //determine effective attack
        switch (type) {
            case MELEE -> {
                if (off_base_attack_level > off_weapon_requirement) {
                    off_weapon_bonus = (off_base_attack_level - off_weapon_requirement) * .3;
                }
                effective_attack = Math.floor((((off_current_attack_level * off_attack_prayer_bonus) * off_additional_bonus) + off_stance_bonus + off_weapon_bonus) * other_bonuses);
                effective_defence = Math.floor((def_current_defence_level * def_defence_prayer_bonus) + def_stance_bonus);
                if (off_style != null) {
                    switch (off_style) {
                        case STAB -> {
                            off_equipment_bonus = off_equipment_stab_attack;
                            def_equipment_bonus = def_equipment_stab_defence;
                        }
                        case SLASH -> {
                            off_equipment_bonus = off_equipment_slash_attack;
                            def_equipment_bonus = def_equipment_slash_defence;
                        }
                        case CRUSH -> {
                            off_equipment_bonus = off_equipment_crush_attack;
                            def_equipment_bonus = def_equipment_crush_defence;
                        }
                        default -> {
                            off_equipment_bonus = Math.max(Math.max(off_equipment_stab_attack, off_equipment_slash_attack), off_equipment_crush_attack);
                            def_equipment_bonus = Math.max(Math.max(def_equipment_stab_defence, def_equipment_slash_defence), def_equipment_crush_defence);
                        }
                    }
                } else {
                    off_equipment_bonus = Math.max(Math.max(off_equipment_stab_attack, off_equipment_slash_attack), off_equipment_crush_attack);
                    def_equipment_bonus = Math.max(Math.max(def_equipment_stab_defence, def_equipment_slash_defence), def_equipment_crush_defence);
                }
                if (enemy.isPlayer()) {
                    def_equipment_bonus *= .6; // TODO (not rly todo) FYI custom: reduced by 40%
                    baseBonus = 16.;
                }
            }
            case RANGED -> {
                effective_attack = Math.floor((((off_current_ranged_level * off_ranged_prayer_bonus) * off_additional_bonus) + off_stance_bonus + off_weapon_bonus) * twistedBowMultiplier);
                effective_defence = Math.floor((def_current_defence_level * def_defence_prayer_bonus) + def_stance_bonus);
                off_equipment_bonus = off_equipment_ranged_attack;
                def_equipment_bonus = def_equipment_ranged_defence;
            }
            case MAGIC -> {
                effective_attack = Math.floor(((off_current_magic_level * off_magic_prayer_bonus) * off_additional_bonus) + off_spell_bonus);
                effective_magic = Math.floor(def_current_magic_level * .7);
                effective_defence = Math.floor((def_current_defence_level * def_defence_prayer_bonus) * .3);
                effective_defence = effective_defence + effective_magic;
                off_equipment_bonus = off_equipment_magic_attack;
                def_equipment_bonus = def_equipment_magic_defence;
            }
        }

        //determine augmented levels
        augmented_attack = Math.floor(((effective_attack + 8) * (off_equipment_bonus + 64.)));
        augmented_defence = Math.floor(((effective_defence + 8) * (def_equipment_bonus + baseBonus)));

        //determine hit chance
        if (augmented_attack < augmented_defence) {
            hit_chance = augmented_attack / ((augmented_defence + 1.) * 2.);
        } else {
            hit_chance = 1. - ((augmented_defence + 2.) / ((augmented_attack + 1.) * 2.));
        }

        switch (type) {
            case MELEE:
                if (def_protect_from_melee) {
                    off_hit_chance = Math.floor((((hit_chance * off_special_attack_bonus) * off_void_bonus) * .6) * 100.);
                    def_block_chance = Math.floor(101 - ((((hit_chance * off_special_attack_bonus) * off_void_bonus) * .6) * 100.));
                } else {
                    off_hit_chance = Math.floor(((hit_chance * off_special_attack_bonus) * off_void_bonus) * 100.);
                    def_block_chance = Math.floor(101 - (((hit_chance * off_special_attack_bonus) * off_void_bonus) * 100.));
                }
                break;
            case RANGED:
                if (def_protect_from_ranged) {
                    off_hit_chance = Math.floor((((hit_chance * off_special_attack_bonus) * off_void_bonus) * .6) * 100.);
                    def_block_chance = Math.floor(101 - ((((hit_chance * off_special_attack_bonus) * off_void_bonus) * .6) * 100.));
                } else {
                    off_hit_chance = Math.floor(((hit_chance * off_special_attack_bonus) * off_void_bonus) * 100.);
                    def_block_chance = Math.floor(101 - (((hit_chance * off_special_attack_bonus) * off_void_bonus) * 100.));
                }
                break;
            case MAGIC:
                if (def_protect_from_magic) {
                    off_hit_chance = Math.floor(((hit_chance * off_void_bonus) * .6) * 100.);
                    def_block_chance = Math.floor(101 - (((hit_chance * off_void_bonus) * .6) * 100.));
                } else {
                    off_hit_chance = Math.floor((hit_chance * off_void_bonus) * 100.);
                    def_block_chance = Math.floor(101 - ((hit_chance * off_void_bonus) * 100.));
                }
                break;
        }

        if (entity.isPlayer()) {

            sb.append("Player (" + entity.getAsPlayer().getUsername() + ") chance to hit is: " + off_hit_chance + "%, vs opponents chance to block is: " + def_block_chance + "%");
            //sb.append("Your opponents chance to block is: " + def_block_chance + "%");
            sb.append("\nYour attack is " + augmented_attack + " (based on lvl " +
                effective_attack+" & "+off_equipment_bonus+", pray "+off_attack_prayer_bonus+", other "+off_additional_bonus+") and his def is " + augmented_defence+" (based on lvl "+effective_defence+" & "+def_equipment_bonus+", pray "+def_defence_prayer_bonus+")");
            //sb.append("Attack bonus " + off_equipment_bonus + ", atk lv " + effective_attack);
            //sb.append("Def bonus " + def_equipment_bonus + ", atk lv " + effective_defence);
           // System.out.printf("def stab %s , slash %s, crush %s%n", def_equipment_stab_defence, def_equipment_slash_defence, def_equipment_crush_defence);
            String msg = String.format("Atk %d v def %d. Bonus %d vs %d. Level %d vs %d. Relative %d%% hit > %d%% block%n", (int) augmented_attack, (int) augmented_defence, off_equipment_bonus, def_equipment_bonus, (int) effective_attack, (int) effective_defence, (int) off_hit_chance, (int) def_block_chance);
            //sb.append(msg);
            //mob.getAsPlayer().message(msg);
        }

        /*
         * Brimstone ring effect.
         */
        if (type == CombatType.MAGIC && entity.isPlayer() && entity.getAsPlayer().getEquipment().contains(BRIMSTONE_RING)) {
            if (Utils.randomFloat() <= 0.25) {
                def_block_chance = def_block_chance * 0.9D;
            }
        }

        off_hit_chance = (int) (srand.nextFloat() * off_hit_chance);
        def_block_chance = (int) (srand.nextFloat() * def_block_chance);

        //determine hit
        if (entity.isPlayer()) {
            //print roll
            //sb.append(("You (" + entity.getAsPlayer().getUsername() + ") rolled: " + (int) off_hit_chance);
            //sb.append(("Your opponent rolled: " + (int) def_block_chance);
            sb.append("\nPlayer "+ entity.getAsPlayer().getUsername()+" hit Success = if " + off_hit_chance + " > " + def_block_chance);
        }
        return sb.toString();
    }
}
