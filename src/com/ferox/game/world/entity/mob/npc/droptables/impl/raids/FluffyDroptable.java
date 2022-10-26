package com.ferox.game.world.entity.mob.npc.droptables.impl.raids;

import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.npc.droptables.Droptable;
import com.ferox.game.world.entity.mob.player.Player;

/**
 * @author Patrick van Elderen | May, 12, 2021, 19:35
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class FluffyDroptable implements Droptable {

    @Override
    public void reward(Npc npc, Player killer) {
        var party = killer.raidsParty;

        if (party != null) {
            var currentKills = party.getKills();
            party.setKills(currentKills + 1);
            party.teamMessage("<col=ef20ff>Fluffy has been defeated!");
            //System.out.println(party.getKills());

            //Progress to the next stage
            if (party.getKills() == 1) {
                party.setRaidStage(4);
                party.teamMessage("<col=ef20ff>You may now progress to the next room!");
                party.setKills(0);//Reset kills back to 0
            }
        }
    }
}
