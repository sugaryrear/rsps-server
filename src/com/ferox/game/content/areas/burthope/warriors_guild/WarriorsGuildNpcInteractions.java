package com.ferox.game.content.areas.burthope.warriors_guild;

import com.ferox.game.content.areas.burthope.warriors_guild.dialogue.*;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.NpcIdentifiers.*;

/**
 * @author Patrick van Elderen | April, 14, 2021, 18:30
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class WarriorsGuildNpcInteractions extends PacketInteraction {

    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if(option == 1) {
            if(npc.id() == AJJAT) {
                player.getDialogueManager().start(new Ajjat());
                return true;
            }
            if(npc.id() == ANTON) {
                player.getDialogueManager().start(new Anton());
                return true;
            }
            if (npc.id() == GHOMMAL) {
                player.getDialogueManager().start(new Ghommal());
                return true;
            }
            if (npc.id() == LIDIO) {
                player.getDialogueManager().start(new Lidio());
                return true;
            }
            if (npc.id() == LILLY) {
                player.getDialogueManager().start(new Lilly());
                return true;
            }
            if (npc.id() == SHANOMI) {
                player.getDialogueManager().start(new Shanomi());
                return true;
            }
        }

        if(option == 2) {
            if(npc.id() == ANTON) {
                World.getWorld().shop(24).open(player);
                return true;
            }
            if(npc.id() == LIDIO) {
                World.getWorld().shop(25).open(player);
                return true;
            }
            if(npc.id() == LILLY) {
                World.getWorld().shop(26).open(player);
                return true;
            }
        }
        return false;
    }
}
