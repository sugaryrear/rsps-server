package com.ferox.game.content.skill.impl.thieving;

import com.ferox.GameServer;
import com.ferox.game.content.achievements.AchievementsManager;
import com.ferox.game.content.achievements.Achievements;
import com.ferox.game.content.diary.ardougne.ArdougneDiaryEntry;
import com.ferox.game.content.skill.impl.slayer.SlayerConstants;
import com.ferox.game.content.tasks.impl.Tasks;
import com.ferox.game.task.TaskManager;
import com.ferox.game.task.impl.ForceMovementTask;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.dialogue.DialogueManager;
import com.ferox.game.world.entity.mob.player.ForceMovement;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.MapObjects;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;

import java.util.Optional;

import static com.ferox.util.CustomItemIdentifiers.DOUBLE_DROPS_LAMP;
import static com.ferox.util.ItemIdentifiers.BLOOD_MONEY;

/**
 * @author Patrick van Elderen | April, 21, 2021, 11:44
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class Stalls extends PacketInteraction {

    public enum Stall {

        //Donator zone stalls
        SPICE_STALL(65, 5, 181.0, 13000, 2007,"spice stall", new int[][]{{6572, 6573}, {11733, 634}, {20348, 20349},}),
        GEM_STALL(75, 10, 200.0, 8500, 1627,"gem stall", new int[][]{{6162, 6984}, {11731, 634},}),
        BAKERS_STALL(2, 10, 20.0, 8500, 1891,"bakersstall", new int[][]{{6162}, {11730,634},}),
      SILVER_STALL(50, 10, 20.0, 8500, 442,"silver stall", new int[][]{{6162}, {11734,634},}),
        SILK_STALL(20, 10, 20.0, 8500, 950,"silk stall", new int[][]{{6162}, {11729,634},}),
        FUR_STALL(35, 10, 20.0, 8500, 6814,"fur stall", new int[][]{{6162}, {11732,634},}),

        //Normal stalls
        CRAFTING_STALL(1, 2, 16.0, 49000, 1597,"crafting stall", new int[][]{{4874, 4797}, {6166, 6984},}),
        MONKEY_GENERAL_STALL(5, 2, 36.0, 3166,49000, "general stall", new int[][]{{4876, 4797},}),
        MAGIC_STALL(65, 2, 100, 565,12000, "magic stall", new int[][]{{4877, 4797},}),
        SCIMITAR_STALL(65, 2, 100.0, 1000,1323,  "scimitar stall", new int[][]{{4878, 4797},});

        public final int levelReq, respawnTime, petOdds, theitem;
        public final int[][] objIDs;
        public final double experience;
        public final String name;

        Stall(int levelReq, int respawnTime, double experience, int petOdds, int theitem, String name, int[][] objIDs) {
            this.levelReq = levelReq;
            this.respawnTime = respawnTime * 1000 / 600;
            this.experience = experience;
            this.petOdds = petOdds;
            this.theitem = theitem;
            this.name = name;
            this.objIDs = objIDs;
        }
    }

    private void attempt(Player player, Stall stall, GameObject object, int replacementID) {
        if(player.locked()){
            return;
        }

        player.faceObj(object);
//        Optional<GameObject> thestall = MapObjects.get(object.getId(), object.tile());
//       int originalrotation =  thestall.get().getRotation();
        if (!player.skills().check(Skills.THIEVING, stall.levelReq, "steal from the " + stall.name))
            return;

        if (player.inventory().isFull()) {
            player.sound(2277);
            DialogueManager.sendStatement(player, "Your inventory is too full to hold any more.");
            return;
        }

        player.message("You attempt to steal from the " + stall.name + "...");
        player.lock();
        player.animate(832);
        if(stall == Stall.BAKERS_STALL)
            player.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.STEAL_CAKE);
        int chance = World.getWorld().random(6);

        Chain.bound(player).runFn(3, () -> {
            if (chance == 1)
            replaceStall(stall, object, replacementID, player);
            var bloodMoney = 0;

            if (stall == Stall.CRAFTING_STALL) {
                bloodMoney = World.getWorld().random(15, 25);
                AchievementsManager.activate(player, Achievements.THIEF_I, 1);
                AchievementsManager.activate(player, Achievements.MASTER_THIEF, 1);
            } else if (stall == Stall.MONKEY_GENERAL_STALL) {
                bloodMoney = World.getWorld().random(15, 37);
                AchievementsManager.activate(player, Achievements.THIEF_II, 1);
                AchievementsManager.activate(player, Achievements.MASTER_THIEF, 1);
            } else if (stall == Stall.MAGIC_STALL) {
                bloodMoney = World.getWorld().random(15, 50);
                AchievementsManager.activate(player, Achievements.THIEF_III, 1);
                AchievementsManager.activate(player, Achievements.MASTER_THIEF, 1);
            } else if (stall == Stall.SCIMITAR_STALL) {
                bloodMoney = World.getWorld().random(15, 100);
                AchievementsManager.activate(player, Achievements.THIEF_IV, 1);
                AchievementsManager.activate(player, Achievements.MASTER_THIEF, 1);
                player.getTaskMasterManager().increase(Tasks.STEAL_FROM_SCIMITAR_STALL);
            } else if (stall == Stall.SPICE_STALL) {
                bloodMoney = World.getWorld().random(15, 175);
                AchievementsManager.activate(player, Achievements.THIEF_IV, 1);
                AchievementsManager.activate(player, Achievements.MASTER_THIEF, 1);
            } else if (stall == Stall.GEM_STALL) {
                bloodMoney = World.getWorld().random(15, 300);
                AchievementsManager.activate(player, Achievements.THIEF_IV, 1);
                AchievementsManager.activate(player, Achievements.MASTER_THIEF, 1);
            }

            var thievingBoostPerk = player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.MORE_BM_THIEVING);
            if (thievingBoostPerk) {
                bloodMoney *= 10.0 / 100;
            }

            var blood_reaper = player.hasPetOut("Blood Reaper pet");
            if(blood_reaper) {
                int extraBM = bloodMoney * 10 / 100;
                bloodMoney += extraBM;
            }

//            if (GameServer.properties().pvpMode) {
//                player.inventory().add(new Item(BLOOD_MONEY, bloodMoney), true);
//            }
            player.inventory().add(new Item(stall.theitem, 1), true);
            var slayerUnlock = player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.DOUBLE_DROP_LAMPS);
            if (World.getWorld().rollDie(200, 1) && slayerUnlock) {
                player.inventory().addOrDrop(new Item(DOUBLE_DROPS_LAMP));
                player.message(Color.RED.wrap("Double drops lamp slayer perk activated."));
            }

//            if (Utils.percentageChance(5)) {
//                TaskManager.submit(new ForceMovementTask(player, 3, new ForceMovement(player.tile().clone(), new Tile(0, 3), 0, 70, 2)));
//                player.animate(3130);
//                player.getMovementQueue().clear();
//                player.stun(10);
//                player.message("A mysterious force knocks you back.");
//            }

            // Woo! A pet!
            var odds = (int) (stall.petOdds * player.getMemberRights().petRateMultiplier());
            if (World.getWorld().rollDie(odds, 1)) {
                ThievingPet.unlockRaccoon(player);
            }
            player.skills().addXp(Skills.THIEVING, stall.experience, true);
            player.unlock();
        });
    }

    private void replaceStall(Stall stall, GameObject object, int replacementID, Player player) {
        var replacement = new GameObject(replacementID, object.tile(), object.getType(), object.getRotation());
        ObjectManager.replace(object, replacement, stall.respawnTime);
    }

    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if (option == 1 || option == 2) {
            for (Stall stall : Stall.values()) {
                for (int[] ids : stall.objIDs) {
                    if (object.getId() == ids[0]) {
                        attempt(player, stall, object, ids[1]);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
