package com.ferox.game.world.entity.combat.method.impl.npcs.godwars.zaros;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.util.Utils;

import static com.ferox.util.NpcIdentifiers.FUMUS;
import static com.ferox.util.NpcIdentifiers.UMBRA;

public class Umbra extends Npc {

    public Umbra(Npc nex, Mob target) {
        super(UMBRA, nex.tile());
        this.putAttrib(AttributeKey.BOSS_OWNER, nex);
        this.setTile(nex.tile().transform(Utils.random(3), -1));
        this.walkRadius(8);
        this.respawns(false);
        this.getCombat().attack(target);
    }

    public static void death(Npc npc) {
        Npc nex = npc.getAttribOr(AttributeKey.BOSS_OWNER, null);
        if (nex != null) {
            nex.putAttrib(AttributeKey.UMBRA_DEAD, true);
            nex.putAttrib(AttributeKey.NEX_UMBRA_SPAWNED, false);
        }
    }
}
