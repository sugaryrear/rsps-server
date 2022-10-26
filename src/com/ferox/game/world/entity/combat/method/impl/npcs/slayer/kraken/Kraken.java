package com.ferox.game.world.entity.combat.method.impl.npcs.slayer.kraken;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;

public class Kraken extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        new Projectile(mob, target, 156, 32, 65, 30, 30, 0).sendProjectile();
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 1, CombatType.MAGIC).checkAccuracy().postDamage(this::handleAfterHit).submit();
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getAsNpc().getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 8;
    }

    public void handleAfterHit(Hit hit) {
        //End gfx when target was hit or splash
        hit.getTarget().graphic(hit.getDamage() > 0 ? 157 : 85);
    }
}
