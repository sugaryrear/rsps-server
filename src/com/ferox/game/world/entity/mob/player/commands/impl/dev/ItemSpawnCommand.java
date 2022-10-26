package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.GameServer;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.IronMode;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.items.Item;
import com.ferox.util.Color;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

import static com.ferox.game.GameConstants.PVP_ALLOWED_SPAWNS;
import static com.ferox.util.CustomItemIdentifiers.*;

public class ItemSpawnCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (player.ironMode() != IronMode.NONE) {
            player.message("As an ironman you cannot use this command.");
            return;
        }

        int amount = 1;
        if (parts.length < 1 || (!StringUtils.isNumeric(parts[1]) || (parts.length > 2 && !StringUtils.isNumeric(parts[2])))) {
            player.message("Invalid syntax. Please use: ::item [ID] (amount)");
            player.message("Example: ::item 385 or ::item 385 20");
            return;
        }
        if (parts.length > 2) {
            amount = Integer.parseInt(parts[2]);
        }
        int id = Integer.parseInt(parts[1]);

       Item item = new Item(id);

        if(item.getId() > 34_000) {
            player.message("Item id not supported, this item doesn't exist.");
            return;
        }

        if (!player.canSpawn() && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            player.message("You can't spawn items here.");
            return;
        }

        if (Item.valid(item) && (player.getPlayerRights().isDeveloperOrGreater(player) || item.definition(World.getWorld()).pvpAllowed)) {
            player.getInventory().add(new Item(id, amount));
            player.message("You have just spawned x"+amount+" "+new Item(Integer.parseInt(parts[1])).unnote().name()+".");
        }
    }

    @Override
    public boolean canUse(Player player) {
        if (!GameServer.properties().pvpMode) {
            return (player.getPlayerRights().isDeveloperOrGreater(player));
        }
        return true;
    }
}
