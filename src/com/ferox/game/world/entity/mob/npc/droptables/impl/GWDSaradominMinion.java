package com.ferox.game.world.entity.mob.npc.droptables.impl;

import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.npc.droptables.Droptable;
import com.ferox.game.world.entity.mob.player.Player;

import static com.ferox.game.world.entity.AttributeKey.GWD_SARADOMIN_KC;

/**
 * @author Patrick van Elderen | April, 29, 2021, 14:23
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class GWDSaradominMinion implements Droptable {

    @Override
    public void reward(Npc npc, Player killer) {
        var current = killer.<Integer>getAttribOr(GWD_SARADOMIN_KC, 0) + 1;
        if (current < 2000) {
            killer.putAttrib(GWD_SARADOMIN_KC, current);
        }
    }
}
