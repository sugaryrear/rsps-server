package com.ferox.game.content.areas.zeah;

import com.ferox.game.task.TaskManager;
import com.ferox.game.task.impl.ForceMovementTask;
import com.ferox.game.task.impl.TickAndStop;
import com.ferox.game.world.entity.mob.Direction;
import com.ferox.game.world.entity.mob.movement.MovementQueue;
import com.ferox.game.world.entity.mob.player.ForceMovement;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.chainedwork.Chain;

import static com.ferox.game.world.entity.AttributeKey.WILDY_COURSE_STATE;
import static com.ferox.util.ObjectIdentifiers.HANDHOLDS;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 20, 2020
 */
public class HandHolds extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            Tile tiletogotoupwardhandholds = new Tile(1460, 3690);
            Tile  tiletogotobottomhandholds = new Tile(1456, 3690);
if(obj.getId() == HANDHOLDS || obj.getId() == 42009){
    player.smartPathTo(obj.getId() == HANDHOLDS ? tiletogotoupwardhandholds : tiletogotobottomhandholds);
    player.waitForTile(obj.getId() == HANDHOLDS ? tiletogotoupwardhandholds : tiletogotobottomhandholds, () -> {

        if (!player.tile().equals(obj.getId() == HANDHOLDS ? tiletogotoupwardhandholds : tiletogotobottomhandholds)) { // Get in position
            player.getMovementQueue().interpolate(obj.getId() == HANDHOLDS ? tiletogotoupwardhandholds : tiletogotobottomhandholds, MovementQueue.StepType.FORCED_WALK);
        }

        Tile end = new Tile(obj.getId() == HANDHOLDS ? 1456 : 1460,3690);
        Chain.bound(player).name("LizardmanHandholds").runFn(1, () -> {
            player.lockDelayDamage();
            player.getMovementQueue().clear();
            player.agilityWalk(false);
            player.getMovementQueue().interpolate(end, MovementQueue.StepType.FORCED_WALK);
            player.looks().render(844, 844, 844, 844, 844, 844, -1);
        });

        Chain.bound(player).name("LizardmanHandholdsUnlock").waitForTile(end, () -> {
            player.looks().resetRender();
            player.agilityWalk(true);
            player.unlock();
        });
    });
    return true;
}
//            if (obj.getId() == HANDHOLDS) {
//                if (obj.tile().equals(west_handholds_down)) {
//                    if (!player.tile().equals(obj.tile().transform(1, 0, 0))) {
//                        player.getMovementQueue().walkTo(obj.tile().transform(1, 0, 0));
//                    }
//                    player.face(new Tile(1462, 3690));
//                    TaskManager.submit(new TickAndStop(1) {
//                        @Override
//                        public void executeAndStop() {
//                            player.animate(1148, 20);
//                            TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(-6, 0), 25, 135, Direction.EAST.toInteger())));
//                        }
//                    });
//                    TaskManager.submit(new TickAndStop(5) {
//                        @Override
//                        public void executeAndStop() {
//                            player.teleport(west_handholds_down.x - 4, west_handholds_down.y);
//                        }
//                    });
//                    return true;
//                } else if (obj.tile().equals(wast_handholds_up)) {
//                    if (!player.tile().equals(obj.tile().transform(-1, 0, 0))) {
//                        player.getMovementQueue().walkTo(obj.tile().transform(-1, 0, 0));
//                    }
//                    player.face(new Tile(1462, 3690));
//
//                    TaskManager.submit(new TickAndStop(1) {
//                        @Override
//                        public void executeAndStop() {
//                            player.animate(1148, 15);
//                            TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(6, 0), 25, 135, Direction.EAST.toInteger())));
//                        }
//                    });
//                    TaskManager.submit(new TickAndStop(5) {
//                        @Override
//                        public void executeAndStop() {
//                            player.teleport(wast_handholds_up.x + 4, wast_handholds_up.y);
//                        }
//                    });
//                    return true;
//                }
//            }
        }
        return false;
    }
}
