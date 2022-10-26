package com.ferox.game.world.entity.combat.method.impl.npcs.slayer.superiors;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;
import com.ferox.util.timers.TimerKey;

/**
 * In combat, the marble gargoyle will occasionally launch a grey ball towards the player.
 * If hit by the projectile, it will inflict up to 38 damage and immobilise the player for a few seconds. The message box states "You have been trapped in stone!" when this occurs.
 * Players are able to avoid this attack by moving at least one tile away from their original position once the projectile is launched.
 *
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * maart 31, 2020
 */
public class MarbleGargoyle extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        if (Utils.random(3) == 0) {
            mob.animate(mob.attackAnimation());
            stoneAttack(mob, target);
            mob.getTimers().register(TimerKey.COMBAT_ATTACK, 6);
        }else {
            mob.animate(mob.attackAnimation());
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
        }
    }

    private void stoneAttack(Mob mob, Mob target) {
        Tile tile = target.tile();
        new Projectile(mob, target,1453, 30, 75, 50, 30, 0, 10,5).sendProjectile();
        Chain.bound(null).runFn(5, () -> {
            if (target.tile().equals(tile)) {
                target.hit(mob, Utils.random(38));
                target.stun(3);
                if (target instanceof Player) {
                    target.message("You have been trapped in stoe!");
                }
            }
        });
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 7;
    }
}
