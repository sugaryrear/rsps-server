package com.ferox.game.content.areas.zeah.woodcutting_guild;

import com.ferox.game.content.packet_actions.interactions.objects.Ladders;
import com.ferox.game.content.skill.impl.agility.MarksOfGrace;
import com.ferox.game.task.TaskManager;
import com.ferox.game.task.impl.TickAndStop;
import com.ferox.game.world.entity.dialogue.DialogueManager;
import com.ferox.game.world.entity.dialogue.Expression;
import com.ferox.game.world.entity.mob.movement.MovementQueue;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Area;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.chainedwork.Chain;

import static com.ferox.util.ObjectIdentifiers.*;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 20, 2020
 */
public class WoodcuttingGuild extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            //entrance
            if(obj.getId() == GATE_28851 || obj.getId() == GATE_28852) {
                enterGuild(player);
                return true;
            }

            // Rope ladders up
            if(obj.getId() == ROPE_LADDER_28857) {
                Ladders.ladderUp(player, player.tile().x >= 1571 ? obj.tile().transform(-1, 0, 1) : obj.tile().transform(1, 0, 1),true);
                return true;
            }

            // Rope ladders down
            if(obj.getId() == ROPE_LADDER_28858) {
                Tile startPos = obj.tile().transform(-1, 0);
                player.smartPathTo(startPos);
                player.waitForTile(startPos, () -> {
                    Ladders.ladderUp(player, player.tile().x >= 1571 ? obj.tile().transform(1, 0, -1) : obj.tile().transform(-1, 0, -1),true);
                });
                return true;
            }

            // Dungeon exit
            if(obj.getId() == VINE_28856) {
                Ladders.ladderUp(player, new Tile(1606, 3508),true);
                return true;
            }

            // Dungeon entrance
            if(obj.getId() == CAVE_28855) {
                player.teleport(1596, 9900);
                return true;
            }
            if(obj.getId() == 26720) {
                Tile endtile =  new Tile(player.getAbsX() > 1589 ? 1588 : 1590,player.getAbsY());
                player.lockNoDamage();
                Chain.bound(player).name("VarrockClotheslineTask").runFn(1, () -> {

                    player.agilityWalk(false);
                    player.getMovementQueue().clear();
                    player.getMovementQueue().interpolate(endtile, MovementQueue.StepType.FORCED_WALK);
                    player.looks().render(839, 839, 839, 839, 839, 839, -1);
                }).waitForTile(endtile, () -> {
                    player.unlock();
                    player.agilityWalk(true);
                    player.looks().resetRender();
                });
                return true;
            }
        }
        return false;
    }

    public static Area AREA_EAST = new Area(1608, 3487, 1655, 3517);
    public static Area AREA_WEST = new Area(1564, 3477, 1600, 3503);

    private void enterGuild(Player player) {
        if (player.skills().xpLevel(Skills.WOODCUTTING) < 60) {
            DialogueManager.sendStatement(player, "You need a Woodcutting level of 60 to access this guild.");
        } else {
            ObjectManager.removeObj(new GameObject(GATE_28851, new Tile(1657, 3505), 0, 2));
            ObjectManager.removeObj(new GameObject(GATE_28852, new Tile(1657, 3504), 0, 2));

            ObjectManager.addObj(new GameObject(28853, new Tile(1658, 3505), 0, 1));
            ObjectManager.addObj(new GameObject(28854, new Tile(1658, 3504), 0, 3));

            if (player.tile().x >= 1658) {
                player.getMovementQueue().walkTo(new Tile(1657, player.tile().y));
            } else {
                player.getMovementQueue().walkTo(new Tile(1658, player.tile().y));
            }

            TaskManager.submit(new TickAndStop(2) {
                @Override
                public void executeAndStop() {
                    ObjectManager.removeObj(new GameObject(28853, new Tile(1658, 3505), 0, 1));
                    ObjectManager.removeObj(new GameObject(28854, new Tile(1658, 3504), 0, 3));

                    ObjectManager.addObj(new GameObject(GATE_28851, new Tile(1657, 3505), 0, 2));
                    ObjectManager.addObj(new GameObject(GATE_28852, new Tile(1657, 3504), 0, 2));
                }
            });
            if (player.tile().x >= 1658)
                DialogueManager.npcChat(player, Expression.HAPPY, 7235, "Welcome to the woodcutting guild, adventurer.");
        }
    }
}
