package com.ferox.game;

import com.ferox.game.task.TaskManager;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.commands.impl.kotlin.MiscKotlin;
import com.ferox.util.NpcPerformance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

import static org.apache.logging.log4j.util.Unbox.box;

/**
 * Formerly known as TimesCx.
 *
 */
public class TimesCycle {

    private static final Logger logger = LogManager.getLogger(TimesCycle.class);

    public static class WorldPro {
        public long player_process, npc_process, player_npc_updating;
    }

    public WorldPro wp;

    public TimesCycle() {
        wp = new WorldPro();
    }

    //Objects don't use processing in this server.
    public long login, objs, tasks, world, gitems, total;

    public String COMPUTED_MSG = "";
    public static int lastComputedMsgTick = -1;
    public static boolean APPEND_WORLDINFO = false;
    public static boolean BENCHMARKING_ENABLED = false;

    public void computeAnd(Consumer<TimesCycle> c) {
        if (!BENCHMARKING_ENABLED) return;

        // already computed this tick
        if (lastComputedMsgTick == GameEngine.gameTicksIncrementor) {
            c.accept(this);
            return;
        }

        long totalMem = Runtime.getRuntime().totalMemory();
        long freeMem = Runtime.getRuntime().freeMemory();
        long maxMem = Runtime.getRuntime().maxMemory();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%sms cycle. Average: %sms. players: %s, npcs: %s, tasks: %s, objects: %s, Memory usage: %sMB/%sMB. ",
            box(total), box(Math.max(1, GameEngine.totalCycleTime) / Math.max(1, GameEngine.gameTicksIncrementor)), box(World.getWorld().getPlayers().size()), box(World.getWorld().getNpcs().size()), box(TaskManager.getTaskAmount()), box(World.getWorld().getSpawnedObjs().size()), box((totalMem - freeMem) / 1024 / 1024), box(totalMem / 1024 / 1024)
        ));
        sb.append("[");
        if (login > 0) sb.append("login:"+login+" ");
        if (objs > 0) sb.append("objs:"+objs+" ");
        if (tasks > 0) sb.append("tasks:"+tasks+" ");
        if (gitems > 0) sb.append("gitems:"+ gitems +" ");
        sb.append("]");
        sb.append(String.format("[npc process:%s, player process:%s, gpi:%s][cycle #%s] ", box(wp.npc_process), box(wp.player_process), box(wp.player_npc_updating), box(GameEngine.gameTicksIncrementor)));

        StringBuilder sb2 = new StringBuilder();
        if ((int)(1. * NpcPerformance.npcA / 1_000_000.) > 0) sb2.append(String.format("prepath:%s ms, ", (int)(1. * NpcPerformance.npcA / 1_000_000.)));
        if ((int)(1. * NpcPerformance.cumeNpcB / 1_000_000.) > 0) sb2.append(String.format("route:%s ms, ", (int)(1. * NpcPerformance.cumeNpcB / 1_000_000.)));
        if ((int)(1. * NpcPerformance.cumeNpcC / 1_000_000.) > 0) sb2.append(String.format("homewalk:%s ms, ", (int)(1. * NpcPerformance.cumeNpcC / 1_000_000.)));
        if ((int)(1. * NpcPerformance.cumeNpcD / 1_000_000.) > 0) sb2.append(String.format("cb:%s ms, ", (int)(1. * NpcPerformance.cumeNpcD / 1_000_000.)));
        //if ((int)(1. * NpcPerformance.cumeNpcE / 1_000_000.) > 0) sb2.append(String.format("core:%s ms, ", (int)(1. * NpcPerformance.cumeNpcE / 1_000_000.)));
        if ((int)(1. * NpcPerformance.F / 1_000_000.) > 0) sb2.append(String.format("tasks:%s ms, ", (int)(1. * NpcPerformance.F / 1_000_000.)));
        if ((int)(1. * NpcPerformance.G / 1_000_000.) > 0) sb2.append(String.format("timers:%s ms, ", (int)(1. * NpcPerformance.G / 1_000_000.)));
        if ((int)(1. * NpcPerformance.H / 1_000_000.) > 0) sb2.append(String.format("agro+retreat:%s ms, ", (int)(1. * NpcPerformance.H / 1_000_000.)));
        if (sb2.toString().length() > 0)
            sb.append("npcs: "+ sb2);

        if (APPEND_WORLDINFO)
            sb.append(". World: "+World.getWorld().benchmark.toString());

        final String message = sb.toString();
        COMPUTED_MSG = message;
        lastComputedMsgTick = GameEngine.gameTicksIncrementor;

        if (tasks > 100)
            MiscKotlin.INSTANCE.runningTasks();

        //Change to logger.info to view this in the IntelliJ console.
        //if (GameServer.properties().displayCycleTime && (!GameServer.properties().linuxOnlyDisplayCycleTime || GameServer.isLinux())) {
            c.accept(this);
        //}
    }
}
