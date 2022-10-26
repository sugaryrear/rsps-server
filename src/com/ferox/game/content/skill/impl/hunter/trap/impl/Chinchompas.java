package com.ferox.game.content.skill.impl.hunter.trap.impl;

import com.ferox.game.content.skill.impl.hunter.Hunter;
import com.ferox.game.content.skill.impl.hunter.trap.Trap;
import com.ferox.game.content.skill.impl.hunter.trap.TrapProcessor;
import com.ferox.game.content.tasks.impl.Tasks;
import com.ferox.game.task.Task;
import com.ferox.game.task.TaskManager;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.npc.pets.Pet;
import com.ferox.game.world.entity.mob.npc.pets.PetAI;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Color;
import com.ferox.util.chainedwork.Chain;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.EnumSet;
import java.util.Optional;

import static com.ferox.util.ItemIdentifiers.*;

/**
 * The box trap implementation of the {@link Trap} class which represents a single box trap.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Chinchompas extends Trap {

    /**
     * Constructs a new {@link Chinchompas}.
     *
     * @param player {@link #getPlayer()}.
     */
    public Chinchompas(Player player) {
        super(player, TrapType.BOX_TRAP);
    }

    /**
     * The npc trapped inside this box.
     */
    private Optional<Npc> trapped = Optional.empty();

    /**
     * The object identification for a dismantled failed box trap.
     */
    private static final int FAILED_ID = 9385;

    /**
     * The object identification for a caught box trap.
     */
    private static final int CAUGHT_ID = 9382;

    /**
     * The distance the npc has to have from the box trap before it gets triggered.
     */
    private static final int DISTANCE_PORT = 3;

    /**
     * A collection of all the npcs that can be caught with a box trap.
     */
    private static final ImmutableSet<Integer> NPC_IDS = ImmutableSet.of(BoxTrapData.GREY_CHINCHOMPA.npcId,
        BoxTrapData.RED_CHINCHOMPA.npcId, BoxTrapData.BLACK_CHINCHOMPA.npcId);

    public static boolean hunterNpc(int id) {
        //Fastest to check the IDs with conditional operators, otherwise use an int array with lang3 ArrayUtils.contains
        return id >= 2910 && id <= 2912;
    }

    /**
     * Kills the specified {@code npc}.
     *
     * @param npc the npc to kill.
     */
    private void kill(Npc npc) {
        World.getWorld().unregisterNpc(npc);
        npc.setHitpoints(0);
        trapped = Optional.of(npc);
    }

    @Override
    public boolean canCatch(Npc npc) {
        Optional<BoxTrapData> data = BoxTrapData.getBoxTrapDataByNpcId(npc.id());

        if (data.isEmpty()) {
            throw new IllegalStateException("Invalid box trap id.");
        }

        if (player.skills().level(Skills.HUNTER) < data.get().requirement) {
            player.message("You do not have the required level to catch these.");
            setState(TrapState.FALLEN);
            return false;
        }
        return true;
    }

    @Override
    public void onPickUp() {
        player.message("You pick up your box trap.");
    }

    @Override
    public void onSetup() {
        player.message("You set-up your box trap.");
    }

    @Override
    public void onCatch(Npc npc) {
        if (!ObjectManager.exists(new Tile(getObject().getX(), getObject().getY(), getObject().getZ()))) {
            return;
        }
        final Trap boxtrap = this;

        TaskManager.submit(new Task("catch_box_trap_task", 1, true) {
            @Override
            protected void execute() {
                npc.smartPathTo(new Tile(getObject().getX(), getObject().getY()));
                //npc.forceChat("going to trap");
                if (isAbandoned()) {
                    stop();
                    return;
                }
                TrapProcessor trapProcessor = Hunter.GLOBAL_TRAPS.get(player);
                if (trapProcessor != null && trapProcessor.getTraps() != null && !trapProcessor.getTraps().contains(boxtrap)) {
                    stop();
                    return;
                }
                if (npc.getX() == getObject().getX() && npc.getY() == getObject().getY()) {
                    stop();
                    //npc.forceChat("attempt trap");

                    int count = random.inclusive(180);
                    int formula = successFormula(npc);
                    if (count > formula) {
                        setState(TrapState.FALLEN);
                        stop();
                        // npc.forceChat("fail");
                        return;
                    }
                    kill(npc);
                    // Equivilent of dying.. reset the npc
                    npc.hidden(true);
                    npc.teleport(npc.spawnTile());
                    npc.face(npc.tile().transform(0, 0));
                    npc.hp(npc.maxHp(), 0); // Heal up to full hp
                    npc.animate(-1); // Reset death animation
                    npc.getCombat().getKiller();
                    npc.getCombat().clearDamagers();

                    // Reset npc
                    Chain.bound(null).runFn(8, () -> {
                        npc.hidden(false);
                        npc.unlock();
                        World.getWorld().registerNpc(npc);
                    });
                    ObjectManager.removeObj(getObject());
                    boxtrap.setObject(CAUGHT_ID);
                    ObjectManager.addObj(getObject());
                    setState(TrapState.CAUGHT);
                }
            }
        });
    }

    @Override
    public void onSequence() {
        for (Npc npc : World.getWorld().getNpcs()) {
            if (npc == null || npc.dead()) {
                continue;
            }
            if (NPC_IDS.stream().noneMatch(id -> npc.id() == id)) {
                continue;
            }
            if (this.getObject().getZ() == npc.getZ() && Math.abs(this.getObject().getX() - npc.getX()) <= DISTANCE_PORT && Math.abs(this.getObject().getY() - npc.getY()) <= DISTANCE_PORT) {
                if (random.inclusive(100) < 20) {
                    return;
                }
                if (this.isAbandoned()) {
                    return;
                }
                trap(npc);
                break;
            }
        }
    }

    @Override
    public void reward() {
        if (trapped.isEmpty()) {
            throw new IllegalStateException("No npc is trapped.");
        }

        Optional<BoxTrapData> data = BoxTrapData.getBoxTrapDataByNpcId(trapped.get().id());

        if (data.isEmpty()) {
            throw new IllegalStateException("Invalid object id.");
        }

        Item reward = switch (data.get()) {
            case GREY_CHINCHOMPA -> new Item(CHINCHOMPA_10033);
            case RED_CHINCHOMPA -> new Item(RED_CHINCHOMPA_10034);
            case BLACK_CHINCHOMPA -> new Item(BLACK_CHINCHOMPA);
        };

        if(data.get() == BoxTrapData.BLACK_CHINCHOMPA) {
            player.getTaskMasterManager().increase(Tasks.BLACK_CHINCHOMPAS);
        }

        player.inventory().addOrDrop(reward);
        tryForPet(player, reward.equals(new Item(CHINCHOMPA_10033)) ? Pet.BABY_CHINCHOMPA_GREY : reward.equals(new Item(RED_CHINCHOMPA_10034)) ? Pet.BABY_CHINCHOMPA_RED : Pet.BABY_CHINCHOMPA_BLACK);
    }

    private void tryForPet(Player player, Pet pet) {
        var chance = pet == Pet.BABY_CHINCHOMPA_GREY ? 2200 : pet == Pet.BABY_CHINCHOMPA_RED ? 1800 : 1200;
        if (World.getWorld().rollDie((chance * (int)player.getMemberRights().petRateMultiplier()), 1)) {
            if (!PetAI.hasUnlocked(player, pet)) {
                // Unlock the varbit. Just do it, rather safe than sorry.
                player.addUnlockedPet(pet.varbit);

                // RS tries to add it as follower first. That only works if you don't have one.
                var currentPet = player.pet();
                if (currentPet == null) {
                    player.message("You have a funny feeling like you're being followed.");
                    PetAI.spawnPet(player, pet, false);
                } else {
                    // Sneak it into their inventory. If that fails, fuck you, no pet for you!
                    if (player.inventory().add(new Item(pet.item), true)) {
                        player.message("You feel something weird sneaking into your backpack.");
                    } else {
                        player.message("Speak to Probita to claim your pet!");
                    }
                }

                World.getWorld().sendWorldMessage("<img=1081> " + player.getUsername() + " has unlocked the pet: <col="+Color.HOTPINK.getColorValue()+">" + new Item(pet.item).name()+ "</col>.");
            } else {
                player.message("You have a funny feeling like you would have been followed...");
            }
        }
    }

    @Override
    public double experience() {
        if (trapped.isEmpty()) {
            throw new IllegalStateException("No npc is trapped.");
        }

        Optional<BoxTrapData> data = BoxTrapData.getBoxTrapDataByNpcId(trapped.get().id());

        if (data.isEmpty()) {
            throw new IllegalStateException("Invalid object id.");
        }

        return data.get().experience;
    }

    @Override
    public boolean canClaim(GameObject object) {
        return trapped.isPresent();
    }

    @Override
    public void setState(TrapState state) {
        if (state.equals(TrapState.PENDING)) {
            throw new IllegalArgumentException("Cannot set trap state back to pending.");
        }
        if (state.equals(TrapState.FALLEN)) {
            ObjectManager.removeObj(getObject());
            this.setObject(FAILED_ID);
            ObjectManager.addObj(this.getObject());
        }
        player.message("Your trap has been triggered by something...");
        super.setState(state);
    }

    /**
     * The enumerated type whose elements represent a set of constants
     * used for box trapping.
     *
     * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
     */
    private enum BoxTrapData {
        GREY_CHINCHOMPA(2910, 53, 198.25),
        RED_CHINCHOMPA(2911, 63, 265),
        BLACK_CHINCHOMPA(2912, 73, 315);

        /**
         * Caches our enum values.
         */
        private static final ImmutableSet<BoxTrapData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(BoxTrapData.class));

        /**
         * The npc id for this box trap.
         */
        private final int npcId;

        /**
         * The requirement for this box trap.
         */
        private final int requirement;

        /**
         * The experience gained for this box trap.
         */
        private final double experience;

        /**
         * Constructs a new {@link BoxTrapData}.
         *
         * @param npcId       {@link #npcId}.
         * @param requirement {@link #requirement}.
         * @param experience  {@link #experience}.
         */
        BoxTrapData(int npcId, int requirement, double experience) {
            this.npcId = npcId;
            this.requirement = requirement;
            this.experience = experience;
        }

        /**
         * Retrieves a {@link BoxTrapData} enumerator dependant on the specified {@code id}.
         *
         * @param id the npc id to return an enumerator from.
         * @return a {@link BoxTrapData} enumerator wrapped inside an optional, {@link Optional#empty()} otherwise.
         */
        public static Optional<BoxTrapData> getBoxTrapDataByNpcId(int id) {
            return VALUES.stream().filter(box -> box.npcId == id).findAny();
        }
    }
}
