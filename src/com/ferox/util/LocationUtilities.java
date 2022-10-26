package com.ferox.util;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.position.Tile;

import java.util.ArrayList;

/**
 * @author Simplemind
 */
public class LocationUtilities {

    public static ArrayList<Npc> copyNpcSpawnsFrom(World world, Tile from, int radius) {
        ArrayList<Npc> copy = new ArrayList<>();
        world.getNpcs().forEach(npc -> {
            if (from.area(radius).contains(npc.spawnTile())) {
                copy.add(npc);
            }
        });

        return copy;
    }

}
