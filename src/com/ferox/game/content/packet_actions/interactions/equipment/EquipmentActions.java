package com.ferox.game.content.packet_actions.interactions.equipment;

import com.ferox.game.content.items.teleport.ArdyCape;
import com.ferox.game.content.skill.impl.slayer.content.SlayerRing;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteractionManager;

public class EquipmentActions {

    public static boolean operate(Player player, int slot, Item item) {
        ArdyCape.onEquipmentOption(player, item, slot);

        if(PacketInteractionManager.onEquipmentAction(player, item, slot)) {
            return true;
        }

        if (SlayerRing.onEquipmentOption(player, item, slot)) {
            return true;
        }
        return false;
    }
}
