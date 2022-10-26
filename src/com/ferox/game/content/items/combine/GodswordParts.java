package com.ferox.game.content.items.combine;

import com.ferox.game.content.duel.Dueling;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.ItemIdentifiers.ANCIENT_HILT;

/**
 * @author Patrick van Elderen | March, 16, 2021, 14:33
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class GodswordParts extends PacketInteraction {

    private final static int SHARD1 = 11818;
    private final static int SHARD2 = 11820;
    private final static int SHARD3 = 11822;
    private final static int SHARD1_AND_2 = 11794;
    private final static int SHARD1_AND_3 = 11796;
    private final static int SHARD2_AND_3 = 11800;
    private final static int BLADE = 11798;
    private final static int SARA_HILT = 11814;
    private final static int ARMA_HILT = 11810;
    private final static int ZAMMY_HILT = 11816;
    private final static int BANDOS_HILT = 11812;
    private final static int AGS = 11802;
    private final static int BGS = 11804;
    private final static int SGS = 11806;
    private final static int ZGS = 11808;
    private final static int ANCIENT_GODSWORD = 26233;
    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == SHARD1 || usedWith.getId() == SHARD1) && (use.getId() == SHARD2 || usedWith.getId() == SHARD2)) {
            if (Dueling.screen_closed(player) && player.inventory().contains(SHARD1) && player.inventory().contains(SHARD2)) {
                player.inventory().remove(new Item(SHARD1), true);
                player.inventory().remove(new Item(SHARD2), true);
                player.inventory().add(new Item(SHARD1_AND_2), true);
            }
            return true;
        }
        if ((use.getId() == SHARD1 || usedWith.getId() == SHARD1) && (use.getId() == SHARD3 || usedWith.getId() == SHARD3)) {
            if (Dueling.screen_closed(player) && player.inventory().containsAll(SHARD1, SHARD3)) {
                player.inventory().remove(new Item(SHARD1), true);
                player.inventory().remove(new Item(SHARD3), true);
                player.inventory().add(new Item(SHARD1_AND_3), true);
            }
            return true;
        }
        if ((use.getId() == SHARD2 || usedWith.getId() == SHARD2) && (use.getId() == SHARD3 || usedWith.getId() == SHARD3)) {
            if (Dueling.screen_closed(player) && player.inventory().containsAll(SHARD2, SHARD3)) {
                player.inventory().remove(new Item(SHARD2), true);
                player.inventory().remove(new Item(SHARD3), true);
                player.inventory().add(new Item(SHARD2_AND_3), true);
            }
            return true;
        }
        if ((use.getId() == SHARD1 || usedWith.getId() == SHARD1) && (use.getId() == SHARD2_AND_3 || usedWith.getId() == SHARD2_AND_3)) {
            if (Dueling.screen_closed(player) && player.inventory().containsAll(SHARD1, SHARD2_AND_3)) {
                player.inventory().remove(new Item(SHARD1), true);
                player.inventory().remove(new Item(SHARD2_AND_3), true);
                player.inventory().add(new Item(BLADE), true);
            }
            return true;
        }
        if ((use.getId() == SHARD2 || usedWith.getId() == SHARD2) && (use.getId() == SHARD1_AND_3 || usedWith.getId() == SHARD1_AND_3)) {
            if (Dueling.screen_closed(player) && player.inventory().containsAll(SHARD2, SHARD1_AND_3)) {
                player.inventory().remove(new Item(SHARD2), true);
                player.inventory().remove(new Item(SHARD1_AND_3), true);
                player.inventory().add(new Item(BLADE), true);
            }
            return true;
        }
        if ((use.getId() == SHARD3 || usedWith.getId() == SHARD3) && (use.getId() == SHARD1_AND_2 || usedWith.getId() == SHARD1_AND_2)) {
            if (Dueling.screen_closed(player) && player.inventory().containsAll(SHARD3, SHARD1_AND_2)) {
                player.inventory().remove(new Item(SHARD3), true);
                player.inventory().remove(new Item(SHARD1_AND_2), true);
                player.inventory().add(new Item(BLADE), true);
            }
            return true;
        }
        if ((use.getId() == BLADE || usedWith.getId() == BLADE) && (use.getId() == SARA_HILT || usedWith.getId() == SARA_HILT)) {
            if (Dueling.screen_closed(player) && player.inventory().containsAll(BLADE, SARA_HILT)) {
                player.inventory().remove(new Item(BLADE), true);
                player.inventory().remove(new Item(SARA_HILT), true);
                player.inventory().add(new Item(SGS), true);
            }
            return true;
        }
        if ((use.getId() == BLADE || usedWith.getId() == BLADE) && (use.getId() == ARMA_HILT || usedWith.getId() == ARMA_HILT)) {
            if (Dueling.screen_closed(player) && player.inventory().containsAll(BLADE, ARMA_HILT)) {
                player.inventory().remove(new Item(BLADE), true);
                player.inventory().remove(new Item(ARMA_HILT), true);
                player.inventory().add(new Item(AGS), true);
            }
            return true;
        }
        if ((use.getId() == BLADE || usedWith.getId() == BLADE) && (use.getId() == ZAMMY_HILT || usedWith.getId() == ZAMMY_HILT)) {
            if (Dueling.screen_closed(player) && player.inventory().containsAll(BLADE, ZAMMY_HILT)) {
                player.inventory().remove(new Item(BLADE), true);
                player.inventory().remove(new Item(ZAMMY_HILT), true);
                player.inventory().add(new Item(ZGS), true);
            }
            return true;
        }
        if ((use.getId() == BLADE || usedWith.getId() == BLADE) && (use.getId() == BANDOS_HILT || usedWith.getId() == BANDOS_HILT)) {
            if (Dueling.screen_closed(player) && player.inventory().containsAll(BLADE, BANDOS_HILT)) {
                player.inventory().remove(new Item(BLADE), true);
                player.inventory().remove(new Item(BANDOS_HILT), true);
                player.inventory().add(new Item(BGS), true);
            }
            return true;
        }
        if ((use.getId() == BLADE || usedWith.getId() == BLADE) && (use.getId() == ANCIENT_HILT || usedWith.getId() == ANCIENT_HILT)) {
            if (Dueling.screen_closed(player) && player.inventory().containsAll(BLADE, ANCIENT_HILT)) {
                player.inventory().remove(new Item(BLADE), true);
                player.inventory().remove(new Item(ANCIENT_HILT), true);
                player.inventory().add(new Item(ANCIENT_GODSWORD), true);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 3) {
            if(item.getId() == AGS) {
                if (Dueling.screen_closed(player) && player.inventory().contains(AGS) && player.inventory().getFreeSlots() >= 2) {
                    player.inventory().remove(new Item(AGS), true);
                    player.inventory().add(new Item(ARMA_HILT), true);
                    player.inventory().add(new Item(BLADE), true);
                }
                return true;
            }
            if(item.getId() == BGS) {
                if (Dueling.screen_closed(player) && player.inventory().contains(BGS) && player.inventory().getFreeSlots() >= 2) {
                    player.inventory().remove(new Item(BGS), true);
                    player.inventory().add(new Item(BANDOS_HILT), true);
                    player.inventory().add(new Item(BLADE), true);
                }
                return true;
            }
            if(item.getId() == SGS) {
                if (Dueling.screen_closed(player) && player.inventory().contains(SGS) && player.inventory().getFreeSlots() >= 2) {
                    player.inventory().remove(new Item(SGS), true);
                    player.inventory().add(new Item(SARA_HILT), true);
                    player.inventory().add(new Item(BLADE), true);
                }
                return true;
            }
            if(item.getId() == ZGS) {
                if (Dueling.screen_closed(player) && player.inventory().contains(ZGS) && player.inventory().getFreeSlots() >= 2) {
                    player.inventory().remove(new Item(ZGS), true);
                    player.inventory().add(new Item(ZAMMY_HILT), true);
                    player.inventory().add(new Item(BLADE), true);
                }
                return true;
            }
            if(item.getId() == ANCIENT_GODSWORD) {
                if (Dueling.screen_closed(player) && player.inventory().contains(ANCIENT_GODSWORD) && player.inventory().getFreeSlots() >= 2) {
                    player.inventory().remove(new Item(ANCIENT_GODSWORD), true);
                    player.inventory().add(new Item(ANCIENT_HILT), true);
                    player.inventory().add(new Item(BLADE), true);
                }
                return true;
            }
        }
        return false;
    }
}
