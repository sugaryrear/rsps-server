package com.ferox.game.content.areas.alkharid;

import com.ferox.game.content.areas.burthope.warriors_guild.CyclopsRoom;
import com.ferox.game.task.Task;
import com.ferox.game.task.TaskManager;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.position.Tile;
import com.ferox.util.ItemIdentifiers;

import static com.ferox.util.CustomItemIdentifiers.RING_OF_VIGOUR;
import static com.ferox.util.ItemIdentifiers.WARRIOR_GUILD_TOKEN;

public class SmokeDungeon {
    private static boolean insideSmokeDungeon(Tile tile) {
        return tile.inArea(3162, 9339, 3329, 9407);
    }
    public static void startSmokeDungeonTask(Player player) {
        TaskManager.submit(new Task("handle_time_spent_task", 1) {
            int internalTimer = 10;

            @Override
            protected void execute() {
                //Player is offline
                if (!player.isRegistered()) {
                    stop();
                }

                //Not inside the dungeon
                if (!insideSmokeDungeon(player.tile())) {
                    stop();
                }

                if (internalTimer-- == 0) {
                    if(!player.getEquipment().hasAt(EquipSlot.HEAD, ItemIdentifiers.FACEMASK)) {
                        player.hit(player,5);
                        player.message("You take damage from the smoke.");
                    }
                    internalTimer = 10;//Reset timer
                }
            }
        });
    }

}
