package com.ferox.game.world.entity.combat.method.impl.npcs.bosses.zulrah;

import com.ferox.game.task.Task;
import com.ferox.game.task.TaskManager;
import com.ferox.game.task.impl.TickAndStop;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.SplatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Area;
import com.ferox.game.world.position.Tile;
import com.ferox.util.NpcIdentifiers;
import com.ferox.util.Tuple;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;

import java.util.stream.Stream;

/**
 * Created by Bart on 3/6/2016.
 */
@SuppressWarnings("ALL")
public class Zulrah {
    private static final Area ENCAMPMENT = new Area(2258, 3065, 2276, 3080);

    public static Area getENCAMPMENT() {
        return ENCAMPMENT;
    }

    /**
     * runs a function
     */
    public static void runFn(Object mob, int startAfterTicks, Runnable r) {
        TaskManager.submit(new Task("zulrahCbTask", startAfterTicks, false) {
            @Override
            protected void execute() {
                if (instanceFinished(mob)) {
                    stop();
                    return;
                }
                r.run();
                stop();
            }
        }.bind(mob));
    }

    private static boolean instanceFinished(Object mob) {
        if (mob instanceof Npc) {
            Npc npc = (Npc) mob;
            if (npc.dead() || !npc.isRegistered()) {
                return true;
            }
            Tuple<Integer, Player> player = npc.getAttribOr(AttributeKey.OWNING_PLAYER, new Tuple<>(-1, null));
            if(player.second() != null && (player.second().tile().distance(npc.tile()) > 20 || !player.second().isRegistered())) {
                return true;
            }
        }
        return false;
    }

    public static void startZulrahBattle(Npc npc, Mob target) {
        TaskManager.submit(new Task() {
            @Override
            protected void execute() {
                if (instanceFinished(npc)) {
                    stop();
                    return;
                }
                if (target instanceof Player) {
                    Player player = (Player) target;
                    ObjectManager.removeObj(new GameObject(11701, new Tile(2263, 3071, player.tile().getLevel())));
                    if (!player.getDialogueManager().isActive()) {
                        stop();

                        //System.out.println("zulrah battle begin");
                        // start the battle!
                        TaskManager.submit(new Task() {
                            int rotIndex = 0;
                            boolean init = true;
                            ZulrahPattern rot = ZulrahPattern.PATTERN_1; // randomize when you add new ones
                            int cooldown = 1;
                            @Override
                            protected void execute() {
                                if (instanceFinished(npc)) {
                                    stop();
                                    return;
                                }
                                //System.out.println("zulrah tick "+cooldown+" on idx "+rotIndex+" "+init);
                                if (cooldown-- < 1) {
                                    ZulrahPhase nextPhase = rot.getPhases().get(rotIndex++ % rot.getPhases().size());
                                    doPatternPhase(npc, nextPhase, target, init);
                                    cooldown = getPatternPhaseTime(npc, nextPhase, target, init);
                                    init = false;
                                }
                            }
                        }.bind(npc));
                    }
                }
                // will loop forever until chatbox closed or npc/player offline/outside instance
            }
        }.bind(npc));
    }

    private static int getPatternPhaseTime(Npc npc, ZulrahPhase phase, Mob target, boolean init) {
        int cooldown = 0;
        if (!init) {
            cooldown += 3; // init rise up from pool
        }
        if (!phase.hasConfig(ZulrahConfig.NO_ATTACK)) {
            switch (phase.getForm()) {
                case MELEE:
                    cooldown += 19;
                    break;
                case MAGIC:
                case RANGE:
                    cooldown += 5 * 3;
                    break;
                case JAD_RM:
                case JAD_MR:
                    cooldown += 10 * 3;
                    break;
            }
        }
        if (phase.hasConfig(ZulrahConfig.FULL_TOXIC_FUMES)) {
            cooldown += 13;
        } else if (phase.hasConfig(ZulrahConfig.SNAKELINGS_CLOUDS_SNAKELINGS)) {
            cooldown += 19;
        } else if (phase.hasConfig(ZulrahConfig.EAST_SNAKELINGS_REST_FUMES)) {
            cooldown += 22;
        } else if (phase.hasConfig(ZulrahConfig.SNAKELING_FUME_MIX)) {
            cooldown += 16;
        }
        cooldown += 3; // sink
        return cooldown;
    }

