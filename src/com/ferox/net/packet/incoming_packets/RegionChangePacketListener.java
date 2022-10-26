package com.ferox.net.packet.incoming_packets;

import com.ferox.game.content.minigames.impl.Barrows;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.ground.GlobalDropsHandler;
import com.ferox.game.world.items.ground.GroundItemHandler;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.region.RegionManager;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegionChangePacketListener implements PacketListener {
private static final Logger logger = LogManager.getLogger(RegionChangePacketListener.class);

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.isAllowRegionChangePacket()) {
            try {

                RegionManager.loadMapFiles(player.tile().getX(), player.tile().getY());
                player.getPacketSender().deleteRegionalSpawns();
                GlobalDropsHandler.load(player);
                GroundItemHandler.updateRegionItems(player);


                Barrows.onRegionChange(player);
                player.GWDHole().updateObjects();
                ObjectManager.onRegionChange(player);
                player.farming().updateObjects();
                player.setAllowRegionChangePacket(false);
                player.afkTimer.reset();
            } catch (Exception e) {
                logger.error(player.toString() + " has encountered an error loading region " + player.tile().region() + " regionid " + player.tile().region());
                logger.catching(e);
            }
        }
    }
}
