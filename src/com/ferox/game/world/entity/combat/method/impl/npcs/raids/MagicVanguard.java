package com.ferox.game.world.entity.combat.method.impl.npcs.raids;


import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;

public class MagicVanguard extends CommonCombatMethod {
    @Override
    public void prepareAttack(Mob mob, Mob target) {
        magicAttack();
    }


    private void magicAttack() {
        //mob.forceChat("MAGIC ATTACK");
        var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
       // mob.graphic(194);
        new Projectile(mob, target, 1348, 20, 12 * tileDist, 35, 30, 0).sendProjectile();

        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();

        target.delayedGraphics(1345,0, delay);
        mob.animate(mob.attackAnimation());
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

