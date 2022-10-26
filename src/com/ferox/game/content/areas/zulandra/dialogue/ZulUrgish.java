package com.ferox.game.content.areas.zulandra.dialogue;

import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.dialogue.Expression;
import com.ferox.util.NpcIdentifiers;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 27, 2020
 */
public class ZulUrgish extends Dialogue {

    @Override
    protected void start(Object... parameters) {
        send(DialogueType.NPC_STATEMENT, NpcIdentifiers.ZULURGISH, Expression.HAPPY, "Human agreed to sacrifice himself to might Zulrah!", "We all most grateful.");
        setPhase(0);
    }

    @Override
    protected void next() {
        if(getPhase() == 0) {
            stop();
        }
    }
}
