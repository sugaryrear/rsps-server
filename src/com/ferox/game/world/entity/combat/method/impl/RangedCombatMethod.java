package com.ferox.game.world.entity.combat.method.impl;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.ranged.*;
import com.ferox.game.world.entity.combat.ranged.RangedData.RangedWeapon;
import com.ferox.game.world.entity.combat.weapon.WeaponType;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.masks.graphics.Graphic;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.container.equipment.Equipment;
import com.ferox.util.chainedwork.Chain;

import static com.ferox.game.world.entity.combat.weapon.WeaponType.BOW;
import static com.ferox.game.world.entity.combat.weapon.WeaponType.THROWN;
import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;

/**
 * Represents the combat method for ranged attacks.
 *
 * @author Professor Oak
 */
public class RangedCombatMethod extends CommonCombatMethod {

    private int ballistaProjectileFor(int ammo) {
        return switch (ammo) {
            case BRONZE_JAVELIN -> 200;
            case IRON_JAVELIN -> 201;
            case STEEL_JAVELIN -> 202;
            case MITHRIL_JAVELIN -> 203;
            case ADAMANT_JAVELIN -> 204;
            case RUNE_JAVELIN -> 205;
            case DRAGON_JAVELIN -> 1301;
            case AMETHYST_JAVELIN -> 1386;
            default -> 1301;
        };
    }

