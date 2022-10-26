package com.ferox.game.world.entity.combat.method.impl.specials.melee;

import com.ferox.game.content.duel.DuelRule;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatSpecial;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.mob.movement.MovementQueue;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Tile;
import com.ferox.util.timers.TimerKey;

public class ZamorakianSpear extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob attacker, Mob victim) {
        attacker.animate(1064);
        attacker.graphic(253, 100, 0);

        attacker.face(victim.tile());
        attacker.getTimers().register(TimerKey.COMBAT_ATTACK, 2);
        attacker.getCombat().setTarget(victim);

        //The special attack can only be used on targets that take up one square.
        if(attacker.isPlayer() && victim.isNpc()) {
            var playerAttacker = (Player) attacker;
            if(victim.getSize() > 1) {
                playerAttacker.message("You can't spear this monster.");
                return;
            }
        }

        //Player vs Player
        if(attacker.isPlayer() && victim.isPlayer()) {
            var player = (Player) attacker;

            //Start with checks first

            if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_MOVEMENT.ordinal()]) {
                player.message("This weapon's special attack cannot be used in this duel.");
                return;
            }

            //The effects of this special are non-stackable, meaning that players cannot use the spear's special attack on a target who is already stunned.
            if (victim.stunned()) {
                player.message("They're already stunned!");
                return;
            }

            // Since this weapon doesn't deal damage, manually extend the in-combat timer.
            victim.getTimers().extendOrRegister(TimerKey.COMBAT_LOGOUT, 16);
            victim.getMovementQueue().clear(); // clears any pre-existing movement. spear replaces their movement

            //It pushes an opponent back and stuns them for three seconds.
            Tile targTile = victim.tile().transform(-1, 0, 0);
            boolean legal = victim.getMovementQueue().canWalkNoLogicCheck(-1, 0);
            if (!legal) {
                targTile = victim.tile().transform(1, 0, 0);
                legal = victim.getMovementQueue().canWalkNoLogicCheck(1, 0);
                if (!legal) {
                    return; // No valid move to go!
                }
            }

            victim.stun(5);
            victim.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
        }
        CombatSpecial.drain(attacker, CombatSpecial.ZAMORAKIAN_SPEAR.getDrainAmount());
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return 0;
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 0;
    }
}
