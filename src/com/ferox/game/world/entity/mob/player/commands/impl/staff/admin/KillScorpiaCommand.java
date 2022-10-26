package com.ferox.game.world.entity.mob.player.commands.impl.staff.admin;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

import static com.ferox.util.NpcIdentifiers.SCORPIA;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 19, 2021
 */
public class KillScorpiaCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        for(Npc npc : World.getWorld().getNpcs()) {
            if(npc == null) continue;

            if(npc.id() == SCORPIA) {
                npc.hit(player, npc.maxHp());
                player.message("Scorpia found and killed.");
                break;//Found scorpia break the loop
            }
        }
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isAdminOrGreater(player);
    }
}
