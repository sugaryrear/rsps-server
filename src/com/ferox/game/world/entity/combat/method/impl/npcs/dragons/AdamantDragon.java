package com.ferox.game.world.entity.combat.method.impl.npcs.dragons;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.formula.CombatFormula;
import com.ferox.game.world.entity.combat.hit.SplatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.combat.prayer.default_prayer.DefaultPrayers;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Area;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 28, 2020
 */
public class AdamantDragon extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob dragon, Mob target) {
        var tileDist = dragon.tile().distance(target.tile());
        var delay = Math.max(1, (50 + (tileDist) * 12) / 30);
        var rand = Utils.random(4);
        Npc npc = (Npc) dragon;

        if (rand == 1) {
            doDragonBreath(npc, target, tileDist, delay);
        } else if (rand == 2) {
            doRangedAttack(npc, target, tileDist, delay);
        } else if (rand == 3) {
            doMagicBlast(npc, target, tileDist, delay);
        } else {
            if (CombatFactory.canReach(dragon, CombatFactory.MELEE_COMBAT, target)) {
                doMelee(npc, target);
            } else {
                int roll = Utils.random(3);
                if (roll == 1) {
                    doDragonBreath(npc, target, tileDist, delay);
                } else if (roll == 2) {
                    doRangedAttack(npc, target, tileDist, delay);
                } else if (roll == 3) {
                    doMagicBlast(npc, target, tileDist, delay);
                }
            }
        }
    }

    private void doMelee(Npc npc, Mob target) {
        npc.animate(npc.attackAnimation());
        target.hit(npc, Utils.random(npc.combatInfo().maxhit), 1, CombatType.MELEE).checkAccuracy().submit();
    }

    private void doDragonBreath(Npc npc, Mob target, int tileDist, int delay) {
        npc.animate(81);
        new Projectile(npc, target, 54, 25, 20 * tileDist, 22, 32, 0, 5, 24).sendProjectile();

        //new Projectile(npc, target, 54, 50, 10 * tileDist, 40, 36, 0).sendProjectile();
        if(target instanceof Player) {
            Player player = (Player) target;
            double max = 50.0;
            int antifire_charges = player.getAttribOr(AttributeKey.ANTIFIRE_POTION, 0);
            boolean hasShield = CombatFormula.hasAntiFireShield(player);
            boolean hasPotion = antifire_charges > 0;
            boolean vorkiPetout = player.hasPetOut("Vorki");
            boolean petTamerI = player.<Boolean>getAttribOr(AttributeKey.ANTI_FIRE_RESISTANT,false);

            //System.out.println(vorkiPetout);
            //System.out.println(petTamerI);
            if(vorkiPetout && petTamerI) {
                //player.message("Your Vorki pet protects you completely from the heat of the dragon's breath!");
                max = 0.0;
            }

            boolean memberEffect = player.getMemberRights().isExtremeMemberOrGreater(player) && !WildernessArea.inWilderness(player.tile());
            if (max > 0 && player.<Boolean>getAttribOr(AttributeKey.SUPER_ANTIFIRE_POTION, false) || memberEffect) {
             //   player.message("Your super antifire potion protects you completely from the heat of the dragon's breath!");
                max = 0.0;
            }

            //Does our player have an anti-dragon shield?
            if (max > 0 && hasShield) {
                player.message("Your shield absorbs most of the dragon fire!");
                max *= 0.3;
            }

            //Has our player recently consumed an antifire potion?
            if (max > 0 && hasPotion) {
                player.message("Your potion protects you from the heat of the dragon's breath!");
                max *= 0.3;
            }

            //Is our player using protect from magic?
            if (max > 0 && DefaultPrayers.usingPrayer(player, DefaultPrayers.PROTECT_FROM_MAGIC)) {
                player.message("Your prayer absorbs most of the dragon's breath!");
                max *= 0.6;
            }

            if (hasShield && hasPotion) {
                max = 0.0;
            }

            int hit = World.getWorld().random((int) max);
            player.hit(npc, hit, delay, CombatType.MAGIC).submit();
            if (max == 65 && hit > 0) {
                player.message("You are badly burned by the dragon fire!");
            }
        }
    }

    private void doRangedAttack(Npc npc, Mob target, int tileDist, int delay) {
        Tile targetTile = target.tile().copy();

        npc.animate(81);

        Chain.bound(null).runFn(1, () -> {
            //Shoots poison projectile to the target.
            new Projectile(npc.tile().transform(1, 1), targetTile, 0,1486, 20 * tileDist, 25, 40, 36, 0).sendProjectile();
        }).then(delay + 1, () -> {
            if (target.tile() == targetTile) {
                target.hit(npc, 8, SplatType.POISON_HITSPLAT);
            }

            World.getWorld().tileGraphic(1487, targetTile, 0, 0);
            new Projectile(targetTile, targetTile.transform(1, 0), 0,1486, 20 * tileDist, 25, 40, 36, 0).sendProjectile();
        }).then(1, () -> {
            if (target.tile() == targetTile) {
                target.hit(npc, 8, CombatType.RANGED, SplatType.POISON_HITSPLAT);
            }
        }).then(1, () -> {
            if (inBlastTile(target, targetTile.area(1))) {
                target.hit(npc, 4, CombatType.RANGED, SplatType.POISON_HITSPLAT);
            }
        }).then(1, () -> {
            World.getWorld().tileGraphic(1487, targetTile.transform(1, 0), 0, 0);
        }).then(1, () -> {
            if (inBlastTile(target, targetTile.area(1))) {
                target.hit(npc, 4, CombatType.RANGED, SplatType.POISON_HITSPLAT);
            }
        });
    }

    private void doMagicBlast(Npc npc, Mob target, int tileDist, int delay) {
        npc.animate(81);
        Chain.bound(null).runFn(1, () -> {
            new Projectile(npc, target, 165, 25, 20 * tileDist, 40, 36, 0, 16, 127).sendProjectile();
            target.hit(npc, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay + 1, CombatType.MAGIC).checkAccuracy().submit();
        });
    }

    private boolean inBlastTile(Mob target, Area area) {
        return (target.tile().inArea(area));
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 8;
    }
}
