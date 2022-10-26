package com.ferox.game.world.entity.combat.method.impl.npcs.bosses.kalphite;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;
import com.ferox.util.timers.TimerKey;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 24, 2020
 */
public class KalphiteQueenSecondForm extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        int distance = getAttackDistance(target);
        boolean inDistance = target.boundaryBounds().within(mob.tile(), mob.getSize(), distance);
        if (inDistance) {
            if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target) && Utils.rollDie(4, 1)) {
                attack(((Npc)mob), ((Player)target), CombatType.MELEE);
            } else {
                int random = Utils.RANDOM_GEN.get().nextInt(100);
                attack(((Npc)mob), ((Player)target), random < 50 ? CombatType.MAGIC : CombatType.RANGED);
            }
        }
    }

    private void attack(Npc npc, Player target, CombatType combatType) {

        int attackAnimation = KalphiteQueen.animation(npc.id(), combatType);

        npc.animate(attackAnimation);

        switch(combatType) {
            case MELEE:
                target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
                break;
            case RANGED:
                for(Player player : World.getWorld().getPlayers()) {
                    if(player != null && player.tile().inArea(KalphiteQueen.getArea())) {
                        new Projectile(npc, target, 473, 41, 60, 45, 30, 0, 10, 15).sendProjectile();
                        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.RANGED), 2, CombatType.RANGED).checkAccuracy().submit();
                    }
                }
                break;
            case MAGIC:
                npc.graphic(278);
                for(Player player : World.getWorld().getPlayers()) {
                    if (player != null && player.tile().inArea(KalphiteQueen.getArea())) {
                        new Projectile(npc, target, 280, 41, 60, 45, 30, 0, 10, 15).sendProjectile();
                        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.MAGIC), 2, CombatType.MAGIC).checkAccuracy().submit();

                        target.delayedGraphics(281,0,2);
                    }
                }
                break;
        }
        npc.getTimers().register(TimerKey.COMBAT_ATTACK, 4);
    }

    public static void death(Npc npc) {
        Npc spawn = new Npc(6500, npc.tile());

        Chain.bound(null).runFn(spawn.combatInfo().respawntime, () -> {
            spawn.respawns(false);
            World.getWorld().registerNpc(spawn);
            npc.animate(6240);
            //here? not sure show ingame again
        });
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
