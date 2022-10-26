package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.game.content.EffectTimer;
import com.ferox.game.content.duel.DuelRule;
import com.ferox.game.content.mechanics.Transmogrify;
import com.ferox.game.task.Task;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.skull.SkullType;
import com.ferox.game.world.entity.combat.skull.Skulling;
import com.ferox.game.world.entity.dialogue.DialogueManager;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.ground.GroundItem;
import com.ferox.game.world.items.ground.GroundItemHandler;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.net.packet.ClientToServerPackets;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;
import com.ferox.util.Debugs;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.timers.TimerKey;
import io.netty.buffer.Unpooled;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This packet listener is called when a player has clicked on either the
 * mini-map or the actual game map to move around.
 *
 * @author Gabriel Hannason
 */
public class MovementPacketListener implements PacketListener {

    public static void main(String[] args) {
        final Packet packet = new Packet(-1, Unpooled.copiedBuffer(new byte[]{(byte) 0,(byte) 28,(byte) 5,(byte) -1,(byte) -1,(byte) -128,(byte) 14,(byte) 0}));
        int size = packet.getSize();
        boolean shiftTeleport = packet.readByte() == 1; //We already send shift teleport as a command, but lets read the byte anyway.
        int path1 = packet.readByte();
        int path2 = packet.readByte();
        System.out.println("was "+ Arrays.toString(packet.getBuffer().array())+" -> size "+size+" shift tp "+shiftTeleport+" path1 "+path1+" path2 "+path2);
    }
    public static int steps;
public static Tile[] tiles;
    @Override
    public void handleMessage(Player player, Packet packet) {
        int size = packet.getSize();
        player.afkTimer.reset();

        if (player.locked() || player.dead()) {
            return;
        }

        if(player.askForAccountPin()) {
            player.sendAccountPinMessage();
            return;
        }

        boolean newAccount = player.getAttribOr(AttributeKey.NEW_ACCOUNT, false);

        if (newAccount) {
            player.message("You have to select your game mode before you can continue.");
            return;
        }
        CombatFactory.unfreezeWhenOutOfRange(player);

        if (!checkReqs(player, packet.getOpcode())) {
            return;
        }

        // Stop our distanced action task because we reset the walking queue by walking
        player.stopDistancedTask();

        /* Clear non walkable actions */
        player.stopActions(false);

        if (packet.getOpcode() == ClientToServerPackets.MINIMAP_MOVEMENT_OPCODE || packet.getOpcode() == ClientToServerPackets.GAME_MOVEMENT_OPCODE) {
            player.getCombat().reset();//Reset combat when moving
            player.getCombat().setCastSpell(null);
        }

        var in_tournament = player.inActiveTournament() || player.isInTournamentLobby();

        if(player.looks().trans() > 0 && !in_tournament) {
            Transmogrify.hardReset(player);
        }

        // Close dialogues
        player.getInterfaceManager().closeDialogue();
        player.getRunePouch().close();

        if (player.loopTask != null && player.loopTask.isRunning()) {
            player.loopTask.stop();
        }

        //Haha some friends thought they were smart, wield ammy of avarice type ::unskull and go in wild.
        if(WildernessArea.inWilderness(player.tile())) {
            if (player.getEquipment().contains(ItemIdentifiers.AMULET_OF_AVARICE)) {
                Skulling.assignSkullState(player, SkullType.WHITE_SKULL);
            }
        }

        if (!WildernessArea.inWilderness(player.tile())) {

            if (player.getTimers().has(TimerKey.TELEBLOCK) || player.getTimers().has(TimerKey.SPECIAL_TELEBLOCK)) {
                player.getTimers().cancel(TimerKey.SPECIAL_TELEBLOCK);
                player.getTimers().cancel(TimerKey.TELEBLOCK);
                player.getTimers().cancel(TimerKey.TELEBLOCK_IMMUNITY);
                player.message("The teleport block fades as you leave the wilderness...");
                player.getTimers().cancel(TimerKey.BLOCK_SPEC_AND_TELE);
                player.getPacketSender().sendEffectTimer(0, EffectTimer.TELEBLOCK);
            }

            int wildernessStreak = player.getAttribOr(AttributeKey.WILDERNESS_KILLSTREAK, 0);
            if (wildernessStreak >= 1) {
                player.message("[<col=ca0d0d>Streak</col>] Your wilderness streak ends at <col=ca0d0d>" + wildernessStreak + "</col> as you exit the wilderness.");
                player.clearAttrib(AttributeKey.WILDERNESS_KILLSTREAK);
            }
        }

         steps = (size - 5) / 2;
        if (steps < 0) {
            return;
        }
        boolean shiftTeleport = packet.readByte() == 1; //We already send shift teleport as a command, but lets read the byte anyway.
        final int firstStepX = packet.readLEShortA();
        final int[][] path = new int[steps][2];
        for (int i = 0; i < steps; i++) {
            path[i][0] = packet.readByte();
            path[i][1] = packet.readByte();
        }
        final int firstStepY = packet.readLEShort();

     tiles = new Tile[steps + 1];

        tiles[0] = new Tile(firstStepX, firstStepY, player.tile().getLevel());
        for (int i = 0; i < steps; i++) {
            tiles[i + 1] = new Tile(path[i][0] + firstStepX, path[i][1] + firstStepY, player.tile().getLevel());
        }

        // Get the ending position..
        Tile end = tiles[tiles.length - 1];

        if (Debugs.MOB_STEPS.enabled) {
            ArrayList<GroundItem> markers = new ArrayList<>(tiles.length);
            for (Tile step : tiles) {
                //if(debug)
                //System.out.println("[adding steps to walk que] "+step.getX()+" "+step.getY());
                //getMovementQueue().walkTo(new Position(step.getX(), step.getY()));
                GroundItem marker = new GroundItem(new Item(ItemIdentifiers.BRONZE_ARROW, 1), new Tile(step.getX(), step.getY()), null);
                GroundItemHandler.createGroundItem(marker);
                markers.add(marker);
            }
            Task.runOnceTask(10, c -> {
                markers.forEach(GroundItemHandler::sendRemoveGroundItem);
            });
        }

        // Validate positions.
        if (player.tile().distance(end) >= 64) {
            return;
        }

        player.smartPathTo(new Tile(end.x, end.y));
//player.message(steps+"");
        // very important to put this AFTER movement.clear is called otherwise attrib is overwritten
        player.putAttrib(AttributeKey.MOVEMENT_PACKET_STEPS, new ArrayDeque(Arrays.asList(tiles)));

    }

