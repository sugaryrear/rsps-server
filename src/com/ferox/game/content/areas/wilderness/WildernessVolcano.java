package com.ferox.game.content.areas.wilderness;

import com.ferox.game.task.TaskManager;
import com.ferox.game.task.impl.ForceMovementTask;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.DialogueManager;
import com.ferox.game.world.entity.mob.FaceDirection;
import com.ferox.game.world.entity.mob.movement.MovementQueue;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.ForceMovement;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;

public class WildernessVolcano extends PacketInteraction {

    private enum Shield {
        MALEDICTION_WARD(new Item(11931), new Item(11932), new Item(11933), new Item(11924), "Malediction"),
        ODIUM_WARD(new Item(11928), new Item(11929), new Item(11930), new Item(11926), "Odium");

        private final Item shard_1, shard_2, shard_3, shield;
        private final String item_name;

        Shield(Item shard_1, Item shard_2, Item shard_3, Item shield, String item_name) {
            this.shard_1 = shard_1;
            this.shard_2 = shard_2;
            this.shard_3 = shard_3;
            this.shield = shield;
            this.item_name = item_name;
        }
    }

    private static final int GOLD_RING = 1635;

    @Override
    public boolean handleItemOnObject(Player player, Item item, GameObject object) {
        if(object.getId() == 26755) {
            var forge = player.<GameObject>getAttrib(AttributeKey.INTERACTION_OBJECT);
            var goblin = new Npc(3028, new Tile(player.tile().x - 1, player.tile().y));
            if (item.getId() == GOLD_RING) {
                player.lock();
                if (!player.tile().equals(forge.tile().transform(0, -1, 0))) {
                    player.getMovementQueue().interpolate(forge.tile().transform(0, -1, 0), MovementQueue.StepType.FORCED_WALK);
                }

                player.waitForTile(forge.tile().transform(0, -1, 0), () -> {
                    World.getWorld().registerNpc(goblin);
                    goblin.face(player.tile());
                    goblin.forceChat("My Precious!!! NOOOOO!!!");
                }).then(1, () -> {
                    goblin.animate(6184);
                    goblin.respawns(false);
                }).then(2, () -> {
                    World.getWorld().unregisterNpc(goblin);
                    player.unlock();
                });
                return true;
            } else {
                for(Shield shield : Shield.values()) {
                    if (item.getId() == shield.shard_1.getId() || item.getId() == shield.shard_2.getId() || item.getId() == shield.shard_3.getId()) {
                        if (player.inventory().contains(shield.shard_1) && player.inventory().contains(shield.shard_2) &&
                            player.inventory().contains(shield.shard_3)) {
                            player.lock();
                            if (!player.tile().equals(forge.tile().transform(0, -1, 0))) {
                                player.getMovementQueue().interpolate(forge.tile().transform(0, -1, 0), MovementQueue.StepType.FORCED_WALK);
                            }
                            player.waitForTile(forge.tile().transform(0, -1, 0), () -> {
                                player.runFn(2, () -> {
                                    DialogueManager.sendStatement(player, "You drop the three shield shards into the mouth of the volcanic", "chamber of fire.");
                                    player.animate(4411);
                                }).then(1, () -> {
                                    TaskManager.submit(new ForceMovementTask(player, 3, new ForceMovement(player.tile().clone(), new Tile(0, -1), 45, 126, FaceDirection.NORTH)));
                                }).then(1, () -> {
                                    player.animate(734, 5);
                                }).then(3, () -> {
                                    player.inventory().remove(shield.shard_1);
                                    player.inventory().remove(shield.shard_2);
                                    player.inventory().remove(shield.shard_3);
                                    player.inventory().add(shield.shield);

                                    player.message("You forge the shield pieces together in the chambers of fire and are blown back by");
                                    player.message("the intense heat.");
                                    player.unlock();
                                });
                            });
                        } else {
                            player.message("You need all three " + shield.item_name + " shards to forge a shield.");
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
