package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.game.content.skill.impl.firemaking.LogLighting;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.ground.GroundItem;
import com.ferox.game.world.items.ground.GroundItemHandler;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;

import java.util.Optional;

/**
 * This packet is received when a player
 * clicks on the second option on a ground item.
 * An example being the "light" option on logs that 
 * are on the ground.
 * 
 * @author Professor Oak
 */
public class SecondGroundItemOptionPacketListener implements PacketListener {

    @Override
    public void handleMessage(final Player player, Packet packet) {
        final int y = packet.readLEShort();
        final int itemId = packet.readShort();
        final int x = packet.readLEShort();
        final Tile tile = new Tile(x, y, player.tile().getLevel());

        if (player.dead()) {
            return;
        }

        if (player.busy())
            return;

        if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
            player.getBankPin().openIfNot();
            return;
        }

        if(player.askForAccountPin()) {
            player.sendAccountPinMessage();
            return;
        }
        //Get ground item..
        Optional<GroundItem> item = GroundItemHandler.getGroundItem(itemId, tile, player);

        if (!player.locked() || !player.dead()) {
            player.stopActions(false);
            player.afkTimer.reset();
            player.putAttrib(AttributeKey.INTERACTED_GROUNDITEM, item.get());
            player.putAttrib(AttributeKey.INTERACTION_OPTION, 2);

            player.getRouteFinder().routeGroundItem(item.get(), distance -> {
                //Make sure distance isn't way off..
                if (Math.abs(player.tile().getX() - x) > 25 || Math.abs(player.tile().getY() - y) > 25) {
                    player.getMovementQueue().clear();
                    return;
                }

                if (item.isPresent()) {
                    //Handle it..
                    LogLighting.onGroundItemOption2(player, item.get().getItem());
                }
            });
        }
    }
}
