package com.ferox.game.world.entity.combat.method.impl.npcs.bosses;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;

public class DagannothSupreme extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        mob.animate(2855);
        new Projectile(mob, target, 475, 30, 45, 30, 25, 0,10,5).sendProjectile();
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), 1, CombatType.RANGED).checkAccuracy().submit();
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
