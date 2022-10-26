package com.ferox.game.content.kill_logs;

import com.ferox.game.content.raids.chamber_of_secrets.ChamberOfSecrets;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.util.Color;
import com.ferox.util.CustomNpcIdentifiers;
import com.ferox.util.NpcIdentifiers;
import com.ferox.util.Utils;

import java.util.Arrays;

import static com.ferox.game.world.entity.AttributeKey.BOSS_POINTS;
import static com.ferox.util.CustomNpcIdentifiers.*;

/**
 * @author Patrick van Elderen | Zerikoth | PVE
 * @date februari 09, 2020 11:14
 */
public class BossKillLog {

    /**
     * The player this is relative to
     */
    private final Player player;

    /**
     * Creates a new {@link BossKillLog} object for a singular player
     *
     * @param player the player
     */
    public BossKillLog(Player player) {
        this.player = player;
    }

    /**
     * This method checks if we can add a kill to our tracking variables.
     *
     * @param npc    The slayer monster being tracked
     */
    public void addKill(Npc npc) {
        for (Bosses boss : Bosses.values()) {
            if (Arrays.stream(boss.getNpcs()).anyMatch(npc_id -> npc_id == npc.id())) {
                int killCount = player.getAttribOr(boss.getKc(),0);
                killCount++;
                player.putAttrib(boss.getKc(), (killCount));
                player.message("Your " + boss.getName() + " kill count is: <col=ca0d0d>" + Utils.format(killCount) + "</col>.");

                var points = player.<Integer>getAttribOr(BOSS_POINTS,0) + boss.points;
                player.putAttrib(BOSS_POINTS, points);
                player.message(Color.PURPLE.wrap("You have received +"+boss.points+" boss points, you now have a total of "+Utils.formatNumber(points)+" boss points."));

                if(npc.combatInfo() == null) return;
                int deathLength = npc.combatInfo().deathlen;
                break;
            }
        }
    }

    private static final int STARTING_NAME_LINE = 46321;

    public enum Bosses {

