package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;

public class ExamineItemPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int item = packet.readShort();
        // 317 doesnt send item slot so we cant get the amount

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

        player.message("%s", World.getWorld().examineRepository().item(item));
    }

}
