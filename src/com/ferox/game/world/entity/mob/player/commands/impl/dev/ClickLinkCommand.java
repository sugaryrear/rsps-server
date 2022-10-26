package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.util.Utils;

public class ClickLinkCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        String msg = "";
        String link = parts[1];
        for (int i = 2; i < parts.length; i++) {
            msg += (i == 2 ? "" : " ") + parts[i];
        }
        player.message("<link=" + link + ">" + " " + Utils.capitalizeFirst(msg));
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
