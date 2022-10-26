package com.ferox.game.world.entity.combat;

public class CombatConstants {

    /** The radius that retribution will hit players in. */
    // All players within currently 5 squares will get hit by the retribution
    // effect.
    public static final int RETRIBUTION_RADIUS = 5;

    /**
     * The amount of time it takes for cached damage to timeout.
     */
    // Damage cached for currently 60 seconds will not be accounted for.
    public static final long DAMAGE_CACHE_TIMEOUT = 60000;

    /**
     * The amount of damage that will be drained by combat protection prayer.
     */
    public static final double ELYSIAN_DAMAGE_REDUCTION = .75; // 25% damage reduction
    public static final double DINHS_BULWARK_REDUCTION = .6; // 20% damage reduction
    public static final double TSTOD_DAMAGE_REDUCTION = .5; //50% damage reduction

    /**
     * The maximum amount of damage inflicted by retribution.
     */
    // Damage between currently 0-15 will be inflicted if in the specified
    // radius when the retribution prayer effect is activated.
    public static final int MAXIMUM_RETRIBUTION_DAMAGE = 15;

    //Recoil item id
    public static final int RING_OF_RECOIL_ID = 2550;

}
