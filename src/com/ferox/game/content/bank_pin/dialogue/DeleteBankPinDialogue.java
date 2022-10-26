package com.ferox.game.content.bank_pin.dialogue;

import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.dialogue.Expression;

/**
 * @author lare96 <http://github.com/lare96>
 */
public class DeleteBankPinDialogue extends Dialogue {
    private final int npcId;

    public DeleteBankPinDialogue(int npcId) {
        this.npcId = npcId;
    }


    @Override
    protected void start(Object... parameters) {
        send(DialogueType.NPC_STATEMENT, npcId, Expression.DEFAULT, "Are you absolutely sure you wish to delete your bank pin?");
        setPhase(0);
    }

    @Override
    protected void next() {
        switch (getPhase()) {
            case 0:
                send(DialogueType.OPTION, "Select an option.",
                    "Yes",
                    "No");
                break;
        }
    }

    @Override
    protected void select(int option) {
        switch (getPhase()) {
            case 0:
                if (option == 1) {
                    player.getBankPin().deletePin();
                }
                stop();
                break;
        }
    }
}
