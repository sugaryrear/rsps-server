package com.ferox.game.content.announcements;

import com.ferox.game.content.kill_logs.BossKillLog;
import com.ferox.game.content.kill_logs.SlayerKillLog;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.util.Utils;

import java.util.Arrays;

public class ServerAnnouncements {

    public static void tryBroadcastDrop(Player player, Npc npc, Item item) {
        for (BossKillLog.Bosses boss : BossKillLog.Bosses.values()) {
            if (Arrays.stream(boss.getNpcs()).anyMatch(id -> id == npc.id())) {
                String name = npc.def() == null ? "Unknown" : npc.def().name;
                if (item.getValue() > 300_000) {
                    int kc = player.getAttribOr(boss.getKc(), 0);
                    World.getWorld().sendWorldMessage("<col=0052cc>" + player.getUsername() + " Killed a " + name + " and received " + Utils.getVowelFormat(item.unnote().name()) + " drop! KC " + kc + "!");
                }
                break;
            }
        }

        for (SlayerKillLog.SlayerMonsters slayerMonster : SlayerKillLog.SlayerMonsters.values()) {
            if (Arrays.stream(slayerMonster.getNpcs()).anyMatch(id -> id == npc.id())) {
                String name = npc.def() == null ? "Unknown" : npc.def().name;
                if (item.getValue() > 300_000) {
                    int kc = player.getAttribOr(slayerMonster.getKc(), 0);
                    World.getWorld().sendWorldMessage("<col=0052cc>" + player.getUsername() + " Killed a " + name + " and received " + Utils.getVowelFormat(item.unnote().name()) + " drop! KC " + kc + "!");
                }
                break;
            }
        }
    }

}
