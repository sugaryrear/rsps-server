package com.ferox.game.world.entity.combat.method.impl.npcs.godwars.saradomin;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;

public class Growler extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        mob.animate(7037);
        mob.graphic(1182);
        new Projectile(mob, target, 1183, 25, 65, 0, 0, 0).sendProjectile();
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
