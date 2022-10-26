package com.ferox.game.world.items.container.impl;

import com.ferox.game.world.items.container.ItemContainer;

/**
 * @author Patrick van Elderen | March, 28, 2021, 16:51
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class CompostContainer extends ItemContainer {

    public static final int CAPACITY = 15;

    public CompostContainer() {
        super(CAPACITY, StackPolicy.STANDARD);
    }

}
