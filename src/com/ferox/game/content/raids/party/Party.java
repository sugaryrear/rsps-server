package com.ferox.game.content.raids.party;

import com.ferox.game.content.raids.chamber_of_secrets.ChamberOfSecrets;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.ground.GroundItem;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.position.areas.impl.RaidsArea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.ferox.game.world.entity.AttributeKey.PERSONAL_POINTS;
import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | April, 26, 2021, 16:56
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class Party {

    public static final List<Item> COX_REWARDS = Arrays.asList(new Item(DEXTEROUS_PRAYER_SCROLL), new Item(ARCANE_PRAYER_SCROLL), new Item(TWISTED_BUCKLER), new Item(DRAGON_HUNTER_CROSSBOW), new Item(DINHS_BULWARK), new Item(ANCESTRAL_HAT), new Item(ANCESTRAL_ROBE_TOP), new Item(ANCESTRAL_ROBE_BOTTOM), new Item(DRAGON_CLAWS), new Item(ELDER_MAUL), new Item(KODAI_WAND), new Item(TWISTED_BOW));
    public static final List<Item> TOB_REWARDS = Arrays.asList(new Item(AVERNIC_DEFENDER), new Item(GHRAZI_RAPIER), new Item(SANGUINESTI_STAFF), new Item(JUSTICIAR_FACEGUARD), new Item(JUSTICIAR_LEGGUARDS), new Item(JUSTICIAR_CHESTGUARD), new Item(SCYTHE_OF_VITUR), new Item(SCYTHE_OF_VITUR_KIT), new Item(TWISTED_BOW_KIT));
    public static final List<Item> HP_REWARDS = Arrays.asList(new Item(ELDER_WAND_HANDLE), new Item(ELDER_WAND_STICK), new Item(SWORD_OF_GRYFFINDOR), new Item(TALONHAWK_CROSSBOW), new Item(SALAZAR_SLYTHERINS_LOCKET));

    public static final int REWARDS_CONTAINER_ID = 12137;
    public static final int TOTAL_POINTS = 12003;
    public static final int NAME_FRAME = 12004;
    public static final int POINTS = 12005;
    public static final int TIME = 12006;

    public static final int COS_CONFIG_ID = 1123;
    public static final int TOB_CONFIG_ID = 1124;
    public static final int HP_CONFIG_ID = 1125;
    private static final int PARTY_INTERFACE = 12100;
    private static final int LEADER_FRAME = 12117;

    private Player leader;
    private final List<Player> members;
    public ArrayList<Npc> monsters = new ArrayList<>();
    public ArrayList<Npc> vespulaadds = new ArrayList<>();

    public ArrayList<Npc> getVespulaAdds() {
        return vespulaadds;
    }




    private ChamberOfSecrets raid;
    private int raidsSelected = 0;
    private int height;
    private int kills;
    private int raidStage = 0;
    private boolean bossFightStarted;
    private boolean vespulaAddsSpawned;
    private int brazierslit = 0;
    public int getBraziersLit() {
        return brazierslit;
    }
    public void addBraziersLit(int brazierslit) {
        this.brazierslit+=brazierslit;
    }
    public void setBraziersLit(int brazierslit) {
        this.brazierslit = brazierslit;
    }
    public void setlastboss(boolean lastboss) {
        this.lastboss = lastboss;
    }
    public boolean lastboss;
    public Party(Player leader) {
        this.leader = leader;
        members = new ArrayList<>();
        members.add(leader);
    }

    public Player getLeader() {
        return leader;
    }

    public void setLeader(Player leader) {
        this.leader = leader;
    }

    public List<Player> getMembers() {
        return members;
    }

    public int getSize() {
        return members.size();
    }

    public void addMember(Player player) {
        members.add(player);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setRaidStage(int raidStage) {
        this.raidStage = raidStage;
    }

    public int getRaidStage() {
        return raidStage;
    }

    public boolean isComplete() {
        return raidStage == 8;
    }

    public boolean bossFightStarted() {
        return bossFightStarted;
    }
    public boolean vespulaAddsSpawned() {
        return vespulaAddsSpawned;
    }
    public void vespulaAddsSpawned(boolean vespulaAddsSpawned) {
        this.vespulaAddsSpawned = vespulaAddsSpawned;
    }
    public void bossFightStarted(boolean bossFightStarted) {
        this.bossFightStarted = bossFightStarted;
    }

    public int getRaidsSelected() {
        return raidsSelected;
    }

    public void setRaidsSelected(int raidsSelected) {
        this.raidsSelected = raidsSelected;
    }

    public void removeMember(Player player) {
        members.remove(player);
        if (members.size() > 0 && player == leader) {
            leader = members.get(0);
        }
    }

    public Player randomPartyPlayer() {
        //Grab a random player from the list
        int index = World.getWorld().random(this.getMembers().size() - 1);
        if(index == 0) {
            return this.getMembers().get(0);
        }
        return this.getMembers().get(index);
    }

    public ChamberOfSecrets getHPRaid() {
        return raid;
    }

    public void setRaid(ChamberOfSecrets raid) {
        this.raid = raid;
    }

    public void forPlayers(Consumer<Player> action) {
        members.forEach(action);
    }

    public void addPersonalPoints(Player player, int points) {
        boolean centaurPet = player.hasPetOut("Centaur");
        boolean olmletPet = player.hasPetOut("Olmlet");
        boolean eliteMember = player.getMemberRights().isEliteMemberOrGreater(player);
        boolean extremeMember = player.getMemberRights().isExtremeMemberOrGreater(player);
        boolean LegendaryMember = player.getMemberRights().isLegendaryMemberOrGreater(player);
        boolean vipMember = player.getMemberRights().isVIPOrGreater(player);
        boolean sponsorMember = player.getMemberRights().isSponsorOrGreater(player);

        var percentageBoost = 0;
        if(centaurPet || olmletPet) {
            percentageBoost += 10;
        }

        if(eliteMember) {
            percentageBoost += 5;
        } else if(extremeMember) {
            percentageBoost += 10;
        } else if(LegendaryMember) {
            percentageBoost += 15;
        } else if(vipMember) {
            percentageBoost += 20;
        } else if(sponsorMember) {
            percentageBoost += 25;
        }

        var extraPoints = points * percentageBoost / 100;
        points += extraPoints;
        var increaseBy = player.<Integer>getAttribOr(PERSONAL_POINTS, 0) + points;
        player.putAttrib(PERSONAL_POINTS, increaseBy);
    }

    public int totalPoints() {
        return members.stream().mapToInt(m -> m.<Integer>getAttribOr(PERSONAL_POINTS, 0)).sum();
    }

    public void teamMessage(String message) {
        forPlayers(p -> p.message(message));
    }

    private static void clearInterface(Player player) {
        //Party members
        for (int i = 12117; i <= 12121; i++) {
            player.getPacketSender().sendString(i, "");
        }
    }

    public static void createParty(Player player) {
        player.raidsParty = new Party(player);
    }

    public static void openPartyInterface(Player player, boolean updateMembers) {
        clearInterface(player);//Clear previous frames

        //Default COX
        player.getPacketSender().sendConfig(COS_CONFIG_ID, 1).sendConfig(TOB_CONFIG_ID,0).sendConfig(HP_CONFIG_ID,0);
        player.getPacketSender().sendItemOnInterface(REWARDS_CONTAINER_ID, COX_REWARDS);

        //Set leader info
        Player partyLeader = player.raidsParty.getLeader();
        player.getPacketSender().sendString(12103, "Raiding party setup - " + partyLeader.getUsername() + "'s party");
        player.getPacketSender().sendString(LEADER_FRAME, "<col=ffffff>" + partyLeader.getUsername());

        //Set the raids we're going to enter, by default COX
        Party party = partyLeader.raidsParty;
        party.raidsSelected = 0;

        //Open interface
        player.getInterfaceManager().open(PARTY_INTERFACE);

        if (updateMembers)
            displayPartyMembers(player, player.raidsParty);
    }

    public static void displayPartyMembers(Player player, Party party) {
        if (party == null) {
            return;
        }

        if (party.getMembers().size() != 0) {
            for (int i = 0; i < party.getMembers().size(); i++) {
                if (party.getMembers().get(i) == party.getLeader())
                    continue;
                player.getPacketSender().sendString(LEADER_FRAME + i, "" + "<col=9f9f9f>" + party.getMembers().get(i).getUsername());
            }
        }
    }

    public static void refreshInterface(Player leader, Party party) {
        if (party == null) {
            return;
        }

        for (Player partyMembers : party.getMembers()) {
            //Clear ghost entries
            for (int i = 0; i < 4; i++) {
                partyMembers.getPacketSender().sendString(LEADER_FRAME + i, "");
                leader.getPacketSender().sendString(LEADER_FRAME + i, "");
            }

            //Shift party members
            for (int i = 0; i < party.getMembers().size(); i++) {
                if (leader.raidsParty != null) {
                    partyMembers.getPacketSender().sendString(LEADER_FRAME + i, "" + "<col=9f9f9f>" + leader.raidsParty.getMembers().get(i).getUsername());
                    leader.getPacketSender().sendString(LEADER_FRAME + i, "" + "<col=9f9f9f>" + leader.raidsParty.getMembers().get(i).getUsername());
                    partyMembers.getPacketSender().sendConfig(COS_CONFIG_ID, leader.raidsParty.raidsSelected == 0 ? 1 : 0).sendConfig(TOB_CONFIG_ID, leader.raidsParty.raidsSelected == 1 ? 1 : 0).sendConfig(HP_CONFIG_ID, leader.raidsParty.raidsSelected == 2 ? 1 : 0);
                    partyMembers.getPacketSender().sendItemOnInterface(REWARDS_CONTAINER_ID, leader.raidsParty.raidsSelected == 0 ? COX_REWARDS : leader.raidsParty.raidsSelected == 1 ? TOB_REWARDS : HP_REWARDS);
                }
            }
        }
    }

    public static void kick(Player player, int index) {
        Party party = player.raidsParty;
        if (party == null) {
            return;
        }

        //There has to be a party member
        if (party.getMembers().size() < index + 1) {
            player.message("There is no member to kick.");
            return;
        }

        //We have at least 2 party members (leader included), check if we are the leader.
        if (party.getLeader() != player) {
            player.message("Only the leader of this party can kick members.");
            return;
        }

        //We are the leader lets continue and grab the party member in the list.
        Player partyMember = party.getMembers().get(index);
        if (partyMember != null) {
            party.removeMember(partyMember);
            partyMember.getInterfaceManager().close();
            player.message(partyMember.getUsername() + " has been successfully kicked from the party.");
            partyMember.message("You have been kicked out the raids party.");
            refreshInterface(party.getLeader(), party);
        }
    }

    public static void leaveParty(Player player, boolean destroyFromBoard) {
        Party party = player.raidsParty;
        if (party == null) {
            return;
        }
        Player partyLeader = party.getLeader();
        if (partyLeader == player) {
            disbandParty(player, destroyFromBoard);
            return;
        }
        player.message("<col=ef20ff>You leave " + player.raidsParty.getLeader().getUsername() + "'s party.");
        party.removeMember(player);
        player.raidsParty = null;
        refreshInterface(partyLeader, partyLeader.raidsParty);
        player.getInterfaceManager().close();
    }

    public static void disbandParty(Player player, boolean destroyFromBoard) {
        /* terminate the party */
        final var raidsParty = player.raidsParty;
        if (player.raidsParty.getLeader() == player) {

            /* remove all the players from the party */
            for (Player member : player.raidsParty.getMembers()) {
                member.message("<col=ef20ff>" + player.getUsername() + " has disbanded the party.");
                member.raidsParty = null;
                if (!destroyFromBoard)
                    member.getChamberOfSecrets().exit();
            }
        }
        raidsParty.members.clear();
    }

    public static void onLogout(Player player) {
        if (player.raidsParty != null) {
            leaveParty(player, false);
            player.getChamberOfSecrets().exit();
        }
    }

    public static void startRaid(Player p) {
        Party party = p.raidsParty;
        if (party.getLeader() != p) {
            p.message("Only the party leader can start the fight.");
            return;
        }

        if(party.getRaidsSelected() == 1 || party.getRaidsSelected() == 2) {
            p.message("You can only raid Chambers of Xeric.");
            return;
        }

        p.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Start raid (normal)", "Start raid (skip to last boss)");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if (isPhase(0)) {
                    if (option == 1) {
                        Party party = p.raidsParty;
                        if (party == null) {
                            stop();
                            return;
                        }
                        stop();
                        if(party.getRaidsSelected() == 0) {
                            ChamberOfSecrets raid = new ChamberOfSecrets(p);
                            party.setRaid(raid);
                        } else if(party.getRaidsSelected() == 1) {

                        } else if(party.getRaidsSelected() == 2) {
//                            ChamberOfSecrets raid = new ChamberOfSecrets(p);
//                            party.setRaid(raid);
                        }

                        party.getMembers().forEach(member -> member.message("<col=ef20ff>The raid has begun!"));
                        p.getInterfaceManager().close();
                        party.getHPRaid().startHPRaid();
                    }
                    if (option == 2) {
                    //    stop();
                        Party party = p.raidsParty;
                        if (party == null) {
                            stop();
                            return;
                        }
                        stop();
                        if(party.getRaidsSelected() == 0) {
                            ChamberOfSecrets raid = new ChamberOfSecrets(p);
                            party.setRaid(raid);
                        } else if(party.getRaidsSelected() == 1) {

                        } else if(party.getRaidsSelected() == 2) {
//                            ChamberOfSecrets raid = new ChamberOfSecrets(p);
//                            party.setRaid(raid);
                        }

                        party.getMembers().forEach(member -> member.message("<col=ef20ff>The raid has begun!"));
                        p.getInterfaceManager().close();
                        party.getHPRaid().startHPRaidlastboss();
                    }
                }
            }
        });
    }

    public int getPlayersInRaidsLocation(Party party) {
        int inRaids = 0;
        if (party == null) {
            return 0;
        }
        for (Player member : party.getMembers()) {
            if (member.getController() != null && member.getController() instanceof RaidsArea) {
                inRaids++;
            }
        }
        return inRaids;
    }

    public int getPlayersInOlmRoom(Party party) {
        int inRaids = 0;
        if (party == null) {
            return 0;
        }
        for (Player member : party.getMembers()) {
            if (member != null && member.tile().inArea(member.getChamberOfSecrets().OLM_ROOM)){
                inRaids++;
            }
        }
        return inRaids;
    }
    private Tile greatolmTile = new Tile(3238, 5738, height);
    private Tile leftHandTile = new Tile(3238, 5733, height);
    private Tile rightHandTile = new Tile(3238, 5743, height);

    Npc leftHandNpc;
    Npc greatolmNpc;
    Npc rightHandNpc;

    GameObject greatolmObject = new GameObject(29881, greatolmTile, 10, 1);
    GameObject leftHandObject = new GameObject(29884, leftHandTile, 10, 1);
    GameObject rightHandObject = new GameObject(29887, rightHandTile, 10, 1);

    private boolean canAttackLeftHand;
    private boolean transitionPhase;

    private int currentPhase = 0;

    private int phaseAmount;
    private boolean olmTurning;
    private boolean clenchedHand;

    private final Tile[] crystalBursts = new Tile[5];
    private final Tile[] lightningSpots = new Tile[5];

    private int crystalAmount;

    private int olmAttackTimer;
    private int leftHandAttackTimer;
    private boolean canAttack;
    private boolean canAttackHand;
    private Tile swapTile;

    private final ArrayList<Player> swapPlayers = new ArrayList<>();

    private boolean leftHandDead;
    private boolean rightHandDead;

    private int graphicSwap;
    private boolean lonePair;

    private int attackCount;

    private boolean leftHandProtected;

    private int currentLeftHandCycle;
    private boolean healingOlmLeftHand;

    private Player fireWallPlayer;
    private final ArrayList<Npc> fireWallNpcs = new ArrayList<>();

    private Npc fireWallSpawn;
    private Npc fireWallSpawn1;

    private ArrayList<String> phaseAttack = new ArrayList<>();

    private boolean lastPhaseStarted;

    private boolean clenchedHandFirst;
    private boolean clenchedHandSecond;

    private boolean unClenchedHandFirst;

    private boolean unClenchedHandSecond;

    private boolean switchingPhases;
    private boolean olmAttacking;
    private boolean switchAfterAttack;

    public boolean isSwitchAfterAttack() {
        return switchAfterAttack;
    }

    public void setSwitchAfterAttack(boolean switchAfterAttack) {
        this.switchAfterAttack = switchAfterAttack;
    }

    public boolean isOlmAttacking() {
        return olmAttacking;
    }

    public void setOlmAttacking(boolean olmAttacking) {
        this.olmAttacking = olmAttacking;
    }

    public boolean isSwitchingPhases() {
        return switchingPhases;
    }

    public void setSwitchingPhases(boolean switchingPhases) {
        this.switchingPhases = switchingPhases;
    }

    public boolean isUnClenchedHandFirst() {
        return unClenchedHandFirst;
    }

    public void setUnClenchedHandFirst(boolean unClenchedHandFirst) {
        this.unClenchedHandFirst = unClenchedHandFirst;
    }

    public boolean isUnClenchedHandSecond() {
        return unClenchedHandSecond;
    }

    public void setUnClenchedHandSecond(boolean unClenchedHandSecond) {
        this.unClenchedHandSecond = unClenchedHandSecond;
    }

    public boolean isClenchedHandFirst() {
        return clenchedHandFirst;
    }

    public void setClenchedHandFirst(boolean clenchedHandFirst) {
        this.clenchedHandFirst = clenchedHandFirst;
    }

    public boolean isClenchedHandSecond() {
        return clenchedHandSecond;
    }

    public void setClenchedHandSecond(boolean clenchedHandSecond) {
        this.clenchedHandSecond = clenchedHandSecond;
    }

    public boolean isLastPhaseStarted() {
        return lastPhaseStarted;
    }

    public void setLastPhaseStarted(boolean lastPhaseStarted) {
        this.lastPhaseStarted = lastPhaseStarted;
    }

    public ArrayList<String> getPhaseAttack() {
        return phaseAttack;
    }

    public void setPhaseAttack(ArrayList<String> phaseAttack) {
        this.phaseAttack = phaseAttack;
    }

    public Npc getFireWallSpawn() {
        return fireWallSpawn;
    }

    public void setFireWallSpawn(Npc fireWallSpawn) {
        this.fireWallSpawn = fireWallSpawn;
    }

    public Npc getFireWallSpawn1() {
        return fireWallSpawn1;
    }

    public void setFireWallSpawn1(Npc fireWallSpawn1) {
        this.fireWallSpawn1 = fireWallSpawn1;
    }

    public ArrayList<Npc> getFireWallNpcs() {
        return fireWallNpcs;
    }

    public Player getFireWallPlayer() {
        return fireWallPlayer;
    }

    public void setFireWallPlayer(Player fireWallPlayer) {
        this.fireWallPlayer = fireWallPlayer;
    }

    public boolean isHealingOlmLeftHand() {
        return healingOlmLeftHand;
    }

    public void setHealingOlmLeftHand(boolean healingOlmLeftHand) {
        this.healingOlmLeftHand = healingOlmLeftHand;
    }

    public int getCurrentLeftHandCycle() {
        return currentLeftHandCycle;
    }

    public void setCurrentLeftHandCycle(int currentLeftHandCycle) {
        this.currentLeftHandCycle = currentLeftHandCycle;
    }

    public boolean isLeftHandProtected() {
        return leftHandProtected;
    }

    public void setLeftHandProtected(boolean leftHandProtected) {
        this.leftHandProtected = leftHandProtected;
    }

    public int getAttackCount() {
        return attackCount;
    }

    public void setAttackCount(int attackCount) {
        this.attackCount = attackCount;
    }

    private final ArrayList<Player> burnPlayers = new ArrayList<>();

    public ArrayList<Player> getBurnPlayers() {
        return burnPlayers;
    }

    public boolean isLonePair() {
        return lonePair;
    }

    public void setLonePair(boolean lonePair) {
        this.lonePair = lonePair;
    }

    public int getGraphicSwap() {
        return graphicSwap;
    }

    public void setGraphicSwap(int graphicSwap) {
        this.graphicSwap = graphicSwap;
    }

    public Tile getSwapTile() {
        return swapTile;
    }

    public void setSwapTile(Tile swapTile) {
        this.swapTile = swapTile;
    }

    public ArrayList<Player> getSwapPlayers() {
        return swapPlayers;
    }

    public boolean isClenchedHand() {
        return clenchedHand;
    }

    public void setClenchedHand(boolean clenchedHand) {
        this.clenchedHand = clenchedHand;
    }

    public boolean isLeftHandDead() {
        return leftHandDead;
    }

    public void setLeftHandDead(boolean leftHandDead) {
        this.leftHandDead = leftHandDead;
    }

    public boolean isRightHandDead() {
        return rightHandDead;
    }

    public void setRightHandDead(boolean rightHandDead) {
        this.rightHandDead = rightHandDead;
    }

    public boolean isCanAttackHand() {
        return canAttackHand;
    }

    public void setCanAttackHand(boolean canAttackHand) {
        this.canAttackHand = canAttackHand;
    }

    public int getLeftHandAttackTimer() {
        return leftHandAttackTimer;
    }

    public void setLeftHandAttackTimer(int leftHandAttackTimer) {
        this.leftHandAttackTimer = leftHandAttackTimer;
    }

    public Tile[] getLightningSpots() {
        return lightningSpots;
    }

    public boolean getCanAttack() {
        return canAttack;
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }

    public boolean getOlmTurning() {
        return olmTurning;
    }

    public void setOlmTurning(boolean olmTurning) {
        this.olmTurning = olmTurning;
    }

    public int getOlmAttackTimer() {
        return olmAttackTimer;
    }

    public void setOlmAttackTimer(int olmAttackTimer) {
        this.olmAttackTimer = olmAttackTimer;
    }

    public int getCrystalAmount() {
        return crystalAmount;
    }

    public void setCrystalAmount(int crystalAmount) {
        this.crystalAmount = crystalAmount;
    }

    public Tile[] getCrystalBursts() {
        return crystalBursts;
    }

    private ArrayList<Player> playersToAttack = new ArrayList<>();

    public ArrayList<Player> getPlayersToAttack() {
        return playersToAttack;
    }

    public void setPlayersToAttack(ArrayList<Player> playersToAttack) {
        this.playersToAttack = playersToAttack;
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(int currentPhase) {
        this.currentPhase = currentPhase;
    }

    public int getPhaseAmount() {
        return phaseAmount;
    }

    public void setPhaseAmount(int phaseAmount) {
        this.phaseAmount = phaseAmount;
    }

    public boolean isTransitionPhase() {
        return transitionPhase;
    }

    public void setTransitionPhase(boolean transitionPhase) {
        this.transitionPhase = transitionPhase;
    }

    public boolean isCanAttackLeftHand() {
        return canAttackLeftHand;
    }

    public void setCanAttackLeftHand(boolean canAttackLeftHand) {
        this.canAttackLeftHand = canAttackLeftHand;
    }

    public GameObject getGreatOlmObject() {
        return greatolmObject;
    }

    public GameObject getLeftHandObject() {
        return leftHandObject;
    }

    public GameObject getRightHandObject() {
        return rightHandObject;
    }

    public void setGreatOlmObject(GameObject greatolmObject) {
        this.greatolmObject = greatolmObject;
    }

    public void setLeftHandObject(GameObject leftHandObject) {
        this.leftHandObject = leftHandObject;
    }

    public void setRightHandObject(GameObject rightHandObject) {
        this.rightHandObject = rightHandObject;
    }

    public Tile getGreatOlmTile() {
        return greatolmTile;
    }

    public void setGreatOlmTile(Tile GreatOlmTile) {
        greatolmTile = GreatOlmTile;
    }

    public Tile getLeftHandTile() {
        return leftHandTile;
    }

    public void setLeftHandTile(Tile LeftHandTile) {
        leftHandTile = LeftHandTile;
    }

    public Tile getRightHandTile() {
        return rightHandTile;
    }

    public void setRightHandTile(Tile RightHandTile) {
        rightHandTile = RightHandTile;
    }

    public Npc getLeftHandNpc() {
        return leftHandNpc;
    }

    public void setLeftHandNpc(Npc leftHandNpc) {
        this.leftHandNpc = leftHandNpc;
    }

    public Npc getGreatOlmNpc() {
        return greatolmNpc;
    }

    public void setGreatOlmNpc(Npc greatolmNpc) {
        this.greatolmNpc = greatolmNpc;
    }

    public Npc getRightHandNpc() {
        return rightHandNpc;
    }

    public void setRightHandNpc(Npc rightHandNpc) {
        this.rightHandNpc = rightHandNpc;
    }
}
