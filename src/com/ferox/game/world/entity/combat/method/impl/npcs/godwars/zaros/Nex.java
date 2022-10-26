package com.ferox.game.world.entity.combat.method.impl.npcs.godwars.zaros;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.SplatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.combat.method.impl.npcs.godwars.GwdLogic;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.masks.graphics.Graphic;
import com.ferox.game.world.entity.mob.Direction;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.position.Area;
import com.ferox.game.world.position.Boundary;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;
import com.ferox.util.timers.TimerKey;

import java.time.Instant;
import java.util.*;

import static com.ferox.util.NpcIdentifiers.*;

public class Nex extends CommonCombatMethod {

    public static boolean isMinion(Npc n) {
        return n.id() == FUMUS || n.id() == UMBRA || n.id() == CRUOR || n.id() == GLACIES;
    }

    private static final Area ENCAMPMENT = new Area(2909, 5187, 2941, 5220);

    public static Area getENCAMPMENT() {
        return ENCAMPMENT;
    }

    private static Mob lastBossDamager = null;

    public static Mob getLastBossDamager() {
        return lastBossDamager;
    }

    public static void setLastBossDamager(Mob lastBossDamager) {
        Nex.lastBossDamager = lastBossDamager;
    }

    private void umbra_phase_aoe_attack(Mob target) {

        mob.forceChat("RUN AWAY LITTLE GIRL RUN AWAY!");
        mob.animate(9183);

        Boundary ventBounds =  target.boundaryBounds(6);
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

        for (Tile ventTile : ventTiles) {
            World.getWorld().tileGraphic(382, ventTile, 0, 1);
        }


                Chain.bound(null).runFn(5, () -> {

                    if (ventTiles.stream().anyMatch(ventTile -> ventTile.equals(target.tile()))) {
                        target.hit(mob, World.getWorld().random(1, 30), 5);
                        target.skills().alterSkill(Skills.PRAYER,-5);
                    }

                });


       // Chain.bound(null).runFn(6, ventTiles::clear);
        specialAttack = true;
        mob.getTimers().register(TimerKey.COMBAT_ATTACK, 6);
    }
//    public void checkNexDeath() {
//        if(AllPlayersInNexRoom().size() == 0){
//            World.getWorld().unregisterAll(getENCAMPMENT());
//
//
//            }
//        }
//    }
public void fumus_drag(Mob randomtarget){


    mob.animate(9181);
    int hit = CombatFactory.calcDamageFromType(mob, randomtarget, CombatType.MELEE);
    mob.forceChat("FLEEEE");
    randomtarget.hit(mob, hit, CombatType.MELEE).submit();
    CombatFactory.disableProtectionPrayers((Player) randomtarget);
    randomtarget.teleport(mob.tile());
    specialAttack = true;

}
    private void melee_attack(Npc npc, Mob target) {
        Tile tile = target.tile();
        mob.animate(9181);


       AllPlayersInNexRoom().forEach(player -> {
            if (tile.inSqRadius(player.tile(), 2)) {
                target.hit(npc, CombatFactory.calcDamageFromType(npc, player, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
            }
        });

        npc.animate(npc.attackAnimation());
        npc.getTimers().register(TimerKey.COMBAT_ATTACK, npc.combatInfo().attackspeed);
    }


    /**
     * Handles the magic attack based on whos alive
     */
    private void magic_attack(Mob mob, Mob target) {


        mob.animate(9182);



                int tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
                int delay = Math.max(1, (30 + (tileDist * 12)) / 30);
            new Projectile(mob, target,  mob.getAttribOr(AttributeKey.NEX_FUMUS_SPAWNED, false) ? 1997 :  mob.getAttribOr(AttributeKey.NEX_UMBRA_SPAWNED, false) ? 1998 :  mob.getAttribOr(AttributeKey.NEX_CRUOR_SPAWNED, false) ? 2002 :  mob.getAttribOr(AttributeKey.NEX_GLACIES_SPAWNED, false) ? 2010 : 2007 , 20, 12 * tileDist, 80, 30, 0).sendProjectile();

                target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
            target.delayedGraphics(new Graphic(2008, 90, 0), delay);

        mob.getTimers().register(TimerKey.COMBAT_ATTACK, 6);
    }

    ArrayList<Player> possibletargets = new ArrayList<>();

    public Player randomPlayerInNexRoom() {

        World.getWorld().getPlayers().forEachInArea(getENCAMPMENT(), player -> {
            possibletargets.add(player);
        });

        return possibletargets.get(new Random().nextInt(possibletargets.size()));
    }

    // a cache to hold the amounts of possible targets so you only have to do the
    // calculation when the size() changes (aka person leaves/enters)
    public ArrayList<Player>  possibletarget_cache = new ArrayList<>();

    public List<Player> AllPlayersInNexRoom() {
possibletargets.clear();
        World.getWorld().getPlayers().forEachInArea(getENCAMPMENT(), player -> {
            possibletargets.add(player);
        });

        return possibletargets;
    }
private void poison_random_player(Mob thepoisontarget){


    thepoisontarget.forceChat("*cough*");
    thepoisontarget.poison(2);
    specialAttack = true;
}

    private void mark_for_bloodsacrifice(Mob randomtarget){


        mob.forceChat("I demand a blood sacrifice!");
        randomtarget.teleport(mob.tile());
        randomtarget.message("You have been marked for blood sacrifice! Run 5 tiles away!");

        Chain.bound(null).runFn(8, () -> {
            if (mob.tile().isWithinDistance(randomtarget.tile(),5)){
                randomtarget.hit(mob, 50, 1);
                randomtarget.graphic(2000);
                mob.graphic(2000);
                mob.heal(50);
                randomtarget.skills().alterSkill(Skills.PRAYER,-5);
                randomtarget.message("You took damage");
            } else {
                randomtarget.message("You did not take damage.");
            }

        });
        specialAttack = true;
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
    private void containment(Mob mob, Mob target) {
        mob.animate(9182);
        Tile base = mob.tile().copy();

        final List<Tile> crystalSpots = new ArrayList<>(List.of(new Tile(0, 0, 0)));

//        if(mob.hp() <= 680) {
//            crystalSpots.add(new Tile(3, 6, 0));
//        }



        Tile centralCrystalSpot = new Tile(3, 1, 0);
        Tile central = base.transform(centralCrystalSpot.x, centralCrystalSpot.y);
        ArrayList<Tile> spots = new ArrayList<>(crystalSpots);
        int[] ticker = new int[1];
        Chain.bound(null).runFn(2, () -> World.getWorld().tileGraphic(369, central, 0, 0)).repeatingTask(1, t -> {
            if (ticker[0] == 10) {
                t.stop();
                return;
            }
            for (Tile spot : spots) {
                World.getWorld().tileGraphic(369, base.transform(spot.x, spot.y), 0, 0);
            }
            ArrayList<Tile> newSpots = new ArrayList<>();
            for (Tile spot : new ArrayList<>(spots)) {
                final Tile curSpot = base.transform(spot.x, spot.y);
                if (curSpot.equals(target.tile())) {
                    target.hit(mob, World.getWorld().random(1, 35), SplatType.HITSPLAT);
                    target.skills().alterSkill(Skills.PRAYER,-5);
                    target.freeze(3,mob);
                } else {
                    final Direction direction = Direction.getDirection(curSpot, target.tile());
                    Tile newSpot = spot.transform(direction.x, direction.y);
                    newSpots.add(newSpot);
                }
            }

            spots.clear();
            spots.addAll(newSpots);
            ticker[0]++;
        });
        specialAttack = true;
    }

    @Override
    public void prepareAttack(Mob mob, Mob target) {



        boolean nex_fumus_spawned = mob.getAttribOr(AttributeKey.NEX_FUMUS_SPAWNED, false);
        boolean nex_umbra_spawned = mob.getAttribOr(AttributeKey.NEX_UMBRA_SPAWNED, false);
        boolean nex_cruor_spawned = mob.getAttribOr(AttributeKey.NEX_CRUOR_SPAWNED, false);
        boolean nex_glacies_spawned = mob.getAttribOr(AttributeKey.NEX_GLACIES_SPAWNED, false);

target = Utils.getRandomPlayerinAnArea(possibletargets,getENCAMPMENT());
//if(target == null){
//    System.out.println("target is null");
//    return;
//}

        mob.getCombat().setTarget(target);
        Npc npc = (Npc) mob;
        if (!mob.hasAttrib(AttributeKey.FUMUS_DEAD)) {
            if (mob.hp() <= 2980 && !mob.hasAttrib(AttributeKey.NEX_FUMUS_SPAWNED)) {
                spawnFumus((Npc) mob, target);
            }
        }
        if (!mob.hasAttrib(AttributeKey.UMBRA_DEAD)) {
            if (mob.hp() <= 2040 && !mob.hasAttrib(AttributeKey.NEX_UMBRA_SPAWNED)) {
                spawnUmbra((Npc) mob, target);
            }
        }
        if (!mob.hasAttrib(AttributeKey.CRUOR_DEAD)) {
            if (mob.hp() <= 1360 && !mob.hasAttrib(AttributeKey.NEX_CRUOR_SPAWNED)) {
                spawnCruor((Npc) mob, target);
            }
        }
        if (!mob.hasAttrib(AttributeKey.GLACIES_DEAD)) {
            if (mob.hp() <= 640 && !mob.hasAttrib(AttributeKey.NEX_GLACIES_SPAWNED)) {
                spawnGlacies((Npc) mob, target);
            }
        }


        if (nex_fumus_spawned) {

            int chance = World.getWorld().random(10);
            if (chance < 2) {               //drag
                fumus_drag(target);
            } else if (chance >= 2 && chance <= 8) {
                poison_random_player(target);
            } else if (chance == 3) {

            }
        }
if(nex_umbra_spawned){
    int chance = World.getWorld().random(3);
    if (chance == 1){
        umbra_phase_aoe_attack(target);
    }
}
        if(nex_cruor_spawned){
            int chance = World.getWorld().random(10);
            if (chance < 2){
              mark_for_bloodsacrifice(target);
            }
        }
        if(nex_glacies_spawned){
            int chance = World.getWorld().random(8);
            if (chance < 2){
              containment(mob,target);
            }
        }
        boolean melee_dist = mob.tile().distance(target.tile()) <= 1;
            if (melee_dist) {
                melee_attack(npc,target);
                // If we're in melee distance it's actually classed as if the target hit us -- has an effect on auto-retal in gwd!
                if (GwdLogic.isBoss(mob.getAsNpc().id())) {
                    Map<Mob, Long> last_attacked_map = mob.getAttribOr(AttributeKey.LAST_ATTACKED_MAP, new HashMap<Mob, Long>());
                    last_attacked_map.put(target, System.currentTimeMillis());
                    mob.putAttrib(AttributeKey.LAST_ATTACKED_MAP, last_attacked_map);
                }

            } else {
                magic_attack(npc, target);
            }
possibletargets.clear();

    }
    private void spawnFumus(Npc nex, Mob target) {
        nex.forceChat("Fumus, don't fail me!");

            Fumus minion = new Fumus(nex, target);
            World.getWorld().registerNpc(minion);

        nex.putAttrib(AttributeKey.NEX_FUMUS_SPAWNED, true);
    }
    private void spawnUmbra(Npc nex, Mob target) {
        nex.forceChat("Umbra, don't fail me!");

        Umbra minion = new Umbra(nex, target);
        World.getWorld().registerNpc(minion);

        nex.putAttrib(AttributeKey.NEX_UMBRA_SPAWNED, true);
    }
    private void spawnCruor(Npc nex, Mob target) {
        nex.forceChat("Cruor, don't fail me!");

        Cruor minion = new Cruor(nex, target);
        World.getWorld().registerNpc(minion);

        nex.putAttrib(AttributeKey.NEX_CRUOR_SPAWNED, true);
    }
    private void spawnGlacies(Npc nex, Mob target) {
        nex.forceChat("Glacies, don't fail me!");

        Glacies minion = new Glacies(nex, target);
        World.getWorld().registerNpc(minion);

        nex.putAttrib(AttributeKey.NEX_GLACIES_SPAWNED, true);
    }
private boolean specialAttack;
    @Override
    public int getAttackSpeed(Mob mob) {
        return specialAttack ? 10 :mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 10;
    }
}
