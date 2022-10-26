package com.ferox.game.task.impl;

import com.ferox.game.task.Task;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.route.RouteFinder;
import com.ferox.game.world.route.routes.DumbRoute;
import com.ferox.util.Debugs;
import com.ferox.util.NpcIdentifiers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A {@link Task} implementation which handles the following of a
 * {@link Mob}
 *
 */
public class MobFollowTask extends Task {

    private static final Logger logger = LogManager.getLogger(MobFollowTask.class);
    private final Mob mob;
    private Mob following;

    public MobFollowTask(Mob mob, Mob following) {
        super("MobFollowTask", 1, mob, true);
        this.mob = mob;
        this.following = following;
    }

    @Override
    protected void execute() {
        if (mob.isNpc() && mob.getAsNpc().finished()) {
            stop();
            return;
        }
        Debugs.CB_FOLO.debug(mob, "folo start", mob.getCombat().getTarget(), false);
        // Combat dummy can't follow
        if (mob.isNpc() && mob.getAsNpc().isCombatDummy()) {
            return;
        }

        if (mob.tile().distance(following.tile()) > 64 || mob.tile().level != following.tile().level) {
            stop();
            return;
        }
        //System.out.println("execute follow task");
        // Update interaction
        mob.setEntityInteraction(following);

        //If entity kills a another entity, don't make them move.
        if (following.dead()) {
            //System.out.println("Got a PK, resetting movement");
            mob.getMovementQueue().clear();
            return;
        }

        //If mob is a killed npc, don't make them move.
        if (mob.isNpc() && mob.dead()) {
            mob.getMovementQueue().clear();
            return;
        }
        // Block if our movement is locked.
        if (!mob.getMovementQueue().canMove()) {
            return;
        }

        boolean combatFollow = CombatFactory.isAttacking(mob) && mob.getCombat().getTarget().equals(following);

        Debugs.CB_FOLO.debug(mob, "goal dist "+mob.tile().distance(following.tile())+" "+combatFollow, following, false);

        if (!combatFollow && following.boundaryBounds().inside(mob.tile(), mob.getSize()) && (!following.getMovementQueue().isMoving() && !following.hasWalkSteps()) && !following.getPreviousTile().equals(mob.tile())) {
            final Tile walkable = RouteFinder.findWalkable(following.tile());
            mob.getRouteFinder().routeAbsolute(walkable.x, walkable.y);
            return;
        }

        int destX, destY;
        if (following.getMovement().hasMoved()) {
            destX = following.getMovement().lastFollowX;
            destY = following.getMovement().lastFollowY;
        } else {
            destX = following.getMovement().followX;
            destY = following.getMovement().followY;
        }

        if (mob.isNpc()) {
           //if (destX == -1 || destY == -1)
                DumbRoute.step(mob, following, 1); // npcs cant run
            //else if (!mob.isAt(destX, destY))
            //    DumbRoute.step(mob, destX, destY);
        } else {
            if (destX == -1 || destY == -1) {
                final Tile walkable = RouteFinder.findWalkable(following.getX(), following.getY(), following.getZ());
                following.getMovement().lastFollowX = following.getMovement().followX = destX = walkable.x;
                following.getMovement().lastFollowY = following.getMovement().followY = destY = walkable.y;
            }
            mob.smartPathTo(new Tile(destX, destY)); // supports running
        }
    }

    public MobFollowTask setFollowing(Mob following) {
        this.following = following;
        return this;
    }

    public Mob getFollow() {
        return following;
    }
}
