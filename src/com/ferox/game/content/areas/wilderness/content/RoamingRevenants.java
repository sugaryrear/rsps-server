package com.ferox.game.content.areas.wilderness.content;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Utils;

import java.util.Arrays;
import java.util.List;

import static com.ferox.util.NpcIdentifiers.*;

/**
 * This class represents the roaming revents around the widlerness.
 *
 * @author Zerikoth
 * @Since september 19, 2020
 */
public class RoamingRevenants {

    private static final int[] REVS = {REVENANT_IMP, REVENANT_GOBLIN, REVENANT_HOBGOBLIN, REVENANT_PYREFIEND, REVENANT_HOBGOBLIN, REVENANT_CYCLOPS, REVENANT_HELLHOUND, REVENANT_DEMON, REVENANT_ORK, REVENANT_DARK_BEAST, REVENANT_KNIGHT, REVENANT_DRAGON};

    private static final List<Tile> Tile_LIST = Arrays.asList(
        new Tile(2951, 3933, 0),
        new Tile(2979, 3923, 0),
        new Tile(2991, 3889, 0),
        new Tile(2959, 3880, 0),
        new Tile(2983, 3853, 0),
        new Tile(2990, 3824, 0),
        new Tile(2977, 3804, 0),
        new Tile(2958, 3787, 0),
        new Tile(2990, 3760, 0),
        new Tile(2963, 3733, 0),
        new Tile(2992, 3721, 0),
        new Tile(2995, 3697, 0),
        new Tile(2975, 3685, 0),
        new Tile(2959, 3662, 0),
        new Tile(2986, 3653, 0),
        new Tile(2994, 3632, 0),
        new Tile(2965, 3623, 0),
        new Tile(2975, 3599, 0),
        new Tile(3018, 3593, 0),
        new Tile(3053, 3604, 0),
        new Tile(3059, 3628, 0),
        new Tile(3061, 3660, 0),
        new Tile(3070, 3679, 0),
        new Tile(3083, 3681, 0),
        new Tile(3106, 3676, 0),
        new Tile(3116, 3655, 0),
        new Tile(3109, 3634, 0),
        new Tile(3090, 3613, 0),
        new Tile(3119, 3598, 0),
        new Tile(3161, 3598, 0),
        new Tile(3166, 3623, 0),
        new Tile(3196, 3632, 0),
        new Tile(3216, 3654, 0),
        new Tile(3188, 3663, 0),
        new Tile(3162, 3689, 0),
        new Tile(3166, 3713, 0),
        new Tile(3189, 3731, 0),
        new Tile(3157, 3751, 0),
        new Tile(3124, 3748, 0),
        new Tile(3093, 3736, 0),
        new Tile(3078, 3758, 0),
        new Tile(3054, 3760, 0),
        new Tile(3046, 3722, 0),
        new Tile(3041, 3777, 0),
        new Tile(3041, 3793, 0),
        new Tile(3024, 3812, 0),
        new Tile(3082, 3806, 0),
        new Tile(3118, 3805, 0),
        new Tile(3147, 3810, 0),
        new Tile(3166, 3792, 0),
        new Tile(3221, 3789, 0),
        new Tile(3243, 3808, 0),
        new Tile(3274, 3787, 0),
        new Tile(3243, 3754, 0),
        new Tile(3229, 3732, 0),
        new Tile(3283, 3731, 0),
        new Tile(3305, 3732, 0),
        new Tile(3303, 3693, 0),
        new Tile(3316, 3670, 0),
        new Tile(3342, 3669, 0),
        new Tile(3355, 3683, 0),
        new Tile(3354, 3718, 0),
        new Tile(3352, 3736, 0),
        new Tile(3343, 3751, 0),
        new Tile(3339, 3781, 0),
        new Tile(3335, 3806, 0),
        new Tile(3302, 3805, 0),
        new Tile(3289, 3815, 0),
        new Tile(3311, 3828, 0),
        new Tile(3338, 3841, 0),
        new Tile(3336, 3874, 0),
        new Tile(3312, 3879, 0),
        new Tile(3279, 3880, 0),
        new Tile(3245, 3874, 0),
        new Tile(3223, 3881, 0),
        new Tile(3180, 3880, 0),
        new Tile(3161, 3877, 0),
        new Tile(3122, 3882, 0),
        new Tile(3105, 3887, 0),
        new Tile(3075, 3887, 0),
        new Tile(3038, 3886, 0),
        new Tile(3026, 3872, 0),
        new Tile(3028, 3907, 0),
        new Tile(3027, 3924, 0),
        new Tile(3057, 3931, 0),
        new Tile(3220, 3913, 0),
        new Tile(3252, 3914, 0),
        new Tile(3292, 3907, 0),
        new Tile(3221, 3689, 0),
        new Tile(3240, 3655, 0));

    public static void populateWorld() {
        for (int index = 0; index < 300; index++) {//Spawn 300 random revs around the wildy
            Tile spawnTile = Utils.randomElement(Tile_LIST);

            int x = spawnTile.getX() + Utils.random(-10, +10);
            int y = spawnTile.getY() + Utils.random(-10, +10);
            Npc revenant = new Npc(Utils.randomElement(REVS), new Tile(x, y));
            World.getWorld().registerNpc(revenant);
            revenant.walkRadius(25);
            revenant.putAttrib(AttributeKey.MAX_DISTANCE_FROM_SPAWN, 25);
        }
    }

}
