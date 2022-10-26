package com.ferox.game.world.entity.mob.npc;

import com.ferox.GameServer;
import com.ferox.fs.NpcDefinition;
import com.ferox.game.content.EffectTimer;
import com.ferox.game.content.achievements.Achievements;
import com.ferox.game.content.achievements.AchievementsManager;
import com.ferox.game.content.announcements.ServerAnnouncements;
import com.ferox.game.content.areas.burthope.warriors_guild.MagicalAnimator;
import com.ferox.game.content.areas.wilderness.content.boss_event.WildernessBossEvent;
import com.ferox.game.content.areas.wilderness.content.todays_top_pkers.TopPkers;
import com.ferox.game.content.daily_tasks.DailyTaskManager;
import com.ferox.game.content.daily_tasks.DailyTasks;
import com.ferox.game.content.raids.chamber_of_xeric.great_olm.Phases;
import com.ferox.game.content.raids.party.Party;
import com.ferox.game.content.skill.impl.prayer.Bone;
import com.ferox.game.content.skill.impl.slayer.Slayer;
import com.ferox.game.content.skill.impl.slayer.SlayerConstants;
import com.ferox.game.content.skill.impl.slayer.slayer_partner.SlayerPartner;
import com.ferox.game.content.tasks.impl.Tasks;
import com.ferox.game.content.treasure.TreasureRewardCaskets;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.bountyhunter.EarningPotential;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.combat.method.impl.npcs.bosses.kalphite.KalphiteQueenFirstForm;
import com.ferox.game.world.entity.combat.method.impl.npcs.bosses.kalphite.KalphiteQueenSecondForm;
import com.ferox.game.world.entity.combat.method.impl.npcs.bosses.wilderness.vetion.VetionMinion;
import com.ferox.game.world.entity.combat.method.impl.npcs.bosses.zulrah.Zulrah;
import com.ferox.game.world.entity.combat.method.impl.npcs.fightcaves.TzTokJad;
import com.ferox.game.world.entity.combat.method.impl.npcs.godwars.GwdLogic;
import com.ferox.game.world.entity.combat.method.impl.npcs.godwars.zaros.Cruor;
import com.ferox.game.world.entity.combat.method.impl.npcs.godwars.zaros.Fumus;
import com.ferox.game.world.entity.combat.method.impl.npcs.godwars.zaros.Glacies;
import com.ferox.game.world.entity.combat.method.impl.npcs.godwars.zaros.Umbra;
import com.ferox.game.world.entity.combat.method.impl.npcs.hydra.AlchemicalHydra;
import com.ferox.game.world.entity.combat.method.impl.npcs.karuulm.Drake;
import com.ferox.game.world.entity.combat.method.impl.npcs.karuulm.Wyrm;
import com.ferox.game.world.entity.combat.method.impl.npcs.raids.Vespula;
import com.ferox.game.world.entity.combat.method.impl.npcs.raids.VespulaMinion;
import com.ferox.game.world.entity.combat.method.impl.npcs.slayer.Gargoyle;
import com.ferox.game.world.entity.combat.method.impl.npcs.slayer.Nechryael;
import com.ferox.game.world.entity.combat.method.impl.npcs.slayer.kraken.KrakenBoss;
import com.ferox.game.world.entity.combat.method.impl.npcs.slayer.superiors.nechryarch.NechryarchDeathSpawn;
import com.ferox.game.world.entity.combat.skull.SkullType;
import com.ferox.game.world.entity.mob.npc.droptables.ScalarLootTable;
import com.ferox.game.world.entity.mob.npc.pets.Pet;
import com.ferox.game.world.entity.mob.npc.pets.PetAI;
import com.ferox.game.world.entity.mob.player.GameMode;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.ground.GroundItem;
import com.ferox.game.world.items.ground.GroundItemHandler;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Area;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.util.*;
import com.ferox.util.chainedwork.Chain;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static com.ferox.game.content.collection_logs.CollectionLog.RAIDS_KEY;
import static com.ferox.game.content.collection_logs.LogType.*;
import static com.ferox.game.world.entity.AttributeKey.*;
import static com.ferox.util.CustomItemIdentifiers.HWEEN_TOKENS;
import static com.ferox.util.CustomNpcIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.BLOOD_MONEY;
import static com.ferox.util.ItemIdentifiers.BRACELET_OF_ETHEREUM;
import static com.ferox.util.NpcIdentifiers.*;

/**
 * Represents an npc's death task, which handles everything
 * an npc does before and after their death animation (including it),
 * such as dropping their drop table items.
 *
 * @author relex lawl
 * @author Created by Bart on 10/6/2015.
 */
public class NpcDeath {

    private static final Logger logger = LogManager.getLogger(NpcDeath.class);
    private static final Logger npcDropLogs = LogManager.getLogger("NpcDropLogs");
    private static final Level NPC_DROPS;

    static {
        NPC_DROPS = Level.getLevel("NPC_DROPS");
    }

    private static final List<Integer> customDrops = Arrays.asList(WHIRLPOOL_496, KRAKEN, CAVE_KRAKEN, WHIRLPOOL, ZULRAH, ZULRAH_2043, ZULRAH_2044);

