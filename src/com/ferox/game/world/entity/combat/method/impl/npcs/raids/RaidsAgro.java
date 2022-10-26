package com.ferox.game.world.entity.combat.method.impl.npcs.raids;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.mob.npc.AggressionCheck;

public class RaidsAgro implements AggressionCheck {

    @Override
    public boolean shouldAgro(Mob mob, Mob victim) {
        return true;
    }
}
