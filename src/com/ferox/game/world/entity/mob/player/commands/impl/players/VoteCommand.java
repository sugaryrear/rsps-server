package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.util.Color;

public class VoteCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getPacketSender().sendURL("https://righteouspk.com");
        player.message("Opening https://ferox-os.com/vote/ in your web browser...");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
