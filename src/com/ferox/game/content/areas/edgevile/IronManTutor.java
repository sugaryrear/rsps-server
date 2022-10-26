package com.ferox.game.content.areas.edgevile;

import com.ferox.GameServer;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueManager;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.dialogue.Expression;
import com.ferox.game.world.entity.mob.player.*;
import com.ferox.game.world.entity.mob.player.rights.PlayerRights;
import com.ferox.game.world.items.Item;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.NpcIdentifiers;
import com.ferox.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.ferox.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | January, 19, 2021, 13:41
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class IronManTutor extends Dialogue {

    private static final Logger logger = LogManager.getLogger(IronManTutor.class);

    public static void armour(Player player) {
        switch (player.ironMode()) {
            case REGULAR -> {
                if (player.ownsAny(IRONMAN_HELM, IRONMAN_PLATEBODY, IRONMAN_PLATELEGS)) {
                    player.message("Come back when you've lost your ironman armour.");
                    return;
                }
                player.inventory().add(new Item(IRONMAN_HELM, 1), true);
                player.inventory().add(new Item(IRONMAN_PLATEBODY, 1), true);
                player.inventory().add(new Item(IRONMAN_PLATELEGS, 1), true);
            }
            case HARDCORE -> {
                if (player.ownsAny(HARDCORE_IRONMAN_HELM, HARDCORE_IRONMAN_PLATEBODY, HARDCORE_IRONMAN_PLATELEGS)) {
                    player.message("Come back when you've lost your ironman armour.");
                    return;
                }
                player.inventory().add(new Item(HARDCORE_IRONMAN_HELM, 1), true);
                player.inventory().add(new Item(HARDCORE_IRONMAN_PLATEBODY, 1), true);
                player.inventory().add(new Item(HARDCORE_IRONMAN_PLATELEGS, 1), true);
            }
            case ULTIMATE -> {
                if (player.ownsAny(ULTIMATE_IRONMAN_HELM, ULTIMATE_IRONMAN_PLATEBODY, ULTIMATE_IRONMAN_PLATELEGS)) {
                    player.message("Come back when you've lost your ironman armour.");
                    return;
                }
                player.inventory().add(new Item(ULTIMATE_IRONMAN_HELM, 1), true);
                player.inventory().add(new Item(ULTIMATE_IRONMAN_PLATEBODY, 1), true);
                player.inventory().add(new Item(ULTIMATE_IRONMAN_PLATELEGS, 1), true);
            }
            default -> player.message("Only ironman players can claim their armour.");
        }

        if (player.ironMode() != IronMode.NONE) {
            DialogueManager.npcChat(player, Expression.HAPPY, NpcIdentifiers.IRON_MAN_TUTOR, "There you go. Wear it with pride.");
        }
    }

    @Override
    protected void start(Object... parameters) {
        if (!GameServer.properties().enableChangeAccountType) {
            player.message("You are currently unable to change your account type. Please try again later.");
            return;
        }

        setPhase(0);
        send(DialogueType.NPC_STATEMENT, player.getInteractingNpcId(), Expression.DEFAULT, "Would you like to change your account type to", (player.mode() == GameMode.INSTANT_PKER ? GameMode.TRAINED_ACCOUNT.toName() : GameMode.INSTANT_PKER.toName()) + "?");
    }

    @Override
    public void next() {
        if (isPhase(0)) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes.", "No thanks.");
            setPhase(1);
        }
        if (isPhase(3)) {
            if (player.mode() == GameMode.INSTANT_PKER) {
                send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes.", "No, I don't want to lose my levels!");
            } else {
                send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes.", "No thanks.");
            }
            setPhase(4);
        }
    }

    @Override
    public void select(int option) {
        if (isPhase(1)) {
            setPhase(2);
            if (option == 1) {
                if (player.mode() == GameMode.TRAINED_ACCOUNT) {
                    send(DialogueType.NPC_STATEMENT, player.getInteractingNpcId(), Expression.DEFAULT, "Are you sure?", "All of your levels you have will be reset.");
                } else {
                    send(DialogueType.NPC_STATEMENT, player.getInteractingNpcId(), Expression.DEFAULT, "Are you sure?");
                }
                setPhase(3);
            } else {
                stop();
            }
        }
        if (isPhase(4)) {
            if (option == 1) {
                player.resetSkills();
                GameMode gameMode = (player.mode() == GameMode.INSTANT_PKER ? GameMode.TRAINED_ACCOUNT : GameMode.INSTANT_PKER);
                player.mode(gameMode);
                player.getPacketSender().sendString(QuestTab.InfoTab.GAME_MODE.childId, QuestTab.InfoTab.INFO_TAB.get(QuestTab.InfoTab.GAME_MODE.childId).fetchLineData(player));
                //logger.info(player.toString() + " has changed their account type to " + gameMode.toName());
                Utils.sendDiscordInfoLog(player.toString() + " has changed their account type to " + gameMode.toName());
                if (gameMode == GameMode.INSTANT_PKER) {
                    player.ironMode(IronMode.NONE);
                    if(!player.getPlayerRights().isStaffMember(player)) {
                        player.setPlayerRights(PlayerRights.PLAYER);
                        player.getPacketSender().sendRights();
                    }
                    max(player);
                    player.getPresetManager().open();
                } else {
                    starter(player);
                }
            }
            stop();
        }
    }

    public static void max(Player player) {
        for (int skill = 0; skill < 7; skill++) {
            player.skills().setXp(skill, Skills.levelToXp(99));
            player.skills().update();
            player.skills().recalculateCombat();
        }
    }

    public static void starter(Player player) {
        Item[] training_equipment = {
            new Item(ItemIdentifiers.BRONZE_ARROW, 10_000),
            new Item(ItemIdentifiers.IRON_KNIFE, 10_000),
            new Item(ItemIdentifiers.AIR_RUNE, 10_000),
            new Item(ItemIdentifiers.MIND_RUNE, 10_000),
            new Item(ItemIdentifiers.CHAOS_RUNE, 10_000),
            new Item(ItemIdentifiers.WATER_RUNE, 10_000),
            new Item(ItemIdentifiers.EARTH_RUNE, 10_000),
            new Item(ItemIdentifiers.FIRE_RUNE, 10_000),
            new Item(ItemIdentifiers.STAFF_OF_AIR, 1),
            new Item(ItemIdentifiers.SHORTBOW, 1),
            new Item(ItemIdentifiers.IRON_SCIMITAR, 1),
            new Item(ItemIdentifiers.IRON_FULL_HELM, 1),
            new Item(ItemIdentifiers.IRON_PLATEBODY, 1),
            new Item(ItemIdentifiers.IRON_PLATELEGS, 1),
            new Item(ItemIdentifiers.CLIMBING_BOOTS, 1),
            new Item(ItemIdentifiers.BLUE_WIZARD_HAT, 1),
            new Item(ItemIdentifiers.BLUE_WIZARD_ROBE, 1),
            new Item(ItemIdentifiers.BLUE_SKIRT, 1),
            new Item(ItemIdentifiers.LEATHER_BODY, 1),
            new Item(ItemIdentifiers.LEATHER_CHAPS, 1),
            new Item(ItemIdentifiers.TINDERBOX, 1),
            new Item(SMALL_FISHING_NET, 1),
            new Item(BRONZE_PICKAXE, 1),
            new Item(BRONZE_AXE, 1),
        };
        player.inventory().addAll(training_equipment);
        player.message("You have been given some training equipment.");
    }
}
