package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

public class ToggleDebugCommand implements Command {


    public void execute(Player player, String command, String[] parts) {
        boolean debugMessages = player.getAttribOr(AttributeKey.DEBUG_MESSAGES, true);
        debugMessages = !debugMessages;
        player.putAttrib(AttributeKey.DEBUG_MESSAGES, debugMessages);
        player.message("Your debug messages are now " + (debugMessages ? "enabled." : "disabled."));
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
