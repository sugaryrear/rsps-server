package com.ferox.game.content;

import com.ferox.game.content.syntax.impl.WikiSearchSyntax;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Utils;

import java.util.HashMap;
import java.util.Map;

public class ScratchCard {

    public ScratchCard(Player player) {
        this.player = player;
    }


    private final Player player;
public void open(){
    player.getInterfaceManager().open(89500);
       //clear button map
    buttonMap.clear();
 //  Item pvp_reward = Utils.randomElement(eco_rewards);
    for(int i = 89506; i < 89515; i++){
        Item pvp_reward = Utils.randomElement(eco_rewards);
//        if(buttonMap.containsValue(pvp_reward))
//            continue;
        buttonMap.put(i, pvp_reward);
       // player.message("button id: "+i+" and item: "+buttonMap.get(i).getId()+" ( "+buttonMap.get(i).name()+" )");
    }
  //  Item digit = buttonMap.get(button);
}

    private static final Item[] eco_rewards = {new Item(1621, 1), //Uncut emerald
        new Item(1623, 1), //Uncut sapphire
        new Item(995, 1000), //Coins
        new Item(359, 1), //Raw Tuna
        new Item(558, 25), //Mind runes
        new Item(1601, 1), //Diamond
        new Item(562, 40), //Chaos runes
        new Item(560, 30), //Death runes
        new Item(554, 30), //Fire runes
        new Item(4151, 1), //whip
        new Item(453, 1), //Coal
        new Item(440, 1), //Iron ore
        new Item(385, 1), //Shark
        new Item(1615, 1)}; //Dragonstone

    private final Map<Integer, Item> buttonMap = new HashMap<>();
public void receivereward(int button){
    Item item = buttonMap.get(button);
    player.getInterfaceManager().close();
    player.message("You receive a "+item.name()+"!");
    player.inventory().add(item.getId(),item.getAmount());
}
int STARTING_BUTTON = 89506;

    public boolean buttonActions(int button) {
        if (!buttonMap.isEmpty() &&
            button >= STARTING_BUTTON && button < STARTING_BUTTON + buttonMap.size()) {
            receivereward(button);

            return true;
        }
        return false;
    }
}
