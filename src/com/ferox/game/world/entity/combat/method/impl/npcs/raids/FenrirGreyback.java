package com.ferox.game.world.entity.combat.method.impl.npcs.raids;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;

/**
 * @author Patrick van Elderen | May, 13, 2021, 11:55
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class FenrirGreyback extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 1;
    }
}
