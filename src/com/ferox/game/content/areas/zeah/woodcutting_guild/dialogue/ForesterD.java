package com.ferox.game.content.areas.zeah.woodcutting_guild.dialogue;

import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.dialogue.Expression;
import com.ferox.util.NpcIdentifiers;
import com.ferox.util.Utils;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 21, 2020
 */
public class ForesterD extends Dialogue {

    @Override
    protected void start(Object... parameters) {
        int roll = Utils.random(2);

        if(roll == 1) {
            send(DialogueType.NPC_STATEMENT, NpcIdentifiers.FORESTER_7238, Expression.NODDING_THREE, "Nice weather we're having today.");
        } else {
            send(DialogueType.NPC_STATEMENT, NpcIdentifiers.FORESTER_7238, Expression.HAPPY, "It's so peaceful here, don't you agree?");
        }
        setPhase(0);
    }

    @Override
    protected void next() {
        if (getPhase() == 0) {
            stop();
        }
    }

}
