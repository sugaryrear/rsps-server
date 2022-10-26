package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.content.clan.Clan;
import com.ferox.game.content.clan.ClanRepository;
import com.ferox.game.content.instance.InstancedAreaManager;
import com.ferox.game.content.teleport.TeleportType;
import com.ferox.game.content.teleport.Teleports;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.position.Area;
import com.ferox.game.world.position.Tile;
import com.ferox.util.NpcIdentifiers;

import java.util.Arrays;

public class ClanOutpostCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (Teleports.canTeleport(player, true, TeleportType.GENERIC)) {
            if (player.getClanChat() != null) {
                Clan clan = ClanRepository.get(player.getClanChat());

                if (clan != null) {
                    if (clan.meetingRoom == null) {
                        clan.meetingRoom = InstancedAreaManager.getSingleton().createSingleInstancedArea(player, new Area(1, 2, 3, 4));
                        Npc pvpDummy = new Npc(NpcIdentifiers.UNDEAD_COMBAT_DUMMY, new Tile(2454, 2846,2 + clan.meetingRoom.getzLevel()));
                        pvpDummy.spawnDirection(1);
                        Npc slayerDummy = new Npc(NpcIdentifiers.COMBAT_DUMMY, new Tile(2454, 2848,2 + clan.meetingRoom.getzLevel()));
                        slayerDummy.spawnDirection(6);
                        clan.dummys = Arrays.asList(pvpDummy, slayerDummy);
                        World.getWorld().registerNpc(clan.dummys.get(0));
                        World.getWorld().registerNpc(clan.dummys.get(1));
                    }

                    Teleports.basicTeleport(player, new Tile(2452, 2847, 2 + clan.meetingRoom.getzLevel()));
                    player.message("You teleport to the " + player.getClanChat() + " clan outpost.");
                }
            }
        }
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
