package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.game.content.packet_actions.interactions.objects.ObjectActions;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.MapObjects;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.ferox.util.ObjectIdentifiers.*;
import static org.apache.logging.log4j.util.Unbox.box;

/**
 * @author PVE
 * @Since augustus 26, 2020
 */
public class ObjectClickOnePacketListener implements PacketListener {

    private static final Logger logger = LogManager.getLogger(ObjectClickOnePacketListener.class);

    /**
     * These are objects in specific locations that trigger {@link ObjectActions#handleAction} before the pathing is complete.
     * <br> This is for agility objects that have custom pathfinding code. It usually means you do some server-side pathfinding (beyond normal client-pathing)
     * to get to a certian stat tile, Then you proceed with normal agility obstacle/etc logic. It's just an extra step for PF before doing an activity.
     * <br><br>
     *     when handling objects (doing custom walkto logic) if {@code
     *             player.smartPathTo(startPos, obj.getSize());} doesn't work or walk exactly where you expect it too, its probably beacuse its a 1999 pathfinder.
     *             <br> use {@code player.doPath(new DefaultPathFinder(), tile)} instead
     * @param object
     * @return
     */
    public static boolean isRemoteObject(GameObject object) {
        //Rogues den basement door.
        if (object.getId() == DOOR_7259 && object.getX() == 3061 && object.getY() == 4984 && object.getZ() == 1) {
            return true;
        }
        //inferno
        if (object.getId() == 30282) {
            return true;
        }

        if (object.getId() == 2143 || object.getId() == 2144) {
            return true;
        }
        if (object.getId() == DOOR_20925) {
            return true;
        }
        if (object.getId() == 1727 && object.getX() == 3131 && object.getY() == 9917) {
            return true;
        }
        if (object.getId() == 1728 && object.getX() == 3132 && object.getY() == 9917) {
            return true;
        }
        //grapple to armadyl
        if (object.getId() == 26380 && object.getX() == 2871 && object.getY() == 5270) {
            return true;
        }
        //culinaromancer's chest in lumbridge basement
        if (object.getId() == 12309 && object.getX() == 3219 && object.getY() == 9623) {
            return true;
        }
        if (object.getId() == 9623 && object.getX() == 12309 && object.getY() == 3219) {
            return true;
        }
        //Rogues den basement passageway.
        if (object.getId() == PASSAGEWAY_7258 && object.getX() == 3061 && object.getY() == 4986 && object.getZ() == 1) {
            return true;
        }
        if (object.getId() == PILLAR_31561) { // "jump-to" pillar in member caves
            return true;
        }
        if (object.getId() == PILLAR_31561 && object.getX() == 2356 && object.getY() == 9841) { // "jump-to" pillar in member caves
            return true;
        }
        if (object.getId() == PILLAR_31561 && object.getX() == 3202 && object.getY() == 10196) { // "jump-to" pillar in rev caves
            return true;
        }
        if (object.getId() == PILLAR_31561 && object.getX() == 3180 && object.getY() == 10209) { // "jump-to" pillar in rev caves
            return true;
        }
        if (object.getId() == PILLAR_31561 && object.getX() == 3241 && object.getY() == 10145) { // "jump-to" pillar in rev caves
            return true;
        }
        if (object.getId() == PILLAR_31561 && object.getX() == 3200 && object.getY() == 10136) { // "jump-to" pillar in rev caves
            return true;
        }
        if (object.getId() == PILLAR_31561 && object.getX() == 3220 && object.getY() == 10086) { // "jump-to" pillar in rev caves
            return true;
        }
        if (object.getId() == GAP_14930) // seer's rooftop gap
            return true;
        if (object.getId() == OBSTACLE_PIPE_20210) { // barbarian enter pipe
            return true;
        }
        if (object.getId() == ANVIL && object.tile().equals(2794, 2793))
            return true;
        if (object.getId() == ANVIL && object.tile().equals(3343, 9652))
            return true;
        // yes they are remote. trigger instantly and handle PF walkto code inside the handler code
        return switch (object.getId()) {
            case LAVA_GAP, STEPS_30189, STEPS_30190, PIPE_21728, STEPPING_STONE_23556, OBSTACLE_PIPE_16509, ROPESWING_23132, GAP_14947, STEPPING_STONE_19040, CABLE, TROPICAL_TREE_14404, LEDGE_14920, NARROW_WALL, LEDGE_14836, EDGE, GAP_14990, GAP_14991, SARCOPHAGUS_20722, STAIRCASE_20670, HAND_HOLDS_14901, GAP_14903, TIGHTROPE_14992, SARCOPHAGUS_20771, GAP_11631, CRATE_11632, ROPE_LADDER_28858, STAIRCASE_20668, WALL_14832, GAP_14835, PILE_OF_FISH, ZIP_LINE_14403, STAIRCASE_20667, REDWOOD, REDWOOD_29670, WALL_11630, GAP_14848, GAP_14846, POLEVAULT, GAP_14847, GAP_14897 -> true;
            default -> false;
        };
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        final int x = packet.readLEShortA();
        final int id = packet.readUnsignedShort();
        final int y = packet.readUnsignedShortA();
        Tile tile = new Tile(x, y, player.tile().getLevel());
        Optional<GameObject> object = MapObjects.get(id, tile);
//System.out.println("object id: "+id);
        if (player.dead()) {
            return;
        }

        //Make sure we aren't doing something else..
        if (player.busy()) {
            return;
        }

        player.afkTimer.reset();

        //Fix object not found for instances
        if (object.isEmpty() && tile.getLevel() > 3) {
            tile = new Tile(x, y, player.tile().getLevel() % 4);
            object = MapObjects.get(id, tile);
            player.debugMessage(String.format("found real mapobj %s from %s", object.orElse(null), player.tile().level));
        }

        object.ifPresent(gameObject -> player.debugMessage("op 1 " + gameObject.toString()));
        object.ifPresent(gameObject -> System.out.println("op 1 " + gameObject.toString()));

        //Make sure the object actually exists in the region...
        if (object.isEmpty()) {
            System.out.println("object "+id+" x: "+x+" y: "+y+" is empty");
            //logger.info("Object op1 with id {} does not exist for player " + player.toString() + " !", box(id));
            //Utils.sendDiscordInfoLog("Object op1 with id " + id + " does not exist for player " + player.toString() + "!");
           // return;
        }

        if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
            player.getBankPin().openIfNot();
            return;
        }

        if(player.askForAccountPin()) {
            player.sendAccountPinMessage();
            return;
        }

        final GameObject gameObject = object.get();

        if (gameObject.definition() == null) {
System.out.println("here lol");
            logger.error("ObjectDefinition for object {} is null for player " + player.toString() + ".", box(id));
            return;
        }

        if (player.locked()) {
            System.out.println("locked player ");
            return;
        }
if(id == 30387 || id == 30388){
    return;
}
        player.stopActions(false);
        player.putAttrib(AttributeKey.INTERACTION_OBJECT, gameObject);
        player.putAttrib(AttributeKey.INTERACTION_OPTION, 1);

        //Do actions...
        player.faceObj(gameObject);

        pathToAndTrigger(player, gameObject, tile,1);
    }

    public static void pathToAndTrigger(Player player, GameObject object, Tile tile, int option) {
        player.getRouteFinder().routeObject(object, () -> ObjectActions.handleAction(player, object, option));
    }

}
