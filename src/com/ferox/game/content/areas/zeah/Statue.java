package com.ferox.game.content.areas.zeah;

import com.ferox.game.task.TaskManager;
import com.ferox.game.task.impl.TickAndStop;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.ObjectIdentifiers.STATUE_27785;

/**
 * @author Patrick van Elderen | Zerikoth | PVE
 * @date februari 29, 2020 22:03
 */
public class Statue extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            if(obj.getId() == STATUE_27785) {
                readStatue(player, obj);
                return true;
            }
        }
        return false;
    }

    private void readStatue(Player player, GameObject obj) {
        player.faceObj(obj);
        player.animate(2171);
        TaskManager.submit(new TickAndStop(1) {
            @Override
            public void executeAndStop() {
                //TODO send interface, we don't have it as of now
            }
        });
    }

    private void investigateState(Player player, GameObject obj) {
        player.faceObj(obj);
        player.message("You investigate what looks like hinges on the plaque and find it opens.");
        player.animate(827);
        TaskManager.submit(new TickAndStop(1) {
            @Override
            public void executeAndStop() {
                //TODO send interface, we don't have it as of now
            }
        });
        TaskManager.submit(new TickAndStop(2) {
            @Override
            public void executeAndStop() {
                player.teleport(new Tile(1666, 10050));
                player.message("You climb down the hole.");
            }
        });
    }
}
