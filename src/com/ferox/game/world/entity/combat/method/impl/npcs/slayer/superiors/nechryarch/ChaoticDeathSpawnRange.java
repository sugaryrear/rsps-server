package com.ferox.game.world.entity.combat.method.impl.npcs.slayer.superiors.nechryarch;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 02, 2020
 */
public class ChaoticDeathSpawnRange extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        new Projectile(mob, target, 393, 40, 55, 31, 43, 0).sendProjectile();
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target,CombatType.RANGED), 2, CombatType.RANGED).checkAccuracy().submit();
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 7;
    }

    @Override
    public boolean canMultiAttackInSingleZones() {
        return true;
    }
}
