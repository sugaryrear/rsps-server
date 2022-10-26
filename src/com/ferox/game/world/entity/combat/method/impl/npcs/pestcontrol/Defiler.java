package com.ferox.game.world.entity.combat.method.impl.npcs.pestcontrol;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.util.chainedwork.Chain;

/**
 * @author Patrick van Elderen | May, 05, 2021, 13:39
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class Defiler extends CommonCombatMethod {

    private void melee(Npc npc, Mob target) {
        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
    }

    private void range(Npc npc, Mob target) {
        npc.animate(npc.attackAnimation());
        new Projectile(npc, target, 657, 50, 80, 50, 30, 0).sendProjectile();
        Chain.bound(null).name("DefilerRangeTask").runFn(2, () -> target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.RANGED), CombatType.RANGED).checkAccuracy().submit());
    }

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        Npc npc = (Npc) mob;
        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
            melee(npc, target);
        } else {
            range(npc, target);
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
