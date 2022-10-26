package com.ferox.game.content.skill.impl.slayer;

import com.ferox.GameServer;
import com.ferox.game.content.skill.impl.slayer.slayer_task.SlayerCreature;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.QuestTab;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.ground.GroundItem;
import com.ferox.game.world.items.ground.GroundItemHandler;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.util.Color;
import com.ferox.util.Utils;

import java.util.Arrays;
import java.util.List;

import static com.ferox.game.content.collection_logs.LogType.KEYS;
import static com.ferox.game.world.entity.AttributeKey.*;
import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.SUPER_ANTIFIRE_POTION4;

/**
 * This class represents the slayer key. The slayer key is a random drop during a slayer task.
 * The key has various rewards including armour, supplies and weaponry.
 * @author Patrick van Elderen | May, 18, 2021, 14:44
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class SlayerKey {

    private static final int RARE_ROLL = 35;
    private static final int UNCOMMON_ROLL = 10;

    private final Player player;

    public SlayerKey(Player player) {
        this.player = player;
    }

    public void drop(Npc npc) {
        var task_id = player.<Integer>getAttribOr(SLAYER_TASK_ID,0);
        var task = SlayerCreature.lookup(task_id);
        var roll = player.getPlayerRights().isDeveloperOrGreater(player) && !GameServer.properties().production ? 1 : 20;
        if (task != null && Slayer.creatureMatches(player, npc.id())) {
            if(World.getWorld().rollDie(roll,1)) {
                var keysReceived = player.<Integer>getAttribOr(SLAYER_KEYS_RECEIVED,0) + 1;
                player.putAttrib(SLAYER_KEYS_RECEIVED, keysReceived);
                player.getPacketSender().sendString(QuestTab.InfoTab.SLAYER_KEYS_RECEIVED.childId, QuestTab.InfoTab.INFO_TAB.get(QuestTab.InfoTab.SLAYER_KEYS_RECEIVED.childId).fetchLineData(player));

                player.inventory().addOrBank(new Item(SLAYER_KEY));
                player.message(Color.PURPLE.wrap("A slayer key appeared, you have now collected a total of "+keysReceived+ " slayer keys."));
            }
        }
    }

    public void open() {
        if(!player.inventory().contains(SLAYER_KEY)) {
            player.message("This chest wont budge, I need some sort of key.");
            return;
        }

        player.inventory().remove(SLAYER_KEY,1);
        Item reward = reward();
        KEYS.log(player, SLAYER_KEY, reward);
        player.inventory().addOrBank(reward);

        var sendWorldMessage = reward.getValue() >= 300_000;
        var amount = reward.getAmount();
        var plural = amount > 1 ? "x" + amount : "x1";
        if (sendWorldMessage && !player.getUsername().equalsIgnoreCase("Box test")) {
            String worldMessage = "<img=505><shad=0>[<col=" + Color.MEDRED.getColorValue() + ">Slayer Key</col>]</shad>:<col=AD800F> " + player.getUsername() + " received " + plural + " <shad=0>" + reward.name() + "</shad>!";
            World.getWorld().sendWorldMessage(worldMessage);
        }

        var keysUsed = player.<Integer>getAttribOr(SLAYER_KEYS_OPENED,0) + 1;
        player.putAttrib(SLAYER_KEYS_OPENED, keysUsed);
        player.message(Color.PURPLE.wrap("You have now opened "+Utils.formatNumber(keysUsed)+" slayer keys!"));
    }

    private Item reward() {
        if (Utils.rollDie(RARE_ROLL, 1)) {
            return Utils.randomElement(RARE);
        } else if (Utils.rollDie(UNCOMMON_ROLL, 1)) {
            return Utils.randomElement(UNCOMMON);
        } else {
            return Utils.randomElement(COMMON);
        }
    }

    private static final List<Item> COMMON = Arrays.asList(
        new Item(DIVINE_SUPER_COMBAT_POTION4 + 1, 10),
        new Item(STAMINA_POTION4 + 1, 10),
        new Item(ANTIVENOM4 + 1, 10),
        new Item(SUPER_ANTIFIRE_POTION4 + 1, 10),
        new Item(BLOOD_MONEY, World.getWorld().random(1_000, 2_000)),
        new Item(GRANITE_MAUL_24225, 1)
    );

    private static final List<Item> UNCOMMON = Arrays.asList(
        new Item(DONATOR_MYSTERY_BOX),
        new Item(DIVINE_SUPER_COMBAT_POTION4 + 1, 50),
        new Item(STAMINA_POTION4 + 1, 25),
        new Item(SUPER_ANTIFIRE_POTION4 + 1, 25),
        new Item(BLOOD_MONEY, World.getWorld().random(3_000, 5_000)

    ));

    private static final List<Item> RARE = Arrays.asList(
        new Item(DRAGON_CLAWS),
        new Item(BLOOD_MONEY, World.getWorld().random(10_000, 75_000))
    );
}
