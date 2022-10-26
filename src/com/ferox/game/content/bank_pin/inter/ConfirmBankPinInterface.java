package com.ferox.game.content.bank_pin.inter;


import com.ferox.GameServer;
import com.ferox.game.GameConstants;
import com.ferox.game.GameEngine;
import com.ferox.game.content.bank_pin.BankPinInterface;
import com.ferox.game.content.bank_pin.dialogue.IncorrectBankPinDialogue;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.dialogue.Expression;
import com.ferox.game.world.entity.mob.player.Player;
import org.mindrot.BCrypt;

import java.util.List;

import static com.ferox.util.NpcIdentifiers.DRUNKEN_DWARF_2408;

/**
 * @author lare96 <http://github.com/lare96>
 */
public final class ConfirmBankPinInterface extends BankPinInterface {

    private final int pinLength;
    private final List<Integer> previousDigits;

    protected ConfirmBankPinInterface(Player player, int pinLength, List<Integer> previousDigits) {
        super(player);
        this.pinLength = pinLength;
        this.previousDigits = previousDigits;
    }

    @Override
    public void onOpen() {
        player.getPacketSender().sendString(14920, "Now, please confirm your desired PIN.");
        player.getPacketSender().sendString(14923, "Bank of "+ GameConstants.SERVER_NAME+"");
        player.getPacketSender().sendString(14914, "");
        player.getPacketSender().sendString(14915, "");
        player.getPacketSender().sendString(14916, "");
        player.getPacketSender().sendString(15313, "Click the " + PIN_LENGTH.get(0) + " digit.");
        player.getPacketSender().sendString(14913, pinLength + " digits left");
    }

    @Override
    public void displayText() {
        int digitsEntered = entered.size();
        player.getPacketSender().sendString(15313, "Click the " + PIN_LENGTH.get(digitsEntered) + " digit.");
        player.getPacketSender().sendString(14913, (pinLength - digitsEntered) + " digits left");
    }

    @Override
    public boolean onDigitEntered(int digit, List<Integer> allDigits) {
        if (allDigits.size() == previousDigits.size()) {
            for (int index = 0; index < allDigits.size(); index++) {
                int prevDigit = previousDigits.get(index);
                int nextDigit = allDigits.get(index);
                if (prevDigit != nextDigit) {
                    player.getDialogueManager().start(new IncorrectBankPinDialogue(bankPin, () ->
                        player.getBankPinSettings().createPin(pinLength)));
                    return false;
                }
            }
            String pinToHash = bankPin.pinToString(allDigits);
            GameEngine.getInstance().submitLowPriority(() -> {
                String hashedPin = BCrypt.hashpw(pinToHash, BCrypt.gensalt());
                GameEngine.getInstance().addSyncTask(() -> {
                    if (!player.getBankPin().hasPin()) {
                        bankPin.setPinEntered();
                        bankPin.setPinLength(pinLength);
                        bankPin.setHashedPin(hashedPin);
                        bankPin.setRecoveryDays(GameServer.properties().defaultBankPinRecoveryDays);
                        var hassetuppinbefore = player.<Boolean>getAttribOr(AttributeKey.FIRST_TIME_PIN,false);
                        if (!hassetuppinbefore) {
                            player.putAttrib(AttributeKey.FIRST_TIME_PIN, true);
                            player.inventory().add(4447, 1);
                            player.message("You receive a double drops lamp for setting up a bank PIN!");

                        }
                        player.message("Your " + pinLength + "-digit PIN has successfully been created.");
                    } else {
                        player.getBankPin().changePin(hashedPin, pinLength);
                    }
                    player.getInterfaceManager().close();
                    World.getWorld().ls.savePlayerAsync(player);
                });
            });
            return false;
        }
        return true;
    }
}
