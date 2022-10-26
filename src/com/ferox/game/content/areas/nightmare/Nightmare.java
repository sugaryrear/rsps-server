package com.ferox.game.content.areas.nightmare;

import com.ferox.game.task.TaskManager;
import com.ferox.game.task.impl.ForceMovementTask;
import com.ferox.game.world.entity.mob.movement.MovementQueue;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.ForceMovement;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.route.Direction;
import com.ferox.game.world.route.StepType;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.NpcIdentifiers.DOMINIC_ONION;
import static com.ferox.util.ObjectIdentifiers.DOOR_11726;

public class Nightmare extends PacketInteraction {

        @Override
        public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
            if(obj.getId() == 32507) {
                player.teleport(player.tile().transform(0, player.tile().y >= 9946 ? -3 : 3));
                return true;
            }
              return false;
        }

    }