    public static void execute(Npc npc) {
        try {
          //  System.out.println("here");
            // Path reset instantly when hitsplat appears killing the npc.
            var respawnTimer = 50;// default 30 seconds
            NpcDefinition def = World.getWorld().definitions().get(NpcDefinition.class, npc.id());
            if(def != null) {
                if(def.combatlevel >= 1 && def.combatlevel <= 50) {
                    respawnTimer = 17;//10 seconds
                } else if(def.combatlevel >= 51 && def.combatlevel <= 150) {
                    respawnTimer = 33;//20 seconds
                } else {
                    respawnTimer = 42;// 25 seconds
                }
            }
            npc.getMovementQueue().clear();
            npc.lockNoDamage();
            npc.combatInfo().aggressive = false;
            
            // Reset interacting entity..
            npc.setEntityInteraction(null);

            Optional<Player> killer_id = npc.getCombat().getKiller();

            // Player that did the most damage.
            Player killer = killer_id.orElse(null);

            Chain.bound(null).runFn(1, () -> {
                // 1t later facing is reset. Stops npcs looking odd when they reset facing their target the tick they die.
                npc.face(null);
            });

            if (killer != null) {
                var biggest_and_baddest_perk = killer.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.BIGGEST_AND_BADDEST) && Slayer.creatureMatches(killer, npc.id());
                var ancientRevSpawnRoll = 25;
                var superiorSpawnRoll = biggest_and_baddest_perk ? 4 : 6;

                var reduction = ancientRevSpawnRoll * killer.memberAncientRevBonus() / 100;
                ancientRevSpawnRoll -= reduction;

                var legendaryInsideCave = killer.tile().memberCave() && killer.getMemberRights().isLegendaryMemberOrGreater(killer);
                var VIPInsideCave = killer.tile().memberCave() && killer.getMemberRights().isLegendaryMemberOrGreater(killer);
                var SponsorInsideCave = killer.tile().memberCave() && killer.getMemberRights().isLegendaryMemberOrGreater(killer);
                if(legendaryInsideCave)
                    respawnTimer = 34;
                if(VIPInsideCave)
                    respawnTimer = 30;
                if(SponsorInsideCave)
                    respawnTimer = 25;
             //   killer.message("Here");
                killer.getCombat().reset();

                // Increment kill.
                killer.getSlayerKillLog().addKill(npc);
                if (!npc.isWorldBoss() /*|| npc.id() != THE_NIGHTMARE_9430*/|| npc.id() != KALPHITE_QUEEN_6500) {
                    killer.getBossKillLog().addKill(npc);
                }

                if (npc.def().name.equalsIgnoreCase("Yak")) {
                    AchievementsManager.activate(killer, Achievements.YAK_HUNTER, 1);
                }

                if (npc.def().name.equalsIgnoreCase("Rock Crab")) {
                    AchievementsManager.activate(killer, Achievements.ROCK_CRAB_HUNTER, 1);
                }

                if (npc.def().name.equalsIgnoreCase("Sand Crab")) {
                    AchievementsManager.activate(killer, Achievements.SAND_CRAB_HUNTER, 1);
                }

                if (npc.def().name.equalsIgnoreCase("Experiment")) {
                    AchievementsManager.activate(killer, Achievements.EXPERIMENTS_HUNTER, 1);
                }

                if (npc.def().name.equalsIgnoreCase("Adamant dragon")) {
                    var kc = killer.<Integer>getAttribOr(ADAMANT_DRAGONS_KILLED, 0) + 1;
                    killer.putAttrib(ADAMANT_DRAGONS_KILLED, kc);
                }

                if (npc.def().name.equalsIgnoreCase("Rune dragon")) {
                    var kc = killer.<Integer>getAttribOr(RUNE_DRAGONS_KILLED, 0) + 1;
                    killer.putAttrib(RUNE_DRAGONS_KILLED, kc);
                }

                if (npc.def().name.equalsIgnoreCase("Lava dragon")) {
                    var kc = killer.<Integer>getAttribOr(LAVA_DRAGONS_KILLED, 0) + 1;
                    killer.putAttrib(LAVA_DRAGONS_KILLED, kc);
                }

                if (npc.def().name.contains("dragon") || npc.def().name.contains("Dragon")) {
                    AchievementsManager.activate(killer, Achievements.DRAGON_SLAYER_I, 1);
                    killer.getTaskMasterManager().increase(Tasks.DRAGONS);
                }

                if (npc.def().name.contains("Black dragon") || npc.def().name.contains("black dragon")) {
                    AchievementsManager.activate(killer, Achievements.DRAGON_SLAYER_II, 1);
                }

                if (npc.def().name.equalsIgnoreCase("K'ril Tsutsaroth") || npc.def().name.equalsIgnoreCase("General Graardor") || npc.def().name.equalsIgnoreCase("Commander Zilyana") || npc.def().name.equalsIgnoreCase("Kree'arra")) {
                    AchievementsManager.activate(killer, Achievements.GODWAR, 1);
                }

                if (npc.def().name.contains("Revenant") || npc.def().name.contains("revenant")) {
                    AchievementsManager.activate(killer, Achievements.REVENANT_HUNTER_I, 1);
                    AchievementsManager.activate(killer, Achievements.REVENANT_HUNTER_II, 1);
                    AchievementsManager.activate(killer, Achievements.REVENANT_HUNTER_III, 1);
                    AchievementsManager.activate(killer, Achievements.REVENANT_HUNTER_IV, 1);
                    killer.getTaskMasterManager().increase(Tasks.REVENANTS);
                    DailyTaskManager.increase(DailyTasks.REVENANTS, killer);
                }

                if (npc.def().name.equalsIgnoreCase("Alchemical Hydra")) {
                    killer.getTaskMasterManager().increase(Tasks.ALCHEMICAL_HYDRA);
                }

                if (npc.def().name.equalsIgnoreCase("Chaos Fanatic")) {
                    killer.getTaskMasterManager().increase(Tasks.CHAOS_FANATIC);
                }

                if (npc.def().name.equalsIgnoreCase("Corporeal Beast")) {
                    AchievementsManager.activate(killer, Achievements.CORPOREAL_CRITTER, 1);
                    DailyTaskManager.increase(DailyTasks.CORPOREAL_BEAST, killer);
                    killer.getTaskMasterManager().increase(Tasks.CORPOREAL_BEAST);
                }

                if (npc.def().name.equalsIgnoreCase("Crazy archaeologist")) {
                    killer.getTaskMasterManager().increase(Tasks.CRAZY_ARCHAEOLOGIST);
                }

                if (npc.def().name.equalsIgnoreCase("Demonic gorilla")) {
                    killer.getTaskMasterManager().increase(Tasks.DEMONIC_GORILLA);
                }

                if (npc.def().name.equalsIgnoreCase("King Black Dragon")) {
                    AchievementsManager.activate(killer, Achievements.DRAGON_SLAYER_II, 1);
                    AchievementsManager.activate(killer, Achievements.DRAGON_SLAYER_III, 1);
                    killer.getTaskMasterManager().increase(Tasks.KING_BLACK_DRAGON);

                    if (World.getWorld().rollDie(10, 1)) {
                        npc.respawns(false);//King black dragon can no longer spawn his ancient version spawns.
                        var ancientKingBlackDragon = new Npc(ANCIENT_KING_BLACK_DRAGON, npc.spawnTile()).respawns(false);
                        World.getWorld().getNpcs().add(ancientKingBlackDragon);
                    }
                }

                if (npc.id() == ANCIENT_KING_BLACK_DRAGON) {
                    AchievementsManager.activate(killer, Achievements.DRAGON_SLAYER_II, 1);
                    AchievementsManager.activate(killer, Achievements.DRAGON_SLAYER_III, 1);
                    killer.getTaskMasterManager().increase(Tasks.KING_BLACK_DRAGON);
                    DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);
                    if(!npc.ancientSpawn()) {
                        Chain.bound(null).runFn(30, () -> {
                            var kingBlackDragon = new Npc(KING_BLACK_DRAGON, npc.spawnTile());
                            World.getWorld().getNpcs().add(kingBlackDragon);
                        });
                    }
                }

                if (npc.def().name.equalsIgnoreCase("Lizardman shaman")) {
                    killer.getTaskMasterManager().increase(Tasks.LIZARDMAN_SHAMAN);
                }

                if (npc.def().name.equalsIgnoreCase("Thermonuclear smoke devil")) {
                    killer.getTaskMasterManager().increase(Tasks.THERMONUCLEAR_SMOKE_DEVIL);
                }

                if (npc.def().name.equalsIgnoreCase("Vet'ion")) {
                    killer.getTaskMasterManager().increase(Tasks.VETION);
                }

                if (npc.def().name.equalsIgnoreCase("Chaos Elemental")) {
                    killer.getTaskMasterManager().increase(Tasks.CHAOS_ELEMENTAL);
                    AchievementsManager.activate(killer, Achievements.ULTIMATE_CHAOS_I, 1);
                    AchievementsManager.activate(killer, Achievements.ULTIMATE_CHAOS_II, 1);
                    AchievementsManager.activate(killer, Achievements.ULTIMATE_CHAOS_III, 1);
                    DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);

                    if (World.getWorld().rollDie(10, 1)) {
                        npc.respawns(false);//Chaos elemental can no longer spawn his ancient version spawns.
                        var ancientChaosEle = new Npc(ANCIENT_CHAOS_ELEMENTAL, npc.spawnTile()).respawns(false);
                        World.getWorld().getNpcs().add(ancientChaosEle);
                    }
                }

                if (npc.id() == ANCIENT_CHAOS_ELEMENTAL) {
                    killer.getTaskMasterManager().increase(Tasks.CHAOS_ELEMENTAL);
                    AchievementsManager.activate(killer, Achievements.ULTIMATE_CHAOS_I, 1);
                    AchievementsManager.activate(killer, Achievements.ULTIMATE_CHAOS_II, 1);
                    AchievementsManager.activate(killer, Achievements.ULTIMATE_CHAOS_III, 1);
                    DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);

