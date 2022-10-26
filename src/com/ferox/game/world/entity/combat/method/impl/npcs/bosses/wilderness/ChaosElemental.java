package com.ferox.game.world.entity.combat.method.impl.npcs.bosses.wilderness;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatSpecial;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.magic.Autocasting;
import com.ferox.game.world.entity.combat.method.CombatMethod;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.combat.weapon.WeaponInterfaces;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;

public class ChaosElemental extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        Npc npc = (Npc) mob;
        int random = Utils.random(7);
        npc.animate(npc.attackAnimation());
        switch (random) {
            case 1:
                if(!target.tile().memberCave()) {
                    teleport_attack(npc, target);
                } else {
                    primary_attack(npc, target);
                }
                break;
            case 2:
                disarming_attack(npc, target);
                break;
            default:
                primary_attack(npc, target);
        }
    }

    private void disarming_attack(Npc npc, Mob target) {
        attack_logic(npc, target, 550, 551, 552);
        Player player = (Player) target;
        final Item item = player.getEquipment().get(EquipSlot.WEAPON);
        if (item != null && player.inventory().hasCapacityFor(item)) {
            player.getEquipment().remove(item, EquipSlot.WEAPON, true);
            player.getEquipment().unequip(EquipSlot.WEAPON);
            WeaponInterfaces.updateWeaponInterface(player);
            CombatSpecial.updateBar(player);
            player.setSpecialActivated(false);
            Autocasting.setAutocast(player, null);
            player.looks().resetRender();
            player.inventory().add(item);
        }
    }

    private void teleport_attack(Npc npc, Mob target) {
        int random = Utils.random(5 + 1);//+1 cuz we can hit 0
        attack_logic(npc, target, 553, 554, 555);
        target.teleport(target.tile().x - random, target.tile().y - random, target.tile().level);
    }

    private void primary_attack(Npc npc, Mob target) {
        CombatType combat_style = World.getWorld().random(10) > 7 ? CombatType.MAGIC : CombatType.RANGED;
        CombatMethod combatMethod = combat_style == CombatType.MAGIC ? CombatFactory.MAGIC_COMBAT : CombatFactory.RANGED_COMBAT;

        attack_logic(npc, target, 556, 557, 558);
        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, combat_style), combat_style).checkAccuracy().submit();
    }

    private void attack_logic(Npc npc, Mob target, int initial_graphic, int projectile, int end_graphic) {
        int tileDist = npc.tile().distance(target.tile());
        int time = Math.max(1, (20 + tileDist * 12) / 30) + 1;
        npc.graphic(initial_graphic, 80, 0);
        new Projectile(npc, target, projectile, 35, 12 * tileDist, 40, 25, 0).sendProjectile();
        Chain.bound(null).name("ChaosElementalAttackLogicTask").runFn(time, () -> target.graphic(end_graphic, 75, 0));
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 10;
    }
}
