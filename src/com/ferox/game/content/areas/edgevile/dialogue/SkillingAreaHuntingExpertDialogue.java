package com.ferox.game.content.areas.edgevile.dialogue;

import com.ferox.game.content.teleport.TeleportType;
import com.ferox.game.content.teleport.Teleports;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Color;

/**
 * @author Patrick van Elderen | December, 16, 2020, 15:20
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class SkillingAreaHuntingExpertDialogue extends Dialogue {

    @Override
    protected void start(Object... parameters) {
        send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Crimson swifts (levels 1-19)", "Tropical wagtails. (levels 19-53)", "Copper long tails and grey chinchompas (levels 9-53)", "Red chinchompas (levels 63-73", "Black chinchompa ("+ Color.RED.tag()+"Dangerous</col>) (level 73+)");
        setPhase(0);
    }

    @Override
    protected void select(int option) {
        if(isPhase(0)) {
            if(option == 1) {
                if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                    stop();
                    return;
                }

                Teleports.minigameTeleport(player, new Tile(2612,2932));
            } else if(option == 2) {
                if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                    stop();
                    return;
                }

                Teleports.minigameTeleport(player, new Tile(2528,2940));
            } else if(option == 3) {
                if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                    stop();
                    return;
                }

                Teleports.minigameTeleport(player, new Tile(2344,3591));
            } else if(option == 4) {
                if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                    stop();
                    return;
                }

                Teleports.minigameTeleport(player, new Tile(2554,2935));
            } else if(option == 5) {
                if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                    stop();
                    return;
                }

                Teleports.minigameTeleport(player, new Tile(3135,3788));
            }
        }
    }
}
