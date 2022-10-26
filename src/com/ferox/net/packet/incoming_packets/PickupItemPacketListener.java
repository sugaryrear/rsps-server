package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.game.task.Task;
import com.ferox.game.task.TaskManager;
import com.ferox.game.task.impl.DistancedActionTask;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.ground.GroundItem;
import com.ferox.game.world.items.ground.GroundItemHandler;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;
import com.ferox.util.Color;

import java.util.Optional;

import static com.ferox.util.CustomItemIdentifiers.WILDERNESS_KEY;

/**
 * This packet listener is used to pick up ground items
 * that exist in the world.
 *
 * @author Patrick van Elderen | 13 feb. 2019 : 12:57:23
 * @see <a href="https://www.rune-server.ee/members/_Patrick_/">Rune-Server profile</a>
 */
public class PickupItemPacketListener implements PacketListener {

    /**
     * Handles picking up the item
     *
     * @param player   The {@link Player} picking up the item
     * @param item       The item
     * @param tile The coordinates of the item
     */
    public static void pickup(Player player, Item item, Tile tile, boolean telekineticgrab) {

        Optional<GroundItem> groundItem = GroundItemHandler.getGroundItem(item.getId(), tile, player);
        if (groundItem.isPresent()) {
            GroundItemHandler.pickup(player, item.getId(), tile, telekineticgrab);
        }
    }

    /**
     * @param player   The {@link Player}
     * @param tile The coordinates of the spot
     * @return If the player is standing on this spot
     */
    private boolean onSpot(Player player, Tile tile, int itemId) {
        return player.getX() == tile.getX() && player.getY() == tile.getY() && player.getZ() == tile.getLevel() ||
            (itemId == 13307 && tile.getX() == 3018 && tile.getY() == 10353) ||
            (itemId == 13307 && tile.getX() == 2950 && tile.getY() == 3824) ||
            (itemId == 542 && tile.getX() == 3059 && tile.getY() == 3488) ||
            (itemId == 544 && tile.getX() == 3059 && tile.getY() == 3487) ||
            (itemId == 542 && tile.getX() == 3044 && tile.getY() == 3488) ||
            (itemId == 544 && tile.getX() == 3044 && tile.getY() == 3487);
    }

    public static void respawn(Item item, Tile position, int ticks) {
        TaskManager.submit(new Task("PickupItemPacketListener_respawn_task", ticks) {
            @Override
            protected void execute() {
                GroundItem next = new GroundItem(item, position.copy(), null);
                GroundItemHandler.createGroundItem(next);
                stop();
            }
        });
    }

    @Override
    public void handleMessage(final Player player, Packet packet) {
        final int y = packet.readLEShort();
        final int itemId = packet.readShort();
        final int x = packet.readLEShort();
        final Tile tile = new Tile(x, y, player.tile().getLevel());

        if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
            player.getBankPin().openIfNot();
            return;
        }

        if(player.askForAccountPin()) {
            player.sendAccountPinMessage();
            return;
        }

        if (player.busy()) {
            return;
        }

        // Make sure distance isn't way off..
        if (Math.abs(player.tile().getX() - x) > 25 || Math.abs(player.tile().getY() - y) > 25) {
            player.getMovementQueue().clear();
            return;
        }

        if (!player.locked() && !player.dead()) {
            boolean newAccount = player.getAttribOr(AttributeKey.NEW_ACCOUNT, false);
            if (newAccount) {
                player.message("You have to select your game mode before you can continue.");
                return;
            }

            if (GameServer.properties().debugMode && player.getPlayerRights().isDeveloperOrGreater(player)) {
                player.debugMessage(String.format("pickup item - Item: %d Location: %s", itemId, tile));
            }

            player.stopActions(false);
            player.skills().stopSkillable();
            player.getCombat().reset();

            player.putAttrib(AttributeKey.INTERACTED_GROUNDITEM, itemId);
            player.putAttrib(AttributeKey.INTERACTION_OPTION, 3);

            //Do actions...

            // If whoever froze you is out of viewport, you get unfrozen when interacting with items.
            CombatFactory.unfreezeWhenOutOfRange(player);

            if (onSpot(player, tile, itemId)) {
                if(itemId == WILDERNESS_KEY) {
                    if(player.skills().combatLevel() < 40) {
                        player.message(Color.RED.wrap("You need to be at least level 40 to pickup this key."));
                        return;
                    }
                }
                pickup(player, new Item(itemId), tile, false);
            } else {
                player.setDistancedTask(new DistancedActionTask() {

                    @Override
                    public void onReach() {
                        if(itemId == WILDERNESS_KEY) {
                            if(player.skills().combatLevel() < 40) {
                                player.message(Color.RED.wrap("You need to be at least level 40 to pickup this key."));
                                stop();
                                return;
                            }
                        }
                        pickup(player, new Item(itemId), tile, false);
                        stop();
                    }

                    @Override
                    public boolean reached() {
                        return onSpot(player, tile, itemId);
                    }
                });
            }
        }
    }
}
