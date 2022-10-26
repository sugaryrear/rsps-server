package com.ferox.game.content.items.mystery_box.impl;

import com.ferox.game.content.items.mystery_box.MboxItem;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.Utils;

import static com.ferox.game.content.collection_logs.LogType.MYSTERY_BOX;
import static com.ferox.util.CustomItemIdentifiers.EPIC_PET_BOX;

/**
 * @author Patrick van Elderen | May, 02, 2021, 16:50
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class EpicPetMysteryBox extends PacketInteraction {

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 1) {
            if(item.getId() == EPIC_PET_BOX) {
                open(player);
                return true;
            }
        }
        return false;
    }

    public static Item rollReward() {
        if (Utils.rollDie(5, 1)) {
            return Utils.randomElement(RARE);
        } else if (Utils.rollDie(3, 1)) {
            return Utils.randomElement(UNCOMMON);
        } else {
            return Utils.randomElement(COMMON);
        }
    }

    public static void open(Player player) {
        if(player.inventory().contains(EPIC_PET_BOX)) {
            player.inventory().remove(new Item(EPIC_PET_BOX),true);
            Item reward = rollReward();
            Utils.sendDiscordInfoLog("Player " + player.getUsername() + " received a "+reward.unnote().name()+" from a mystery box.", "box_and_tickets");
           // MYSTERY_BOX.log(player, EPIC_PET_BOX, reward);
            player.inventory().addOrBank(reward);
            int count = player.<Integer>getAttribOr(AttributeKey.EPIC_PET_MYSTERY_BOXES_OPENED,0) + 1;
            player.putAttrib(AttributeKey.EPIC_PET_MYSTERY_BOXES_OPENED, count);
            //The user box test doesn't yell.
            if(player.getUsername().equalsIgnoreCase("Box test")) {
                return;
            }
            String worldMessage = "<img=505><shad=0>[<col=" + Color.MEDRED.getColorValue() + ">Epic Pet Box</col>]</shad>:<col=AD800F> " + player.getUsername() + " received a <shad=0>" + reward.name() + "</shad>!";
            World.getWorld().sendWorldMessage(worldMessage);
        }
    }

    private static final MboxItem[] RARE = new MboxItem[] {
        new MboxItem(CustomItemIdentifiers.JAWA_PET),
        new MboxItem(CustomItemIdentifiers.ZRIAWK),
        new MboxItem(CustomItemIdentifiers.FAWKES),
        new MboxItem(CustomItemIdentifiers.NIFFLER),
        new MboxItem(CustomItemIdentifiers.WAMPA),
        new MboxItem(CustomItemIdentifiers.BABY_ARAGOG),
        new MboxItem(CustomItemIdentifiers.MINI_NECROMANCER),
        new MboxItem(CustomItemIdentifiers.PET_CORRUPTED_NECHRYARCH),
        new MboxItem(CustomItemIdentifiers.GRIM_REAPER_PET)
    };

    private static final MboxItem[] UNCOMMON = new MboxItem[] {
        new MboxItem(CustomItemIdentifiers.KERBEROS_PET),
        new MboxItem(CustomItemIdentifiers.SKORPIOS_PET),
        new MboxItem(CustomItemIdentifiers.ARACHNE_PET),
        new MboxItem(CustomItemIdentifiers.ARTIO_PET),
        new MboxItem(ItemIdentifiers.LITTLE_NIGHTMARE),
        new MboxItem(CustomItemIdentifiers.DEMENTOR_PET),
        new MboxItem(CustomItemIdentifiers.FENRIR_GREYBACK_JR),
        new MboxItem(CustomItemIdentifiers.FLUFFY_JR),
        new MboxItem(CustomItemIdentifiers.ANCIENT_KING_BLACK_DRAGON_PET),
        new MboxItem(CustomItemIdentifiers.ANCIENT_CHAOS_ELEMENTAL_PET),
        new MboxItem(CustomItemIdentifiers.ANCIENT_BARRELCHEST_PET),
        new MboxItem(CustomItemIdentifiers.FOUNDER_IMP),
        new MboxItem(CustomItemIdentifiers.BABY_LAVA_DRAGON),
        new MboxItem(CustomItemIdentifiers.JALTOK_JAD)
    };

    private static final MboxItem[] COMMON = new MboxItem[] {
        new MboxItem(ItemIdentifiers.TZREKZUK),
        new MboxItem(CustomItemIdentifiers.RING_OF_ELYSIAN),
        new MboxItem(CustomItemIdentifiers.BLOOD_MONEY_PET),
        new MboxItem(CustomItemIdentifiers.GENIE_PET),
        new MboxItem(CustomItemIdentifiers.DHAROK_PET),
        new MboxItem(CustomItemIdentifiers.PET_ZOMBIES_CHAMPION),
        new MboxItem(CustomItemIdentifiers.BABY_ABYSSAL_DEMON),
        new MboxItem(CustomItemIdentifiers.BABY_DARK_BEAST_EGG),
        new MboxItem(CustomItemIdentifiers.BABY_SQUIRT),
        new MboxItem(ItemIdentifiers.JALNIBREK),
        new MboxItem(CustomItemIdentifiers.CENTAUR_FEMALE),
        new MboxItem(CustomItemIdentifiers.CENTAUR_MALE)
    };
}
