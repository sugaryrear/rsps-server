package com.ferox.game.content.minigames.impl.inferno;



import com.ferox.game.content.achievements.Achievements;
import com.ferox.game.content.achievements.AchievementsManager;
import com.ferox.game.content.minigames.Minigame;
import com.ferox.game.content.minigames.MinigameManager;
import com.ferox.game.content.minigames.MinigameManager.ItemRestriction;
import com.ferox.game.content.minigames.MinigameManager.ItemType;
import com.ferox.game.content.minigames.MinigameManager.MinigameType;
import com.ferox.game.content.minigames.impl.fight_caves.FightCavesMinigame;
import com.ferox.game.task.Task;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.SplatType;
import com.ferox.game.world.entity.combat.method.impl.npcs.fightcaves.YtHurKot;
import com.ferox.game.world.entity.combat.method.impl.npcs.inferno.AncestralGlyph;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueManager;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.dialogue.Expression;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.movement.MovementQueue;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Color;
import com.ferox.util.NpcIdentifiers;
import com.ferox.util.chainedwork.Chain;

import java.util.HashSet;
import java.util.Set;

import static com.ferox.game.world.entity.AttributeKey.ACCOUNT_PIN_ATTEMPTS_LEFT;
import static com.ferox.game.world.entity.AttributeKey.INFERNO_WAVE;
import static com.ferox.util.ItemIdentifiers.*;
import static com.ferox.util.NpcIdentifiers.*;
import static com.ferox.util.Utils.randomElement;

/**
 * Handles the inferno minigame
 *
 * @author 2022 <http://www.rune-server.org/members/socklol/>
 */
public class InfernoMinigame extends Minigame {

    /**
     * The starting wave
     */
    private int wave;

    /**
     * The amount to kill
     */
    private int toKill;

    /**
     * The total killed
     */
    private int totalKilled;

    /**
     * Represents the inferno minigame
     *
     * @param wave   the wave
     */
    public InfernoMinigame(int wave) {
        this.wave = wave;
        this.toKill = 0;
        this.totalKilled = 0;
    }

    public final static Tile TZKALZUK_SPAWN = new Tile(2268,5360);
    public final static Tile START_TILE = new Tile(2271,5357);



