package com.ferox;

import com.ferox.game.GameBuilder;
import com.ferox.game.content.announcements.dyk.DidYouKnowTask;
import com.ferox.game.content.areas.wilderness.content.boss_event.WildernessBossEvent;
import com.ferox.game.content.areas.wilderness.content.key.WildernessKeyPlugin;
import com.ferox.game.content.areas.wilderness.content.todays_top_pkers.TopPkers;

import com.ferox.game.task.TaskManager;
import com.ferox.game.task.impl.WeeklyRewards;
import com.ferox.game.task.impl.dailyRewards;
import com.ferox.game.world.entity.combat.method.impl.npcs.godwars.GwdLogic;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.ground.GlobalDropsHandler;
import com.ferox.net.HostBlacklist;
import com.ferox.net.NetworkBuilder;
import com.ferox.util.DoorPairer;

/**
 * The bootstrap that will prepare the game, network, and various utilities.
 * This class effectively enables Eldritch to be put online.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class Bootstrap {

    /**
     * The port that the {@link NetworkBuilder} will listen for connections on.
     */
    private final int port;

    /**
     * The network builder that will initialize the core components of the
     * network.
     */
    private final NetworkBuilder networkBuilder = new NetworkBuilder();

    /**
     * The game builder that will initialize the core components of the game.
     */
    private final GameBuilder gameBuilder = new GameBuilder();

    /**
     * Creates a new {@link Bootstrap}.
     *
     * @param port
     *            the port that the network handler will listen on.
     */
    protected Bootstrap(int port) {
        this.port = port;
    }

    /**
     * Binds the core of the server together and puts righteouspk online.
     *
     * @throws Exception
     *             if any errors occur while puting the server online.
     */
    public void bind() throws Exception {
        gameBuilder.initialize();
        networkBuilder.initialize(port);
        GwdLogic.onServerStart();
        GlobalDropsHandler.initialize();
     //   DoorPairer.main();
        HostBlacklist.loadBlacklist();
        TaskManager.submit(new GlobalDropsHandler());

        if (GameServer.properties().enableDidYouKnowMessages) {
            TaskManager.submit(new DidYouKnowTask());
        }
        TaskManager.submit(new WeeklyRewards());
        TaskManager.submit(new dailyRewards());
        if (GameServer.properties().enableWildernessBossEvents /*&& GameServer.properties().pvpMode*/) {// Events only on PvP.
            WildernessBossEvent.onServerStart();
            WildernessKeyPlugin.onServerStart();
            TopPkers.SINGLETON.init();
        }
        Item.onServerStart();
    }
}
