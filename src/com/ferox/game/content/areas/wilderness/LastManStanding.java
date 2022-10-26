package com.ferox.game.content.areas.wilderness;

import com.ferox.game.content.areas.edgevile.IronManTutor;
import com.ferox.game.content.duel.Dueling;
import com.ferox.game.content.items.combine.PvPCombining;
import com.ferox.game.content.mechanics.MagicalAltarDialogue;
import com.ferox.game.content.mechanics.Poison;
import com.ferox.game.content.tournaments.TournamentManager;
import com.ferox.game.content.tradingpost.TradingPost;
import com.ferox.game.task.TaskManager;
import com.ferox.game.task.impl.ForceMovementTask;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.combat.CombatSpecial;
import com.ferox.game.world.entity.combat.Venom;
import com.ferox.game.world.entity.mob.movement.MovementQueue;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.ForceMovement;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import com.ferox.util.chainedwork.Chain;
import com.ferox.util.timers.TimerKey;

import static com.ferox.util.ItemIdentifiers.*;
import static com.ferox.util.NpcIdentifiers.*;
import static com.ferox.util.NpcIdentifiers.RANGED_COMBAT_TUTOR;
import static com.ferox.util.ObjectIdentifiers.ORNATE_REJUVENATION_POOL;
import static com.ferox.util.ObjectIdentifiers.STEPPING_STONE_14918;

public class LastManStanding extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {

