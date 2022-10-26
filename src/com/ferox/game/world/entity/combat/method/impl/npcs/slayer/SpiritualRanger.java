package com.ferox.game.world.entity.combat.method.impl.npcs.slayer;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.util.chainedwork.Chain;

public class SpiritualRanger extends CommonCombatMethod {

    private int getProjectileHeight(int npc) {
        return npc == 2211 ? 45 : 30;
    }

    private int getDelay(int npc) {
        return npc == 2242 ? 20 : 40;
    }

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        if (mob.isNpc()) {
            mob.animate(mob.attackAnimation());
            Npc npc = (Npc) mob;
            var tileDist = mob.tile().transform(1, 1, 0).distance(target.tile());
            var delay = Math.max(1, (50 + (tileDist * 12)) / 30);
            Chain.bound(null).runFn(2, () -> {
                new Projectile(mob, target, 1995, 15, 12 * tileDist, 25, 31, 0).sendProjectile();

            });


            int hit = CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED);
            target.hit(mob, hit,1, CombatType.RANGED).checkAccuracy().submit();
        }
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }
    @Override
    public int getAttackDistance(Mob mob) {
        return 7;
    }
}
