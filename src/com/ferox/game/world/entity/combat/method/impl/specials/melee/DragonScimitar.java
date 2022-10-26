package com.ferox.game.world.entity.combat.method.impl.specials.melee;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatSpecial;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.mob.player.Player;

public class DragonScimitar extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        mob.animate(1872);
        mob.graphic(347, 100, 0);
        //TODO it.sound(2540)
        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
        hit.submit();

        if(target.isPlayer()) {
            if (hit.getDamage() > 0) {
                Player player = (Player) mob;
                Player playerAttacker = (Player) target;
                CombatFactory.disableProtectionPrayers(playerAttacker);
                player.message("Your target can no longer use protection prayers.");
            }
        }
        CombatSpecial.drain(mob, CombatSpecial.DRAGON_SCIMITAR.getDrainAmount());
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