                    if(!npc.ancientSpawn()) {
                        Chain.bound(null).runFn(30, () -> {
                            var chaosElemental = new Npc(CHAOS_ELEMENTAL, npc.spawnTile());
                            World.getWorld().getNpcs().add(chaosElemental);
                        });
                    }
                }

                if (npc.def().name.contains("Zulrah")) {
                    killer.getTaskMasterManager().increase(Tasks.ZULRAH);
                    DailyTaskManager.increase(DailyTasks.ZULRAH, killer);
                }

                if (npc.def().name.equalsIgnoreCase("Vorkath")) {
                    killer.getTaskMasterManager().increase(Tasks.VORKATH);
                    DailyTaskManager.increase(DailyTasks.VORKATH, killer);
                }

                if (npc.def().name.equalsIgnoreCase("Zombies Champion") || npc.def().name.equalsIgnoreCase("Skotizo") || npc.def().name.equalsIgnoreCase("Tekton")) {
                    killer.getTaskMasterManager().increase(Tasks.WORLD_BOSS);
                }

                if (npc.def().name.equalsIgnoreCase("Kalphite Queen")) {
                    killer.getTaskMasterManager().increase(Tasks.KALPHITE_QUEEN);
                }

                if (npc.def().name.equalsIgnoreCase("Dagannoth Supreme") || npc.def().name.equalsIgnoreCase("Dagannoth Prime") || npc.def().name.equalsIgnoreCase("Dagannoth Rex")) {
                    AchievementsManager.activate(killer, Achievements.LORD_OF_THE_RINGS_I, 1);
                    AchievementsManager.activate(killer, Achievements.LORD_OF_THE_RINGS_II, 1);
                    killer.getTaskMasterManager().increase(Tasks.DAGANNOTH_KINGS);
                }

                if (npc.def().name.equalsIgnoreCase("Giant Mole")) {
                    AchievementsManager.activate(killer, Achievements.HOLEY_MOLEY_I, 1);
                    AchievementsManager.activate(killer, Achievements.HOLEY_MOLEY_II, 1);
                    AchievementsManager.activate(killer, Achievements.HOLEY_MOLEY_III, 1);
                    killer.getTaskMasterManager().increase(Tasks.GIANT_MOLE);
                }

                if (npc.def().name.equalsIgnoreCase("Barrelchest")) {
                    DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);

                    if (World.getWorld().rollDie(10, 1)) {
                        npc.respawns(false);//Barrelchest can no longer spawn his ancient version spawns.
                        var ancientBarrelchest = new Npc(ANCIENT_BARRELCHEST, npc.spawnTile()).respawns(false);
                        World.getWorld().getNpcs().add(ancientBarrelchest);
                    }
                }

                if (npc.id() == ANCIENT_BARRELCHEST) {
                    DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);

                    if(!npc.ancientSpawn()) {
                        Chain.bound(null).runFn(30, () -> {
                            var barrelchest = new Npc(BARRELCHEST_6342, npc.spawnTile());
                            World.getWorld().getNpcs().add(barrelchest);
                        });
                    }
                }

                Slayer.reward(killer, npc);
                SlayerPartner.reward(killer, npc);

                if (killer.getMinigame() != null) {//what handles the kill count in fight cave and other minigames
                    killer.getMinigame().killed(killer, npc);
                }

                // Check if the dead npc is a barrows brother. Award killcount.
                var isBarrowsBro = false;

                switch (npc.id()) {
                    case SKELETON, BLOODWORM, CRYPT_RAT, GIANT_CRYPT_RAT ->{
                        isBarrowsBro = true;
                    }
                    case DHAROK_THE_WRETCHED -> {
                        isBarrowsBro = true;
                        killer.putAttrib(DHAROK, 1);
                    }
                    case AHRIM_THE_BLIGHTED -> {
                        isBarrowsBro = true;
                        killer.putAttrib(AHRIM, 1);
                    }
                    case VERAC_THE_DEFILED -> {
                        isBarrowsBro = true;
                        killer.putAttrib(VERAC, 1);
                    }
                    case TORAG_THE_CORRUPTED -> {
                        isBarrowsBro = true;
                        killer.putAttrib(TORAG, 1);
                    }
                    case KARIL_THE_TAINTED -> {
                        isBarrowsBro = true;
                        killer.putAttrib(KARIL, 1);
                    }
                    case GUTHAN_THE_INFESTED -> {
                        isBarrowsBro = true;
                        killer.putAttrib(GUTHAN, 1);
                    }

                    case KrakenBoss.KRAKEN_NPCID -> {// Kraken boss transmogged KC
                        AchievementsManager.activate(killer, Achievements.SQUIDWARD_I, 1);
                        AchievementsManager.activate(killer, Achievements.SQUIDWARD_II, 1);
                        AchievementsManager.activate(killer, Achievements.SQUIDWARD_III, 1);
                        killer.getTaskMasterManager().increase(Tasks.KRAKEN);
                    }

                    case CORRUPTED_NECHRYARCH -> {
                        DailyTaskManager.increase(DailyTasks.CORRUPTED_NECHRYARCHS, killer);
                    }

                    case ADAMANT_DRAGON, ADAMANT_DRAGON_8090, RUNE_DRAGON, RUNE_DRAGON_8031, RUNE_DRAGON_8091 -> AchievementsManager.activate(killer, Achievements.DRAGON_SLAYER_IV, 1);

                    case CERBERUS, CERBERUS_5863, CERBERUS_5866 -> {
                        killer.getTaskMasterManager().increase(Tasks.CERBERUS);
                        AchievementsManager.activate(killer, Achievements.FLUFFY_I, 1);
                        AchievementsManager.activate(killer, Achievements.FLUFFY_II, 1);

                        if (World.getWorld().rollDie(superiorSpawnRoll, 1)) {
                            npc.respawns(false);//Cerberus can no longer spawn his superior spawns in 1 minute.
                            var kerberos = new Npc(KERBEROS, npc.spawnTile()).respawns(false);
                            World.getWorld().getNpcs().add(kerberos);
                        }
                    }

                    case KERBEROS -> Chain.bound(null).runFn(30, () -> {
                        var cerberus = new Npc(CERBERUS, npc.spawnTile());
                        World.getWorld().getNpcs().add(cerberus);
                    });

                    case KALPHITE_QUEEN_6501 -> {
                        AchievementsManager.activate(killer, Achievements.BUG_EXTERMINATOR_I, 1);
                        AchievementsManager.activate(killer, Achievements.BUG_EXTERMINATOR_II, 1);
                    }

                    case LIZARDMAN_SHAMAN_6767 -> {
                        AchievementsManager.activate(killer, Achievements.DR_CURT_CONNORS_I, 1);
                        AchievementsManager.activate(killer, Achievements.DR_CURT_CONNORS_II, 1);
                        AchievementsManager.activate(killer, Achievements.DR_CURT_CONNORS_III, 1);
                    }

                    case THERMONUCLEAR_SMOKE_DEVIL -> {
                        AchievementsManager.activate(killer, Achievements.TSJERNOBYL_I, 1);
                        AchievementsManager.activate(killer, Achievements.TSJERNOBYL_II, 1);
                        AchievementsManager.activate(killer, Achievements.TSJERNOBYL_III, 1);
                    }

                    case VETION, VETION_REBORN -> {
                        AchievementsManager.activate(killer, Achievements.VETION_I, 1);
                        AchievementsManager.activate(killer, Achievements.VETION_II, 1);
                        AchievementsManager.activate(killer, Achievements.VETION_III, 1);
                        DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);
                    }

                    case VENENATIS_6610 -> {
                        killer.getTaskMasterManager().increase(Tasks.VENENATIS);
                        AchievementsManager.activate(killer, Achievements.BABY_ARAGOG_I, 1);
                        AchievementsManager.activate(killer, Achievements.BABY_ARAGOG_II, 1);
                        AchievementsManager.activate(killer, Achievements.BABY_ARAGOG_III, 1);
                        DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);

                        if (World.getWorld().rollDie(superiorSpawnRoll, 1)) {
                            npc.respawns(false);//Venenatis can no longer spawn his superior spawns in 1 minute.
                            var arachne = new Npc(CustomNpcIdentifiers.ARACHNE, npc.spawnTile()).respawns(false);
                            World.getWorld().getNpcs().add(arachne);
                        }
                    }

                    case ARACHNE -> Chain.bound(null).runFn(30, () -> {
                        var venenatis = new Npc(VENENATIS_6610, npc.spawnTile());
                        World.getWorld().getNpcs().add(venenatis);
                    });

                    case CALLISTO_6609 -> {
                        killer.getTaskMasterManager().increase(Tasks.CALLISTO);
                        AchievementsManager.activate(killer, Achievements.BEAR_GRYLLS_I, 1);
                        AchievementsManager.activate(killer, Achievements.BEAR_GRYLLS_II, 1);
                        AchievementsManager.activate(killer, Achievements.BEAR_GRYLLS_III, 1);
                        DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);

                        if (World.getWorld().rollDie(superiorSpawnRoll, 1)) {
                            npc.respawns(false);//Callisto can no longer spawn his superior spawns in 1 minute.
                            var artio = new Npc(ARTIO, npc.spawnTile()).respawns(false);
                            World.getWorld().getNpcs().add(artio);
                        }
                    }

                    case ARTIO -> Chain.bound(null).runFn(30, () -> {
                        var callisto = new Npc(CALLISTO_6609, npc.spawnTile());
                        World.getWorld().getNpcs().add(callisto);
                    });

                    case REVENANT_DARK_BEAST -> {
                        if (World.getWorld().rollDie(ancientRevSpawnRoll, 1)) {
                            npc.respawns(false);//Rev dark beast can no longer spawn when we spawn his ancient version
                            var ancientDarkbeast = new Npc(ANCIENT_REVENANT_DARK_BEAST, npc.spawnTile()).respawns(false);
                            World.getWorld().getNpcs().add(ancientDarkbeast);
                        }
                    }

                    case ANCIENT_REVENANT_DARK_BEAST -> {
                        if(!npc.ancientSpawn()) {
                            Chain.bound(null).runFn(30, () -> {
                                var revDarkBeast = new Npc(REVENANT_DARK_BEAST, npc.spawnTile());
                                World.getWorld().getNpcs().add(revDarkBeast);
                            });
                        }
                    }

                    case REVENANT_ORK -> {
                        if (World.getWorld().rollDie(ancientRevSpawnRoll, 1)) {
                            npc.respawns(false);//Rev ork can no longer spawn when we spawn his ancient version
                            var ancientOrk = new Npc(ANCIENT_REVENANT_ORK, npc.spawnTile()).respawns(false);
                            World.getWorld().getNpcs().add(ancientOrk);
                        }
                    }

                    case ANCIENT_REVENANT_ORK -> {
                        if(!npc.ancientSpawn()) {
                            Chain.bound(null).runFn(30, () -> {
                                var revenantOrk = new Npc(REVENANT_ORK, npc.spawnTile());
                                World.getWorld().getNpcs().add(revenantOrk);
                            });
                        }
                    }

                    case REVENANT_CYCLOPS -> {
                        if (World.getWorld().rollDie(ancientRevSpawnRoll, 1)) {
                            npc.respawns(false);//Rev cyclops can no longer spawn when we spawn his ancient version
                            var ancientCyclops = new Npc(ANCIENT_REVENANT_CYCLOPS, npc.spawnTile()).respawns(false);
                            World.getWorld().getNpcs().add(ancientCyclops);
                        }
                    }

                    case ANCIENT_REVENANT_CYCLOPS -> {
                        if(!npc.ancientSpawn()) {
                            Chain.bound(null).runFn(30, () -> {
                                var revCyclops = new Npc(REVENANT_CYCLOPS, npc.spawnTile());
                                World.getWorld().getNpcs().add(revCyclops);
                            });
                        }
                    }

                    case REVENANT_DRAGON -> {
                        if (World.getWorld().rollDie(ancientRevSpawnRoll, 1)) {
                            npc.respawns(false);//Rev dragon can no longer spawn when we spawn his ancient version
                            var ancientDragon = new Npc(ANCIENT_REVENANT_DRAGON, npc.spawnTile()).respawns(false);
                            World.getWorld().getNpcs().add(ancientDragon);
                        }
                    }

                    case ANCIENT_REVENANT_DRAGON -> {
                        if(!npc.ancientSpawn()) {
                            Chain.bound(null).runFn(30, () -> {
                                var revDragon = new Npc(REVENANT_DRAGON, npc.spawnTile());
                                World.getWorld().getNpcs().add(revDragon);
                            });
                        }
                    }

                    case REVENANT_KNIGHT -> {
                        if (World.getWorld().rollDie(ancientRevSpawnRoll, 1)) {
                            npc.respawns(false);//Rev knight can no longer spawn when we spawn his ancient version
                            var ancientKnight = new Npc(ANCIENT_REVENANT_KNIGHT, npc.spawnTile()).respawns(false);
                            World.getWorld().getNpcs().add(ancientKnight);
                        }
                    }

                    case ANCIENT_REVENANT_KNIGHT -> {
                        if(!npc.ancientSpawn()) {
                            Chain.bound(null).runFn(30, () -> {
                                var revKnight = new Npc(REVENANT_KNIGHT, npc.spawnTile());
                                World.getWorld().getNpcs().add(revKnight);
                            });
                        }
                    }

                    case ZULRAH, ZULRAH_2043, ZULRAH_2044 -> {
                        AchievementsManager.activate(killer, Achievements.SNAKE_CHARMER_I, 1);
                        AchievementsManager.activate(killer, Achievements.SNAKE_CHARMER_II, 1);
                        AchievementsManager.activate(killer, Achievements.SNAKE_CHARMER_III, 1);
                    }

                    case VORKATH_8061 -> {
                        AchievementsManager.activate(killer, Achievements.VORKY_I, 1);
                        AchievementsManager.activate(killer, Achievements.VORKY_II, 1);
                        AchievementsManager.activate(killer, Achievements.VORKY_III, 1);
                    }

                    case BATTLE_MAGE, BATTLE_MAGE_1611, BATTLE_MAGE_1612 -> {
                        AchievementsManager.activate(killer, Achievements.MAGE_ARENA_I, 1);
                        AchievementsManager.activate(killer, Achievements.MAGE_ARENA_II, 1);
                        AchievementsManager.activate(killer, Achievements.MAGE_ARENA_III, 1);
                        AchievementsManager.activate(killer, Achievements.MAGE_ARENA_IV, 1);
                        DailyTaskManager.increase(DailyTasks.BATTLE_MAGE, killer);
                    }

                }

                if (isBarrowsBro) {
                    killer.clearAttrib(barrowsBroSpawned);
                    killer.putAttrib(BARROWS_MONSTER_KC, 1 + (int) killer.getAttribOr(BARROWS_MONSTER_KC, 0));
                    var newkc = killer.getAttribOr(BARROWS_MONSTER_KC, 0);
                    killer.getPacketSender().sendString(4536, "Kill Count: " + newkc);
                    killer.getPacketSender().sendEntityHintRemoval(false);
                }

                //Make sure spawns are killed on boss death
                if (npc.id() == SCORPIA) {
                    killer.getTaskMasterManager().increase(Tasks.SCORPIA);
                    npc.clearAttrib(AttributeKey.SCORPIA_GUARDIANS_SPAWNED);
                    AchievementsManager.activate(killer, Achievements.BARK_SCORPION_I, 1);
                    AchievementsManager.activate(killer, Achievements.BARK_SCORPION_II, 1);
                    AchievementsManager.activate(killer, Achievements.BARK_SCORPION_III, 1);
                    DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);
                    World.getWorld().getNpcs().forEachInArea(new Area(3219, 3248, 10329, 10353), n -> {
                        if (n.id() == SCORPIAS_GUARDIAN) {
                            World.getWorld().unregisterNpc(n);
                        }
                    });

                    if (World.getWorld().rollDie(superiorSpawnRoll, 1)) {
                        npc.respawns(false);//Cerberus can no longer spawn his superior spawns in 1 minute.
                        var skorpios = new Npc(SKORPIOS, npc.spawnTile()).respawns(false);
                        World.getWorld().getNpcs().add(skorpios);
                    }
                }

                if (npc.id() == SKORPIOS) {
                    World.getWorld().getNpcs().forEachInArea(new Area(3219, 3248, 10329, 10353), n -> {
                        if (n.id() == SCORPIAS_GUARDIAN) {
                            World.getWorld().unregisterNpc(n);
                        }
                    });

                    Chain.bound(null).runFn(30, () -> {
                        var scorpia = new Npc(SCORPIA, npc.spawnTile());
                        World.getWorld().getNpcs().add(scorpia);
                    });
                }

                //Do custom area deaths
                if (killer.getController() != null) {
                    killer.getController().defeated(killer, npc);
                }

                //Do bots death
                if (npc.getBotHandler() != null) {
                    npc.getBotHandler().onDeath(killer);
                }

                var killerOpp = killer.<Mob>getAttribOr(AttributeKey.LAST_DAMAGER, null);
                if (killer.<Integer>getAttribOr(AttributeKey.MULTIWAY_AREA, -1) == 0 && killerOpp != null && killerOpp == npc) { // Last fighting with this dead npc.
                    killer.clearAttrib(AttributeKey.LAST_WAS_ATTACKED_TIME); // Allow instant aggro from other npcs/players.
                }

                var done = false;
                for (MagicalAnimator.ArmourSets set : MagicalAnimator.ArmourSets.values()) {
                    if (!done && set.npc == npc.id()) {
                        done = true;
                        killer.getPacketSender().sendEntityHintRemoval(true);// remove hint arrow
                    }
                }
            }

            //Do death animation
            if (npc instanceof AlchemicalHydra) {
                npc.animate(8257);
                Chain.bound(null).runFn(2, () -> {
                    npc.transmog(8622);
                    npc.animate(8258);
                });
            } else if (npc instanceof Drake) {
                npc.animate(8277);
                Chain.bound(null).runFn(1, () -> {
                    npc.transmog(8613);
                    npc.animate(8278);
                });
            } else if (npc instanceof TzTokJad) {
                npc.graphic(453);
            } else {
                npc.animate(npc.combatInfo() != null ? npc.combatInfo().animations.death : -1);
            }

            // Death sound!
            if (killer != null) {
                if (npc.combatInfo() != null && npc.combatInfo().sounds != null) {
                    var sounds = npc.combatInfo().sounds.death;
                    if (sounds != null && sounds.length > 0) {
                        killer.sound(Utils.randomElement(sounds));
                    }
                }
            }

            int finalRespawnTimer = respawnTimer;
            Chain.bound(null).runFn(npc.combatInfo() != null ? npc.combatInfo().deathlen : 5, () -> {
                if (killer != null) {
                    //Do inferno minigame death here and fight caves

                    //Do death scripts
                    if (npc.id() == KRAKEN) {
                        KrakenBoss.onDeath(npc); //Kraken uses its own death script
                    }

                    if (npc.getCombatMethod() instanceof CommonCombatMethod) {
                        CommonCombatMethod commonCombatMethod = (CommonCombatMethod) npc.getCombatMethod();
                        commonCombatMethod.set(npc, killer);
                        commonCombatMethod.onDeath();
                    }

                    //Rock crabs
                    if (npc.id() == 101 || npc.id() == 103) {
                        switch (npc.id()) {
                            case 101 -> npc.transmog(101);
                            case 103 -> npc.transmog(103);
                        }
                        npc.walkRadius(0);
                    }

                    // so in java .. we dont have functions so we need to hardcode the id check
                    if (WildernessBossEvent.getINSTANCE().getActiveNpc().isPresent() &&
                        npc == WildernessBossEvent.getINSTANCE().getActiveNpc().get()) {
                        WildernessBossEvent.getINSTANCE().bossDeath(npc);
                    }

                    if(npc.id() == NEX) {
                        nexDrops(npc);
                    }

                    killer.getBossTimers().submit(npc.def().name, (int) killer.getCombat().getFightTimer().elapsed(TimeUnit.SECONDS), killer);

                    ScalarLootTable table = ScalarLootTable.forNPC(npc.id());

                    //Drop loot, but the first form of KQ, Runite golem and world bosses do not drop anything.
                    if (table != null && (npc.id() != KALPHITE_QUEEN_6500 && npc.id() != RUNITE_GOLEM && !npc.isWorldBoss() /*&& npc.id() != NEX*/)) {
                        boolean dropUnderPlayer = npc.id() == NpcIdentifiers.KRAKEN || npc.id() == NpcIdentifiers.CAVE_KRAKEN || npc.id() >= NpcIdentifiers.ZULRAH && npc.id() <= NpcIdentifiers.ZULRAH_2044 || npc.id() >= NpcIdentifiers.VORKATH_8059 && npc.id() <= NpcIdentifiers.VORKATH_8061;
                        boolean jad = npc.id() == TZTOKJAD;
                        boolean doubleDropsLampActive = (Integer) killer.getAttribOr(DOUBLE_DROP_LAMP_TICKS, 0) > 0;
                        boolean founderImp = killer.pet() != null && killer.pet().def().name.equalsIgnoreCase("Founder Imp");
                        boolean rolledDoubleDrop = World.getWorld().rollDie(10, 1);

                        Tile tile = jad ? new Tile(2438, 5169, 0) : dropUnderPlayer ? killer.tile() : npc.tile();

                        table.rollForLarransKey(npc, killer);
                        table.rollForKeyOfDrops(killer, npc);
                        table.rollForTotemBase(killer);
                        table.rollForTotemMiddle(killer);
                        table.rollForTotemTop(killer);

                        if (!customDrops.contains(npc.id())) {
                            table.getGuaranteedDrops().forEach(tableItem -> {
                                if (killer.inventory().contains(13116)) {
                                    int[] BONES = new int[]{526, 528, 530, 2859, 532, 10976, 10977, 3125, 534, 536, 4812,
                                        4834, 6812, 6729, 11943};
                                    for (int bone : BONES) {
                                        if (tableItem.convert().getId() == bone) {
                                            Bone bones = Bone.get(tableItem.convert().getId());
                                            if (bones != null)
                                                killer.skills().addXp(Skills.PRAYER, bones.xp);
                                        }
                                    }
                                } else {
                                    if (tableItem.min > 0) {
                                        // not fixed-amount drop, amount has a min/max amount randomly given
                                        Item dropped = new Item(tableItem.id, Utils.random(tableItem.min, tableItem.max));

                                        if(dropped.getId() == HWEEN_TOKENS) {
                                            if(WildernessArea.inWilderness(killer.tile())) {
                                                int extraTokens = dropped.getAmount() * 50 / 100;
                                                dropped.setAmount(dropped.copy().getAmount() + extraTokens);

                                                if(killer.getSkullType() != SkullType.NO_SKULL) {
                                                    dropped.setAmount(dropped.copy().getAmount() * 2);
                                                }
                                            }
                                        }
                                    if(dropped.getId() == 21820){
                                        if(killer.getEquipment().contains(BRACELET_OF_ETHEREUM)){
                                            int amtdropped = dropped.getAmount();
                                            int originalamount = killer.getAttribOr(AttributeKey.BRACELET_OF_ETHEREUM_CHARGES, 0);
                                           dropped = dropped.createWithId(-1);
                                            killer.putAttrib(BRACELET_OF_ETHEREUM_CHARGES,originalamount+amtdropped);

                                            killer.message("@blu@Absorbed "+amtdropped+" Revenant Ether.");

                                        }

                                    }
                                        if ((dropped.getId() == ItemIdentifiers.DRAGON_BONES || dropped.getId() == ItemIdentifiers.LAVA_DRAGON_BONES && killer.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.NOTED_DRAGON_BONES)) && WildernessArea.inWilderness(killer.tile())) {
                                            dropped = dropped.note();
                                        }

                                        if (killer.nifflerPetOut() && killer.nifflerCanStore() && dropped.getValue() > 0) {
                                            killer.nifflerStore(dropped);
                                        } else {
                                            GroundItemHandler.createGroundItem(new GroundItem(dropped, tile, killer));
                                        }
                                    } else {
                                        // fixed amount items
                                        if ((tableItem.convert().getId() == ItemIdentifiers.DRAGON_BONES || tableItem.convert().getId() == ItemIdentifiers.LAVA_DRAGON_BONES && killer.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.NOTED_DRAGON_BONES)) && WildernessArea.inWilderness(killer.tile())) {
                                            tableItem.convert().setId(tableItem.convert().note().getId());
                                        }

                                        if (killer.nifflerPetOut() && killer.nifflerCanStore() && tableItem.convert().getValue() > 0) {
                                            killer.nifflerStore(tableItem.convert());
                                        } else {
                                            GroundItemHandler.createGroundItem(new GroundItem(tableItem.convert(), tile, killer));
                                        }
                                    }
                                }
                            });
                        }

                        int dropRolls = npc.combatInfo().droprolls;

                        if (killer.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.DOUBLE_DROP_CHANCE) && World.getWorld().rollDie(100, 1)) {
                            dropRolls += 1;
                            killer.message("The Double drops perk grants you a second drop!");
                        }

                        for (int i = 0; i < dropRolls; i++) {
                            Item reward = table.randomItem(World.getWorld().random());
                            if (reward != null) {
                                boolean canDoubleDrop = doubleDropsLampActive || founderImp;
                                if (canDoubleDrop) {
                                    if (rolledDoubleDrop) {
                                        //Drop the item to the ground instead of editing the item instance
                                        GroundItem doubleDrop = new GroundItem(reward, tile, killer);

                                        if (killer.nifflerPetOut() && killer.nifflerCanStore() && reward.getValue() > 0) {
                                            killer.nifflerStore(doubleDrop.getItem());
                                        } else {
                                            GroundItemHandler.createGroundItem(doubleDrop);
                                        }
                                        killer.message("The double drop effect doubled your drop.");
                                    }
                                }

                                // bosses, find npc ID, find item ID
                                BOSSES.log(killer, npc.id(), reward);
                                RAIDS.log(killer, RAIDS_KEY, reward);
                                OTHER.log(killer, npc.id(), reward);

                                if ((reward.getId() == ItemIdentifiers.DRAGON_BONES || reward.getId() == ItemIdentifiers.LAVA_DRAGON_BONES && killer.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.NOTED_DRAGON_BONES)) && WildernessArea.inWilderness(killer.tile())) {
                                    reward = reward.note();
                                }

                                if (killer.nifflerPetOut() && killer.nifflerCanStore() && reward.getValue() > 0) {
                                    killer.nifflerStore(reward);
                                } else {
                                    GroundItemHandler.createGroundItem(new GroundItem(reward, tile, killer));
                                }

                                killer.getSlayerKey().drop(npc);
                                ServerAnnouncements.tryBroadcastDrop(killer, npc, reward);
                                npcDropLogs.log(NPC_DROPS, "Player " + killer.getUsername() + " got drop item " + reward.unnote().name());
                                Utils.sendDiscordInfoLog("Player " + killer.getUsername() + " got drop item " + reward.unnote().name(), "npcdrops");

                                // Corp beast drops are displayed to surrounding players.
                                if (npc.id() == 319) {
                                    Item finalReward = reward;
                                    World.getWorld().getPlayers().forEachInArea(new Area(2944, 4352, 3007, 4415), p -> {
                                        String amtString = finalReward.unnote().getAmount() == 1 ? finalReward.unnote().name() : "" + finalReward.getAmount() + " x " + finalReward.unnote().getAmount() + ".";
                                        p.message("<col=0B610B>" + killer.getUsername() + " received a drop: " + amtString);
                                    });
                                }
                            }
                        }

                        // Pets, anyone?! :)
                        Optional<Pet> pet = checkForPet(killer, table);
                        pet.ifPresent(value -> BOSSES.log(killer, npc.id(), new Item(value.item)));

                        //Only give BM when the npc is flagged as boss and we have the perk unlocked
                        if (npc.combatInfo().boss && killer.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.BLOOD_MONEY_FROM_KILLING_BOSSES)) {
                            int combat = def.combatlevel;

                            var amount = 0;
                            if (combat > 200) {
                                amount = Utils.random(350, 750);
                            } else {
                                amount = Utils.random(125, 350);
                            }

                            var blood_reaper = killer.hasPetOut("Blood Reaper pet");
                            if(blood_reaper) {
                                int extraBM = amount * 10 / 100;
                                amount += extraBM;
                            }

                            Item BM = new Item(BLOOD_MONEY, amount);

                            //Niffler should only pick up items of monsters and players that you've killed.
                            if(killer.nifflerPetOut() && killer.nifflerCanStore()) {
                                killer.nifflerStore(BM);
                            } else {
                                GroundItemHandler.createGroundItem(new GroundItem(BM, tile, killer));
                            }
                        }

                        treasure(killer, npc, tile);
                    }

                    // Custom drop tables
                    if (npc.combatInfo() != null && npc.combatInfo().scripts != null && npc.combatInfo().scripts.droptable_ != null) {
                        npc.combatInfo().scripts.droptable_.reward(npc, killer);
                    }

                    EarningPotential.increaseByKill(killer, npc);
                }

                // Post-death scripts

