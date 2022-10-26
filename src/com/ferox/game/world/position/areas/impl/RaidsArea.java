package com.ferox.game.world.position.areas.impl;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.areas.Controller;
import com.ferox.util.Utils;

import java.util.Collections;
import java.util.stream.IntStream;

import static com.ferox.game.content.raids.party.Party.*;
import static com.ferox.game.world.entity.AttributeKey.PERSONAL_POINTS;

/**
 * @author Patrick van Elderen | May, 10, 2021, 18:44
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class RaidsArea extends Controller {

    private static final int POINTS_WIDGET = 12000;

    public RaidsArea() {
        super(Collections.emptyList());
    }

    @Override
    public void enter(Mob mob) {

    }

    @Override
    public void leave(Mob mob) {
    }

    @Override
    public void process(Mob mob) {
        if (mob.isPlayer()) {
            Player player = (Player)mob;
            var party = player.raidsParty;
            if(party != null && party.getRaidStage() < 10) {
                player.getPacketSender().sendString(NAME_FRAME, player.getUsername()+":");
                player.getPacketSender().sendString(TOTAL_POINTS,"" + Utils.formatNumber(party.totalPoints()));
                player.getPacketSender().sendString(POINTS,"" + Utils.formatNumber(player.<Integer>getAttribOr(PERSONAL_POINTS, 0)));
            //    player.getPacketSender().sendString(TIME, party.getLeader().getChamberOfSecrets().getTimeSinceStart());
                player.getPacketSender().sendString(TIME, party.getLeader().getChamberOfSecrets().getStringTime(party.getLeader().getChamberOfSecrets().getTimeSinceStart()));
                player.getInterfaceManager().openWalkable(POINTS_WIDGET);
            } else {
                player.getInterfaceManager().openWalkable(-1);

            }

        }
    }

    @Override
    public boolean canTeleport(Player player) {
        return false;
    }

    @Override
    public boolean canAttack(Mob attacker, Mob target) {
        return !attacker.isPlayer() || !target.isPlayer();
    }

    @Override
    public boolean canTrade(Player player, Player target) {
        return true;
    }

    @Override
    public boolean isMulti(Mob mob) {
        return true;
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
        return false;
    }

    @Override
    public boolean handleNpcOption(Player player, Npc npc, int type) {
        return false;
    }
    public int[] raids_regions = new int[]{
        13136,13137,13138,13139,13140,13392,13393,13394,13395,13396,13397,12889
    };

    @Override
    public boolean inside(Mob mob) {
        return IntStream.of(raids_regions).anyMatch(id -> mob.tile().region()  == id);
//        return mob.tile().region() == 12889 || mob.tile().region() == 13136 || mob.tile().region() == 13137 || mob.tile().region() == 13138 || mob.tile().region() == 13139
//            ;
    }

    @Override
    public boolean useInsideCheck() {
        return true;
    }
}
