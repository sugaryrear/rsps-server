package com.ferox.game.world.entity.combat.method.impl;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.Combat;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.magic.Autocasting;
import com.ferox.game.world.entity.combat.magic.CombatSpell;
import com.ferox.game.world.entity.combat.magic.CombatSpells;
import com.ferox.game.world.entity.masks.graphics.Graphic;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.Player;

import java.util.Arrays;

import static com.ferox.game.content.items.combine.ElderWand.*;
import static com.ferox.util.CustomItemIdentifiers.ELDER_WAND;
import static com.ferox.util.CustomItemIdentifiers.HOLY_SANGUINESTI_STAFF;
import static com.ferox.util.ItemIdentifiers.*;

/**
 * Represents the combat method for magic attacks.
 *
 * @author Professor Oak
 */
public class MagicCombatMethod extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        CombatSpell spell = mob.getCombat().getCastSpell() != null ? mob.getCombat().getCastSpell() : mob.getCombat().getAutoCastSpell();

        if (spell == null) {
            mob.message("What spell is that?");
            return;
        }

        if (target != null && !target.dead() && !mob.dead()) {

            // delete runes here using the canCast method. doesnt check canCast, that is already done before.
            spell.canCast(mob.getAsPlayer(), target, true);
            spell.startCast(mob, target);
        }
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        CombatSpell spell = mob.getCombat().getCastSpell() != null ? mob.getCombat().getCastSpell() : mob.getCombat().getAutoCastSpell();
        if (spell != null) {
            return spell.getAttackSpeed(mob);
        }
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        if (mob.isPlayer()) {
            Player player = (Player) mob;
            //Trident of the seas and Trident of the swamp have a default range of 8, but also allow longrange attack style.
            if (player.getEquipment().hasAt(EquipSlot.WEAPON, TRIDENT_OF_THE_SEAS) || player.getEquipment().hasAt(EquipSlot.WEAPON, TRIDENT_OF_THE_SWAMP) || player.getEquipment().hasAt(EquipSlot.WEAPON, SANGUINESTI_STAFF) || player.getEquipment().hasAt(EquipSlot.WEAPON, HOLY_SANGUINESTI_STAFF)) {
                return 8;
            }
        }
        //All combat magic spells have an attack range of 10 regardless of the level of the spell of which to cast it.
        return 10;
    }

    public void handleAfterHit(Hit hit) {
        Mob attacker = hit.getAttacker();
        Mob target = hit.getTarget();
        boolean accurate = hit.isAccurate();

        if (attacker.dead() || target.dead()) {
            return;
        }

        CombatSpell spell = hit.spell;

        if (spell != null) {
            //Elder wand in PvM
            if (attacker.isPlayer() && target.isNpc()) {
                Player player = attacker.getAsPlayer();

                if (player.getEquipment().hasAt(EquipSlot.WEAPON, ELDER_WAND) && Arrays.stream(Combat.ELDER_WAND_SPELLS).anyMatch(hp_spell -> hp_spell == spell)) {
                    //Randomize new spell
                    var roll = World.getWorld().random(66);
                    if (roll >= 0 && roll <= 20) {
                        Autocasting.toggleAutocast(player, CRUCIATUS_CURSE_SPELL);
                    } else if (roll >= 21 && roll <= 40) {
                        Autocasting.toggleAutocast(player, EXPELLIARMUS_SPELL);
                    } else if (roll >= 41 && roll <= 60) {
                        Autocasting.toggleAutocast(player, PETRIFICUS_TOTALUS_SPELL);
                    } else if (roll >= 61 && roll <= 65) {
                        Autocasting.toggleAutocast(player, SECTUMSEMPRA_SPELL);// a slim chance to hit sectum sempra
                    } else {
                        Autocasting.toggleAutocast(player, AVADA_KEDAVRA_SPELL);// 1/66 chance to hit avada kedavra
                    }
                }
            }

            if (accurate) {

                // Send proper end graphics for the spell because it was accurate
                spell.endGraphic().ifPresent(target::performGraphic);

                if (spell.name().equals("Sanguinesti spell")) {
                    if (World.getWorld().rollDie(6, 1)) {
                        attacker.heal(hit.getDamage() / 2);
                        target.graphic(1542);
                    }
                }
            } else {
                // Send splash graphics for the spell because it wasn't accurate
                target.delayedGraphics(new Graphic(85, 90, 30),attacker.getCombat().magicSpellDelay(target) - 1);
            }
        }
    }

    @Override
    public void postAttack() {
        boolean spellWeapon = mob.getCombat().getCastSpell() == CombatSpells.ELDRITCH_NIGHTMARE_STAFF.getSpell() || mob.getCombat().getCastSpell() == CombatSpells.VOLATILE_NIGHTMARE_STAFF.getSpell();

        if (mob.getCombat().getAutoCastSpell() == null && !spellWeapon) {
            mob.getCombat().reset();// combat is stopped for magic when not autocasting. spell on entity is a 1-time attack.
        }
        mob.getCombat().setCastSpell(null);
    }
}