//                if (npc.id() == VESPULA_7531) {
//                 Vespula.death(npc);
//                    return;
//                }
                if (npc.id() == KALPHITE_QUEEN_6500) {
                    KalphiteQueenFirstForm.death(npc);
                    return;
                } else if (npc.id() == KALPHITE_QUEEN_6501) {
                    KalphiteQueenSecondForm.death(npc);
                }

                if (npc.id() == Phases.OLM_LEFT_HAND) {
                    if (killer == null) return;
                    Party party = killer.raidsParty;
                    if (party != null) {
                        party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getLeftHandObject(), 7370));
                        Chain.bound(null).runFn(2, () -> {
                            ObjectManager.addObj(new GameObject(29885, new Tile(3238, 5733, killer.tile().getZ()), 10, 1));
                            ObjectManager.addObj(new GameObject(29885, new Tile(3220, 5743, killer.tile().getZ()), 10, 3));
                        });
                        party.setLeftHandDead(true);
                    }
                }

                if (npc.id() == Phases.OLM_RIGHT_HAND) {
                    if (killer == null) return;
                    Party party = killer.raidsParty;
                    if (party != null) {
                        party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getRightHandObject(), 7352));
                        Chain.bound(null).runFn(2, () -> {
                            party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getRightHandObject(), 7353));
                        });
                        party.setRightHandDead(true);
                        party.setCanAttackLeftHand(true);
                    }
                }

                if (npc.id() == VETION_REBORN) {
                    npc.putAttrib(AttributeKey.VETION_REBORN_ACTIVE, false);
                    npc.clearAttrib(AttributeKey.VETION_HELLHOUND_SPAWNED);
                    npc.transmog(VETION);
                }
                if (npc.id() == NEX) {
                    npc.clearAttrib(AttributeKey.NEX_FUMUS_SPAWNED);
                    npc.clearAttrib(AttributeKey.NEX_UMBRA_SPAWNED);
                    npc.clearAttrib(AttributeKey.NEX_CRUOR_SPAWNED);
                    npc.clearAttrib(AttributeKey.NEX_GLACIES_SPAWNED);

                }

                TopPkers.SINGLETON.handleBosses(killer, npc.id());



                if (npc.def().name.equalsIgnoreCase("Alchemical hydra")) {
                    if (killer != null && killer.getAlchemicalHydraInstance() != null) {
                        killer.getAlchemicalHydraInstance().death(killer);//Do Alchemical hydra death
                    }
                }

                if (npc.id() == 6613) {
                    VetionMinion.death(npc); //Do Vetíon minion death
                }
                if (npc.id() == FUMUS) {
                    Fumus.death(npc); //Do fumus death
                }
                if (npc.id() == UMBRA) {
                    Umbra.death(npc); //Do umbra death
                }
                if (npc.id() == CRUOR) {
                    Cruor.death(npc); //Do cruor death
                }
                if (npc.id() == GLACIES) {
                    Glacies.death(npc); //Do glacies eath
                }
                if (npc.id() == VESPINE_SOLDIER) {
                    VespulaMinion.death(npc, killer); //Do vespula adds death
                }
                if (npc.id() == 6716 || npc.id() == 6723 || npc.id() == 7649) {
                    NechryarchDeathSpawn.death(npc); //Do death spawn death
                }

                if (npc.id() == NECHRYAEL || npc.id() == NECHRYAEL_11) {
                    new Nechryael().onDeath(npc);
                }

                Zulrah.death(killer, npc);

                if (npc.id() == CORPOREAL_BEAST) { // Corp beast
                    // Reset damage counter

                    Npc corp = npc.getAttribOr(AttributeKey.BOSS_OWNER, null);
                    if (corp != null) {
                        //Check for any minions.
                        List<Npc> minList = corp.getAttribOr(AttributeKey.MINION_LIST, null);
                        if (minList != null) {
                            minList.remove(npc);
                        }
                    }
                }

                //Forgot to say its ALL npcs, happens to bots, kraken any npc
