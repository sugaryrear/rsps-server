package com.ferox.game.world.entity.combat.method.impl.specials.melee;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatSpecial;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.chainedwork.Chain;

public class AncientGodsword extends CommonCombatMethod {
    @Override
    public void prepareAttack(Mob mob, Mob target) {
        final Player player = (Player) mob;
        player.animate(player.getEquipment().contains(ItemIdentifiers.ANCIENT_GODSWORD) ? 7639 : 7638);
        boolean gfx_gold = player.getAttribOr(AttributeKey.ZGS_GFX_GOLD, false);
        player.graphic(gfx_gold ? 1996 : 1996);
        //TODO it.player().world().spawnSound(it.player().tile(), 3869, 0, 10)

        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
        hit.submit();
        Chain.bound(null).runFn(8, () -> {
                if (mob.tile().isWithinDistance(target.tile(), 5)) {
                    target.hit(mob, 25, 1);
                    target.graphic(2003);
                    mob.message("You heal from the ancient godswords special.");
                    mob.heal(25);
                    if (target.isPlayer()) {
                        target.message("You took damage");
                    }

                } else {
                    if (target.isPlayer()) {
                        target.message("You took no damage");
                    }
                }
            });

        CombatSpecial.drain(mob, CombatSpecial.ANCIENT_GODSWORD.getDrainAmount());
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
