package com.ferox.game.content.raids;

import com.ferox.game.content.raids.party.Party;
import com.ferox.game.content.raids.party.dialogue.PartyDialogue;
import com.ferox.game.content.syntax.EnterSyntax;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueManager;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import com.ferox.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.ferox.game.content.raids.party.Party.*;
import static com.ferox.util.ObjectIdentifiers.RECRUITING_BOARD;

/**
 * @author Patrick van Elderen | April, 26, 2021, 17:25
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class RaidPartyActions extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if (option == 1) {
            if (object.getId() == RECRUITING_BOARD || object.getId() == 29777) {
                player.faceObj(object);
                player.getDialogueManager().start(new PartyDialogue());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handleButtonInteraction(Player player, int button) {
        Party party = player.raidsParty;
        if (party == null) {
            return false;
        }

        if(button == 12115) {
            if (party.getLeader() != player) {
                player.message("Only the leader of this party can select a raids type.");
                return true;
            }

            player.getPacketSender().sendConfig(COS_CONFIG_ID, 1);
            player.getPacketSender().sendConfig(TOB_CONFIG_ID, 0);
            player.getPacketSender().sendItemOnInterface(REWARDS_CONTAINER_ID, COX_REWARDS);
            party.setRaidsSelected(0);
            refreshInterface(party.getLeader(), party);
            return true;
        }

        if(button == 12116) {
            if (party.getLeader() != player) {
                player.message("Only the leader of this party can select a raids type.");
                return true;
            }

            player.getPacketSender().sendConfig(COS_CONFIG_ID, 0);
            player.getPacketSender().sendConfig(TOB_CONFIG_ID, 1);
            player.getPacketSender().sendItemOnInterface(REWARDS_CONTAINER_ID, TOB_REWARDS);
            party.setRaidsSelected(1);
            refreshInterface(party.getLeader(), party);
            return true;
        }

        if(button == 12139) {
            if (party.getLeader() != player) {
                player.message("Only the leader of this party can select a raids type.");
                return true;
            }

            player.getPacketSender().sendConfig(COS_CONFIG_ID, 0);
            player.getPacketSender().sendConfig(TOB_CONFIG_ID, 0);
            player.getPacketSender().sendConfig(HP_CONFIG_ID, 1);
            player.getPacketSender().sendItemOnInterface(REWARDS_CONTAINER_ID, HP_REWARDS);
            party.setRaidsSelected(2);
            refreshInterface(party.getLeader(), party);
            return true;
        }

        if (button == 12140) {
            if (player.tile().region() != 4919) {
                player.message("You can't invite players from here.");
                return true;
            }
            player.setEnterSyntax(new EnterSyntax() {
                @Override
                public void handleSyntax(Player player, @NotNull String input) {
                    Optional<Player> target = World.getWorld().getPlayerByName(input);

                    if (target.isPresent()) {
                        if (target.get().tile().region() != 4919) {
                            player.message(Utils.formatName(input) + " is nowhere near the raids area.");
                            return;
                        }
                        invite(player, target.get());
                    } else {
                        player.message(Utils.formatName(input) + " is not online and cannot join your party.");
                        player.getInterfaceManager().closeDialogue();
                    }
                }
            });
            player.getPacketSender().sendEnterInputPrompt("Who would you like to invite?");
            return true;
        }
        if (button == 12143) {
            if(player.raidsParty.getLeader() != player) {
                player.message(Color.RED.wrap("Only the leader can start this raiding party."));
                return true;
            }
            Party.startRaid(player);//Start raid
            return true;
        }
        if (button == 12146) {
            Party.leaveParty(player,true);
            return true;
        }
        if (button == 42125) {
            Party.kick(player, 1);
            return true;
        }
        if (button == 42128) {
            Party.kick(player, 2);
            return true;
        }
        if (button == 22131) {
            Party.kick(player, 3);
            return true;
        }
        if (button == 22134) {
            Party.kick(player, 4);
            return true;
        }
        return false;
    }

    public static void invite(Player p, Player target) {
        if (p.raidsParty == null) {
            p.message("You need to create a party before requesting " + target.getUsername() + " to join it.");
            return;
        }

        if (target.raidsParty != null) {
            p.message(target.getUsername() + " is already in a party.");
            return;
        }

        if (p.raidsParty.getLeader() != p) {
            p.message("You need to be the leader of your party in order to invite a player to join it.");
            return;
        }

        DialogueManager.sendStatement(p, "Requesting..");
        target.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, p.getUsername() + " has invited you to join their party.", "Accept", "Decline");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if (isPhase(0)) {
                    if (option == 1) {
                        if (target.raidsParty != null) {
                            DialogueManager.sendStatement(p, target.getUsername() + " is already in a party.");
                            return;
                        } else {
                            Party party = p.raidsParty;
                            if(party != null) {
                                if (party.getRaidStage() >= 1) {
                                    target.getInterfaceManager().close();
                                    target.message(Color.RED.wrap("Could not join party, the party had already begun."));
                                    return;
                                }
                                party.addMember(target);
                                target.raidsParty = party;
                                target.message("You've joined " + p.getUsername() + "'s raid party.");
                                DialogueManager.sendStatement(p, target.getUsername() + " has joined your raid party.");
                                Party.openPartyInterface(p, true);
                                Party.openPartyInterface(target, true);
                            }
                            stop();
                        }
                    }
                    if (option == 2) {
                        DialogueManager.sendStatement(p, target.getUsername() + " has declined your request to join your raid party.");
                        target.message("You decline " + p.getUsername() + "'s request to join their party.");
                        stop();
                    }
                }
            }
        });
    }
}
