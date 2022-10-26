package com.ferox.game.world.entity.combat.bountyhunter;

public enum BountyHunterSkull {

    NONE(-1, 28030, "None"), BRONZE(0, 28032, "V. Low"), SILVER(1_000_000, 28034, "Low"), GREEN(3_000_000, 28036, "Medium"), BLUE(6_000_000, 28038, "High"), RED(10_000_000, 28040,
        "V. High");

    private long risk;
    private int frameId;
    private String representation;

    private BountyHunterSkull(long risk, int frameId, String representation) {
        this.risk = risk;
        this.frameId = frameId;
        this.representation = representation;
    }

    public long getRisk() {
        return risk;
    }

    public int getFrameId() {
        return frameId;
    }

    public String getRepresentation() {
        return representation;
    }

    public static BountyHunterSkull getSkull(long risk) {
        BountyHunterSkull skull = BountyHunterSkull.BRONZE;
        for (int i = BountyHunterSkull.BRONZE.ordinal(); i < BountyHunterSkull.values().length; i++) {
            long req = BountyHunterSkull.values()[i].risk;
            if (skull.risk < req && risk >= req) {
                skull = BountyHunterSkull.values()[i];
            }
        }
        return skull;
    }

}

