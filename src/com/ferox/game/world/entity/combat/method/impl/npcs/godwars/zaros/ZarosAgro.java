package com.ferox.game.world.entity.combat.method.impl.npcs.godwars.zaros;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.mob.npc.AggressionCheck;

import java.util.Arrays;
import java.util.List;

public class ZarosAgro implements AggressionCheck {

    private final List<Integer> ZAROS_PROTECTION_EQUIPMENT = Arrays.asList(26374, 26233);

    @Override
    public boolean shouldAgro(Mob mob, Mob victim) {
        for(int armour : ZAROS_PROTECTION_EQUIPMENT) {
            if(victim.isPlayer()) {
                if(victim.getAsPlayer().getEquipment().contains(armour))
                    return false;
            }
        }
        return true;
    }
}
