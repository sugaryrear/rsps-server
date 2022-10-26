package com.ferox.game.content.security.impl;

import com.ferox.game.content.syntax.EnterSyntax;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.util.Color;
import com.ferox.util.chainedwork.Chain;

import static com.ferox.game.world.entity.AttributeKey.TEMP_ACCOUNT_PIN;

/**
 * @author Patrick van Elderen | April, 29, 2021, 18:20
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class SetupAccountPin implements EnterSyntax {

    private static final int INVALID_PIN = 12345;

    @Override
    public void handleSyntax(Player player, long input) {
        String pinToString = Long.toString(input);
        if(pinToString.length() != 5) {
            player.message(Color.RED.wrap("The pin has to be exactly 5 digits."));
            return;
        }
        if(pinToString.equalsIgnoreCase(String.valueOf(INVALID_PIN))) {
            player.message(Color.RED.wrap("The pin wasn't strong enough."));
            return;
        }
        player.putAttrib(TEMP_ACCOUNT_PIN, (int) input);
        Chain.bound(player).runFn(2, () -> {
            player.setEnterSyntax(new ConfirmAccountPin());
            player.getPacketSender().sendEnterAmountPrompt("Confirm your pin");
        });
    }
}
