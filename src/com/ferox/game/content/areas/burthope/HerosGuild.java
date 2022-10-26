package com.ferox.game.content.areas.burthope;

import com.ferox.game.content.areas.home.dialogue.FairyFixitD;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.dialogue.Expression;
import com.ferox.game.world.entity.mob.movement.MovementQueue;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.chainedwork.Chain;

import static com.ferox.util.NpcIdentifiers.*;

public class HerosGuild extends PacketInteraction {
    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int opt) {
        if (obj.getId() == 2624 || obj.getId() == 2625) {
            if (opt == 1) {
                passThrough(player);
                return true;
            }

        }
        return false;
    }
    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if(option == 1) {
            if(npc.id() == HELEMOS) {
                World.getWorld().shop(54).open(player);
                return true;
            }
        }
        return false;
    }
    private void passThrough(Player player) {
        if (player.skills().level(Skills.ATTACK) < 60) {
            player.message("You need an attack level of 60 to enter the Hero's Guild.");
            return;
        }
        GameObject obj = player.getAttribOr(AttributeKey.INTERACTION_OBJECT, null);

        player.lock();

        ObjectManager.removeObj(new GameObject(2625, new Tile(2902,3511), 0, 0));
        ObjectManager.removeObj(new GameObject(2624, new Tile(2902,3510), 0, 0));
        ObjectManager.addObj(new GameObject(2625, new Tile(2902,3511), 0, 1));
        ObjectManager.addObj(new GameObject(2624, new Tile(2902,3510), 0, 3));

        player.sound(69);

        if (player.tile().x <= 2901) {
            player.getMovementQueue().step(obj.tile().x+1, obj.tile().y, MovementQueue.StepType.FORCED_WALK);
        } else {
            player.getMovementQueue().step(obj.tile().x-1, obj.tile().y, MovementQueue.StepType.FORCED_WALK);
        }

        Chain.bound(player).name("HerosGuildDoorTask").runFn(2, () -> {
            ObjectManager.removeObj(new GameObject(2625, new Tile(2902,3511), 0, 1));
            ObjectManager.removeObj(new GameObject(2624, new Tile(2902,3510),  0, 3));
            ObjectManager.addObj(new GameObject(2625, new Tile(2902,3511), 0, 0));
            ObjectManager.addObj(new GameObject(2624, new Tile(2902,3510), 0, 0));
        }).then(1, player::unlock);
    }
}
