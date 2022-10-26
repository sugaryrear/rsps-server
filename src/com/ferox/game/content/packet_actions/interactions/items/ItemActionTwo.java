package com.ferox.game.content.packet_actions.interactions.items;

import com.ferox.game.content.consumables.potions.Potions;
import com.ferox.game.content.items.BraceletOfEthereum;
import com.ferox.game.content.items.combine.EldritchNightmareStaff;
import com.ferox.game.content.items.combine.HarmonisedNightmareStaff;
import com.ferox.game.content.items.combine.VolatileNightmareStaff;
import com.ferox.game.content.items.teleport.ArdyCape;
import com.ferox.game.content.skill.impl.slayer.content.SlayerRing;
import com.ferox.game.world.entity.mob.npc.pets.PetPaint;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteractionManager;

import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * mei 08, 2020
 */
public class ItemActionTwo {

    public static void click(Player player, Item item) {
        int id = item.getId();

        PetPaint.wipePaint(player, item);
        ArdyCape.onItemOption2(player, item);

        if (PacketInteractionManager.checkItemInteraction(player, item, 2)) {
            return;
        }

        if (player.getMysteryBox().open(item)) {
            return;
        }

        if(Potions.onItemOption2(player, item)) {
            return;
        }

        if (VolatileNightmareStaff.dismantle(player, item)) {
            return;
        }
        if(BraceletOfEthereum.onItemOption2(player,item)){
            return;
        }
        if (EldritchNightmareStaff.dismantle(player, item)) {
            return;
        }

        if (HarmonisedNightmareStaff.dismantle(player, item)) {
            return;
        }

        if(SlayerRing.onItemOption2(player, item)) {
            return;
        }

        switch (id) {
            case RUNE_POUCH, RUNE_POUCH_I -> player.getRunePouch().empty();
            case LOOTING_BAG, LOOTING_BAG_22586 -> player.getLootingBag().setSettings();
        }
    }
}
