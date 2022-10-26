package com.ferox.game.world.entity.combat.magic;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;

import java.util.*;

import static com.ferox.util.CustomItemIdentifiers.RUNE_POUCH_I;
import static com.ferox.util.ItemIdentifiers.RUNE_POUCH;

public class SpellFilters {

    private Player player;
    public SpellFilters(Player player) {
        this.player = player;
    }

    public boolean filtercombatspells = false;
    public boolean filterteleportspells = false;
    public boolean filterutilityspells = false;

    public int[][] FILTER_LOCATIONS = {
        {0, 18  ,13}, {1,65  ,13}, {2,112  ,13}, {3,152  ,13},
        {4,18  ,41  }, {5,65  ,41 }, {6,112   ,41 }, {7, 152  ,41  },
        {8,18  ,69   }, {9,65  ,69  },{10,112   ,69   }, {11,152  ,69   },
        {12,18  ,97  }, {13,65  ,97   }, {14,112   ,97   }, {15,152  ,97  },
        {16,18  ,125   }, {17, 65  ,125  }, {18, 112 , 125   }, {19, 149 ,125  },
        {20,18  , 153   }, {21,65 , 153   }, {22, 112   , 153  }, {23, 152  , 153    },
        {24,18  , 181  }, {25,65  , 181   }, {26,112   ,181  }, {27,152  ,181   }

    };

    public static int[][] ANCIENTS_CHILDREN = { {17, 12939}, {24, 12987}, {9,12901}, {1,12861},//rush
        {20, 12963}, {28, 13011}, {13,12919}, {5,12881},//burst
        {18, 12951}, {26, 12999}, {11, 12911}, {3,12871}, //blitz
        {22,12975}, {30,13023}, {15,12929},{7,12891},//barrage
        {48,21741},{32,13035},{34,13045}, {36,13053}, {38,13061},{40,13069},{42,13079},{2,34674}, {44,13087},{46,13095},//the teleport spells
    };
    public static int[][] ANCIENTS_CHILDREN_IN_ORDER = { {48,21741},{17, 12939}, {24, 12987}, {32,13035},{9,12901}, {1,12861},
        {34,13045}, {20, 12963}, {28, 13011}, {36,13053},{13,12919}, {5,12881},
        {38,13061},	{18, 12951}, {26, 12999},	{40,13069}, {11, 12911}, {3,12871},
        {42,13079},{2,34674},{22,12975}, {30,13023}, {44,13087},{15,12929},{7,12891},
        {46,13095},
    };
    public static int[] COMBAT_SPELLS_ANCIENTS = {12861, 12929, 12901, 12871, 12881, 12891, 12911, 12919, 12939, 12951, 12963, 12975,
        12987, 12999, 13011, 13023};

    public static int[] TELEPORT_SPELLS_ANCIENTS = {21741, 13035, 13045, 13053, 13061, 13069, 13079, 13087, 13095, 34674};


    public void resetButtons(){

        player.getPacketSender().sendConfig(780, 1);//on
        player.getPacketSender().sendConfig(781, 1);//on
        player.getPacketSender().sendConfig(782, 1);//on - utility spells not used
        player.getPacketSender().sendConfig(783, 1);//on
        player.getPacketSender().sendConfig(784, 1);//on
    }
    public void handleShowCombatSpells(){//hides/shows combat spells (aka Show Teleports button - the 2nd one)
        clearSpellBookAncients();
        final int[] TELEPORTANCIENTS = new int[] { 48,32,34,36,38,40,42,44,46,2};

        if(filtercombatspells && filterteleportspells){//filtering both , but now u gotta show teleport spells again
            //sendFrame70(0,0,12855);
            for (int i = 0; i < TELEPORT_SPELLS_ANCIENTS.length; i++) {//next spells
                if(!filteredspells.contains(ANCIENTS_CHILDREN[i+16][0]))
                    filteredspells.add(ANCIENTS_CHILDREN[i+16][0]);
            }
            filtercombatspells = false;
            player.message("No longer hiding teleport spells.");
            player.getPacketSender().sendConfig(781, 1);//on
            showWhatWeFiltered();
            return;
        }



        if(filterteleportspells){//lets say you also click showteleport spells... so show all spells !
            clearSpellBookFilters();
            player.message("Hiding all combat spells and teleport spells.");
            filtercombatspells = true;
            player.getPacketSender().sendConfig(781, 0);//off
            showWhatWeFiltered();
            return;
        }

        if(!filtercombatspells) {
            if (filteredspells.size() == 0) {//nothing in array, just construct default list of combat spells
                for (int i = 0; i < COMBAT_SPELLS_ANCIENTS.length; i++) {
                    if(!filteredspells.contains(ANCIENTS_CHILDREN[i][0]))
                        filteredspells.add(ANCIENTS_CHILDREN[i][0]);
                }
            } else {
                for (int item : TELEPORTANCIENTS) {
                    if (filteredspells.contains(item)) {
                        filteredspells.remove(filteredspells.indexOf((item)));
                    }
                }
            }
            filtercombatspells = true;
            player.message("Hiding all teleport spells.");
            showWhatWeFiltered();
            player.getPacketSender().sendConfig(781, 0);//off
        } else {

            clearSpellBookAncients();
            clearSpellBookFilters();
            for (int i = 0; i < ANCIENTS_CHILDREN_IN_ORDER.length; i++) {
                filteredspells.add(ANCIENTS_CHILDREN_IN_ORDER[i][0]);
            }
            showWhatWeFiltered();
            filtercombatspells = false;

            player.message("No longer hiding all teleport spells.");
            player.getPacketSender().sendConfig(781, 1);//off
        }
    }

