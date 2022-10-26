package com.ferox.game.world.entity.combat.method.impl.npcs.raids;


import com.ferox.fs.NpcDefinition;
import com.ferox.game.content.raids.chamber_of_secrets.ChamberOfSecretsNpc;
import com.ferox.game.content.raids.party.Party;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.combat.method.impl.npcs.bosses.wilderness.vetion.VetionMinion;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Tile;
import com.ferox.util.chainedwork.Chain;
import com.ferox.util.timers.TimerKey;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.ferox.util.NpcIdentifiers.*;

public class Vespula extends CommonCombatMethod {
    @Override
    public void prepareAttack(Mob mob, Mob target) {
        Player player = (Player) target;
        Party party = player.raidsParty;
        if(party == null) {
            return;
        }

        int chance = World.getWorld().random(20);
        if(chance < 5) {
            lightning(player);
        } else if(chance >= 5 && chance <=15){
            attack(player);
        } else {
            if (mob.hp() <= 170 && !party.vespulaAddsSpawned()) {
                spawnVespineSoldier((Npc) mob, target);
            }
        }

    }
    private void spawnVespineSoldier(Npc vetion, Mob target) {
        Player player = (Player) target;
        Party party = player.raidsParty;
        if(party == null) {
            return;
        }

        List<Npc> minions = new ArrayList<>();
        for (int index = 0; index < 2; index++) {
            VespulaMinion minion = new VespulaMinion(vetion, target);
            minions.add(minion);
            World.getWorld().registerNpc(minion);
            party.monsters.add(minion);
            party.vespulaadds.add(minion);


        }
        party.vespulaAddsSpawned(true);

    }
    private void attack(Player player) {


        mob.animate(7455);
        Party party = player.raidsParty;
        if(party == null) {
            return;
        }



        for (Player member : party.getMembers()) {
            if (member != null && member.isInsideRaids()) {

                new Projectile(mob, member, 473, 41, 60, 45, 30, 0, 10, 15).sendProjectile();
                target.hit(mob, CombatFactory.calcDamageFromType(mob, member, CombatType.RANGED), 2, CombatType.RANGED).checkAccuracy().submit();
            }
        }


        mob.getTimers().register(TimerKey.COMBAT_ATTACK, 4);
    }

    private void lightning(Player player) {
        //player.message("here lightning");
        mob.animate(7455);
        Party party = player.raidsParty;
        if(party == null) {
            return;
        }

         for (Player member : party.getMembers()) {
//             if(member!=null)
//                 member.message("not null");
//                 else
//                     member.message("is null");
//
//             if(!member.isInsideRaids())
//                 member.message("not in raids?");
//             else
//                 member.message("is in raid");


         //  if (member != null && member.isInsideRaids()) {
   //     member.message("inside lightning");
        var tile1 = member.tile().copy();
      //  System.out.println("player tile: "+player.tile().getX());
        var tile2 = member.tile().copy().transform(1, 0);
        var tile3 = member.tile().copy().transform(1, 1);
        //System.out.println("player tile: "+player.tile().getX());

       // System.out.println("player tile after: "+player.tile().toString());
        new Projectile(mob.getCentrePosition(), tile1, 1, 731, 125, 40, 25, 0, 0, 16, 96).sendProjectile();
        new Projectile(mob.getCentrePosition(), tile2, 1, 731, 125, 40, 25, 0, 0, 16, 96).sendProjectile();
        new Projectile(mob.getCentrePosition(), tile3, 1, 731, 125, 40, 25, 0, 0, 16, 96).sendProjectile();
        Chain.bound(null).runFn(5, () -> {
            World.getWorld().tileGraphic(83, new Tile(tile1.getX(), tile1.getY(), member.getZ()), 0, 0);
            World.getWorld().tileGraphic(83, new Tile(tile2.getX(), tile2.getY(), member.getZ()), 0, 0);
            World.getWorld().tileGraphic(83, tile3, 0, 0);
            if (member.tile().equals(tile1) || member.tile().equals(tile2) || member.tile().equals(tile3)) {
                     member.hit(mob, World.getWorld().random(1, 23), CombatType.MAGIC).submit();
               // member.hit(mob, CombatFactory.calcDamageFromType(mob, member, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
             //   member.message("here");

            }
        });
           }
        // }
        mob.getTimers().register(TimerKey.COMBAT_ATTACK, 6);
    }
    private void meleeAttack() {
        mob.animate(mob.attackAnimation());
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
    }

    private void magicAttack() {
        //mob.forceChat("MAGIC ATTACK");
        var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
        //mob.graphic(194);
        new Projectile(mob, target, 1382, 20, 12 * tileDist, 35, 30, 0).sendProjectile();

        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();

        target.delayedGraphics(1028,0, delay);
        mob.animate(mob.attackAnimation());
    }

    private void stealGoodMemories() {
        //     mob.forceChat("STEAL MEMORIES!");
        mob.animate(7422);

        var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
        new Projectile(mob, target, 1382, 20, 12 * tileDist, 35, 30, 0).sendProjectile();

        final Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC);
        hit.checkAccuracy().submit();

        mob.heal(hit.getDamage());
        mob.graphic(1423);

        target.animate(2046);
        target.graphic(1433);
        target.message("Vespula heals!");
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 8;
    }

    public static void death(Npc form1){

        Npc kek = Npc.of(VESPULA_7532, form1.tile().copy());
        World.getWorld().registerNpc(kek);

    }

}
