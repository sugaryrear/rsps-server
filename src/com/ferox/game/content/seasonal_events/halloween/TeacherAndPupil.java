package com.ferox.game.content.seasonal_events.halloween;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.NpcIdentifiers.TEACHER_AND_PUPIL_1922;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 12, 2021
 */
public class TeacherAndPupil extends PacketInteraction {

    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if(option == 1) {
            if(npc.id() == TEACHER_AND_PUPIL_1922) {
                player.getDialogueManager().start(new TeacherAndPupilD());
                return true;
            }
        } else if(option == 2) {
            if(npc.id() == TEACHER_AND_PUPIL_1922) {
                World.getWorld().shop(48).open(player);
                return true;
            }
        }
        return false;
    }
}
