package com.ferox.game.world.entity.mob.player.commands.impl.owner;

import com.ferox.GameServer;
import com.ferox.game.GameEngine;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.combat.skull.Skulling;
import com.ferox.game.world.entity.mob.player.GameMode;
import com.ferox.game.world.entity.mob.player.IronMode;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.entity.mob.player.rights.PlayerRights;
import com.ferox.game.world.entity.mob.player.save.PlayerSave;
import com.ferox.game.world.items.Item;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ferox.game.world.entity.AttributeKey.*;

/**
 * @author Patrick van Elderen | February, 21, 2021, 16:53
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class EcoResetCommand implements Command {

    public static class AtomicStorage {
        public AtomicInteger topPlayers = new AtomicInteger(0);
        public AtomicInteger toScanAmt = new AtomicInteger(0);
        public AtomicInteger scannedCount = new AtomicInteger(0);
        public ConcurrentLinkedQueue<Player> loaded = new ConcurrentLinkedQueue<>();
    }

    private static final Logger logger = LogManager.getLogger(EcoResetCommand.class);

    HashSet<String> checkedPlayers = new HashSet<>();

    private void submitOfflineScan(Player dev, final HashSet<String> checkedPlayers) {
        AtomicStorage storage = new AtomicStorage();
        GameEngine.getInstance().submitLowPriority(() -> {
            try (Stream<Path> walk = Files.walk(Paths.get("data", "saves", "characters"))) {
                List<String> result = walk.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
                ArrayList<Player> offlineToScan = new ArrayList<>(result.size());
                for (String p2name : result) {
                    String rsn = p2name.substring(p2name.lastIndexOf("\\") + 1, p2name.lastIndexOf("."));
                    p2name = Utils.formatText(rsn);
                    //If we checked the online player, skip them.
                    if (checkedPlayers.contains(p2name)) {
                        continue;
                    }
                    //We probably don't need to add them to checked players, but lets do it anyway.
                    checkedPlayers.add(p2name);
                    Player opp = new Player();
                    opp.setUsername(Utils.formatText(p2name));
                    offlineToScan.add(opp);
                }

                dev.getPacketSender().sendLogout();

                storage.toScanAmt.set(offlineToScan.size());

                for (Player player : offlineToScan) {
                    GameEngine.getInstance().submitLowPriority(() -> {
                        try {
                            if (PlayerSave.loadOfflineWithoutPassword(player)) {
                                storage.loaded.add(player);
                                //Reset the account status to brand new
                                player.mode(GameMode.INSTANT_PKER);

                                if (player.ironMode() != IronMode.NONE) {
                                    //De rank all irons
                                    player.setPlayerRights(PlayerRights.PLAYER);
                                }
                                //Deiron
                                player.ironMode(IronMode.NONE);

                                //Make the accounts a new account
                                player.putAttrib(AttributeKey.NEW_ACCOUNT, true);
                                player.putAttrib(IS_RUNNING, false);
                                player.putAttrib(RUN_ENERGY, 100.0);
                                //place player at edge
                                player.setTile(GameServer.properties().defaultTile.copy());

                                //Clear content
                                Arrays.fill(player.getPresets(), null);
                                player.achievements().clear();
                                player.getHostAddressMap().clear();
                                player.getInsuredPets().clear();
                                player.getSlayerRewards().getBlocked().clear();
                                player.getSlayerRewards().getUnlocks().clear();
                                player.getSlayerRewards().getExtendable().clear();
                                player.getRecentKills().clear();
                                player.getRecentTeleports().clear();
                                player.getFavorites().clear();
                                player.getBossTimers().getTimes().clear();
                                player.getCollectionLog().collectionLog.clear();
                                player.getRelations().getFriendList().clear();
                                player.getRelations().getIgnoreList().clear();

                                //Unskull
                                Skulling.unskull(player);

                                //Clear attributes
                                AttributeKey[] keysToSkip = {RUN_ENERGY, NEW_ACCOUNT, GAME_TIME, ACCOUNT_PIN, TOTAL_PAYMENT_AMOUNT, MEMBER_UNLOCKED, SUPER_MEMBER_UNLOCKED, ELITE_MEMBER_UNLOCKED, EXTREME_MEMBER_UNLOCKED, LEGENDARY_MEMBER_UNLOCKED, VIP_UNLOCKED, SPONSOR_UNLOCKED};
                                for(AttributeKey key : AttributeKey.values()) {
                                    if (Arrays.stream(keysToSkip).anyMatch(k -> k == key)) {
                                        continue;
                                    }
                                    player.clearAttrib(key);
                                }

                                //Clear bank
                                player.getBank().clear(false);
                                player.getBank().tabAmounts = new int[10];
                                player.getBank().placeHolderAmount = 0;
                                //Clear inventory
                                player.inventory().clear(false);
                                //Clear equipment
                                player.getEquipment().clear(false);
                                //Clear rune pouch
                                player.getRunePouch().clear(false);
                                //Clear looting bag
                                player.getLootingBag().clear(false);
                                //Clear the niffler
                                player.putAttrib(NIFFLER_ITEMS_STORED, new ArrayList<Item>());
                                //Clear luzox coins cart
                                player.putAttrib(CART_ITEMS, new ArrayList<Item>());

                                PlayerSave.save(player);

                                // println every 20 files scanned for progress updates
                                int current = storage.scannedCount.addAndGet(1);
                                int goal = storage.toScanAmt.get();

                                if (current % 20 == 0) {
                                    // every 20
                                    logger.info("offline profile scanning {}/{}  ({}%) complete. {} remaining...", current, goal, (1d * current) / goal * 100, goal - current);
                                }

                                if (current == goal) {
                                    logger.info("scanning complete! saving info...");
                                }
                            } else {
                                dev.message("Something wrong went resetting the account for offline Player " + player.getUsername());
                                logger.error("Something wrong went resetting the account for offline Player " + player.getUsername());
                            }
                        } catch (Exception e) {
                            dev.message("Something wrong went resetting the account for offline Player " + player.getUsername());
                            logger.error("Something wrong went resetting the account for offline Player " + player.getUsername());
                            logger.catching(e);
                        }
                    });
                }
            } catch (Exception e) {
                logger.catching(e);
            }
        });
    }

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.requestLogout();
        Chain.bound(null).runFn(5, () -> {
            checkedPlayers.clear();
            submitOfflineScan(player, new HashSet<>(checkedPlayers));
        });
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isOwner(player);
    }
}
