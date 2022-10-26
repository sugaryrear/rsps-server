package com.ferox.game.content.areas.dungeons.taverley;

import com.ferox.game.task.TaskManager;
import com.ferox.game.task.impl.ForceMovementTask;
import com.ferox.game.world.entity.mob.player.ForceMovement;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.route.StepType;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.chainedwork.Chain;

import static com.ferox.util.ObjectIdentifiers.*;

public class TaverleyObjects extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if (option == 1) {
            if (obj.getId() == OBSTACLE_PIPE_16509) {
                if (!player.skills().check(Skills.AGILITY, 70, "use this shortcut"))
                    return true;

                if (obj.tile().equals(2887, 9799)) {
                    player.walkAndWait(new Tile(2886, 9799), () -> {
                        player.lockDelayDamage();
                        player.animate(749, 30);
                        TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(6, 0), 33, 126, 4)));
                        Chain.bound(null).runFn(1, () -> player.animate(749)).then(2, player::unlock);
                    });
                }
                if (obj.tile().equals(2890, 9799)) { // eats side
                    player.walkAndWait(new Tile(2892, 9799), () -> {
                        player.lockDelayDamage();
                        player.animate(749, 30);
                        TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(-6, 0), 33, 126, 4)));
                        Chain.bound(null).runFn(1, () -> player.animate(749)).then(2, player::unlock);
                    });
                }
                return true;
            }

            if (obj.getId() == LOOSE_RAILING_28849) {
                player.lockDelayDamage();
                player.looks().render(1237, 1237, 1237, 1237, 1237, 1237, -1);
                if (obj.tile().x == player.getAbsX() && obj.tile().y == player.getAbsY())
                    player.step(1, 0, StepType.FORCE_WALK);
                else
                    player.step(-1, 0, StepType.FORCE_WALK);
                Chain.bound(null).runFn(1, () -> {
                    player.looks().resetRender();
                    player.unlock();
                });
                return true;
            }

            if (obj.getId() == STRANGE_FLOOR) {
                if (!player.skills().check(Skills.AGILITY, 80, "use this shortcut"))
                    return true;
                int dir = player.getAbsX() - obj.tile().x > 0 ? 3 : 2;
                player.lock();
                player.animate(741);
                TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(dir == 3 ? -2 : 2, 0), 15, 30, 2)));
                Chain.bound(null).runFn(1, player::unlock);
                return true;
            }

            if (obj.getId() == GATE_2623) {
                if (obj.tile().equals(2924, 9803)) {
                    if (player.tile().x == 2924) {
                        player.teleport(player.tile().transform(-1, 0));
                    } else {
                        player.teleport(player.tile().transform(1, 0));
                    }
                }
                return true;
            }

            if (obj.getId() == STEPS_30189) {
                if (!player.skills().check(Skills.AGILITY, 80, "use this shortcut"))
                    return true;
                if (obj.tile().equals(2881, 9825, 0)) {
                    player.walkAndWait(new Tile(2883, 9825, 0), () -> player.teleport(2880, 9825, 1));
                }

                if (obj.tile().equals(2904, 9813, 0)) {
                    player.walkAndWait(new Tile(2906, 9813, 0), () -> player.teleport(2903, 9813, 1));
                }
                return true;
            }

            if (obj.getId() == STEPS_30190) {
                if (obj.tile().equals(2904, 9813, 1)) {
                    player.walkAndWait(new Tile(2903, 9813, 1), () -> player.teleport(2906, 9813, 0));
                }
                if (obj.tile().equals(2881, 9825, 1)) {
                    player.walkAndWait(new Tile(2880, 9825, 1), () -> player.teleport(2883, 9825, 0));
                }
                return true;
            }

            if (obj.getId() == ROCKS) {
                if (!player.skills().check(Skills.AGILITY, 70, "use this shortcut"))
                    return true;

                player.lock();
                player.animate(741);
                TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(1, 0), 15, 30, 2)));
                Chain.bound(null).runFn(2, () -> {
                    player.animate(2588);
                    player.teleport(player.getAbsX() + 1, player.getAbsY(), 1);
                }).then(1, player::unlock);
                return true;
            }
        }

        if (obj.getId() == CAVE_26569) {
            player.lock();
            player.animate(2796);
            Chain.bound(null).runFn(2, () -> {
                player.resetAnimation();
                player.teleport(1310, 1237, 0);
                player.unlock();
            });
            return true;
        }

        if (obj.getId() == CAVE_26567) {
            player.lock();
            player.animate(2796);
            Chain.bound(null).runFn(2, () -> {
                player.resetAnimation();
                player.teleport(1310, 1237, 0);
                player.unlock();
            });
            return true;
        }

        if (obj.getId() == CAVE_26568) {
            player.lock();
            player.animate(2796);
            Chain.bound(null).runFn(2, () -> {
                player.resetAnimation();
                player.teleport(1310, 1237, 0);
                player.unlock();
            });
            return true;
        }

        if (obj.getId() == CAVE_26564) {
            player.lock();
            player.animate(2796);
            Chain.bound(null).runFn(2, () -> {
                player.resetAnimation();
                player.teleport(2873, 9847, 0);
                player.unlock();
            });
            return true;
        }

        if (obj.getId() == CAVE_26565) {
            player.lock();
            player.animate(2796);
            Chain.bound(null).runFn(2, () -> {
                player.resetAnimation();
                player.teleport(2873, 9847, 0);
                player.unlock();
            });
            return true;
        }

        if (obj.getId() == CAVE_26566) {
            player.lock();
            player.animate(2796);
            Chain.bound(null).runFn(2, () -> {
                player.resetAnimation();
                player.teleport(2873, 9847, 0);
                player.unlock();
            });
            return true;
        }
        return false;
    }
}
