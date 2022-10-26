package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.game.content.raids.party.Party;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;
import com.ferox.util.Tuple;

import java.lang.ref.WeakReference;

import static com.ferox.game.world.entity.combat.method.impl.npcs.slayer.kraken.KrakenBoss.KRAKEN_WHIRLPOOL;
import static com.ferox.util.NpcIdentifiers.ICE_DEMON_7585;

/**
 * @author PVE
 * @Since augustus 27, 2020
 */
public class AttackNpcPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int index = packet.readShortA();
        if (index < 0 || index > World.getWorld().getNpcs().capacity())
            return;
        final Npc other = World.getWorld().getNpcs().get(index);

        if (other == null) {
            return;
        }

        if (player == null || player.dead()) {
            return;
        }

        player.afkTimer.reset();

        if (player.busy()) {
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

        if (!player.locked() && !player.dead()) {
            player.stopActions(false);

            if (!other.dead()) {
                if(other.id() == ICE_DEMON_7585) {
                    Party party = player.raidsParty;
                    if (party != null && party.getBraziersLit() < 4) {
                        player.message("You need to light all 4 braziers before you can attack the Ice Demon!");
                        return;
                    }
                }
                if (other.combatInfo() == null) {
                    player.message("Without combat attributes this monster is unattackable.");
                    return;
                }
//                if(other.id() == KRAKEN_WHIRLPOOL) {
//                    player.message("You need to disturb the tentacles first or throw a fishing explosive at the whirlpool.");
//                    return;
//                }
                // See if it's exclusively owned
                Tuple<Integer, Player> ownerLink = other.getAttribOr(AttributeKey.OWNING_PLAYER, new Tuple<>(-1, null));
                if (ownerLink.first() != null && ownerLink.first() >= 0 && ownerLink.first() != player.getIndex()) {
                    player.message("They don't seem interested in fighting you.");
                    player.getCombat().reset();
                    return;
                }

                other.getMovementQueue().setBlockMovement(true);
                player.putAttrib(AttributeKey.INTERACTION_OPTION, 2);
                player.putAttrib(AttributeKey.TARGET, new WeakReference<Mob>(other));

                player.getCombat().attack(other);
                //CombatFactory.debug(player, "Executed attack in AttackNPC packet", other, true);
                other.getMovementQueue().setBlockMovement(false);
            }
        }
    }

}
