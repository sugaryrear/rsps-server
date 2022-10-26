package com.ferox.game.content.items.equipment;

import com.ferox.GameServer;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.hit.SplatType;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.chainedwork.Chain;
import com.ferox.util.timers.TimerKey;

import static com.ferox.util.ItemIdentifiers.DRAGONFIRE_WARD;

/**
 * This class represents all the dragonfire shield actions.
 *
 * @author Patrick van Elderen | Zerikoth | PVE
 * @date januari 31, 2020 09:41
 */
public class DragonfireShield extends PacketInteraction {

    @Override
    public boolean handleEquipmentAction(Player player, Item item, int slot) {
        for (DragonfireShieldType type : DragonfireShieldType.values()) {
            if (item.getId() == type.charged() && slot == EquipSlot.SHIELD) {
                executeSpecialAttack(player, type);
                return true;
            }
        }
        return false;
    }

    private void executeSpecialAttack(Player player, DragonfireShieldType type) {
        if ((GameServer.properties().production || !player.getPlayerRights().isDeveloperOrGreater(player)) && player.getTimers().has(TimerKey.DRAGONFIRE_SPECIAL)) {
            player.message("Your shield is still on cooldown from its last use.");
        } else {
            Item shield = player.getEquipment().get(EquipSlot.SHIELD);
            if (shield == null)
                return;

            Mob target = player.getCombat().getTarget();
            if (target == null)
                return;

            final boolean inDistance = (player.tile().distance(target.tile()) <= 10);
            if (target != player && !player.dead() && CombatFactory.inCombat(player) && inDistance) {

                player.face(target.tile());

                // Allow timers to fire before player event
                player.getTimers().extendOrRegister(TimerKey.COMBAT_ATTACK, 4);
                player.getTimers().register(TimerKey.DRAGONFIRE_SPECIAL, 200);
                player.getCombat().reset();

                if (shield.getId() == DragonfireShieldType.WYVERN.charged()) {
                    wyvernSpecial(player, target);
                } else {
                    dragonfireSpecial(player, target);
                }
            }
        }
    }

    private void wyvernSpecial(Player attacker, Mob target) {
        int dmg = World.getWorld().random(attacker.getEquipment().hasAt(EquipSlot.SHIELD, DRAGONFIRE_WARD) ? 25 : 15);

        attacker.animate(7700);

        if (dmg > 0) {
            target.freeze(25, attacker);
        }

        Chain.bound(null).cancelWhen(() -> {
            return !attacker.tile().isWithinDistance(target.tile()) || attacker.dead(); // cancels as expected
        }).runFn(3, () -> {
            target.graphic(367);
            if (target instanceof Npc) {
                attacker.skills().addXp(Skills.MAGIC, dmg * 4);
                attacker.skills().addXp(Skills.DEFENCE, dmg * 4);
                attacker.skills().addXp(Skills.HITPOINTS, (int) (dmg * .70));
            } else {
                attacker.skills().addXp(Skills.DEFENCE, dmg * 4);
            }

            target.hit(attacker, dmg);
        });
    }

    private void dragonfireSpecial(Player attacker, Mob target) {
        int dmg = World.getWorld().random(25);
        attacker.animate(6696);
        attacker.graphic(1165);
        new Projectile(attacker, target, 1166, 20, 100, 31, 16, 0).sendProjectile();
        Chain.bound(null).cancelWhen(() -> {
            return !attacker.tile().isWithinDistance(target.tile()) || attacker.dead(); // cancels as expected
        }).runFn(3, () -> {
            if (target instanceof Npc) {
                attacker.skills().addXp(Skills.MAGIC, dmg * 4);
                attacker.skills().addXp(Skills.DEFENCE, dmg * 4);
                attacker.skills().addXp(Skills.HITPOINTS, (int) (dmg * .70));
            } else {
                attacker.skills().addXp(Skills.DEFENCE, dmg * 4);
            }
            target.hit(attacker, dmg, SplatType.HITSPLAT);
        });
    }

    public static DragonfireShieldType getType(Player player) {
        for (DragonfireShieldType type : DragonfireShieldType.values()) {
            Item shield = player.getEquipment().get(EquipSlot.SHIELD);
            if (shield != null) {
                if (shield.getId() == type.charged() || shield.getId() == type.uncharged()) {
                    return type;
                }
            }
        }
        return null;
    }

    public enum DragonfireShieldType {
        REGULAR(11284, 11283, 6700),
        HALLOWEEN(30241, 30241, 6700),
        WARD(22003, 22002, 65535),
        WYVERN(21634, 21633, 65535);

        DragonfireShieldType(int uncharged, int charged, int unchargeAnimationId) {
            this.charged = charged;
            this.uncharged = uncharged;
            this.unchargeAnimationId = unchargeAnimationId;
        }

        private final int uncharged;
        private final int charged;
        private final int unchargeAnimationId;

        public int charged() {
            return charged;
        }

        public int uncharged() {
            return uncharged;
        }

        public int unchargeAnimationId() {
            return unchargeAnimationId;
        }
    }
}
