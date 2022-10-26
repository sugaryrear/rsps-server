package com.ferox.game.content.areas.dungeons.godwars;

import com.ferox.game.content.skill.impl.farming.Constants;
import com.ferox.game.content.skill.impl.farming.Farming;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Tile;

import java.util.Optional;

import static com.ferox.util.ObjectIdentifiers.ROPE_26370;

public class GWDHole {
    private final Player player;

    public GWDHole(Player player) {
        this.player = player;
    }

    public void updateObjects() {
     //   player.message(player.tile().region()+"");
        boolean godwarsDungeon = player.getAttribOr(AttributeKey.GOD_WARS_DUNGEON, false);
        boolean firstropefromsaradomin = player.getAttribOr(AttributeKey.FIRST_SARADOMIN_ROPE, false);
        boolean secondropefromsaradomin = player.getAttribOr(AttributeKey.SECOND_SARADOMIN_ROPE, false);

//        if (player.tile().region() == 11578) {
//
//            if (godwarsDungeon) {
//                GameObject grass = new GameObject(26418, new Tile(2917,3745, player.getZ()), 10, 0);
//                ObjectManager.addObj(grass);
//            } else {
//
//            }
//       }

        if (player.tile().region() == 11602) {

            if (firstropefromsaradomin) {
                GameObject rope = new GameObject(ROPE_26370, new Tile(2914,5300, 1), 10, 0);
                ObjectManager.addObj(rope);
            } else {

            }
            if (secondropefromsaradomin) {
                GameObject rope2 = new GameObject(ROPE_26370, new Tile(2920,5274,0), 10, 0);
                ObjectManager.addObj(rope2);
            } else {

            }
        }
    }

}
