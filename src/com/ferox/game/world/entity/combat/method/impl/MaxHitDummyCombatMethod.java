package com.ferox.game.world.entity.combat.method.impl;

import com.ferox.game.world.entity.Mob;

/**
 * @author Patrick van Elderen | February, 14, 2021, 12:08
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class MaxHitDummyCombatMethod extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {

    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return 0;
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 0;
    }
}
