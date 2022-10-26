package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.game.content.clan.ClanManager;
import com.ferox.game.content.duel.Dueling;
import com.ferox.game.content.gambling.GambleState;
import com.ferox.game.content.gambling.GamblingArea;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.CommandManager;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This packet listener manages commands a player uses by using the command
 * console prompted by using the "`" char.
 *
 * @author Gabriel Hannason
 */
public class CommandPacketListener implements PacketListener {

    private static final Logger logger = LogManager.getLogger(CommandPacketListener.class);

    //Ken comment: For some reason, getLogger with a String parameter uses reflection but it doesn't cause an UnsupportedOperationException in Java 11 as long as the getLogger parameter isn't empty. In the future, may need to re-write this so it uses the class name but there isn't a getLogger that takes both logger name (i.e. PrivateMessageLogs) and class name (i.e. PlayerRelationPacketListener). Right now the only classes using the string are PlayerRelationPacketListener and ChatPacketListener and CommandPacketListener. The fix for this if this ever became a problem, in theory, would be LogManager.getLogger(CommandPacketListener.class);
    //private static final Logger commandLogs = LogManager.getLogger(CommandPacketListener.class);
    //ken comment, never-mind the UnsupportedOperationException Java 11 comment above, just use "Multi-Release: true" in MANIFEST.MF for the server jar manifest so log4j2 uses the proper Java 9+ API for Java 11 instead of the older Java 8 API.. LogManager.getLogger(ClassName.class) is not required anywhere if using multi-release jar properly. Probably will remove these two or three comments some time in the future.
    private static final Logger commandLogs = LogManager.getLogger("CommandLogs");
    private static final Level COMMAND;
    static {
        COMMAND = Level.getLevel("COMMAND");
    }

    public static final int OP_CODE = 103;

    @Override
    public void handleMessage(Player player, Packet packet) {
        player.afkTimer.reset();
        String command = packet.readString();
        String[] parts = command.split(" ");
        if (parts.length == 0) // doing ::  with some spaces lol
            return;
        parts[0] = parts[0].toLowerCase();
        commandLogs.log(COMMAND, "Command received: "+command);

        if (player.dead() && !player.isInsideRaids()) {
            return;
        }

        boolean newAccount = player.getAttribOr(AttributeKey.NEW_ACCOUNT, false);
        
        if (newAccount) {
            player.message("You have to select your game mode before you can continue.");
            return;
        }
        
        if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
            player.getBankPin().openIfNot();
            return;
        }

        if(player.askForAccountPin()) {
            player.sendAccountPinMessage();
            return;
        }

        if (player.locked() && !player.getPlayerRights().isDeveloperOrGreater(player)) { // For any player type
            player.message("You cannot do this right now.");
            return;
        }

        if(Dueling.in_duel(player) && !player.getPlayerRights().isStaffMember(player)) { // Only staff can use commands in duel arena
            player.message("You cannot do this right now.");
            return;
        }

        if(player.getGamblingSession().matchActive() || player.getGamblingSession().state() == GambleState.PLACING_BET) {
            player.message("You cannot do this right now.");
            return;
        }

        if (player.jailed() && !player.getPlayerRights().isStaffMember(player)) { // Only staff can use commands in jail
            player.message("You cannot do this right now.");
            return;
        }

        var in_tournament = player.inActiveTournament() || player.isInTournamentLobby();
        if(in_tournament && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            if(!command.startsWith("yell") && !command.startsWith("exit")) {
                player.message("You cannot do this right now.");
                return;
            }
        }

        if (command.startsWith("/") && command.length() >= 1) {
            ClanManager.message(player, command.substring(1, command.length()));
            return;
        }
        if (command.contains("\r") || command.contains("\n")) {
            return;
        }
        CommandManager.attempt(player, command, parts);
    }
}
