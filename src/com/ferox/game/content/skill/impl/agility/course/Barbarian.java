package com.ferox.game.content.skill.impl.agility.course;

import com.ferox.game.content.packet_actions.interactions.objects.Ladders;
import com.ferox.game.content.skill.impl.agility.UnlockAgilityPet;
import com.ferox.game.task.TaskManager;
import com.ferox.game.task.impl.ForceMovementTask;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.movement.MovementQueue;
import com.ferox.game.world.entity.mob.player.ForceMovement;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;

/**
 * Created by Jak on 13/06/2016.
 */
public class Barbarian extends PacketInteraction {

    private static final int ENTRANCE_PIPE = 20210;
    private static final int ROPE_SWING = 23131;
    private static final int LOG_BALANCE = 23144;
    private static final int NET = 20211;
    private static final int LEDGE = 23547;
    private static final int LEDGE_LADDER = 16682;
    private static final int WALL = 1948;
    private static final int DUNGEONLADDER_DOWN = 16680;

    private void putStage(Player player, int stageBit) {
        int stage = player.getAttribOr(AttributeKey.BARBARIAN_COURSE_STATE, 0);
        stage = stage | stageBit;
        player.putAttrib(AttributeKey.BARBARIAN_COURSE_STATE, stage);
    }

    // TODO make formula include exponential success growth for levels over requirement ending at 70
    public boolean successful(Player player) {
        int req = 35;
        int endeffectiveness = 70;
        int gap = player.skills().xpLevel(Skills.AGILITY) - req;
        double failrate = 0.30;
        return Math.random() >= (failrate - ((failrate / (endeffectiveness - req)) * gap));
    }

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if (option == 1) {
            if (obj.getId() == ENTRANCE_PIPE) {
                player.smartPathTo(obj.tile());
                player.waitUntil(1, () -> !player.getMovementQueue().isMoving(), () -> {
                    if (player.tile().y >= 3560) {
                        if (player.skills().level(Skills.AGILITY) < 35) {
                            player.message("You need a agility level of 35 to enter this course.");
                        } else {
                            player.teleport(new Tile(2552, 3558, 0));
                        }
                    } else {
                        player.teleport(new Tile(2552, 3561, 0));
                    }
                });
                return true;
            }

            if (obj.getId() == ROPE_SWING) {
                // Get in position
                if (!player.tile().equals(2551, 3554)) { // Get in position
                    player.getMovementQueue().clear();
                    player.getMovementQueue().interpolate(2551, 3554);
                }

                player.waitForTile(new Tile(2551, 3554), () -> {
                    player.lockDelayDamage();
                    player.getPacketSender().sendObjectAnimation(obj, 54);// make rope pull back
                    player.animate(751); //Swing
                    TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(0, -5), 30, 50, 2)));// move
                    Chain.bound(player).name("BarbarianRopeSwing3Task").runFn(1, () -> {
                        player.getPacketSender().sendObjectAnimation(obj, 55);// make the rope go forwards while we swing
                        putStage(player, 1);
                        player.skills().addXp(Skills.AGILITY, 22.0);
                        player.unlock();
                    });
                });
                return true;
            }

            if (obj.getId() == LOG_BALANCE) {
                // Get in position
                player.smartPathTo(new Tile(2551, 3546));
                Chain.bound(player).name("BarbarianLogBalanceTask").waitForTile(new Tile(2551, 3546), () -> {
                    player.lockDelayDamage();
                    player.message("You walk carefully across the slippery log...");
                    player.agilityWalk(false);
                    player.getMovementQueue().clear();
                    player.getMovementQueue().interpolate(new Tile(2541, 3546), MovementQueue.StepType.FORCED_WALK);
                    player.looks().render(763, 762, 762, 762, 762, 762, -1);
                    boolean success = successful(player);
                    if (success) {
                        Chain.bound(player).waitForTile(new Tile(2541, 3546), () -> {
                            player.agilityWalk(true);
                            player.looks().resetRender();
                            putStage(player, 2);
                            player.skills().addXp(Skills.AGILITY, 13.7);
                            player.message("...You make it safely to the other side.");
                            player.unlock();
                        });
                    } else {
                        Chain.bound(player).runFn(3, () -> {
                            player.getMovementQueue().clear();
                            player.animate(771);
                            player.message("...You loose your footing and fall into the water. Something bites you.");
                        }).then(1, () -> {
                            player.looks().render(772, 772, 772, 772, 772, 772, -1);
                            player.graphic(68);
                            player.teleport(player.tile().transform(0, 1, 0));
                        }).then(1, () -> {
                            player.hit(player,Utils.random(5, 7));
                            player.teleport(new Tile(2544, 3549));
                            player.agilityWalk(true);
                            player.looks().resetRender();
                            player.unlock();
                        });
                    }
                });
                return true;
            }

            if (obj.getId() == NET) {
                if (player.tile().x < 2539) {
                    player.message("You can't climb the net from here.");
                    return true;
                }
                Ladders.ladderUp(player, player.tile().transform(-1, 0, 1), true);
                putStage(player, 4);
                player.skills().addXp(Skills.AGILITY, 8.2);
                return true;
            }

            if (obj.getId() == LEDGE) {
                Chain.bound(player).name("BarbarianLedgeTask").waitForTile(new Tile(2536, 3547, 1), () -> {
                    player.lockDelayDamage();
                    player.animate(753);
                    player.face(new Tile(0, player.tile().y));
                    player.agilityWalk(false);
                    player.getMovementQueue().clear();
                    player.getMovementQueue().interpolate(2533, 3547, MovementQueue.StepType.FORCED_WALK);
                    player.looks().render(756, 756, 756, 756, 756, 756, -1);
                    boolean success = successful(player);
                    if (success) {
                        Chain.bound(player).name("BarbarianLedge2Task").runFn(3, () -> {
                            player.getMovementQueue().clear();
                            player.getMovementQueue().interpolate(2532, 3546, MovementQueue.StepType.FORCED_WALK);
                        }).waitForTile(new Tile(2532, 3546), () -> {
                            player.agilityWalk(true);
                            player.looks().resetRender();
                            putStage(player, 8);
                            player.skills().addXp(Skills.AGILITY, 22.0);
                            player.unlock();
                        });
                    } else {
                        Chain.bound(player).name("BarbarianLedge2Task").runFn(3, () -> {
                            player.getMovementQueue().clear();
                            player.looks().resetRender();
                            player.animate(766);
                        }).then(1, () -> {
                            player.teleport(2534, 3546, 0);
                            player.hit(player,Utils.random(5, 7));
                        }).then(1, () -> {
                            player.getMovementQueue().clear();
                            Tile end = new Tile(2536, 3546);
                            player.getMovementQueue().walkTo(end);
                            player.agilityWalk(true);
                            player.unlock();
                        });
                    }
                });
                return true;
            }

            if (obj.getId() == LEDGE_LADDER) {
                Ladders.ladderDown(player, player.tile().transform(0, 0, -1), true);
            }

            if (obj.getId() == WALL) {
                if (obj.tile().equals(new Tile(2536, 3553)) || obj.tile().equals(new Tile(2539, 3553)) || obj.tile().equals(new Tile(2542, 3553))) {
                    player.waitForTile(obj.tile().transform(-1, 0, 0), () -> {
                        var end = obj.tile().equals(2542, 3553);
                        if (!player.tile().equals(obj.tile().transform(-1, 0, 0))) {
                            player.getMovementQueue().walkTo(obj.tile().transform(-1, 0, 0));
                        }
                        player.lockDelayDamage();
                        player.animate(839);
                        TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(2, 0), 0, 60, 1))); //Move
                        Chain.bound(player).name("BarbarianWallTask").runFn(1, () -> {
                            player.skills().addXp(Skills.AGILITY, 13.7);
                            player.unlock();
                            int stage = player.getAttribOr(AttributeKey.BARBARIAN_COURSE_STATE, 0);
                            if (end && stage == 15) {
                                player.putAttrib(AttributeKey.BARBARIAN_COURSE_STATE, 0);
                                player.skills().addXp(Skills.AGILITY, 46.2);

                                // Woo! A pet!
                                var odds = (int) (22000.00 * player.getMemberRights().petRateMultiplier());
                                if (World.getWorld().rollDie(odds, 1)) {
                                    UnlockAgilityPet.unlockGiantSquirrel(player);
                                }
                            }
                        });
                    });
                }
                return true;
            }

            if (obj.getId() == DUNGEONLADDER_DOWN) {
                if (obj.tile().equals(new Tile(2884, 3397))) { // Taverly ldader
                    Ladders.ladderDown(player, new Tile(2884, 9798), true);
                } else if (obj.tile().equals(2547, 3551)) { // Barb ladder
                    Ladders.ladderDown(player, new Tile(2548, 9951), true);
                } else if (obj.tile().equals(3088, 3571)) { // air obelisk
                    Ladders.ladderDown(player, new Tile(player.tile().x, player.tile().y + 6400), true);
                } else {
                    player.message("This ladder does not seem to lead anywhere... Odd!");
                }
                return true;
            }
            if (obj.getId() == 42487) {
                Ladders.ladderDown(player, new Tile(2532,3546,0), true);
                return true;
            }
        }
        return false;
    }


}