    @Override
    public void prepareAttack(Mob attacker, Mob target) {

        //TODO sound here
        attacker.animate(attacker.attackAnimation());

        if (attacker.isNpc()) {
            new Projectile(attacker, target, attacker.getAsNpc().combatInfo().projectile, 41, 60, 40, 36, 15).sendProjectile();
            return;
        }

        if (attacker.isPlayer()) {
            Player player = attacker.getAsPlayer();

            //Decrement ammo
            CombatFactory.decrementAmmo(player);

            WeaponType weaponType = player.getCombat().getWeaponInterface();
            var weaponId = player.getEquipment().getId(EquipSlot.WEAPON);
            var ammoId = player.getEquipment().getId(EquipSlot.AMMO);
            var crystalBow = (weaponId >= 4212 && weaponId <= 4223);
            var crawsBow = weaponId == CRAWS_BOW || weaponId == CRAWS_BOW_C || weaponId == BEGINNER_CRAWS_BOW || weaponId == HWEEN_CRAWS_BOW;
            var bowOfFaerdhinen = weaponId == BOW_OF_FAERDHINEN || (weaponId >= BOW_OF_FAERDHINEN_1 && weaponId <= BOW_OF_FAERDHINEN_7);
            var blowpipe = weaponId == 12926 || weaponId == 12924;
            var chins = weaponType == WeaponType.CHINCHOMPA;
            var ballista = weaponId == 19478 || weaponId == 19481;

            var baseDelay = chins ? 20 : ballista ? 30 : 41;
            var startHeight = 40;
            var endHeight = 36;
            var curve = 15;
            var graphic = -1; // 228

            if (crystalBow) {
                attacker.graphic(250, 96, 0);
                graphic = 249;
            }

            if(bowOfFaerdhinen) {
                attacker.graphic(1923, 96, 0);
                graphic = 1922;
            }

            if (crawsBow) {
                graphic = 1574;
            }

            if (chins) {
                graphic = weaponId == 10033 ? 908 : weaponId == 10034 ? 909 : 1272;
            }

            // Bows need special love.. projectile and graphic :D
            if (weaponType == BOW && !crystalBow && !crawsBow && !bowOfFaerdhinen) {
                var db = ArrowDrawBack.find(ammoId);

                // Find the gfx and do it : -)
                if (db != null) {
                    if (Equipment.darkbow(weaponId)) {
                        var db2 = DblArrowDrawBack.find(ammoId);
                        if (db2 != null) {
                            player.graphic(db2.gfx, 96, 0);
                            graphic = db.projectile;
                        }
                    } else {
                        player.graphic(db.gfx, 96, 0);
                        graphic = db.projectile;
                    }
                }
            }

            // Knives are not your de-facto stuff either
            if (weaponType == THROWN) {
                if(weaponId == TOXIC_BLOWPIPE || weaponId == MAGMA_BLOWPIPE || weaponId == HWEEN_BLOWPIPE) {
                    graphic = 1122;
                }
                // Is this a knife? There are more than only knives that people throw.. think.. asparagus. uwat? darts, thrownaxes, javelins
                var drawback = KnifeDrawback.find(weaponId);
                if (drawback != null) {
                    player.graphic(drawback.gfx, 96, 0);
                    graphic = drawback.projectile;
                } else {
                    var db2 = DartDrawback.find(weaponId);
                    if (db2 != null) {
                        player.graphic(db2.gfx, 96, 0);
                        graphic = db2.projectile;
                    } else {
                        var db3 = ThrownaxeDrawback.find(weaponId);
                        if (db3 != null) {
                            player.graphic(db3.gfx, 96, 0);
                            graphic = db3.projectile;
                        }
                    }
                }
            }

            if(ballista) {
                baseDelay = 31;
                startHeight = 40;
                endHeight = 30;
                curve = 5;
                graphic = ballistaProjectileFor(ammoId);
            }

            // Crossbows are the other type of special needs
            if (weaponType == WeaponType.CROSSBOW) {
                baseDelay = 41;
                startHeight = 40;
                endHeight = 36;
                curve = 5;
                graphic = 27;
            }
            // Darkbow is double hits.
          //  System.out.println(target.dead()+"");

            if (attacker.getCombat().getRangedWeapon() != null) {
                weaponId = -1;
               weaponType = WeaponType.UNARMED;
                if(attacker.isPlayer()) {
                    weaponId = attacker.getAsPlayer().getEquipment().getId(EquipSlot.WEAPON);
                    weaponType = attacker.getAsPlayer().getCombat().getWeaponInterface();
                }

                var tileDist = attacker.tile().distance(target.tile());
                var delay = calcHitDelay(weaponId, weaponType, tileDist) + 1;

                //System.out.println("delay: "+delay);

                // At range 3 the hits hit the same time. may indicate mathematical rounding in calc.
                var secondArrowDelay = delay + (tileDist == 3 ? 0 : tileDist == 7 || tileDist == 8 || tileDist == 10 ? 2 : 1);
                //System.out.println("secondArrowDelay: "+secondArrowDelay);


                target.hit(attacker, CombatFactory.calcDamageFromType(attacker, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().postDamage(this::handleAfterHit).submit();




                // secondary hits
                if (attacker.getCombat().getRangedWeapon() == RangedWeapon.DARK_BOW) {
                    target.hit(attacker, CombatFactory.calcDamageFromType(attacker, target, CombatType.RANGED), secondArrowDelay, CombatType.RANGED).checkAccuracy().submit();
                } else if (attacker.getCombat().getRangedWeapon() == RangedWeapon.SANGUINE_TWISTED_BOW) {
                    target.hit(attacker, CombatFactory.calcDamageFromType(attacker, target, CombatType.RANGED) / 2, secondArrowDelay, CombatType.RANGED).checkAccuracy().submit();
                }
            }
            int finalGraphic = graphic;
            int finalBaseDelay = baseDelay;
            int finalStartHeight = startHeight;
            int finalEndHeight = endHeight;
            int finalCurve = curve;
            Chain.bound(null).runFn(baseDelay, () -> {
                System.out.println(target.dead()+" "+target.hp());
                new Projectile(attacker, target, !target.dead() || target.hp() > 0 ? finalGraphic : -1, finalBaseDelay, attacker.projectileSpeed(target), finalStartHeight, finalEndHeight, 0, finalCurve, 11).sendProjectile();

            });



            if (Equipment.darkbow(weaponId) || attacker.getCombat().getRangedWeapon() == RangedWeapon.SANGUINE_TWISTED_BOW) {
                // dark bow 2nd arrow
                new Projectile(attacker, target, graphic, 10 + baseDelay, 10 + attacker.projectileSpeed(target), startHeight, endHeight, 0, curve, 105).sendProjectile();
            }
        }

        // Darkbow is double hits.
//        if (attacker.getCombat().getRangedWeapon() != null) {
//            var weaponId = -1;
//            WeaponType weaponType = WeaponType.UNARMED;
//            if(attacker.isPlayer()) {
//                weaponId = attacker.getAsPlayer().getEquipment().getId(EquipSlot.WEAPON);
//                weaponType = attacker.getAsPlayer().getCombat().getWeaponInterface();
//            }
//
//            var tileDist = attacker.tile().distance(target.tile());
//            var delay = calcHitDelay(weaponId, weaponType, tileDist) + 1;
//
//            //System.out.println("delay: "+delay);
//
//            // At range 3 the hits hit the same time. may indicate mathematical rounding in calc.
//            var secondArrowDelay = delay + (tileDist == 3 ? 0 : tileDist == 7 || tileDist == 8 || tileDist == 10 ? 2 : 1);
//            //System.out.println("secondArrowDelay: "+secondArrowDelay);
//
//            // primary range hit
//            target.hit(attacker, CombatFactory.calcDamageFromType(attacker, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().postDamage(this::handleAfterHit).submit();
//
//            // secondary hits
//            if (attacker.getCombat().getRangedWeapon() == RangedWeapon.DARK_BOW) {
//                target.hit(attacker, CombatFactory.calcDamageFromType(attacker, target, CombatType.RANGED), secondArrowDelay, CombatType.RANGED).checkAccuracy().submit();
//            } else if (attacker.getCombat().getRangedWeapon() == RangedWeapon.SANGUINE_TWISTED_BOW) {
//                target.hit(attacker, CombatFactory.calcDamageFromType(attacker, target, CombatType.RANGED) / 2, secondArrowDelay, CombatType.RANGED).checkAccuracy().submit();
//            }
//        }
    }

    private boolean ballista(int weaponId) {
        return weaponId == 19478 || weaponId == 19481;
    }

    // The hitmark delay without pid. If pid, it gets adjusted elsewhere.
    // https://i.gyazo.com/d84b1fb9969e3166ff5abf2978e77b4d.png
    private int calcHitDelay(int weaponId, WeaponType weaponType, int dist) {
        if (ballista(weaponId))
            return (dist <= 4) ? 2 : 3;
        if (weaponId == 12926 || weaponType == WeaponType.CHINCHOMPA)   // Blowpipe / chins longer range throwning weps
            return (dist <= 5) ? 2 : 3;
        if (weaponType == THROWN) {
            return 2; // darts / knives with max dist 4
        } else {
            return (dist <= 2) ? 3 : (dist <= 8) ? 3 : 4; // shortbow (and darkbow), longbow, karils xbow, crystal bow, crossbows
        }
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        RangedWeapon weapon = mob.getCombat().getRangedWeapon();
        if (weapon != null) {

            // Long range fight type has longer attack distance than other types
            if (mob.getCombat().getFightType() == weapon.getType().getLongRangeFightType()) {
                return weapon.getType().getLongRangeDistance();
            }

            return weapon.getType().getDefaultDistance();
        }
        return 6;
    }

    public void handleAfterHit(Hit hit) {
        if (hit.getAttacker() == null) {
            return;
        }

        final RangedWeapon rangedWeapon = hit.getAttacker().getCombat().getRangedWeapon();
        if (rangedWeapon == null) {
            return;
        }

        boolean chins = rangedWeapon == RangedWeapon.CHINCHOMPA;

        if (chins) {
            hit.getTarget().performGraphic(new Graphic(157, 100, 0));
        }
    }
}
