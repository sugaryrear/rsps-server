package com.ferox.game.content.areas;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.ObjectIdentifiers.STEPPING_STONE_19040;

public class pestcontrol extends PacketInteraction {

    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if (option == 1) {
            if (npc.id() == 1765) {
                World.getWorld().shop(36).open(player);
                return true;
            }
            if (npc.id() == 1768) {
                World.getWorld().shop(1).open(player);
                return true;
            }
            if (npc.id() == 1767) {
                World.getWorld().shop(52).open(player);
                return true;
            }
        }
        if (option == 2) {
            if (npc.id() == 1765) {
                World.getWorld().shop(36).open(player);
                return true;
            }
            if (npc.id() == 1768) {
                World.getWorld().shop(1).open(player);
                return true;
            }
            if (npc.id() == 1767) {
                World.getWorld().shop(52).open(player);
                return true;
            }
        }
        return false;
    }
}
