package com.ferox.game.world.entity.combat.method.impl.specials.melee;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatSpecial;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;

public class AbyssalTentacle extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        mob.animate(1658);
        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
        hit.submit();

        if (target.dead()) {
            return;
        }

        target.graphic(341, 100, 0);
        target.freeze(8, mob);// 5 second freeze timer
        if (World.getWorld().rollDie(100, 25)) {
            target.poison(4);
        }
        CombatSpecial.drain(mob, CombatSpecial.ABYSSAL_TENTACLE.getDrainAmount());
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
