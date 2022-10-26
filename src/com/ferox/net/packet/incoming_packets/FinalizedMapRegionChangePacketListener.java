package com.ferox.net.packet.incoming_packets;

import com.ferox.game.content.minigames.impl.Barrows;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.ground.GlobalDropsHandler;
import com.ferox.game.world.items.ground.GroundItemHandler;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.region.RegionManager;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;

/**
 * This packet listener is called when a player's region has been loaded.
 * 
 * @author relex lawl
 */
public class FinalizedMapRegionChangePacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {

        if (player == null || player.dead()) {
            return;
        }
       // RegionManager.loadMapFiles(player.tile().getX(), player.tile().getY());
        player.getPacketSender().deleteRegionalSpawns();
        GlobalDropsHandler.load(player);
        GroundItemHandler.updateRegionItems(player);


     //   Barrows.onRegionChange(player);
        Barrows.onRegionChange(player);
        ObjectManager.onRegionChange(player);
        player.farming().updateObjects();
       //player.setAllowRegionChangePacket(false);
        player.afkTimer.reset();
        player.GWDHole().updateObjects();
     //   player.message("here");
    }
}