    private static void doPatternPhase(Npc npc, ZulrahPhase phase, Mob target, boolean init) {
        if (!init) {
            Tile targetTile = npc.spawnTile().transform(phase.getZulrahPosition().getTile().x,
                phase.getZulrahPosition().getTile().y, phase.getZulrahPosition().getTile().level);
            npc.teleport(targetTile);
            runFn(npc, 1, () -> {
                //npc.forceChat("emerge from pool");
                npc.transmog(phase.getForm().getId());
                npc.face(targetTile.transform(2, 2, 0).transform(phase.getZulrahPosition().getDirection().x,
                    phase.getZulrahPosition().getDirection().y, phase.getZulrahPosition().getDirection().level));
                npc.animate(5073);
                npc.lockDelayDamage();
            });
            runFn(npc, 3, () -> _doPatternPhasePart2(npc, phase, target));
           // System.out.println("phase start "+phase+" with emerge anim");
            return;
        }
        //System.out.println("phase start "+phase);
         _doPatternPhasePart2(npc, phase, target);
    }

    private static void _doPatternPhasePart2(Npc npc, ZulrahPhase phase, Mob target) {
        int cooldown = 0;
        final int[] recurringCumlativeTimer = {0};

        npc.setEntityInteraction(target);
        npc.unlock();

        if (!phase.hasConfig(ZulrahConfig.NO_ATTACK)) {
            switch (phase.getForm()) {
                case MELEE:
                    doMeleePhase(npc, target);
                    cooldown += 19;
                    break;
                case MAGIC:
                    cooldown += 5 * 3;
                    for (int i = 0; i < 5; i++) {
                        recurringCumlativeTimer[0]++; // start at 1
                        // runs code with tick values: 3, 6, 9, 12, 15
                        runFn(npc, recurringCumlativeTimer[0] * 3, () -> {
                            if (Utils.getRandom(6) == 1) {
                                doRangedAttack(npc, target);
                            } else {
                                doMagicAttack(npc, target);
                            }
                        });
                    }
                    break;
                case RANGE:
                    cooldown += 5 * 3;
                    for (int i = 0; i < 5; i++) {
                        recurringCumlativeTimer[0]++; // start at 1
                        // runs code with tick values: 3, 6, 9, 12, 15
                        runFn(npc, recurringCumlativeTimer[0] * 3, () -> doRangedAttack(npc, target));
                    }
                    break;
                case JAD_RM:
                case JAD_MR:
                    cooldown += 10 * 3;
                    final boolean[] range = {phase.getForm() == ZulrahForm.JAD_RM};
                    for (int i = 0; i < 10; i++) {
                        recurringCumlativeTimer[0]++; // start at 1
                        // runs code with tick values: 3, 6, 9, 12, 15
                        runFn(npc, recurringCumlativeTimer[0] * 3, () -> {
                            if (range[0]) {
                                doRangedAttack(npc, target);
                            } else {
                                doMagicAttack(npc, target);
                            }
                            range[0] = !range[0];
                        });
                    }
                    break;
            }
        }


        // run after top section goes
        Chain.bound(null).cancelWhen(() -> instanceFinished(npc)).runFn(cooldown, () -> {
            npc.setEntityInteraction(null);
            if (phase.hasConfig(ZulrahConfig.FULL_TOXIC_FUMES)) {
                fillToxicFumes(npc, target);
            } else if (phase.hasConfig(ZulrahConfig.SNAKELINGS_CLOUDS_SNAKELINGS)) {
                snakelingsCloudsSnakelings(npc, target);
            } else if (phase.hasConfig(ZulrahConfig.EAST_SNAKELINGS_REST_FUMES)) {
                eastSnakelingsRestFumes(npc, target);
            } else if (phase.hasConfig(ZulrahConfig.SNAKELING_FUME_MIX)) {
                snakelingFumeMix(npc, target);
            }
        });
        if (phase.hasConfig(ZulrahConfig.FULL_TOXIC_FUMES)) {
            cooldown += 13;
        } else if (phase.hasConfig(ZulrahConfig.SNAKELINGS_CLOUDS_SNAKELINGS)) {
            cooldown += 19;
        } else if (phase.hasConfig(ZulrahConfig.EAST_SNAKELINGS_REST_FUMES)) {
            cooldown += 22;
        } else if (phase.hasConfig(ZulrahConfig.SNAKELING_FUME_MIX)) {
            cooldown += 16;
        }

        runFn(npc, cooldown, () -> {
            //npc.forceChat("sinking down...");
            target.stopActions(false);
            npc.getCombat().reset();
            npc.animate(5072);
            npc.face(null);
            npc.lockDelayDamage();
            runFn(npc, 2, () -> {
                npc.unlock();
            });
        });
    }

