package com.ferox.game.world.entity.mob.npc.droptables.impl.raids;

import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.npc.droptables.Droptable;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.util.Utils;

import static com.ferox.util.ItemIdentifiers.XERICS_AID_4;

public class IceDemonDroptable implements Droptable {
    @Override
    public void reward(Npc npc, Player killer) {
        var party = killer.raidsParty;

        if (party != null) {
            var currentKills = party.getKills();
            party.setKills(currentKills + 1);
            //
            for (Player member : party.getMembers()) {
                if (member != null && member.isInsideRaids()) {
                    int chance = Utils.random(10);
                    if(chance < 2)
                    drop(npc, member, new Item(20996, 1));//overload

                    else if(chance >2 && chance < 6)
                        drop(npc, member, new Item(20976, 1));//xerics aid

                    else if(chance >=6)
                        drop(npc, member, new Item(20972, 1));//prayer enhance

                }
            }
            //Progress to the next stage
            if (party.getKills() > 0) {
                party.setRaidStage(6);
                party.teamMessage("<col=ef20ff>You may now progress to the next room!");
                party.setKills(0);//Reset kills back to 0
            }
        }
    }
}
