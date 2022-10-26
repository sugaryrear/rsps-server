package com.ferox.game.world.entity.mob.player.commands.impl.super_member;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

public class YellColourCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if(!player.getMemberRights().isSuperMemberOrGreater(player) && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            player.message("<col=ca0d0d>Only Super Members may use this feature.");
            return;
        }
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... options) {
                send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "<col=255>Blue", "<col=ca0d0d>Red", "<col=ffffff>White", "<col=006601>Green");
                setPhase(0);
            }

            @Override
            public void select(int option) {
                if (isPhase(0)) {
                    if (option == 1) {
                        player.putAttrib(AttributeKey.YELL_COLOUR, "255"); //blue
                        stop();
                    } else if (option == 2) {
                        player.putAttrib(AttributeKey.YELL_COLOUR, "CA0D0D"); //red
                        stop();
                    } else if (option == 3) {
                        player.putAttrib(AttributeKey.YELL_COLOUR, "ffffff"); //white
                        stop();
                    } else if (option == 4) {
                        player.putAttrib(AttributeKey.YELL_COLOUR, "006601"); //green
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
