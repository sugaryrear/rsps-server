package com.ferox.game.world.entity.combat.method.impl.npcs.karuulm;

import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.position.Tile;
import com.ferox.util.timers.TimerKey;

/**
 * @author Patrick van Elderen | December, 22, 2020, 13:39
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class Wyrm extends Npc {

    public static final int IDLE = 8610;
    public static final int ACTIVE = 8611;

    public Wyrm(int id, Tile tile) {
        super(id, tile);
    }

    @Override
    public void sequence() {
        super.sequence();

        var target = getCombat().getTarget();
        if (id() == ACTIVE && !getTimers().has(TimerKey.COMBAT_LOGOUT) && target == null) {
            transmog(IDLE);
            animate(8269);
            getTimers().register(TimerKey.COMBAT_ATTACK,3);
        }
    }

    @Override
    public void onHit(Hit hit) {
        if (id() == ACTIVE) {
            return;
        }

        animate(8268);
        transmog(ACTIVE);
        getTimers().register(TimerKey.COMBAT_ATTACK,3);
    }
}
