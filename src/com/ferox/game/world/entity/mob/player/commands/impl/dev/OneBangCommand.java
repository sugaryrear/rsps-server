package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.util.Color;

public class OneBangCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        boolean oneHitMob = player.getAttribOr(AttributeKey.ONE_HIT_MOB, false);

        oneHitMob =! oneHitMob;
        player.putAttrib(AttributeKey.ONE_HIT_MOB, oneHitMob);

        String plural = oneHitMob ? "activated" : "deactivated";
        player.message(String.format("One hitting npcs and players is now: "+Color.GREEN.tag()+" "+plural, oneHitMob));
        if(oneHitMob)
            player.putAttrib(AttributeKey.ALWAYS_HIT, 0);
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
