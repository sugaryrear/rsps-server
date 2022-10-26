package com.ferox.game.world.entity.combat.method.impl.npcs.bosses.aragog;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.player.Player;

/**
 * @author Patrick van Elderen | April, 07, 2021, 16:35
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class Aragog extends CommonCombatMethod {

    private void webAttack() {
        //mob.forceChat("WEB -> RANGED ATTACK");
        if(target.isPlayer()) {
            Player player = target.getAsPlayer();

            //In raids its multi attack
            if(player.isInsideRaids()) {
                mob.face(null); // Stop facing the target
                //Target all raids party members
                if(player.raidsParty != null) {
                    for (Player p : player.raidsParty.getMembers()) {
                        if(p == null || p.tile().level != mob.tile().level) {
                            continue;
                        }
                        var tileDist = mob.tile().transform(3, 3, 0).distance(p.tile());
                        new Projectile(mob, p,1254, 20, 12 * tileDist, 30, 30, 0, 14, 5).sendProjectile();
                        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
                        p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.MAGIC), delay, CombatType.RANGED).checkAccuracy().submit();
                    }
                }
                mob.face(target.tile()); // Go back to facing the target.
            }
        }
    }

    private void fireSkullAttack() {
        //mob.forceChat("FIRE SKULL -> MAGIC ATTACK");
        if(target.isPlayer()) {
            Player player = target.getAsPlayer();

            //In raids its multi attack
            if(player.isInsideRaids()) {
                mob.face(null); // Stop facing the target
                //Target all raids party members
                if(player.raidsParty != null) {
                    for (Player p : player.raidsParty.getMembers()) {
                        if(p == null || p.tile().level != mob.tile().level) {
                            continue;
                        }
                        var tileDist = mob.tile().transform(3, 3, 0).distance(player.tile());
                        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
                        new Projectile(mob, p,88, 20, 12 * tileDist, 30, 30, 0, 14, 5).sendProjectile();
                        p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
                    }
                }
                mob.face(target.tile()); // Go back to facing the target.
            }
        }
    }

    private void meleeAttack() {
        //mob.forceChat("MELEE ATTACK");
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
        mob.animate(mob.attackAnimation());
    }

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        // Determine if we're going to melee or mage
        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
            int chance = World.getWorld().random(6);
            if (chance == 1) {
                fireSkullAttack();
            } else if (chance == 2) {
                webAttack();
            } else {
                meleeAttack();
            }
        } else {
            int chance = World.getWorld().random(3);
            if (chance == 1) {
                fireSkullAttack();
            } else {
                webAttack();
            }
            // Do an animation..
            mob.animate(5322);
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
