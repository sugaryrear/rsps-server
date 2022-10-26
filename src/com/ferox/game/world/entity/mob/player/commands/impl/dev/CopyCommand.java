package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.combat.weapon.WeaponInterfaces;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.util.Utils;

import java.util.Optional;

/**
 * @author PVE
 * @Since september 13, 2020
 */
public class CopyCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        final String player2 = Utils.formatText(command.substring(parts[0].length() + 1));

        Optional<Player> otherp = World.getWorld().getPlayerByName(player2);
        if (otherp.isPresent()) {
            Player other = otherp.get();
            player.getEquipment().setItems(other.getEquipment().getItems().clone());
            player.inventory().setItems(other.inventory().getItems().clone());
            player.inventory().refresh();
            player.getEquipment().refresh();
            WeaponInterfaces.updateWeaponInterface(player);
            player.message("Copied %s's inventory and equipment.", other.getUsername());
        } else {
            player.message("cannot find player with name '"+player2+"'");
        }
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }
}
