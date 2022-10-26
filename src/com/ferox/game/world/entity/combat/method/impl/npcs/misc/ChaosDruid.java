package com.ferox.game.world.entity.combat.method.impl.npcs.misc;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.util.Utils;

public class ChaosDruid extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        if (Utils.rollDie(2, 1)) {
            int hit = CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC);
            target.hit(mob, hit, CombatType.MAGIC).checkAccuracy().submit();
            if (hit > 0) {
                //We succeed! Send the player a message
                target.message("You feel weakened.");

                //Remove the players attack level by 1
                target.skills().alterSkill(Skills.ATTACK, -1);
                target.graphic(181);
            }
        } else {
            //Else we attack the target with a melee attack, if we're in attacking distance.
            if (mob.tile().distance(target.tile()) <= 1)
                target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
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
