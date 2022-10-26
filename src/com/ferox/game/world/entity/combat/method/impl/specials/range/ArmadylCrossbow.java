package com.ferox.game.world.entity.combat.method.impl.specials.range;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatSpecial;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.masks.animations.Animation;
import com.ferox.game.world.entity.masks.animations.Priority;
import com.ferox.game.world.entity.mob.player.Player;

public class ArmadylCrossbow extends CommonCombatMethod {

    private static final Animation ANIMATION = new Animation(4230, Priority.HIGH);

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        final Player player = mob.getAsPlayer();

        player.animate(ANIMATION);

        //Fire projectile
        new Projectile(mob, target, 301, 50, 70, 44, 35, 0).sendProjectile();

        //Decrement ammo by 1
        CombatFactory.decrementAmmo(player);

        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED),2, CombatType.RANGED).checkAccuracy();
        hit.submit();
        CombatSpecial.drain(mob, CombatSpecial.ARMADYL_CROSSBOW.getDrainAmount());
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 6;
    }
}
