package com.ferox.game.world.entity.combat.method.impl.npcs.godwars.zaros;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.chainedwork.Chain;

import static com.ferox.util.ItemIdentifiers.CRYSTAL_KEY;

public class NexLostItemsChest extends PacketInteraction {
    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if (object.getId() == 42854) {
            player.getNexLostItems().retain();
            return true;
        }
        return false;
    }
}
