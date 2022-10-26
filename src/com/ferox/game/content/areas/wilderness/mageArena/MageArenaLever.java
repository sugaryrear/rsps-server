package com.ferox.game.content.areas.wilderness.mageArena;

import com.ferox.game.content.areas.wilderness.content.key.WildernessKeyPlugin;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.chainedwork.Chain;
import com.ferox.util.timers.TimerKey;

public class MageArenaLever extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if (option == 1) {
            //Into the arena
            if (obj.getId() == 9706) {
                player.faceObj(obj);
                //Check to see if the player is teleblocked
                if (player.getTimers().has(TimerKey.TELEBLOCK) || player.getTimers().has(TimerKey.SPECIAL_TELEBLOCK)) {
                    player.teleblockMessage();
                    return true;
                }

                if (WildernessKeyPlugin.hasKey(player) && WildernessArea.inWilderness(player.tile())) {
                    player.message("You cannot teleport outside the Wilderness with the Wilderness key.");
                    return true;
                }

                player.lock();
                Chain.bound(null).runFn(1, () -> {
                    player.animate(2710);
                    player.message("You pull the lever...");
                }).then(2, () -> {
                    player.animate(714);
                    player.graphic(111, 110, 0);
                }).then(4, () -> {
                    player.teleport(new Tile(3105, 3951, 0));
                    player.animate(-1);
                    player.unlock();
                    player.putAttrib(AttributeKey.MAGEBANK_MAGIC_ONLY, true);
                    player.message("...and get teleported into the arena!");
                });
                return true;
            }
            //Inside magebank.. to outside
            if (obj.getId() == 9707) {
                //Check to see if the player is teleblocked
                if (!player.getTimers().has(TimerKey.TELEBLOCK) || !player.getTimers().has(TimerKey.SPECIAL_TELEBLOCK)) {
                    player.lock();
                    player.faceObj(obj);
                    Chain.bound(null).runFn(1, () -> {
                        player.animate(2710);
                        player.message("You pull the lever...");
                    }).then(2, () -> {
                        player.animate(714);
                        player.graphic(111, 110, 0);
                    }).then(4, () -> {
                        player.teleport(new Tile(3105, 3956, 0));
                        player.animate(-1);
                        player.unlock();
                        player.putAttrib(AttributeKey.MAGEBANK_MAGIC_ONLY, false);
                        player.message("...and get teleported out of the arena!");
                    });
                } else {
                    player.teleblockMessage();
                }
                return true;
            }
        }
        return false;
    }
}
