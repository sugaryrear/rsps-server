package com.ferox.game.world.entity.combat.method.impl.specials.melee;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatSpecial;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.util.ItemIdentifiers;

import static com.ferox.util.CustomItemIdentifiers.BEGINNER_AGS;
import static com.ferox.util.CustomItemIdentifiers.HWEEN_ARMADYL_GODSWORD;

public class ArmadylGodsword extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        final Player player = (Player) mob;
        int animation = 7644;
        if(player.getEquipment().contains(ItemIdentifiers.ARMADYL_GODSWORD_OR))
            animation = 7645;
        if(player.getEquipment().contains(BEGINNER_AGS) || player.getEquipment().contains(HWEEN_ARMADYL_GODSWORD))
            animation = 11902;
        player.animate(animation);
        boolean gfx_gold = player.getAttribOr(AttributeKey.AGS_GFX_GOLD, false);
        player.graphic(gfx_gold ? 1747 : 1211);
        //TODO it.player().world().spawnSound(it.player().tile(), 3869, 0, 10)

        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
        hit.submit();
        CombatSpecial.drain(mob, CombatSpecial.ARMADYL_GODSWORD.getDrainAmount());
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
