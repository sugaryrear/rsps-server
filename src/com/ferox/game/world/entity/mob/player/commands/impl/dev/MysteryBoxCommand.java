package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.game.content.items.mystery_box.MboxItem;
import com.ferox.game.content.items.mystery_box.MysteryBox;
import com.ferox.game.content.items.mystery_box.impl.EpicPetMysteryBox;
import com.ferox.game.content.items.mystery_box.impl.GrandMysteryBox;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.items.Item;

import java.util.Optional;

import static com.ferox.util.CustomItemIdentifiers.*;

public class MysteryBoxCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (parts.length != 3) {
            player.message("Invalid use of command.");
            player.message("Use: ::box 100 weapon_box");
            return;
        }

        int amount = Integer.parseInt(parts[1]);
        String box_name = parts[2];

        switch (box_name) {
            case "armour_box":
                for (int i = 0; i < amount; i++) {
                    Optional<MysteryBox> mBox = MysteryBox.getMysteryBox(ARMOUR_MYSTERY_BOX);
                    if (mBox.isPresent()) {
                        player.getMysteryBox().box = mBox.get();
                        MboxItem mboxItem = mBox.get().rollReward(false).copy();
                        player.getMysteryBox().reward = mboxItem;
                        player.getMysteryBox().broadcast = mboxItem.broadcastItem;
                        player.getMysteryBox().reward();
                    }
                }
                break;
            case "donator_box":
                for (int i = 0; i < amount; i++) {
                    Optional<MysteryBox> mBox = MysteryBox.getMysteryBox(DONATOR_MYSTERY_BOX);
                    if (mBox.isPresent()) {
                        player.getMysteryBox().box = mBox.get();
                        MboxItem mboxItem = mBox.get().rollReward(false).copy();
                        player.getMysteryBox().reward = mboxItem;
                        player.getMysteryBox().broadcast = mboxItem.broadcastItem;
                        player.getMysteryBox().reward();
                    }
                }
                break;
            case "grand_box":
                player.inventory().add(new Item(GRAND_MYSTERY_BOX, amount));
                for (int i = 0; i < amount; i++) {
                    GrandMysteryBox.reward(player);
                }
                break;
            case "epic_pet_box":
                player.inventory().add(new Item(EPIC_PET_BOX, amount));
                for (int i = 0; i < amount; i++) {
                    EpicPetMysteryBox.open(player);
                }
                break;
            case "legendary_box":
                for (int i = 0; i < amount; i++) {
                    Optional<MysteryBox> mBox = MysteryBox.getMysteryBox(LEGENDARY_MYSTERY_BOX);
                    if (mBox.isPresent()) {
                        player.getMysteryBox().box = mBox.get();
                        MboxItem mboxItem = mBox.get().rollReward(false).copy();
                        player.getMysteryBox().reward = mboxItem;
                        player.getMysteryBox().broadcast = mboxItem.broadcastItem;
                        player.getMysteryBox().reward();
                    }
                }
                break;
            case "weapon_box":
                for (int i = 0; i < amount; i++) {
                    Optional<MysteryBox> mBox = MysteryBox.getMysteryBox(WEAPON_MYSTERY_BOX);
                    if (mBox.isPresent()) {
                        player.getMysteryBox().box = mBox.get();
                        MboxItem mboxItem = mBox.get().rollReward(false).copy();
                        player.getMysteryBox().reward = mboxItem;
                        player.getMysteryBox().broadcast = mboxItem.broadcastItem;
                        player.getMysteryBox().reward();
                    }
                }
                break;
        }
        player.message("You have opened "+amount+" "+box_name.replaceAll("_", " ")+"'s.");
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }
}