    private static void doMagicAttack(Npc npc, Mob target) {
        //npc.forceChat("magic attack");
        npc.animate(5069);
        int dist = npc.tile().transform(2, 2, 0).distance(target.tile());
        new Projectile(npc, target,1046, 20,15 + (10 * dist),65, 20, 10).sendProjectile();
        int delay = Math.max(1, (20 + dist * 12) / 30);
        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
        target.venom(npc);
    }

    private static void doRangedAttack(Npc npc, Mob target) {
        //npc.forceChat("range attack");
        npc.animate(5069);
        int dist = npc.tile().transform(2, 2, 0).distance(target.tile());
        new Projectile(npc, target,1044,20, 15 + (10 * dist),65, 20, 10).sendProjectile();
        int delay = Math.max(1, (20 + dist * 12) / 30);
        int max = npc.combatInfo().maxhit;
        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
        target.venom(npc);
    }

    private static void doMeleePhase(Npc npc, Mob target) {
        npc.face(null);
        runFn(npc, 1, () -> _doMeleePhaseInner(npc, target));
        runFn(npc, 10, () -> _doMeleePhaseInner(npc, target));
    }

    private static void _doMeleePhaseInner(Npc npc, Mob target) {
        npc.setEntityInteraction(null);
        //npc.forceChat("melee attack");
        npc.animate(5806);
        Tile p1 = target.tile().copy();
        npc.face(p1);
        Chain.bound(null).cancelWhen(() -> instanceFinished(npc)).runFn(1, () -> {
            npc.face(p1);
        }).then(4, () -> {
            if (p1.area(1).contains(target) && !isMeleeSafespot(npc, target.tile())) {
                target.stun(4);
                target.hit(npc, Utils.random(41), 0, CombatType.MELEE).checkAccuracy().submit();
            }
        }).then(2, () -> {
            npc.setEntityInteraction(target);
        }).then(2, () -> {
            npc.setEntityInteraction(null);
        });
    }

    /**
     * kill off alive snakelins on boss death
     */
    public static void death(Player killer, Npc npc) {
        if (!Stream.of(NpcIdentifiers.ZULRAH, NpcIdentifiers.ZULRAH_2043, NpcIdentifiers.ZULRAH_2044).anyMatch(i->i==npc.id())) {
            return;
        }
        killer.getLocalNpcs().forEach(n -> {
            if (n.id() == NpcIdentifiers.SNAKELING && n.tile().getLevel() == killer.tile().getLevel()) {
                n.hit(n, n.hp());
            }
        });

        ObjectManager.addObj(new GameObject(11701, new Tile(2263, 3071, killer.tile().getLevel())));
    }

    public static boolean is(Mob mob) {
        return mob.isNpc() && mob.getAsNpc().id() >= NpcIdentifiers.ZULRAH && mob.getAsNpc().id() <= NpcIdentifiers.ZULRAH_2044;
    }

    /**
     * dummy class. do not use for submitting hits otherwise there will be NPEs due to lack of flexibility in customizing internals
     */
    public static final class EmptyCombatMethod extends CommonCombatMethod {

        private Mob mob;
        private Mob target;
        private int max;

        public static EmptyCombatMethod make() {
            return new EmptyCombatMethod();
        }

        @Override
        public void prepareAttack(Mob mob, Mob target) {
        }

        @Override
        public int getAttackSpeed(Mob mob) {
            return 1;
        }

        @Override
        public int getAttackDistance(Mob mob) {
            return 1;
        }

        public EmptyCombatMethod max(int max) {
            this.max = max;
            return this;
        }
    }

    private static void fillToxicFumes(Npc npc, Mob target) {
        Tile spawnTile = npc.spawnTile();

        // Fix facing first
        npc.face(null);

        Chain.bound(null).cancelWhen(() -> instanceFinished(npc)).runFn(1, () -> {
            npc.animate(5069);
            npc.face(spawnTile.transform(4, -4));
            spitFume(npc, spawnTile.transform(2, -4), target, 3);
            spitFume(npc, spawnTile.transform(5, -4), target, 3);
        }).then(3, () -> {
            // South-west
            npc.animate(5069);
            npc.face(spawnTile.transform(-2, -4));
            spitFume(npc, spawnTile.transform(-1, -4), target, 3);
            spitFume(npc, spawnTile.transform(-4, -3), target, 3);
        }).then(3, () -> {
            // East
            npc.animate(5069);
            npc.face(spawnTile.transform(6, 2));
            spitFume(npc, spawnTile.transform(6, -1), target, 3);
            spitFume(npc, spawnTile.transform(6, 2), target, 3);
        }).then(3, () -> {
            // West
            npc.animate(5069);
            npc.face(spawnTile.transform(-4, 2));
            spitFume(npc, spawnTile.transform(-4, 3), target, 3);
            spitFume(npc, spawnTile.transform(-4, 0), target, 3);
        });

    }