    private boolean checkReqs(Player player, int opcode) {
        if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
            player.getBankPin().openIfNot();
            player.debugMessage("MovementPacket checkReqs need bank pin.");
            return false;
        }

        if(player.askForAccountPin()) {
            player.sendAccountPinMessage();
            return false;
        }

        /*
         * JAK/ SHADOWRS:
         * DO NOT put frozen/stunned checks here. that should go in the movement handler code. you want to allow the REQUEST
         * to walk to always come through, then decide if you can/cant later.
         */

        if (player.getTrading().getButtonDelay().active() || player.getDueling().getButtonDelay().active() || player.getGamblingSession().getButtonDelay().active()) {
            player.message("You cannot do that right now.");
            return false;
        }

        // Duel, disabled movement?
        if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_MOVEMENT.ordinal()]) {
            if (opcode != ClientToServerPackets.COMMAND_MOVEMENT_OPCODE) {
                DialogueManager.sendStatement(player, "Movement has been disabled in this duel!");
            }
            return false;
        }

        if (player.isNeedsPlacement() || player.getMovementQueue().isMovementBlocked()) {
            player.debugMessage("MovementPacket checkReqs needs placement and movement blocked: "+player.getMovementQueue().isMovementBlocked());
            return false;
        }

        return true;
    }
}
