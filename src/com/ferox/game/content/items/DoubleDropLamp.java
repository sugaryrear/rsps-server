package com.ferox.game.content.items;

import com.ferox.game.content.EffectTimer;
import com.ferox.game.task.TaskManager;
import com.ferox.game.task.impl.DropRateLampTask;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.rights.MemberRights;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import com.ferox.util.Utils;

import static com.ferox.util.CustomItemIdentifiers.DOUBLE_DROPS_LAMP;

/**
 * @author Patrick van Elderen | January, 11, 2021, 13:26
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class DoubleDropLamp extends PacketInteraction {

    private int ticks(MemberRights memberRights) {
        int ticks = 0;
        switch (memberRights) {
            case NONE -> ticks = 6000;//One hour
            case MEMBER -> ticks = 6500;//One hour and 5 minutes
            case SUPER_MEMBER -> ticks = 7000;//One and 10 minutes
            case ELITE_MEMBER -> ticks = 7500;//One hour and 15 minutes
            case EXTREME_MEMBER -> ticks = 9000;//One hour and 30 minutes
            case LEGENDARY_MEMBER -> ticks = 10500;//One hour and 45 minutes
            case VIP -> ticks = 11000;//One hour and 50 minutes
            case SPONSOR_MEMBER -> ticks = 11500;//One hour and 55 minutes
        }
        return ticks;
    }

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(item.getId() == DOUBLE_DROPS_LAMP && option == 1) {
            var ticks = player.<Integer>getAttribOr(AttributeKey.DOUBLE_DROP_LAMP_TICKS, 0);
            var dropRateLampActive = ticks > 0;

            if(dropRateLampActive) {
                player.message(Color.RED.tag()+"You already have a Drop rate lamp active.");
                return false;
            }
            int timer = ticks(player.getMemberRights());
            player.message("You rub the mysterious lamp.");
            player.message("You will now have "+Color.BLUE.tag()+"double drops bonus</col> from bosses for 1 hour.");
            player.message("The lamp turns into dust.");
            player.inventory().remove(new Item(DOUBLE_DROPS_LAMP), true);
            player.putAttrib(AttributeKey.DOUBLE_DROP_LAMP_TICKS, timer);
            player.getPacketSender().sendEffectTimer((int) Utils.ticksToSeconds(timer), EffectTimer.DROP_LAMP);
            TaskManager.submit(new DropRateLampTask(player));
            return true;
        }
        return false;
    }
}
