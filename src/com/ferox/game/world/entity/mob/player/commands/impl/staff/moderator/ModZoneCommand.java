package com.ferox.game.world.entity.mob.player.commands.impl.staff.moderator;

import com.ferox.game.world.entity.masks.animations.Animation;
import com.ferox.game.world.entity.masks.animations.Priority;
import com.ferox.game.world.entity.masks.graphics.Graphic;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.position.Tile;
import com.ferox.util.chainedwork.Chain;

public class ModZoneCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.animate(new Animation(714, Priority.HIGH));
        player.performGraphic(new Graphic(308, 50));
        Chain.bound(player).name("ModZoneTeleportTask").runFn(2, () -> {
            player.teleport(new Tile(2525, 4776));
            player.animate(new Animation(715, Priority.HIGH));
        });

        player.message("Welcome to the mod zone!");
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isModeratorOrGreater(player));
    }

}
