package com.ferox.game.world.entity.combat.ranged;

public enum KnifeDrawback {

    BRONZE_KNIFE(864, 219, 212),
    IRON_KNIFE(863, 220, 213),
    STEEL_KNIFE(865, 221, 214),
    BLACK_KNIFE(869, 222, 215),
    MITHRIL_KNIFE(866, 223, 216),
    ADAMANT_KNIFE(867, 224, 217),
    RUNITE_KNIFE(868, 225, 218),


    //(p) .. gfx and projectile are the same as default. they need to be identified.
    BRONZE_KNIFE_P(870, 219, 212),
    IRON_KNIFE_P(871, 220, 213),
    STEEL_KNIFE_P(872, 221, 214),
    BLACK_KNIFE_P(874, 222, 215),
    MITHRIL_KNIFE_P(873, 223, 216),
    ADAMANT_KNIFE_P(875, 224, 217),
    RUNITE_KNIFE_P(876, 225, 218),

    // (p+)
    BRONZE_KNIFE_P_PLUS(5654, 219, 212),
    IRON_KNIFE_P_PLUS(5655, 220, 213),
    STEEL_KNIFE_P_PLUS(5656, 221, 214),
    BLACK_KNIFE_P_PLUS(5658, 222, 215),
    MITHRIL_KNIFE_P_PLUS(5657, 223, 216),
    ADAMANT_KNIFE_P_PLUS(5659, 224, 217),
    RUNITE_KNIFE_P_PLUS(5660, 225, 218),

    // (p++)
    BRONZE_KNIFE_P_PLUS_PLUS(5661, 219, 212),
    IRON_KNIFE_P_PLUS_PLUS(5662, 220, 213),
    STEEL_KNIFE_P_PLUS_PLUS(5663, 221, 214),
    BLACK_KNIFE_P_PLUS_PLUS(5665, 222, 215),
    MITHRIL_KNIFE_P_PLUS_PLUS(5664, 223, 216),
    ADAMANT_KNIFE_P_PLUS_PLUS(5666, 224, 217),
    RUNITE_KNIFE_P_PLUS_PLUS(5667, 225, 218),

    DRAGON_KNIFE(22804, -1, 28),
    DRAGON_KNIFE_P(22806, -1, 697),
    DRAGON_KNIFE_P_PLUS(22808, -1, 697),
    DRAGON_KNIFE_P_PLUS_PLUS(22810, -1, 697);

    public int arrow, gfx, projectile;

    KnifeDrawback(int arrow, int gfx, int projectile) {
        this.arrow = arrow;
        this.gfx = gfx;
        this.projectile = projectile;
    }

    public static KnifeDrawback find(int ammo) {
        for (KnifeDrawback knifeDrawback : KnifeDrawback.values()) {
            if(knifeDrawback.arrow == ammo) {
                return knifeDrawback;
            }
        }
        return null;
    }
}
