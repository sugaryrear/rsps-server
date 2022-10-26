package com.ferox.game.content.interfaces;

import com.ferox.game.content.areas.edgevile.BobBarter;
import com.ferox.game.content.mechanics.Poison;
import com.ferox.game.world.entity.combat.CombatSpecial;
import com.ferox.game.world.entity.combat.Venom;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import com.ferox.util.SecondsTimer;
import com.ferox.util.timers.TimerKey;

public class DataOrbs extends PacketInteraction {

    private final SecondsTimer button_delay = new SecondsTimer();

    @Override
    public boolean handleButtonInteraction(Player player, int button) {
        if (button == 1510) {
            if(WildernessArea.inWilderness(player.tile())) {
                player.message(Color.RED.wrap("What the hell are you thinking? That doesn't work in dangerous areas!"));
                return true;
            }

            if (button_delay.active()) {
                player.message(Color.RED.wrap("You can only use this every 5 seconds."));
                return true;
            }

            if(!player.tile().homeRegion() || player.tile().region() == 9772) {
                player.message(Color.RED.wrap("You can't heal yourself here!"));
                return true;
            }
            button_delay.start(5);
            player.getBank().depositInventory();
            player.getBank().depositeEquipment();
            return true;
        }

        if (button == 1511) {
            if(WildernessArea.inWilderness(player.tile())) {
                player.message(Color.RED.wrap("What the hell are you thinking? That doesn't work in dangerous areas!"));
                return true;
            }

            if (button_delay.active()) {
                player.message(Color.RED.wrap("You can only use this every 5 seconds."));
                return true;
            }

            if(!player.tile().homeRegion() || player.tile().region() == 9772) {
                player.message(Color.RED.wrap("You can't heal yourself here!"));
                return true;
            }
            button_delay.start(5);
            BobBarter.decant(player);
            return true;
        }

        if (button == 1512) {
            if(WildernessArea.inWilderness(player.tile())) {
                player.message(Color.RED.wrap("What the hell are you thinking? That doesn't work in dangerous areas!"));
                return true;
            }

            if (button_delay.active()) {
                player.message(Color.RED.wrap("You can only use this every 5 seconds."));
                return true;
            }

            if(!player.tile().homeRegion() || player.tile().region() == 9772) {
                player.message(Color.RED.wrap("You can't heal yourself here!"));
                return true;
            }
            player.heal();
            button_delay.start(5);
            return true;
        }
        return false;
    }
}