            if (obj.getId() == 39640) {
                TournamentManager.openTournamentWidget(player);
                return true;
            }
            if (obj.getId() == 31858) {
                player.getDialogueManager().start(new MagicalAltarDialogue());
                return true;
            }
            if(obj.getId() == 39652 || obj.getId() == 39653) {
                Tile movetotile = new Tile(0,0);
                if (player.tile().x == 3154) {
                    movetotile = new Tile(player.tile().x + 1, obj.tile().y);
                    //  player.getMovementQueue().step(player.tile().x + 1, obj.tile().y, MovementQueue.StepType.FORCED_WALK);

                } else if (player.tile().x == 3155) {
                    movetotile = new Tile(player.tile().x - 1, obj.tile().y);
                    // player.getMovementQueue().step(player.tile().x - 1, obj.tile().y, MovementQueue.StepType.FORCED_WALK);

                } else if (player.tile().y == 3639) {
                    movetotile = new Tile(player.tile().x, obj.tile().y+1);
                    //  player.getMovementQueue().step(player.tile().x, obj.tile().y + 1, MovementQueue.StepType.FORCED_WALK);
                } else if (player.tile().y == 3640) {
                    movetotile = new Tile(player.tile().x, obj.tile().y-1);
                    //   player.getMovementQueue().step(player.tile().x, obj.tile().y - 1, MovementQueue.StepType.FORCED_WALK);
                } else if (player.tile().x == 3123) {
                    movetotile = new Tile(player.tile().x-1, obj.tile().y);

                    //player.getMovementQueue().step(player.tile().x - 1, obj.tile().y, MovementQueue.StepType.FORCED_WALK);
                } else if (player.tile().x == 3122) {
                    movetotile = new Tile(player.tile().x+1, obj.tile().y);
                    // player.getMovementQueue().step(player.tile().x + 1, obj.tile().y, MovementQueue.StepType.FORCED_WALK);
                } else if (player.tile().y == 3617) {
                    movetotile = new Tile(player.tile().x, obj.tile().y-1);
                    //
                    //  player.getMovementQueue().step(player.tile().x, obj.tile().y - 1, MovementQueue.StepType.FORCED_WALK);
                } else if (player.tile().y == 3616) {
                    movetotile = new Tile(player.tile().x, obj.tile().y+1);
                    // player.getMovementQueue().step(player.tile().x, obj.tile().y + 1, MovementQueue.StepType.FORCED_WALK);
                }
                player.getMovementQueue().interpolate(movetotile, MovementQueue.StepType.FORCED_RUN);

                return true;
            }
            if (obj.getId() == 39651) {
                Chain.bound(null).name("RejuvenationPoolTask").runFn(1, () -> player.animate(7305)).then(2, () -> {
                    player.lock();
                    player.message("<col=" + Color.BLUE.getColorValue() + ">You have restored your hitpoints, run energy and prayer.");
                    player.message("<col=" + Color.HOTPINK.getColorValue() + ">You've also been cured of poison and venom.");
                    player.skills().resetStats();
                    int increase = player.getEquipment().hpIncrease();
                    player.hp(Math.max(increase > 0 ? player.skills().level(Skills.HITPOINTS) + increase : player.skills().level(Skills.HITPOINTS), player.skills().xpLevel(Skills.HITPOINTS)), 39); //Set hitpoints to 100%
                    player.skills().replenishSkill(5, player.skills().xpLevel(5)); //Set the players prayer level to fullplayer.putAttrib(AttributeKey.RUN_ENERGY, 100.0);
                    player.setRunningEnergy(100.0, true);
                    Poison.cure(player);
                    Venom.cure(2, player);

                    if (player.tile().region() != 13386) {
                        player.message(Color.RED.tag() + "When being a member your special attack will also regenerate.");
                        if (player.getMemberRights().isRegularMemberOrGreater(player)) {
                            if (player.getTimers().has(TimerKey.RECHARGE_SPECIAL_ATTACK)) {
                                player.message("Special attack energy can be restored in " + player.getTimers().asMinutesAndSecondsLeft(TimerKey.RECHARGE_SPECIAL_ATTACK) + ".");
                            } else {
                                player.restoreSpecialAttack(100);
                                player.setSpecialActivated(false);
                                CombatSpecial.updateBar(player);
                                int time = 0;
                                if (player.getMemberRights().isRegularMemberOrGreater(player))
                                    time = 300;//3 minutes
                                if (player.getMemberRights().isSuperMemberOrGreater(player))
                                    time = 100;//1 minute
                                if (player.getMemberRights().isEliteMemberOrGreater(player))
                                    time = 0;//always
                                player.getTimers().register(TimerKey.RECHARGE_SPECIAL_ATTACK, time); //Set the value of the timer.
                                player.message("<col=" + Color.HOTPINK.getColorValue() + ">You have restored your special attack.");
                            }
                        }
                    } else {
                        player.restoreSpecialAttack(100);
                        player.setSpecialActivated(false);
                        CombatSpecial.updateBar(player);
                        player.message("<col=" + Color.HOTPINK.getColorValue() + ">You have restored your special attack.");
                    }
                    player.unlock();
                });
                return true;
            }
        }
        return false;
    }
    private enum GodswordRecolors {
        AGS(GOLDEN_ARMADYL_SPECIAL_ATTACK, ARMADYL_GODSWORD),
        BGS(GOLDEN_BANDOS_SPECIAL_ATTACK, BANDOS_GODSWORD),
        SGS(GOLDEN_SARADOMIN_SPECIAL_ATTACK, SARADOMIN_GODSWORD),
        ZGS(GOLDEN_ZAMORAK_SPECIAL_ATTACK, ZAMORAK_GODSWORD);

        private final int item1;
        private final int item2;


        GodswordRecolors(int item1, int item2) {
            this.item1 = item1;
            this.item2 = item2;
        }

    }
    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        for (GodswordRecolors combine : GodswordRecolors.values()) {
            if ((use.getId() == combine.item1) && (usedWith.getId() == combine.item2)) {
                    if (player.inventory().contains(combine.item1) && player.inventory().contains(combine.item2)) {
                        player.inventory().remove(new Item(combine.item1));
                        switch(combine){
                            case AGS:
                                player.putAttrib(AttributeKey.AGS_GFX_GOLD,true);
                                player.message("You have unlocked the gold Armadyl godsword special attack.");
                                break;
                            case BGS:
                                player.putAttrib(AttributeKey.BGS_GFX_GOLD,true);
                                player.message("You have unlocked the gold Bandos godsword special attack.");
                                break;
                            case SGS:
                                player.putAttrib(AttributeKey.SGS_GFX_GOLD,true);
                                player.message("You have unlocked the gold Saradomin godsword special attack.");
                                break;
                            case ZGS:
                                player.putAttrib(AttributeKey.ZGS_GFX_GOLD,true);
                                player.message("You have unlocked the gold godsword special attack.");
                                break;
                        }
                        return true;
                    } else {
                        player.message("You don't have the required supplies to do this.");
                    }
                }
            }

        return false;
    }
    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if(option == 1) {
            if(npc.id() == JUSTINE) {
                World.getWorld().shop(51).open(player);
                return true;
            }
            if(npc.id() == 7137) {
                TournamentManager.openTournamentWidget(player);
                return true;
            }
        }
        if(option == 2) {
            if(npc.id() == JUSTINE) {
                World.getWorld().shop(51).open(player);
                return true;
            }
            if(npc.id() == 7137) {
                TournamentManager.openTournamentWidget(player);
                return true;
            }
        }
        if(option == 3) {

        }
        if(option == 4) {

        }
        return false;
    }
}
