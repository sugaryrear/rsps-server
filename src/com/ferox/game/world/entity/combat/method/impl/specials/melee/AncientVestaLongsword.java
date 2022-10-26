package com.ferox.game.world.entity.combat.method.impl.specials.melee;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatSpecial;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;

/**
 * @author Patrick van Elderen | July, 12, 2021, 15:01
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class AncientVestaLongsword extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        mob.animate(8145);
        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
        hit.submit();
        CombatSpecial.drain(mob, CombatSpecial.ANCIENT_VESTA_LONGSWORD.getDrainAmount());
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
