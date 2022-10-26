package com.ferox.game.world.entity.combat.method.impl.npcs.bosses.wilderness;

import com.ferox.game.content.mechanics.MultiwayCombat;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Skills;

/**
 * Handles Venenatis' combat.
 *
 * @author Professor Oak
 */
public class Venenatis extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        // Determine if we do a special hit, or a regular hit.
        if (World.getWorld().rollDie(14, 1)) {
            hurlWeb((Npc) mob, target);
        }

        // Determine if we're going to melee or mage
        if(CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
            mob.animate(mob.attackAnimation());
            //mob.forceChat("MELEE");
        } else {
            // Grab all players in a radius and do our magic projectile on them.
            World.getWorld().getPlayers().forEachInArea(mob.bounds(6), enemy -> {
                //If the target is currently in multi we..
                if (MultiwayCombat.includes(enemy.tile())) {
                    magicAttack(mob, enemy);
                    //mob.forceChat("MULTI PLAYER");
                } else if (enemy == target) {
                    //mob.forceChat("SINGLE PLAYER");
                    magicAttack(mob, enemy);
                }
            });
            // Do an animation..
            mob.animate(5322);
        }

        if (World.getWorld().rollDie(20, 1)) {
            drainPrayer(mob, target);
        }
    }

    private void magicAttack(Mob npc, Mob target) {
        // Throw a magic projectile
        var tileDist = npc.tile().transform(3, 3, 0).distance(target.tile());
        new Projectile(mob, target, 165, 20, 12 * tileDist, 30, 30, 0, 14, 5).sendProjectile();
        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
    }

    private void hurlWeb(Npc npc, Mob target) {
        //npc.forceChat("WEB");
        var tileDist = npc.tile().transform(3, 3, 0).distance(target.tile());
        new Projectile(mob, target, 1254, 20, 12 * tileDist, 20, 5,0, 15, 10).sendProjectile();
        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
        target.message("Venenatis hurls her web at you, sticking you to the ground.");
        target.stun(6, false,true,true);
        target.hit(npc, npc.combatInfo().maxhit, delay);// Cannot protect from this.
    }

    private void drainPrayer(Mob npc, Mob target) {
        //npc.forceChat("PRAYER");
        if (target.isPlayer()) {
            var tileDist = npc.tile().transform(3, 3, 0).distance(target.tile());
            new Projectile(mob, target, 171, 30, 12 * tileDist, 25, 25,0, 10, 10).sendProjectile();

            var player = target.getAsPlayer();
            var curpray = player.skills().level(Skills.PRAYER);
            var add = curpray / 5 + 1;
            var drain = 10 + add; // base 10 drain + 20% of current prayer + 1. Example 50 prayer becomes 30. Best tactic to keep prayer low.
            player.skills().alterSkill(Skills.PRAYER, (drain > curpray) ? -curpray : -drain);

            if (curpray > 0) {
                target.message("Your prayer was drained!");
            }
        }
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 10;
    }
}
