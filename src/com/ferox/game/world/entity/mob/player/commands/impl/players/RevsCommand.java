package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.content.skill.impl.slayer.SlayerConstants;
import com.ferox.game.content.teleport.TeleportType;
import com.ferox.game.content.teleport.Teleports;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.position.Tile;

/**
 * @author Patrick van Elderen | January, 11, 2021, 18:08
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class RevsCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Level 17 entrance.", "Level 39 entrance.");
                setPhase(1);
            }

            @Override
            protected void select(int option) {
                if(isPhase(1)) {
                    if (option == 1) {
                        Tile tile = new Tile(3075, 3651, 0);
                        if (!Teleports.canTeleport(player, true, TeleportType.GENERIC) || !Teleports.pkTeleportOk(player, tile)) {
                            stop();
                            return;
                        }
                        Teleports.basicTeleport(player,tile);
                        player.message("You have been teleported to the revenants cave.");
                        stop();
                    } else if (option == 2) {
                        Tile tile = player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.REVENANT_TELEPORT) ? new Tile(3244, 10145, 0) : new Tile(3130, 3828);;
                        if (!Teleports.canTeleport(player, true, TeleportType.GENERIC) || !Teleports.pkTeleportOk(player, tile)) {
                            stop();
                            return;
                        }
                        Teleports.basicTeleport(player, tile);
                        player.message("You have been teleported to the revenants cave.");
                        stop();
                    }
                }
            }
        });
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
