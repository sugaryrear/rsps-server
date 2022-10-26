package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.magic.CombatSpell;
import com.ferox.game.world.entity.combat.magic.CombatSpells;
import com.ferox.game.world.entity.combat.method.impl.npcs.slayer.kraken.KrakenBoss;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;
import com.ferox.util.Tuple;

import java.lang.ref.WeakReference;

import static com.ferox.game.world.entity.combat.method.impl.npcs.slayer.kraken.KrakenBoss.KRAKEN_WHIRLPOOL;

/**
 * @author PVE
 * @Since augustus 27, 2020
 */
public class MagicOnNpcPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int targetIndex = packet.readLEShortA();
        int spellId = packet.readShortA();

        if (targetIndex < 0 || spellId < 0 || targetIndex > World.getWorld().getNpcs().capacity()) {
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

        Npc other = World.getWorld().getNpcs().get(targetIndex);
        if (other == null) {
            player.message("Unable to find npc.");
        } else {
            if (!player.locked() && !player.dead()) {
                player.stopActions(false);
                player.face(other.tile());
                if (!other.dead()) {
                    player.putAttrib(AttributeKey.TARGET, new WeakReference<Mob>(other));
                    player.putAttrib(AttributeKey.INTERACTION_OPTION, 2);
                    if(other.id() == KRAKEN_WHIRLPOOL) {
                        player.message("You need to disturb the tentacles first or throw a fishing explosive at the whirlpool.");
                        return;
                    }
                    if (other.combatInfo() == null) {
                        player.message("Without combat attributes this monster is unattackable.");
                        return;
                    }

                    // See if it's exclusively owned
                    Tuple<Integer, Player> ownerLink = other.getAttribOr(AttributeKey.OWNING_PLAYER, new Tuple<>(-1, null));
                    if (ownerLink.first() != null && ownerLink.first() >= 0 && ownerLink.first() != player.getIndex()) {
                        player.message("They don't seem interested in fighting you.");
                        player.getCombat().reset();
                        return;
                    }

                    CombatSpell spellSelected = CombatSpells.getCombatSpell(spellId);

                    if (spellSelected == null) {
                        player.getMovementQueue().clear();
                        return;
                    }
                    other.getMovementQueue().setBlockMovement(true);
                    player.face(other.tile());

                    //These always overwrite it I tried so many things
                    player.getCombat().setCastSpell(spellSelected);
                    player.getCombat().attack(other);
                    other.getMovementQueue().setBlockMovement(false);
                }
            }
        }
    }
}