    public void showWhatWeFiltered() {
        for(int i = 0; i < filteredspells.size(); i++){
            player.message("spellfilters##"+(filteredspells.get(i) == 2 ? 838 : 12855)+"##"+(filteredspells.get(i) == 2 ? 2 : filteredspells.get(i))+"##"+FILTER_LOCATIONS[i][1]+"##"+FILTER_LOCATIONS[i][2]);
            //  sendFrame67((filteredspells.get(i) == 2 ? 838 : 12855), (filteredspells.get(i) == 2 ? 2 : filteredspells.get(i)),FILTER_LOCATIONS[i][1],FILTER_LOCATIONS[i][2]);
        }

      //  player.debugMessage("size: "+filteredspells.size());
        StringBuilder s = new StringBuilder();
        for (int random_values : filteredspells)
        {
            s.append(filteredspells.get(filteredspells.indexOf(random_values))+", ");
        }
       // player.debugMessage("list of values: "+s.toString());


    }
    /**
     * clears everything from the spellbook away from screen
     */
    public void clearSpellBookAncients() {
        for (int i = 0; i < ANCIENTS_CHILDREN.length; i++) {
            player.message("spellfilters##"+(ANCIENTS_CHILDREN[i][0] == 2 ? 838 : 12855)+"##"+(ANCIENTS_CHILDREN[i][0]== 2 ? 2 : ANCIENTS_CHILDREN[i][0])+"##5000##0");

            //  sendFrame67((ANCIENTS_CHILDREN[i][0] == 2 ? 838 : 12855), (ANCIENTS_CHILDREN[i][0] == 2 ? 2 : ANCIENTS_CHILDREN[i][0]),5000,0);
        }
    }
    public void clearSpellBookFilters(){
        filteredspells.clear();
    }
    public void clearSpellBookfiltersandReset(){
        filteredspells.clear();
        filtercombatspells = false;
        filterteleportspells = false;
        filterrunesfor = false;
        filtermagiclevelfor = false;
        resetButtons();
        for (int i = 0; i < ANCIENTS_CHILDREN_IN_ORDER.length; i++) {
            filteredspells.add(ANCIENTS_CHILDREN_IN_ORDER[i][0]);
        }
    }
    public void handleShowUtilitySpells() {
        if(!filterutilityspells){
            player.message("No longer showing utility spells.");
            player.getPacketSender().sendConfig(782, 0);//off
            filterutilityspells = true;
        } else {
            player.message("Showing utility spells.");
            filterutilityspells = false;
            player.getPacketSender().sendConfig(782, 1);//on
        }
    }
    public void handleShowTeleportSpells() {
        clearSpellBookAncients();
        final int[] COMBATANCIENTS = new int[]{17, 24, 9, 1, 20, 28, 13, 5, 18, 26, 11, 3, 22, 30, 15, 7};



        if(filtercombatspells){//lets say you also click showteleport spells... so show all spells !
            //sendFrame70(0,0,12855);
            clearSpellBookAncients();
            player.message("Hiding all combat spells and teleport spells.");
//			   player.getPacketSender().sendConfig(780, 1);//off
//			   player.getPacketSender().sendConfig(781, 1);//off
            showWhatWeFiltered();
            return;
        }
        if(!filterteleportspells) {
            if (filteredspells.size() == 0/* || !filterrunesfor || !filtermagiclevelfor*/) {
//		if(!filterrunesfor || !filtermagiclevelfor)
//			filteredspells.clear();

                for (int i = 0; i < TELEPORT_SPELLS_ANCIENTS.length; i++) {
                    filteredspells.add(ANCIENTS_CHILDREN[i + 16][0]);
                }
            } else {
                for (int item : COMBATANCIENTS) {
                    if (filteredspells.contains(item)) {
                        filteredspells.remove(filteredspells.indexOf((item)));
                    }
                }

            }
            player.getPacketSender().sendConfig(780, 0);//off
            filterteleportspells = true;
            player.message("Hiding all combat spells.");
            showWhatWeFiltered();
            return;
        } else {

            clearSpellBookAncients();
            clearSpellBookFilters();
            for (int i = 0; i < ANCIENTS_CHILDREN_IN_ORDER.length; i++) {
                filteredspells.add(ANCIENTS_CHILDREN_IN_ORDER[i][0]);
            }
            showWhatWeFiltered();
            filterteleportspells = false;
            player.message("No longer hiding all combat spells.");
            player.getPacketSender().sendConfig(780, 1);//on
        }
    }


