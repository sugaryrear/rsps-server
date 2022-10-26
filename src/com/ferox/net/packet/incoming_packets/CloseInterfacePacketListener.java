package com.ferox.net.packet.incoming_packets;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;

public class CloseInterfacePacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.dead()) {
            return;
        }
        player.afkTimer.reset();
        player.getInterfaceManager().close();
        //Because the play button is calling this packet due to the Client. (what happens when you click close after welcome screen)
        //We have to hardcode the starter here after all actions are completed.
        //This is the entry point of new players.

        boolean newAccount = player.getAttribOr(AttributeKey.NEW_ACCOUNT, false);
        if (newAccount) {
            //The player can select their appearance here.
         //   player.getInterfaceManager().open(3559);
            player.getInterfaceManager().openInventory(3559, 5063);

        }
    }
}
