package com.ferox.game.content.security;

import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.NpcIdentifiers.COUNT_CHECK;
import static com.ferox.util.NpcIdentifiers.SECURITY_GUARD;

/**
 * @author Patrick van Elderen | April, 29, 2021, 18:16
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class SecurityAdvisor extends PacketInteraction {

    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if(option == 1) {
            if(npc.id() == COUNT_CHECK) {
                player.getDialogueManager().start(new SecurityAdvisorDialogue());
                return true;
            }
        }
        return false;
    }
}
