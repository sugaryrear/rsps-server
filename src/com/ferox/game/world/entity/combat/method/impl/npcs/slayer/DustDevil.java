package com.ferox.game.world.entity.combat.method.impl.npcs.slayer;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.Utils;

/**
 * @author PVE
 * @Since augustus 06, 2020
 */
public class DustDevil extends CommonCombatMethod {

    private static final int[] DRAIN = { Skills.ATTACK, Skills.STRENGTH, Skills.RANGED, Skills.MAGIC};

    private void basicAttack(Mob mob, Mob target) {
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
        mob.animate(mob.attackAnimation());
    }

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        Player player = (Player) target;
        if ((player.getEquipment().getId(EquipSlot.HEAD) != ItemIdentifiers.FACEMASK && !player.getEquipment().wearingSlayerHelm())) {
            player.hit(mob, 16, CombatType.MELEE).submit();
            player.message("<col=ff0000>The devil's dust blinds and damages you!");
            player.message("<col=ff0000>A facemask can protect you from this attack.");
        }
        basicAttack(mob, target);
        if(Utils.rollDie(5, 1)) {
            for (int skill : DRAIN) {
                player.skills().alterSkill(skill, -5);
            }
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
