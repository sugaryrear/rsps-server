package com.ferox.game.content.areas.home;

import com.ferox.db.transactions.CollectPayments;
import com.ferox.game.content.areas.burthope.rogues_den.dialogue.BrianORichard;
import com.ferox.game.content.areas.edgevile.IronManTutor;
import com.ferox.game.content.areas.edgevile.dialogue.GeneralStoreDialogue;
import com.ferox.game.content.areas.edgevile.dialogue.PaymentManagerDialogue;
import com.ferox.game.content.areas.edgevile.dialogue.VotePollDialogue;
import com.ferox.game.content.packet_actions.interactions.objects.Ladders;
import com.ferox.game.content.tradingpost.TradingPost;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.dialogue.Expression;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.route.StepType;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;

import java.util.Arrays;

import static com.ferox.util.NpcIdentifiers.*;
import static com.ferox.util.ObjectIdentifiers.STAIRCASE_25801;
import static com.ferox.util.ObjectIdentifiers.STAIRCASE_25935;

/**
 * @author Patrick van Elderen | April, 23, 2021, 10:49
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class HomeArea extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if(option == 1) {
            if(object.getId() == 14398) {
                if(object.tile().equals(2037,3621,0)) {
                    player.lockDelayDamage();
                    Chain.bound(player).name("FaladorTightrope1Task").runFn(1, () -> {
                        player.looks().render(763, 762, 762, 762, 762, 762, -1);
                        player.agilityWalk(false);
                        player.stepAbs(2053, 3621, StepType.FORCE_WALK);
                    }).waitForTile(new Tile(2053, 3621), () -> {
                        player.agilityWalk(true);
                        player.looks().resetRender();
                        player.skills().addXp(Skills.AGILITY, 17.0);
                        player.unlock();
                    });
                    return true;
                }
                player.lockDelayDamage();
                Chain.bound(player).name("FaladorTightrope1Task").runFn(1, () -> {
                    player.looks().render(763, 762, 762, 762, 762, 762, -1);
                    player.agilityWalk(false);
                    player.stepAbs(2037, 3621, StepType.FORCE_WALK);
                }).waitForTile(new Tile(2037, 3621), () -> {
                    player.agilityWalk(true);
                    player.looks().resetRender();
                    player.skills().addXp(Skills.AGILITY, 17.0);
                    player.unlock();
                });
                return true;
            }

            if(object.definition().name.contains("Bank Deposit Box") ){
             //   System.out.println("ba: "+object.definition().name);
                player.getDepositBox().open();
                return true;
            }
            if(object.definition().name.contains("Voting")){
                player.getDialogueManager().start(new VotePollDialogue());
                return true;
            }
            if(object.getId() == 34446) {
                player.getSlayerKey().open();
                return true;
            }
            if(object.getId() == 23311) {
                player.getPacketSender().sendString(29078, "World Teleports - Favourite");
                player.setCurrentTabIndex(1);
                player.getTeleportInterface().displayFavorites();
                player.getInterfaceManager().open(29050);
                return true;
            }
            if (object.getId() == STAIRCASE_25801) {
                Ladders.ladderDown(player, new Tile(2021, 3567, 0), true);
                return true;
            }
            if (object.getId() == STAIRCASE_25935) {
                Ladders.ladderUp(player, new Tile(2021, 3567, 1), true);
                return true;
            }
            if (object.getId() == 14315) {
               player.message("@red@disabled");
                return true;
            }
        }
        if(option == 2) {
        }
        if(option == 3){
            if(object.getId() == 10355) {
                TradingPost.open(player);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if(option == 1) {
            if(npc.id() == GERRANT_2891) {
                World.getWorld().shop(46).open(player);
                return true;
            }
            if(npc.id() == 2110) {
                World.getWorld().shop(20).open(player);
                return true;
            }
//            if(npc.id() == 1633) {
//                World.getWorld().shop(46).open(player);
//                return true;
//            }
            if(npc.id() == 2148) {
                TradingPost.open(player);
                return true;
            }

            if(npc.id() == GRAND_EXCHANGE_CLERK) {
                TradingPost.open(player);
                return true;
            }
            if(npc.id() == TRAIBORN) {
                World.getWorld().shop(30).open(player);
                return true;
            }
            if(npc.id() == GUNNJORN) {
                World.getWorld().shop(43).open(player);
                return true;
            }
            if(npc.id() == DONATOR_STORE){
                player.getDialogueManager().start(new PaymentManagerDialogue());
                return true;
            }
            if(npc.id() == ZORGOTH){
                player.getDialogueManager().start(new Dialogue() {
                    @Override
                    protected void start(Object... options) {
                        send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "How many boss points do I have?", "Open shop");
                        setPhase(0);
                    }
                    @Override
                    protected void next() {
                        if (isPhase(0)) {
                            int bossPoints = player.getAttribOr(AttributeKey.BOSS_POINTS, 0);
                            String value = Utils.formatNumber(bossPoints);
                            send(DialogueType.NPC_STATEMENT, ZORGOTH, Expression.HAPPY, "You have "+value+" boss points.");
                            setPhase(1);
                        }
                        else if (isPhase(1)) {
                            stop();
                        }
                    }
                    @Override
                    public void select(int option) {
                        if (isPhase(0)) {
                            if (option == 1) {
                                int bossPoints = player.getAttribOr(AttributeKey.BOSS_POINTS, 0);
                                String value = Utils.formatNumber(bossPoints);
                                send(DialogueType.NPC_STATEMENT, ZORGOTH, Expression.HAPPY, "You have "+value+" boss points.");
                                setPhase(1);
                            } else if (option == 2) {
                                World.getWorld().shop(47).open(player);

                                stop();
                            }
                        }
                    }
                });
                return true;
            }
            if(npc.id() == PK_STORE){
                player.getDialogueManager().start(new Dialogue() {
                    @Override
                    protected void start(Object... parameters) {
                        send(DialogueType.PLAYER_STATEMENT, Expression.HAPPY, "How do I get blood money?");
                        setPhase(0);
                    }

                    @Override
                    protected void next() {
                        if (isPhase(0)) {
                            send(DialogueType.NPC_STATEMENT, PK_STORE, Expression.HAPPY, "You can get blood money from bosses or killing players.");
                            setPhase(1);
                        }
                        else if (isPhase(1)) {
                            stop();
                        }
                    }


                    @Override
                    protected void select(int option) {

                    }
                });

                return true;
            }
            if(npc.id() == IRON_MAN_TUTOR) {
                player.getDialogueManager().start(new IronManTutor());
                return true;
            }
            if(npc.id() == MAGIC_COMBAT_TUTOR) {
                World.getWorld().shop(30).open(player);
                return true;
            }
            if(npc.id() == MELEE_COMBAT_TUTOR) {
                World.getWorld().shop(33).open(player);
                return true;
            }
            if(npc.id() == RANGED_COMBAT_TUTOR) {
                World.getWorld().shop(36).open(player);
                return true;
            }
            if(npc.id() == SHOP_ASSISTANT_2820 || npc.id() == 2816 || npc.id() == 2815) {
                player.getDialogueManager().start(new GeneralStoreDialogue());

                return true;
            }
        }
        if(option == 2) {
            if(npc.id() == DONATOR_STORE){
                World.getWorld().shop(43).open(player);
                player.getPacketSender().sendConfig(1125, 1);
                player.getPacketSender().sendConfig(1126, 0);
                player.getPacketSender().sendConfig(1127, 0);
                return true;
            }
            if(npc.id() == SHOP_ASSISTANT_2820 || npc.id() == 2816 || npc.id() == 2815) {
                World.getWorld().shop(1).open(player);
                player.getPacketSender().sendConfig(1125, 1);
                player.getPacketSender().sendConfig(1126, 0);
                player.getPacketSender().sendConfig(1127, 0);
                return true;
            }
            if(npc.id() == PK_STORE){
                World.getWorld().shop(4).open(player);
                player.getPacketSender().sendConfig(1125, 1);
                player.getPacketSender().sendConfig(1126, 0);
                player.getPacketSender().sendConfig(1127, 0);
                return true;
            }
            if(npc.id() == GERRANT_2891) {
                World.getWorld().shop(46).open(player);
                return true;
            }
            if(npc.id() == TRAIBORN) {
                World.getWorld().shop(29).open(player);
                return true;
            }
            if(npc.id() == GUNNJORN) {
                World.getWorld().shop(32).open(player);
                return true;
            }
            if(npc.id() == MAGIC_COMBAT_TUTOR) {
                World.getWorld().shop(29).open(player);
                return true;
            }
            if(npc.id() == MELEE_COMBAT_TUTOR) {
                World.getWorld().shop(32).open(player);
                return true;
            }
            if(npc.id() == RANGED_COMBAT_TUTOR) {
                World.getWorld().shop(35).open(player);
                return true;
            }
            if(npc.id() == GRUM_2889) {
                World.getWorld().shop(38).open(player);
                return true;
            }
            if(npc.id() == JATIX) {
                World.getWorld().shop(40).open(player);
                return true;
            }
        }
        if(option == 3) {
            if(npc.id() == DONATOR_STORE){
                CollectPayments.INSTANCE.collectPayments(player);
            }
            if(npc.id() == TRAIBORN) {
                World.getWorld().shop(31).open(player);
                return true;
            }
            if(npc.id() == GUNNJORN) {
                World.getWorld().shop(34).open(player);
                return true;
            }
            if(npc.id() == RANGED_COMBAT_TUTOR) {
                World.getWorld().shop(37).open(player);
                return true;
            }
        }
        if(option == 4) {

        }
        return false;
    }
}
