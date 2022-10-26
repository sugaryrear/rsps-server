package com.ferox.test.generic;

import com.ferox.game.world.position.Tile;

import java.util.Arrays;
import java.util.Set;

public class TileExpandBoundTest {

    public static void main(String[] args) {
        Tile tile = new Tile(100, 100);
        int ex = 2;
        Set<Tile> tiles = tile.expandedBounds(ex, null);
        System.out.println("expanded by "+ex+" results: "+tiles.size());
        System.out.println(Arrays.toString(tiles.stream().map(e -> e.x+","+e.y).toArray()));
    }
}
