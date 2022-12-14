package com.ferox.game.world.entity.combat.method.impl.npcs.raids;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;

public class VespulaGroundForm extends CommonCombatMethod {
    @Override
    public void prepareAttack(Mob mob, Mob target) {
        if(CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
            meleeAttack();//Melee attack ignores prayer
        }
    }

    private void meleeAttack() {
        mob.animate(mob.attackAnimation());
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
    }

    private void magicAttack() {
        //mob.forceChat("MAGIC ATTACK");
        var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
        mob.graphic(194);
        new Projectile(mob, target, 195, 20, 12 * tileDist, 35, 30, 0).sendProjectile();

        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();

        target.delayedGraphics(196,0, delay);
        mob.animate(mob.attackAnimation());
    }

    private void stealGoodMemories() {
        //     mob.forceChat("STEAL MEMORIES!");
        mob.animate(7422);

        var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
        new Projectile(mob, target, 1382, 20, 12 * tileDist, 35, 30, 0).sendProjectile();

        final Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC);
        hit.checkAccuracy().submit();

        mob.heal(hit.getDamage());
        mob.graphic(1423);

        target.animate(2046);
        target.graphic(1433);
        target.message("The muttadile heals!");
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 1;
    }
}
