package com.ferox.game.content.minigames.impl.fight_caves;

import com.ferox.game.content.areas.lumbridge.dialogue.Hans;
import com.ferox.game.content.bank_pin.dialogue.BankTellerDialogue;
import com.ferox.game.content.minigames.MinigameManager;
import com.ferox.game.content.minigames.impl.fight_caves.dialogue.TzHaarMejJalDialogue;
import com.ferox.game.content.skill.impl.slayer.SlayerConstants;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.NpcIdentifiers;

/**
 * Created by Kaleem on 19/08/2017.
 */
public class TzHaarCityPlugin extends PacketInteraction {

    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        int npcId = npc.id();

        if (npcId == NpcIdentifiers.TZHAARMEJJAL) {
            if (option == 1 || option == 2) {
                handleMejJal(player);
            }
            return true;
        }

        if (npcId == NpcIdentifiers.TZHAARKETZUH) {
            if(option == 1) {
                player.getDialogueManager().start(new BankTellerDialogue(), npc);
            } else if(option == 2) {
                player.getBank().open();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int type) {
        if (object.getId() == 11833) { //Fight caves entrance
//            if(!player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.TZTOK_JAD) && !player.getPlayerRights().isDeveloperOrGreater(player)) {
//                player.message("You need the Tztok Jad Slayer Reward unlocked before you can enter this cave.");
//                return true;
//            }
            player.getDialogueManager().start(new FightCaveDialogue());
            return true;
        } else if (object.getId() == 11834) { //Fight caves leaving
            if(player.getMinigame() != null) {
                player.getMinigame().end(player);
            } else {
                player.teleport(FightCavesMinigame.OUTSIDE);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean handleItemOnObject(Player player, Item item, GameObject object) {
        return super.handleItemOnObject(player, item, object);
    }

    private void handleMejJal(Player player) {
        player.getDialogueManager().start(new TzHaarMejJalDialogue());
    }
}
