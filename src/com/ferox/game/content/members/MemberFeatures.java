package com.ferox.game.content.members;

import com.ferox.game.GameConstants;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.util.Color;

import java.util.Calendar;

import static com.ferox.util.CustomItemIdentifiers.KEY_OF_DROPS;
import static com.ferox.util.CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX;

public class MemberFeatures {

    /**
     * Every first day of a new month sponsor players receive a free legendary mystery box and key of drops.
     * @param player The sponsor player
     */
    public static void checkForMonthlySponsorRewards(Player player) {
        if(player.<Boolean>getAttribOr(AttributeKey.RECEIVED_MONTHLY_SPONSOR_REWARDS,false)) {
            return;
        }

        if(World.getWorld().getCalendar().get(Calendar.DAY_OF_MONTH) == 1) {
            player.putAttrib(AttributeKey.RECEIVED_MONTHLY_SPONSOR_REWARDS, true);
            player.inventory().addOrBank(new Item(KEY_OF_DROPS), new Item(LEGENDARY_MYSTERY_BOX));
            player.message(Color.PURPLE.wrap("You have received your monthly sponsor rewards. Thank you for supporting "+ GameConstants.SERVER_NAME+"!"));
        }
    }
}
