package com.ferox.game.world.entity.combat.method.impl.specials.melee;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatSpecial;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.position.Tile;
import com.ferox.util.NpcIdentifiers;
import com.ferox.util.chainedwork.Chain;
import com.ferox.util.timers.TimerKey;

/**
 * @author Patrick van Elderen | February, 16, 2021, 10:10
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class AncientWarriorSword extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        //First attack
        Chain.bound(null).runFn(1, () -> {
            mob.animate(4198);
            mob.forceChat("I CALL UP ON THE STRENGTH OF THE ANCIENT WARRIORS!");
            CombatSpecial.drain(mob, CombatSpecial.ANCIENT_WARRIOR_SWORD.getDrainAmount());
            Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
            hit.submit();
            mob.getTimers().register(TimerKey.COMBAT_ATTACK,8);
            //Second attack
        }).then(3, () -> {
            mob.animate(2891);
            mob.forceChat("I SUMMON THE ANCIENT WARRIOR!");
            Npc ancient_warrior = new Npc(NpcIdentifiers.THORVALD_THE_WARRIOR, new Tile(target.tile().x, target.tile().y - 1));
            World.getWorld().registerNpc(ancient_warrior);
            ancient_warrior.setEntityInteraction(target);

            Chain.bound(null).runFn(3, () -> {
                ancient_warrior.animate(401);
                target.hit(mob, World.getWorld().random(1,55));
            }).then(2, () -> World.getWorld().unregisterNpc(ancient_warrior));
        });
        CombatSpecial.drain(mob, CombatSpecial.ANCIENT_WARRIOR_SWORD.getDrainAmount());
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 1;
    }
}
