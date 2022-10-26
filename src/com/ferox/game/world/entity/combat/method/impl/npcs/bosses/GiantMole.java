package com.ferox.game.world.entity.combat.method.impl.npcs.bosses;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GiantMole extends CommonCombatMethod {

    private static final int BURROW_DOWN_ANIM = 3314;
    private static final int BURROW_SURFACE_ANIM = 3315;

    ArrayList<Tile> burrow_points = new ArrayList<>(Arrays.asList(
        new Tile(1770   ,5201),
        new Tile(1750,5196),
        new Tile(1750,5174),
        new Tile(1769,5174)

    ));
    @Override
    public void prepareAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
        if (World.getWorld().rollDie(10, 1)) {
            burrow((Npc) mob,target);
        }
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 1;
    }

    private void burrow(Npc npc, Mob target) {
        Random rand = new Random();
        Tile randomElement = burrow_points.get(rand.nextInt(burrow_points.size()));

        Tile burrowDestination = randomElement;
        target.getCombat().reset();//When mole digs reset combat
        npc.lockNoDamage();
        npc.faceEntity(null);
        npc.getMovement().reset();
        npc.animate(BURROW_DOWN_ANIM);
        target.message("The mole burrows...");
        Chain.bound(null).runFn(3, () -> {
            npc.teleport(burrowDestination);
            npc.animate(BURROW_SURFACE_ANIM);
        }).then(2, npc::unlock);
    }

}
