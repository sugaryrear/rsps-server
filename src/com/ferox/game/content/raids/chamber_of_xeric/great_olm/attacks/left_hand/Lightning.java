package com.ferox.game.content.raids.chamber_of_xeric.great_olm.attacks.left_hand;

import com.ferox.game.content.raids.chamber_of_xeric.great_olm.GreatOlm;
import com.ferox.game.content.raids.chamber_of_xeric.great_olm.attacks.Attacks;
import com.ferox.game.content.raids.party.Party;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.prayer.default_prayer.DefaultPrayers;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Color;
import com.ferox.util.chainedwork.Chain;

/**
 * @author Patrick van Elderen | May, 16, 2021, 18:41
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class Lightning {

    public static void performAttack(Party party, int height) {
        //System.out.println("Lightning");
        party.setLeftHandAttackTimer(20);
        Chain.bound(null).runFn(1, () -> {
            if (party.isLeftHandDead()) {
                return;
            }
            party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getLeftHandObject(), 7358));
        }).then(2, () -> {
            for (int i = 0; i < 2; i++) {
                party.getLightningSpots()[i] = new Tile(Attacks.randomLocation(height).getX(), 5748, height);
                World.getWorld().tileGraphic(Attacks.GREEN_LIGHTNING, party.getLightningSpots()[i], 0, 0);
            }
            for (int i = 2; i < 4; i++) {
                party.getLightningSpots()[i] = new Tile(Attacks.randomLocation(height).getX(), 5731, height);
                World.getWorld().tileGraphic(Attacks.GREEN_LIGHTNING, party.getLightningSpots()[i], 0, 0);
            }
        }).then(4, () -> {
            for (int index = 0; index < 2; index++) {
                party.getLightningSpots()[index] = new Tile(party.getLightningSpots()[index].getX(), party.getLightningSpots()[index].getY() - 1, height);
            }
            for (int index = 2; index < 4; index++) {
                party.getLightningSpots()[index] = new Tile(party.getLightningSpots()[index].getX(), party.getLightningSpots()[index].getY() + 1, height);
            }
            for (int index = 0; index < 4; index++) {
                World.getWorld().tileGraphic(Attacks.GREEN_LIGHTNING, party.getLightningSpots()[index], 0, 0);
                if (party.getLightningSpots()[index].getY() > 5748 || party.getLightningSpots()[index].getY() < 5731) {
                    return;
                }

                if(party.getMembers() == null) {
                    System.err.println("The party has no members!");
                    return;
                }

                for (Player member : party.getMembers()) {
                    if (member != null && member.isInsideRaids() && GreatOlm.insideChamber(member)) {
                        if (member.tile().sameAs(party.getLightningSpots()[index])) {
                            member.message("I should be frozen for 2 seconds right now.");
                            DefaultPrayers.closeAllPrayers(member);
                            member.message(Color.RED.wrap("You've been electrocuted to the spot!"));
                            member.message("You've been injured and can't use protection prayers!");
                            member.hit(party.getGreatOlmNpc(), World.getWorld().random(10, 25), CombatType.MAGIC).checkAccuracy().submit();
                        }
                    }
                }
            }
        });
    }
}
