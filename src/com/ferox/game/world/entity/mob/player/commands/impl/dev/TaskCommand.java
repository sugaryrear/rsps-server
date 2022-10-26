package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

/**
 * @author Patrick van Elderen | November, 15, 2020, 15:36
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class TaskCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (parts.length != 2) {
            player.message("usage ::task pvptask/skillingtask/pvmtask/reset");
            return;
        }
        String cmd = parts[1];
        if(cmd.equalsIgnoreCase("pvptask")) {
            player.getTaskMasterManager().giveTask(true, false, false);
        } else if(cmd.equalsIgnoreCase("skillingtask")) {
            player.getTaskMasterManager().giveTask(false,true,false);
        } else if(cmd.equalsIgnoreCase("pvmtask")) {
            player.getTaskMasterManager().giveTask(false,false,true);
        } else if(cmd.equalsIgnoreCase("reset")) {
            player.getTaskMasterManager().resetTask();
        }
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
