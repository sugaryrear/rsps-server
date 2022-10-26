package com.ferox.game.content.syntax;

import com.ferox.game.world.entity.mob.player.Player;
import org.jetbrains.annotations.NotNull;

public interface EnterSyntax {

    default void handleSyntax(Player player, @NotNull String input) {

    }

   default void handleSyntax(Player player, long input) {

   }

   default void close(Player player) {
        player.getInterfaceManager().closeDialogue();
   }
}
