package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.game.world.entity.combat.prayer.default_prayer.DefaultPrayers;
import com.ferox.game.world.entity.mob.Flag;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.entity.mob.player.commands.Command;

public class MasterCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        for (int skill = 0; skill < Skills.SKILL_COUNT; skill++) {
            player.skills().setXp(skill, Skills.levelToXp(99));
            player.skills().update();
            player.skills().recalculateCombat();
        }
        // Turn off prayers
        DefaultPrayers.closeAllPrayers(player);
        player.getUpdateFlag().flag(Flag.APPEARANCE);
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isDeveloperOrGreater(player);
    }

}
