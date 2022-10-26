package com.ferox.game.content.items.combine;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.combat.method.impl.npcs.bosses.corruptedhunleff.CorruptedHunleff;
import com.ferox.game.world.entity.combat.method.impl.npcs.bosses.corruptedhunleff.CorruptedHunleffCombatStrategy;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;

import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.NpcIdentifiers.CORRUPTED_HUNLLEF;

public class CorruptTotem extends PacketInteraction {

    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == CORRUPT_TOTEM_BASE || usedWith.getId() == CORRUPT_TOTEM_BASE) && (use.getId() == CORRUPT_TOTEM_MIDDLE || usedWith.getId() == CORRUPT_TOTEM_MIDDLE)) {
            if(player.inventory().containsAll(CORRUPT_TOTEM_BASE, CORRUPT_TOTEM_MIDDLE, CORRUPT_TOTEM_TOP)) {
                player.inventory().remove(CORRUPT_TOTEM_BASE);
                player.inventory().remove(CORRUPT_TOTEM_MIDDLE);
                player.inventory().remove(CORRUPT_TOTEM_TOP);
                player.inventory().add(new Item(CORRUPT_TOTEM));
                player.message(Color.PURPLE.wrap("You combined all the corrupt totem pieces to build a corrupt totem!"));
            }
            return true;
        }
        if ((use.getId() == CORRUPT_TOTEM_MIDDLE || usedWith.getId() == CORRUPT_TOTEM_MIDDLE) && (use.getId() == CORRUPT_TOTEM_BASE || usedWith.getId() == CORRUPT_TOTEM_BASE)) {
            if(player.inventory().containsAll(CORRUPT_TOTEM_BASE, CORRUPT_TOTEM_MIDDLE, CORRUPT_TOTEM_TOP)) {
                player.inventory().remove(CORRUPT_TOTEM_BASE);
                player.inventory().remove(CORRUPT_TOTEM_MIDDLE);
                player.inventory().remove(CORRUPT_TOTEM_TOP);
                player.inventory().add(new Item(CORRUPT_TOTEM));
                player.message(Color.PURPLE.wrap("You combined all the corrupt totem pieces to build a corrupt totem!"));
            }
            return true;
        }
        if ((use.getId() == CORRUPT_TOTEM_TOP || usedWith.getId() == CORRUPT_TOTEM_TOP) && (use.getId() == CORRUPT_TOTEM_BASE || usedWith.getId() == CORRUPT_TOTEM_BASE)) {
            if(player.inventory().containsAll(CORRUPT_TOTEM_BASE, CORRUPT_TOTEM_MIDDLE, CORRUPT_TOTEM_TOP)) {
                player.inventory().remove(CORRUPT_TOTEM_BASE);
                player.inventory().remove(CORRUPT_TOTEM_MIDDLE);
                player.inventory().remove(CORRUPT_TOTEM_TOP);
                player.inventory().add(new Item(CORRUPT_TOTEM));
                player.message(Color.PURPLE.wrap("You combined all the corrupt totem pieces to build a corrupt totem!"));
            }
            return true;
        }
        if ((use.getId() == CORRUPT_TOTEM_TOP || usedWith.getId() == CORRUPT_TOTEM_TOP) && (use.getId() == CORRUPT_TOTEM_MIDDLE || usedWith.getId() == CORRUPT_TOTEM_MIDDLE)) {
            if(player.inventory().containsAll(CORRUPT_TOTEM_BASE, CORRUPT_TOTEM_MIDDLE, CORRUPT_TOTEM_TOP)) {
                player.inventory().remove(CORRUPT_TOTEM_BASE);
                player.inventory().remove(CORRUPT_TOTEM_MIDDLE);
                player.inventory().remove(CORRUPT_TOTEM_TOP);
                player.inventory().add(new Item(CORRUPT_TOTEM));
                player.message(Color.PURPLE.wrap("You combined all the corrupt totem pieces to build a corrupt totem!"));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean handleItemOnObject(Player player, Item item, GameObject object) {
        if(item.getId() == CORRUPT_TOTEM && object.getId() == 31626) {
            player.optionsTitled("Sacrifice totem to fight the Corrupted Hunleff!", "Yes", "No", () -> {
                if(player.inventory().contains(CORRUPT_TOTEM)) {
                    player.inventory().remove(CORRUPT_TOTEM);
                    int level = player.getIndex() * 4;
                    player.teleport(new Tile(1679, 9888, level));
                    Npc corruptedHunleff = Npc.of(CORRUPTED_HUNLLEF, new Tile(1693, 9886, level));
                    corruptedHunleff.respawns(false);
                    World.getWorld().registerNpc(corruptedHunleff);
                }
            });
            return true;
        }
        return false;
    }
}
