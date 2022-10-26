package com.ferox.game.content.gambling.impl;

import com.ferox.game.content.gambling.Gamble;
import com.ferox.game.content.gambling.GambleState;
import com.ferox.game.content.gambling.GamblingSession;
import com.ferox.game.task.Task;
import com.ferox.game.task.TaskManager;
import com.ferox.game.world.entity.mob.movement.MovementQueue;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Utils;

import java.util.*;

public class FlowerPoker extends Gamble {

    //TODO can't spawn a set of players on a set of tiles that already being used to flower poker

    private final List<Tile> spawnTiles = Arrays.asList(
        new Tile(3047,3373,0),
        new Tile(3044,3373,0),
        new Tile(3041,3373,0),
        new Tile(3038,3373,0));

    @Override
    public String toString() {
        return "Flower Poker";
    }

    public FlowerPoker(Player host, Player opponent) {
        super(host, opponent);
    }

    @Override
    public void gamble() {
        host.lock();
        opponent.lock();
        //host.forceChat("LOCKED: "+host.locked());
        //opponent.forceChat("LOCKED: "+host.locked());
        host.getGamblingSession().flowers.clear();
        opponent.getGamblingSession().flowers.clear();
        host.getGamblingSession().gameFlowers.clear();
        opponent.getGamblingSession().gameFlowers.clear();

        for (int index = 0; index < 5; index++) {
            Flower hostFlower = Flower.flower();
            Flower opponentFlower = Flower.flower();
            while (hostFlower == Flower.BLACK || hostFlower == Flower.WHITE) {
                hostFlower = Flower.flower();
            }
            while (opponentFlower == Flower.BLACK || opponentFlower == Flower.WHITE) {
                opponentFlower = Flower.flower();
            }
            host.getGamblingSession().flowers.add(hostFlower);
            opponent.getGamblingSession().flowers.add(opponentFlower);
        }

        Ranking hostResult = getRank(host);
        Ranking opponentResult = getRank(opponent);

        TaskManager.submit(new Task("FlowerPokerTask",1, false) {
            int time = 0;
            @Override
            public void execute() {
                //host.forceChat("LOCKED: "+host.locked());
                //opponent.forceChat("LOCKED: "+host.locked());
                if(host.getGamblingSession().state() != GambleState.IN_PROGRESS) {
                    this.stop();
                    return;
                }

                switch (time) {
                    case 0 -> {
                        Player player = host;
                        Player op = opponent;
                        Tile spawn = Utils.randomElement(spawnTiles);
                        player.teleport(spawn);
                        op.teleport(spawn.copy().add(-1, 0));
                    }
                    case 1 -> {
                        plant(host, opponent);
                        plant(opponent, host);
                    }
                    case 25 -> {
                        host.forceChat(Utils.formatEnum(hostResult.name()));
                        opponent.forceChat(Utils.formatEnum(opponentResult.name()));
                    }
                    case 26 -> {
                        host.getGamblingSession().finish(GamblingSession.FLOWER_POKER_ID, host, opponent, hostResult.ordinal(), opponentResult.ordinal());
                        this.stop();
                    }
                }
                time++;
            }
        });
    }

    private enum Ranking {
        /**
         * Nothing
         */
        BUST,
        /**
         * Got 1 pair of the same flower
         */
        ONE_PAIR,
        /**
         * Got 2 pairs of the same flower
         */
        TWO_PAIR,
        /**
         * Got 3 of the same flower
         */
        THREE_OAK,
        /**
         * Got 3 of the same flower plus 2 other of the same flower
         */
        FULL_HOUSE,
        /**
         * Got 4 of the same flower
         */
        FOUR_OAK,
        /**
         * Got 5 of the same flower
         */
        FIVE_OAK,
    }

    private static Ranking getRank(Player player) {
        ArrayList<Flower> flowers = new ArrayList<>(player.getGamblingSession().flowers);
        Collections.sort(flowers);
        Map<Integer, Integer> pairs = getPairs(flowers);
        for (int i = 0; i < pairs.size(); i++) {
            if (pairs.get(i) == null) {
                continue;
            }
            if (pairs.get(i) == 5) {
                return Ranking.FIVE_OAK;
            }
        }
        if (pairs.size() == 2) {
            if ((pairs.get(0) == 3 && pairs.get(1) == 2)
                || (pairs.get(1) == 3 && pairs.get(0) == 2)) {
                return Ranking.FULL_HOUSE;
            }
        }
        int totalPairs = 0;
        for (int i = 0; i < pairs.size(); i++) {
            if (pairs.get(i) == null) {
                continue;
            }
            if (pairs.get(i) == 4) {
                return Ranking.FOUR_OAK;
            }
            if (pairs.get(i) == 3) {
                return Ranking.THREE_OAK;
            }
            if (pairs.get(i) == 2) {
                totalPairs++;
            }
        }
        if (totalPairs == 2) {
            return Ranking.TWO_PAIR;
        }
        if (totalPairs == 1) {
            return Ranking.ONE_PAIR;
        }
        return Ranking.BUST;
    }

    private static Map<Integer, Integer> getPairs(ArrayList<Flower> list) {
        Map<Integer, Integer> finalPairs = new HashMap<>();
        int[] pairs = new int[14];
        for (Flower flower : list) {
            pairs[flower.ordinal()]++;
        }
        int slot = 0;
        for (int pair : pairs) {
            if (pair >= 2) {
                finalPairs.put(slot, pair);
                slot++;
            }
        }
        return finalPairs;
    }

    private void walk(Player player) {
        Tile targTile = player.tile().transform(0, +1, 0);
        player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
    }

    private void plant(Player player, Player opponent) {
        TaskManager.submit( new Task("FlowerPokerPlantTask",1, false) {
            int time = 0;
            int type = 0;
            Flower flower = player.getGamblingSession().flowers.get(type);
            @Override
            public void execute() {
                if(host.getGamblingSession().state() != GambleState.IN_PROGRESS) {
                    this.stop();
                    return;
                }
                switch (time) {
                    case 0 -> {
                        walk(player);
                        walk(opponent);
                    }
                    case 1 -> player.animate(827);
                    case 2 -> {
                        flower = player.getGamblingSession().flowers.get(type);
                        if (flower == null)
                            break;
                        GameObject gameFlower = new GameObject(flower.getId(), player.tile().copy());
                        ObjectManager.addObj(gameFlower);
                        player.getGamblingSession().gameFlowers.add(gameFlower);
                        player.faceObj(gameFlower);
                        opponent.faceObj(gameFlower);
                        if (Objects.nonNull(getOtherPlayer(player)))
                            ObjectManager.addObj(gameFlower);
                        walk(player);
                        walk(opponent);
                    }
                    case 4 -> {
                        if (flower.name().equalsIgnoreCase("WHITE") || flower.name().equalsIgnoreCase("BLACK")) {
                            host.getGamblingSession().finish(GamblingSession.FLOWER_POKER_ID, player, opponent, 0, 0);
                            stop();
                            break;
                        }
                        if (type == 4) {
                            this.stop();
                            break;
                        }
                        time = 0;
                        type++;
                    }
                }
                time++;
            }
        });
    }

    private Player getOtherPlayer(Player player) {
        /**
         * No game
         */
        if (player.getGamblingSession().game == null) {
            return null;
        }
        return player.getGamblingSession().game.host == player ? player.getGamblingSession().game.opponent : player.getGamblingSession().game.host;
    }

}
