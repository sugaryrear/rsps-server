package com.ferox.game.world.entity.combat.method.impl.npcs.raids;

import com.ferox.game.content.raids.party.Party;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.masks.graphics.Graphic;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Area;
import com.ferox.game.world.position.Boundary;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;
import com.ferox.util.timers.TimerKey;

import java.util.ArrayList;
import java.util.List;

public class Fluffy extends CommonCombatMethod {



    private void ventAttack(Player player) {
        mob.forceChat("RUN AWAY LITTLE GIRL RUN AWAY!");
        mob.animate(7412);
        Party party = player.raidsParty;
        Boundary npcBounds = new Boundary(new Tile(3344,5264),1);
        Boundary ventBounds =  mob.boundaryBounds(5);
        List<Tile> ventTiles = new ArrayList<>();
        for (int x = ventBounds.getMinimumX(); x < ventBounds.getMaximumX(); x++) {
            for (int y = ventBounds.getMinimumY(); y < ventBounds.getMaximumY(); y++) {
                if (Utils.random(3) == 2) {
                    Tile tile = new Tile(x, y, target.tile().getLevel());
                   // if (!npcBounds.inside(tile)) {
                        ventTiles.add(tile);
                   }
              //  }
            }
        }
//        ventTiles.add(player.tile());
//        ventTiles.add(player.tile().transform(1,1));
//        ventTiles.add(player.tile().transform(1,0));
    //    Tile tile = target.tile();

//        World.getWorld().getPlayers().forEach(player -> {
//            if (tile.inSqRadius(player.tile(), 3)) {
//                int tileDist = npc.tile().transform(3, 3, 0).distance(player.tile());
//                int delay = Math.max(1, (30 + (tileDist * 12)) / 30);
//
//                new Projectile(npc, player, 165, 20, 12 * tileDist, 80, 30, 0).sendProjectile();
//
//                target.hit(npc, CombatFactory.calcDamageFromType(npc, player, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
//                player.delayedGraphics(new Graphic(166, 90, 0), delay);
//            }
//        });

//player.message("min x: "+ventBounds.getMinimumX()+" and min y: "+ventBounds.getMinimumY());
//        player.message("max x: "+ventBounds.getMaximumX()+" and max y: "+ventBounds.getMaximumY());

//        for (Tile rockTile : ventTiles) {
//            new Projectile(mob.getCentrePosition(), rockTile, 1, 1289, 150, 20, 30, 0, 25).sendFor(target.getAsPlayer());
//        }
        //Chain.bound(null).runFn(3, () -> {
            for (Tile ventTile : ventTiles) {
                //player.message(String.valueOf(ventTile));
                World.getWorld().tileGraphic(1290, ventTile, 0, 1);
            }
        //});
//        Tile tile = target.tile();
//        if (tile.inSqRadius(player.tile(), 3)) {
//            int tileDist = mob.tile().transform(3, 3, 0).distance(player.tile());
//            int delay = Math.max(1, (30 + (tileDist * 12)) / 30);
//
//            new Projectile(mob, player, 1289, 20, 12 * tileDist, 80, 30, 0).sendProjectile();
//
//            target.hit(mob, CombatFactory.calcDamageFromType(mob, player, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
//            player.delayedGraphics(new Graphic(166, 90, 0), delay);
//        }
        for (Player member : party.getMembers()) {
            if (member != null && member.isInsideRaids()) {


                // member.message("here damage from vent before");
//                if (ventTiles.stream().anyMatch(p -> p.getX() == member.tile().getX() && p.getY() == member.tile().getY())) {
//                    member.message("here using runeddragon tihng");
//                }
                Chain.bound(null).runFn(5, () -> {

                    if (ventTiles.stream().anyMatch(ventTile -> ventTile.equals(member.tile()))) {
                        // member.message("here damage from vent");
                        member.hit(mob, World.getWorld().random(1, 30), 5);
                    }

                });
            }
        }
        Chain.bound(null).runFn(6, ventTiles::clear);
        mob.getTimers().register(TimerKey.COMBAT_ATTACK, 6);
    }

//    private void fallingRocks(Player player) {
//        mob.forceChat("SQUASH");
//        mob.animate(7414);
//        Party party = player.raidsParty;
//        Boundary npcBounds = mob.boundaryBounds();
//        Boundary rockBounds = mob.boundaryBounds(11);
//        List<Tile> rockTiles = new ArrayList<>();
//        for (int x = rockBounds.getMinimumX(); x < rockBounds.getMaximumX(); x++) {
//            for (int y = rockBounds.getMinimumY(); y < rockBounds.getMaximumY(); y++) {
//                if (Utils.random(3) == 2) {
//                    Tile tile = new Tile(x, y, mob.tile().getLevel());
//                    if (!npcBounds.inside(tile)) {
//                        rockTiles.add(tile);
//                    }
//                }
//            }
//        }
//
//        for (Tile rockTile : rockTiles) {
//            new Projectile(mob.getCentrePosition(), rockTile, 1, 1327, 150, 20, 30, 0, 25).sendFor(target.getAsPlayer());
//        }
//
//        for (Player member : party.getMembers()) {
//            if (member != null && member.isInsideRaids()) {
//                if (rockTiles.stream().anyMatch(rockTile -> rockTile.equals(member.tile()))) {
//                    member.hit(mob, World.getWorld().random(1, 30), 5);
//                    member.message("You took damage from falling rocks!");
//                }
//            }
//        }
//
//       // Chain.bound(null).runFn(3, rockTiles::clear);
//        mob.getTimers().register(TimerKey.COMBAT_ATTACK, 6);
//    }

