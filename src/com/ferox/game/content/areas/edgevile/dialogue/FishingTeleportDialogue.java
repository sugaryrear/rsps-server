package com.ferox.game.content.areas.edgevile.dialogue;

import com.ferox.game.content.teleport.TeleportType;
import com.ferox.game.content.teleport.Teleports;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.position.Tile;

public class FishingTeleportDialogue extends Dialogue {
    @Override
    protected void start(Object... parameters) {
        send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Catherby Fishing Area", "Fishing Guild", "Nevermind");
        setPhase(0);
    }

    @Override
    protected void select(int option) {
        if (option == 1) {
            if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                stop();
                return;
            }
            Teleports.minigameTeleport(player, new Tile(2835,3433));
        } else if (option == 2) {
            if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                stop();
                return;
            }
            Teleports.minigameTeleport(player, new Tile(2594, 3415));
        } else if (option == 3) {
            stop();
        }
    }
}
