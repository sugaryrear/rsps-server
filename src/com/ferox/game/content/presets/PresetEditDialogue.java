package com.ferox.game.content.presets;

import com.ferox.game.content.syntax.impl.ChangePresetName;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.items.Item;
import com.ferox.util.Color;
import com.ferox.util.Utils;

import java.util.Arrays;
import java.util.Objects;

import static com.ferox.game.content.presets.PresetManager.ILLEGAL_ITEMS;

/**
 * Description
 *
 * @author Patrick van Elderen | dinsdag 21 mei 2019 (CEST) : 10:14
 * @see <a href="https://github.com/Patrick9-10-1995">Github profile</a>
 */
public class PresetEditDialogue extends Dialogue {

    @Override
    protected void start(Object... parameters) {
        send(DialogueType.OPTION, "Select option", "Change name", "Copy current setup", "Delete preset", "Cancel");
        setPhase(0);
    }

    @Override
    public void select(int option) {
        if (isPhase(0)) {
            setPhase(1);
            final int presetIndex = player.getPresetIndex();
            stop();
            switch (option) {
                case 1 -> {
                    player.setEnterSyntax(new ChangePresetName(presetIndex));
                    player.getPacketSender().sendEnterInputPrompt("Enter a new name for your preset below.");
                }
                case 2 -> {
                    Item[] inventory = player.inventory().copyValidItemsArray();
                    for (Item item : inventory) {
                        if (Arrays.stream(ILLEGAL_ITEMS).anyMatch(id -> id == item.getId())) {
                            player.message(Color.RED.wrap("You cannot create presets which contain illegal items."));
                            return;
                        }
                    }
                    Item[] equipment = player.getEquipment().copyValidItemsArray();
                    for (Item item : equipment) {
                        if (Arrays.stream(ILLEGAL_ITEMS).anyMatch(id -> id == item.getId())) {
                            player.message(Color.RED.wrap("You cannot create presets which contain illegal items."));
                            return;
                        }
                    }
                    for (Item t : Utils.concat(inventory, equipment)) {
                        if (t.noted()) {
                            player.message("You cannot create presets which contain noted items.");
                            return;
                        }
                    }
                    player.getPresets()[presetIndex].setInventory(inventory);
                    player.getPresets()[presetIndex].setEquipment(equipment);
                    player.getPresets()[presetIndex].setRunePouch(player.getRunePouch().stream().filter(Objects::nonNull).map(Item::copy).toArray(Item[]::new));

                    //Update stats
                    int[] stats = new int[7];
                    for (int i = 0; i < stats.length; i++) {
                        stats[i] = player.skills().xpLevel(i);
                    }

                    // Update stats
                    player.getPresets()[presetIndex].setStats(stats);
                    player.getPresets()[presetIndex].setSpellbook(player.getSpellbook());

                    player.message("You have updated your preset.");
                    player.getPresetManager().open();
                }
                case 3 -> {
                    player.getPresets()[presetIndex] = null;
                    player.setCurrentPreset(null);
                    player.setLastPreset(null);
                    player.message("The preset has been deleted.");
                    player.getPresetManager().open();
                }
                case 4 -> stop();
            }
        }
    }
}