        KREE_ARRA("Kree'Arra",2, AttributeKey.KREE_ARRA_KILLED, 0, STARTING_NAME_LINE, NpcIdentifiers.KREEARRA),
        COMMANDER_ZILYANA("Commander Zilyana",2, AttributeKey.COMMANDER_ZILYANA_KILLED, 0, STARTING_NAME_LINE + 6, NpcIdentifiers.COMMANDER_ZILYANA),
        GENERAL_GRAARDOR("General Graardor",2, AttributeKey.GENERAL_GRAARDOR_KILLED, 0, STARTING_NAME_LINE + (6 * 2), NpcIdentifiers.GENERAL_GRAARDOR),
        KRIL_TSUTSAROTH("K'ril Tsutsaroth",2, AttributeKey.KRIL_TSUTSAROTHS_KILLED, 0, STARTING_NAME_LINE + (6 * 3), NpcIdentifiers.KRIL_TSUTSAROTH),
        DAGANNOTH_REX("Dagannoth Rex",1, AttributeKey.KC_REX, 0, STARTING_NAME_LINE + (6 * 4), NpcIdentifiers.DAGANNOTH_REX),
        DAGANNOTH_PRIME("Dagannoth Prime",1, AttributeKey.KC_PRIME, 0, STARTING_NAME_LINE + (6 * 5), NpcIdentifiers.DAGANNOTH_PRIME),
        DAGANNOTH_SUPREME("Dagannoth Supreme",1, AttributeKey.KC_SUPREME, 0, STARTING_NAME_LINE + (6 * 6), NpcIdentifiers.DAGANNOTH_SUPREME),
        GIANT_MOLE("Giant Mole",1, AttributeKey.KC_GIANTMOLE, 0, STARTING_NAME_LINE + (6 * 7), NpcIdentifiers.GIANT_MOLE),
        KALPHITE_QUEEN("Kalphite Queen",1, AttributeKey.KC_KQ, 0, STARTING_NAME_LINE + (6 * 8), NpcIdentifiers.KALPHITE_QUEEN_6501),
        KING_BLACK_DRAGON("King Black Dragon",1, AttributeKey.KING_BLACK_DRAGONS_KILLED, 0, STARTING_NAME_LINE + (6 * 9), NpcIdentifiers.KING_BLACK_DRAGON),
        CALLISTO("Callisto",3, AttributeKey.CALLISTOS_KILLED, 0, STARTING_NAME_LINE + (6 * 10), NpcIdentifiers.CALLISTO_6609),
        VENENATIS("Venenatis",3, AttributeKey.VENENATIS_KILLED, 0, STARTING_NAME_LINE + (6 * 11), NpcIdentifiers.VENENATIS_6610),
        CHAOS_ELEMENTAL("Chaos Elemental",2, AttributeKey.CHAOS_ELEMENTALS_KILLED, 0, STARTING_NAME_LINE + (6 * 12), NpcIdentifiers.CHAOS_ELEMENTAL),
        CHAOS_FANATIC("Chaos Fanatic",2, AttributeKey.CHAOS_FANATICS_KILLED, 0, STARTING_NAME_LINE + (6 * 13), NpcIdentifiers.CHAOS_FANATIC),
        CRAZY_ARCHAEOLOGIST("Crazy Archaeologist",2, AttributeKey.CRAZY_ARCHAEOLOGISTS_KILLED, 0, STARTING_NAME_LINE + (6 * 14), NpcIdentifiers.CRAZY_ARCHAEOLOGIST),
        SCORPIA("Scorpia",2, AttributeKey.SCORPIAS_KILLED, 0, STARTING_NAME_LINE + (6 * 15), NpcIdentifiers.SCORPIA),
        BARROWS_CHESTS("Barrows Chests",0, AttributeKey.BARROWS_CHESTS_OPENED, 0, STARTING_NAME_LINE + (6 * 16)),
        CORPOREAL_BEAST("Corporeal Beast",5, AttributeKey.CORPOREAL_BEASTS_KILLED, 0, STARTING_NAME_LINE + (6 * 17), NpcIdentifiers.CORPOREAL_BEAST),
        ZULRAH("Zulrah",3, AttributeKey.ZULRAHS_KILLED, 0, STARTING_NAME_LINE + (6 * 18), NpcIdentifiers.ZULRAH, NpcIdentifiers.ZULRAH_2043, NpcIdentifiers.ZULRAH_2044),
        TEKTON("Tekton",5, AttributeKey.TEKTONS_KILLED, 0, STARTING_NAME_LINE + (6 * 19), NpcIdentifiers.TEKTON_7541),
        TZTOK_JAD("TzTok-Jad",1, AttributeKey.JADS_KILLED, 0, STARTING_NAME_LINE + (6 * 20), NpcIdentifiers.TZTOKJAD),
        TZKAL_ZUK("TzKal-Zuk",5, AttributeKey.KC_TZKAL_ZUK, 0, STARTING_NAME_LINE + (6 * 21), NpcIdentifiers.TZKALZUK),
        KRAKEN("Kraken",1, AttributeKey.KRAKENS_KILLED, 0, STARTING_NAME_LINE + (6 * 22), NpcIdentifiers.KRAKEN),
        THERMONUCLEAR_SMOKE_DEVIL("Thermonuclear smoke devil",1, AttributeKey.THERMONUCLEAR_SMOKE_DEVILS_KILLED, 0, STARTING_NAME_LINE + (6 * 23), NpcIdentifiers.THERMONUCLEAR_SMOKE_DEVIL),
        CERBERUS("Cerberus",2, AttributeKey.CERBERUS_KILLED, 0, STARTING_NAME_LINE + (6 * 24), NpcIdentifiers.CERBERUS),
        ABYSSAL_SIRE("Abyssal Sire",2, AttributeKey.KC_ABYSSALSIRE, 0, STARTING_NAME_LINE + (6 * 25), 5890),
        SKOTIZO("Skotizo",5, AttributeKey.SKOTIZOS_KILLED, 0, STARTING_NAME_LINE + (6 * 26), NpcIdentifiers.SKOTIZO),
        WINTERTODT("Wintertodt",1, AttributeKey.WINTERTODT, 0, STARTING_NAME_LINE + (6 * 27)),
        DERANGED_ARCHAEOLOGIST("Deranged Archaeologist",1, AttributeKey.DERANGED_ARCH, 0, STARTING_NAME_LINE + (6 * 28), NpcIdentifiers.DERANGED_ARCHAEOLOGIST),
        GROTESQUE_GUARDIANS("Grotesque Guaridans",2, AttributeKey.GROTESQUE_GUARDIANS, 0, STARTING_NAME_LINE + (6 * 29), NpcIdentifiers.MIDNIGHT, NpcIdentifiers.NOON),
        VORKATH("Vorkath",3, AttributeKey.VORKATHS_KILLED, 0, STARTING_NAME_LINE + (6 * 30), NpcIdentifiers.VORKATH_8061),
        BYROPHYTA("Byrophyta",1, AttributeKey.BYROPHYTA, 0, STARTING_NAME_LINE + (6 * 31), NpcIdentifiers.BRYOPHYTA),
        CORRUPTED_NECHRYARCH("Corrupted Nechryarch",3, AttributeKey.CORRUPTED_NECHRYARCHS_KILLED, 0, STARTING_NAME_LINE + (6 * 32), CustomNpcIdentifiers.CORRUPTED_NECHRYARCH),
        ALCHEMICAL_HYDRA("Alchemical Hydra",4, AttributeKey.ALCHY_KILLED, 0, STARTING_NAME_LINE + (6 * 33), NpcIdentifiers.ALCHEMICAL_HYDRA, NpcIdentifiers.ALCHEMICAL_HYDRA_8616, NpcIdentifiers.ALCHEMICAL_HYDRA_8617, NpcIdentifiers.ALCHEMICAL_HYDRA_8618, NpcIdentifiers.ALCHEMICAL_HYDRA_8619, NpcIdentifiers.ALCHEMICAL_HYDRA_8620, NpcIdentifiers.ALCHEMICAL_HYDRA_8621, NpcIdentifiers.ALCHEMICAL_HYDRA_8622),
        HESPORI("Hespori",2, AttributeKey.HESPORI, 0, STARTING_NAME_LINE + (6 * 34), NpcIdentifiers.HESPORI),
        SARACHNIS("Sarachnis",2, AttributeKey.SARACHNIS, 0, STARTING_NAME_LINE + (6 * 35), NpcIdentifiers.SARACHNIS),
        ZALCANO("Zalcano",2, AttributeKey.ZALCANO, 0, STARTING_NAME_LINE + (6 * 36), NpcIdentifiers.ZALCANO),
        THE_GAUNTLET("The Gauntlet",5, AttributeKey.THE_GAUNTLET, 0, STARTING_NAME_LINE + (6 * 37)),
        THE_CORRUPTED_GAUNTLET("The Corrupted Gauntlet",10, AttributeKey.THE_CORRUPTED_GAUNTLET, 0, STARTING_NAME_LINE + (6 * 38)),
        ZOMBIES_CHAMPION("Zombies Champion",5, AttributeKey.ZOMBIES_CHAMPIONS_KILLED, 0, STARTING_NAME_LINE + (6 * 39), NpcIdentifiers.ZOMBIES_CHAMPION),
        LIZARDMAN_SHAMAN("Lizardman Shaman",2, AttributeKey.LIZARDMAN_SHAMANS_KILLED, 0, STARTING_NAME_LINE + (6 * 40), NpcIdentifiers.LIZARDMAN_SHAMAN, NpcIdentifiers.LIZARDMAN_SHAMAN_6767),
        BARRELCHEST("Barrelchest",2, AttributeKey.BARRELCHESTS_KILLED,0,STARTING_NAME_LINE + (6 * 41), NpcIdentifiers.BARRELCHEST_6342),
        BRUTAL_LAVA_DRAGON("Brutal Lava Dragon",5, AttributeKey.BRUTAL_LAVA_DRAGONS_KILLED, 0, STARTING_NAME_LINE + (6 * 42), BRUTAL_LAVA_DRAGON_FLYING),
        ANCIENT_BARRELCHEST("Ancient Barrelchest",4, AttributeKey.ANCIENT_BARRELCHESTS_KILLED, 0, STARTING_NAME_LINE + (6 * 43), CustomNpcIdentifiers.ANCIENT_BARRELCHEST),
        ANCIENT_CHAOS_ELEMENTAL("Ancient Chaos Elemental",4, AttributeKey.ANCIENT_CHAOS_ELEMENTALS_KILLED, 0, STARTING_NAME_LINE + (6 * 44), CustomNpcIdentifiers.ANCIENT_CHAOS_ELEMENTAL),
        ANCIENT_KING_BLACK_DRAGON("Ancient King Black Dragon",2, AttributeKey.ANCIENT_KING_BLACK_DRAGONS_KILLED, 0, STARTING_NAME_LINE + (6 * 45), CustomNpcIdentifiers.ANCIENT_KING_BLACK_DRAGON),
        ANCIENT_REVENANT("Ancient Revenants",1, AttributeKey.ANCIENT_REVENANTS_KILLED, 0, STARTING_NAME_LINE + (6 * 46), ANCIENT_REVENANT_DARK_BEAST, ANCIENT_REVENANT_ORK, ANCIENT_REVENANT_CYCLOPS, ANCIENT_REVENANT_DRAGON, ANCIENT_REVENANT_KNIGHT),
        DEMONIC_GORILLA("Demonic gorillas", 2, AttributeKey.DEMONIC_GORILLAS_KILLED,0, STARTING_NAME_LINE + (6 * 47), NpcIdentifiers.DEMONIC_GORILLA, NpcIdentifiers.DEMONIC_GORILLA_7145, NpcIdentifiers.DEMONIC_GORILLA_7146, NpcIdentifiers.DEMONIC_GORILLA_7147, NpcIdentifiers.DEMONIC_GORILLA_7148, NpcIdentifiers.DEMONIC_GORILLA_7149),
        THE_NIGHTMARE("The Nightmare", 15, AttributeKey.THE_NIGHTMARE_KC,0, STARTING_NAME_LINE + (6 * 48), NpcIdentifiers.THE_NIGHTMARE_9430),
        CORRUPTED_HUNLEFFS("Corrupted Hunleff", 15, AttributeKey.CORRUPTED_HUNLEFFS_KILLED,0, STARTING_NAME_LINE + (6 * 49), NpcIdentifiers.CORRUPTED_HUNLLEF, NpcIdentifiers.CORRUPTED_HUNLLEF_9036, NpcIdentifiers.CORRUPTED_HUNLLEF_9037),
        NEX("Nex", 15, AttributeKey.NEX_KILLED,0, STARTING_NAME_LINE + (6 * 50), NpcIdentifiers.NEX),

