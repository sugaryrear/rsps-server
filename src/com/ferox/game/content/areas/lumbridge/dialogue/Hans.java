package com.ferox.game.content.areas.lumbridge.dialogue;

import com.ferox.game.content.mechanics.referrals.ReferralD;
import com.ferox.game.content.new_players.Tutorial;
import com.ferox.game.content.teleport.TeleportType;
import com.ferox.game.content.teleport.Teleports;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Color;

/**
 * @author PVE
 * @Since augustus 28, 2020
 */
public class Hans extends Dialogue {

    @Override
    protected void start(Object... parameters) {
        send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Replay Tutorial", "Referrals");
        setPhase(0);
    }

    @Override
    protected void select(int option) {
        if (isPhase(0)) {
            if (option == 1) {
                stop();
                player.getDialogueManager().start(new Tutorial());

                return;

            } else if (option == 2) {
                stop();
                player.getDialogueManager().start(new ReferralD());
                return;


            }
        }
    }
}
