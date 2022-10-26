package com.ferox.game.content.areas.lumbridge;

import com.ferox.game.content.tradingpost.TradingPost;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.movement.MovementQueue;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.chainedwork.Chain;

import static com.ferox.util.ObjectIdentifiers.DARK_HOLE;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 19, 2020
 */
public class Lumbridge extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            if(obj.getId() == DARK_HOLE) {
                player.teleport(new Tile(3184, 9549, 0));
                return true;
            }
            if(obj.getId() == 14880) {//trapdoor to cooks
                player.teleport(new Tile(3209, 9617, 0));
                return true;
            }
            if(obj.getId() == 12309) {//culinaromancer's chest
                Tile chesttile = new Tile(3218,9623);  player.getBank().open();
//                Chain.bound(player).name("CulinaromancersChestTask").waitForTile(chesttile, () -> {
//
//
//                });

                return true;
            }
        }
        if(option == 3){
            if(obj.getId() == 12309) {//culinaromancer's chest

                TradingPost.open(player);
                return true;
            }
        }
        if(option == 4){
            if(obj.getId() == 12309) {//culinaromancer's chest - buy food
                World.getWorld().shop(67).open(player);
                return true;
            }
            if(obj.getId() == 9623) {//culinaromancer's chest - buy items
                Tile chesttile = new Tile(3218,9623);
                if (!player.tile().equals(chesttile)) {
                    player.getMovementQueue().interpolate(chesttile, MovementQueue.StepType.FORCED_WALK);

                }

                Chain.bound(player).name("CulinaromancersChestTask").waitForTile(chesttile, () -> {
                    World.getWorld().shop(68).open(player);

                });
                return true;
            }
        }
        return false;
    }
}
