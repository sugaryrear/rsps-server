package com.ferox.game.content.areas;

import com.ferox.game.world.entity.mob.movement.MovementQueue;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.chainedwork.Chain;

public class HarmonyIsland extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int opt) {
        if (obj.getId() == 22119) {


            Chain.bound(null).runFn(1, () -> {
                GameObject opendoor = new GameObject(obj.getId(), obj.tile(), obj.getType(), 1);
                GameObject closedoor = new GameObject(opendoor.getId(), opendoor.tile(), opendoor.getType(), 0);
                ObjectManager.openAndCloseDoor(opendoor, closedoor);
            }).then(1, () -> {
                //Walk trough
                player.getMovementQueue().interpolate(player.tile().x < 3805 ? 3806 : 3804,player.tile().y, MovementQueue.StepType.FORCED_WALK);
            });
            return true;
        }
        return false;
    }
}
