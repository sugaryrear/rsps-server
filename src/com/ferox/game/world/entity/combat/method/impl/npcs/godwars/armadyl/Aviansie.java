package com.ferox.game.world.entity.combat.method.impl.npcs.godwars.armadyl;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.npc.Npc;

public class Aviansie extends CommonCombatMethod {

    private int get_animation(int npc) {
        return npc == 3168 ? 6975 : 6970;
    }

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        if (mob.isNpc()) {
            Npc npc = (Npc) mob;
            mob.animate(get_animation(npc.id()));
            new Projectile(mob, target, projectile(npc.id()), 29, 65, 95, 33, 0).sendProjectile();
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), 2, CombatType.RANGED).checkAccuracy().submit();
        }
    }

    private int projectile(int npc) {
        return switch (npc) {
            case 3170, 3171, 3172, 3173, 3175, 3178, 3182, 3181, 3179, 3180 -> 1192;
            default -> 1193;
        };
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
