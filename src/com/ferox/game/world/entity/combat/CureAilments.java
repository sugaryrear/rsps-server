package com.ferox.game.world.entity.combat;

import com.ferox.game.content.mechanics.Poison;
import com.ferox.game.world.entity.mob.player.InfectionType;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import static com.ferox.util.ItemIdentifiers.CRYSTAL_KEY;

public class CureAilments {

    private final Player c;

    public CureAilments(Player player) {
        this.c = player;
    }
    public enum antiPoisons {
        WS1(0, 2448, 181),
        WS2(1, 181, 183),
        WS3(2, 183, 185),
        WS4(3, 185, -1),
        WS5(4, 2446, 175),
        WS6(5, 175, 177),
        WS7(6, 177, 179),
        WS8(7, 179, -1);


        private final int priority;
        private final int itemId;
        private final int replaceWith;

        public int getItemId() {
            return itemId;
        }
        public int getReplacement() {
            return replaceWith;
        }
        public int getPriority() {
            return priority;
        }
        antiPoisons(int priority, int itemId, int replacementId) {
            this.priority = priority;
            this.itemId = itemId;
            this.replaceWith = replacementId;

        }

    }

    private static final Set<antiPoisons> ANTIPOISONS = Collections.unmodifiableSet(EnumSet.allOf(antiPoisons.class));


    public antiPoisons getBestantiPoison() {
        antiPoisons ap = null;
        for (antiPoisons antipoison : ANTIPOISONS) {
            if (c.inventory().contains(antipoison.itemId)) {
                if (ap == null || antipoison.priority > ap.priority) {
                    ap = antipoison;

                }
            }
        }
        return ap;
    }

    public enum antiVenoms {
        WS1(0, 5943, 5945),
        WS2(1, 5945, 5947),
        WS3(2, 5947, 5949),
        WS4(3, 5949, -1);


        private final int priority;
        private final int itemId;
        private final int replaceWith;

        public int getItemId() {
            return itemId;
        }
        public int getReplacement() {
            return replaceWith;
        }
        public int getPriority() {
            return priority;
        }
        antiVenoms(int priority, int itemId, int replacementId) {
            this.priority = priority;
            this.itemId = itemId;
            this.replaceWith = replacementId;

        }

    }
    private static final Set<antiVenoms> ANTIVENOMS = Collections.unmodifiableSet(EnumSet.allOf(antiVenoms.class));


    public antiVenoms getBestantiVenom(Player player) {
        antiVenoms ad = null;
        for (antiVenoms antivenom : ANTIVENOMS) {
            if (c.inventory().contains(antivenom.itemId)) {
                if (ad == null || antivenom.priority > ad.priority) {
                    ad = antivenom;

                }
            }
        }
        return ad;
    }

    public void handleCureAilments(){

        if(c.getInfection() == null){// Reset and then send poison after
            c.message("You do not have any ailments affecting you.");
            return;
        }
if(c.getInfection() == InfectionType.POISON_INFECTION){

            antiPoisons ap = getBestantiPoison();
            if (ap == null) {
                c.message("You do not have any available antipoisons in your inventory.");
                return;
            }

    c.inventory().replace(ap.getItemId(), ap.getReplacement(), true);

if(ap == antiPoisons.WS1 || ap == antiPoisons.WS2 || ap == antiPoisons.WS3 || ap == antiPoisons.WS4)
    Poison.cureAndImmune(c, 23);
else
    Poison.cureAndImmune(c, 6);
            c.message("You resolve your poison status.");

        }
        if(c.getInfection() == InfectionType.VENOM_INFECTION){

            antiVenoms ad = getBestantiVenom(c);
            if (ad == null) {
                c.message("You do not have any available antidotes in your inventory.");
                return;
            }

            c.inventory().replace(ad.getItemId(), ad.getReplacement(), true);

            Venom.cure(2, c);
            c.message("You resolve your venom status.");

        }
    }
}
