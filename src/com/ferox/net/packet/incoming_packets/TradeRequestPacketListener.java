package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.PlayerStatus;
import com.ferox.game.world.route.routes.TargetRoute;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;

import java.lang.ref.WeakReference;


public class TradeRequestPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        player.stopActions(false);
        int index = packet.readLEShort();
        if (index > World.getWorld().getPlayers().capacity() || index < 0) {
            return;
        }
        player.afkTimer.reset();

        Player other = World.getWorld().getPlayers().get(index);

        if (player == null
                || player.dead()
                || !player.isRegistered()
                || other == null || other.dead()
                || !other.isRegistered()) {
            return;
        }

        boolean newAccount = player.getAttribOr(AttributeKey.NEW_ACCOUNT, false);

        if (newAccount) {
            player.message("You have to select your game mode before you can continue.");
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
        if (player.dead()) {
            return;
        }
        player.setEntityInteraction(other);
        if (!other.dead()) {
            player.putAttrib(AttributeKey.TARGET, new WeakReference<Mob>(other));
            player.putAttrib(AttributeKey.INTERACTION_OPTION, 4);
            player.getCombat().reset();
            player.stopActions(false);
            TargetRoute.set(player, other, () -> {
                player.runFn(1, () -> {
                    player.face(other.tile());
                    player.setEntityInteraction(null);
                });
                if (player.getMovementQueue().isFollowing(other)) {
                    player.getMovementQueue().resetFollowing();
                    player.setEntityInteraction(null);
                }

                if (player.busy()) {
                    player.message("You cannot do that right now.");
                    return;
                }

                if (other.busy() || other.getInterfaceManager().isInterfaceOpen(7424) || (!other.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin)) {
                    String msg = "That player is currently busy.";

                    if (other.getStatus() == PlayerStatus.TRADING) {
                        msg = "That player is currently trading with someone else.";
                    }

                    player.message(msg);
                    return;
                }

                if (player.getController() != null) {
                    if (!player.getController().canTrade(player, other)) {
                        player.message("You cannot trade here.");
                        return;
                    }
                }

                if (player.getLocalPlayers().contains(other)) {
                    if (player.tile().distance(other.tile()) < 3) {
                        player.getTrading().requestTrade(other);
                    }
                }
            });

        }
    }
}
