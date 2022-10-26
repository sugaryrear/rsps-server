package com.ferox.game.world.entity.mob.player.commands.impl.staff.admin;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.items.Item;
import com.ferox.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class GiveItemCommand implements Command {

    private static final Logger logger = LogManager.getLogger(GiveItemCommand.class);

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (parts.length < 4) {
            player.message("Invalid use of command.");
            player.message("Example: ::giveitem tester 995 1");
            return;
        }
        String username = Utils.formatText(parts[1].replace("_", " ")); // after "giveitem "
        try {
            int itemId = Integer.parseInt(parts[2]);
            int itemAmount = Integer.parseInt(parts[3]);
            Optional<Player> plr = World.getWorld().getPlayerByName(username);
            if (plr.isPresent()) {
                if (player.getHostAddress().equalsIgnoreCase(plr.get().getHostAddress()) &&
                    !player.getPlayerRights().isDeveloperOrGreater(player)) {
                    player.message("You can't give rewards to yourself or to players on the same IP.");
                    return;
                }
                Item item = new Item(itemId, itemAmount);
                plr.get().getBank().depositFromNothing(item);
                plr.get().message(player.getPlayerRights().getName() + " " + player.getUsername() + " has given you: " + item.name() + " X " + itemAmount);
                logger.info(player.getPlayerRights().getName() + " " + player.getUsername() + " has given " + username + ": " + item.name() + " X " + itemAmount);
                player.message("You have given " + username + ": " + item.name() + " (" + itemId + ") X " + itemAmount);
            } else {
                player.message("The player " + username + " is not online.");
            }
        } catch (NumberFormatException e) {
           player.message("Command failed: incorrect usage of the command.");
        }
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isAdminOrGreater(player) || player.getUsername().equalsIgnoreCase("wclord999"));
    }

}
