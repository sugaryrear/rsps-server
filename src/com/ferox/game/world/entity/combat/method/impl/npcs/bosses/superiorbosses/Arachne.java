package com.ferox.game.world.entity.combat.method.impl.npcs.bosses.superiorbosses;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Utils;

public class Arachne extends CommonCombatMethod {

    private void rangedAttack() {
        mob.animate(5322);
        // Throw a ranged projectile
        var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
        Projectile projectile = new Projectile(mob, target, 1379, 20,12 * tileDist, 10, 10, 0);
        projectile.sendProjectile();
        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
    }

    private void magicAttack() {
        mob.animate(5322);
        // Throw a magic projectile
        var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
        Projectile projectile = new Projectile(mob, target, 1380, 20,12 * tileDist, 10, 10, 0);
        projectile.sendProjectile();
        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
    }

    private void meleeAttack() {
        mob.animate(mob.attackAnimation());
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
    }

    private void webAttack() {
        Tile[] positions = {target.tile().copy(),
            new Tile(mob.getAbsX() + Utils.random(-4, mob.getSize() + 4), mob.getAbsY() + Utils.random(-4, mob.getSize() + 4), mob.getZ()),
            new Tile(mob.getAbsX() + Utils.random(-4, mob.getSize() + 4), mob.getAbsY() + Utils.random(-4, mob.getSize() + 4), mob.getZ())};
        for (Tile pos : positions) {
            mob.runFn(1, () -> World.getWorld().tileGraphic(1601, new Tile(pos.getX(), pos.getY(), pos.getZ()), 0, 0)).then(2, () -> {
                if (target == null)
                    return;
                if (target.tile().equals(pos)) {
                    target.hit(mob, World.getWorld().random(10, 15));
                } else if (Utils.getDistance(target.tile(), pos) == 1) {
                    target.hit(mob, 7);
                }
            }).then(2, () -> World.getWorld().tileGraphic(1601, new Tile(pos.getX(), pos.getY(), pos.getZ()), 0, 0)).then(1, () -> {
                if (target == null)
                    return;
                if (target.tile().equals(pos)) {
                    target.hit(mob,World.getWorld().random(10, 18));
                } else if (Utils.getDistance(target.tile(), pos) == 1) {
                    target.hit(mob,10);
                }
            });
        }
    }

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target) && Utils.rollPercent(25)) {
            meleeAttack();
        } else if (Utils.rollPercent(50)) {
            rangedAttack();
        } else if (Utils.rollPercent(10)) {
            webAttack();
        } else {
            magicAttack();
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
