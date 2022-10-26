package com.ferox.game.world.entity.combat.method.impl.npcs.slayer;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.util.ItemIdentifiers;

/**
 * @author PVE
 * @Since augustus 06, 2020
 */
public class Cockatrice extends CommonCombatMethod {

    private static final int[] DRAIN = {Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.RANGED, Skills.MAGIC};

    private void basicAttack(Mob mob, Mob target) {
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
        mob.animate(mob.attackAnimation());
    }

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());

        Player player = (Player) target;
        basicAttack(mob, target);
        if (!player.getEquipment().contains(ItemIdentifiers.MIRROR_SHIELD)) {
            for (int skill : DRAIN) {
                player.skills().alterSkill(skill, -8);
            }
            player.message("<col=ff0000>The cockatrice's piercing gaze drains your stats!");
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
