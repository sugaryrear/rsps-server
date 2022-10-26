
package com.ferox.game.world.entity.combat.method.impl.npcs.misc;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;

public class Lizardman extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        mob.animate(7193);
        new Projectile(mob, target, 1291, 50, 60, 50, 30, 0).sendProjectile();
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), 2, CombatType.RANGED).checkAccuracy().submit();
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 7;
    }
}
