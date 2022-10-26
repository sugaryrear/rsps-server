package com.ferox.game.world.entity.combat.method.impl.npcs.bosses.cerberus;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.position.Tile;
import com.ferox.util.TickDelay;
import com.ferox.util.Utils;

/**
 * @author Patrick van Elderen | March, 10, 2021, 09:44
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class Cerberus extends CommonCombatMethod {

    private final TickDelay comboAttackCooldown = new TickDelay();
    private final TickDelay spreadLavaCooldown = new TickDelay();
    private boolean combatAttack = false;

    private void rangedAttack() {
        //mob.forceChat("RANGE");
        var tileDist = mob.tile().distance(target.tile());
        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
        new Projectile(mob, target, 1245, 25, 20 * tileDist, 65, 31, 0, 15, 220).sendProjectile();
        mob.animate(4492);
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), delay + 1, CombatType.RANGED).checkAccuracy().submit();
        target.delayedGraphics(1244, 100, delay + 1);
    }

    private void magicAttack() {
        //mob.forceChat("MAGIC");
        var tileDist = mob.tile().distance(target.tile());
        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
        new Projectile(mob, target, 1242, 25, 20 * tileDist, 65, 31, 0, 15, 220).sendProjectile();
        mob.animate(4492);

        int hit = CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC);

        target.hit(mob, hit, delay + 1, CombatType.RANGED).checkAccuracy().submit();

        if (hit > 0) {
            target.delayedGraphics(1243, 100, delay + 1);
        }
    }

    private void meleeAttack(boolean animate) {
        if (animate)
            mob.animate(mob.attackAnimation());
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 1, CombatType.MELEE).checkAccuracy().submit();
    }

    private void comboAttack() {
        mob.animate(4490); // triple attack
        combatAttack = true;
        //mob.forceChat("MAGIC COMBO");
        magicAttack();
        mob.runFn(2, () -> {
            //mob.forceChat("RANGED COMBO");
            if (mob == null || target.dead()) {
                return;
            }
            rangedAttack();
        }).then(2, () -> {
            combatAttack = true;
            if (!CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
                return;
            }
            //mob.forceChat("MELEE COMBO");
            meleeAttack(false);
        });
        comboAttackCooldown.delay(66); // ~40 seconds cooldown
    }

    private void spreadLava() {
        spreadLavaCooldown.delay(36);
        mob.animate(4493);
        mob.forceChat("Grrrrrrrrrr");
        Tile[] positions = {target.tile().copy(),
            new Tile(mob.getAbsX() + Utils.random(-4, mob.getSize() + 4), mob.getAbsY() + Utils.random(-4, mob.getSize() + 4), mob.getZ()),
            new Tile(mob.getAbsX() + Utils.random(-4, mob.getSize() + 4), mob.getAbsY() + Utils.random(-4, mob.getSize() + 4), mob.getZ())};
        for (Tile pos : positions) {
            new Projectile(mob.tile(), target.tile(), 1, 1247, 86, 25, 65, 0, 0, 15, 220).sendProjectile();
            mob.runFn(1, () -> {
                World.getWorld().tileGraphic(1246, new Tile(pos.getX(), pos.getY(), pos.getZ()), 0, 0);
            }).then(2, () -> {
                for (int i = 0; i < 6; i++) {
                    if (target == null)
                        return;
                    if (target.tile().equals(pos)) {
                        target.hit(mob,World.getWorld().random(10, 15));
                    } else if (Utils.getDistance(target.tile(), pos) == 1) {
                        target.hit(mob,7);
                    }
                }
            }).then(2, () -> {
                World.getWorld().tileGraphic(1247, new Tile(pos.getX(), pos.getY(), pos.getZ()), 0, 0);
            }).then(1, () -> {
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
    public void onDeath() {
        comboAttackCooldown.reset();
        spreadLavaCooldown.reset();
    }

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        if (!comboAttackCooldown.isDelayed()) {
            comboAttack();
        }
        if (mob.hp() <= 200 && !spreadLavaCooldown.isDelayed()) {
            spreadLava();
        }
        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target) && Utils.rollPercent(25)) {
            meleeAttack(true);
        } else if (Utils.rollPercent(50)) {
            magicAttack();
        } else {
            rangedAttack();
        }
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return combatAttack ? 12 : mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 10;
    }
}
