package com.ferox.game.world.entity.combat.method.impl.npcs.bosses;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;

public class ThermonuclearSmokeDevil extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        int tileDist = mob.tile().transform(1, 1).distance(target.tile());
        new Projectile(mob, target, 644, 20, 12 * tileDist, 30, 30, 0).sendProjectile();
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), 2, CombatType.RANGED).checkAccuracy().submit();
        target.graphic(643);
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
