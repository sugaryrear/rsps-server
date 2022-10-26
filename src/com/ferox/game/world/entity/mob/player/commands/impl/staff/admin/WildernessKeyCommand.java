package com.ferox.game.world.entity.mob.player.commands.impl.staff.admin;

import com.ferox.game.content.areas.wilderness.content.key.WildernessKeyLocation;
import com.ferox.game.content.areas.wilderness.content.key.WildernessKeyPlugin;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Patrick van Elderen | November, 25, 2020, 18:56
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class WildernessKeyCommand implements Command {

    private static final Logger log = LoggerFactory.getLogger(WildernessKeyCommand.class);

    @Override
    public void execute(Player player, String command, String[] parts) {
        WildernessKeyLocation location = WildernessKeyPlugin.spawnKey();
        if (location != null) {
            //log.trace("Key location selected: {} (absolute: {}).", location, location.tile());
        } else {
            player.message("There is already a wilderness spawned.");
        }
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isAdminOrGreater(player);
    }
}
