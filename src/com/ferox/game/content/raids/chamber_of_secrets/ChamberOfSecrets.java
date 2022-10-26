package com.ferox.game.content.raids.chamber_of_secrets;

import com.ferox.game.content.daily_tasks.DailyTaskManager;
import com.ferox.game.content.daily_tasks.DailyTasks;
import com.ferox.game.content.mechanics.Poison;
import com.ferox.game.content.raids.party.Party;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.combat.Venom;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.entity.mob.player.commands.CommandManager;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.position.Area;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Color;
import com.ferox.util.SerializablePair;
import com.ferox.util.Utils;

import java.time.Duration;
import java.time.Instant;

import static com.ferox.game.world.entity.AttributeKey.CHAMBERS_OF_XERIC;
import static com.ferox.game.world.entity.AttributeKey.PERSONAL_POINTS;
import static com.ferox.util.NpcIdentifiers.*;

/**
 * @author Patrick van Elderen | April, 26, 2021, 16:58
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class ChamberOfSecrets {

    public final Area OLM_ROOM = new Area(3228, 5730, 3237, 5748);
    public static final Area OLM = new Area(3217, 5711, 3260, 5760);

    public static final int REWARD_WIDGET = 12020;

    private final Player player;

    public ChamberOfSecrets(Player player) {
        this.player = player;
    }

   // private Instant startTime = null;
    private final double combatPointsFactor = 1;

    public boolean handleDeath(Player player) {
        Party party = player.raidsParty;
        if (party == null)
            return false;
        player.teleport(getRespawnPosition(party, player.tile().level));
        if(party.getRaidStage() == 9){
            World.getWorld().unregisterNpc(party.getGreatOlmNpc());
            World.getWorld().unregisterNpc(party.getLeftHandNpc());
            World.getWorld().unregisterNpc(party.getRightHandNpc());

            party.bossFightStarted(false);
            clearParty();
        }
        int pointsLost = (int) (player.<Integer>getAttribOr(PERSONAL_POINTS, 0) * 0.4);
        if (pointsLost > 0)
            addPoints(-pointsLost);

        //Make sure to heal
        player.healPlayer();
       // player.getInterfaceManager().openWalkable(-1);

        return true;
    }


    private Tile getRespawnPosition(Party party, int level) {
        return switch (party.getRaidStage()) {
            case 1 -> new Tile(3307, 5208, level);//lizardman shamans
            case 2 -> new Tile(3311, 5309, level);//muttadiles
            case 3 -> new Tile(3312, 5218 ,level);//skeletal mystics
            case 4 -> new Tile(3312, 5312, level);//vanguards
            case 5 -> new Tile(3311, 5344, level);//ice demon
            case 6 -> new Tile(3311, 5277, level);//vespula
            case 7 -> new Tile(3344,5248, level);//vasa nistirio
            case 8 -> new Tile(3309, 5277, level);//tekton
            case 9 -> new Tile(3279, 5170, level);

            default -> new Tile(3299, 5189, level);
        };
    }

    public void exit() {

        Party party = player.raidsParty;

        if(party != null) {
            party.removeMember(player);
            //Last player in the party leaves clear the whole thing
            if(party.getMembers().size() == 0) {
                //Clear all party members that are left
                party.getMembers().clear();
                clearParty();
            }
            player.raidsParty = null;
        }
//        Duration d = getTimeSinceStart();
//        long howlongittook = d.toMillis();
//        String time = String.format("%02d:%02d", d.toMinutes(), d.getSeconds() % 60);
//
//        player.message("duration: "+time);
        deleteRaidsItems();
        //Reset points
        player.putAttrib(PERSONAL_POINTS,0);
        player.skills().resetStats();
        int increase = player.getEquipment().hpIncrease();
        player.hp(Math.max(increase > 0 ? player.skills().level(Skills.HITPOINTS) + increase : player.skills().level(Skills.HITPOINTS), player.skills().xpLevel(Skills.HITPOINTS)), 39); //Set hitpoints to 100%
        player.skills().replenishSkill(5, player.skills().xpLevel(5)); //Set the players prayer level to fullplayer.putAttrib(AttributeKey.RUN_ENERGY, 100.0);
        player.setRunningEnergy(100.0, true);
        Poison.cure(player);
        Venom.cure(2, player);

        //Move outside of raids
        player.teleport(1245, 3561, 0);
        player.getInterfaceManager().close();
        player.getInterfaceManager().openWalkable(-1);

    }

    public boolean isRaiding() {
        return player.raidsParty != null && player.raidsParty.getHPRaid() != null;
    }

    public void addPoints(int points) {
        if (!isRaiding())
            return;
        player.raidsParty.addPersonalPoints(player, points);
    }

    public void addDamagePoints(Npc target, int points) {
        if (!isRaiding())
            return;
        if (target.getAttribOr(AttributeKey.RAIDS_NO_POINTS, false))
            return;
        points *= 5;
        points *= player.raidsParty.getHPRaid().combatPointsFactor;
        addPoints(points);
    }
    public Instant startTime;

public void gettime() {
    startTime = Instant.now();

}
    public Duration getTimeSinceStart() {
     //   stopwatch.stop();
        //  d.toMillis()
            return Duration.between(startTime, Instant.now());
     //   return String.format("%02d:%02d", d.toMinutes(), d.getSeconds() % 60);
    }

    public String getStringTime(Duration d) {

    return String.format("%02d:%02d", d.toMinutes(), d.getSeconds() % 60);
    }

    public void completeRaid(Party party) {
        //String time = getTimeSinceStart();
        party.setRaidStage(10);

        Duration d = getTimeSinceStart();
        long howlongittook = d.toMillis();
        long best = player.getAttribOr(AttributeKey.CHAMBERS_OF_XERIC_TIME, 0L);
        String time = String.format("%02d:%02d", d.toMinutes(), d.getSeconds() % 60);

        party.forPlayers(p -> {
            p.message(Color.RAID_PURPLE.wrap("Congratulations - your raid is complete! Duration: " + Color.RED.wrap(time) + " " + (howlongittook < best? "(New personal best)" : "" )+"."));

            p.message(String.format("Total points: " + Color.RAID_PURPLE.wrap("%,d") + ", Personal points: " + Color.RAID_PURPLE.wrap("%,d") + " (" + Color.RAID_PURPLE.wrap("%.2f") + "%%)",
                party.totalPoints(), p.<Integer>getAttribOr(PERSONAL_POINTS, 0), (double) (p.<Integer>getAttribOr(PERSONAL_POINTS, 0) / party.totalPoints()) * 100));


            var completed = player.<Integer>getAttribOr(CHAMBERS_OF_XERIC,0) + 1;
            player.putAttrib(CHAMBERS_OF_XERIC, completed);
            player.message(Color.PURPLE.wrap("You have now completed "+ Utils.formatNumber(completed)+" Chambers of Xeric!"));
            deleteRaidsItems();
            //Daily raids task
            DailyTaskManager.increase(DailyTasks.DAILY_RAIDS, p);

            //this is what actually allows you to withdraw items from the chest - it populates the array
            ChamberOfSecretsReward.giveRewards(p);
        });

//        if (howlongittook < best) {
//            player.putAttrib(AttributeKey.CHAMBERS_OF_XERIC_TIME, howlongittook);
//        }
//        SerializablePair<String, Long> globalBest = World.getServerData().getZulrahTime();
//        if (globalBest.getFirst() == null || globalBest.getSecond() == null || howlongittook < globalBest.getSecond() && globalBest.getSecond() != 0) {
//            World.getWorld().sendBroadcast("<col=CC0000>" + player.getUsername()
//                + "</col> set the record for best time in Chambers of Xeric with " + time + "!");
//            if (globalBest.getFirst() != null && globalBest.getSecond() != null) {
//                World.getWorld().sendBroadcast("The old record was set by <col=CC0000>" + globalBest.getFirst()
//                    + "</col> with a time of <col=CC0000>" + Utils.toFormattedMS(globalBest.getSecond()) + "</col>.");
//
//            }
//            World.getServerData().setSerializablePair(new SerializablePair<>(player.getUsername(), howlongittook));
//        }
        World.getWorld().unregisterAll(OLM);
        World.getWorld().unregisterAll(OLM_ROOM);
    }


    public void startHPRaid() {

        Party party = player.raidsParty;
        if (party == null)
            return;
        party.setRaidStage(1);
party.setBraziersLit(0);

        final int height = party.getLeader().getIndex() * 4;

        for (Player member : party.getMembers()) {
            member.teleport(new Tile(3299, 5189, height));
            party.setHeight(height);
         //   member.message(gettime());
        }

        //Clear kills
        party.setKills(0);

        //Clear npcs that somehow survived first:
        clearParty();

        //Spawn all creatures
        spawnLizardmanShaman();
        spawnMuttadile();
        spawnVanguards();
        spawnSkeletalMystics();

        spawnIceDemon();
        spawnVespula();
        spawnVasaNistirio();
        spawnTekton();
        CommandManager.attempt(player, "timeraids");

      //  spawnFenrirGreyback();
    }
    public void startHPRaidlastboss() {

        Party party = player.raidsParty;
        if (party == null)
            return;
        party.setRaidStage(1);
        party.setBraziersLit(0);
party.setlastboss(true);
        final int height = party.getLeader().getIndex() * 4;

        for (Player member : party.getMembers()) {
            member.teleport(new Tile(3277,5169, height));
            party.setHeight(height);
            //   member.message(gettime());
        }

        //Clear kills
        party.setKills(0);

        //Clear npcs that somehow survived first:
        clearParty();

        //Spawn all creatures
        spawnLizardmanShaman();
        spawnMuttadile();
        spawnVanguards();
        spawnSkeletalMystics();

        spawnIceDemon();
        spawnVespula();
        spawnVasaNistirio();
        spawnTekton();
        CommandManager.attempt(player, "timeraids");

        //  spawnFenrirGreyback();
    }
    private void spawnLizardmanShaman() {
        //Get the raids party
        Party party = player.raidsParty;

        //Create LIZARDMAN SHAMANS
        Npc SHAMAN1 = new ChamberOfSecretsNpc(LIZARDMAN_SHAMAN_7573, new Tile(3311, 5260, party.getHeight()), party.getSize());
        Npc SHAMAN2 = new ChamberOfSecretsNpc(LIZARDMAN_SHAMAN_7573, new Tile(3304, 5268, party.getHeight()), party.getSize());
        Npc SHAMAN3 = new ChamberOfSecretsNpc(LIZARDMAN_SHAMAN_7573, new Tile(3306, 5260, party.getHeight()), party.getSize());


        //Spawn  LIZARDMAN SHAMANS
        World.getWorld().registerNpc(SHAMAN1);
        party.monsters.add(SHAMAN1);
        World.getWorld().registerNpc(SHAMAN2);
        party.monsters.add(SHAMAN2);
        World.getWorld().registerNpc(SHAMAN3);
        party.monsters.add(SHAMAN3);
    }
    private void spawnSkeletalMystics() {
        //Get the raids party
        Party party = player.raidsParty;

        //Create LIZARDMAN SHAMANS
        Npc MYSTIC1 = new ChamberOfSecretsNpc(SKELETAL_MYSTIC, new Tile(3312,5260, party.getHeight()+1), party.getSize());
        Npc  MYSTIC2 = new ChamberOfSecretsNpc(SKELETAL_MYSTIC_7605, new Tile(3317,5269, party.getHeight()+1), party.getSize());
        Npc MYSTIC3 = new ChamberOfSecretsNpc(SKELETAL_MYSTIC_7606, new Tile(3305,5268, party.getHeight()+1), party.getSize());


        //Spawn  LIZARDMAN SHAMANS
        World.getWorld().registerNpc( MYSTIC1 );
        party.monsters.add( MYSTIC1 );
        World.getWorld().registerNpc(MYSTIC2);
        party.monsters.add(MYSTIC2);
        World.getWorld().registerNpc(MYSTIC3);
        party.monsters.add(MYSTIC3);
    }
    private void spawnMuttadile() {
        //Get the raids party
        Party party = player.raidsParty;

        //Create muttadile
        Npc muttadile = new ChamberOfSecretsNpc(MUTTADILE_7562, new Tile(3308, 5328, party.getHeight()+1), party.getSize());

        //Spawn muttadile
        World.getWorld().registerNpc(muttadile);
        party.monsters.add(muttadile);

    }

    private void spawnVanguards() {
        //Get the raids party
        Party party = player.raidsParty;

        //Create vanguards
        Npc melee = new ChamberOfSecretsNpc( VANGUARD_7527 , new Tile(3312, 5331, party.getHeight()), party.getSize());
        Npc range = new ChamberOfSecretsNpc( VANGUARD_7528, new Tile(3309, 5332, party.getHeight()), party.getSize());

        Npc magic = new ChamberOfSecretsNpc( VANGUARD_7529, new Tile(3313,5327, party.getHeight()), party.getSize());

        //Spawn vanguards
        World.getWorld().registerNpc(melee);
        party.monsters.add(melee);

        World.getWorld().registerNpc( range);
        party.monsters.add( range);

        World.getWorld().registerNpc(magic);
        party.monsters.add(magic);
    }

    private void spawnIceDemon() {
        //Get the raids party
        Party party = player.raidsParty;

        //Create ice demon
        Npc icedemon = new ChamberOfSecretsNpc(ICE_DEMON_7585, new Tile(3310, 5368, party.getHeight()), party.getSize());

        //Spawn ice demon
        World.getWorld().registerNpc(icedemon);
        party.monsters.add(icedemon);
    }

    private void spawnVespula() {
        //Get the raids party
        Party party = player.raidsParty;

        //Create vespula
        Npc vespula = new ChamberOfSecretsNpc(VESPULA_7531, new Tile(3307,5297, party.getHeight()+2), party.getSize());
        //Spawn vespula
        World.getWorld().registerNpc(vespula);
        party.monsters.add(vespula);
    }
    private void spawnVespulaGround() {
        //Get the raids party
        Party party = player.raidsParty;

        //Create vespula
        Npc vespula = new ChamberOfSecretsNpc(VESPULA_7532, new Tile(3307,5297, party.getHeight()), party.getSize());
        //Spawn vespula
        World.getWorld().registerNpc(vespula);
        party.monsters.add(vespula);
    }
    private void spawnVasaNistirio() {
        //Get the raids party
        Party party = player.raidsParty;

        //Create vespula
        Npc vasanistirio = new ChamberOfSecretsNpc(VASA_NISTIRIO, new Tile(3343,5263, party.getHeight()), party.getSize());
        //Spawn vespula
        World.getWorld().registerNpc(vasanistirio);
        party.monsters.add(vasanistirio);
    }
    private void spawnTekton() {
        //Get the raids party
        Party party = player.raidsParty;

        //Create tekton
        Npc tekton = new ChamberOfSecretsNpc(7542, new Tile(3311,5295, party.getHeight()+1), party.getSize());

        //Spawn tekton
        World.getWorld().registerNpc(tekton);
        party.monsters.add(tekton);

    }
    private void clearParty() {
        Party party = player.raidsParty;
        if(party == null) return;
        if(party.monsters == null) {
            return;
        }
        for(Npc npc : party.monsters) {
            //If npc is alive remove them
            if(npc.isRegistered() || !npc.dead()) {
                World.getWorld().unregisterNpc(npc);
            }
        }
        party.monsters.clear();
    }
    public void deleteRaidsItems(){
        final int[] raids_items = new int[] { 20799,590,1351,20996,20995,20994,20993,25765,25764,25763,25762,20972,20971,20970,20969,
            20976,20975,20974,20973  };
        for (int raids_item : raids_items) {
            if (player.inventory().contains(new Item(raids_item)))
                player.inventory().remove(new Item(raids_item, 28), true);

        }
    }
}
