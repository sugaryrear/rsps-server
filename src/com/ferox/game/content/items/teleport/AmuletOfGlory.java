package com.ferox.game.content.items.teleport;

import com.ferox.GameServer;
import com.ferox.game.content.items.PoisoningStuff;
import com.ferox.game.content.teleport.TeleportType;
import com.ferox.game.content.teleport.Teleports;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static com.ferox.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | December, 28, 2020, 13:48
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class AmuletOfGlory extends PacketInteraction {
    private enum GloryAmulet {
        AMULET_OF_GLORY6(11978,11976, 5),
        AMULET_OF_GLORY5(11976, 1712, 4 ),
        AMULET_OF_GLORY4(1712,1710,3 ),
        AMULET_OF_GLORY3(1710,1708,2),
        AMULET_OF_GLORY2(1708,1706,1),
        AMULET_OF_GLORY1(1706,1704,0 );

        private final int itemId;
        private final int replacementid;
        private final int chargesleft;

        GloryAmulet(int itemId, int replacementid, int chargesleft) {
            this.itemId = itemId;
            this.replacementid = replacementid;
            this.chargesleft = chargesleft;

        }

        static Map<Integer, GloryAmulet> glories = new HashMap<>();

        static {
            for (GloryAmulet amulets : GloryAmulet.values()) {
                glories.put(amulets.itemId, amulets);
            }
        }
    }

    @Override
    public boolean handleEquipmentAction(Player player, Item item, int slot) {
        GloryAmulet glory = GloryAmulet.glories.get(item.getId());
        if (glory != null) {
            handleglory(player, glory.itemId,false);
            return true;
        }
        return false;
    }
    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 2) {
            GloryAmulet glory = GloryAmulet.glories.get(item.getId());
            if (glory != null) {
                handleglory(player, glory.itemId,true);
                return true;
            }
        }
        return false;
    }
    public void handleremovingglory(Player player, int itemId, Tile teleporttile, boolean frominventory){
        GloryAmulet glory = GloryAmulet.glories.get(itemId);
        if (glory != null) {
            if(frominventory)
            player.inventory().replace(glory.itemId, glory.replacementid, true);
                else
                player.getEquipment().set(EquipSlot.AMULET,new Item(glory.replacementid), true); // add new to equip. use SET instead of ADD to use special equip index.
        player.message("@mag@Your amulet of glory has "+(glory.chargesleft > 0 ? glory.chargesleft : "no" )+" charges left.");
        }
        Teleports.basicTeleport(player, teleporttile);
    }
    public void handleglory(Player player, int itemId, Boolean frominventory) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Edgeville", "Karamja", "Draynor", "Al-Kharid");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if (option == 1) {
                    Tile teleporttile = new Tile(3088, 3493);
                    if (!Teleports.canTeleport(player,true, TeleportType.ABOVE_20_WILD)) {
                        stop();
                        return;
                    }
                    handleremovingglory(player,itemId, teleporttile, frominventory);
                } else if (option == 2) {
                    Tile teleporttile = new Tile(2925,3173);
                    if (!Teleports.canTeleport(player,true, TeleportType.ABOVE_20_WILD)) {
                        stop();
                        return;
                    }
                    handleremovingglory(player,itemId, teleporttile, frominventory);
                } else if (option == 3) {
                    Tile teleporttile = new Tile(3079,3250);
                    if (!Teleports.canTeleport(player,true, TeleportType.ABOVE_20_WILD)) {
                        stop();
                        return;
                    }
                    handleremovingglory(player,itemId, teleporttile, frominventory);
                } else if (option == 4) {
                    Tile teleporttile = new Tile(3293,3176);
                    if (!Teleports.canTeleport(player,true, TeleportType.ABOVE_20_WILD)) {
                        stop();
                        return;
                    }
                    handleremovingglory(player,itemId, teleporttile, frominventory);
                }
            }
        });
    }
}
