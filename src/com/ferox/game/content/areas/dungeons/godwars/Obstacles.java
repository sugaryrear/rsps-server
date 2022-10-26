package com.ferox.game.content.areas.dungeons.godwars;

import com.ferox.game.content.QuestTabInterface;
import com.ferox.game.content.bank_pin.dialogue.BankTellerDialogue;
import com.ferox.game.task.TaskManager;
import com.ferox.game.task.impl.ForceMovementTask;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.movement.MovementQueue;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.ForceMovement;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.route.Direction;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.NpcIdentifiers;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;

import static com.ferox.util.ObjectIdentifiers.*;

public class Obstacles extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            if(obj.getId() == ICE_BRIDGE) {
                if (player.hp() < 70 || player.skills().xpLevel(Skills.HITPOINTS) < 70) {
                    player.message("Without at least 70 Hitpoints, you would never survive the icy water."); // TODO correct message
                    return false;
                }

                if (player.tile().y >= 5344) {
                    Chain.bound(null).runFn(1, () -> {
                        // Go to the right spot if we're not there
                        if (!player.tile().equals(obj.tile().transform(0, 0, 0))) {
                            player.smartPathTo(obj.tile().transform(0, 0, 0));
                        }
                    }).then(1, () -> {
                     //   player.teleport(2885, 5343, 2);
                        player.graphic(68);
                        player.looks().render(6993, 6993, 6993, 6993, 6993, 6993, 6993);
                        player.getMovementQueue().clear();
                        player.getMovementQueue().interpolate(new Tile(2885,5332), MovementQueue.StepType.FORCED_WALK);
                    }).then(6, () -> {
                        player.looks().resetRender();
                       // player.teleport(2885, 5332, 2);
                        player.message("Dripping, you climb out of the water.");
                    });
                } else {
                    Chain.bound(null).runFn(1, () -> {
                        // Go to the right spot if we're not there
                        if (!player.tile().equals(obj.tile().transform(0, 0, 0))) {
                            player.smartPathTo(obj.tile().transform(0, 0, 0));
                        }
                    }).then(1, () -> {

//                        player.teleport(2885, 5334, 2);
                        player.graphic(68);
                        player.looks().render(6993, 6993, 6993, 6993, 6993, 6993, 6993);
//                     //   player.getMovementQueue().step(player.tile().x, 5345, MovementQueue.StepType.FORCED_WALK);
                        player.getMovementQueue().clear();
                        player.getMovementQueue().interpolate(new Tile(2885,5345), MovementQueue.StepType.FORCED_WALK);
                    }).then(6, () -> {
                     //   player.teleport(2885, 5345, 2);
                        if (player.skills().level(Skills.PRAYER) > 0) {
                            player.message("Dripping, you climb out of the water.");
                            player.message("The extreme evil of this area leaves your Prayer drained.");
                            player.skills().setLevel(Skills.PRAYER, 0);
                            player.looks().resetRender();


                        } else {
                            player.message("Dripping, you climb out of the water.");
                        }
                    });
                }
                return true;
            }
            if (obj.getId() == 42936) {
                QuestTabInterface.scoreboard_nex(player);

                return true;
            }
            if(obj.getId() == PILLAR_26380) {//to armadyl
                int weapon = player.getEquipment().hasWeapon() ? player.getEquipment().getWeapon().getId() : -1;
                String wepName = weapon == -1 ? "" : new Item(weapon).name().toLowerCase();
                boolean hasCrossbow = wepName.contains("c'bow") || wepName.contains("crossbow");

                Tile toarmadyl = new Tile(2871,5279);
                Tile fromarmadyl = new Tile(2871,5269);
                if (player.tile().y >= 5270) {
                    player.smartPathTo(toarmadyl);
                    Chain.bound(player).name("ArmadylEerieJumpTask").waitForTile(toarmadyl, () -> {
                        if (player.inventory().contains(9418) && hasCrossbow && player.skills().level(Skills.RANGED) >= 70) {

                            player.lockDelayDamage();
                            player.message("You grapple across the pillar...");
                            player.agilityWalk(false);
                            player.getMovementQueue().clear();
                            player.getMovementQueue().interpolate(fromarmadyl, MovementQueue.StepType.FORCED_WALK);
                            player.looks().render(763, 762, 762, 762, 762, 762, -1);

                            Chain.bound(player).waitForTile(fromarmadyl, () -> {
                                player.agilityWalk(true);
                                player.looks().resetRender();

                                player.message("...You make it safely to the other side.");
                                player.unlock();
                            });

                        } else {
                            player.message("You need to be wearing a crossbow, have a mith grapple, and 70 ranged to grapple this.");
                            return;
                        }
                    });

                } else {
                    Chain.bound(player).name("ArmadylEerieJumpTask").waitForTile(fromarmadyl, () -> {
                        if (player.inventory().contains(9418) && hasCrossbow && player.skills().level(Skills.RANGED) >= 70) {

                            player.lockDelayDamage();
                            player.message("You grapple across the pillar...");
                            player.agilityWalk(false);
                            player.getMovementQueue().clear();
                            player.getMovementQueue().interpolate(toarmadyl, MovementQueue.StepType.FORCED_WALK);
                            player.looks().render(763, 762, 762, 762, 762, 762, -1);

                            Chain.bound(player).waitForTile(toarmadyl, () -> {
                                player.agilityWalk(true);
                                player.looks().resetRender();

                                player.message("...You make it safely to the other side.");
                                player.unlock();
                            });

                        } else {
                            player.message("You need to be wearing a crossbow, have a mith grapple, and 70 ranged to grapple this.");
                            return;
                        }
                    });
                }
                return true;
            }
            if(obj.getId() == 26561) {//first object to saradomin enclave
                boolean saradominrope = player.getAttribOr(AttributeKey.FIRST_SARADOMIN_ROPE, false);
                if (!saradominrope) {
                    if (player.inventory().contains(954)) {
                        player.putAttrib(AttributeKey.FIRST_SARADOMIN_ROPE, true);
                        player.inventory().remove(new Item(954, 1), true);
                        player.message("You hang the rope off the rock.");

                    } else {
                        player.message("You need a rope to go down.");

                    }
                } else {
                    Chain.bound(null).runFn(1, () -> player.teleport(new Tile(2915, 5300, 1)));

                }
                return true;

            }
            if(obj.getId() == 26562) {//second object to saradomin enclave
                boolean saradominrope2 = player.getAttribOr(AttributeKey.SECOND_SARADOMIN_ROPE, false);
                if (!saradominrope2) {
                    if (player.inventory().contains(954)) {
                        player.putAttrib(AttributeKey.SECOND_SARADOMIN_ROPE, true);
                        player.inventory().remove(new Item(954, 1), true);
                        player.message("You hang the rope off the rock.");

                    } else {
                        player.message("You need a rope to go down.");

                    }
                } else {
                    Chain.bound(null).runFn(1, () -> player.teleport(new Tile(2919, 5273, 0)));

                }
                return true;

            }
                if(obj.getId() == BIG_DOOR) {

                    if (player.tile().x >= 2851) {
                        if (!(player.inventory().contains(2347) && player.skills().level(Skills.STRENGTH) >= 70)) {
                            player.message("You need a hammer and 70 strength to ring the gong.");
                            return false;
                        }
                    }
                        Chain.bound(null).runFn(1, () -> {
                            GameObject opendoor = new GameObject(obj.getId(), obj.tile(), obj.getType(), 1);
                            GameObject closedoor = new GameObject(opendoor.getId(), opendoor.tile(), opendoor.getType(), 0);
                            ObjectManager.openAndCloseDoor(opendoor, closedoor);
                        }).then(1, () -> {
                            //Walk trough
                            player.getMovementQueue().interpolate(new Tile(player.tile().x>=2851 ?obj.tile().x - 1 : obj.tile().x+1, obj.tile().y), MovementQueue.StepType.FORCED_WALK);
                        });

                return true;
            }
            if(obj.getId() == 42840 || obj.getId() == 42841) {
                boolean frozendoor = player.getAttribOr(AttributeKey.FROZEN_DOOR, false);
if(!frozendoor){
    if(player.inventory().contains(26356)){
        player.putAttrib(AttributeKey.FROZEN_DOOR, true);
        player.inventory().remove(new Item(26356, 1), true);
        player.message("@red@You have unlocked the entrance to the Zaros encampment!");
        player.teleport(2881, 5227, 0);

    } else {
        player.message("You need a Frozen key to enter the Zaros encampment.");

    }
} else {
    player.teleport(2881, 5227, 0);
}
                return true;
            }
            if(obj.getId() == 42934) {//door into nex waiting room

                        Chain.bound(null).runFn(1, () -> {
                            GameObject opendoor = new GameObject(obj.getId(), obj.tile(), obj.getType(), 1);
                            GameObject closedoor = new GameObject(opendoor.getId(), opendoor.tile(), opendoor.getType(), 2);
                            ObjectManager.openAndCloseDoor(opendoor, closedoor);
                          //  player.getMovementQueue().walkTo(player.tile().x < 2899 ? new Tile(obj.tile().x+3,obj.tile().y) :  new Tile(obj.tile().x-3,obj.tile().y));
                        }).then(1, () -> {
                            player.getMovementQueue().interpolate(player.tile().x<2899 ? new Tile(obj.tile().x+1,obj.tile().y) : new Tile(obj.tile().x-1,obj.tile().y), MovementQueue.StepType.FORCED_WALK);

                        });


                return true;

            }

            if(obj.getId() == 42931 || obj.getId() == 42932) {
                player.teleport(2883,5280,2);
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        int npcId = npc.id();

        if (npcId == NpcIdentifiers.ASHUELOT_REIS) {
            if(option == 1) {
                player.getDialogueManager().start(new BankTellerDialogue(), npc);
            } else if(option == 2) {
                player.getBank().open();
            }
            return true;
        }
        return false;
    }
}
