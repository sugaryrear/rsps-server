package com.ferox.game.world.entity.combat.method.impl.npcs.raids;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;

public class RangedVanguard extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());

        new Projectile(mob, target, 1343, 41, 12 * tileDist, 30, 30, 0, 10, 15).sendProjectile();
        var delay = Math.max(1, 20 + tileDist * 12 / 30);
        if (delay > 2)
            delay = 2;

        target.delayedGraphics(1344,0, delay);
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getAsNpc().getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 6;
    }
}
