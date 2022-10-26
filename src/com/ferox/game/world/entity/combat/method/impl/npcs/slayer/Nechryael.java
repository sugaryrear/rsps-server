package com.ferox.game.world.entity.combat.method.impl.npcs.slayer;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.npc.NpcDeath;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.region.Region;
import com.ferox.game.world.region.RegionManager;
import com.ferox.game.world.route.routes.ProjectileRoute;
import com.ferox.util.NpcIdentifiers;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PVE
 * @Since augustus 07, 2020
 */
public class Nechryael extends CommonCombatMethod {

    private void basicAttack(Mob mob, Mob target) {
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
        mob.animate(mob.attackAnimation());
    }

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        if (Utils.rollDie(4, 1))
            spawnDeathSpawns(mob, target);
        basicAttack(mob, target);
    }

    public void onDeath(Mob mob) {
        List<Npc> minions = mob.getAttribOr(AttributeKey.MINION_LIST, new ArrayList<Npc>());
        for (Npc deathSpawn : minions) {
            if (!deathSpawn.hidden()) {
                NpcDeath.deathReset(deathSpawn);
                deathSpawn.animate(deathSpawn.combatInfo().animations.death);
                deathSpawn.respawns(false);
            }
        }

        Chain.bound(null).name("DeathSpawnTask").runFn(2, () -> {
            for (Npc deathSpawn : minions) {
                deathSpawn.hidden(true);
                World.getWorld().unregisterNpc(deathSpawn);
            }
        });
    }

    private void spawnDeathSpawns(Mob mob, Mob target) {
        List<Npc> minions = mob.getAttribOr(AttributeKey.MINION_LIST, new ArrayList<Npc>());
        for (Npc deathSpawn : minions) {
            if(deathSpawn.isRegistered()) {
                return;
            }
        }
        for (int i = 0; i < 2; i++) {
            Tile pos = getSpawnPosition(mob, target);
            if (pos == null) {
                continue;
            }
            Npc spawn = new Npc(NpcIdentifiers.DEATH_SPAWN, pos);
            World.getWorld().registerNpc(spawn);
            spawn.putAttrib(AttributeKey.BOSS_OWNER, mob);

            List<Npc> list = mob.getAttribOr(AttributeKey.MINION_LIST, new ArrayList<Npc>());
            list.add(spawn);
            mob.putAttrib(AttributeKey.MINION_LIST, list);
            spawn.getCombat().setTarget(target);
            spawn.face(target.tile());
        }
    }

    private Tile getSpawnPosition(Mob mob, Mob target) {
        List<Tile> tiles = new ArrayList<>(18);
        int radius = 1;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                Region tile = RegionManager.getRegion(target.getX() + x, target.getY() + y);
                if (tile == null || ProjectileRoute.allow(target, target.getX() + x, target.getY() + y)) {
                    tiles.add(new Tile(target.getX() + x, target.getY(), target.getZ()));
                }
            }
        }
        if (tiles.size() == 0) {
            return null;
        }
        return Utils.randomElement(tiles);
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 1;
    }

    @Override
    public boolean canMultiAttackInSingleZones() {
        return true;
    }
}
