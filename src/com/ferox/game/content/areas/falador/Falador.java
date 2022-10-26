package com.ferox.game.content.areas.falador;

import com.ferox.game.content.syntax.impl.SearchByItem;
import com.ferox.game.content.syntax.impl.SearchByNpc;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.NumberUtils;
import com.ferox.util.Utils;

import static com.ferox.util.ItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.ZAMORAKIAN_HASTA;
import static com.ferox.util.NpcIdentifiers.HORVIK;

public class Falador extends PacketInteraction {
    @Override
    public boolean handleItemOnNpc(Player player, Item item, Npc npc) {

        if (npc.id() == 10371) {
            gambleitem(player, item, npc);
            return true;
        }
        return false;
    }
    private void gambleitem(Player player, Item item, Npc npc) {
player.animate(2764);
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.ITEM_STATEMENT, item, "", "Gamble " + item.name() + " for a chance to double it?");
                setPhase(0);
            }

            @Override
            protected void next() {
                if(isPhase(0)) {
                    send(DialogueType.OPTION, "Are you sure you want to do this?", "Yes", "No");
                    setPhase(1);
                } else if(isPhase(2)) {
                    stop();
                }
            }

            @Override
            protected void select(int option) {
                if (isPhase(1)) {
                    if (option == 1) {
                        if (!player.inventory().contains(item)) {
                            stop();
                            return;
                        }
                       int npcroll =  Utils.random(100);

                        if(npcroll > 55){ //player wins
                            npc.animate(3067);
                            player.message("The wizard rolled "+npcroll+", you win!");
                            player.inventory().addOrBank(new Item(item.getId(),item.getAmount()));
                        } else {
                            npc.animate(3068);

                            player.message("The wizard rolled "+npcroll+", you lose!");
                            player.inventory().remove(item);
                        }

                        stop();
                    }
                    if (option == 2) {
                        stop();
                    }
                }
            }
        });
    }

    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if (option == 1) {
            if (npc.id() == 2679) {
                World.getWorld().shop(59).open(player);
                return true;
            }

            if(npc.id() == 5422) {
                player.getDialogueManager().start(new Dialogue() {
                @Override
                protected void start(Object... parameters) {
                    send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Convert all mole skins and claws into BM (500 per)", "Cancel");
                    setPhase(0);
                }

                @Override
                protected void select(int option) {
                    if (isPhase(0)) {
                        if (option == 1) {
                            stop();
                            int mole_skins = player.inventory().count(MOLE_SKIN);
                            int mole_claws =  player.inventory().count(MOLE_CLAW);

                            if(mole_skins < 1 || mole_claws < 1 ){
                                player.message("You need at least 1 mole skin or claw to convert into blood money.");
                                return;
                            }

                         player.inventory().remove(new Item(MOLE_SKIN, mole_skins), true);
                                player.inventory().remove(new Item(MOLE_CLAW, mole_claws), true);
                            player.inventory().add(new Item(13307, (mole_claws+mole_skins)*500), true);


                        } else if (option == 2) {
                            stop();
                        } else if (option == 3) {
                            stop();
                        }
                    }
                }
            });
                return true;
            }
        }
        if (option == 2) {
            if (npc.id() == 2679) {
                World.getWorld().shop(59).open(player);
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            if (obj.getId() == 12230) {//rope from mole lair
                player.teleport(new Tile(2986,3318));

                return true;
            }
        }
        return false;
    }
}
