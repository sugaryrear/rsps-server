package com.ferox.game.world.entity.mob.npc.impl;

import com.ferox.game.world.entity.Entity;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.position.Tile;
import com.ferox.util.NpcIdentifiers;

/**
 * @author Patrick van Elderen | February, 14, 2021, 12:10
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class MaxHitDummyNpc extends Npc {

    public MaxHitDummyNpc(int id, Tile tile) {
        super(NpcIdentifiers.COMBAT_DUMMY, tile);
    }

    @Override
    public Mob face(Tile positionToFace) {
        return this;
    }

    @Override
    public Mob setEntityInteraction(Entity entity) {
        return this;
    }

    @Override
    public Npc setHitpoints(int hitpoints) {
        return this;
    }

    @Override
    public void sequence() {
        getCombat().process();
    }

}