    public void handleShowSpellsYouHaveMagicLevelFor() {
        clearSpellBookAncients();
        checkWhatSpellsYouHaveMagicLevelFor();
        showWhatWeFiltered();

    }
    public void handleShowSpellsYouHaveRunesFor() {
        clearSpellBookAncients();
        checkWhatSpellsYouHaveRunesFor();
        showWhatWeFiltered();

    }
    /**
     * the spells which appear in the interface
     */
    private ArrayList<Integer> filteredspells = new ArrayList<>();
    public boolean doWeHaveMagicLevelForAllSpells(int childid) {
        switch (childid) {
            case 17:
                if (player.skills().level(Skills.MAGIC) < 50) { //{//smoke rush
                    return false;
                } else {
                    return true;
                }

            case 24:
                if (player.skills().level(Skills.MAGIC) < 52) { //shadowrush
                    return false;
                } else {
                    return true;
                }
                //  break;
            case 32:
                if (player.skills().level(Skills.MAGIC) < 56) { //paddewwa
                    return false;
                } else {
                    return true;
                }
            case 9:
                if (player.skills().level(Skills.MAGIC) < 56) //shadowrush
                {return false;
                } else {
                    return true;
                }
                //  break;

            case 1:
                if (player.skills().level(Skills.MAGIC) < 58) //ice rush
                {
                    return false;
                } else {
                    return true;
                }
                // break;
            case 34:
                if (player.skills().level(Skills.MAGIC) < 60) { //senntisten
                    return false;
                } else {
                    return true;
                }
            case 20:
                if (player.skills().level(Skills.MAGIC) < 62) //smoke burst
                {
                    return false;
                } else {
                    return true;
                }
                //break;

            case 28:
                if (player.skills().level(Skills.MAGIC) < 64) //shadow burst
                {
                    return false;
                } else {
                    return true;
                }
                //break;
            case 36:
                if (player.skills().level(Skills.MAGIC) < 66) { //kharyll
                    return false;
                } else {
                    return true;
                }
            case 13:
                if (player.skills().level(Skills.MAGIC) < 68) //blood burst
                {
                    return false;
                } else {
                    return true;
                }
                // break;
            case 5:
                if (player.skills().level(Skills.MAGIC) < 70) //ice burst
                {
                    return false;
                } else {
                    return true;
                }
                //break;
            case 38:
                if (player.skills().level(Skills.MAGIC) < 72) { //lassar
                    return false;
                } else {
                    return true;
                }
            case 18:
                if (player.skills().level(Skills.MAGIC) < 74)//smoke blitz
                {
                    return false;
                } else {
                    return true;
                }
                //break;

            case 26:
                if (player.skills().level(Skills.MAGIC) < 76)//shadow blitz
                {
                    return false;
                } else {
                    return true;
                }
                // break;
            case 40:
                if (player.skills().level(Skills.MAGIC) < 78) { //dareeyak
                    return false;
                } else {
                    return true;
                }
            case 11:
                if (player.skills().level(Skills.MAGIC) < 80)//blood blitz
                {
                    return false;
                } else {
                    return true;
                }
                // break;
            case 3:
                if (player.skills().level(Skills.MAGIC) < 82)//ice blitz
                {
                    return false;
                } else {
                    return true;
                }
                // break;

            case 42:
                if (player.skills().level(Skills.MAGIC) < 84) { //carrallangar
                    return false;
                } else {
                    return true;
                }
            case 2:
                if (player.skills().level(Skills.MAGIC) < 86) { //teleport to target
                    return false;
                } else {
                    return true;
                }

            case 22:
                if (player.skills().level(Skills.MAGIC) < 86) //smoke barrage
                {
                    return false;
                } else {
                    return true;
                }
                //   break;

            case 30:
                if (player.skills().level(Skills.MAGIC) < 88) //shadow barrage
                {
                    return false;
                } else {
                    return true;
                }
                //   break;

            case 44:
                if (player.skills().level(Skills.MAGIC) < 90) { //annakarl
                    return false;
                } else {
                    return true;
                }
            case 15:
                if (player.skills().level(Skills.MAGIC) < 92) //blood barrage
                {
                    return false;
                } else {
                    return true;
                }
                //  break;

            case 7:
                if (player.skills().level(Skills.MAGIC) < 94) //ice barrage
                {
                    return false;
                } else {
                    return true;
                }
            case 46:
                if (player.skills().level(Skills.MAGIC) < 96) { //ghorrock
                    return false;
                } else {
                    return true;
                }
        }
        return false;
    }
    public boolean doWeHaveMagicLevelForCombatSpells(int childid){
        switch(childid) {
            case 17:
                if (player.skills().level(Skills.MAGIC) < 50) { //{//smoke rush
                    return false;
                } else {
                    return true;
                }

            case 24:
                if (player.skills().level(Skills.MAGIC) < 52) { //shadowrush
                    return false;
                } else {
                    return true;
                }
                //  break;
            case 9:
                if (player.skills().level(Skills.MAGIC) < 56) //shadowrush
                {return false;
                } else {
                    return true;
                }
                //  break;

            case 1:
                if (player.skills().level(Skills.MAGIC) < 58) //ice rush
                {
                    return false;
                } else {
                    return true;
                }
                // break;

            case 20:
                if (player.skills().level(Skills.MAGIC) < 62) //smoke burst
                {
                    return false;
                } else {
                    return true;
                }
                //break;

            case 28:
                if (player.skills().level(Skills.MAGIC) < 64) //shadow burst
                {
                    return false;
                } else {
                    return true;
                }
                //break;

            case 13:
                if (player.skills().level(Skills.MAGIC) < 68) //blood burst
                {
                    return false;
                } else {
                    return true;
                }
                // break;
            case 5:
                if (player.skills().level(Skills.MAGIC) < 70) //ice burst
                {
                    return false;
                } else {
                    return true;
                }
                //break;
            case 18:
                if (player.skills().level(Skills.MAGIC) < 74)//smoke blitz
                {
                    return false;
                } else {
                    return true;
                }
                //break;

            case 26:
                if (player.skills().level(Skills.MAGIC) < 76)//shadow blitz
                {
                    return false;
                } else {
                    return true;
                }
                // break;

            case 11:
                if (player.skills().level(Skills.MAGIC) < 80)//blood blitz
                {
                    return false;
                } else {
                    return true;
                }
                // break;
            case 3:
                if (player.skills().level(Skills.MAGIC) < 82)//ice blitz
                {
                    return false;
                } else {
                    return true;
                }
                // break;

            case 22:
                if (player.skills().level(Skills.MAGIC) < 86) //smoke barrage
                {
                    return false;
                } else {
                    return true;
                }
                //   break;

            case 30:
                if (player.skills().level(Skills.MAGIC) < 88) //shadow barrage
                {
                    return false;
                } else {
                    return true;
                }
                //   break;
            case 15:
                if (player.skills().level(Skills.MAGIC) < 92) //blood barrage
                {
                    return false;
                } else {
                    return true;
                }
                //  break;

            case 7:
                if (player.skills().level(Skills.MAGIC) < 94) //ice barrage
                {
                    return false;
                } else {
                    return true;
                }
                //  break;



        }
        return false;
    }
    public boolean doWeHaveMagicLevelForTeleportSpells(int childid){
        switch(childid) {
            case 48:
                return true;
            case 32:
                if (player.skills().level(Skills.MAGIC) < 56) { //paddewwa
                    return false;
                } else {
                    return true;
                }
            case 34:
                if (player.skills().level(Skills.MAGIC) < 60) { //senntisten
                    return false;
                } else {
                    return true;
                }
            case 36:
                if (player.skills().level(Skills.MAGIC) < 66) { //kharyll
                    return false;
                } else {
                    return true;
                }

            case 38:
                if (player.skills().level(Skills.MAGIC) < 72) { //lassar
                    return false;
                } else {
                    return true;
                }

            case 40:
                if (player.skills().level(Skills.MAGIC) < 78) { //dareeyak
                    return false;
                } else {
                    return true;
                }

            case 42:
                if (player.skills().level(Skills.MAGIC) < 84) { //carrallangar
                    return false;
                } else {
                    return true;
                }

            case 2:
                if (player.skills().level(Skills.MAGIC) < 86) { //teleport to target
                    return false;
                } else {
                    return true;
                }

            case 44:
                if (player.skills().level(Skills.MAGIC) < 90) { //annakarl
                    return false;
                } else {
                    return true;
                }
            case 46:
                if (player.skills().level(Skills.MAGIC) < 96) { //ghorrock
                    return false;
                } else {
                    return true;
                }
        }
        return false;
    }
    public boolean doWeHaveRunesForCombatSpells(int childid){

        switch(childid) {
            case 17:
                CombatSpell smokerush = CombatSpells.getCombatSpellByName("Smoke rush");

                if (!checkRunesReq(player,smokerush)) {//smoke rush
                    return false;
                } else {
                    return true;
                }

            case 24:
                CombatSpell shadowrush = CombatSpells.getCombatSpellByName("Shadow rush");

                if (!checkRunesReq(player,shadowrush)) {//shadow rush
                    return false;
                } else {
                    return true;
                }
                //  break;
            case 9:
                CombatSpell bloodrush = CombatSpells.getCombatSpellByName("Blood rush");

                if (!checkRunesReq(player,bloodrush)) {//blood rush
                    return false;
                } else {
                    return true;
                }
                //  break;

            case 1:
                CombatSpell icerush = CombatSpells.getCombatSpellByName("Ice rush");

                if (!checkRunesReq(player,icerush)) {//ice rush
                    return false;
                } else {
                    return true;
                }
                // break;

            case 20:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Smoke burst"))) {//smoke burst
                    return false;
                } else {
                    return true;
                }
                //break;

            case 28:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Shadow burst"))){ //shadow burst
                    return false;
                } else {
                    return true;
                }
                //break;

