package com.ferox.game.content.skill.impl.agility.rooftops;

import com.ferox.game.content.skill.impl.agility.MarksOfGrace;
import com.ferox.game.content.skill.impl.agility.UnlockAgilityPet;
import com.ferox.game.task.TaskManager;
import com.ferox.game.task.impl.ForceMovementTask;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.Direction;
import com.ferox.game.world.entity.mob.movement.MovementQueue;
import com.ferox.game.world.entity.mob.player.ForceMovement;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.chainedwork.Chain;

import java.util.Arrays;
import java.util.List;

import static com.ferox.util.ObjectIdentifiers.*;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * juni 14, 2020
 */
public class RellekkaRooftop extends PacketInteraction {

    private static final List<Tile> MARK_SPOTS = Arrays.asList(new Tile(2622, 3676, 3), new Tile(2617, 3664, 3), new Tile(2618, 3660, 3), new Tile(2628, 3652, 3), new Tile(2628, 3655, 3), new Tile(2641, 3649, 3), new Tile(2643, 3651, 3), new Tile(2649, 3659, 3), new Tile(2644, 3662, 3), new Tile(2658, 3674, 3), new Tile(2656, 3681, 3));

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        // Wall climb
        if(obj.getId() == ROUGH_WALL_14946) {
            if (player.skills().level(Skills.AGILITY) >= 80) {
                player.lock();
                player.face(player.tile().transform(0, -1));
                Chain.bound(player).name("RellekkaRooftopWallClimbTask").runFn(1, () -> {
                    player.face(player.tile().transform(0, -1));
                    player.animate(828, 15);
                }).then(2, () -> {
                    player.teleport(2626, 3676, 3);
                    player.animate(-1);
                    player.skills().addXp(Skills.AGILITY, 20.0);
                    MarksOfGrace.trySpawn(player, MARK_SPOTS, 42, 80);
                    player.unlock();
                });
            } else {
                player.message("You need at least 80 Agility to attempt this course.");
            }
            return true;
        }

