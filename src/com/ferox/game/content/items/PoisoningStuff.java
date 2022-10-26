package com.ferox.game.content.items;

import com.ferox.game.content.skill.impl.herblore.PestleAndMortar;
import com.ferox.game.task.Task;
import com.ferox.game.task.TaskManager;
import com.ferox.game.world.entity.mob.npc.pets.Pet;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.util.Utils;

import java.util.HashMap;
import java.util.Map;

import static com.ferox.util.ItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.PET_KREEARRA;

public class PoisoningStuff {
    private enum Poisonedstuff {
        UNICORN_HORN(1215, 5698),
        SUPERIOR_DRAGON_BONES(22124, 21975 );

        private final int before;
        private final int after;


        Poisonedstuff(int before, int after) {
            this.before = before;
            this.after = after;

        }

        static Map<Integer, Poisonedstuff> Poisonedstuffmap = new HashMap<>();

        static {
            for (Poisonedstuff poisonedstuff : Poisonedstuff.values()) {
                Poisonedstuffmap.put(poisonedstuff.before, poisonedstuff);
            }
        }
    }

    public static boolean poisonwep(Player player, Item use, Item with) {
        if (use.getId() == WEAPON_POISON_5940 || with.getId() == WEAPON_POISON_5940) {
            int before = use.getId() == WEAPON_POISON_5940 ? with.getId() : use.getId();
            Poisonedstuff c = Poisonedstuff.Poisonedstuffmap.get(before);
            if (c != null) {
                if ((use.getId() == c.before && with.getId() == WEAPON_POISON_5940) || (use.getId() == WEAPON_POISON_5940 && with.getId() == c.before)) {


                    if (!player.inventory().contains(c.before) || !player.inventory().contains(WEAPON_POISON_5940)) {
                        return false;
                    }

                    player.inventory().replace(new Item(c.before), new Item(c.after), true);

                    player.inventory().replace(new Item(WEAPON_POISON_5940), new Item(229), true);

                }

            }
            return true;
        }
        if (use.getId() == WEAPON_POISON_5940 || with.getId() == WEAPON_POISON_5940) {
            int before = use.getId() == WEAPON_POISON_5940 ? with.getId() : use.getId();
            Poisonedstuff c = Poisonedstuff.Poisonedstuffmap.get(before);
            if (c != null) {
                if ((use.getId() == c.before && with.getId() == WEAPON_POISON_5940) || (use.getId() == WEAPON_POISON_5940 && with.getId() == c.before)) {


                    if (!player.inventory().contains(c.before) || !player.inventory().contains(WEAPON_POISON_5940)) {
                        return false;
                    }

                    player.inventory().replace(new Item(c.before), new Item(c.after), true);

                    player.inventory().replace(new Item(WEAPON_POISON_5940), new Item(229), true);

                }

            }
            return true;
        }
//        if ((use.getId() == 9416 && with.getId() == 9142)) {
//            if(!player.inventory().contains(954)){
//                player.message("You need a mithril grapple tip, a rope, and 1 unfinished mithril bolt to do this.");
//                return false;
//            }
//            // if (player.inventory().count(9416) > 0 && player.inventory().count(9142) > 0) {
//
//            player.inventory().remove(new Item(9142));
//            player.inventory().remove(new Item(9416));
//            player.inventory().remove(new Item(954));
//
//            player.inventory().add(new Item(9419));
//            player.message("You create a mithril grapple.");
//            //  }
//            return true;
//        }
//        if ((use.getId() == 954 && use.getId() == 9418)) {
//            //     if (player.inventory().count(9418) > 0 && player.inventory().count(954) > 0) {
//
//            player.inventory().remove(new Item(9418));
//            player.inventory().remove(new Item(954));
//            player.inventory().add(new Item(9419));
//            player.message("You create a mithril grapple.");
//            //   }
//            return true;
//        }
        return false;
    }


}
