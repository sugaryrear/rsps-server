package com.ferox.game.world.entity.combat.method.impl.specials.melee;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatSpecial;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.formula.AccuracyFormula;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;

/**
 * The Saradomin sword has a special attack, Saradomin's Lightning, that deals 10% more melee damage and 1-16 extra Magic damage.
 * This special attack consumes 100% of the wielder's special attack energy. The special attack rolls against the opponent's Magic defence bonus using the player's
 * slash attack bonus, thus making this special attack extremely accurate on melee armour. If the melee attack misses, then the Magic attack will also fail. However,
 * if the melee hit is a successful hit but rolls a 0, the Magic damage will still be applied. Players receive 2 Magic experience for each point of damage caused by the
 * extra Magic damage. The Magic damage will always hit 0 on cyclopes in the Warriors' Guild, as with all non-melee damage there. It will also hit 0 on Callisto,
 * Venenatis and Vet'ion as they are immune to Magic damage.
 */
public class SaradominSword extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        mob.animate(1132);
        mob.graphic(1213, 100, 0);

        boolean accurate = AccuracyFormula.doesHit(mob, target, CombatType.MELEE);
        int meleeHit = CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE);
        int magicHit = CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC);
        if (accurate) {
            if (meleeHit > 0) {
                magicHit = World.getWorld().random(1, 16);
            }
        } else {
            meleeHit = 0;
            magicHit = 0;
        }

        Hit hit = target.hit(mob, meleeHit,1, CombatType.MELEE).checkAccuracy();
        hit.submit();
        Hit hit2 = target.hit(mob, magicHit,1, CombatType.MAGIC).checkAccuracy();
        hit2.submit();
        CombatSpecial.drain(mob, CombatSpecial.SARADOMIN_SWORD.getDrainAmount());
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
