package com.ferox.game.world.entity.combat.method.impl.npcs.godwars.zaros;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.util.Utils;

import static com.ferox.util.NpcIdentifiers.GLACIES;
import static com.ferox.util.NpcIdentifiers.UMBRA;

public class Glacies extends Npc {

    public Glacies(Npc nex, Mob target) {
        super(GLACIES, nex.tile());
        this.putAttrib(AttributeKey.BOSS_OWNER, nex);
        this.setTile(nex.tile().transform(Utils.random(3), -1));
        this.walkRadius(8);
        this.respawns(false);
        this.getCombat().attack(target);
    }

    public static void death(Npc npc) {
        Npc nex = npc.getAttribOr(AttributeKey.BOSS_OWNER, null);
        if (nex != null) {
            nex.putAttrib(AttributeKey.GLACIES_DEAD, true);
            nex.putAttrib(AttributeKey.NEX_GLACIES_SPAWNED, false);
        }
    }
}
