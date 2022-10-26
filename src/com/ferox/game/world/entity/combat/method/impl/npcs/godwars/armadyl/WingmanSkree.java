package com.ferox.game.world.entity.combat.method.impl.npcs.godwars.armadyl;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;

public class WingmanSkree extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        mob.animate(6955);
        new Projectile(mob, target, 1201, 25, 65, 80, 25, 0).sendProjectile();
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 2, CombatType.MAGIC).checkAccuracy().submit();
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 10;
    }
}
