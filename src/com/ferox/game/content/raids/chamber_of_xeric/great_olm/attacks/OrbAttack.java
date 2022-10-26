package com.ferox.game.content.raids.chamber_of_xeric.great_olm.attacks;

import com.ferox.game.content.raids.chamber_of_xeric.great_olm.GreatOlm;
import com.ferox.game.content.raids.chamber_of_xeric.great_olm.OlmAnimations;
import com.ferox.game.content.raids.party.Party;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.prayer.default_prayer.DefaultPrayers;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.util.chainedwork.Chain;

/**
 * @author Patrick van Elderen | May, 16, 2021, 13:34
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class OrbAttack {

    private static final CombatType[] randomTypes = new CombatType[]{CombatType.MAGIC, CombatType.RANGED, CombatType.MELEE};
    private static final String[] sphereNames = new String[]{"magical power", "accuracy and dexterity", "agression"};
    private static final String[] sphereColors = new String[]{"@blu@", "@gre@", "@red@"};

    public static void performAttack(Party party) {
        //System.out.println("OrbAttack");
        Npc olm = party.getGreatOlmNpc();
        olm.performGreatOlmAttack(party);
        party.setOlmAttackTimer(6);
        Chain.bound(null).runFn(1, () -> {
            if (olm.dead() || party.isSwitchingPhases()) {
                return;
            }

            for (int i = 0; i < 3; i++) {
                Player member = party.randomPartyPlayer();
                if (member.isInsideRaids() && GreatOlm.insideChamber(member)) {
                    if (!party.getPlayersToAttack().contains(member)) {
                        member.message(sphereColors[i] + "The Basilisk fires a sphere of " + sphereNames[i] + " your way, </col>affecting your prayers.");
                        DefaultPrayers.closeAllPrayers(member);
                        new Projectile(olm, member, 1341 + (i * 2), 25, olm.projectileSpeed(member), 70, 31, 0).sendProjectile();
                        party.getPlayersToAttack().add(member);
                    }
                }
            }
        }).then(1, () -> {
            OlmAnimations.resetAnimation(party);
        }).then(3, () -> {
            int combatMethodIndex = 0;
            for (Player member : party.getPlayersToAttack()) {
                if (member.isInsideRaids() && GreatOlm.insideChamber(member)) {
                    if (DefaultPrayers.usingPrayer(member, DefaultPrayers.getProtectingPrayer(randomTypes[combatMethodIndex]))) {
                        member.skills().alterSkill(Skills.PRAYER,-member.skills().level(Skills.PRAYER) / 2);
                    }
                    combatMethodIndex++;
                }
            }
            party.getPlayersToAttack().clear();
        });
    }
}
