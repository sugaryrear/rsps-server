package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.ClientToServerPackets;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;
import com.ferox.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

/**
 * This packet listener is called when a player is doing something relative
 * to their friends or ignore list, such as adding or deleting a player from said list.
 *
 * @author relex lawl
 */

public class PlayerRelationPacketListener implements PacketListener {
    private static final Logger logger = LogManager.getLogger(PlayerRelationPacketListener.class);

    @Override
    public void handleMessage(Player player, Packet packet) {
        try {
            String username = packet.readString();
            if (player == null || player.dead()) {
                return;
            }

            if (!Utils.VALID_NAME.matcher(username).matches())
                return;

            player.afkTimer.reset();
            if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
                player.getBankPin().openIfNot();
                return;
            }
            if(player.askForAccountPin()) {
                player.sendAccountPinMessage();
                return;
            }
            switch (packet.getOpcode()) {
                case ClientToServerPackets.ADD_FRIEND_OPCODE -> player.getRelations().addFriend(username);
                case ClientToServerPackets.ADD_IGNORE_OPCODE -> player.getRelations().addIgnore(username);
                case ClientToServerPackets.REMOVE_FRIEND_OPCODE -> player.getRelations().deleteFriend(username);
                case ClientToServerPackets.REMOVE_IGNORE_OPCODE -> player.getRelations().deleteIgnore(username);
                case ClientToServerPackets.SEND_PM_OPCODE -> {
                    Optional<Player> friend = World.getWorld().getPlayerByName(username.replaceAll("_", " "));
                    int size = packet.getSize() - packet.getBuffer().nioBuffer().position();
                    byte[] message = packet.readBytes(size);
                    if (player.muted()) {
                        player.message("You are muted and cannot chat. Please try again later.");
                        return;
                    }
                    if (friend.isPresent()) {
                        String chatMessage = Utils.ucFirst(Utils.textUnpack(message, size).toLowerCase());
                        if (chatMessage.length() <= 0) {
                            return;
                        }
                        if (chatMessage.length() > 80) {
                            chatMessage = chatMessage.substring(0, 79);
                        }
                        player.getRelations().message(friend.get(), message, size);
                        Utils.sendDiscordInfoLog( player.getUsername() + " to " + friend.get().getUsername() + ": " + chatMessage, "pm");
                    } else {
                        player.getPacketSender().sendMessage(username +" is offline.");
                        //logger.trace("cant message longUsername {} name {} since they are offline.", username, username);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
