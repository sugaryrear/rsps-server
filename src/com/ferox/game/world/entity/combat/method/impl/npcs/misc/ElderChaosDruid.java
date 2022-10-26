package com.ferox.game.world.entity.combat.method.impl.npcs.misc;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Utils;
import com.ferox.util.timers.TimerKey;

public class ElderChaosDruid extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        //Check to see if we're able to teleport the player beside us.
        if (Utils.rollDie(5, 1) && mob.tile().distance(target.tile()) > 3 && mob.tile().distance(target.tile()) < 6 &&
            !target.getTimers().has(TimerKey.ELDER_CHAOS_DRUID_TELEPORT)) {
            target.getTimers().addOrSet(TimerKey.ELDER_CHAOS_DRUID_TELEPORT, 5);
            target.teleport(new Tile(mob.tile().getX(), mob.tile().getY() - 1));
            target.graphic(409);
            mob.forceChat("You dare run from us!");
        } else {
            mob.animate(mob.attackAnimation());
            mob.graphic(158);
            new Projectile(mob, target, 159, 51, 60, 43, 31, 0).sendProjectile();
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 2, CombatType.MAGIC).checkAccuracy().submit();
        }
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 5;
    }
}
