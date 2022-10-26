package com.ferox.game.world.entity.mob.player.commands.impl.staff.admin;

import com.ferox.game.content.bank_pin.BankPinModification;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.entity.mob.player.save.PlayerSave;
import com.ferox.util.Utils;

import java.time.LocalDateTime;
import java.util.Optional;

public class DeleteBankPinCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        LocalDateTime activationDate = LocalDateTime.now().plusDays(0);
        final String player2 = Utils.formatText(command.substring(parts[0].length() + 1));
        Optional<Player> plr = World.getWorld().getPlayerByName(player2);
        if (!PlayerSave.playerExists(player2)) {
            player.message("Player " + player2 + " does not exist.");
            return;
        }
        if (plr.isPresent()) {
            if (plr.get().getBankPin().setPendingMod(null)) {
                player.message("You have deleted the pending bank pin modification of " + player2 + ".");
            }
            if (plr.get().getBankPin().setPendingMod(new BankPinModification("No bank pin.", 0, 0, activationDate, "Delete PIN"))) {
                player.message("You have deleted the bank PIN of " + player2 + ".");
                plr.get().getBankPin().activateMod();
                plr.get().message("A staff member has deleted your bank PIN.");
            }
        } else {
            player.message("The player " + player2 + " is not online.");
        }

    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isAdminOrGreater(player));
    }

}
