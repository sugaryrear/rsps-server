package com.ferox.game.world.entity.combat.method.impl.npcs.godwars.zaros;

import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.player.IronMode;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.ground.GroundItem;
import com.ferox.game.world.items.ground.GroundItemHandler;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ferox.util.ItemIdentifiers.SARADOMIN_CAPE;

@SuppressWarnings("serial")
public class NexLostItems extends ArrayList<Item> {

    /**
     * The player that has lost items
     */
    private final Player player;

    /**
     * Creates a new class for managing lost items by a single player
     *
     * @param player the player who lost items
     */
    public NexLostItems(final Player player) {
        this.player = player;
    }
    int lostitemprice = 100_000;
    /**
     * Stores the players items into a list and deletes their items
     */
    public void store(List<Item> itemstostore) {
        addAll(itemstostore);
      //  player.message("size: "+size()+" ");

    }
public void giveItems() {
    for (Item item : this) {
        if (player.ironMode() == IronMode.ULTIMATE) {
            if (!player.inventory().add(item)) {
                player.message("<col=CC0000>1x " + Item.of(item.getId()).unnote().name()+" +  has been dropped on the ground.</col>");
                GroundItem groundItem = new GroundItem(new Item(item.getId(),item.getAmount()), player.tile(), player);
                groundItem.setState(GroundItem.State.SEEN_BY_OWNER);
                GroundItemHandler.createGroundItem(groundItem);
            }
        } else {

            player.getBank().depositFromNothing(new Item(item.getId(), item.getAmount()));
        }
    }
    player.message("Your lost items have been added to your bank.");
    clear();
}
public void whatDOIgot() {
        for ( Item item : this){
            player.message("item: "+Item.getItemName(item.getId()));
        }
    }
    public void retain() {
        if (size() < 1) {
            player.message("The chest does not contain any lost items from Nex.");
            return;
        }
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Purchase your lost items for " + Utils.insertCommasToNumber(String.valueOf(lostitemprice)) + " ?", "Yes", "What items are inside?");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if (option == 1) {
                    if (player.inventory().contains(995, lostitemprice)) {
                        player.inventory().remove(995, lostitemprice);
                        giveItems();
                        stop();
                    } else {
                        player.message("You don't have enough gold.");
                    }
                    stop();
                } else if (option == 2) {
                    whatDOIgot();
                    stop();
                }
            }
        });
    }

}
