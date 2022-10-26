package com.ferox.game.content.seasonal_events.halloween;

import com.ferox.game.content.seasonal_events.SeasonalEventUtils;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

import static com.ferox.game.content.seasonal_events.rewards.UnlockEventRewards.UNLOCKED_ITEM_SLOT;
import static com.ferox.game.world.entity.AttributeKey.*;
import static com.ferox.util.CustomItemIdentifiers.HWEEN_TOKENS;
import static com.ferox.util.NpcIdentifiers.KILLER;
import static com.ferox.util.NpcIdentifiers.TEACHER_AND_PUPIL_1922;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 12, 2021
 */
public class Halloween extends PacketInteraction {

    private static final Logger logger = LogManager.getLogger(Halloween.class);

    public static void loadNpcs() {
        if (SeasonalEventUtils.HALLOWEEN_EVENT_ACTIVE) {
            int index = 0;
            Npc teacher = new Npc(TEACHER_AND_PUPIL_1922, new Tile(3099, 3483));
            World.getWorld().registerNpc(teacher);
            index++;

            loadScreams(index);
            logger.info("Loaded {} halloween npcs.", index);
        }
    }

    private static final List<Tile> lumbridgeSpawns = Arrays.asList(
        new Tile(3205, 3233), new Tile(3191, 3203),
        new Tile(3210, 3203), new Tile(3206, 3246),
        new Tile(3235, 3201), new Tile(3245, 3225),
        new Tile(3245, 3209), new Tile(3242, 3182),
        new Tile(3231, 3231), new Tile(3218, 3237),
        new Tile(3220, 3254), new Tile(3235, 3261),
        new Tile(3246, 3240), new Tile(3258, 3260),
        new Tile(3251, 3288), new Tile(3230, 3296),
        new Tile(3228, 3149), new Tile(3200, 3168),
        new Tile(3199, 3190), new Tile(3165, 3173));
    private static final List<Tile> draynorSpawns = Arrays.asList(
        new Tile(3093, 3243), new Tile(3118, 3241),
        new Tile(3088, 3221), new Tile(3076, 3248),
        new Tile(3110, 3295), new Tile(3113, 3205),
        new Tile(3109, 3167), new Tile(3111, 3325),
        new Tile(3122, 3163), new Tile(3092, 3270));
    private static final List<Tile> varrockSpawns = Arrays.asList(
        new Tile(3164, 3463), new Tile(3187, 3489),
        new Tile(3214, 3502), new Tile(3212, 3441),
        new Tile(3176, 3456), new Tile(3182, 3441),
        new Tile(3171, 3428), new Tile(3143, 3440),
        new Tile(3182, 3421), new Tile(3187, 3401),
        new Tile(3206, 3415), new Tile(3206, 3399),
        new Tile(3213, 3466), new Tile(3255, 3480),
        new Tile(3249, 3447), new Tile(3254, 3422),
        new Tile(3253, 3396), new Tile(3228, 3410),
        new Tile(3151, 3415), new Tile(3167, 3433));
    private static final List<Tile> faladorSpawns = Arrays.asList(
        new Tile(2979, 3340), new Tile(2964, 3347),
        new Tile(2945, 3340), new Tile(2940, 3357),
        new Tile(2947, 3374), new Tile(2965, 3381),
        new Tile(2964, 3359), new Tile(2980, 3376),
        new Tile(2991, 3383), new Tile(3009, 3377),
        new Tile(3005, 3362), new Tile(3007, 3342),
        new Tile(3022, 3361), new Tile(3045, 3369),
        new Tile(3061, 3371), new Tile(3042, 3352),
        new Tile(2990, 3317), new Tile(3006, 3325),
        new Tile(2965, 3396), new Tile(3022, 3338));
    private static final List<Tile> wildernessSpawns = Arrays.asList(
        new Tile(3246, 10225), new Tile(3247, 10211),
        new Tile(3256, 10191), new Tile(3244, 10176),
        new Tile(3250, 10161), new Tile(3251, 10143),
        new Tile(3148, 3670), new Tile(3154, 3681),
        new Tile(3174, 3683), new Tile(3173, 3675),
        new Tile(3165, 3672), new Tile(3173, 3662),
        new Tile(3137, 3784), new Tile(3137, 3797),
        new Tile(3149, 3761), new Tile(3155, 3789),
        new Tile(3141, 3810), new Tile(3121, 3775),
        new Tile(2979, 3859), new Tile(2989, 3865),
        new Tile(2976, 3845), new Tile(2992, 3854),
        new Tile(3000, 3847), new Tile(3001, 3874),
        new Tile(3337, 3714), new Tile(3340, 3698),
        new Tile(3330, 3686), new Tile(3351, 3697),
        new Tile(3353, 3720), new Tile(3323, 3707),
        new Tile(2983, 3600), new Tile(2991, 3612),
        new Tile(2985, 3624), new Tile(2971, 3626),
        new Tile(2965, 3612), new Tile(2974, 3599),
        new Tile(3181, 3947), new Tile(3173, 3953),
        new Tile(3171, 3939), new Tile(3160, 3959),
        new Tile(3202, 3944), new Tile(3192, 3955),
        new Tile(3099, 3959), new Tile(3116, 3958),
        new Tile(3078, 3952), new Tile(3098, 3939),
        new Tile(3106, 3936), new Tile(3103, 3928),
        new Tile(2998, 3910), new Tile(3016, 3907),
        new Tile(3004, 3936), new Tile(3017, 3927),
        new Tile(3031, 3921), new Tile(3020, 3941),
        new Tile(3014, 3940), new Tile(3033, 3933),
        new Tile(3078, 3949), new Tile(3136, 3947),
        new Tile(3214, 3916), new Tile(3187, 3909),
        new Tile(3135, 3916), new Tile(3039, 3906),
        new Tile(3249, 3915), new Tile(3213, 3927),
        new Tile(3256, 3928), new Tile(3272, 3946),
        new Tile(3298, 3908), new Tile(3330, 3880),
        new Tile(3331, 3841), new Tile(3306, 3818),
        new Tile(3284, 3795), new Tile(3347, 3748),
        new Tile(3347, 3723), new Tile(3340, 3692),
        new Tile(3364, 3689), new Tile(3352, 3649),
        new Tile(3312, 3663), new Tile(3298, 3635),
        new Tile(3327, 3607), new Tile(3340, 3584),
        new Tile(3313, 3580), new Tile(3283, 3585),
        new Tile(3267, 3610), new Tile(3257, 3579),
        new Tile(3239, 3573), new Tile(3203, 3572),
        new Tile(3196, 3554), new Tile(3230, 3543),
        new Tile(3266, 3550), new Tile(3296, 3551),
        new Tile(3167, 3537), new Tile(3147, 3555),
        new Tile(3117, 3574), new Tile(3077, 3586),
        new Tile(3094, 3599), new Tile(3086, 3620),
        new Tile(3099, 3647), new Tile(3107, 3672),
        new Tile(3075, 3679), new Tile(3081, 3716),
        new Tile(3098, 3741), new Tile(3105, 3777),
        new Tile(3123, 3812), new Tile(3143, 3808),
        new Tile(3148, 3847), new Tile(3178, 3881),
        new Tile(3139, 3881), new Tile(3036, 3883),
        new Tile(2987, 3839), new Tile(3024, 3811),
        new Tile(2976, 3797), new Tile(3053, 3787),
        new Tile(2987, 3717), new Tile(2964, 3664),
        new Tile(2981, 3644), new Tile(3001, 3659),
        new Tile(2988, 3578), new Tile(2978, 3557),
        new Tile(3013, 3566), new Tile(3035, 3593),
        new Tile(3063, 3609), new Tile(3052, 3565),
        new Tile(3150, 3705), new Tile(3194, 3756),
        new Tile(3238, 3785), new Tile(3253, 3802),
        new Tile(3259, 3879), new Tile(3225, 3885));

