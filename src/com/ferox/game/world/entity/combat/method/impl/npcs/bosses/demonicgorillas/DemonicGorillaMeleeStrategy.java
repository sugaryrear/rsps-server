package com.ferox.game.world.entity.combat.method.impl.npcs.bosses.demonicgorillas;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;

/**
 * @author Patrick van Elderen | March, 13, 2021, 22:10
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class DemonicGorillaMeleeStrategy extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        //mob.forceChat("MELEE!");
        mob.animate(mob.attackAnimation());
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 1, CombatType.MELEE).checkAccuracy().postDamage(h -> ((DemonicGorilla)mob).getCombatAI().handleAfterHit(h)).submit();
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
