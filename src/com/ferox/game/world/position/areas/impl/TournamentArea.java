package com.ferox.game.world.position.areas.impl;

import com.ferox.game.content.tournaments.TournamentManager;
import com.ferox.game.content.tournaments.TournamentUtils;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Area;
import com.ferox.game.world.position.areas.Controller;

import java.util.Arrays;

/**
 * @author Patrick van Elderen | Zerikoth | PVE
 * @date maart 23, 2020 13:19
 */
public class TournamentArea extends Controller {

    public TournamentArea() {
        super(Arrays.asList(
            //Lobby
            new Area(1672, 4691, 1694, 4714),

            //Fight area
            new Area(1699, 4691, 1721, 4714)));
    }

    @Override
    public void enter(Mob mob) {

    }

    @Override
    public void leave(Mob mob) {
        if (mob.isPlayer()) {
            Player player = (Player) mob;
            player.getInterfaceManager().openWalkable(-1);

            //Clear all items when leaving the area, players could use reflection run out and bank.
            TournamentManager.leaveTourny(player,false,false);
        }
    }

    @Override
    public void process(Mob mob) {
        if (mob.isPlayer()) {
            Player player = mob.getAsPlayer();
            player.getInterfaceManager().openWalkable(TournamentUtils.TOURNAMENT_WALK_INTERFACE);
        }
    }

    @Override
    public boolean canTeleport(Player player) {
        return player.getPlayerRights().isDeveloperOrGreater(player);
    }

    @Override
    public boolean canAttack(Mob attacker, Mob target) {
        return true;
    }

    @Override
    public boolean canTrade(Player player, Player target) {
        return false;
    }

    @Override
    public boolean isMulti(Mob mob) {
        return false;
    }

    @Override
    public boolean canEat(Player player, int itemId) {
        return true;
    }

    @Override
    public boolean canDrink(Player player, int itemId) {
        return true;
    }

    @Override
    public void onPlayerRightClick(Player player, Player rightClicked, int option) {
    }

    @Override
    public void defeated(Player player, Mob mob) {
    }

    @Override
    public boolean handleObjectClick(Player player, GameObject object, int type) {
        return false; // dealt with elsewhere
    }

    @Override
    public boolean handleNpcOption(Player player, Npc npc, int type) {
        return false;
    }

    @Override
    public boolean inside(Mob mob) {
        return false; // no need, assuming coords are accurate
    }

    @Override
    public boolean useInsideCheck() {
        return false; // no need, assuming coords are accurate
    }
}
