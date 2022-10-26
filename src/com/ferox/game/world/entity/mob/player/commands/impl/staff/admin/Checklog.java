package com.ferox.game.world.entity.mob.player.commands.impl.staff.admin;

import com.ferox.game.content.collection_logs.Collection;
import com.ferox.game.content.collection_logs.CollectionLog;
import com.ferox.game.content.collection_logs.LogType;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class Checklog implements Command {
    private static final Logger logger = LogManager.getLogger(Checklog.class);
    @Override
    public void execute(Player player, String command, String[] parts) {
        int globaltotal = 0;
        int globaltocollect = 0;
        List<Collection> log = Collection.getAsList(LogType.KEYS);
        final int total = log.size();
        int totaltocollect = 0;
        int amountwehavecollected = 0;
Collection col;
        for (int i = 0; i < total; i++){
            col = log.get(i);
            totaltocollect+= col.totalCollectables();
            amountwehavecollected+= player.getCollectionLog().totalObtained(log.get(i));
        }
        player.message("total collected in keys: "+amountwehavecollected+" out of "+totaltocollect);
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isAdminOrGreater(player));
    }

}
