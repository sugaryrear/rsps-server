package com.ferox.game.world.entity.combat.method.impl.npcs.pestcontrol;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.util.chainedwork.Chain;

/**
 * @author Patrick van Elderen | May, 05, 2021, 13:40
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class Torcher extends CommonCombatMethod {

    private void magic(Npc npc, Mob target) {
        npc.animate(npc.attackAnimation());
        new Projectile(npc, target, 647, 50, 80, 50, 30, 0).sendProjectile();
        Chain.bound(target).name("TorcherMagicTask").runFn(2, () -> target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.MAGIC), CombatType.MAGIC).checkAccuracy().submit());
    }

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        Npc npc = (Npc) mob;
        magic(npc, target);
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
