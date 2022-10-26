package com.ferox.game.world.object.gates;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.MapObjects;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;

import java.util.function.Predicate;

public class Woodengates extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        // Opening gates
        if(obj.getId() == 1558 || obj.getId() == 1560) {
            if (obj.tile().equals(3080,3501) || obj.tile().equals(3079,3501)) {
                openEdgevilleGate();
                return true;
            }
        }

        if(obj.getId() == 1558 ) {
            Tile north = obj.tile().transform(0, 1);
            Tile west = obj.tile().transform(-1, 0);
            //First remove old objects
            if (obj.getRotation() == 0) {

                if (MapObjects.get(1560, north).isPresent()) {
                    GameObject thenorthgate = new GameObject(1560, north, 0, 0);
                    ObjectManager.removeObj(thenorthgate);
                    ObjectManager.removeObj(obj);
                    GameObject newgate1 = new GameObject(1558, new Tile(obj.tile().x - 1, obj.tile().y), 0, 3);
                    GameObject newgate2 = new GameObject(1560, new Tile(obj.tile().x - 2, obj.tile().y), 0, 3);
                    ObjectManager.addObj(newgate1);
                    ObjectManager.addObj(newgate2);
                    return true;
                }
            } else if (obj.getRotation() == 3) {
                if (MapObjects.get(1560, west).isPresent()) {
                    GameObject thewestgate = new GameObject(1558, west, 0, 3);
                    ObjectManager.removeObj(thewestgate);
                    ObjectManager.removeObj(obj);
                    GameObject newgate1 = new GameObject(1560, new Tile(obj.tile().x +1, obj.tile().y+1), 0, 0);
                    GameObject newgate2 = new GameObject(1558, new Tile(obj.tile().x+1, obj.tile().y), 0, 0);
                    ObjectManager.addObj(newgate1);
                    ObjectManager.addObj(newgate2);
                    return true;
                }
            }
        }
        if(obj.getId() == 1560){

            //First remove old objects

            Tile south = obj.tile().transform(0, -1);
            Tile east = obj.tile().transform(1,0);
            if(obj.getRotation() == 0){
            if (MapObjects.get(1558, south).isPresent()) {
                GameObject thesouthgate = new GameObject(1558,south,0,0);
                ObjectManager.removeObj(thesouthgate);
                ObjectManager.removeObj(obj);
                GameObject newgate1 = new GameObject(1558, new Tile(obj.tile().x-1, obj.tile().y-1), 0, 3);
                GameObject newgate2 = new GameObject(1560, new Tile(obj.tile().x-2, obj.tile().y-1), 0, 3);
                ObjectManager.addObj(newgate1);
                ObjectManager.addObj(newgate2);
                return true;
            }
            } else if (obj.getRotation() == 3) {
            if (MapObjects.get(1558, east).isPresent()) {
                GameObject theeastgate = new GameObject(1558, east, 0, 3);
                ObjectManager.removeObj(theeastgate);
                ObjectManager.removeObj(obj);
                GameObject newgate1 = new GameObject(1560, new Tile(obj.tile().x +2, obj.tile().y+1), 0, 0);
                GameObject newgate2 = new GameObject(1558, new Tile(obj.tile().x+2, obj.tile().y), 0, 0);
                ObjectManager.addObj(newgate1);
                ObjectManager.addObj(newgate2);
                return true;
            }
        }
            return true;
        }
            if (( obj.getId() == 1561 && obj.tile().equals(3031,3313) ) ||  ( obj.getId() == 1562 && obj.tile().equals(3032,3313))) {
                open1561Gate();
                return true;
            }

        if (( obj.getId() == 1561 && obj.tile().equals(3031,3312) ) ||  ( obj.getId() == 1562 && obj.tile().equals(3031,3313))) {
            close1561Gate();
            return true;
        }
        // Opening gate
        if(obj.getId() == 21600) {
            if (obj.tile().equals(2326,3802)) {
                openYaksGate();
                return true;
            }
        }

        // Closing gate
        if(obj.getId() == 21601) {
            if (obj.tile().equals(2326,3802)) {
                closeYaksGate();
                return true;
            }
        }

        // Closing gates
        if(obj.getId() == 1559 || obj.getId() == 1567) {
            if (obj.tile().equals(3080, 3500) || obj.tile().equals(3080,3499)) {
                closeEdgevilleGate();
                return true;
            }
        }

        return false;
    }

    private static void open1561Gate() {

        GameObject gate1 = new GameObject(1561, new Tile(3031, 3313), 0, 1);
        GameObject gate2 = new GameObject(1562, new Tile(3032, 3313), 0, 1);

        GameObject replacement1 = new GameObject(1562, new Tile(3031, 3313), 0, 0);
        GameObject replacement2 = new GameObject(1561, new Tile(3031, 3312), 0, 4);


        //First remove old objects
        ObjectManager.removeObj(gate1);
        ObjectManager.removeObj(gate2);

        //Add new objects after
        ObjectManager.addObj(replacement1);
        ObjectManager.addObj(replacement2);
    }
    private static void close1561Gate() {

        GameObject replacement1 = new GameObject(1561, new Tile(3031, 3313), 0, 1);
        GameObject replacement2 = new GameObject(1562, new Tile(3032, 3313), 0, 1);

        GameObject gate1 = new GameObject(1562, new Tile(3031, 3313), 0, 0);
        GameObject gate2 = new GameObject(1561, new Tile(3031, 3312), 0, 4);


        //First remove old objects
        ObjectManager.removeObj(gate1);
        ObjectManager.removeObj(gate2);

        //Add new objects after
        ObjectManager.addObj(replacement1);
        ObjectManager.addObj(replacement2);
    }
    private static void openEdgevilleGate() {

        GameObject gate1 = new GameObject(1558, new Tile(3080, 3501), 0, 3);
        GameObject gate2 = new GameObject(1560, new Tile(3079, 3501), 0, 3);

        GameObject replacement1 = new GameObject(1559, new Tile(3080,3500),0,2);
        GameObject replacement2 = new GameObject(1567, new Tile(3080,3499),0,2);

        //First remove old objects
        ObjectManager.removeObj(gate1);
        ObjectManager.removeObj(gate2);

        //Add new objects after
        ObjectManager.addObj(replacement1);
        ObjectManager.addObj(replacement2);
    }

    private static void closeEdgevilleGate() {
        GameObject gate1 = new GameObject(1559, new Tile(3080,3500),0,2);
        GameObject gate2 = new GameObject(1567, new Tile(3080,3499),0,2);

        GameObject replacement1 = new GameObject(1558, new Tile(3080, 3501), 0, 3);
        GameObject replacement2 = new GameObject(1560, new Tile(3079, 3501), 0, 3);

        //First remove old objects
        ObjectManager.removeObj(gate1);
        ObjectManager.removeObj(gate2);

        //Add new objects after
        ObjectManager.addObj(replacement1);
        ObjectManager.addObj(replacement2);
    }

    private static void openYaksGate() {
        ObjectManager.removeObj(new GameObject(21600, new Tile(2326,3802),0,3));
        ObjectManager.addObj(new GameObject(21601, new Tile(2326,3802),0,2));
    }

    private static void closeYaksGate() {
        ObjectManager.removeObj(new GameObject(21601, new Tile(2326,3802),0,2));
        ObjectManager.addObj(new GameObject(21600, new Tile(2326,3802),0,3));
    }
}
