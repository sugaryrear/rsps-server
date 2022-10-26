package com.ferox.game.content.title.req.impl.pk;

import com.ferox.game.content.title.req.TitleRequirement;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;

/**
 * Created by Kaleem on 25/03/2018.
 */
public class DeathRequirement extends TitleRequirement {

    private final int deaths;

    public DeathRequirement(int deaths) {
        super("Die " + deaths + " times in the <br>Wilderness to a player");
        this.deaths = deaths;
    }

    @Override
    public boolean satisfies(Player player) {
        int deaths = player.getAttribOr(AttributeKey.PLAYER_DEATHS, 0);
        return deaths >= this.deaths;
    }
}
