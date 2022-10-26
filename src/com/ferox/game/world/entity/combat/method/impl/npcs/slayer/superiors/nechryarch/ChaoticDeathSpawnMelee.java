package com.ferox.game.world.entity.combat.method.impl.npcs.slayer.superiors.nechryarch;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 02, 2020
 */
public class ChaoticDeathSpawnMelee extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target,CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 1;
    }
    @Override
    public boolean canMultiAttackInSingleZones() {
        return true;
    }
}
