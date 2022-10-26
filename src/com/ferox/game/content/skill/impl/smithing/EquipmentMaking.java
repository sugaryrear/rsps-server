package com.ferox.game.content.skill.impl.smithing;

import com.ferox.game.content.skill.ItemCreationSkillable;
import com.ferox.game.content.skill.Skillable;
import com.ferox.game.world.entity.masks.animations.Animation;
import com.ferox.game.world.entity.masks.animations.AnimationLoop;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.RequiredItem;
import com.ferox.util.ItemIdentifiers;

import java.util.Arrays;
import java.util.Optional;

/**
 * Handles making equipment from bars.
 * @author Professor Oak
 */
public class EquipmentMaking {

    /**
     * The interface used for creating equipment using the
     * Smithing skill.
     */
    public static final int EQUIPMENT_CREATION_INTERFACE_ID = 994;

    /**
     * The interface ids used for selecting an item to create in the
     * {@code EQUIPMENT_CREATION_INTERFACE_ID}.
     */
    public static final int EQUIPMENT_CREATION_COLUMN_1 = 1119;
    public static final int EQUIPMENT_CREATION_COLUMN_2 = 1120;
    public static final int EQUIPMENT_CREATION_COLUMN_3 = 1121;
    public static final int EQUIPMENT_CREATION_COLUMN_4 = 1122;
    public static final int EQUIPMENT_CREATION_COLUMN_5 = 1123;

    /**
     * This method is triggered when a player clicks
     * on an anvil in the game.
     *
     * We will search for bars and then open the
     * corresponding interface if one was found.
     *
     * @param player
     */
    public static void openInterface(Player player) {
        //Search for bar..
        Optional<Bar> bar = Optional.empty();
        for (Bar b : Bar.values()) {
            if (!b.getItems().isPresent()) {
                continue;
            }
            if (player.inventory().contains(b.getBar())) {
                if (player.skills().levels()[Skills.SMITHING] >= b.getLevelReq()) {
                    bar = Optional.of(b);
                }
            }
        }

        //Did we find a bar in the player's inventory?
        if (bar.isPresent()) {
            //Go through the bar's items..
            for (SmithableEquipment b : bar.get().getItems().get()) {
                player.getPacketSender().sendItemOnInterfaceSlot(b.getItemFrame(), b.getItemId(), b.getAmount(), b.getItemSlot());

                int bars = player.inventory().count(b.getBarId());
                int smithLevel = player.skills().levels()[Skills.SMITHING];
                String name = b.getName();
                boolean meetsRequirementsForOilLamp = true;
                player.getPacketSender().sendString(b.getNameFrame(),name);
                player.getPacketSender().sendConfig(210, bars);
                player.getPacketSender().sendConfig(211, smithLevel);
                player.getPacketSender().sendConfig(262, meetsRequirementsForOilLamp ? 1 : 0);
            }

            //Send interface..
            player.getInterfaceManager().open(EQUIPMENT_CREATION_INTERFACE_ID);
        } else {
            player.message("You don't have any bars in your inventory which can be used with your Smithing level.");
        }
    }

    /**
     * Attempts to initialize a new {@link Skillable
     * @param itemId
     * @param interfaceId
     * @param slot
     * @param amount
     */
    public static void initialize(Player player, int itemId, int interfaceId, int slot, int amount) {
        //First verify the item we're trying to make..
        for (SmithableEquipment smithable : SmithableEquipment.values()) {
            if (smithable.getItemId() == itemId && smithable.getItemFrame() == interfaceId
                && smithable.getItemSlot() == slot) {
                //Start making items..
                player.skills().startSkillable(new ItemCreationSkillable(Arrays.asList(new RequiredItem(new Item(ItemIdentifiers.HAMMER)), new RequiredItem(new Item(smithable.getBarId(), smithable.getBarsRequired()), true)),
                    new Item(smithable.getItemId(), smithable.getAmount()), amount, Optional.of(new AnimationLoop(new Animation(898), 3)), smithable.getRequiredLevel(), smithable.getExperience(), Skills.SMITHING));
                break;
            }
        }
    }
}