    private void rangeAttack(Player player) {
        mob.face(null); // Stop facing the target
        mob.animate(7410);
        Party party = player.raidsParty;
        for (Player member : party.getMembers()) {
           // member.message("vasa ranged attack here 1");

            if (member != null && member.isInsideRaids()) {
            //    member.message("vasa ranged attack");

                var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
                var delay = Math.max(1, (20 + (tileDist * 12)) / 30);

                new Projectile(mob, member, 182, 25, 12 * tileDist, 65, 31, 0, 15, 220).sendProjectile();
                member.hit(mob, CombatFactory.calcDamageFromType(mob, member, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
                member.delayedGraphics(183, 50, delay);
            }
        }

        mob.face(target.tile()); // Go back to facing the target.
    }

    private void meleeAttack() {
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
        mob.animate(mob.attackAnimation());
    }

    private void magicAttack(Player player) {
        mob.face(null); // Stop facing the target
        mob.animate(7409);
        Party party = player.raidsParty;
        for (Player member : party.getMembers()) {
            if (member != null && member.isInsideRaids()) {
                //member.message("vasa magic attack");
                var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
                var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
                new Projectile(mob, member, 1327, 25, 12 * tileDist, 65, 31, 0, 15, 220).sendProjectile();
                member.hit(mob, CombatFactory.calcDamageFromType(mob, member, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
            }
        }
        mob.face(target.tile()); // Go back to facing the target.
    }

    private boolean specialAttack = false;

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        if (!mob.isNpc() || !target.isPlayer())
            return;

        Player player = (Player) target;

    //    if (World.getWorld().rollDie(10, 1)) { //10% chance the npc sends lightning
//            lighting(player);
//            specialAttack = true;
       // }
//
//        if (World.getWorld().rollDie(10, 1)) { //10% chance the npc sends vents
//            ventAttack(player);
//            specialAttack = true;
//        }
//
//        if (World.getWorld().rollDie(10, 1)) { //10% chance the npc sends rocks
//            fallingRocks(player);
//            specialAttack = true;
//        }
//
//        // Determine if we're going to melee or mage


        //if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
            int chance = World.getWorld().random(10);
            if (chance <6) {
             magicAttack(player);

            } else if (chance >=6) {
                ventAttack(player);
                specialAttack = true;
            }
//            else {
//                meleeAttack();
//            }
       // } else {
//            int chance = World.getWorld().random(3);
//            if (chance == 1) {
//                rangeAttack(player);
//            } else {
//                magicAttack(player);
//            }
       // }
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return specialAttack ? 10 : mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 8;
    }
}
