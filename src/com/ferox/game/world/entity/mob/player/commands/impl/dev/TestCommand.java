
package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.game.content.daily_tasks.DailyTaskManager;
import com.ferox.game.content.daily_tasks.DailyTasks;
import com.ferox.game.content.tasks.impl.Tasks;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.npc.pets.Pet;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.ground.GroundItem;
import com.ferox.game.world.items.ground.GroundItemHandler;
import com.ferox.util.Color;
import com.ferox.util.Utils;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static com.ferox.game.content.collection_logs.LogType.OTHER;
import static com.ferox.game.world.entity.AttributeKey.*;
import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;
import static com.ferox.util.NpcIdentifiers.ABYSSAL_DEMON_415;

public class TestCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        /*final List<AttributeKey> keyList = Arrays.asList(EVENT_REWARD_1_CLAIMED, EVENT_REWARD_2_CLAIMED, EVENT_REWARD_3_CLAIMED, EVENT_REWARD_4_CLAIMED, EVENT_REWARD_5_CLAIMED, EVENT_REWARD_6_CLAIMED, EVENT_REWARD_7_CLAIMED, EVENT_REWARD_8_CLAIMED, EVENT_REWARD_9_CLAIMED, EVENT_REWARD_10_CLAIMED, EVENT_REWARD_11_CLAIMED, EVENT_REWARD_12_CLAIMED, EVENT_REWARD_13_CLAIMED, EVENT_REWARD_14_CLAIMED, EVENT_REWARD_15_CLAIMED, EVENT_REWARD_16_CLAIMED, EVENT_REWARD_17_CLAIMED, EVENT_REWARD_18_CLAIMED, EVENT_REWARD_19_CLAIMED, EVENT_REWARD_20_CLAIMED, EVENT_REWARD_21_CLAIMED, EVENT_REWARD_22_CLAIMED, EVENT_REWARD_23_CLAIMED, EVENT_REWARD_24_CLAIMED, EVENT_REWARD_25_CLAIMED, EVENT_REWARD_26_CLAIMED, EVENT_REWARD_27_CLAIMED, EVENT_REWARD_28_CLAIMED, EVENT_REWARD_29_CLAIMED, EVENT_REWARD_30_CLAIMED, EVENT_REWARD_31_CLAIMED, EVENT_REWARD_32_CLAIMED, EVENT_REWARD_33_CLAIMED, EVENT_REWARD_34_CLAIMED, EVENT_REWARD_35_CLAIMED);

        for (AttributeKey key : keyList) {
            player.putAttrib(key, false);
        }*/
        //player.getEventRewards().open();
        /*Item[] dummy_items = {
            //Row 1
            new Item(4151), new Item(4151), new Item(4151), new Item(4151), new Item(4151), new Item(4151), new Item(4151), new Item(4151), new Item(4151), new Item(4151), new Item(4151),
            //Row 2
            new Item(1038), new Item(1038), new Item(1038), new Item(1038), new Item(1038), new Item(1038), new Item(1038), new Item(1038), new Item(1038), new Item(1038), new Item(1038),
            //Row 3
            new Item(11802), new Item(11802), new Item(11802), new Item(11802), new Item(11802), new Item(11802), new Item(11802), new Item(11802), new Item(11802), new Item(11802), new Item(11802),
            //Row 4
            new Item(1057), new Item(1057), new Item(1057), new Item(1057), new Item(1057), new Item(1057), new Item(1057), new Item(1057), new Item(1057), new Item(1057), new Item(1057),
        };
        player.getPacketSender().sendItemOnInterfaceSlot(73315, new Item(4151),0);
        player.getPacketSender().sendItemOnInterfaceSlot(73316, new Item(HWEEN_TOKENS,5000),0);
        player.getPacketSender().sendItemOnInterfaceSlot(73317, new Item(4151),0);
        player.getPacketSender().sendItemOnInterface(73318, dummy_items);*/

        //GroundItemHandler.createGroundItem(new GroundItem(new Item(WILDERNESS_KEY), player.tile(),null));

        System.out.println(player.pet().def().name);
        player.message("Test command has been activated.");
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
