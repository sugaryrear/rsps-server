package com.ferox.game.content.areas.varrock;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.ObjectIdentifiers;
import com.ferox.util.chainedwork.Chain;

import static com.ferox.util.ItemIdentifiers.MOLE_CLAW;
import static com.ferox.util.ItemIdentifiers.MOLE_SKIN;
import static com.ferox.util.NpcIdentifiers.*;

/**
 * @author Patrick van Elderen | April, 14, 2021, 19:18
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class Castle extends PacketInteraction {

    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if (option == 1) {
            if (npc.id() == ZAFF) {
                World.getWorld().shop(63).open(player);
                return true;
            }
            if (npc.id() == 2884) {
                World.getWorld().shop(64).open(player);
                return true;
            }
            if (npc.id() == HORVIK) {
                World.getWorld().shop(65).open(player);
                return true;
            }
            if (npc.id() == LOWE) {
                World.getWorld().shop(66).open(player);
                return true;
            }
            if (npc.id() == THESSALIA) {
                World.getWorld().shop(69).open(player);
                return true;
            }
        }
        if (option == 2) {
            if (npc.id() == ZAFF) {
                World.getWorld().shop(63).open(player);
                return true;
            }
            if (npc.id() == 2884) {
                World.getWorld().shop(64).open(player);
                return true;
            }
            if (npc.id() == HORVIK) {
                World.getWorld().shop(65).open(player);
                return true;
            }
            if (npc.id() == LOWE) {
                World.getWorld().shop(66).open(player);
                return true;
            }
            if (npc.id() == THESSALIA) {
                World.getWorld().shop(69).open(player);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            if (obj.getId() == ObjectIdentifiers.STAIRCASE_11807) {
                player.lock();
                Chain.bound(player).name("CastleTask1").runFn(1, () -> {
                    player.teleport(new Tile(player.tile().x, 3476, 1));
                    player.unlock();
                });
                return true;
            }
            if (obj.getId() == ObjectIdentifiers.STAIRCASE_11799) {
                player.lock();
                Chain.bound(player).name("CastleTask2").runFn(1, () -> {
                    player.teleport(new Tile(player.tile().x, 3472, 0));
                    player.unlock();
                });
                return true;
            }
        }
        return false;
    }
}
