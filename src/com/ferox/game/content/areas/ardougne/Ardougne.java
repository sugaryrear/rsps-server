package com.ferox.game.content.areas.ardougne;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.ItemIdentifiers.MOLE_CLAW;
import static com.ferox.util.ItemIdentifiers.MOLE_SKIN;

public class Ardougne extends PacketInteraction {
    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if (option == 1) {
            if (npc.id() == 1307) {
                World.getWorld().shop(71).open(player);
                return true;
            }
            if (npc.id() == 3208) {
                World.getWorld().shop(72).open(player);
                return true;
            }
            if (npc.id() == 3209) {
                World.getWorld().shop(73).open(player);
                return true;
            }
            if (npc.id() == 3211) {
                World.getWorld().shop(74).open(player);
                return true;
            }
            if (npc.id() == 3207) {
                World.getWorld().shop(75).open(player);
                return true;
            }
        }
        if (option == 2) {
            if (npc.id() == 1307) {
                World.getWorld().shop(71).open(player);
                return true;
            }
            if (npc.id() == 3208) {
                World.getWorld().shop(72).open(player);
                return true;
            }
            if (npc.id() == 3209) {
                World.getWorld().shop(73).open(player);
                return true;
            }
            if (npc.id() == 3207) {
                World.getWorld().shop(75).open(player);
                return true;
            }
        }
        return false;
    }
}
