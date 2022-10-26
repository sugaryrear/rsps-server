package com.ferox.game.world.entity.combat.method.impl.specials.melee;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatSpecial;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.util.ItemIdentifiers;

public class SaradominGodsword extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        final Player player = (Player) mob;
        player.animate(player.getEquipment().contains(ItemIdentifiers.SARADOMIN_GODSWORD_OR) ? 7641 : 7640);
        boolean gfx_gold = player.getAttribOr(AttributeKey.SGS_GFX_GOLD, false);
        player.graphic(gfx_gold ? 1745 : 1209);
        //TODO it.player().world().spawnSound(it.player().tile(), 3869, 0, 10)
        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
        hit.submit();

        // Heal the player & restore prayer and hit the enemy
        player.heal(Math.max(10, hit.getDamage() / 2)); // Min heal is 10

        player.skills().alterSkill(Skills.PRAYER, Math.max(5, hit.getDamage() / 4)); // Min heal is 5 for prayer
        CombatSpecial.drain(mob, CombatSpecial.SARADOMIN_GODSWORD.getDrainAmount());
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
