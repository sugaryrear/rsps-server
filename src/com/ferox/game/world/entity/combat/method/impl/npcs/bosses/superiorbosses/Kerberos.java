package com.ferox.game.world.entity.combat.method.impl.npcs.bosses.superiorbosses;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;

public class Kerberos extends CommonCombatMethod {

    private void rangedAttack() {
        new Projectile(mob, target, 1381, 25, 106, 125, 31, 0, 15, 220).sendProjectile();
        mob.animate(4492);
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), 4, CombatType.RANGED).checkAccuracy().submit();
        target.delayedGraphics(1715, 100, 4);
    }

    private void magicAttack() {
        new Projectile(mob, target, 1382, 25, 106, 125, 31, 0, 15, 220).sendProjectile();
        mob.animate(4492);
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 4, CombatType.RANGED).checkAccuracy().submit();
        target.delayedGraphics(1710, 100, 4);
    }

    private void meleeAttack() {
        mob.animate(mob.attackAnimation());
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
    }

    private void doubleAttack() {
        mob.forceChat("RAWRRRRRRRRRRRRRR");
        Chain.bound(null).runFn(1, this::rangedAttack).then(3, this::magicAttack);
    }

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target) && Utils.rollPercent(25)) {
            meleeAttack();
        } else if (Utils.rollPercent(50)) {
            rangedAttack();
        } else if (Utils.rollPercent(10)) {
            doubleAttack();
        } else {
            magicAttack();
        }
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
