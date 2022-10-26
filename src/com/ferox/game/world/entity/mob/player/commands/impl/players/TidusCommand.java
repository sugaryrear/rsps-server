package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

public class TidusCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getPacketSender().sendURL("https://www.youtube.com/channel/UC2vFpv7IIOC_LZbmYrmrNzg");
        player.message("Opening Tidus Cross channel in your web browser...");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
