package com.ferox.game.world.entity.combat.method.impl.npcs.godwars.zaros;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.masks.graphics.Graphic;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;

import static com.ferox.util.NpcIdentifiers.*;
import static com.ferox.util.NpcIdentifiers.SPIRITUAL_MAGE_ZAROS;

public class FumusCombat extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        // Attack the player

        if (Utils.rollDie(5, 1)) { // 20% chance to do aoe
            doMagic(mob,target);
        } else {
            attackregular(mob,target);
        }
    }
private void attackregular (Mob mob, Mob target) {
    mob.animate(1978);
    new Projectile(mob, target, 384, 45, 65, 55, 35, 0).sendProjectile();
    target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), 2, CombatType.RANGED).checkAccuracy().submit();
}

    private void doMagic(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        Tile lightning_one = target.tile();
        Tile lightning_two = lightning_one.transform(1, 0);
        Tile lightning_three = lightning_one.transform(1, 1);
        int tileDist = mob.tile().distance(target.tile());
        int delay = Math.max(1, (30 + tileDist * 12) / 30);

        Chain.bound(null).runFn(2, () -> {
            new Projectile(new Tile(mob.tile().x + -1, mob.tile().y + 1), lightning_one, 0, 384, 10 * tileDist, delay, 70, 45, 0).sendProjectile();
            new Projectile(new Tile(mob.tile().x + -1, mob.tile().y + 1), lightning_two, 0, 384, 10 * tileDist, delay, 70, 45, 0).sendProjectile();
            new Projectile(new Tile(mob.tile().x + -1, mob.tile().y + 1), lightning_three, 0, 384, 10 * tileDist, delay, 70, 45, 0).sendProjectile();

            World.getWorld().tileGraphic(391, lightning_one,0, 10 * tileDist);
            World.getWorld().tileGraphic(391, lightning_two, 0, 10 * tileDist);
            World.getWorld().tileGraphic(391, lightning_three, 0, 10 * tileDist);
        }).then(3, () -> {
            if (target.tile() == (lightning_one) || target.tile() == (lightning_one.transform(1, 0)) || target.tile() == (lightning_one.transform(1, 1))) {
                target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), CombatType.MAGIC).checkAccuracy().submit();
            }
        });
        mob.getCombat().delayAttack(6);
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
