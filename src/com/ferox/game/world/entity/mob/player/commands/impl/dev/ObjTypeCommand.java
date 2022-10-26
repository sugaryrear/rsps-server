package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.object.GameObject;

public class ObjTypeCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
     //   int type = Integer.parseInt(parts[1]);
       // player.getPacketSender().sendObject(new GameObject(Integer.parseInt(parts[1]), player.tile().copy(), type));
        int id = Integer.parseInt(parts[1]);
        int face = Integer.parseInt(parts[2]);
        player.getPacketSender().sendObject(new GameObject(id, player.tile().copy(),0,face));

    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
