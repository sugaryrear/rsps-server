package com.ferox.game.world.entity.combat.method.impl.specials.melee;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatSpecial;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;

public class AncientMace extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        mob.animate(6147);
        mob.graphic(1027);
        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
        hit.submit();

        if (target.dead()) {
            return;
        }

        //TODO in combat ignore prayer, mace ignores overheads
        if (target.isPlayer()) {
            Player t = (Player) target;
            Player p = (Player) mob;
            t.skills().alterSkill(Skills.PRAYER, -hit.getDamage());
            p.skills().alterSkill(Skills.PRAYER, hit.getDamage());
        }
        CombatSpecial.drain(mob, CombatSpecial.ANCIENT_MACE.getDrainAmount());
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
