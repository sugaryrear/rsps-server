package com.ferox.net.packet.incoming_packets;

import com.ferox.game.content.items.RottenPotato;
import com.ferox.game.content.skill.impl.slayer.content.BagOfSalt;
import com.ferox.game.content.skill.impl.slayer.content.FungicideSpray;
import com.ferox.game.content.skill.impl.slayer.content.IceCooler;
import com.ferox.game.content.skill.impl.slayer.content.RockHammer;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.combat.method.impl.npcs.slayer.kraken.KrakenBoss;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.route.routes.TargetRoute;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;
import com.ferox.net.packet.interaction.PacketInteractionManager;

import java.lang.ref.WeakReference;

/**
 * @author PVE
 * @Since augustus 24, 2020
 */
public class ItemOnNpcPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        final int itemId = packet.readShortA();
        final int npcIdx = packet.readShortA();
        final int itemSlot = packet.readLEShort();

        Npc npc = World.getWorld().getNpcs().get(npcIdx);
        if (npc == null) {
            return;
        }

        // priority code
        if(itemId == 5733 && RottenPotato.onItemOnMob(player, npc)) {
            return;
        }

        if ( player.locked() || player.dead()) {
            return;
        }

        player.stopActions(true);
        player.setEntityInteraction(npc);

        if (npc.dead()) {
            return;
        }

        Item item = player.inventory().get(itemSlot);
        if (item == null || item.getId() != itemId)
            return;

        // Store attribs
        player.putAttrib(AttributeKey.ITEM_SLOT, itemSlot);
        player.putAttrib(AttributeKey.INTERACTION_OPTION, -1); // secret key
        player.putAttrib(AttributeKey.ITEM_ID, itemId);
        player.putAttrib(AttributeKey.FROM_ITEM, item);
        player.putAttrib(AttributeKey.TARGET, new WeakReference<>(npc));
if(npc.id() == 496 && itemId == 6664){
    KrakenBoss.throwFishingExplosive(player, npc);
}
        //Do actions below
        TargetRoute.set(player, npc, () -> {
            player.face(npc.tile());

            if (PacketInteractionManager.checkItemOnNpcInteraction(player, item, npc)) {
                return;
            }

            if (RockHammer.onItemOnNpc(player, npc)) {
                return;
            }

            if(IceCooler.onItemOnNpc(player, npc)) {
                return;
            }

            if (BagOfSalt.onItemOnNpc(player, npc)) {
                return;
            }

            if (FungicideSpray.onItemOnNpc(player, npc)) {
                return;
            }

            player.message("Nothing interesting happens...");
        });
    }
}
