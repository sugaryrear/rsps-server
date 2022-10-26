package com.ferox.game.content.areas.dungeons.godwars;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.combat.method.impl.npcs.godwars.zaros.Nex;
import com.ferox.game.world.entity.mob.movement.MovementQueue;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ferox.util.ObjectIdentifiers.*;

public class BossRoomDoors extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if (option == 1) {
            // Zamorak
            int zamorak_kc = player.getAttribOr(AttributeKey.GWD_ZAMORAK_KC, 0);
            if (obj.getId() == BIG_DOOR_26505) {

                if (player.tile().y > 5332) {
                    if(zamorak_kc < 10){
                        player.message("You need at least 10 zamorak forces kills to enter.");
                        return false;
                    }
                    player.putAttrib(AttributeKey.GWD_ZAMORAK_KC, zamorak_kc - 10);
                    player.teleport(2925, 5331, 2);
                } else if (player.tile().y == 5331) {
                    player.teleport(2925, 5333, 2);
                }
                return true;
            }



            int bandos_kc = player.getAttribOr(AttributeKey.GWD_BANDOS_KC, 0);

            // Bandos
            if (obj.getId() == BIG_DOOR_26503) {


                if (player.tile().x < 2863) {
                    if(bandos_kc < 10){
                        player.message("You need at least 10 bandos forces kills to enter.");
                        return false;
                    }
                    player.putAttrib(AttributeKey.GWD_BANDOS_KC, bandos_kc - 10);
                    player.teleport(2864, 5354, 2);
                } else if (player.tile().x == 2864) {
                    player.teleport(2862, 5354, 2);
                }
                return true;
            }
            int saradomin_kc = player.getAttribOr(AttributeKey.GWD_SARADOMIN_KC, 0);

            // Saradomin
            if (obj.getId() == BIG_DOOR_26504) {


                if (player.tile().x >= 2909) {
                    if(saradomin_kc < 10){
                        player.message("You need at least 10 saradomin forces kills to enter.");
                        return false;
                    }
                    player.putAttrib(AttributeKey.GWD_SARADOMIN_KC, saradomin_kc - 10);


                    player.teleport(2907, 5265, 0);
                } else if (player.tile().x == 2907) {
                    player.teleport(2909, 5265, 0);
                }
                return true;
            }
            int armadyl_kc = player.getAttribOr(AttributeKey.GWD_ARMADYL_KC, 0);

            // Armadyl
            if (obj.getId() == BIG_DOOR_26502) {

                if (player.tile().y <= 5294) {
                    if(armadyl_kc < 10){
                        player.message("You need at least 10 armadyl forces kills to enter.");
                        return false;
                    }
                    player.putAttrib(AttributeKey.GWD_SARADOMIN_KC, saradomin_kc - 10);


                    player.teleport(2839, 5296, 2);
                } else if (player.tile().y == 5296) {
                    player.teleport(2839, 5294, 2);
                }
                return true;
            }
            int zaros_kc = player.getAttribOr(AttributeKey.ZAROS_KC, 0);

            // Zaros
            if (obj.getId() == NEX_DOOR) {
                if (!World.getWorld().checkNPCSincombatinArea(Nex.getENCAMPMENT())) {
                    player.message("Nex is already fighting someone.");
                    return false;
                }


                if (player.tile().x <= 2909) {
                    if (zaros_kc < 10) {
                        player.message("You need at least 10 zaros forces kills to enter.");
                        return false;
                    }
                    player.startTime();

                    player.putAttrib(AttributeKey.ZAROS_KC, zaros_kc - 10);
                }
                    player.getMovementQueue().interpolate(player.tile().x<2909 ? new Tile(obj.tile().x+2,obj.tile().y) : new Tile(obj.tile().x-2,obj.tile().y), MovementQueue.StepType.FORCED_WALK);
if(player.tile().x>2909)
    player.endNexTime();

                return true;
            }
        }
        return false;
    }
}
