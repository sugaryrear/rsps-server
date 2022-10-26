package com.ferox.game.world.entity.mob.player.commands.impl.owner;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.ground.GroundItem;
import com.ferox.game.world.items.ground.GroundItemHandler;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DropPartyCommand implements Command {
    private static final Logger logger = LogManager.getLogger(DropPartyCommand.class);

    @Override
    public void execute(Player player, String command, String[] parts) {
        //Example of command: ::dropparty 7 10 4151
        if (parts.length < 4) {
            player.message("Usage: ::dropparty [radius] [percentageChance] [itemId]");
            return;
        }
        //TODO: finish this drop party command by adding different item types
        final Tile startingTile = player.tile();
        int startX = startingTile.getX();
        int startY = startingTile.getY();
        int areaRadius = Integer.parseInt(parts[1]);
        int spreadChance = Integer.parseInt(parts[2]); // percent
        int itemId = Integer.parseInt(parts[3]); // percent
        int offset = areaRadius; // How many tiles away should items be spawned?
        Tile[] tiles = new Tile[(int) Math.pow((2 * offset + 1), 2) - 1];
        //System.out.println(tiles.length);
        int index = 0;
         for (int x = startX - offset; x <= startX + offset; x++) {
            for (int y = startY - offset; y <= startY + offset; y++) {
                if (x == startX && y == startY) {
                    continue;
                }
                tiles[index] = new Tile(x, y);
                index++;
            }
        }

        for (Tile pos : tiles) {
            logger.info("Spawning for: " + player + " Whos name is : " + player.getUsername());
            if (Utils.percentageChance(spreadChance)) {
                //TODO: possibly replace itemId with different item types as a param, also declare item types as an Item array in this class.
                GroundItemHandler.createGroundItem(new GroundItem(new Item(itemId, 1), pos, player));
            }
        }

        //System.out.println("Index: " + index);
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isAdminOrGreater(player);
    }
}
