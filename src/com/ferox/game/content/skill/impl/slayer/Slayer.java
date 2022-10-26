package com.ferox.game.content.skill.impl.slayer;

import com.ferox.game.content.daily_tasks.DailyTaskManager;
import com.ferox.game.content.daily_tasks.DailyTasks;
import com.ferox.game.content.skill.impl.slayer.master.SlayerMaster;
import com.ferox.game.content.skill.impl.slayer.slayer_task.SlayerCreature;
import com.ferox.game.content.skill.impl.slayer.superior_slayer.SuperiorSlayer;
import com.ferox.game.content.tasks.impl.Tasks;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.GameMode;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.QuestTab;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.ferox.game.world.entity.mob.player.QuestTab.InfoTab.*;
import static com.ferox.util.ItemIdentifiers.BLOOD_MONEY;

/**
 * @author PVE
 * @Since juli 20, 2020
 */
public class Slayer {

    private static final Logger logger = LogManager.getLogger(Slayer.class);

    private static List<SlayerMaster> masters = new ArrayList<>();

    public static SlayerMaster master(int npc) {
        for (SlayerMaster master : masters) {
            if (master.npcId == npc) {
                return master;
            }
        }
        return null;
    }

    public static int findIdByMaster(int npc) {
        for (SlayerMaster master : masters) {
            if (master.npcId == npc) {
                return master.id;
            }
        }
        return 0;
    }

    public static SlayerMaster lookup(int id) {
        for (SlayerMaster master : masters) {
            if (master.id == id) {
                return master;
            }
        }
        return null;
    }

    public void loadMasters() {
        long start = System.currentTimeMillis();
        // Load all masters from the json file
        masters = Utils.jsonArrayToList(Paths.get("data", "def", "slayermasters.json"), SlayerMaster[].class);

        if (masters == null) return;
        // Verify integrity, make sure matches are made.
        masters.forEach(master -> master.defs.forEach(taskdef -> {
            if (taskdef != null) {
                if (SlayerCreature.lookup(taskdef.getCreatureUid()) == null) {
                    throw new RuntimeException("could not load slayer task def " + taskdef.getCreatureUid() + " could not resolve uid; " + master.npcId);
                }
            }
        }));
        long elapsed = System.currentTimeMillis() - start;
        logger.info("Loaded slayer masters for {}. It took {}ms.", "./data/def/slayermasters.json", elapsed);
    }

    public static boolean creatureMatches(Player player, int id) {
        SlayerCreature task = SlayerCreature.lookup(player.getAttribOr(AttributeKey.SLAYER_TASK_ID, 0));
        return task != null && task.matches(id);
    }

    public static String taskName(int id) {
        return SlayerCreature.lookup(id) != null ? Utils.formatEnum(SlayerCreature.lookup(id).name()) : "None";
    }

    public static void displayCurrentAssignment(Player player) {
        String name = taskName(player.getAttribOr(AttributeKey.SLAYER_TASK_ID, 0));
        int num = player.getAttribOr(AttributeKey.SLAYER_TASK_AMT, 0);

        if (num == 0) {
            player.getPacketSender().sendString(63208, "None");
        } else {
            player.getPacketSender().sendString(63208, "" + num + " x " + name);
        }
    }

