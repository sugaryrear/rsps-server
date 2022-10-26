package com.ferox.game.content.items;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;

import static com.ferox.util.CustomItemIdentifiers.GAMBLER_SCROLL;

public class GamblerScroll extends PacketInteraction {

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if (option == 1) {
            if (item.getId() == GAMBLER_SCROLL) {
                if (player.<Boolean>getAttribOr(AttributeKey.GAMBLER, false)) {
                    player.message(Color.RED.wrap("You're already an gambler!"));
                    return true;
                }

                player.optionsTitled("Would you like to become a gambler?", "Yes", "No", () -> {
                    if (!player.inventory().contains(GAMBLER_SCROLL)) {
                        return;
                    }
                    player.inventory().remove(GAMBLER_SCROLL);
                    player.putAttrib(AttributeKey.GAMBLER, true);
                    player.message(Color.BLUE.wrap("You're now a gambler!"));
                });
                return true;
            }
        }
        return false;
    }
}
