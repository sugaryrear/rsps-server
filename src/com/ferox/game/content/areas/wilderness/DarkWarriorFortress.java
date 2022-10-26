package com.ferox.game.content.areas.wilderness;

import com.ferox.game.content.packet_actions.interactions.objects.Ladders;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.ObjectIdentifiers.*;

public class DarkWarriorFortress extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(obj.getId() == LADDER_14745) {
            //bottom floor, up
            if (obj.tile().x == 3036 && obj.tile().y == 3638) {
                //NE
                Ladders.ladderUp(player, player.tile().transform(0, 0, 1), true);
            }
            if (obj.tile().x == 3036 && obj.tile().y == 3625) {
                //se
                Ladders.ladderUp(player, player.tile().transform(0, 0, 1), true);
            }
            if (obj.tile().x == 3022 && obj.tile().y == 3625) {
                //sw
                Ladders.ladderUp(player, player.tile().transform(0, 0, 1), true);
            }
            return true;
        }

        if(obj.getId() == LADDER_14747) {
            if (option == 1) {
                //TODO chatbox
            } else if (option == 2) {
                //up
                if (obj.tile().x == 3036 && obj.tile().y == 3638) {
                    //NE
                    Ladders.ladderUp(player, player.tile().transform(0, 0, 1), true);
                }
                if (obj.tile().x == 3036 && obj.tile().y == 3625) {
                    //se
                    Ladders.ladderUp(player, player.tile().transform(0, 0, 1), true);
                }
                if (obj.tile().x == 3022 && obj.tile().y == 3625) {
                    //sw
                    Ladders.ladderUp(player, player.tile().transform(0, 0, 1), true);
                }
            } else if (option == 3) {
                //down
                if (obj.tile().x == 3036 && obj.tile().y == 3638) {
                    //NE
                    Ladders.ladderDown(player, player.tile().transform(0, 0, -1), true);
                }
                if (obj.tile().x == 3036 && obj.tile().y == 3625) {
                    //se
                    Ladders.ladderDown(player, player.tile().transform(0, 0, -1), true);
                }
                if (obj.tile().x == 3022 && obj.tile().y == 3625) {
                    //sw
                    Ladders.ladderDown(player, player.tile().transform(0, 0, -1), true);
                }
            }
            return true;
        }

        if(obj.getId() == LADDER_14746) {
            //this object is the ladder found on the very top floor. goes down.
            if (obj.tile().x == 3036 && obj.tile().y == 3638) {
                //NE
                Ladders.ladderDown(player, player.tile().transform(0, 0, -1), true);
            }
            if (obj.tile().x == 3036 && obj.tile().y == 3625) {
                //se
                Ladders.ladderDown(player, player.tile().transform(0, 0, -1), true);
            }
            if (obj.tile().x == 3022 && obj.tile().y == 3625) {
                //sw
                Ladders.ladderDown(player, player.tile().transform(0, 0, -1), true);
            }
            return true;
        }
        return false;
    }
}
