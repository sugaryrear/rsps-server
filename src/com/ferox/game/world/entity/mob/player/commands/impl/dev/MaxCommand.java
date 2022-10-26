package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.entity.mob.player.commands.Command;

/**
 * @author PVE
 * @Since september 13, 2020
 */
public class MaxCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        // Boost stats instead of having to waste time potting when testing combat functions.
//        player.skills().setLevel(Skills.ATTACK, 118);
//        player.skills().setLevel(Skills.STRENGTH, 118);
//        player.skills().setLevel(Skills.DEFENCE, 118);
//        player.skills().setLevel(Skills.RANGED, 112);
//        player.skills().setLevel(Skills.MAGIC, 104);
                        for (int skill = 0; skill < 22; skill++) {
                    player.skills().setXp(skill, Skills.levelToXp(99));
                    player.skills().update();
                    player.skills().recalculateCombat();
                }
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }
}
