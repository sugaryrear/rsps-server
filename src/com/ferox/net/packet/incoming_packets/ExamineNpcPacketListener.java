package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.fs.NpcDefinition;

import com.ferox.game.content.DropsDisplay;
import com.ferox.game.world.World;

import com.ferox.game.world.entity.mob.npc.NpcCombatInfo;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;
import com.ferox.util.ExamineRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExamineNpcPacketListener implements PacketListener {
    private static final Logger logger = LogManager.getLogger(ExamineNpcPacketListener.class);

    @Override
    public void handleMessage(Player player, Packet packet) {
        int npc = packet.readShort();
        //System.out.println("npc: "+npc);
        if (npc <= 0) {
            return;
        }

        if (player == null || player.dead()) {
            return;
        }

        if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
            player.getBankPin().openIfNot();
            return;
        }

        if(player.askForAccountPin()) {
            player.sendAccountPinMessage();
            return;
        }

      //  DropsDisplay.display(player, npc);

        player.message("%s", World.getWorld().examineRepository().npc(npc));

    }
}
