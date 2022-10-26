package com.ferox.game.content.areas.edgevile.dialogue;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.dialogue.Expression;
import com.ferox.game.world.position.Tile;
import com.ferox.util.chainedwork.Chain;

import java.lang.ref.WeakReference;

import static com.ferox.util.NpcIdentifiers.AUBURY;

/**
 * @author Patrick van Elderen | March, 26, 2021, 10:43
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class AuburyDialogue extends Dialogue {

    @Override
    protected void start(Object... parameters) {
       send(DialogueType.NPC_STATEMENT, AUBURY, Expression.NODDING_THREE, "Do you want to buy some runes?");
       setPhase(0);
    }

    @Override
    protected void next() {
        if(isPhase(0)) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes please!", "Oh, it's a rune shop. No thank you, then.", "Can you teleport me to the Rune Essence?");
            setPhase(1);
        } else if(isPhase(2)) {
            send(DialogueType.NPC_STATEMENT, AUBURY, Expression.NODDING_FIVE, "Well, if you find someone who does want runes, please", "send them my way.");
            setPhase(3);
        } else if(isPhase(3)) {
            stop();
        }
    }

    @Override
    protected void select(int option) {
        if(isPhase(1)) {
            if(option == 1) {
                World.getWorld().shop(23).open(player);
            }
            if(option == 2) {
                send(DialogueType.PLAYER_STATEMENT,Expression.HAPPY, "Oh, it's a rune shop. No thank you, then.");
                setPhase(2);
            }
            if(option == 3) {
                stop();
                Mob target = ((WeakReference<Mob>) player.getAttribOr(AttributeKey.TARGET, new WeakReference<>(null))).get();
                if(target != null) {
                    target.forceChat("Seventhior Distine Molenko!");
                }
                player.graphic(110, 124, 0);
                player.lockNoDamage();
                Chain.bound(player).runFn(3, () -> {
                    player.teleport(new Tile(2911, 4830, 0));
                    player.unlock();
                });
            }
        }
    }
}
