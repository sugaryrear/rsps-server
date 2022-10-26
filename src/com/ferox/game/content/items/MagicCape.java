package com.ferox.game.content.items;

import com.ferox.game.content.DropsDisplay;
import com.ferox.game.content.mechanics.MagicalAltarDialogue;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;

import static com.ferox.util.ItemIdentifiers.*;

public class MagicCape {
    public static boolean onItemOption3(Player player, Item item) {
        if (item.getId() == MAGIC_CAPE || item.getId() == MAGIC_CAPET) {
            player.getDialogueManager().start(new MagicalAltarDialogue());
            return true;
        }
        return false;
    }
}
