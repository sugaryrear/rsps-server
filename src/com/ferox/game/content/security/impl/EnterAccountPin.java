package com.ferox.game.content.security.impl;

import com.ferox.game.content.syntax.EnterSyntax;
import com.ferox.game.task.TaskManager;
import com.ferox.game.task.impl.AccountPinFrozenTask;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.util.Color;

import static com.ferox.game.world.entity.AttributeKey.*;

/**
 * @author Patrick van Elderen | May, 01, 2021, 15:24
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class EnterAccountPin implements EnterSyntax {

    @Override
    public void handleSyntax(Player player, long input) {
        String pinToString = Integer.toString((int) input);

        if(pinToString.equalsIgnoreCase(Integer.toString(player.<Integer>getAttribOr(ACCOUNT_PIN,0)))) {
            player.message(Color.GREEN.wrap("You have entered the correct account pin."));
            player.putAttrib(ASK_FOR_ACCOUNT_PIN,false);
            player.putAttrib(ACCOUNT_PIN_ATTEMPTS_LEFT,5);
        } else {
            //Invalid pin, ask again
            player.putAttrib(ASK_FOR_ACCOUNT_PIN,true);
            int attemptsLeft = player.<Integer>getAttribOr(ACCOUNT_PIN_ATTEMPTS_LEFT,5) - 1;
            player.putAttrib(ACCOUNT_PIN_ATTEMPTS_LEFT, attemptsLeft);

            if(attemptsLeft == 0) {
                //set waiting task
                player.putAttrib(ACCOUNT_PIN_FREEZE_TICKS,600);
                TaskManager.submit(new AccountPinFrozenTask(player));
                return;
            }

            player.setEnterSyntax(new EnterAccountPin());
            player.getPacketSender().sendEnterAmountPrompt("Enter your account pin, attempts left: "+attemptsLeft+"/5");
        }
    }
}
