package com.ferox.game.content.areas.alkharid;

import com.ferox.game.content.areas.edgevile.IronManTutor;
import com.ferox.game.content.packet_actions.interactions.objects.Ladders;
import com.ferox.game.content.tradingpost.TradingPost;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueManager;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.dialogue.Expression;
import com.ferox.game.world.entity.mob.movement.MovementQueue;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.route.StepType;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.ObjectIdentifiers;
import com.ferox.util.chainedwork.Chain;

import static com.ferox.util.ItemIdentifiers.SHANTAY_PASS;
import static com.ferox.util.NpcIdentifiers.*;
import static com.ferox.util.NpcIdentifiers.RANGED_COMBAT_TUTOR;
import static com.ferox.util.ObjectIdentifiers.*;

public class ShantayPass extends PacketInteraction {
    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            //System.out.println("fishing object " + obj.toString());
            if(obj.getId() == ObjectIdentifiers.SHANTAY_PASS) {
        passThrough(player);
                return true;
            }
        }
            if (option == 2) {
            }
            return false;
        }
    private void passThrough(Player player) {
        GameObject obj = player.getAttribOr(AttributeKey.INTERACTION_OBJECT, null);
        if (player.tile().y > 3116 && player.inventory().contains(new Item(SHANTAY_PASS,1))) {

        player.lock();

        player.inventory().remove(new Item(SHANTAY_PASS, 1), true);

            player.getMovementQueue().interpolate(player.tile().x, obj.tile().y-3, MovementQueue.StepType.FORCED_WALK);


            Chain.bound(player).name("ShantayPassTask").runFn(1, () -> {
            }).then(2, player::unlock);
        } else {
            player.lock();


            player.getMovementQueue().interpolate(player.tile().x, obj.tile().y+3, MovementQueue.StepType.FORCED_WALK);


            Chain.bound(player).name("ShantayPassTask").runFn(1, () -> {
            }).then(2, player::unlock);
        }

    }
    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if(option == 1) {
            if(npc.id() == SHANTAY) {
                World.getWorld().shop(50).open(player);
                return true;
            }

        }
        if(option == 2) {
            if(npc.id() == SHANTAY) {
                World.getWorld().shop(50).open(player);
                return true;
            }
        }
        if(option == 3) {
            if(npc.id() == SHANTAY) {
                if (!player.inventory().contains(new Item(995, 10))) {
                 player.message("You need 10gp to purchase a shantay pass.");
                } else {
                    player.getDialogueManager().start(new Dialogue() {
                        @Override
                        protected void start(Object... parameters) {
                            send(DialogueType.ITEM_STATEMENT, new Item(SHANTAY_PASS), "", "You purchase a shantay pass.");
                            player.inventory().remove(new Item(995, 10), true);
                            setPhase(0);
                        }

                        @Override
                        protected void next() {
                            if(isPhase(0)) {
                                stop();
                            }
                        }
                    });
                }
                return true;
            }
        }
        if(option == 4) {

        }
        return false;
    }
}
