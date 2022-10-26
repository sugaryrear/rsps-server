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
import com.ferox.game.world.entity.masks.graphics.Graphic;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.util.timers.TimerKey;

public class DragonThrownaxe extends CommonCombatMethod {

    private static final Animation ANIMATION = new Animation(7521, Priority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(1317, 120);

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        final Player player = mob.getAsPlayer();

        player.animate(ANIMATION);
        player.performGraphic(GRAPHIC);

        new Projectile(player, target, 1318, 45, 65, 40, 33, 0).sendProjectile();

        CombatFactory.decrementAmmo(player);

        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED),0, CombatType.RANGED).checkAccuracy();
        hit.submit();
        CombatSpecial.drain(mob, CombatSpecial.DRAGON_THROWNAXE.getDrainAmount());

        player.getTimers().register(TimerKey.THROWING_AXE_DELAY,1);
        player.getTimers().register(TimerKey.COMBAT_ATTACK,1); // 1 tick delay before another normal melee
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed() + 1;
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 4;
    }
}
