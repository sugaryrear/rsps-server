package com.ferox.game.world.entity.combat.method.impl.npcs.fightcaves;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.player.Player;

/**
 * Handles Jad's combat.
 *
 * @author Professor Oak
 */
public class TztokJadCombatScript extends CommonCombatMethod {

    private static final int MAX_DISTANCE = 10;

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        Player player = target.getAsPlayer();

        // Select attack type..

        //Disable healers until further notice
        TzTokJad jad = (TzTokJad) mob.getAsNpc();

        if (jad.hp() <= jad.maxHp() / 2) {
            jad.spawnHealers(player);
        }

        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
            if(World.getWorld().rollDie(2,1)) {
                target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
                mob.animate(mob.attackAnimation());
            } else {
                if (World.getWorld().rollDie(2, 1)) {
                    /*
                     * Magic attack
                     */
                    mob.animate(2656);
                    mob.graphic(447, 500, 0);
                    new Projectile(mob, target, 448, 50, 120, 128, 31, 0).sendProjectile();
                    target.delayedGraphics(157, 0, 5);
                    target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 5, CombatType.MAGIC).checkAccuracy().submit();
                } else {
                    /*
                     * Ranged attack
                     */
                    mob.animate(2652);
                    target.graphic(451);
                    target.delayedGraphics(157, 0, 2);
                    target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), 2, CombatType.RANGED).checkAccuracy().submit();
                }
            }
        } else {
            if (World.getWorld().rollDie(2, 1)) {
                /*
                 * Magic attack
                 */
                mob.animate(2656);
                mob.graphic(447, 500, 0);
                new Projectile(mob, target, 448, 50, 120, 128, 31, 0).sendProjectile();
                target.delayedGraphics(157, 0, 5);
                target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 5, CombatType.MAGIC).checkAccuracy().submit();
            } else {
                /*
                 * Ranged attack
                 */
                mob.animate(2652);
                target.graphic(451);
                target.delayedGraphics(157, 0, 2);
                target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), 2, CombatType.RANGED).checkAccuracy().submit();
            }
        }
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return MAX_DISTANCE;
    }
}
