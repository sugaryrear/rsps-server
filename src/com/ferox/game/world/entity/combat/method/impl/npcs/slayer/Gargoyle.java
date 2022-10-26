package com.ferox.game.world.entity.combat.method.impl.npcs.slayer;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.util.ItemIdentifiers;

/**
 * @author PVE
 * @Since augustus 05, 2020
 */
public class Gargoyle extends CommonCombatMethod {

    public static int getNormalId() {
        return 412;
    }

    public static int getCrumblingId() {
        return 413;
    }

    public static void onDeath(Npc npc) {
        npc.transmog(getNormalId());
    }

    public static void smash(Player player, Npc npc, boolean manual) {
        if (npc.getCombat().getTarget() != player) {
            player.message("That gargoyle is not fighting you.");
            return;
        }
        if (manual && npc.hp() > 9) {
            player.message("The gargoyle is not weak enough to be smashed!");
            return;
        }

        player.animate(401);
        String plural = player.getEquipment().containsAny(ItemIdentifiers.GRANITE_MAUL, ItemIdentifiers.GRANITE_MAUL_12848, ItemIdentifiers.GRANITE_MAUL_24225) ? "granite maul" : "rock hammer";
        player.message("You smash the Gargoyle with the "+plural+".");
        npc.hp(0, 0);
        npc.die();
        npc.transmog(getCrumblingId());
        npc.animate(1520);
    }

    private void basicAttack(Mob mob, Mob target) {
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
        mob.animate(mob.attackAnimation());
    }

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        basicAttack(mob, target);
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
