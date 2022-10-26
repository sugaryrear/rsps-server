package com.ferox.game.content.items;

import com.ferox.game.content.DropsDisplay;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.npc.pets.Pet;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.position.Tile;

import static com.ferox.game.world.entity.dialogue.Expression.HAPPY;
import static com.ferox.util.ItemIdentifiers.RING_OF_WEALTH;
import static com.ferox.util.ItemIdentifiers.RING_OF_WEALTH_I;

/**
 * ring of wealth 'features' option
 */
public class RingOfWealth {


    public static boolean onItemOption3(Player player, Item item) {
        if (item.getId() == RING_OF_WEALTH_I) {
            displayOptions(player);
            return true;
        }
        return false;
    }

    public static void displayOptions(Player player) {

        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Choose an option", "Display NPC drops", "Display Kill log");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        DropsDisplay.start(player);
                        stop();
                    } else if(option == 2) {
                        player.getBossKillLog().openLog();
                        stop();
                    }
                }
            }
        });
    }
}
