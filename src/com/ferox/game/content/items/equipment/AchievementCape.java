package com.ferox.game.content.items.equipment;

import com.ferox.GameServer;
import com.ferox.game.content.teleport.TeleportType;
import com.ferox.game.content.teleport.Teleports;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.ItemIdentifiers;

/**
 * @author Patrick van Elderen | December, 28, 2020, 15:28
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class AchievementCape extends PacketInteraction {

    private void teleport(Player player) {
        Tile tile = GameServer.properties().defaultTile;

        if (Teleports.canTeleport(player,true, TeleportType.ABOVE_20_WILD)) {
            Teleports.basicTeleport(player, tile);
            player.message("You have been teleported to home.");
        }
    }

    private void untrim(Player player) {
        if(!player.inventory().contains(ItemIdentifiers.ACHIEVEMENT_DIARY_CAPE_T)) {
            return;
        }

        player.inventory().remove(new Item(ItemIdentifiers.ACHIEVEMENT_DIARY_CAPE_T),true);
        player.inventory().add(new Item(ItemIdentifiers.ACHIEVEMENT_DIARY_CAPE),true);
        player.message("You untrim your Achievement cape.");
    }

    private void trim(Player player) {
        if(!player.inventory().contains(ItemIdentifiers.ACHIEVEMENT_DIARY_CAPE)) {
            return;
        }

        player.inventory().remove(new Item(ItemIdentifiers.ACHIEVEMENT_DIARY_CAPE),true);
        player.inventory().add(new Item(ItemIdentifiers.ACHIEVEMENT_DIARY_CAPE_T),true);
        player.message("You trim your Achievement cape.");
    }

    @Override
    public boolean handleEquipmentAction(Player player, Item item, int slot) {
        if(item.getId() == ItemIdentifiers.ACHIEVEMENT_DIARY_CAPE_T || item.getId() == ItemIdentifiers.ACHIEVEMENT_DIARY_CAPE && slot == EquipSlot.CAPE) {
            teleport(player);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if (option == 2) {
            if(item.getId() == ItemIdentifiers.ACHIEVEMENT_DIARY_CAPE) {
                trim(player);
                return true;
            }

            if(item.getId() == ItemIdentifiers.ACHIEVEMENT_DIARY_CAPE_T) {
                untrim(player);
                return true;
            }
        }

        if (option == 3) {
            if(item.getId() == ItemIdentifiers.ACHIEVEMENT_DIARY_CAPE_T || item.getId() == ItemIdentifiers.ACHIEVEMENT_DIARY_CAPE) {
                teleport(player);
                return true;
            }
        }
        return false;
    }
}
