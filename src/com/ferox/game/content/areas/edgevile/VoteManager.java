package com.ferox.game.content.areas.edgevile;

import com.ferox.game.GameConstants;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.NpcIdentifiers.FANCY_DAN;

/**
 * @author Patrick van Elderen | April, 22, 2021, 12:51
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class VoteManager extends PacketInteraction {

    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if (option == 1) {
            if (npc.id() == FANCY_DAN) {
                World.getWorld().shop(6).open(player);
                return true;
            }
        }
        if (option == 2) {
            if (npc.id() == FANCY_DAN) {
                player.optionsTitled("Would you like to open our voting page?", "Yes.", "No.", () -> {
                    player.getPacketSender().sendURL(GameConstants.VOTE_URL);
                });
                return true;
            }
        }
        return false;
    }
}