    public final static Tile GLYPH_SPAWN = new Tile(2270,5357);
    /**
     * The spawn coordinates
     */
public static void jumptoinferno(Player player, int wave){
    Tile infernotile = new Tile(2493, 5124);
    player.lockDelayDamage();
    player.agilityWalk(false);
    player.getMovementQueue().clear();
    player.getMovementQueue().interpolate(infernotile, MovementQueue.StepType.FORCED_RUN);
    player.looks().render(6723, 6723, 6723, 6723, 6723, 6723, -1);

    Chain.bound(player).waitForTile(infernotile, () -> {
        player.teleport(START_TILE);
        player.agilityWalk(true);
        player.looks().resetRender();

        player.unlock();
        MinigameManager.playMinigame(player, new InfernoMinigame(wave));
    });


}
    public static void choosewavetype(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION,"Choose wave type (69 total waves)", "Start from wave 1", "Go straight to last wave (100,000 blood money)");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(option == 2) {
                        if(player.inventory().contains(BLOOD_MONEY,100_000)){
                        player.inventory().remove(BLOOD_MONEY,100_000);
jumptoinferno(player,69);
                        //jump into inferno
                    } else {
                        player.message("You don't have any of that item.");
                    }
                    stop();
                } else if(option == 1) {
                    jumptoinferno(player,1);

                    stop();
                }
            }
        });
    }
    public static void tzhaarketkehdialogue(Player player) {
        boolean sacrificedcape = player.getAttribOr(AttributeKey.SACRIFICED_FIRE_CAPE, false);
        if (!sacrificedcape) {
   dialogue(player,true);

        } else {

            player.getDialogueManager().start(new Dialogue() {
                @Override
                protected void start(Object... parameters) {
                    send(DialogueType.NPC_STATEMENT, TZHAARKETKEH, Expression.HAPPY, "Sup");
                    setPhase(0);
                }

                @Override
                public void next() {
                    if (getPhase() == 0) {
                        stop();
                    }
                }
            });
        }



    }
    public static void dialogue(Player player, boolean clickedfromnpc) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION,"Sacrifice Fire cape?", "Yes", "No");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(option == 1) {
                    if(player.inventory().contains(FIRE_CAPE)){
                        player.inventory().remove(FIRE_CAPE,1);
                        player.putAttrib(AttributeKey.SACRIFICED_FIRE_CAPE, true);
                        if(!clickedfromnpc)
                        InfernoMinigame.choosewavetype(player);

                        player.message("You may now enter the Inferno.");
                        //jump into inferno
                    } else {
                        player.message("You don't have any of that item.");
                    }
                    stop();
                } else if(option == 2) {
                    stop();
                }
            }
        });
    }
    public final static Tile[] COORDINATES = {new Tile(2278,5346), new Tile(2272,5349 ),
        new Tile(2272,5349), new Tile(2272,5342), new Tile(2278,5346)};

    /**
     * The possition outside the cave
     */
    public static final Tile OUTSIDE = new Tile(2496, 5113, 0);

    /**
     * All the 69 waves
     */

    public static final int
        JAL_NIB = 7691,
        JAL_MEJRAH = 7692,
        JAL_AK = 7693,
        JAL_AKREK_MEJ = 7694,
        JAL_AKREK_XIL = 7695,
        JAL_AKREK_KET = 7696,
        JAL_IMKOT = 7697,
        JAL_XIL = 7702,
        JAL_ZEK = 7699,
        JALTOK_JAD = 7700,
        YT_HURKOT = 7705,
        TZKAL_ZUK = 7706,
        ANCESTRAL_GLYPH = 7707,
        JAL_MEJJAK = 7708;


    private static final int[][] WAVES = {
        {JAL_NIB, JAL_NIB, JAL_NIB, JAL_MEJRAH}, //1
        {JAL_NIB, JAL_NIB, JAL_NIB, JAL_MEJRAH, JAL_MEJRAH},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_NIB, JAL_NIB, JAL_NIB},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_AK},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_AK, JAL_MEJRAH}, //5
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_AK, JAL_MEJRAH, JAL_MEJRAH},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_AK, JAL_AK},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_NIB, JAL_NIB},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_IMKOT},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_IMKOT, JAL_MEJRAH}, //10
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_IMKOT, JAL_MEJRAH, JAL_MEJRAH},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_IMKOT, JAL_MEJRAH},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_IMKOT, JAL_MEJRAH, JAL_AK},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_IMKOT, JAL_MEJRAH, JAL_MEJRAH, JAL_AK},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_AK, JAL_AK, JAL_IMKOT}, //15 -
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_IMKOT, JAL_IMKOT},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_NIB, JAL_NIB },
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_XIL},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_XIL, JAL_MEJRAH},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_XIL, JAL_MEJRAH, JAL_MEJRAH}, //20
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_XIL, JAL_AK},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_XIL, JAL_AK, JAL_MEJRAH},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_XIL, JAL_MEJRAH, JAL_MEJRAH, JAL_AK},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_XIL, JAL_AK, JAL_AK},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_XIL, JAL_IMKOT}, //25
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_XIL, JAL_IMKOT, JAL_MEJRAH},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_XIL, JAL_MEJRAH, JAL_MEJRAH, JAL_IMKOT},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_XIL, JAL_IMKOT, JAL_AK},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_XIL, JAL_AK, JAL_IMKOT},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_XIL, JAL_IMKOT, JAL_MEJRAH, JAL_MEJRAH, JAL_AK}, //30
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_XIL, JAL_AK, JAL_AK, JAL_IMKOT},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_XIL, JAL_IMKOT, JAL_IMKOT},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_XIL, JAL_XIL},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_NIB, JAL_NIB},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK}, //35
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_MEJRAH},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_MEJRAH, JAL_MEJRAH},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_AK},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_AK, JAL_MEJRAH},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_MEJRAH, JAL_MEJRAH, JAL_AK}, //40
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_AK, JAL_AK},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_IMKOT},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_IMKOT, JAL_MEJRAH},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_MEJRAH, JAL_MEJRAH, JAL_IMKOT},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_IMKOT, JAL_AK}, //45
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_MEJRAH, JAL_AK, JAL_IMKOT},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_MEJRAH, JAL_MEJRAH, JAL_AK, JAL_IMKOT},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_AK, JAL_AK, JAL_IMKOT},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_IMKOT, JAL_IMKOT},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_XIL}, //50
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_MEJRAH, JAL_XIL},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_XIL, JAL_MEJRAH, JAL_MEJRAH},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_XIL, JAL_AK},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_MEJRAH, JAL_XIL, JAL_AK},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_MEJRAH, JAL_MEJRAH, JAL_AK, JAL_XIL}, //55
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_AK, JAL_AK, JAL_XIL},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_ZEK, JAL_IMKOT},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_MEJRAH, JAL_IMKOT, JAL_XIL},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_MEJRAH, JAL_MEJRAH, JAL_IMKOT, JAL_XIL},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_AK, JAL_IMKOT, JAL_XIL}, //60
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_MEJRAH, JAL_AK, JAL_IMKOT, JAL_XIL},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_MEJRAH, JAL_MEJRAH, JAL_AK, JAL_IMKOT, JAL_XIL},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_AK, JAL_AK, JAL_IMKOT, JAL_XIL},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_IMKOT, JAL_IMKOT, JAL_XIL},
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_XIL, JAL_XIL}, //65
		{JAL_NIB, JAL_NIB, JAL_NIB, JAL_ZEK, JAL_ZEK, JAL_ZEK},
		{JALTOK_JAD},
		{JALTOK_JAD,JALTOK_JAD,JALTOK_JAD},{TZKAL_ZUK}
    };


    /* The highest reachable wave. */
    private static final int MAX_WAVE = WAVES.length;

    private final Set<Npc> npcSet = new HashSet<>();

    /**
     * Spawns a wave
     *
     * @param wave the wave
     */
    private void spawnWave(Player player, int wave) {

        for (int i = 0; i < WAVES[wave - 1].length; i++) {
            Tile pos = randomElement(COORDINATES);


            pos = new Tile(pos.x, pos.y, player.tile().getLevel());
            Npc monster = Npc.of(WAVES[wave - 1][i], wave == MAX_WAVE ? TZKALZUK_SPAWN : pos);

            npcSet.add(monster);
            monster.walkRadius(wave == MAX_WAVE ? 1 : 300);
            monster.respawns(false);
            monster.putAttrib(AttributeKey.MAX_DISTANCE_FROM_SPAWN, 300);
            World.getWorld().registerNpc(monster);

            toKill++;
            Task.runOnceTask(3, t -> {
                monster.setEntityInteraction(player);
                monster.face(player.tile());
                monster.getCombat().attack(player);
            });
        }
        player.message(Color.RED.tag()+"Wave: " + wave);
    }
