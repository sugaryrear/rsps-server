package com.ferox.game.world.entity.combat.method.impl.npcs.slayer;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.Utils;

/**
 * @author PVE
 * @Since augustus 06, 2020
 */
public class Basilisk extends CommonCombatMethod {

    private static final int[] DRAIN = { Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.RANGED, Skills.MAGIC};

    private void basicAttack(Mob mob, Mob target) {
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
        mob.animate(mob.attackAnimation());
    }

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        Player player = (Player) target;
        basicAttack(mob, target);
        if(!player.getEquipment().contains(ItemIdentifiers.MIRROR_SHIELD)) {
            player.hit(mob, Utils.random(2, 5), CombatType.MELEE).submit();
            for (int skill : DRAIN) {
                player.skills().alterSkill(skill, -4);
            }
            player.message("<col=ff0000>The basilisk's piercing gaze drains your stats!");
            player.message("<col=ff0000>A mirror shield can protect you from this attack.");
        }
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
