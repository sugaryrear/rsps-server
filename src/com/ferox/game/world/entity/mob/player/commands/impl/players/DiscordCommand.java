package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

public class DiscordCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getPacketSender().sendURL("https://discord.com/invite/mqQ9DxVMMd");
        player.message("Opening https://discord.com/invite/mqQ9DxVMMd in your web browser...");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
