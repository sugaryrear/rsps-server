package com.ferox.game.content.tasks.rewards;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ferox.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | April, 08, 2021, 21:57
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class TaskReward {

    private static final List<Item> COMMON = Arrays.asList(

        new Item(14524,1),
        new Item(21880,3000),
        new Item(3145,800),
        new Item(UNCUT_ZENYTE),
        new Item(2510,200),
        new Item(RUNITE_BAR+1,200),
        new Item(ABYSSAL_DAGGER),
        new Item(STAFF_OF_THE_DEAD),
        new Item(TOXIC_BLOWPIPE),
        new Item(TOXIC_STAFF_OF_THE_DEAD),
        new Item(ABYSSAL_DAGGER_P_13271),
        new Item(SERPENTINE_HELM),
        new Item(NEITIZNOT_FACEGUARD),
        new Item(DRAGONFIRE_WARD),
        new Item(24999)
    );

    private static final List<Item> UNCOMMON = Arrays.asList(
        new Item(ABYSSAL_DAGGER),
        new Item(STAFF_OF_THE_DEAD),
        new Item(TOXIC_BLOWPIPE),
        new Item(TOXIC_STAFF_OF_THE_DEAD),
        new Item(ABYSSAL_DAGGER_P_13271),
        new Item(ARMADYL_CROSSBOW),
        new Item(SERPENTINE_HELM),
        new Item(DINHS_BULWARK),
        new Item(NEITIZNOT_FACEGUARD),
        new Item(DRAGONFIRE_WARD),
        new Item(SARADOMIN_GODSWORD),
        new Item(ZAMORAK_GODSWORD),
        new Item(BANDOS_GODSWORD)
    );

    private static final List<Item> RARE = Arrays.asList(
        new Item(HARMONISED_ORB),
        new Item(ELDRITCH_ORB),
        new Item(VOLATILE_ORB),
        new Item(NIGHTMARE_STAFF),
        new Item(ELDER_MAUL),
        new Item(MORRIGANS_COIF),
        new Item(MORRIGANS_LEATHER_BODY),
        new Item(MORRIGANS_LEATHER_CHAPS),
        new Item(ZURIELS_HOOD),
        new Item(ZURIELS_ROBE_TOP),
        new Item(ZURIELS_ROBE_BOTTOM),
        new Item(STATIUSS_FULL_HELM),
        new Item(STATIUSS_PLATEBODY),
        new Item(STATIUSS_PLATELEGS),
        new Item(STATIUSS_WARHAMMER),
        new Item(VESTAS_CHAINBODY),
        new Item(VESTAS_PLATESKIRT),
        new Item(VESTAS_SPEAR),
        new Item(VESTAS_LONGSWORD),
        new Item(DRAGON_CLAWS),
        new Item(ARMADYL_GODSWORD),
        new Item(HEAVY_BALLISTA),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(RING_OF_SUFFERING),
        new Item(TORMENTED_BRACELET)
    );

    public static List<Item> getPossibleRewards() {
        return new ArrayList<>(COMMON);
    }

    public static void reward(Player player) {
        var bmReward = 15_000;
        List<Item> items;
//        if (Utils.rollDie(100, 1)) {
//            items = RARE;
//        } else if (Utils.rollDie(50, 1)) {
//            items = UNCOMMON;
//        } else {
//            items = COMMON;
//        }
        items = COMMON;
        Item item = Utils.randomElement(items);
        if (item.getValue() >= 30000 && !player.getUsername().equalsIgnoreCase("Box test")) {
            boolean amOverOne = item.getAmount() > 1;
            String amtString = amOverOne ? "x " + Utils.format(item.getAmount()) + "" : Utils.getAOrAn(item.name());
            String msg = "<img=1081> <col=AD800F>" + player.getUsername() + " has received " + amtString + "<shad=0> " + item.name() + "</shad>!";
            World.getWorld().sendWorldMessage(msg);
        }

        var blood_reaper = player.hasPetOut("Blood Reaper pet");
        if(blood_reaper) {
            int extraBM = bmReward * 10 / 100;
            bmReward += extraBM;
        }

        player.inventory().addOrBank(new Item(BLOOD_MONEY, bmReward), item);
    }
}
