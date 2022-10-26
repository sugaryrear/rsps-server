package com.ferox.game.content.areas.alkharid.dialogue;

import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.dialogue.Expression;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.interaction.PacketInteraction;

import java.util.Arrays;
import java.util.List;

import static com.ferox.util.NpcIdentifiers.CAMEL;
import static com.ferox.util.NpcIdentifiers.OLLIE_THE_CAMEL;

/**
 * @author Patrick van Elderen | April, 14, 2021, 18:20
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class Camel extends PacketInteraction {

    private final static List<Integer> CAMELS = Arrays.asList(OLLIE_THE_CAMEL, CAMEL);

    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        for(int camel : CAMELS) {
            if(npc.id() == camel) {
                player.getDialogueManager().start(new Dialogue() {
                    @Override
                    protected void start(Object... parameters) {
                        send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Ask the camel about its dung.", "Say something unpleasant.", "Neither - I'm a polite person.");
                        setPhase(0);
                    }

                    @Override
                    protected void next() {
                        if(isPhase(1)) {
                            send(DialogueType.STATEMENT, "The camel didn't seem to appreciate that question.");
                            setPhase(2);
                        } else if(isPhase(2)) {
                            stop();
                        }
                    }

                    @Override
                    protected void select(int option) {
                        if(isPhase(0)) {
                            if(option == 1) {
                                send(DialogueType.PLAYER_STATEMENT, Expression.NODDING_THREE, "I'm sorry to bother you, but could", "you spare a little dung?");
                                setPhase(1);
                            } else if(option == 2) {
                                send(DialogueType.PLAYER_STATEMENT, Expression.NODDING_ONE, "I wonder if that camel has flees..");
                                player.message("The camel spits at you, and you jump back hurriedly.");
                                setPhase(2);
                            } else if(option == 3) {
                                player.message("You decide not to be rude.");
                                stop();
                            }
                        }
                    }
                });
                return true;
            }
        }
        return false;
    }
}
