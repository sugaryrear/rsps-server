package com.ferox.game.world.entity.mob.npc.droptables.impl.raids;


import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.npc.droptables.Droptable;
import com.ferox.game.world.entity.mob.player.Player;

public class VanguardsDroptable implements Droptable {
    @Override
    public void reward(Npc npc, Player killer) {
        var party = killer.raidsParty;

        if (party != null) {
            var currentKills = party.getKills();
            party.setKills(currentKills + 1);


            //Progress to the next stage
            if (party.getKills() == 3) {
                party.setRaidStage(5);
                party.teamMessage("<col=ef20ff>You may now progress to the next room!");
                party.setKills(0);//Reset kills back to 0
            }
        }
    }
}
