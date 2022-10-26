package com.ferox.game.content.areas.fishing_guild;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.dialogue.DialogueManager;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.chainedwork.Chain;

import static com.ferox.util.NpcIdentifiers.DOMINIC_ONION;
import static com.ferox.util.ObjectIdentifiers.DOOR_20925;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 21, 2020
 */
public class FishingGuild extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            //System.out.println("fishing object " + obj.toString());
            if(obj.getId() == DOOR_20925) {
                int change = player.tile().y >= 3394 ? -1 : 1;
                if (change == 1 && player.skills().level(Skills.FISHING) < 68) {
                    DialogueManager.sendStatement(player,"You do not meet the level 68 Fishing requirement to enter the Guild.");
                    return false;
                }

                GameObject old = new GameObject(obj.getId(), obj.tile(), obj.getType(), 3);
                GameObject spawned = new GameObject(obj.getId(), obj.tile(), obj.getType(), 4);
                ObjectManager.replace(old, spawned, 1);

                player.getMovementQueue().walkTo(new Tile(player.tile().x, player.tile().y + change));
                player.lockNoDamage();
                Chain.bound(null).runFn(1, () -> {
                    player.unlock();
                    String plural = change == -1 ? "leave" : "enter";
                    player.message("You "+plural+" the Fishing Guild.");
                });
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if (option == 1) {
            if (npc.id() == 8684) {
                World.getWorld().shop(78).open(player);
                return true;
            }
        }
        if (option == 2) {
            if (npc.id() == 8684) {
                World.getWorld().shop(78).open(player);
                return true;
            }
        }
        return false;
    }
}
