package com.ferox.game.content.packet_actions.interactions.items;

import com.ferox.game.content.consumables.potions.Potions;
import com.ferox.game.content.items.PoisoningStuff;
import com.ferox.game.content.items.combine.*;
import com.ferox.game.content.skill.impl.firemaking.LogLighting;
import com.ferox.game.content.skill.impl.herblore.HerbTar;
import com.ferox.game.content.skill.impl.herblore.PestleAndMortar;
import com.ferox.game.content.skill.impl.herblore.PotionBrewing;
import com.ferox.game.content.skill.impl.herblore.SuperCombatPotions;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.npc.pets.PetPaint;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteractionManager;

import static com.ferox.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * juni 15, 2020
 */
public class ItemOnItem {

    public static int slotOf(Player player, int item) {

        Item from = player.getAttrib(AttributeKey.FROM_ITEM);
        Item to = player.getAttrib(AttributeKey.TO_ITEM);
        if (from == null || to == null)
            return -1;

        if (from.getId() == item)
            return player.getAttrib(AttributeKey.ITEM_SLOT);
        if (to.getId() == item)
            return player.getAttrib(AttributeKey.ALT_ITEM_SLOT);

        return -1;
    }

    public static void itemOnItem(Player player, Item use, Item with) {

        if (PacketInteractionManager.checkItemOnItemInteraction(player, use, with)) {
            return;
        }
        if(LogLighting.onItemOnItem(player, use, with)) {
            return;
        }
        if(player.getMysteryBox().onItemonItem(use, with)) {
            return;
        }

        if(PetPaint.paintPet(player, use, with)) {
            return;
        }
        if(PoisoningStuff.poisonwep(player, use, with)) {
            return;
        }

        if(PotionBrewing.onItemOnItem(player, use, with)) {
            return;
        }

        if(PestleAndMortar.onItemOnItem(player, use, with)) {
            return;
        }
        if(HerbTar.onItemOnItem(player, use, with)) {
            return;
        }
        if(Potions.onItemOnItem(player, use, with)) {
            return;
        }
        if(SuperCombatPotions.makePotion(player, use, with)) {
            return;
        }

        if(player.getRunePouch().itemOnItem(use, with)) {
            return;
        }

        if (player.getLootingBag().itemOnItem(use, with)) {
            return;
        }

        if ((use.getId() == VOLATILE_ORB || with.getId() == VOLATILE_ORB) && (use.getId() == NIGHTMARE_STAFF || with.getId() == NIGHTMARE_STAFF)) {
            player.getDialogueManager().start(new VolatileNightmareStaff());
            return;
        }

        if ((use.getId() == ELDRITCH_ORB || with.getId() == ELDRITCH_ORB) && (use.getId() == NIGHTMARE_STAFF || with.getId() == NIGHTMARE_STAFF)) {
            player.getDialogueManager().start(new EldritchNightmareStaff());
            return;
        }

        if ((use.getId() == HARMONISED_ORB || with.getId() == HARMONISED_ORB) && (use.getId() == NIGHTMARE_STAFF || with.getId() == NIGHTMARE_STAFF)) {
            player.getDialogueManager().start(new HarmonisedNightmareStaff());
            return;
        }

        player.message("Nothing interesting happens.");
    }
}
