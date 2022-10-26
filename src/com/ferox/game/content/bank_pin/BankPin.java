package com.ferox.game.content.bank_pin;

import com.ferox.GameServer;
import com.ferox.game.content.bank_pin.inter.EnterBankPinInterface;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.Player;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lare96 <http://github.com/lare96>
 */
public final class BankPin {

    private final Player player;

    private BankPinInterface pinInterface;

    private boolean enteredPin;

    private String hashedPin = "No bank pin.";

    private int pinLength;

    private int recoveryDays = GameServer.properties().defaultBankPinRecoveryDays;

    private BankPinModification pendingMod;


    public BankPin(Player player) {
        this.player = player;
    }

    public void openIfNot() {
        if (!isEnteringPin()) {
            enterPin();
        }
    }

    public boolean enterPin(Runnable onEnterPin) {
        activateMod();
        if (hasEnteredPin()) {
            return true;
        }
        pinInterface = new EnterBankPinInterface(player, onEnterPin);
        pinInterface.open();
        return false;
    }

    public boolean enterPin() {
        return enterPin(null);
    }

    public boolean hasPin() {
        return !hashedPin.equals("No bank pin.");
    }

    public boolean hasEnteredPin() {
        return !hasPin() || enteredPin;
    }

    public String pinToString(List<Integer> digits) {
        return digits.stream().map(digit -> Integer.toString(digit)).
            collect(Collectors.joining());
    }

    public void setPinEntered() {
        enteredPin = true;
    }

    public void clearPinInterface() {
        pinInterface = null;
    }

    public boolean isEnteringPin() {
        return pinInterface != null;
    }

    public void setHashedPin(String hashedPin) {
        if (hashedPin == null)
            hashedPin = "No bank pin.";
        this.hashedPin = hashedPin;
    }

    public String getHashedPin() {
        return hashedPin;
    }

    public BankPinInterface getPinInterface() {
        return pinInterface;
    }

    public void setRecoveryDays(int recoveryDays) {
        this.recoveryDays = recoveryDays;
    }

    public int getRecoveryDays() {
        return recoveryDays;
    }

    public void setPinInterface(BankPinInterface pinInterface) {
        this.pinInterface = pinInterface;
    }

    public int getPinLength() {
        return pinLength;
    }

    public void setPinLength(int pinLength) {
        this.pinLength = pinLength;
    }

    public void deletePin() {
        if (player.getBankPin().hasEnteredPin()) {
            LocalDateTime activationDate = LocalDateTime.now().plusDays(0);
            if (setPendingMod(new BankPinModification("No bank pin.", 0, 0, activationDate, "Delete PIN"))) {
                player.message("Your have entered your PIN so changes will take effect immediately.");
                activateMod();
            }
        }
        else {
            LocalDateTime activationDate = LocalDateTime.now().plusDays(recoveryDays);
            if (setPendingMod(new BankPinModification("No bank pin.", 0, recoveryDays, activationDate, "Delete PIN"))) {
                player.message("Your bank pin will be deleted in " + recoveryDays + " days.");
            }
        }
    }

    public void changePin(String hashedPin, int pinLength) {
        if (player.getBankPin().hasEnteredPin()) {
            LocalDateTime activationDate = LocalDateTime.now().plusDays(0);
            if (setPendingMod(new BankPinModification(hashedPin, pinLength, 0, activationDate, "Change PIN"))) {
                player.message("Your have entered your PIN so changes will take effect immediately.");
                activateMod();
            }
        }
        else {
            LocalDateTime activationDate = LocalDateTime.now().plusDays(recoveryDays);
            if (setPendingMod(new BankPinModification(hashedPin, pinLength, recoveryDays, activationDate, "Change PIN"))) {
                player.message("Your bank pin will be changed in " + recoveryDays + " days.");
            }
        }
    }

    public void changeRecoveryDays(int newRecoveryDays) {
        LocalDateTime activationDate = LocalDateTime.now().plusDays(recoveryDays);
        if (setPendingMod(new BankPinModification(hashedPin, pinLength, newRecoveryDays, activationDate, "Change recovery delay"))) {
            player.message("Your recovery delay will be changed in " + recoveryDays + " days.");
        }
    }

    public boolean setPendingMod(BankPinModification newPendingMod) {
        if (pendingMod != null && newPendingMod != null) {
            player.message("Please cancel your pending PIN changes first, before applying another one.");
            return false;
        }
        pendingMod = newPendingMod;
        return true;
    }
    public void activateModonlogin() {
        if (pendingMod == null) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(pendingMod.getActivationDate())) {
          //  setPendingMod(new BankPinModification("No bank pin.", 0, 0, now, "Delete PIN"));
            new BankPinModification("No bank pin.", 0, 0, now, "Delete PIN");
            hashedPin = pendingMod.getHashedPin();
            pinLength = pendingMod.getPinLength();
            recoveryDays = pendingMod.getRecoveryDays();
            pendingMod = null;
            World.getWorld().ls.savePlayerAsync(player);
            setPinEntered();
            player.message("Your bank pin settings were successfully changed.");
        }
    }
    public void activateMod() {
        if (pendingMod == null) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(pendingMod.getActivationDate())) {
            hashedPin = pendingMod.getHashedPin();
            pinLength = pendingMod.getPinLength();
            recoveryDays = pendingMod.getRecoveryDays();
            pendingMod = null;
            World.getWorld().ls.savePlayerAsync(player);
          //  setPinEntered();
            player.message("Your bank pin settings were successfully changed.");
        }
    }

    public BankPinModification getPendingMod() {
        return pendingMod;
    }

}
