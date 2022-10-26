package com.ferox.game.world.object.doors;

import com.ferox.game.content.areas.varrock.CookingGuild;
import com.ferox.game.content.skill.impl.agility.course.WildernessCourse;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.net.packet.interaction.PacketInteraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ferox.util.ObjectIdentifiers.*;

/**
 * @author Heaven
 */
public class Doors extends PacketInteraction {

    private final List<Integer> have_scripts = Arrays.asList(WildernessCourse.LOWER_GATE, WildernessCourse.UPPERGATE_EAST, WildernessCourse.UPPERGATE_WEST, CookingGuild.GUILD_DOOR, 11728, 24057,
        DOOR_24058, DOOR_11727, DOOR_24309, DOOR_24306, DOOR_24318, DOOR_2108, DOOR_2111, DOOR_2112, DOOR_2113, DOOR_10043, DOOR_1536);

    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        Door door = CACHE.stream().filter(d -> d.id() == object.getId()).findAny().orElse(null);
        if (door == null)
            return false;
        if(object.getX() == 3101 && ( object.getY() == 3510 || object.getY() == 3509))
            return false;
        if (have_scripts.stream().anyMatch(d -> d == door.id())) {
            // These must have their own hooks.
            return false;
        }
        if (door.closed()) {
            door.open(object, player, true);
            return true;
        } else if (door.open()) {
            door.close(object, player,true, true);
            return true;
        }
        return false;
    }

    public static final ArrayList<Door> CACHE = new ArrayList<>();
}
