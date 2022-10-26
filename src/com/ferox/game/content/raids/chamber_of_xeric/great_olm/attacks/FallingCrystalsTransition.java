package com.ferox.game.content.raids.chamber_of_xeric.great_olm.attacks;

import com.ferox.game.content.raids.chamber_of_xeric.great_olm.GreatOlm;
import com.ferox.game.content.raids.party.Party;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Tile;
import com.ferox.util.chainedwork.Chain;

/**
 * @author Patrick van Elderen | May, 16, 2021, 13:34
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class FallingCrystalsTransition {

    public static void performAttack(Party party, int height) {
        //System.out.println("FallingCrystalsTransition");

        boolean doAction = false;
        boolean doActionPlayer = false;

        int roll = World.getWorld().random(5);
        if(roll == 2)
            doAction = true;
        else if(roll == 3)
            doActionPlayer = true;

        if (party.isTransitionPhase() || (party.getCurrentPhase() == 3 && party.isLeftHandDead() && party.isRightHandDead() && party.getGreatOlmNpc().hp() > 0 && !party.getGreatOlmNpc().dead())) {
            if (doActionPlayer && World.getWorld().random(2) == 1) {
                Player member = party.randomPartyPlayer();
                if (member.isInsideRaids() && GreatOlm.insideChamber(member)) {
                    Tile posPlayer = member.tile();
                    new Projectile(new Tile(posPlayer.getX(), posPlayer.getY() - 1, height), posPlayer, -1, Attacks.FALLING_CRYSTAL, 100, 40, 220, 0, 0).sendProjectile();
                    Chain.bound(null).runFn(4, () -> {
                        World.getWorld().tileGraphic(Attacks.GREEN_PUFF, posPlayer, 0, 0);

                        for (Player player : party.getMembers()) {
                            if (player != null && player.isInsideRaids() && GreatOlm.insideChamber(member)) {
                                if (player.tile().sameAs(posPlayer)) {
                                    player.hit(party.getGreatOlmNpc(), World.getWorld().random(1, 15), 1, CombatType.MAGIC).checkAccuracy().submit();
                                } else if (player.tile().isWithinDistance(posPlayer, 1)) {
                                    player.hit(party.getGreatOlmNpc(), World.getWorld().random(1, 10), 1, CombatType.MAGIC).checkAccuracy().submit();
                                }
                            }
                        }
                    });
                }
            }
            if (doAction) {
                dropCrystal(party, height);
            }
        }
    }

    private static void dropCrystal(Party party, int height) {
        Tile pos = Attacks.randomLocation(height);
        Npc spawn = Npc.of(7556, pos);
        World.getWorld().registerNpc(spawn);
        Npc spawn1 = Npc.of(7556, new Tile(pos.getX(), pos.getY() - 1, height));
        World.getWorld().registerNpc(spawn1);

        new Projectile(spawn1, spawn, Attacks.FALLING_CRYSTAL,40, 125, 220, 0, 0).sendProjectile();
        Chain.bound(null).runFn(4, () -> {
            World.getWorld().tileGraphic(Attacks.GREEN_PUFF, pos,0,0);

            for (Player member : party.getMembers()) {
                if (member != null && member.isInsideRaids() && GreatOlm.insideChamber(member)) {
                    if (member.tile().sameAs(pos)) {
                        member.hit(member, World.getWorld().random(10, 15));
                    } else if (member.tile().isWithinDistance(pos,1)) {
                        member.hit(member, World.getWorld().random(5, 10));
                    }
                }
            }

            World.getWorld().unregisterNpc(spawn);
            World.getWorld().unregisterNpc(spawn1);
        });
    }
}