            case 13:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Blood burst"))){//blood burst
                    return false;
                } else {
                    return true;
                }
                // break;
            case 5:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Ice burst"))){//ice burst
                    return false;
                } else {
                    return true;
                }
                //break;
            case 18:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Smoke blitz"))){//smoke blitz
                    return false;
                } else {
                    return true;
                }
                //break;

            case 26:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Shadow blitz"))){//shadow blitz
                    return false;
                } else {
                    return true;
                }
                // break;

            case 11:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Blood blitz"))){//blood blitz
                    return false;
                } else {
                    return true;
                }
                // break;
            case 3:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Ice blitz"))){//ice blitz
                    return false;
                } else {
                    return true;
                }
                // break;

            case 22:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Smoke barrage"))){//smoke barrage
                    return false;
                } else {
                    return true;
                }
                //   break;

            case 30:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Shadow barrage"))){//shadow barrage

                    return false;
                } else {
                    return true;
                }
                //   break;
            case 15:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Blood barrage"))){//blood barrage
                    return false;
                } else {
                    return true;
                }
                //  break;

            case 7:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Ice barrage"))){//ice barrage
                    return false;
                } else {
                    return true;
                }
                //  break;



        }
        return false;
    }
    public boolean doWeHaveRunesForAllSpells(int childid) {
        switch(childid) {
            case 48:
                return true;
            case 17:
                CombatSpell smokerush = CombatSpells.getCombatSpellByName("Smoke rush");

                if (!checkRunesReq(player,smokerush)) {//smoke rush
                    return false;
                } else {
                    return true;
                }

            case 24:
                CombatSpell shadowrush = CombatSpells.getCombatSpellByName("Shadow rush");

                if (!checkRunesReq(player,shadowrush)) {//shadow rush
                    return false;
                } else {
                    return true;
                }
                //  break;
            case 32:
                if (!checkRunesReq(player, MagicClickSpells.getMagicSpellByName("Paddewwa"))) {//paddewwa
                    return false;
                } else {
                    return true;
                }
            case 9:
                CombatSpell bloodrush = CombatSpells.getCombatSpellByName("Blood rush");

                if (!checkRunesReq(player,bloodrush)) {//blood rush
                    return false;
                } else {
                    return true;
                }
                //  break;

            case 1:
                CombatSpell icerush = CombatSpells.getCombatSpellByName("Ice rush");

                if (!checkRunesReq(player,icerush)) {//ice rush
                    return false;
                } else {
                    return true;
                }
                // break;

            case 34:
                if (!checkRunesReq(player, MagicClickSpells.getMagicSpellByName("Senntisten"))) {//senntisten
                    return false;
                } else {
                    return true;
                }
            case 20:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Smoke burst"))) {//smoke burst
                    return false;
                } else {
                    return true;
                }
                //break;

            case 28:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Shadow burst"))){ //shadow burst
                    return false;
                } else {
                    return true;
                }
                //break;

            case 36:
                if (!checkRunesReq(player, MagicClickSpells.getMagicSpellByName("Kharyll"))) {//kharyll
                    return false;
                } else {
                    return true;
                }
            case 13:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Blood burst"))){//blood burst
                    return false;
                } else {
                    return true;
                }
                // break;
            case 5:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Ice burst"))){//ice burst
                    return false;
                } else {
                    return true;
                }
                //break;

            case 38:
                if (!checkRunesReq(player, MagicClickSpells.getMagicSpellByName("Lassar"))) {//lassar
                    return false;
                } else {
                    return true;
                }
            case 18:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Smoke blitz"))){//smoke blitz
                    return false;
                } else {
                    return true;
                }
                //break;

            case 26:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Shadow blitz"))){//shadow blitz
                    return false;
                } else {
                    return true;
                }
                // break;
            case 40:
                if (!checkRunesReq(player, MagicClickSpells.getMagicSpellByName("Dareeyak"))) {//dareeyak
                    return false;
                } else {
                    return true;
                }
            case 11:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Blood blitz"))){//blood blitz
                    return false;
                } else {
                    return true;
                }
                // break;
            case 3:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Ice blitz"))){//ice blitz
                    return false;
                } else {
                    return true;
                }
                // break;
            case 42:
                if (!checkRunesReq(player, MagicClickSpells.getMagicSpellByName("Carrallangar"))) {//carrallangar
                    return false;
                } else {
                    return true;
                }
            case 2:
                if (!checkRunesReq(player, MagicClickSpells.getMagicSpellByName("Teleport to target"))) {//teleport to target
                    return false;
                } else {
                    return true;
                }
            case 22:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Smoke barrage"))){//smoke barrage
                    return false;
                } else {
                    return true;
                }
                //   break;

            case 30:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Shadow barrage"))){//shadow barrage

                    return false;
                } else {
                    return true;
                }
                //   break;
            case 44:
                if (!checkRunesReq(player, MagicClickSpells.getMagicSpellByName("Annakarl"))) {//annakarl
                    return false;
                } else {
                    return true;
                }
            case 15:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Blood barrage"))){//blood barrage
                    return false;
                } else {
                    return true;
                }
                //  break;

            case 7:
                if (!checkRunesReq(player,CombatSpells.getCombatSpellByName("Ice barrage"))){//ice barrage
                    return false;
                } else {
                    return true;
                }



            case 46:
                if (!checkRunesReq(player, MagicClickSpells.getMagicSpellByName("Ghorrock"))) {//ghorrock
                    return false;
                } else {
                    return true;
                }
        }
        return false;

    }
    public boolean doWeHaveRunesForTeleportSpells(int childid){
        switch(childid) {
            case 48:
                return true;
            case 32:
                if (!checkRunesReq(player, MagicClickSpells.getMagicSpellByName("Paddewwa"))) {//paddewwa
                    return false;
                } else {
                    return true;
                }
            case 34:
                if (!checkRunesReq(player, MagicClickSpells.getMagicSpellByName("Senntisten"))) {//senntisten
                    return false;
                } else {
                    return true;
                }
            case 36:
                if (!checkRunesReq(player, MagicClickSpells.getMagicSpellByName("Kharyll"))) {//kharyll
                    return false;
                } else {
                    return true;
                }
            case 38:
                if (!checkRunesReq(player, MagicClickSpells.getMagicSpellByName("Lassar"))) {//lassar
                    return false;
                } else {
                    return true;
                }
            case 40:
                if (!checkRunesReq(player, MagicClickSpells.getMagicSpellByName("Dareeyak"))) {//dareeyak
                    return false;
                } else {
                    return true;
                }
            case 42:
                if (!checkRunesReq(player, MagicClickSpells.getMagicSpellByName("Carrallangar"))) {//carrallangar
                    return false;
                } else {
                    return true;
                }
            case 2:
                if (!checkRunesReq(player, MagicClickSpells.getMagicSpellByName("Teleport to target"))) {//teleport to target
                    return false;
                } else {
                    return true;
                }
            case 44:
                if (!checkRunesReq(player, MagicClickSpells.getMagicSpellByName("Annakarl"))) {//annakarl
                    return false;
                } else {
                    return true;
                }
            case 46:
                if (!checkRunesReq(player, MagicClickSpells.getMagicSpellByName("Ghorrock"))) {//ghorrock
                    return false;
                } else {
                    return true;
                }

        }
//        switch(childid) {
//            case 48:
//                return true;
//            case 32:
//                if (!c.getCombat().checkJustRunes(70)) {//paddewwa
//                    return false;
//                } else {
//                    return true;
//                }
//            case 34:
//                if (!c.getCombat().checkJustRunes(71)) {//senntisten
//                    return false;
//                } else {
//                    return true;
//                }
//            case 36:
//                if (!c.getCombat().checkJustRunes(72)) {//kharyll
//                    return false;
//                } else {
//                    return true;
//                }
//
//            case 38:
//                if (!c.getCombat().checkJustRunes(73)) {//lassar
//                    return false;
//                } else {
//                    return true;
//                }
//
//            case 40:
//                if (!c.getCombat().checkJustRunes(74)) {//dareeyak
//                    return false;
//                } else {
//                    return true;
//                }
//
//            case 42:
//                if (!c.getCombat().checkJustRunes(75)) {//carrallangar
//                    return false;
//                } else {
//                    return true;
//                }
//
//            case 2:
//                if (!c.getCombat().checkJustRunes(106)) {//teleport to target
//                    return false;
//                } else {
//                    return true;
//                }
//
//            case 44:
//                if (!c.getCombat().checkJustRunes(76)) {//annakarl
//                    return false;
//                } else {
//                    return true;
//                }
//            case 46:
//                if (!c.getCombat().checkJustRunes(77)) {//ghorrock
//                    return false;
//                } else {
//                    return true;
//                }
//
//
//
//        }
        return false;
    }
    public boolean filtermagiclevelfor = false;
    public boolean filterrunesfor = false;


    /**
     * add all spells to filter on login
     */

    public void addAllSpellsToFilter() {
        resetButtons();
        filteredspells.clear();
        for (int i = 0; i < ANCIENTS_CHILDREN_IN_ORDER.length; i++) {
            filteredspells.add(ANCIENTS_CHILDREN_IN_ORDER[i][0]);
        }
        //player.message(filteredspells.size()+" ");
        showWhatWeFiltered();
    }

    public void checkWhatSpellsYouHaveRunesFor() {
        if(filterrunesfor && !filtercombatspells && !filterteleportspells){ //clicking it once again w/o combat and teleport filters
            clearSpellBookFilters();
            clearSpellBookAncients();
            for (int i = 0; i < ANCIENTS_CHILDREN_IN_ORDER.length; i++) {
                filteredspells.add(ANCIENTS_CHILDREN_IN_ORDER[i][0]);
            }


            player.getPacketSender().sendConfig(784, 1);//on
            filterrunesfor = false;
            player.message("Showing all spells that you also lack the runes for.");
            return;
        }
        if (filterrunesfor && filtercombatspells) {
            filterrunesfor = false;
            for (int i = 0; i < COMBAT_SPELLS_ANCIENTS.length; i++) {//next spells
                if(!filteredspells.contains(ANCIENTS_CHILDREN[i][0]))
                    filteredspells.add(ANCIENTS_CHILDREN[i][0]);
            }
            showWhatWeFiltered();

            player.message("No longer filtering only spells you have the runes for.");
            player.getPacketSender().sendConfig(784, 1);//on
            return;
        }

        if(!filterteleportspells && filtercombatspells) {
            for (Iterator<Integer> itr = filteredspells.iterator(); itr.hasNext();) {
                int childid = itr.next();
                if (!doWeHaveRunesForCombatSpells(childid)) {
                    itr.remove();
                }
            }
            player.getPacketSender().sendConfig(784, 0);//off
            player.message("Showing all combat spells for which you have runes for.");
            filterrunesfor = true;
            return;
        }
        if (filterrunesfor && filterteleportspells) {
            filterrunesfor = false;
            for (int i = 0; i < TELEPORT_SPELLS_ANCIENTS.length; i++) {//next spells
                if(!filteredspells.contains(ANCIENTS_CHILDREN[i+16][0]))
                    filteredspells.add(ANCIENTS_CHILDREN[i+16][0]);
            }
            showWhatWeFiltered();

            player.message("No longer filtering only spells you have the runes for.");
            player.getPacketSender().sendConfig(784, 1);//on
            return;
        }

        if(filterteleportspells && !filtercombatspells) {
            for (Iterator<Integer> itr = filteredspells.iterator(); itr.hasNext();) {
                int childid = itr.next();

                if (!doWeHaveRunesForTeleportSpells(childid)) {
                    itr.remove();
                }
            }
            player.getPacketSender().sendConfig(784, 0);//off
            player.message("Showing all teleport spells for which you have runes for.");
            filterrunesfor = true;
            return;
        }
        if(!filterteleportspells && !filtercombatspells) {

            Iterator<Integer> iter = filteredspells.iterator();
            while(iter.hasNext()) {
                int blah = iter.next();
                if (!doWeHaveRunesForAllSpells(blah)) {
                    iter.remove();
                }
            }
            player.getPacketSender().sendConfig(784, 0);//off
            player.message("Showing all spells for which you have the runes for.");
            filterrunesfor = true;
            return;
        }
        if(filterteleportspells && filtercombatspells) {

            Iterator<Integer> iter = filteredspells.iterator();
            while(iter.hasNext()) {
                int blah = iter.next();
                if (!doWeHaveRunesForAllSpells(blah)) {
                    iter.remove();
                }
            }

            player.getPacketSender().sendConfig(784, 0);//off
            player.message("Showing all spells for which you have the runes for.");
            filterrunesfor = true;
            return;
        }


    }

    public void checkWhatSpellsYouHaveMagicLevelFor(){

        if (filtermagiclevelfor && filtercombatspells) {
            filtermagiclevelfor = false;
            for (int i = 0; i < COMBAT_SPELLS_ANCIENTS.length; i++) {//first 16
                if(!filteredspells.contains(ANCIENTS_CHILDREN[i][0]))
                    filteredspells.add(ANCIENTS_CHILDREN[i][0]);
            }
            showWhatWeFiltered();
            //	handleShowCombatSpells();

            player.message("No longer filtering only spells you have the magic level for.");
            player.getPacketSender().sendConfig(783, 1);//on
            return;
        }

        if (filtermagiclevelfor && filterteleportspells) {
            filtermagiclevelfor = false;
            clearSpellBookFilters();
            for (int i = 0; i < TELEPORT_SPELLS_ANCIENTS.length; i++) {
                filteredspells.add(ANCIENTS_CHILDREN[i + 16][0]);
            }
            player.message("No longer only filtering teleport spells you have the magic level for.");
            player.getPacketSender().sendConfig(783, 1);//on

            return;
        }
        if(!filterteleportspells && filtercombatspells) {
            for (Iterator<Integer> itr = filteredspells.iterator(); itr.hasNext(); ) {
                int childid = itr.next();
                if (!doWeHaveMagicLevelForCombatSpells(childid)) {
                    itr.remove();
                }

            }
            player.getPacketSender().sendConfig(783, 0);//off
            player.message("Showing all combat spells you do have the magic level for.");
            filtermagiclevelfor = true;
            return;
        }
        if(filterteleportspells && !filtercombatspells) {//after clicking 'show' teleport spells
            for (Iterator<Integer> itr = filteredspells.iterator(); itr.hasNext(); ) {
                int childid = itr.next();
                if (!doWeHaveMagicLevelForTeleportSpells(childid)) {
                    itr.remove();
                }
            }
            player.getPacketSender().sendConfig(783, 0);//off
            player.message("Showing all teleport spells for which you have the magic level for.");
            filtermagiclevelfor = true;
            return;
        }
        if(filtermagiclevelfor && !filtercombatspells && !filterteleportspells){ //clicking it once again w/o combat and teleport filters
            //clearSpellBookFilters();
            clearSpellBookAncients();
            player.getPacketSender().sendConfig(783, 1);//on
            filtermagiclevelfor = false;

            clearSpellBookFilters();
            clearSpellBookAncients();
            for (int i = 0; i < ANCIENTS_CHILDREN_IN_ORDER.length; i++) {
                filteredspells.add(ANCIENTS_CHILDREN_IN_ORDER[i][0]);
            }
            player.message("Showing all spells you lack the magic level for.");
            return;
        }
        if(!filterteleportspells && !filtercombatspells) {
            Iterator<Integer> iter = filteredspells.iterator();
            while(iter.hasNext()) {
                int blah = iter.next();
                if (!doWeHaveMagicLevelForAllSpells(blah)) {
                    iter.remove();
                }
            }
            player.getPacketSender().sendConfig(783, 0);//off
            player.message("Showing all spells you have the magic level for.");
            filtermagiclevelfor = true;
            return;
        }

        if (filtermagiclevelfor && filtercombatspells) {
            filtermagiclevelfor = false;
            filtercombatspells = false;
            handleShowCombatSpells();
            player.message("No longer filtering spells you have the magic level for.");
            player.getPacketSender().sendConfig(783, 0);//off
            return;
        }


        if(!filtercombatspells && !filterteleportspells){//no combat spells filtered & no teleport spells filtered - show all spells u lack magic level for

            filteredspells.add(48);//home tele by default

            if (player.skills().level(Skills.MAGIC)  < 50) {//smoke rush
                filteredspells.add(17);

            }
            if (player.skills().level(Skills.MAGIC)  < 52) {//shadowrush
                filteredspells.add(24);

            }
            if(player.skills().level(Skills.MAGIC)  < 56){

                filteredspells.add(32);//paddewwa
                filteredspells.add(9);//blood rush
            }
            if(player.skills().level(Skills.MAGIC)  < 58) {//ice rush
                filteredspells.add(1);
            }
            if(player.skills().level(Skills.MAGIC)  < 60) {//senntisten
                filteredspells.add(34);
            }

            if(player.skills().level(Skills.MAGIC)  < 62) {//smoke burst

                filteredspells.add(20);
            }

            if(player.skills().level(Skills.MAGIC) < 64) {//shadow burst
                filteredspells.add(28);
            }
            if(player.skills().level(Skills.MAGIC)  < 66) {//kharyll
                filteredspells.add(36);
            }
            if(player.skills().level(Skills.MAGIC)  < 68) {//blood burst
                filteredspells.add(13);
            }
            if(player.skills().level(Skills.MAGIC) <70) {//ice burst
                filteredspells.add(5);
            }
            if(player.skills().level(Skills.MAGIC)  <72) {//lassar
                filteredspells.add(38);
            }
            if(player.skills().level(Skills.MAGIC) < 74) {//smoke blitz
                filteredspells.add(18);
            }
            if(player.skills().level(Skills.MAGIC)  <76) {//shadow blitz
                filteredspells.add(26);
            }
            if(player.skills().level(Skills.MAGIC) < 78) {//dareeyak
                filteredspells.add(40);
            }
            if(player.skills().level(Skills.MAGIC)  <80) {//blood blitz
                filteredspells.add(11);
            }
            if(player.skills().level(Skills.MAGIC)  < 82) {//ice blitz
                filteredspells.add(3);
            }
            if(player.skills().level(Skills.MAGIC)  <84) {//carrallangar
                filteredspells.add(42);
            }
            if(player.skills().level(Skills.MAGIC)  < 86) {//teleport to target
                filteredspells.add(2);
            }
            if(player.skills().level(Skills.MAGIC)  < 86) {//smoke barrage
                filteredspells.add(22);
            }
            if(player.skills().level(Skills.MAGIC)  < 88) {//shadow barrage
                filteredspells.add(30);
            }
            if(player.skills().level(Skills.MAGIC) < 90) {//annakarl
                filteredspells.add(44);
            }
            if(player.skills().level(Skills.MAGIC) < 92) {//blood barrage
                filteredspells.add(15);
            }
            if(player.skills().level(Skills.MAGIC)  <94) {//ice barrage
                filteredspells.add(7);
            }
            if(player.skills().level(Skills.MAGIC) < 96) {//ghorrock
                filteredspells.add(46);
            }
            filtermagiclevelfor = true;
            player.message("Only Showing spells you lack the magic level for.");
            player.getPacketSender().sendConfig(783, 0);//on
        } else if(filtercombatspells && !filterteleportspells) {//filter combat spells dont filter teleport spells - show combat spells you have magic level for
            for(Iterator<Integer> itr = filteredspells.iterator(); itr.hasNext();){
                int childid = itr.next();
                if (doWeHaveMagicLevelForCombatSpells(childid)) {
                    itr.remove();
                }
            }
            player.message("Showing combat spells you lack the magic level for.");
            filtermagiclevelfor = true;
            player.getPacketSender().sendConfig(783, 0);//off

        } else if(!filtercombatspells && filterteleportspells){
            for(Iterator<Integer> itr = filteredspells.iterator(); itr.hasNext();){
                int childid = itr.next();
                if (doWeHaveMagicLevelForTeleportSpells(childid)) {
                    itr.remove();
                }
            }
            player.message("Showing teleport spells you lack the magic level for.");
            filtermagiclevelfor = true;
            player.getPacketSender().sendConfig(783, 0);//off

        }

    }
    public boolean checkRunesReq(Player player, Spell spell) {
        try {

            // Then we check the items required.
            final var itemsRequired = spell.itemsRequired(player);


            if (!itemsRequired.isEmpty()) {
                List<Item> items = PlayerMagicStaff.suppressRunes(player, itemsRequired);

                Map<Integer, Integer> runeCosts = new HashMap<>();
                items.forEach(rune -> runeCosts.put(rune.getId(), rune.getAmount()));
                HashMap<Integer, Integer> comboRunes = new HashMap<>();
                CombinationRunes.COMBO_RUNES.keySet().forEach(r -> {
                    if (player.getRunePouch().containsId(r)) {
                        comboRunes.put(r, player.getRunePouch().getRuneAmount(r));
                    } else if (player.inventory().contains(r)) {
                        comboRunes.put(r, player.inventory().count(r));
                    }
                });

                // Check combo runes
                if (!comboRunes.isEmpty()) {
                    comboRunes.forEach((k, v) -> {
                        CombinationRunes.ComboRune comboRune = CombinationRunes.get(k);
                        comboRune.elements().forEach(element -> {
                            int remainingCost = runeCosts.getOrDefault(element, 0);
                            if (remainingCost > 0) {
                                runeCosts.put(element, remainingCost - 1);
                            }
                        });
                    });
                }

                //First check rune pouch
                for (Item item : items) {
                    final int runeId = item.getId();
                    if (player.getRunePouch().containsId(runeId) && (player.inventory().contains(RUNE_POUCH) || player.inventory().contains(RUNE_POUCH_I))) {
                        runeCosts.put(runeId, Math.max(0, runeCosts.get(runeId) - player.getRunePouch().getRuneAmount(runeId)));
                    } else {
                        runeCosts.put(runeId, Math.max(0, runeCosts.get(runeId) - player.inventory().count(runeId)));
                    }
                }



                // Now check if we have all of the runes.
                if (runeCosts.values().stream().mapToInt(cost -> cost).sum() > 0) {
                    // We don't, so we can't cast.
                    //player.message("You do not have the required runes to cast this spell.");
                    return false;
                }



            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
