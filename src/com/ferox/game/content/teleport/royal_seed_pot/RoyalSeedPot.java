package com.ferox.game.content.teleport.royal_seed_pot;

import com.ferox.GameServer;
import com.ferox.game.content.teleport.TeleportType;
import com.ferox.game.content.teleport.Teleports;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.chainedwork.Chain;
import com.ferox.util.timers.TimerKey;

public class RoyalSeedPot extends PacketInteraction {

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 1) {
            if(item.getId() == ItemIdentifiers.ROYAL_SEED_POD) {
                player.stopActions(true);
                if (!Teleports.canTeleport(player, true, TeleportType.ABOVE_20_WILD))
                    return true;
                player.graphic(767);
                player.animate(4544);
                player.lockNoDamage();
                Chain.bound(null).runFn(3, () -> player.looks().transmog(716)).then(1, () -> player.teleport(3088,3504)).then(2, () -> player.graphic(769)).then(2, () -> {
                    player.looks().transmog(-1);
                    player.animate(-1);
                    player.getTimers().cancel(TimerKey.FROZEN);
                    player.getTimers().cancel(TimerKey.REFREEZE);
                    player.unlock();
                });
                return true;
            }
        }
        return false;
    }

}
