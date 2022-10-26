package com.ferox.game.content.members;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.mob.npc.AggressionCheck;

public class MemberCaveAgro implements AggressionCheck {

    @Override
    public boolean shouldAgro(Mob mob, Mob victim) {
        return !mob.tile().memberCave();
    }
}
