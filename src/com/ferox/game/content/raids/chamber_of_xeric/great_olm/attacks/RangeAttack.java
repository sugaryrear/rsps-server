package com.ferox.game.content.raids.chamber_of_xeric.great_olm.attacks;

import com.ferox.game.content.raids.chamber_of_xeric.great_olm.GreatOlm;
import com.ferox.game.content.raids.chamber_of_xeric.great_olm.OlmAnimations;
import com.ferox.game.content.raids.party.Party;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.util.chainedwork.Chain;

/**
 * @author Patrick van Elderen | May, 16, 2021, 13:27
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class RangeAttack {

    public static void performAttack(Party party) {
        //System.out.println("RangeAttack");
        party.setOlmAttackTimer(6);
        Npc olm = party.getGreatOlmNpc();
        olm.performGreatOlmAttack(party);
        Chain.bound(null).runFn(1, () -> {
            if (olm.dead() || party.isSwitchingPhases()) {
                return;
            }

            for (Player member : party.getMembers()) {
                if (member != null && member.isInsideRaids() && GreatOlm.insideChamber(member)) {
                    if(member.dead() || !member.isRegistered()) {
                        return;
                    }
                    var delay = olm.getProjectileHitDelay(member);
                    new Projectile(olm, member, Attacks.GREEN_CRYSTAL_FLYING, 25, olm.projectileSpeed(member), 70, 31, 0).sendProjectile();
                    member.hit(olm, CombatFactory.calcDamageFromType(olm, member, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
                }
            }

            Chain.bound(null).runFn(2, () -> OlmAnimations.resetAnimation(party));
        });
    }
}