        ;

        private final String name;
        private final int points;
        private final AttributeKey kc;
        private final int streak;
        private final int startLine;
        private final int[] npcs;

        Bosses(String name, int points, AttributeKey kc, int streak, int nameLine, int... npcs) {
            this.name = name;
            this.points = points;
            this.kc = kc;
            this.streak = streak;
            this.startLine = nameLine;
            this.npcs = npcs;
        }

        public String getName() {
            return name;
        }

        public int getPoints() {
            return points;
        }

        public AttributeKey getKc() {
            return kc;
        }

        public int getStreak() {
            return streak;
        }

        public int getStartLine() {
            return startLine;
        }

        public int[] getNpcs() {
            return npcs;
        }
    }

    public void openLog() {
        player.getInterfaceManager().open(46300);
        player.getPacketSender().changeWidgetText(46304, "Boss Kill Log");

        int count = 0;
        for (Bosses bosses : Bosses.values()) {
            count++;
            player.getPacketSender().sendString(bosses.getStartLine(), bosses.getName());
            int kc = player.getAttribOr(bosses.getKc(), 0);
            player.getPacketSender().sendString(bosses.getStartLine()+1, ""+ Utils.formatNumber(kc));
            player.getPacketSender().sendString(bosses.getStartLine()+2, ""+ Utils.formatNumber(bosses.getStreak()));
        }
        player.getPacketSender().sendScrollbarHeight(46310, (int) (count * 17.2));
    }

}
