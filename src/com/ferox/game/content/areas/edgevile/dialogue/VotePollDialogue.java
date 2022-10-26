package com.ferox.game.content.areas.edgevile.dialogue;

import com.ferox.GameServer;
import com.ferox.db.transactions.CollectVotes;
import com.ferox.db.transactions.UpdateDeathsDatabaseTransaction;
import com.ferox.db.transactions.UpdateKdrDatabaseTransaction;
import com.ferox.db.transactions.UpdateKillsDatabaseTransaction;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;

import static com.ferox.util.ItemIdentifiers.BLOOD_MONEY;

public class VotePollDialogue extends Dialogue {
    @Override
    protected void start(Object... parameters) {
        send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Claim votes", "Vote store");
        setPhase(0);
    }

    @Override
    protected void select(int option) {
        if(isPhase(0)) {
            if (option == 1) {
                CollectVotes.INSTANCE.collectVotes(player);

                stop();
            } else if (option == 2) {
                World.getWorld().shop(6).open(player);
            }
        }
        }
}
