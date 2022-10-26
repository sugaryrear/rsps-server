package com.ferox.game.content.areas.burthope.warriors_guild;

import com.ferox.game.content.packet_actions.interactions.objects.Ladders;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.DialogueManager;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.ItemIdentifiers.*;
import static com.ferox.util.ObjectIdentifiers.*;

/**
 * @author Patrick van Elderen | March, 26, 2021, 09:45
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class Basement extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(obj.getId() == DOOR_10043) {
            if(!(player.inventory().containsAny(RUNE_DEFENDER, DRAGON_DEFENDER, DRAGON_DEFENDER_T)) && !player.getEquipment().containsAny(RUNE_DEFENDER, DRAGON_DEFENDER, DRAGON_DEFENDER_T)) {
                DialogueManager.sendStatement(player, "You need at least a rune defender to enter this area.");
                return true;
            }

            if(!player.inventory().contains(WARRIOR_GUILD_TOKEN, 100) && player.getX() < 2912) {
                DialogueManager.sendStatement(player, "You need at least 100 warrior guild tokens to enter this area.");
                return true;
            }
            if (player.inventory().contains(new Item(DRAGON_DEFENDER)) || player.getEquipment().hasAt(EquipSlot.SHIELD, DRAGON_DEFENDER))
                set_item(player, DRAGON_DEFENDER);
            else if (player.inventory().contains(new Item(RUNE_DEFENDER)) || player.getEquipment().hasAt(EquipSlot.SHIELD, RUNE_DEFENDER))
                set_item(player, DRAGON_DEFENDER);
            //TODO add proper doors

            player.teleport(player.getX() < 2912 ? 2912 : 2911, 9968);
            CyclopsRoom.handle_time_spent(player,true);
            return true;
        }

        if (obj.getId() == LADDER_10042) {
            Ladders.ladderDown(player, new Tile(2907, 9968), true);
            return true;
        }
        if (obj.getId() == LADDER_9742) {
            Ladders.ladderUp(player, new Tile(2834, 3542), true);
            return true;
        }
        return false;
    }

    private static void set_item(Player player, int item) {
        player.putAttrib(AttributeKey.WARRIORS_GUILD_CYCLOPS_ROOM_DEFENDER, item);
    }
}
