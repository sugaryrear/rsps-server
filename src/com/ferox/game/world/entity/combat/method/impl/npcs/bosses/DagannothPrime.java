package com.ferox.game.world.entity.combat.method.impl.npcs.bosses;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;

public class DagannothPrime extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        mob.animate(2854);
        new Projectile(mob, target, 162, 20, 45, 60, 30, 0,10,14).sendProjectile();
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 1, CombatType.MAGIC).checkAccuracy().submit();
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 8;
    }
}
