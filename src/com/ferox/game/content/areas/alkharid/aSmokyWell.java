package com.ferox.game.content.areas.alkharid;

import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.ObjectIdentifiers;

import static com.ferox.util.ObjectIdentifiers.ROPE_6439;

public class aSmokyWell extends PacketInteraction {
    public void wellDialog(Player player){
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.STATEMENT, "Inside the smoke dungeon you will take damage unless", "you're wearing a @blu@Face mask");

                setPhase(0);
            }

            @Override
            protected void next() {
                if (isPhase(0)) {
                    send(DialogueType.OPTION, "Climb down well?", "Yes", "No");
                    setPhase(1);
                }
            }

            @Override
            protected void select(int option) {
                if (isPhase(1)) {
                    if (option == 1) {

                        player.teleport(new Tile(2404,9416));
                        SmokeDungeon.startSmokeDungeonTask(player);
                        stop();
                    } else if (option == 2) {
                        stop();
                    }
                }
            }
        });
    }
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            if(obj.getId() == ObjectIdentifiers.SMOKEY_WELL) {
                wellDialog(player);
                return true;
            }

            if(obj.getId() == ROPE_6439) {//rope back up
                player.teleport(new Tile(3310,2961));
                return true;
            }
        }
        if (option == 2) {
        }
        return false;
    }
}
