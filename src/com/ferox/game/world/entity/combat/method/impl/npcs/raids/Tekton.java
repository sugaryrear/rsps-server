package com.ferox.game.world.entity.combat.method.impl.npcs.raids;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.combat.skull.SkullType;
import com.ferox.game.world.entity.combat.skull.Skulling;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;
import com.ferox.util.timers.TimerKey;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 11, 2020
 */
public class Tekton extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        if (Utils.random(3) == 0) {
            mob.animate(7494);
            stoneAttack(mob, target);
            mob.getTimers().register(TimerKey.COMBAT_ATTACK, 6);
        } else {
            mob.animate(7494);
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
                    target.message("You have been rooted to the ground!");
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
        return 1;
    }
}
