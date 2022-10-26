package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

public class ClearBankCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getDialogueManager().start(new EmptyBankDialogue());
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    private final static class EmptyBankDialogue extends Dialogue {

        @Override
        protected void start(Object... parameters) {
            send(DialogueType.STATEMENT, "Are you sure you wish to empty your bank?", "This cannot be undone.");
            setPhase(0);
        }

        @Override
        public void next() {
            if (isPhase(0)) {
                send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes", "No");
                setPhase(1);
            }
        }

        @Override
        public void select(int option) {
            if (isPhase(1)) {
                setPhase(2);
                switch (option) {
                    case 1 -> {
                        player.stopActions(false);
                        player.getBank().clear();
                        player.message("Your bank has been cleared.");
                        stop();
                    }
                    case 2 -> stop();
                }
            }
        }
    }

}
