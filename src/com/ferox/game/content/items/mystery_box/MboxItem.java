package com.ferox.game.content.items.mystery_box;

import com.ferox.game.world.items.Item;

public class MboxItem extends Item {

    public byte rarityType = -1;

    public boolean broadcastItem;

    public MboxItem(int id) {
        super(id);
    }

    public MboxItem(int id, int amount) {
        super(id, amount);
    }

    /**
     * optional rarity type override beyond {@link MysteryBox#}
     * @param rarity
     * @return
     */
    public MboxItem rarity(int rarity) {
        this.rarityType = (byte) rarity;
        return this;
    }

    public MboxItem broadcastWorldMessage(boolean broadcast) {
        this.broadcastItem = broadcast;
        return this;
    }

    public MboxItem copy() {
        return new MboxItem(getId(), getAmount()).rarity(rarityType).broadcastWorldMessage(broadcastItem);
    }
}