    public static void loadScreams(int index) {
        for (Tile tile : lumbridgeSpawns) {
            Npc killer = new Npc(KILLER, tile);
            killer.walkRadius(5);
            World.getWorld().registerNpc(killer);
            index++;
        }
        for (Tile tile : draynorSpawns) {
            Npc killer = new Npc(KILLER, tile);
            killer.walkRadius(5);
            World.getWorld().registerNpc(killer);
            index++;
        }
        for (Tile tile : varrockSpawns) {
            Npc killer = new Npc(KILLER, tile);
            killer.walkRadius(5);
            World.getWorld().registerNpc(killer);
            index++;
        }
        for (Tile tile : faladorSpawns) {
            Npc killer = new Npc(KILLER, tile);
            killer.walkRadius(5);
            World.getWorld().registerNpc(killer);
            index++;
        }
        for (Tile tile : wildernessSpawns) {
            Npc killer = new Npc(KILLER, tile);
            killer.walkRadius(5);
            World.getWorld().registerNpc(killer);
            index++;
        }
    }

    @Override
    public boolean handleButtonInteraction(Player player, int button) {
        if (button == 73307) {
            player.optionsTitled("Exchange your 5,000 H'ween tokens for a reward?", "Yes", "No", () -> {
                if (!player.inventory().contains(new Item(HWEEN_TOKENS, 5000))) {
                    return;
                }

                final List<AttributeKey> keyList = Arrays.asList(EVENT_REWARD_1_CLAIMED, EVENT_REWARD_2_CLAIMED, EVENT_REWARD_3_CLAIMED, EVENT_REWARD_4_CLAIMED, EVENT_REWARD_5_CLAIMED, EVENT_REWARD_6_CLAIMED, EVENT_REWARD_7_CLAIMED, EVENT_REWARD_8_CLAIMED, EVENT_REWARD_9_CLAIMED, EVENT_REWARD_10_CLAIMED, EVENT_REWARD_11_CLAIMED, EVENT_REWARD_12_CLAIMED, EVENT_REWARD_13_CLAIMED, EVENT_REWARD_14_CLAIMED, EVENT_REWARD_15_CLAIMED, EVENT_REWARD_16_CLAIMED, EVENT_REWARD_17_CLAIMED, EVENT_REWARD_18_CLAIMED, EVENT_REWARD_19_CLAIMED, EVENT_REWARD_20_CLAIMED, EVENT_REWARD_21_CLAIMED, EVENT_REWARD_22_CLAIMED, EVENT_REWARD_23_CLAIMED, EVENT_REWARD_24_CLAIMED, EVENT_REWARD_25_CLAIMED, EVENT_REWARD_26_CLAIMED, EVENT_REWARD_27_CLAIMED, EVENT_REWARD_28_CLAIMED, EVENT_REWARD_29_CLAIMED, EVENT_REWARD_30_CLAIMED, EVENT_REWARD_31_CLAIMED, EVENT_REWARD_32_CLAIMED, EVENT_REWARD_33_CLAIMED, EVENT_REWARD_34_CLAIMED, EVENT_REWARD_35_CLAIMED, EVENT_REWARD_36_CLAIMED, EVENT_REWARD_37_CLAIMED, EVENT_REWARD_38_CLAIMED, EVENT_REWARD_39_CLAIMED, EVENT_REWARD_40_CLAIMED, EVENT_REWARD_41_CLAIMED, EVENT_REWARD_42_CLAIMED, EVENT_REWARD_43_CLAIMED, EVENT_REWARD_44_CLAIMED);
                var unlockedAllRewards = keyList.stream().allMatch(key -> player.<Boolean>getAttribOr(key, false));
                if (unlockedAllRewards) {
                    player.message(Color.RED.wrap("You have already unlocked all rewards."));
                    return;
                }

                Item reward = player.getEventRewards().generateReward();
                if (reward == null) {
                    return;
                }

                if (player.inventory().contains(new Item(HWEEN_TOKENS, 5000))) {
                    player.getEventRewards().refreshItems();
                    player.getPacketSender().sendItemOnInterfaceSlot(UNLOCKED_ITEM_SLOT, reward.copy(),0);
                    player.getEventRewards().rollForReward(HWEEN_TOKENS, 5000, reward.copy());
                } else {
                    player.message(Color.RED.wrap("You do not have enough H'ween tokens to roll for a reward."));
                }
            });
            return true;
        }
        if (button == 73310) {
            player.getEventRewards().reset("H'ween");
            return true;
        }
        return false;
    }

    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if (option == 1) {
            if (object.getId() == 2654) {
                player.getEventRewards().open();
                return true;
            }
        }
        return false;
    }
}
