package com.ferox.game.content.title.req.impl.other;

import com.ferox.game.content.title.req.TitleRequirement;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;

/**
 * @author Patrick van Elderen | February, 15, 2021, 18:02
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class RockyBalboaRequirement extends TitleRequirement {

    public RockyBalboaRequirement() {
        super("Must have completed<br>Punching bags III");
    }

    @Override
    public boolean satisfies(Player player) {
        return player.<Boolean>getAttribOr(AttributeKey.ROCKY_BALBOA_TITLE_UNLOCKED, false);
    }

}