    public static void cancelTask(Player player, boolean adminCancel) {
        if (adminCancel) {
            player.putAttrib(AttributeKey.SLAYER_TASK_ID, 0);
            player.putAttrib(AttributeKey.SLAYER_TASK_AMT, 0);
            player.getPacketSender().sendString(SLAYER_TASK.childId, QuestTab.InfoTab.INFO_TAB.get(SLAYER_TASK.childId).fetchLineData(player));
            return;
        }

        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Would you like to reset your task?", "Yes.", "No.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if (isPhase(0)) {
                    if (option == 1) {
                        send(DialogueType.OPTION, "Reset slayer task with BM or Slayer points?", "BM. (5.000)", "Slayer Points. (10)");
                        setPhase(1);
                    } else {
                        stop();
                    }
                } else if (isPhase(1)) {
                    if (option == 1) {
                        boolean canReset = false;
                        int resetAmount = 5000;
                        int bmInInventory = player.inventory().count(BLOOD_MONEY);
                        if (bmInInventory > 0) {
                            if (bmInInventory >= resetAmount) {
                                canReset = true;
                                player.inventory().remove(BLOOD_MONEY, resetAmount);
                            }
                        }

                        if (!canReset) {
                            player.message("You do not have enough BM to do this.");
                            stop();
                            return;
                        }
                        player.putAttrib(AttributeKey.SLAYER_TASK_ID, 0);
                        player.putAttrib(AttributeKey.SLAYER_TASK_AMT, 0);
                        player.putAttrib(AttributeKey.SLAYER_TASK_SPREE, 0);
                        player.getPacketSender().sendString(SLAYER_TASK.childId, QuestTab.InfoTab.INFO_TAB.get(SLAYER_TASK.childId).fetchLineData(player));
                        player.getPacketSender().sendString(TASK_STREAK.childId, QuestTab.InfoTab.INFO_TAB.get(TASK_STREAK.childId).fetchLineData(player));
                        Slayer.displayCurrentAssignment(player);
                        player.message("You have successfully cancelled your task.");
                    } else {
                        int pts = player.getAttribOr(AttributeKey.SLAYER_REWARD_POINTS, 0);
                        int required = 10;

                        if (10 > pts) {
                            player.message("You need " + required + " points to cancel your task.");
                        } else {
                            player.putAttrib(AttributeKey.SLAYER_TASK_ID, 0);
                            player.putAttrib(AttributeKey.SLAYER_TASK_AMT, 0);
                            player.putAttrib(AttributeKey.SLAYER_TASK_SPREE, 0);
                            player.getPacketSender().sendString(TASK_STREAK.childId, QuestTab.InfoTab.INFO_TAB.get(TASK_STREAK.childId).fetchLineData(player));
                            player.getPacketSender().sendString(SLAYER_TASK.childId, QuestTab.InfoTab.INFO_TAB.get(SLAYER_TASK.childId).fetchLineData(player));
                            player.putAttrib(AttributeKey.SLAYER_REWARD_POINTS, pts - required);
                            player.getPacketSender().sendString(SLAYER_POINTS.childId, QuestTab.InfoTab.INFO_TAB.get(SLAYER_POINTS.childId).fetchLineData(player));
                            Slayer.displayCurrentAssignment(player);
                            player.message("You have successfully cancelled your task.");
                        }
                    }
                    stop();
                }
            }
        });
    }

    public static void reward(Player killer, Npc npc) {
        if (killer.slayerTaskAmount() > 0) {

            // Check our task. Decrease. Reward. leggo
            int task = killer.getAttribOr(AttributeKey.SLAYER_TASK_ID, 0);
            int amt = killer.getAttribOr(AttributeKey.SLAYER_TASK_AMT, 0);

            if (task > 0) {
                // Resolve taskdef
                SlayerCreature taskdef = SlayerCreature.lookup(task);
                if (taskdef != null && taskdef.matches(npc.id())) {
                    //Making sure that we have a fallback exp drop
                    killer.skills().addXp(Skills.SLAYER, npc.combatInfo().slayerxp != 0 ? npc.combatInfo().slayerxp : npc.maxHp());
                    killer.putAttrib(AttributeKey.SLAYER_TASK_AMT, amt - 1);
                    amt -= 1;

                    //Update quest tab
                    killer.getPacketSender().sendString(SLAYER_TASK.childId, QuestTab.InfoTab.INFO_TAB.get(SLAYER_TASK.childId).fetchLineData(killer));

                    // Update the slayer interface with the proper assignment amt
                    Slayer.displayCurrentAssignment(killer);

                    // Finished?
                    if (amt == 0) {

                        // Give points
                        var doublePointsUnlocked = killer.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.DOUBLE_SLAYER_POINTS);
                        var weekendBonus = World.getWorld().slayerRewardPointsMultiplier > 1;
                        var trainedAccount = killer.mode() == GameMode.TRAINED_ACCOUNT;
                        var darklordAccount = killer.mode() == GameMode.DARK_LORD;
                        int base = 30;

                        if(taskdef.bossTask) {
                            base += 5;
                        }

                        //If you have the double perk unlocked base * 2
                        if (doublePointsUnlocked) {
                            base *= 2;
                        }

                        //Trained accounts get + 30 slayer points on the total points reward
                        if (trainedAccount) {
                            base += 30;
                        }

                        //Dark lord accounts get + 40 slayer points on the total points reward
                        if (darklordAccount) {
                            base += 40;
                        }

                        //Legendary account bonus
                        if(killer.getMemberRights().isLegendaryMemberOrGreater(killer)) {
                            base += 25;
                        }

                        //Weekend bonus
                        if (weekendBonus) {
                            base += 25;
                        }

                        int spree = (int) killer.getAttribOr(AttributeKey.SLAYER_TASK_SPREE, 0) + 1;

                        if (spree % 1000 == 0) {
                            base += 1500;
                            killer.message("<col=7F00FF>+1500 bonus slayer reward points, for having a 1000 tasks streak.");
                        } else if (spree % 250 == 0) {
                            base += 1050;
                            killer.message("<col=7F00FF>+1050 bonus slayer reward points, for having a 250 tasks streak.");
                        } else if (spree % 100 == 0) {
                            base += 750;
                            killer.message("<col=7F00FF>+750 bonus slayer reward points, for having a 100 tasks streak.");
                        } else if (spree % 50 == 0) {
                            base += 450;
                            killer.message("<col=7F00FF>+450 bonus slayer reward points, for having a 50 tasks streak.");
                        } else if (spree % 10 == 0) {
                            base += 150;
                            killer.message("<col=7F00FF>+150 bonus slayer reward points, for having a 10 tasks streak.");
                        }

                        var slayerRewardPoints = killer.<Integer>getAttribOr(AttributeKey.SLAYER_REWARD_POINTS,0) + base;
                        killer.putAttrib(AttributeKey.SLAYER_REWARD_POINTS, slayerRewardPoints);

                        killer.message("<col=7F00FF>You've completed " + spree + " tasks in a row and received " + base + " points; return to a Slayer Master.");
                        killer.getPacketSender().sendString(SLAYER_POINTS.childId, QuestTab.InfoTab.INFO_TAB.get(SLAYER_POINTS.childId).fetchLineData(killer));

                        //TODO achievements here
                        killer.getTaskMasterManager().increase(Tasks.COMPLETE_SLAYER_TASKS);
                        DailyTaskManager.increase(DailyTasks.SLAYER, killer);

                        killer.putAttrib(AttributeKey.SLAYER_TASK_SPREE, spree);
                        killer.putAttrib(AttributeKey.COMPLETED_SLAYER_TASKS, (int) killer.getAttribOr(AttributeKey.COMPLETED_SLAYER_TASKS, 0) + 1);
                    } else {
                        // Chance to spawn a superior slayer one if unlocked.
                        SuperiorSlayer.trySpawn(killer, taskdef, npc);
                    }
                }
            }
        }
    }

}
