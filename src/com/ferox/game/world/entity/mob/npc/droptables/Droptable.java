package com.ferox.game.world.entity.mob.npc.droptables;

import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.ground.GroundItem;
import com.ferox.game.world.items.ground.GroundItemHandler;
import com.ferox.game.world.position.Tile;

import static com.ferox.game.content.collection_logs.LogType.BOSSES;
import static com.ferox.game.content.collection_logs.LogType.OTHER;

/**
 * Created by Bart on 10/6/2015.
 */
public interface Droptable {

    public void reward(Npc npc, Player killer);

    default void drop(Npc npc, Player player, Item item) {
        drop(npc, npc.tile(), player, item);
    }

    default void drop(Npc npc, Tile tile, Player player, Item item) {
        if (player.nifflerPetOut() && player.nifflerCanStore() && item.getValue() > 0) {
            player.nifflerStore(item);
        } else {
            GroundItemHandler.createGroundItem(new GroundItem(item, tile, player));
        }
        BOSSES.log(player, npc.id(), item);
        OTHER.log(player, npc.id(), item);
    }

}
