package com.ferox.game.world.entity.combat.method.impl.npcs.misc;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.masks.graphics.Graphic;
import com.ferox.game.world.entity.mob.npc.Npc;

/**
 * @author Patrick van Elderen | Zerikoth
 * <p>
 * Revenants use all three forms of attack.
 * Their attacks have very high if not 100% accuracy, and will often deal high damage.
 * They will react to a player's overhead prayers and defensive bonuses.
 * By default, all revenants attack with Magic, but can quickly adapt based on the player's defensive bonuses and prayers.
 */
public class Revenant extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        Npc npc = (Npc) mob;

        if (npc.hp() < npc.maxHp() / 2 && World.getWorld().rollDie(5, 1)) {
            npc.graphic(1221);
            npc.heal(npc.maxHp() / 3);
        } else if (CombatFactory.canAttack(mob, CombatFactory.MELEE_COMBAT, target) && World.getWorld().random(2) == 1)
            meleeAttack(npc, target);
        else if (World.getWorld().rollDie(2, 1))
            rangedAttack(npc, target);
        else
            magicAttack(npc, target);
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 8;
    }

    private void meleeAttack(Npc npc, Mob target) {
        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
        npc.animate(npc.attackAnimation());
    }

    private void rangedAttack(Npc npc, Mob target) {
        var tileDist = npc.tile().transform(1, 1, 0).distance(target.tile());
        var delay = Math.max(1, (50 + (tileDist * 12)) / 30);

        npc.animate(npc.attackAnimation());
        new Projectile(npc, target, 206, 31,12 * tileDist, 30, 31, 0, 15, 10).sendProjectile();
        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
    }

    private void magicAttack(Npc npc, Mob target) {
        var tileDist = npc.tile().transform(1, 1, 0).distance(target.tile());
        var delay = Math.max(1, (50 + (tileDist * 12)) / 30);

        npc.animate(npc.attackAnimation());
        new Projectile(npc, target, 1415, 31, 12 * tileDist, 43, 31, 0, 15, 10).sendProjectile();

        int damage = CombatFactory.calcDamageFromType(npc, target, CombatType.MAGIC);
        target.hit(npc, damage, delay, CombatType.MAGIC).checkAccuracy().submit();
        target.delayedGraphics(damage > 0 ? new Graphic(1454, 124, 0) : new Graphic(85, 124, 0), delay);
    }
}
