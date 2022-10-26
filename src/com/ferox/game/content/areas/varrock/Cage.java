package com.ferox.game.content.areas.varrock;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.ObjectIdentifiers;

/**
 * @author Patrick van Elderen | April, 14, 2021, 19:19
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class Cage extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if(object.getId() == ObjectIdentifiers.CAGE_20873) {
            player.message("You can't unlock the pillory, you'll let all the criminals out!");
            return true;
        }
        return false;
    }
}