    private static void snakelingsCloudsSnakelings(Npc npc, Mob target) {
        Tile spawnTile = npc.spawnTile();

        // Fix facing first
        npc.face(null);
        runFn(npc, 1, () -> {
            // Snakelings
            npc.animate(5069);
            npc.face(spawnTile.transform(-3, 4));
            createSnakeling(npc, spawnTile.transform(-3, 4), 4, target);

            runFn(npc, 3, () -> {
                npc.face(spawnTile.transform(-3, 1));
                createSnakeling(npc, spawnTile.transform(-3, 1), 4, target);

                runFn(npc, 3, () -> {
                    // Fumes
                    npc.animate(5069);
                    npc.face(spawnTile.transform(-4, -3));
                    spitFume(npc, spawnTile.transform(-4, -3), target, 3);
                    spitFume(npc, spawnTile.transform(-1, -4), target, 3);

                    runFn(npc, 3, () -> {
                        npc.animate(5069);
                        npc.face(spawnTile.transform(5, -2));
                        spitFume(npc, spawnTile.transform(5, -4), target, 3);
                        spitFume(npc, spawnTile.transform(6, -1), target, 3);

                        runFn(npc, 3, () -> {
                            // Snakelings
                            npc.animate(5069);
                            npc.face(spawnTile.transform(7, 3));
                            createSnakeling(npc, spawnTile.transform(7, 3), 4, target);

                            runFn(npc, 3, () -> {
                                npc.animate(5069);
                                npc.face(spawnTile.transform(7, 6));
                                createSnakeling(npc, spawnTile.transform(7, 6), 4, target);
                            });
                        });
                    });
                });
            });
        });
    }

    private static void eastSnakelingsRestFumes(Npc npc, Mob target) {
        Tile spawnTile = npc.spawnTile();

        // Fix facing first
        npc.face(null);
        runFn(npc, 1, () -> {
            // Fumes
            npc.animate(5069);
            npc.face(spawnTile.transform(3, -4));
            spitFume(npc, spawnTile.transform(2, -4), target, 3);
            spitFume(npc, spawnTile.transform(5, -4), target, 3);

            runFn(npc, 3, () -> {
                // Fumes
                npc.animate(5069);
                npc.face(spawnTile.transform(-2, -3));
                spitFume(npc, spawnTile.transform(-4, -3), target, 3);
                spitFume(npc, spawnTile.transform(-1, -4), target, 3);

                runFn(npc, 3, () -> {
                    // Fumes
                    npc.animate(5069);
                    npc.face(spawnTile.transform(-4, 2));
                    spitFume(npc, spawnTile.transform(-4, 0), target, 3);
                    spitFume(npc, spawnTile.transform(-4, 3), target, 3);

                    runFn(npc, 3, () -> {
                        // Snakelings
                        npc.animate(5069);
                        npc.face(spawnTile.transform(6, -3));
                        createSnakeling(npc, spawnTile.transform(6, -3), 4, target);

                        runFn(npc, 3, () -> {
                            npc.animate(5069);
                            npc.face(spawnTile.transform(7, 3));
                            createSnakeling(npc, spawnTile.transform(7, 3), 4, target);

                            runFn(npc, 3, () -> {
                                npc.animate(5069);
                                npc.face(spawnTile.transform(7, 6));
                                createSnakeling(npc, spawnTile.transform(7, 6), 4, target);

                                runFn(npc, 3, () -> {
                                    npc.animate(5069);
                                    npc.face(spawnTile.transform(7, 0));
                                    createSnakeling(npc, spawnTile.transform(7, 0), 4, target);
                                });
                            });
                        });
                    });
                });
            });
        });

    }

