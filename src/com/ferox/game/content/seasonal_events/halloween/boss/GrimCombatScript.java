package com.ferox.game.content.seasonal_events.halloween.boss;

import com.ferox.game.task.Task;
import com.ferox.game.task.TaskManager;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.util.chainedwork.Chain;

import static com.ferox.util.NpcIdentifiers.FEAR_REAPER;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 15, 2021
 */
public class GrimCombatScript extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        if(World.getWorld().rollDie(10,1)) {
            spawnMinions((Npc) mob);
        }

        if(World.getWorld().rollDie(5,1)) {
            fallingCandy((Npc) mob, target);
        }

        meleeAttack((Npc) mob, target);
    }

    private void spawnMinions(Npc mob) {
        Npc fearRepearOne = new Npc(FEAR_REAPER, mob.tile().transform(-2,0));
        Npc fearRepearTwo = new Npc(FEAR_REAPER, mob.tile().transform(+2,0));
        Task.runOnceTask(2, t -> {
            Chain.runGlobal(1, () -> {
                World.getWorld().registerNpc(fearRepearOne);
                World.getWorld().registerNpc(fearRepearTwo);

                World.getWorld().getPlayers().forEach(p -> {
                    if (p != null && mob.tile().inSqRadius(p.tile(), 12)) {
                        fearRepearOne.face(p.tile());
                        fearRepearTwo.face(p.tile());
                        new Projectile(fearRepearOne, p, 1403, 45, 140, 50, 33, 0).sendProjectile();
                        new Projectile(fearRepearTwo, p, 1404, 45, 120, 50, 33, 0).sendProjectile();
                        target.hit(fearRepearOne, CombatFactory.calcDamageFromType(fearRepearOne, p, CombatType.MAGIC), 3, CombatType.MAGIC).checkAccuracy().submit();
                        target.hit(fearRepearTwo, CombatFactory.calcDamageFromType(fearRepearTwo, p, CombatType.RANGED), 4, CombatType.RANGED).checkAccuracy().submit();
                    }
                });
            }).then(5, () -> {
                World.getWorld().unregisterNpc(fearRepearOne);
                World.getWorld().unregisterNpc(fearRepearTwo);
            });
        });
    }

    private void fallingCandy(Mob mob, Mob target) {
        World.getWorld().getPlayers().forEach(p -> {
            if (p != null && target.tile().inSqRadius(p.tile(), 12)) {
                final var tile = p.tile().copy();

                new Projectile(mob.tile().transform(1, 1,0), tile, 1, 1671, 165, 30, 200, 6, 0).sendProjectile();

                Chain.bound(null).runFn(6, () -> {
                    World.getWorld().tileGraphic(1765, tile, 5, 0);
                    if (p.tile().equals(tile)) {
                        p.hit(mob, World.getWorld().random(1, 10));
                    }
                });
            }
        });
    }

    private void meleeAttack(Npc npc, Mob target) {
        npc.animate(8056);
        npc.face(null); // Stop facing the target
        World.getWorld().getPlayers().forEach(p -> {
            if (p != null && target.tile().inSqRadius(p.tile(), 12)) {
                p.hit(npc, CombatFactory.calcDamageFromType(npc, p, CombatType.MELEE), 1, CombatType.MELEE).checkAccuracy().submit();
            }
        });
        npc.face(target.tile()); // Go back to facing the target.
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