public void initiateTzkalZuk(Player player){
    player.teleport(START_TILE);
    player.lockDelayDamage();
    Chain.bound(null).runFn(1, () -> {
        player.getPacketSender().stillCamera(2271, 5365, 2, 0, 10);

    }).then(4, () -> {
        ObjectManager.addObj(new GameObject(30342, new Tile( 2267, 5366), 10, 3));
        ObjectManager.addObj(new GameObject(30341, new Tile( 2275, 5366), 10, 1));
        ObjectManager.addObj(new GameObject(30340, new Tile( 2267, 5364), 10, 1));
        ObjectManager.addObj(new GameObject(30339, new Tile( 2275, 5364), 10, 3));
        ObjectManager.addObj(new GameObject(30344, new Tile( 2268, 5364), 10, 3));
        ObjectManager.addObj(new GameObject(30343, new Tile( 2273, 5364), 10, 3));
        ObjectManager.addObj(new GameObject(-1, new Tile( 2270, 5363), 10, 3));

    }).then(5, () -> {

        player.getPacketSender().sendObjectAnimation(new GameObject(30344, new Tile( 2268, 5364), 10, 3), 7560);
        player.getPacketSender().sendObjectAnimation(new GameObject(30343, new Tile( 2273, 5364), 10, 3), 7559);

    }).then(3, () -> {
        player.getPacketSender().resetCamera();

        player.unlock();
        spawnWave(player, wave);


   glyph = new AncestralGlyph(NpcIdentifiers.ANCESTRAL_GLYPH, GLYPH_SPAWN);

        npcSet.add(glyph);
        World.getWorld().registerNpc(glyph);

    });
}
public AncestralGlyph glyph;
    public void addNpc(Npc npc) {
        npcSet.add(npc);
    }

    @Override
    public void start(Player player) {
        int level = player.getIndex() * 4;
        player.teleport(new Tile(2271,5358, level));
        player.getPacketSender().sendString(4536, "Wave: " + wave);
        player.getInterfaceManager().openWalkable(4535);

        Npc npc = Npc.of(NpcIdentifiers.TZHAARMEJJAL, new Tile(3222, 3222, 0));
        DialogueManager.npcChat(player, Expression.CALM_TALK, npc.id(),"Good luck, Jal-Yt!");
        if(wave == MAX_WAVE){
            initiateTzkalZuk(player);
        } else {
            spawnWave(player, wave);
        }

    }

    @Override
    public Task getTask(Player player) {
        return null;
    }

    @Override
    public void end(Player player) {
        player.teleport(OUTSIDE);
        if (wave == (MAX_WAVE + 1)) {

            Npc npc = Npc.of(NpcIdentifiers.TZHAARMEJJAL, new Tile(3222, 3222, 0));
            DialogueManager.npcChat(player, Expression.CALM_TALK, npc.id(), "You even defeated TzkalZuk, I am most impressed!", "Please accept this gift.");

            player.inventory().addOrDrop(new Item(INFERNAL_CAPE, 1));
//            AchievementsManager.activate(player, Achievements.FIGHT_CAVES_I, 1);
//            AchievementsManager.activate(player, Achievements.FIGHT_CAVES_II, 1);
        }
        npcSet.forEach(npc -> World.getWorld().unregisterNpc(npc));
        player.setMinigame(null);
    }

    @Override
    public void killed(Player player, Mob entity) {
        if (entity.isNpc()) {
            Npc npc = entity.getAsNpc();
            npcSet.remove(npc);

            toKill--;
            totalKilled++;
            if (toKill == 0 || (wave == MAX_WAVE && toKill == 1)) {
                wave++;
                toKill = 0;
                if (wave == (MAX_WAVE + 1)) {
                    end(player);
                } else if(wave == MAX_WAVE){
                    initiateTzkalZuk(player);

                    } else {
                    spawnWave(player, wave);
                }
                }
            }
            player.putAttrib(INFERNO_WAVE,wave);
        }


    @Override
    public ItemType getType() {
        return ItemType.SAFE;
    }

    @Override
    public ItemRestriction getRestriction() {
        return ItemRestriction.NONE;
    }

    @Override
    public MinigameType getMinigameType() {
        return MinigameType.SAFE_MULTI;
    }

    @Override
    public boolean canTeleportOut() {
        return false;
    }

    @Override
    public boolean hasRequirements(Player player) {
        return true;
    }

}