    private static void snakelingFumeMix(Npc npc, Mob target) {
        Tile spawnTile = npc.spawnTile();

        // Fix facing first
        npc.face(null);
        runFn(npc, 1, () -> {
            // Snakelings
            npc.animate(5069);
            npc.face(spawnTile.transform(-3, -2));
            createSnakeling(npc, spawnTile.transform(-3, -2), 4, target);

            runFn(npc, 3, () -> {
                // Fumes
                npc.animate(5069);
                npc.face(spawnTile.transform(1, -4));
                spitFume(npc, spawnTile.transform(-1, -4), target, 3);
                spitFume(npc, spawnTile.transform(2, -4), target, 3);

                runFn(npc, 3, () -> {
                    // Snakelings
                    npc.animate(5069);
                    npc.face(spawnTile.transform(-3, 4));
                    createSnakeling(npc, spawnTile.transform(-3, 4), 4, target);

                    runFn(npc, 3, () -> {
                        // Fumes
                        npc.animate(5069);
                        npc.face(spawnTile.transform(5, -4));
                        spitFume(npc, spawnTile.transform(5, -4), target, 3);
                        spitFume(npc, spawnTile.transform(6, -1), target, 3);

                        runFn(npc, 3, () -> {
                            // Snakelings
                            npc.animate(5069);
                            npc.face(spawnTile.transform(-3, 1));
                            createSnakeling(npc, spawnTile.transform(-3, 1), 4, target);
                        });
                    });
                });
            });
        });
    }

    private static void spitFume(Npc npc, Tile tile, Mob target, int delay) {
        GameObject obj = new GameObject(11700, tile, 10, 0);
        Area area = tile.transform(1, 1).area(1); // Center, 3x3

        // Do a nice graphic.
        new Projectile(npc.tile().transform(2, 2, 0), tile.transform(1, 1),0,1045, 12 + (10 * 6),20, 65, 0, 10).sendProjectile();

        TaskManager.submit(new TickAndStop(delay) {
            @Override
            public void executeAndStop() {
                ObjectManager.addObj(obj);
                TaskManager.submit(new Task() {
                    int internalCounter = 30;
                    @Override
                    protected void execute() {
                        if (instanceFinished(npc)) {
                            ObjectManager.removeObj(obj);
                            stop();
                            return;
                        }
                        if (internalCounter-- > 0) {
                            if (area.contains(target,true)) {
                                // just standing here causes damage, not only when venom ticker is applied
                                target.hit(npc, 1 + Utils.getRandom(3), SplatType.VENOM_HITSPLAT);
                                target.venom(npc); // apply venom
                            }
                        } else {
                            ObjectManager.removeObj(obj);
                            stop();
                        }
                    }
                });
            }
        });
    }

    private static void createSnakeling(Npc npc, Tile tile, int delay, Mob target) {
        new Projectile(npc.tile().transform(2, 2, 0), tile,0,1047, 12 + (16 * 6),20, 65, 0, 10).sendProjectile();

        TaskManager.submit(new TickAndStop(delay) {
            @Override
            public void executeAndStop() {
                //Target can only be a player
                if (target instanceof Player) {
                    Player player = (Player) target;

                    Npc snakeling = new Npc(NpcIdentifiers.SNAKELING, tile.copy());

                    //Add snakeling to the npc list of this instance
                    player.getZulrahInstance().npcList.add(snakeling);

                    World.getWorld().registerNpc(snakeling);
                    snakeling.respawns(false);
                    snakeling.animate(2413);
                    snakeling.putAttrib(AttributeKey.OWNING_PLAYER, new Tuple<>(player.getIndex(), player));
                    snakeling.lockNoDamage();
                    snakeling.setController(npc.getController());

                    TaskManager.submit(new TickAndStop(3) {
                        @Override
                        public void executeAndStop() {
                            snakeling.unlock();
                            snakeling.getCombat().attack(target);
                        }
                    });
                    TaskManager.submit(new Task() { // despawn when no longer valid
                        @Override
                        protected void execute() {
                            if (instanceFinished(npc) || snakeling.dead() || !snakeling.isRegistered()) {
                                World.getWorld().unregisterNpc(snakeling);
                                stop();
                            }
                        }
                    });
                    snakeling.runUninterruptable(60, () -> {
                        snakeling.hit(snakeling, snakeling.hp());
                    });
                }
            }
        });
    }

    private static boolean isMeleeSafespot(Npc npc, Tile tile) {
        Tile spawnTile = npc.spawnTile();
        return tile == spawnTile.transform(-3, 0) || tile == spawnTile.transform(7, 0) || tile == spawnTile.transform(4, -2) || tile == spawnTile.transform(0, -2);
    }
}
