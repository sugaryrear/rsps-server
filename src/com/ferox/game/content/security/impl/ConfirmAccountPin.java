package com.ferox.game.content.security.impl;

import com.ferox.game.content.syntax.EnterSyntax;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.util.Color;

import static com.ferox.game.world.entity.AttributeKey.*;

/**
 * @author Patrick van Elderen | April, 29, 2021, 18:23
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class ConfirmAccountPin implements EnterSyntax {

    @Override
    public void handleSyntax(Player player, long input) {
        if(input == player.<Integer>getAttribOr(TEMP_ACCOUNT_PIN,0)) {
            player.putAttrib(ACCOUNT_PIN, (int)input);
            player.putAttrib(ASK_FOR_ACCOUNT_PIN,false);
            player.message(Color.PURPLE.wrap("Your account pin is now: "+input+". Keep it somewhere safe!"));
        } else {
            player.message(Color.RED.wrap("The pin you entered does not equal the previous pin."));
        }
    }
}
