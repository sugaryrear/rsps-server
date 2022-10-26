package com.ferox.game.world.entity.combat.method.impl.specials.melee;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatSpecial;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.util.Utils;

public class BarrelchestAnchor extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        mob.animate(5870);
        mob.graphic(1027);
        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
        hit.submit();

        if (target.isPlayer()) {
            Player playerTarget = (Player) target;

            int roll = Utils.random(4);
            int skill;
            int deduceVal = (int) (hit.getDamage() * 0.10);

            if (roll == 1) {
                skill = Skills.ATTACK;
            } else if (roll == 2) {
                skill = Skills.DEFENCE;
            } else if (roll == 3) {
                skill = Skills.RANGED;
            } else {
                skill = Skills.MAGIC;
            }

            playerTarget.skills().alterSkill(skill, -deduceVal);
        }
        CombatSpecial.drain(mob, CombatSpecial.BARRELSCHEST_ANCHOR.getDrainAmount());
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
