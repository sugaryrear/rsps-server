package com.ferox.game.content.items.mystery_box.impl;

import com.ferox.GameServer;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.Utils;

import static com.ferox.game.content.collection_logs.LogType.MYSTERY_BOX;
import static com.ferox.util.CustomItemIdentifiers.ZENYTE_MYSTERY_BOX;

public class ZenyteMysterybox extends PacketInteraction {

    private boolean broadcast = false;

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 1) {
            if(item.getId() == ZENYTE_MYSTERY_BOX) {
                open(player);
                return true;
            }
        }
        return false;
    }

    private void open(Player player) {
        if(player.inventory().contains(ZENYTE_MYSTERY_BOX)) {
            player.inventory().remove(new Item(ZENYTE_MYSTERY_BOX),true);
            Item reward = rollReward();
           // MYSTERY_BOX.log(player, ZENYTE_MYSTERY_BOX, reward);
            Utils.sendDiscordInfoLog("Player " + player.getUsername() + " received a "+reward.unnote().name()+" from a zenyte mystery box.", "box_and_tickets");
            player.inventory().add(reward,true);
            int count = player.<Integer>getAttribOr(AttributeKey.ZENYTE_MYSTERY_BOXES_OPENED,0) + 1;
            player.putAttrib(AttributeKey.ZENYTE_MYSTERY_BOXES_OPENED, count);
            if(broadcast && !player.getUsername().equalsIgnoreCase("Box test")) {
                String worldMessage = "<img=505><shad=0>[<col=" + Color.MEDRED.getColorValue() + ">Zenyte Mystery Box</col>]</shad>:<col=AD800F> " + player.getUsername() + " received a <shad=0>" + reward.name() + "</shad>!";
                World.getWorld().sendWorldMessage(worldMessage);
            }
        }
    }

    private static final Item[] ITEMS = new Item[] {
        new Item(ItemIdentifiers.HEAVY_BALLISTA),
        new Item(ItemIdentifiers.LIGHT_BALLISTA),
        new Item(ItemIdentifiers.AMULET_OF_TORTURE),
        new Item(ItemIdentifiers.NECKLACE_OF_ANGUISH),
        new Item(ItemIdentifiers.RING_OF_SUFFERING),
        new Item(ItemIdentifiers.NECKLACE_OF_ANGUISH),
    };

    private static final Item[] RARE = new Item[] {
        new Item(ItemIdentifiers.AMULET_OF_TORTURE_OR),
        new Item(ItemIdentifiers.NECKLACE_OF_ANGUISH_OR),
        new Item(ItemIdentifiers.TORMENTED_BRACELET_OR),
    };

    public Item rollReward() {
        if (Utils.rollDie(GameServer.properties().nerfDropRateBoxes ? 50 : 14, 1)) {
            broadcast = true;
            return Utils.randomElement(RARE);
        } else {
            broadcast = false;
            return Utils.randomElement(ITEMS);
        }
    }
}
