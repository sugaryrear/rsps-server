package com.ferox.game.content.areas.dungeons.godwars;

import com.ferox.game.content.skill.impl.woodcutting.BirdNest;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.dialogue.Expression;
import com.ferox.game.world.entity.dialogue.ItemActionDialogue;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.npc.pets.Pet;
import com.ferox.game.world.entity.mob.npc.pets.insurance.PetInsurance;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.Utils;

import static com.ferox.game.world.entity.AttributeKey.RC_DIALOGUE;
import static com.ferox.game.world.entity.AttributeKey.TORVA_CREATING;
import static com.ferox.util.ItemIdentifiers.*;
import static com.ferox.util.NpcIdentifiers.PROBITA;
import static com.ferox.util.ObjectIdentifiers.*;

public class AncientForge extends PacketInteraction {
    public enum AncientForgeSmelt {

        BANDOS_CHESTPLAT(BANDOS_CHESTPLATE, 3),
        BANDOS_TASSET(BANDOS_TASSETS, 2);

        public final int bandositem,components;

        AncientForgeSmelt(int bandositem, int components) {
            this.bandositem = bandositem;
            this.components = components;
        }

    }
    public enum AncientForgeTorva {

        TORVAFULLHELM(TORVA_FULL_HELM_DAMAGED, TORVA_FULL_HELM, 1),
        TORVAPLATEBODY(TORVA_PLATEBODY_DAMAGED, TORVA_PLATEBODY, 2),
        TORVAPLATELEGS(TORVA_PLATELEGS_DAMAGED, TORVA_PLATELEGS, 2);


        public final int brokenitem,repaireditem,bandosiancomponentsreq;

        AncientForgeTorva(int brokenitem, int repaireditem, int bandosiancomponentsreq) {
            this.brokenitem = brokenitem;
            this.repaireditem = repaireditem;
            this.bandosiancomponentsreq = bandosiancomponentsreq;
        }

    }
    public static void handleCreatingTorva(Player player, Item item){
        player.getInterfaceManager().close();
        for (AncientForgeTorva piecetocreate : AncientForgeTorva.values()) {
            if (item.getId() == piecetocreate.repaireditem) {
               // Item item = Item.of(piecetocreate.repaireditem);

                if (player.inventory().contains(piecetocreate.brokenitem) && player.inventory().contains(BANDOSIAN_COMPONENTS, piecetocreate.bandosiancomponentsreq)) {
                    player.inventory().remove(piecetocreate.brokenitem, 1);
                    player.inventory().remove(BANDOSIAN_COMPONENTS, piecetocreate.bandosiancomponentsreq);
                    player.skills().addXp(Skills.SMITHING, 2250, true);

                    player.inventory().add(piecetocreate.repaireditem, 1);
                    player.getDialogueManager().createItemMessage(player, piecetocreate.repaireditem, "You repair the broken "+item.unnote().name(), "");
                } else {

                    player.message("You need a "+Item.of(piecetocreate.brokenitem).unnote().name()+" and "+piecetocreate.bandosiancomponentsreq+" Bandosian component.");
                }
            }
        }
    }

    @Override
    public boolean handleItemOnObject(Player player, Item item, GameObject object) {
        if (object.getId() == ANCIENT_FORGE) {
            for (AncientForgeSmelt bandospiece : AncientForgeSmelt.values()) {
                if (item.getId() == bandospiece.bandositem) {
                    player.getDialogueManager().start(new Dialogue() {
                        @Override
                        protected void start(Object... parameters) {
                            send(DialogueType.OPTION,"Smelt down the "+item.unnote().name()+" and recieve "+bandospiece.components+" bandosian components?", "Yes", "No");
                            setPhase(0);
                        }

                        @Override
                        protected void select(int option) {
                            if(option == 1) {
                        if(player.inventory().contains(bandospiece.bandositem)){
                            player.inventory().remove(bandospiece.bandositem,1);
                            player.inventory().add(26394,bandospiece.components);
                            player.message("You smelt down the bandos piece into "+bandospiece.components+" bandosian components.");
                                } else {
                            player.message("You don't have any of that item.");
                                }
                                stop();
                            } else if(option == 2) {
                                stop();
                            }
                        }
                    });
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            if(obj.getId() == ANCIENT_FORGE) {

                player.getDialogueManager().start(new Dialogue() {
                    @Override
                    protected void start(Object... parameters) {
                        send(DialogueType.OPTION,"@blu@        Ancient Forge", "Smelt down Bandos pieces", "Repair broken Torva (90 Smithing)");
                        setPhase(0);
                    }
                    @Override
                    protected void select(int option) {
                        if (getPhase() == 0) {
                            if (option == 1) {
                                send(DialogueType.OPTION,"", "Smelt down Bandos Chestplate", "Smelt down Bandos tassets");
                                setPhase(1);
                            }
                            if (option == 2) {
                                if (player.skills().level(Skills.SMITHING) < 90) {
                                    player.message("You need a smithing level of 90 to repair broken Torva.");
                                    stop();
                                    return;
                                }
                                    stop();
                                player.putAttrib(TORVA_CREATING, true);
                                ItemActionDialogue.sendInterface(player, ItemIdentifiers.TORVA_FULL_HELM,ItemIdentifiers.TORVA_PLATEBODY,ItemIdentifiers.TORVA_PLATELEGS);
                                //setPhase(1);
                            }
                        }
                            if (getPhase() == 1) {

                            if (option == 1) {
                                if (player.inventory().contains(11832)) {
                                    player.inventory().remove(11832, 1);
                                    player.inventory().add(26394, 3);
                                    player.message("You smelt down the bandos piece into 3 bandosian components.");
                                    stop();
                                } else {
                                    player.message("You don't have any of that item.");
                                }

                            } else if (option == 2) {
                                if (player.inventory().contains(11834)) {
                                    player.inventory().remove(11834, 1);
                                    player.inventory().add(26394, 2);
                                    player.message("You smelt down the bandos piece into 2 bandosian components.");
                                    stop();
                                } else {
                                    player.message("You don't have any of that item.");
                                }
                            }
                        }
                    }
                });

                return true;
            }

        }
        return false;
    }
}

