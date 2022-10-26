package com.ferox.game.world.entity.combat.method.impl.npcs.raids;

import com.ferox.game.content.raids.party.Party;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.util.timers.TimerKey;

public class VespineSoldier extends CommonCombatMethod {
    @Override
    public void prepareAttack(Mob mob, Mob target) {
        Player player = (Player) target;
       attack(player);

    }
    private void attack(Player player) {


        mob.animate(7455);
        Party party = player.raidsParty;
        if(party == null) {
            return;
        }



        for (Player member : party.getMembers()) {
            if (member != null && member.isInsideRaids()) {

                new Projectile(mob, member, 473, 41, 60, 45, 30, 0, 10, 15).sendProjectile();
                target.hit(mob, CombatFactory.calcDamageFromType(mob, member, CombatType.RANGED), 2, CombatType.RANGED).checkAccuracy().submit();
            }
        }


        mob.getTimers().register(TimerKey.COMBAT_ATTACK, 4);
    }
    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 8;
    }

}
