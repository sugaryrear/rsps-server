package com.ferox.game.content.seasonal_events.halloween;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.dialogue.Expression;
import com.ferox.game.world.items.Item;
import com.ferox.util.Color;
import com.ferox.util.Utils;

import static com.ferox.util.CustomItemIdentifiers.HWEEN_MYSTERY_BOX;
import static com.ferox.util.CustomItemIdentifiers.HWEEN_TOKENS;
import static com.ferox.util.ItemIdentifiers.PINK_SWEETS;
import static com.ferox.util.ItemIdentifiers.PUMPKIN_LANTERN;
import static com.ferox.util.NpcIdentifiers.*;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 12, 2021
 */
public class TeacherAndPupilD extends Dialogue {

    @Override
    protected void start(Object... parameters) {
        if (player.getAttribOr(AttributeKey.FINISHED_HALLOWEEN_TEACHER_DIALOGUE, false)) {
            send(DialogueType.NPC_STATEMENT, TEACHER, Expression.HAPPY, "Have you found our candy yet?");
            setPhase(10);
            return;
        }
        send(DialogueType.NPC_STATEMENT, SCHOOLGIRL, Expression.SAD, "*sniffles*");
        setPhase(0);
    }

    @Override
    protected void next() {
        if (isPhase(0)) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Why are you crying?", "Teacher why is this girl crying?");
            setPhase(1);
        } else if (isPhase(3)) {
            send(DialogueType.PLAYER_STATEMENT, Expression.DEFAULT, "What can I do to help?");
            setPhase(4);
        } else if (isPhase(4)) {
            send(DialogueType.NPC_STATEMENT, TEACHER, Expression.CALM_TALK, "Can you go around the map and see if she is telling", " the truth?");
            setPhase(5);
        } else if (isPhase(5)) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Sure.", "No thanks.");
            setPhase(6);
        } else if (isPhase(10)) {
            if (player.inventory().contains(PINK_SWEETS)) {
                send(DialogueType.PLAYER_STATEMENT, Expression.DEFAULT, "I have found some candies.");
                setPhase(12);
            } else {
                send(DialogueType.PLAYER_STATEMENT, Expression.DEFAULT, "Sadly not yet, I will come back when I found some!");
                setPhase(13);
            }
        } else if (isPhase(12)) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Give the teacher your candies.", "Keep them yourself.");
            setPhase(12);
        } else if (isPhase(13)) {
            stop();
        }
    }

    @Override
    protected void select(int option) {
        if (isPhase(1)) {
            if (option == 1) {
                send(DialogueType.NPC_STATEMENT, SCHOOLGIRL, Expression.SAD, "These big scary men in black costumes and white", "scary masks took all my candy.");
                setPhase(13);
            }
            if (option == 2) {
                send(DialogueType.NPC_STATEMENT, TEACHER, Expression.CALM_TALK, "She said that there are men in black robes going", "around stealing kids candy. I'm not sure if I believe her.");
                setPhase(3);
            }
        }
        if (isPhase(6)) {
            if (option == 1) {
                send(DialogueType.ITEM_STATEMENT, new Item(PUMPKIN_LANTERN), "", "The teacher hands you a Pumpkin Lantern to collect", "candies.");
                setPhase(13);
                player.inventory().addOrDrop(new Item(PUMPKIN_LANTERN));
                player.putAttrib(AttributeKey.FINISHED_HALLOWEEN_TEACHER_DIALOGUE,true);
            }
            if (option == 2) {
                stop();
            }
        }
        if (isPhase(12)) {
            if (option == 1) {
                int count = player.inventory().count(PINK_SWEETS);
                player.inventory().remove(PINK_SWEETS, count);
                var candies = player.<Integer>getAttribOr(AttributeKey.CANDIES_TRADED,0)+ count;
                player.putAttrib(AttributeKey.CANDIES_TRADED, candies);
                player.message(Color.PURPLE.wrap("You have now given the teacher "+ Utils.formatNumber(candies)+" candies."));
                player.inventory().addOrBank(new Item(HWEEN_MYSTERY_BOX, count));
                String plural = count > 1 ? "boxes" : "box";
                player.message(Color.PURPLE.wrap("The teacher gave you "+ Utils.formatNumber(count)+" H'ween mystery "+plural+" in return."));
                stop();
            }
            if (option == 2) {
                stop();
            }
        }
    }
}
