package com.ferox.game.content.duel;

import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.util.timers.TimerKey;

public class DuelForfeitDialogue extends Dialogue {

    @Override
    protected void start(Object... parameters) {
        send(DialogueType.STATEMENT, "Are you sure you wish to forfeit?");
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
            switch (option) {
                case 1 -> {
                    // We could be in the middle of counting down. Better stop.
                    player.getTimers().cancel(TimerKey.STAKE_COUNTDOWN);
                    player.getDueling().getOpponent().getTimers().cancel(TimerKey.STAKE_COUNTDOWN);
                    player.getDueling().onDeath();
                    stop();
                }
                case 2 -> stop();
            }
        }
    }
}


