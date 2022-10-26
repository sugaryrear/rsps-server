package com.ferox.game.world.entity.mob.npc.pets;

import com.ferox.game.task.Task;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.movement.MovementQueue;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Tuple;

/**
 * A regional pet check, for respawning pets out of distance
 *
 * @author Patrick van Elderen | 3 augustus. 2019 : 16:55
 * @see <a href="https://github.com/Patrick9-10-1995">Github profile</a>
 */
public class RegionalPetTask extends Task {

    private final Npc npc;

    /**
     * The constructor of the regional check.
     */
    public RegionalPetTask(Npc npc) {
        super("RegionalPetTask", 1);
        this.npc = npc;
    }

    @Override
    public void execute() {
        Player player = npc.<Tuple<Integer, Player>>getAttribOr(AttributeKey.OWNING_PLAYER, new Tuple<>(-1, null)).second();
        if (player == null) {
            stop();
            return;
        }

        if (!player.finished() && !npc.finished() && !npc.dead()) {
            if (player.tile().distance(npc.tile()) > 6) {
                npc.teleport(player.tile());
            }

            npc.getMovementQueue().follow(player);
            npc.face(player.tile());
        } else {
            stop();
        }
    }

}
