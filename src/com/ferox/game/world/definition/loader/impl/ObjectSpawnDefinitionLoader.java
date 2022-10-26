package com.ferox.game.world.definition.loader.impl;

import com.ferox.GameServer;
import com.ferox.game.world.definition.ObjectSpawnDefinition;
import com.ferox.game.world.definition.loader.DefinitionLoader;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.region.RegionManager;
import com.google.gson.Gson;

import java.io.FileReader;

public class ObjectSpawnDefinitionLoader extends DefinitionLoader {

    @Override
    public void load() throws Exception {
        try (FileReader reader = new FileReader(file())) {
            ObjectSpawnDefinition[] defs = new Gson().fromJson(reader, ObjectSpawnDefinition[].class);
           for (ObjectSpawnDefinition def : defs) {
//                if (!def.isEnabled())
//                    continue;
//                if (!GameServer.properties().pvpMode && def.PVPWorldExclusive) {
//                    //Skip PVP objects in eco world.
//                    continue;
//                }
//                if (GameServer.properties().pvpMode && def.economyExclusive) {
//                    //Skip eco objects in PVP world.
//                    continue;
//                }
                ObjectManager.addObj(new GameObject(def.getId(), def.getTile(), def.getType(), def.getFace()));


           }
         //  System.out.println(defs.length);
        }
    }

    @Override
    public String file() {
        return GameServer.properties().definitionsDirectory + "object_spawns.json";
    }
}