//                if (killer != null) {
//                    if (npc.respawns() && !npc.isBot())
//                        killer.getPacketSender().sendEffectTimer((int) Utils.ticksToSeconds(finalRespawnTimer), EffectTimer.MONSTER_RESPAWN);
//                }

                deathReset(npc);
                if (npc.respawns()) {
                    npc.teleport(npc.spawnTile());
                    npc.hidden(true);
                    World.getWorld().unregisterNpc(npc);
                    // Remove from area..
                    if (npc.getController() != null) {
                        npc.getController().leave(npc);
                        npc.setController(null);
                    }

                    // Remove from instance..
                    //TODO

                    Chain.bound(null).runFn(finalRespawnTimer, () -> {
                        GwdLogic.onRespawn(npc);
                        respawn(npc);
                    });
                } else if (npc.id() != KALPHITE_QUEEN_6500) {
                    //System.out.println("herevespdeath");
                    npc.hidden(true);
                    World.getWorld().unregisterNpc(npc);
                }
            });
        } catch (Exception e) {
            logger.catching(e);
        }
    }

    private static void treasure(Player killer, Npc npc, Tile tile) {
        if (!killer.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.TREASURE_HUNT)) {
            return;
        }

        if(Slayer.creatureMatches(killer, npc.id())) {
            return;
        }

        int treasureCasketChance;
        if (killer.getMemberRights().isSponsorOrGreater(killer))
            treasureCasketChance = 95;
        else if (killer.getMemberRights().isVIPOrGreater(killer))
            treasureCasketChance = 100;
        else if (killer.getMemberRights().isLegendaryMemberOrGreater(killer))
            treasureCasketChance = 105;
        else if (killer.getMemberRights().isExtremeMemberOrGreater(killer))
            treasureCasketChance = 110;
        else if (killer.getMemberRights().isEliteMemberOrGreater(killer))
            treasureCasketChance = 115;
        else if (killer.getMemberRights().isSuperMemberOrGreater(killer))
            treasureCasketChance = 120;
        else if (killer.getMemberRights().isRegularMemberOrGreater(killer))
            treasureCasketChance = 125;
        else
            treasureCasketChance = 128;

        var reduction = treasureCasketChance * killer.masterCasketMemberBonus() / 100;
        treasureCasketChance -= reduction;

        if (World.getWorld().rollDie(killer.getPlayerRights().isDeveloperOrGreater(killer) && !GameServer.properties().production ? 1 : treasureCasketChance, 1)) {
            Item clueItem = new Item(TreasureRewardCaskets.MASTER_CASKET);
            killer.inventory().addOrDrop(clueItem);
            notification(killer, clueItem);
            killer.message("<col=0B610B>You have received a treasure casket drop!");
        }

        boolean inWilderness = WildernessArea.inWilderness(killer.tile());
        Item smallCasket = new Item(ItemIdentifiers.CASKET_7956);
        Item bigChest = new Item(CustomItemIdentifiers.BIG_CHEST);
        int combat = killer.skills().combatLevel();
        int mul;

        if ((killer.mode() == GameMode.TRAINED_ACCOUNT))
            mul = 2;
        else mul = 1;

        int chance;

        if (combat <= 10)
            chance = 1;
        else if (combat <= 20)
            chance = 2;
        else if (combat <= 80)
            chance = 3;
        else if (combat <= 120)
            chance = 4;
        else
            chance = 5;

        int regularOdds = 100;

        chance *= mul;

        //If the player is in the wilderness, they have an increased chance at a casket drop
        if ((npc.maxHp() > 20 || WildernessArea.inWilderness(killer.tile()))) {
            if (inWilderness && Utils.random(regularOdds - 15) < chance) {
                if (npc.combatInfo() != null && npc.combatInfo().boss && Utils.random(3) == 2) {
                    killer.message("<col=0B610B>You have received a Big chest drop!");
                    killer.inventory().addOrDrop(bigChest);
                    notification(killer, bigChest);
                } else {
                    killer.message("<col=0B610B>You have received a small casket drop!");
                    killer.inventory().addOrDrop(smallCasket);
                    notification(killer, smallCasket);
                }
            } else if (!inWilderness && Utils.random(regularOdds) < chance) {
                if (npc.combatInfo() != null && npc.combatInfo().boss && Utils.random(5) == 2) {
                    killer.message("<col=0B610B>You have received a Big chest drop!");
                    killer.inventory().addOrDrop(bigChest);
                    notification(killer, bigChest);
                } else {
                    killer.message("<col=0B610B>You have received a small casket drop!");
                    killer.inventory().addOrDrop(smallCasket);
                    notification(killer, smallCasket);
                }
            }
        }
    }

    /**
     * If you're resetting an Npc as if it were by death but not, for example maybe kraken tentacles which go back down to
     * the depths when the boss is killed.
     */
    public static void deathReset(Npc npc) {
        if (npc.id() != KALPHITE_QUEEN_6500) { // KQ first stage keeps damage onto stage 2!
            npc.getCombat().clearDamagers(); //Clear damagers
        }

        npc.clearAttrib(AttributeKey.TARGET);
        npc.clearAttrib(AttributeKey.LAST_ATTACKED_MAP);
        npc.putAttrib(AttributeKey.VENOM_TICKS, 0);
        npc.putAttrib(AttributeKey.POISON_TICKS, 0);
        npc.clearAttrib(VENOMED_BY);
    }

    public static void respawn(Npc npc) {
        World.getWorld().registerNpc(npc);
        if (npc.id() == KrakenBoss.KRAKEN_NPCID) {
            npc.transmog(KrakenBoss.KRAKEN_WHIRLPOOL);
            // Transmog kraken info after the drop table is done otherwise it'll look for the wrong table
            npc.combatInfo(World.getWorld().combatInfo(KrakenBoss.KRAKEN_WHIRLPOOL));
        }

        if (npc.id() == KrakenBoss.TENTACLE_WHIRLPOOL || npc.id() == NpcIdentifiers.ENORMOUS_TENTACLE) {
            Npc boss = npc.getAttrib(AttributeKey.BOSS_OWNER);
            if (boss != null && npc.dead()) {
                // only respawn minions if our boss is alive
                return;
            }
        }

        if (npc.id() == NpcIdentifiers.GARGOYLE) {
            Gargoyle.onDeath(npc);
        }

        if (npc.id() == NpcIdentifiers.VETION) {//Just do it again for extra safety
            npc.clearAttrib(AttributeKey.VETION_HELLHOUND_SPAWNED);
            npc.putAttrib(AttributeKey.VETION_REBORN_ACTIVE, false);
        }
        if (npc.id() == NpcIdentifiers.NEX) {//Just do it again for extra safety
            npc.clearAttrib(AttributeKey.NEX_FUMUS_SPAWNED);
            npc.clearAttrib(AttributeKey.NEX_UMBRA_SPAWNED);
            npc.clearAttrib(AttributeKey.NEX_CRUOR_SPAWNED);
            npc.clearAttrib(AttributeKey.NEX_GLACIES_SPAWNED);


        }
        if (npc.hidden()) { // not respawned yet. we do this check incase it was force-respawned by .. group spawning (gwd)
            deathReset(npc);
            npc.hidden(false);
            if (npc.combatInfo() != null) {
                if (npc.combatInfo().stats != null || npc.combatInfo().originalStats != null)
                    npc.combatInfo().stats = npc.combatInfo().originalStats.clone(); // Replenish all stats on this NPC.
                if (npc.combatInfo().bonuses != null || npc.combatInfo().originalBonuses != null)
                    npc.combatInfo().bonuses = npc.combatInfo().originalBonuses.clone(); // Replenish all stats on this NPC.
            }

            npc.hp(npc.maxHp(), 0); // Heal up to full hp
            npc.animate(-1); // Reset death animation
            npc.unlock();
            if (npc instanceof Drake) {
                npc.transmog(DRAKE_8612);
            }

            if (npc instanceof Wyrm) {
                npc.transmog(Wyrm.IDLE);
            }
        }
    }

    public static Optional<Pet> checkForPet(Player killer, ScalarLootTable table) {
        Optional<Pet> pet = table.rollForPet(killer);
        if (pet.isPresent()) {
            // Do we already own this pet?
            boolean caught = killer.isPetUnlocked(pet.get().varbit);

            // RS tries to add it as follower first. That only works if you don't have one.
            Npc currentPet = killer.pet();
            if (caught && pet.get().varbit != -1) {//Only applies to untradeable pets
                killer.message("You have a funny feeling like you would have been followed...");
            } else if (currentPet == null) {
                killer.message("You have a funny feeling like you're being followed.");
                PetAI.spawnPet(killer, pet.get(), false);
            } else {
                killer.inventory().addOrBank(new Item(pet.get().item));
                killer.message("You feel something weird sneaking into your backpack.");
            }

            if (!killer.isPetUnlocked(pet.get().varbit)) {
                if (pet.get().varbit != -1) { // -1 means tradeable pet
                    if (!killer.isPetUnlocked(pet.get().varbit)) {
                        killer.addUnlockedPet(pet.get().varbit);
                    }
                }
            }

            //Special collection log unlocks
            switch (pet.get()) {
                case CENTAUR_MALE -> RAIDS.log(killer, RAIDS_KEY, new Item(Pet.CENTAUR_MALE.item));
                case CENTAUR_FEMALE -> RAIDS.log(killer, RAIDS_KEY, new Item(Pet.CENTAUR_FEMALE.item));
                case DEMENTOR -> RAIDS.log(killer, RAIDS_KEY, new Item(Pet.DEMENTOR.item));
                case FLUFFY_JR -> RAIDS.log(killer, RAIDS_KEY, new Item(Pet.FLUFFY_JR.item));
                case FENRIR_GREYBACK_JR -> RAIDS.log(killer, RAIDS_KEY, new Item(Pet.FENRIR_GREYBACK_JR.item));
                case SKELETON_HELLHOUND_PET -> RAIDS.log(killer, KILLER, new Item(Pet.SKELETON_HELLHOUND_PET.item));
            }

            npcDropLogs.log(NPC_DROPS, "Player " + killer.getUsername() + " got pet " + new Item(pet.get().item).name());
            World.getWorld().sendWorldMessage("<img=1081> <col=844e0d>" + killer.getUsername() + " has received a: " + new Item(pet.get().item).name() + ".");
        }
        return pet;
    }

    public static void notification(Player killer, Item drop) {
        Item loot = drop.unnote();
        //TODO: implement these
        // Enabled? Untradable buttons are only enabled if the threshold is enabled. Can't have one without the other.
        boolean notifications_enabled = killer.getAttribOr(AttributeKey.ENABLE_LOOT_NOTIFICATIONS_BUTTONS, false);
        boolean untrade_notifications = killer.getAttribOr(AttributeKey.UNTRADABLE_LOOT_NOTIFICATIONS, false);
        int lootDropThresholdValue = killer.getAttribOr(AttributeKey.LOOT_DROP_THRESHOLD_VALUE, 0);
        if (notifications_enabled) {
            if (loot.rawtradable()) {
                if (untrade_notifications) {
                    killer.message("Untradable drop: " + loot.getAmount() + " x <col=cc0000>" + loot.name() + "</col>.");
                }
            } else if (loot.getValue() >= lootDropThresholdValue) {
                killer.message("Valuable drop: " + loot.getAmount() + " x <col=cc0000>" + loot.name() + "</col> (" + loot.getValue() * loot.getAmount() + "coins).");
            }
        }
    }
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("mm:ss");
    private static void nexDrops(Mob mob) {
        mob.getCombat().getDamageMap().forEach((key, hits) -> {
            Player player = (Player) key;
            player.message(Color.RED.wrap("You've dealt " + hits.getDamage() + " damage to Nex!"));
            // Only people nearby are rewarded. This is to avoid people 'poking' the boss to do some damage
            // without really risking being there.
            if (mob.tile().isWithinDistance(player.tile(),30) && hits.getDamage() >= 500) {
                if(mob instanceof Npc) {
                    player.message("You received a drop roll from the table for dealing at least 500 damage!");
                    Npc npc = mob.getAsNpc();

                    //Always log kill timer
                    player.completeNex();
                    player.getBossTimers().submit(npc.def().name, (int) player.getCombat().getFightTimer().elapsed(TimeUnit.SECONDS), player);


                    //Always increase kill counts
                  //  player.getBossKillLog().addKill(npc);

                    //Always drop random BM
             //       GroundItemHandler.createGroundItem(new GroundItem(new Item(BLOOD_MONEY, World.getWorld().random(500, 5_500)), npc.tile(), player));

                    //Random drop from the table
//                    ScalarLootTable table = ScalarLootTable.forNPC(npc.id());
//                    if (table != null) {
//                        Item reward = table.randomItem(World.getWorld().random());
//                        if (reward != null) {
//
//                            // bosses, find npc ID, find item ID
//                            BOSSES.log(player, npc.id(), reward);
//
//                            //Niffler doesn't loot world The Nightmare loot
//                            GroundItemHandler.createGroundItem(new GroundItem(reward, npc.tile(), player));
//                            ServerAnnouncements.tryBroadcastDrop(player, npc, reward);
//
//                            Utils.sendDiscordInfoLog("Player " + player.getUsername() + " got drop item " + reward, "npcdrops");
//                        }
//                    }
                }
            }
        });
    }
}
