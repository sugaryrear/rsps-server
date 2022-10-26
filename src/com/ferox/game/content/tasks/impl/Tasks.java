package com.ferox.game.content.tasks.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Patrick van Elderen | April, 08, 2021, 21:53
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public enum Tasks {

    //#Default
    NONE(true,false,false,0,"", ""),
    //#PVP tasks
    WEAR_TORAGS_TASK(true,false,false,1,"Kill your opponent while wearing<br>Torag's armour.", "- Must have out a Zulrah pet...<br>while completing this task."),
    WEAR_FULL_DH_TASK(true,false,false,1,"Kill your opponent while wearing<br>Dharok's armour.", "- Must kill the player while holding<br>the greataxe."),
    KILL_WITH_20K_BM_RISK(true,false,false,1,"Kill your opponent while risking<br>20k+ BM.", "- You can't use the 'Protect item' prayer."),
    KILL_WITH_DRAGON_SCIMITAR_OR(true,false,false,1,"Kill your opponent while Dragon<br>Scimitar(or) is your main weapon.", ""),
    KILL_WITH_INQUISITORS_MACE(true,false,false,1,"Kill your opponent with an...<br>Inquisitor's mace.", ""),
    KILL_WITHOUT_HEAD_BODY_AND_LEGS(true,false,false,1,"Kill your opponent without...<br>wearing any head, body and...<br>legs.", ""),
    KILL_WITH_AN_IMBUED_SLAYER_HELM_EQUIPED(true,false,false,1,"Kill your opponent while...<br>equipping any imbued slayer helm.", ""),
    KILL_WITHOUT_RING_AMULET_AND_GLOVES(true,false,false,1,"Kill your opponent without...<br>equipping any rings, amulets...<br>and gloves.", ""),
    KILL_WEARING_FULL_OBSIDIAN(true,false,false,1,"Kill your opponent while wearing<br>full Obsidian armour.", ""),
    KILL_WITHOUT_BOOSTED_STATS(true,false,false,1,"Kill your opponent without...<br>boosted stats (such as potions).", ""),

    //#Skilling tasks
    BONES_ON_ALTAR(false,true,false,125,"Pledge 100 bones on any altar.","- None"),
    CRAFT_DEATH_RUNES(false,true,false,100,"Craft 100 death runes.","- Level 65 Runecrafting"),
    WILDERNESS_COURSE(false,true,false,10,"Complete 10 laps at the Wilderness agility course.","- Level 50 Agility"),
    MAKE_SUPER_COMBAT_POTIONS(false,true,false,10,"Make 10 super combat potions.","- Level 90 Herblore"),
    STEAL_FROM_SCIMITAR_STALL(false,true,false,15,"Steal from the scimitar stall 15 times.","- Level 65 Thieving"),
    CRAFT_DRAGONSTONES(false,true,false,20,"Craft 20 dragonstones.","- Level 55 Crafting"),
    MAGIC_SHORTBOW(false,true,false,20,"Make 20 magic bows.","- Level 80 Fletching"),
    COMPLETE_SLAYER_TASKS(false,true,false,2,"Complete 2 slayer tasks.","- None"),
    BLACK_CHINCHOMPAS(false,true,false,20,"Catch 20 black chinchompas.","- Level 73 Hunter"),
    MINE_RUNITE_ORE(false,true,false,28,"Mine 28 Runite ores.","- Level 85 Mining"),
    MAKE_ADAMANT_PLATEBODY(false,true,false,15,"Create 15 Adamant platebodies.","- Level 88 Smithing"),
    CATCH_SHARKS(false,true,false,28,"Catch 28 sharks.","- Level 76 Fishing"),
    COOK_SHARKS(false,true,false,28,"Cook 28 sharks.","- Level 80 Cooking"),
    BURN_MAGIC_LOGS(false,true,false,100,"Burn 100 magic logs.","- Level 75 Firemaking"),
    CUT_MAGIC_TREES(false,true,false,28,"Cut down 28 Magic logs.","- Level 75 Woodcutting"),
    CUT_YEW_TREES(false,true,false,28,"Cut down 28 Yew logs.","- Level 60 Woodcutting"),
    PLANT_TORSTOL_SEED(false,true,false,4,"Plant 4 Torstol seeds.","- Level 85 Farming"),

    //#PVM tasks
    REVENANTS(false,false,true, 20, "Kill 20 Revenants.","- None"),
    DRAGONS(false,false,true, 20,"Kill 20 Dragons.", "- None"),
    CALLISTO(false,false,true,2,"Kill Callisto 2 times.","- None"),
    CERBERUS(false,false,true,2,"Kill Cerberus 2 times.","- None"),
    CHAOS_FANATIC(false,false,true,2,"Kill Chaos Fanatic 2 times.","- None"),
    CORPOREAL_BEAST(false,false,true,2,"Kill the Corporal Beast 2 times.","- None"),
    CRAZY_ARCHAEOLOGIST(false,false,true,2,"Kill the Crazy Archaeologist 2 times.","- None"),
    DEMONIC_GORILLA(false,false,true,10,"Kill 10 Demonic Gorillas.","- None"),
    KING_BLACK_DRAGON(false,false,true,2,"Kill the King Black Dragon 2 times.","- None"),
    KRAKEN(false,false,true,2,"Kill 2 Kraken.","- None"),
    LIZARDMAN_SHAMAN(false,false,true,2,"Kill 2 Lizardman Shamans.","- None"),
    THERMONUCLEAR_SMOKE_DEVIL(false,false,true,5,"Kill 5 Thermonuclear smoke devils.","- None"),
    VENENATIS(false,false,true,2,"Kill Venenatis 2 times.","- None"),
    VETION(false,false,true,2,"Kill Vet'ion 2 times.","- None"),
    SCORPIA(false,false,true,2,"Kill Scorpia 2 times.","- None"),
    CHAOS_ELEMENTAL(false,false,true,2,"Kill 2 Chaos Elementals.","- None"),
    ZULRAH(false,false,true,2,"Kill Zulrah 2 times.","- None"),
    VORKATH(false,false,true,2,"Kill Vorkath 2 times.","- None"),
    WORLD_BOSS(false,false,true,2,"Kill any world boss 2 times.","- None"),
    KALPHITE_QUEEN(false,false,true,3,"Kill the Kalphite Queen 3 times.","- None"),
    DAGANNOTH_KINGS(false,false,true,3,"Kill 3 Dagannoth Kings.","- None"),
    GIANT_MOLE(false,false,true,5,"Kill 5 Giant Moles.","- None"),
    ALCHEMICAL_HYDRA(false,false,true,3,"Kill 3 Alchemical Hydras.","- None"),
    ;

    private final boolean pvpTask;
    private final boolean skillingTask;
    private final boolean pvmTask;
    private final int taskAmount;
    private final String task;
    private final String[] taskRequirements;

    public boolean isPvpTask() {
        return pvpTask;
    }

    public boolean isSkillingTask() {
        return skillingTask;
    }

    public boolean isPvmTask() {
        return pvmTask;
    }

    public int getTaskAmount() {
        return taskAmount;
    }

    public String task() {
        return task;
    }

    public String[] getTaskRequirements() {
        return taskRequirements;
    }

    Tasks(boolean pvpTask, boolean skillingTask, boolean pvmTask, int taskAmount, String task, String... requirements) {
        this.pvpTask = pvpTask;
        this.skillingTask = skillingTask;
        this.pvmTask = pvmTask;
        this.taskAmount = taskAmount;
        this.task = task;
        this.taskRequirements = requirements;
    }

    /**
     * Picks a random PVP task from the Tasks enum.
     */
    public static Tasks randomPVPTask() {
        List<Tasks> tasks = Arrays.stream(Tasks.values()).filter(task -> task != NONE && !task.skillingTask && !task.pvmTask).collect(Collectors.toList());
        Collections.shuffle(tasks);
        return tasks.get(0);
    }

    /**
     * Picks a random Skilling task from the Tasks enum.
     */
    public static Tasks randomSkillingTask() {
        List<Tasks> tasks = Arrays.stream(Tasks.values()).filter(task -> task != NONE && !task.pvpTask && !task.pvmTask).collect(Collectors.toList());
        Collections.shuffle(tasks);
        return tasks.get(0);
    }

    /**
     * Picks a random PVM task from the Tasks enum.
     */
    public static Tasks randomPVMTask() {
        List<Tasks> tasks = Arrays.stream(Tasks.values()).filter(task -> task != NONE && !task.skillingTask && !task.pvpTask).collect(Collectors.toList());
        Collections.shuffle(tasks);
        return tasks.get(0);
    }
}