        // Gap leap
        if(obj.getId() == GAP_14947) {
            player.smartPathTo(new Tile(2622, 3672, 3));
            player.waitForTile(new Tile(2622, 3672, 3), () -> {
                player.lock();
                player.face(player.tile().transform(0, -1));
                Chain.bound(player).name("RellekkaRooftopGapLeapTask").runFn(1, () -> player.animate(1995, 15)).then(1, () -> {
                    player.sound(1936);
                    player.animate(1603);
                    TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(0, -4), 8, 50, Direction.SOUTH.toInteger())));
                }).then(2, () -> player.teleport(2622, 3668, 3)).then(1, () -> {
                    player.skills().addXp(Skills.AGILITY, 30.0);
                    MarksOfGrace.trySpawn(player, MARK_SPOTS, 42, 80);
                    player.unlock();
                });
            });
            return true;
        }

        // Tightrope
        if(obj.getId() == TIGHTROPE_14987) {
            Chain.bound(player).name("RellekkaRooftopTightropeTask").runFn(1, () -> {
                player.lock();
                player.agilityWalk(false);
                player.getMovementQueue().clear();
                //player.sound(2495, 0, 3);
                player.looks().render(763, 762, 762, 762, 762, 762, -1);
                player.getMovementQueue().interpolate(2626, 3654, MovementQueue.StepType.FORCED_WALK);
                player.getMovementQueue().step(2627, 3654, MovementQueue.StepType.FORCED_WALK);
            }).waitForTile(new Tile(2626, 3654), () -> {
                player.agilityWalk(true);
                player.looks().resetRender();
                player.skills().addXp(Skills.AGILITY, 40.0);
                MarksOfGrace.trySpawn(player, MARK_SPOTS, 42, 80);
                player.unlock();
            });
            return true;
        }

        // Gap jump + tightrope
        if(obj.getId() == GAP_14990) {
            Tile startPos = obj.tile().transform(0, -1);
            player.smartPathTo(startPos);
            player.waitForTile(startPos, () -> {})
                .name("RellekkaRooftopGapJumpTightropeTask").then(1, () -> {
                player.lock();
                //player.sound(2468, 20, 1);
                TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(0, 3), 25, 30, Direction.NORTH.toInteger())));
            }).then(1, () -> {
                player.teleport(2629, 3658, 3);
                //player.sound(2451, 2, 10);
                player.animate(752);
                player.agilityWalk(false);
                player.looks().render(755, 755, 754, 754, 754, 754, -1);
                player.face(player.tile().transform(1, 0));
            }).then(1, () -> player.getMovementQueue().interpolate(2635, 3658, MovementQueue.StepType.FORCED_WALK)).then(6, () -> {
                //player.sound(2495, 0, 4);
                player.looks().render(763, 762, 762, 762, 762, 762, -1);
                player.getMovementQueue().interpolate(2639, 3654, MovementQueue.StepType.FORCED_WALK);
                player.getMovementQueue().step(2639, 3653, MovementQueue.StepType.FORCED_WALK);
            }).then(6, () -> {
                player.skills().addXp(Skills.AGILITY, 85.0);
                MarksOfGrace.trySpawn(player, MARK_SPOTS, 42, 80);
                player.agilityWalk(true);
                player.looks().resetRender();
                player.unlock();
            });
            return true;
        }

        // Gap jump
        if(obj.getId() == GAP_14991) {
            Tile startPos = obj.tile().transform(0, -1);
            player.smartPathTo(startPos);
            player.waitForTile(startPos, () -> {})
                .name("RellekkaRooftopGapjumpTask").then(1, () -> {
                player.lock();
                player.animate(1995, 15);
            }).then(1, () -> {
                player.sound(1936);
                player.animate(1603);
                TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(0, 4), 8, 50, Direction.NORTH.toInteger())));
            }).then(2, () -> {
                player.teleport(2643, 3657, 3);
                player.skills().addXp(Skills.AGILITY, 25.0);
                MarksOfGrace.trySpawn(player, MARK_SPOTS, 42, 80);
                player.unlock();
            });
            return true;
        }

        // Tightrope
        if(obj.getId() == TIGHTROPE_14992) {
            Tile startPos = obj.tile().transform(0, -1);
            player.smartPathTo(startPos);
            player.waitForTile(startPos, () -> {})
                .name("RellekkaRooftopTightropeTask").then(1, () -> {
                player.lock();
                player.getMovementQueue().clear();
                //player.sound(2495, 0, 6);
                player.agilityWalk(false);
                player.looks().render(763, 762, 762, 762, 762, 762, -1);
                player.getMovementQueue().step(2647, 3663, MovementQueue.StepType.FORCED_WALK);
                player.getMovementQueue().interpolate(2654, 3670, MovementQueue.StepType.FORCED_WALK);
                player.getMovementQueue().step(2655, 3670, MovementQueue.StepType.FORCED_WALK);
            }).waitForTile(new Tile(2654, 3670), () -> {
                player.agilityWalk(true);
                player.looks().resetRender();
                player.skills().addXp(Skills.AGILITY, 105.0);
                MarksOfGrace.trySpawn(player, MARK_SPOTS, 42, 80);
                player.unlock();
            });
            return true;
        }

        // Jump fish pile
        if(obj.getId() == PILE_OF_FISH) {
            Tile startPos = obj.tile().transform(1, 0);
            player.smartPathTo(startPos);
            player.waitForTile(startPos, () -> {}).then(1, () -> {
                player.lock();
                player.animate(2586, 15);
                player.sound(2462, 15);
            }).then(1, () -> {
                player.animate(2588);
                player.teleport(2653, 3676, 0);
            }).then(1, () -> {
                player.skills().addXp(Skills.AGILITY, 475.0);
                MarksOfGrace.trySpawn(player, MARK_SPOTS, 42, 80);
                player.getMovementQueue().step(Direction.WEST);

                // Woo! A pet!
                var odds = (int) (12000 * player.getMemberRights().petRateMultiplier());
                if (World.getWorld().rollDie(odds, 1)) {
                    UnlockAgilityPet.unlockGiantSquirrel(player);
                }
            }).then(1, player::unlock);
            return true;
        }
        return false;
    }

}
