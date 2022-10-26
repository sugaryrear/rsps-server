package com.ferox.game.world.entity.combat.method.impl.npcs.bosses;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.combat.skull.SkullType;
import com.ferox.game.world.entity.combat.skull.Skulling;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.masks.graphics.Graphic;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Utils;
import com.ferox.util.timers.TimerKey;

import java.security.SecureRandom;

public class Skotizo extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        int roll = new SecureRandom().nextInt(5);

        //10% chance that the wold boss skulls you!
        if (World.getWorld().rollDie(10, 1)) {
            Skulling.assignSkullState(((Player) target), SkullType.WHITE_SKULL);
            target.message("The "+mob.getMobName()+" has skulled you, be careful!");
        }

        Npc npc = (Npc) mob;
        if (roll == 1) {
            //System.out.println("rolled one, using ranged attack.");
            ranged_attack(npc, target);
        } else if (roll == 2) {
            //System.out.println("rolled two, using magic attack.");
            magic_attack(npc, target);
        } else {
            if (CombatFactory.canReach(npc, CombatFactory.MELEE_COMBAT, target)) {
               // System.out.println("melee distance, using melee attack.");
                melee_attack(npc, target);
            } else if (Utils.rollDie(2, 1)) {
                magic_attack(npc, target);
               // System.out.println("Otherwise rolled magic attack due to out of melee distance.");
            } else {
                ranged_attack(npc, target);
               // System.out.println("Otherwise rolled ranged attack due to out of melee distance.");
            }
        }
    }

    /**
     * Handles the melee attack
     */
    private void melee_attack(Npc npc, Mob target) {
        Tile tile = target.tile();

        World.getWorld().getPlayers().forEach(player -> {
            if (tile.inSqRadius(player.tile(), 2)) {
                //Wild boss, should deal damage regardless of prayer and don't check attack accuracy makes it harder!
                target.hit(npc, CombatFactory.calcDamageFromType(npc, player, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
            }
        });

        npc.animate(npc.attackAnimation());
        npc.getTimers().register(TimerKey.COMBAT_ATTACK, npc.combatInfo().attackspeed);
    }

    /**
     * Handles the magic attack
     */
    private void magic_attack(Npc npc, Mob target) {
        Tile tile = target.tile();

        World.getWorld().getPlayers().forEach(player -> {
            if (tile.inSqRadius(player.tile(), 3)) {
                int tileDist = npc.tile().transform(3, 3, 0).distance(player.tile());
                int delay = Math.max(1, (30 + (tileDist * 12)) / 30);

                new Projectile(npc, player, 165, 20, 12 * tileDist, 80, 30, 0).sendProjectile();

                target.hit(npc, CombatFactory.calcDamageFromType(npc, player, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
                player.delayedGraphics(new Graphic(166, 90, 0), delay);
            }
        });
        npc.animate(69);
        npc.getTimers().register(TimerKey.COMBAT_ATTACK, npc.combatInfo().attackspeed);
    }


    /**
     * Handles the ranged attack
     */
    private void ranged_attack(Npc npc, Mob target) {
        Tile tile = target.tile();

        World.getWorld().getPlayers().forEach(player -> {
            if (tile.inSqRadius(player.tile(), 3)) {
                int tileDist = npc.tile().transform(3, 3, 0).distance(player.tile());
                int delay = Math.max(1, (30 + (tileDist * 12)) / 30);

                new Projectile(npc, player, 1242, 20, 12 * tileDist, 80, 50, 0).sendProjectile();
                target.hit(npc, CombatFactory.calcDamageFromType(npc, player, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
                player.delayedGraphics(new Graphic(1243, 80, 0), delay);
            }
        });

        npc.animate(69);
        npc.getTimers().register(TimerKey.COMBAT_ATTACK, npc.combatInfo().attackspeed);
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
