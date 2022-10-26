package com.ferox.game.content.minigames.impl.fight_caves;

import com.ferox.game.content.mechanics.referrals.ReferralD;
import com.ferox.game.content.minigames.MinigameManager;
import com.ferox.game.content.new_players.Tutorial;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.items.Item;

public class FightCaveDialogue extends Dialogue {

    @Override
    protected void start(Object... parameters) {
        send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Start at wave 1", "Go straight to Jad (@red@100,000@bla@) BM");
        setPhase(0);
    }

    @Override
    protected void select(int option) {
        if (isPhase(0)) {
            if (option == 1) {
                stop();
                MinigameManager.playMinigame(player, new FightCavesMinigame(1));

                return;

            } else if (option == 2) {
                stop();
                if (!player.inventory().contains(new Item(13307, 100_000))) {
                    player.message("You need 100,000 @blu@blood money@bla@ to go straight to Jad in the Fight Caves.");
                        return;
                }
                player.inventory().remove(new Item(13307, 100_000), true);

                MinigameManager.playMinigame(player, new FightCavesMinigame(63));
                return;


            }
        }
    }
}

