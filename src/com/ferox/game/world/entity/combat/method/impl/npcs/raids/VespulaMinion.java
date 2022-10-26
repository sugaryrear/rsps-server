package com.ferox.game.world.entity.combat.method.impl.npcs.raids;

import com.ferox.game.content.raids.party.Party;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.util.Utils;

import java.util.List;

import static com.ferox.util.NpcIdentifiers.VESPINE_SOLDIER;

public class VespulaMinion extends Npc {

    public VespulaMinion(Npc vetion, Mob target) {
        super(VESPINE_SOLDIER, vetion.tile());

        this.setTile(target.tile().transform(Utils.random(3), -2));
        this.walkRadius(8);
        this.respawns(false);
        this.getCombat().attack(target);
    }

    public static void death(Npc npc, Player player) {
        Party party = player.raidsParty;
        if(party == null) {
            return;
        }

            List<Npc> minList = party.getVespulaAdds();
            if (minList != null) {
                minList.remove(npc);
                // All minions dead? Enable damage on vetion again
                if (minList.size() == 0) {
                    party.vespulaAddsSpawned(false);
                }
            }

    }
}
