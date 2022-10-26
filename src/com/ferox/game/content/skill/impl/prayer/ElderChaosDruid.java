package com.ferox.game.content.skill.impl.prayer;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueManager;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.dialogue.Expression;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.NpcIdentifiers.ELDER_CHAOS_DRUID_7995;

public class ElderChaosDruid extends PacketInteraction {

    private void swap(Player player, int original, int result) {
        var count = player.inventory().count(original);
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Un-Note 1", "Un-Note 5", "Un-Note 10", "Un-Note All");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if (isPhase(0)) {
                    if (option == 1) {
                        if (count <= 0) {
                            stop();
                            return;
                        }
                        unnote(player, 1, original, result);
                    }
                    if (option == 2) {
                        if (count <= 0) {
                            stop();
                            return;
                        }
                        unnote(player, 5, original, result);
                    }
                    if (option == 3) {
                        if (count <= 0) {
                            stop();
                            return;
                        }
                        unnote(player, 10, original, result);
                    }
                    if (option == 4) {
                        if (count <= 0) {
                            stop();
                            return;
                        }
                        unnoteall(player, count, original, result);
                    }
                }
            }
        });
    }

    private void unnoteall(Player player, int amount, int original, int result) {
        player.getInterfaceManager().close();
        int amtofbloodmoney = player.inventory().count(13307);
        int howmanybones = player.inventory().count(original);
        int howmuchcanweconvert = amtofbloodmoney / 50; //max amt we can convert
//
//        player.message(howmuchcanweconvert+"");

//
//        int toRemove = amount;
//
//        if (player.inventory().count(original) < toRemove) {
//            toRemove = player.inventory().count(original);
//        }
//
//        if (toRemove == 0) {
//            player.message("You do not have any bones.");
//            return;
//        }
        if(howmuchcanweconvert > howmanybones){
            howmuchcanweconvert = howmanybones;
        }
        if (player.inventory().getFreeSlots() < howmanybones) {
            player.message("You have no room in in your inventory to do this.");
            return;
        }

//        if (player.inventory().getFreeSlots() < toRemove) {
//            toRemove = player.inventory().getFreeSlots();
//        }
        if(!player.inventory().contains(13307,50)){
            player.message("You need at least 50 blood money to convert 1 noted bone.");
            return;
        }
        if(!player.inventory().contains(original,howmuchcanweconvert)){
            return;
        }
        var currency = 13307;
        var name = "blood money";

        if (!player.inventory().contains(new Item(currency, howmuchcanweconvert * 50))) {
            DialogueManager.npcChat(player, Expression.CALM_TALK, ELDER_CHAOS_DRUID_7995, "Unfortunately, you don't have enough " + name + " "," right now to do that.");
        } else {
            player.inventory().remove(new Item(currency, howmuchcanweconvert * 50));
            player.inventory().remove(new Item(original, howmuchcanweconvert));
            player.inventory().add(new Item(result, howmuchcanweconvert));
            player.itemBox("The Druid converts "+howmuchcanweconvert+" noted bones.", original);
        }
    }
    private void unnote(Player player, int amount, int original, int result) {
        player.getInterfaceManager().close();
        var toRemove = amount;

        if (player.inventory().count(original) < toRemove) {
            toRemove = player.inventory().count(original);
        }

        if (toRemove == 0) {
            player.message("You do not have any bones.");
            return;
        }
        if (player.inventory().getFreeSlots() == 0) {
            player.message("You have no room in in your inventory to do this.");
            return;
        }
        if (player.inventory().getFreeSlots() < toRemove) {
            toRemove = player.inventory().getFreeSlots();
        }
        var currency = 13307;
        var name = "blood money";

        if (!player.inventory().contains(new Item(currency, toRemove * 50))) {
            DialogueManager.npcChat(player, Expression.CALM_TALK, ELDER_CHAOS_DRUID_7995, "Unfortunately, you don't have enough " + name + " "," right now to do that.");
        } else {
            player.inventory().remove(new Item(currency, toRemove * 50));
            player.inventory().remove(new Item(original, toRemove));
            player.inventory().add(new Item(result, toRemove));
            player.itemBox("The Druid converts your banknotes to items.", original);
        }
    }

    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if (npc.id() == ELDER_CHAOS_DRUID_7995) {
            var name = "blood money";
            player.getDialogueManager().start(new Dialogue() {
                @Override
                protected void start(Object... parameters) {
                    send(DialogueType.NPC_STATEMENT, ELDER_CHAOS_DRUID_7995, Expression.CALM_TALK, "Hello. I can convert items to banknotes, for 50 " + name + "", "per item. Just hand me the items you'd like me to", "convert.");
                    setPhase(0);
                }

                @Override
                protected void next() {
                    if (isPhase(0)) {
                        send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Who are you?", "Thanks");
                        setPhase(1);
                    } else if (isPhase(2)) {
                        send(DialogueType.NPC_STATEMENT, ELDER_CHAOS_DRUID_7995, Expression.CALM_TALK, "I'm just a druid, My real name is Yaz. I'm lazy but", "I make money converting your noted bones", "to unnoted bones.");
                        setPhase(3);
                    } else if (isPhase(3)) {
                        send(DialogueType.PLAYER_STATEMENT, Expression.NODDING_ONE, "Thanks");
                        setPhase(4);
                    } else if (isPhase(4)) {
                        stop();
                    }
                }

                @Override
                protected void select(int option) {
                    if (isPhase(1)) {
                        if (option == 1) {
                            send(DialogueType.PLAYER_STATEMENT, Expression.NODDING_ONE, "Who are you?");
                            setPhase(2);
                        }
                        if (option == 2) {
                            send(DialogueType.PLAYER_STATEMENT, Expression.NODDING_ONE, "Thanks");
                            setPhase(4);
                        }
                    }
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public boolean handleItemOnNpc(Player player, Item item, Npc npc) {
        if (npc.id() == ELDER_CHAOS_DRUID_7995) {
            if (new Item(item).noted() && new Item(item).unnote().definition(World.getWorld()).name.toLowerCase().endsWith("bones")) {
                swap(player, item.getId(), new Item(item).unnote().getId());
            } else {
                DialogueManager.npcChat(player, Expression.VERY_SAD, ELDER_CHAOS_DRUID_7995, "Sorry, I wasn't expecting anyone to want to convert", "that sort of item, so I haven't any banknotes for it.");
            }
            return true;
        }
        return false;
    }
}
