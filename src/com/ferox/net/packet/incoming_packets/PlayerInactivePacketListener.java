package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;

import java.util.concurrent.TimeUnit;

public class PlayerInactivePacketListener implements PacketListener {

    private static final boolean enabled = false;

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.afkTimer.elapsed(GameServer.properties().afkLogoutMinutes, TimeUnit.MINUTES) && !CombatFactory.inCombat(player) && enabled) {
            player.requestLogout();
        }
    }
}
