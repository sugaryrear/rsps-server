package com.ferox.game.world.entity.combat.method.impl.npcs.slayer;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;

/**
 * @author PVE
 * @Since augustus 07, 2020
 */
public class InfernalMage extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        mob.graphic(129, 92, 0);
        new Projectile(mob, target, 130, 51, mob.projectileSpeed(target), 43, 31, 0,16, 64).sendProjectile();
        int delay = mob.getProjectileHitDelay(target);
        int hit = CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC);
        target.hit(mob, hit, delay, CombatType.MAGIC).checkAccuracy().submit();

        if(hit > 0) {
            target.graphic(131, 124, delay);
        } else {
            target.graphic(85, 124, delay);
        }
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
