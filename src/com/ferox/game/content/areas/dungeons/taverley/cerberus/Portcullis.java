package com.ferox.game.content.areas.dungeons.taverley.cerberus;

import com.ferox.game.content.areas.edgevile.IronManTutor;
import com.ferox.game.content.tradingpost.TradingPost;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.ferox.util.NpcIdentifiers.*;
import static com.ferox.util.NpcIdentifiers.RANGED_COMBAT_TUTOR;
import static com.ferox.util.ObjectIdentifiers.PORTCULLIS_21772;

public class Portcullis extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(obj.getId() == PORTCULLIS_21772) {
            Tile destination = obj.tile().equals(new Tile(1239, 1225)) ? new Tile(1291, 1253) : //West
                obj.tile().equals(new Tile(1367, 1225)) ? new Tile(1328, 1253) : //East
                    obj.tile().equals(new Tile(1303, 1289)) ? new Tile(1309, 1269) : //North
                        new Tile(0, 0);

            if (option == 1) {
                teleportPlayer(player, destination);
            }
            return true;
        }
        return false;
    }
    public static final List<Tile> RARE = Arrays.asList(
        new Tile(1240,1226),
        new Tile(1304,1291),
        new Tile(1368,1226)
    );

    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if(option == 1) {
            if(npc.id() == KEY_MASTER) {
            startCerb(player);
                return true;
            }

        }
        if(option == 2) {

        }
        if(option == 3) {

        }
        if(option == 4) {

        }
        return false;
    }
    public static void randomroomtest(Player player){
        Random rand = new Random();
        Tile randomElement = RARE.get(rand.nextInt(RARE.size()));
        player.teleport(randomElement);
    }
    public void startCerb(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                if (player.skills().xpLevel(Skills.SLAYER) < 91){
                    player.message("You need a slayer level of 91 to fight Cerberus.");
                    stop();
                    return;
            }
                send(DialogueType.OPTION, "Fight Cerberus (91 Slayer Req)", "Yes", "No");
                setPhase(0);
            }
            @Override
            public void select(int option) {
                if (getPhase() == 0) {
                    if(option == 1) {
                        Random rand = new Random();
                        Tile randomElement = RARE.get(rand.nextInt(RARE.size()));
                        player.teleport(randomElement);
                    }
                    stop();
                }
            }
        });
    }
    private void teleportPlayer(Player player, Tile tile) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Do you wish to leave?", "Yes, I'm scared.", "Nah, I'll stay.");
                setPhase(0);
            }
            @Override
            public void select(int option) {
                if (getPhase() == 0) {
                    if(option == 1) {
                        player.teleport(tile);
                    }
                    stop();
                }
            }
        });
    }
}
