package com.ferox.net.packet.incoming_packets;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;

/**
 * This {@link PacketListener} receives the client's current plane
 * and compares it to the player's server-sided one.
 * 
 * If they do not match, we will manually send the proper plane
 * to the client.
 * 
 * This fixed the exploit where players would use third-party softwares
 * to teleport to different planes.
 * 
 * @author Gabriel Hannason
 */
public class HeightCheckPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int plane = packet.readByte();
        
        if (player.tile().getLevel() >= 0 && player.tile().getLevel() < 4) {
            if (plane != player.tile().getLevel()) {
                if (player.getMovementQueue().canMove()) {
                    player.getMovementQueue().clear();
                    player.setNeedsPlacement(true);
                }
            }
        }
    }
}
