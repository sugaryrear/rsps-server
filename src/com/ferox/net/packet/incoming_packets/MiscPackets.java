package com.ferox.net.packet.incoming_packets;

import com.ferox.game.GameConstants;
import com.ferox.game.content.areas.edgevile.dialogue.FishingTeleportDialogue;
import com.ferox.game.content.areas.edgevile.dialogue.SkillingAreaHuntingExpertDialogue;
import com.ferox.game.content.areas.edgevile.dialogue.WCTeleportDialogue;
import com.ferox.game.content.areas.edgevile.dialogue.WizardMizgogDialogue;
import com.ferox.game.content.teleport.TeleportType;
import com.ferox.game.content.teleport.Teleports;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.combat.CureAilments;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;
import com.ferox.util.Utils;
import com.ferox.util.timers.TimerKey;

public class MiscPackets implements PacketListener {

    public static final int OBJECT_EXAMINE = 10;
    public static final int MINIGAMES_TELEPORT = 20;
    public static final int SPELLBOOK_CLEAR = 40;

    @Override
    public void handleMessage(Player player, Packet packet) {
        int id = packet.readShort();//change this
        int slot = packet.readShortA();//and this
        int interfaceId = packet.readShortA();//and maybe this

       // System.out.println("id: "+id+" slot: "+slot);
        switch (id){

            case SPELLBOOK_CLEAR:
                player.getSpellFilters().clearSpellBookfiltersandReset();
                break;

            case OBJECT_EXAMINE:
                player.message("%s", World.getWorld().examineRepository().object(slot));
                break;

            case MINIGAMES_TELEPORT:
                if(player.locked()){
                    return;
                }
                int minigameteleportlength =  player.getAttribOr(AttributeKey.MINIGAME_LENGTH, 0);
                long timeleft2 = System.currentTimeMillis() - player.minigamesDelay;
                long realtimeleft2 = minigameteleportlength - timeleft2;

                if ((System.currentTimeMillis() - player.minigamesDelay < minigameteleportlength)) {
                    String s = Utils.howMuchTimeLeft(realtimeleft2);
                    s =  s+ "until you can teleport again.";
                    player.message(s);

                    return;
                }
                if (player.getTimers().has(TimerKey.COMBAT_LOGOUT)) {
                    player.message("You can't teleport until 10 seconds after the end of combat.");
                    return;
                }
                if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                    return;
                }

                Tile tile = null;

                switch(slot){

                    case 4:// skilling
                        switch(interfaceId) {
                            case 200://none selected
                                player.message("Select an option from the dropdown menu first.");
                                break;
                            case 60://gnome agility
                                tile = new Tile(2469,3435, 0);
                                break;
                            case 61://barbarian agility
                                tile = new Tile(2552,3561, 0);
                                break;
                            case 62://hunter
                                player.getDialogueManager().start(new SkillingAreaHuntingExpertDialogue());
                                return;
                            case 64://runecrafting
                                player.getDialogueManager().start(new WizardMizgogDialogue());
                                return;
                            case 63://woodcutting
                                player.getDialogueManager().start(new WCTeleportDialogue());
                                return;
                            case 65://Farming
                                tile = new Tile(3001,3376, 0);
                                break;
                            case 66://Fishing
                                player.getDialogueManager().start(new FishingTeleportDialogue());
                                return;
                        }
                        break;
                    case 0://training
                        switch(interfaceId) {
                            case 200://none selected
                                player.message("Select an option from the dropdown menu first.");
                                break;
                            case 19://rock crabs
                                tile = new Tile(2676,3714, 0);
                                //     c.getPA().startTeleport(2676, 3714, 0, "minigames", false);
                                break;
                            case 20://slayer tower
                                tile = new Tile(3428,3535, 0);
                                // c.getPA().startTeleport(3428,3535, 0, "minigames", false);
                                break;
                            case 21://chasm
                                //redo
                                //       c.getPA().startTeleport(1436,10094, 3, "minigames", false);
                                tile = new Tile(2021,4635, 0);
                                break;
                            case 22://desert bandits
                                // c.getPA().startTeleport(3164,3002, 0, "minigames", false);
                                tile = new Tile(3164,3002, 0);
                                break;
                            case 23://miths
                                tile = new Tile(2511,3516, 0);
                                //c.getPA().startTeleport(2511,3516, 0, "minigames", false);
                                break;
                            case 24://frem
                                tile = new Tile(2807,10002, 0);
                                // c.getPA().startTeleport(2807,10002, 0, "minigames", false);

                                break;

                            case 25://kourend
                                tile = new Tile(1666,10049, 0);
                                //  c.getPA().startTeleport(1666,10049, 0, "minigames", false);

                                break;

                            case 26://kuruulm
                                tile = new Tile(1311,3805, 0);
                                // c.getPA().startTeleport(1311,3805, 0, "minigames", false);
                                break;
                            case 27://kourend catacombs
                                tile = new Tile(1568,5065, 0);
                                //    c.getPA().startTeleport(1568,5065, 0, "minigames", false);
                                break;
//                            case 28://lithkriken vault
//                                if(c.combatLevel < 100) {
//                                    c.sendMessage("You need a combat level of 100 to enter Lithriken vault.");
//                                    return;
//                                }
                            //     tile = new Tile(1568,5065, 0);
                            //     c.getPA().startTeleport(1568,5065, 0, "minigames", false);
                            // break;
                            case 29://smoke dung
                                tile = new Tile(3310,2961, 0);
                                //       c.getPA().startTeleport(2404,9415, 0, "minigames", false);
                                break;

                            case 30://experiments
                                tile = new Tile(3567,9935, 0);
                                //  c.getPA().startTeleport(3577,9927, 0, "minigames", false);
                                break;
                            case 31://wyverns
                                tile = new Tile(3597,10292, 0);
                                // tile = new Tile(2468,4008, 0);
                                //c.getPA().startTeleport(2468,4008, 0, "minigames", false);
                                break;
                            case 32://skeletal wyverns
                                tile = new Tile(3056,9555, 0);
                                // c.getPA().startTeleport(3623,10219, 0, "minigames", false);
                                break;
                            case 33://cave horrors
                                tile = new Tile(3735,9399, 0);
                                //     c.getPA().startTeleport(3735,9399, 0, "minigames", false);
                                break;
                            case 47://demonic gorillas
                                if(player.isTavelry){
                                    tile = new Tile(2884, 9798,0);//tavelry
                                } else {
                                    tile = new Tile(2709,9564 ,0);//brimhaven
                                }

                                //  c.getPA().startTeleport(2139,5662, 0, "minigames", false);
                                break;
                            case 34://sand crabs
                                tile = new Tile(2709,9564 ,0);//brimhaven
                                //     c.getPA().startTeleport(1781,3463,0, "minigames", false);
                                break;
                            case 35://cave horrors
                                tile = new Tile(3747,9374, 0);
                                //   c.getPA().startTeleport(3747,9374, 0, "minigames", false);
                                break;
                            case 50://brimhaven dungeon
                                tile = new Tile(2709,9564, 0);
                                //   c.getPA().startTeleport(1571,3658, 0, "minigames", false);
                                break;
                        }
                        break;
                    case 2://minigames
                        switch(interfaceId) {
                            case 200://none selected
                                player.message("Select an option from the dropdown menu first.");
                                break;
                            case 0://barrows
                                tile = new Tile(3565,3313, 0);
                                //   c.getPA().startTeleport(3565, 3313, 0, "minigames", false);
                                break;
                            case 1://raids
                                tile = new Tile(1246,3562, 0);
                                // c.getPA().startTeleport(1254, 3571, 0, "minigames", false);
                                break;
                            case 2://duel arena
                                tile = new Tile(3365,3266, 0);
                                //  c.getPA().startTeleport(3365, 3266, 0, "minigames", false);
                                break;//
                            case 3://fight pits
                                tile = new Tile(2439,5171, 0);
                                // c.getPA().startTeleport(2399, 5178, 0, "minigames", false);
                                break;
                            case 4://pest control
//                                player.message("@red@disabled");
//                                return;
                                tile = new Tile(2658,2659, 0);
                                //   c.getPA().startTeleport(2658, 2661, 0, "minigames", false);
                                break;
                            case 5://mage bank
                                tile = new Tile(2564,3310, 0);
                                //c.getPA().startTeleport(2562, 3310, 0, "minigames", false);
                                break;
                            case 6://inferno
//                                if (!c.getItems().ownsItem(6570, 24223, 24224, 21285, 13329)) {
//                                    c.sendMessage("You must have either a fire cape or an infernal cape in your inventory or bank.");
//                                    return;
//                                }
                                tile = new Tile(2497,5113, 0);
                                // c.getPA().startTeleport(2497, 5113, 0, "minigames", false);
                                break;
                            case 7://shayzien
                                tile = new Tile(1461,3689, 0);
                                //  c.getPA().startTeleport(1461, 3689, 0, "minigames", false);
                                break;
                            case 8://warriors guild
                                tile = new Tile(2879,3546, 0);
                                //   c.getPA().startTeleport(2879, 3546, 0, "minigames", false);
                                break;
                            case 44://the gauntlet
                                // tile = new Tile(3226, 6117, 0);
                                player.message("@red@todo list");
                                return;
                            //     c.getPA().startTeleport(3228,6117, 0, "minigames", false);
                            // break;
                            case 45://theatre of blood
                                tile = new Tile(3669,3219, 0);
                                //c.getPA().startTeleport(3669,3219, 0, "minigames", false);
                                break;
                            case 51://flower poker
                                tile = new Tile(3045,3377, 0);
                                //c.getPA().startTeleport(3669,3219, 0, "minigames", false);
                                break;
                            case 52://nmz
                                tile = new Tile(2609,3115, 0);
                                //c.getPA().startTeleport(3669,3219, 0, "minigames", false);
                                break;

                        }
                        break;
                    case 3://wilderness
                        switch(interfaceId) {
                            case 200://none selected
                                player.message("Select an option from the dropdown menu first.");
                                break;
                            case 37://east drags
                                tile = new Tile(2967,3600, 0);
                                //   c.getPA().startTeleport(2976,3600, 0, "minigames", false);
                                break;

                            case 38://west drags
                                tile = new Tile(3350, 3666, 0);
                                //   c.getPA().startTeleport(3350,3666, 0, "minigames", false);
                                break;

                            case 39://chaos temple
                                tile = new Tile(2966,3820, 0);
                                //  c.getPA().startTeleport(2966,3820, 0, "minigames", false);
                                break;
                            case 40://rev cave
                                tile = new Tile(3076,3650, 0);
                                //  c.getPA().startTeleport(3076,3650, 0, "minigames", false);
                                break;
                            case 41:
                                tile = new Tile(3288,3868, 0);

                                break;
                                case 42://lms
                                tile = new Tile(3142,3634, 0);
                                // c.getPA().startTeleport(3151,3640, 0, "minigames", false);

                                break;
                            case 43://forgotten cemetary
                                tile = new Tile(3146,3678, 0);
                                //     c.getPA().startTeleport(3149,3671, 0, "minigames", false);
                                break;
                            case 48://fountain of rune
                                tile = new Tile(3348,3872, 0);
                                //  c.getPA().startTeleport(3348,3872, 0, "minigames", false);
                                break;

                        }
                    case 1://bosses
                        switch(interfaceId) {
                            case 200://none selected
                                player.message("Select an option from the dropdown menu first.");
                                break;
                            case 9://sire
                                //c.getPA().startTeleport(3039, 4768, 0, "minigames", false);
                                tile = new Tile(3103,4830,0);
                                break;
                            case 10://corp
                                //    c.getPA().startTeleport(2964, 4382, 2, "minigames", false);
                                tile = new Tile(2964,4382,2);
                                break;
                            case 11://cerb
//                                if (c.getSlayer().isCerberusRoute()) {
//                                    c.getPA().startTeleport(1310, 1242, 0, "minigames", false);
//                                } else {
//                                    c.sendMessage("You must unlock the route to cerberus from a slayer master first.");
//                                    return;
//                                }
                                tile = new Tile(1310,1242,0);
                                break;
                            case 12://dags
                                //    c.getPA().startTeleport(1913, 4367, 0, "minigames", false);
                                tile = new Tile(1913,4367,0);
                                break;
                            case 13://the nightmare
                                //c.getPA().startTeleport(3808,9716,1, "minigames", false);
                                //	c.getPA().startTeleport(2999, 3380, 0, "minigames", false);
                                // player.message("@red@todo list");
                                tile = new Tile(1171,9949,0);
                                break;
                            case 14://godwars

                                //  c.getPA().startTeleport(2912,3747, 0, "minigames", false);
                                //GlobalObjects.checkGWD();
                                //Server.getGlobalObjects().checkGWD(c);
                                tile = new Tile(2912,3747,0);

                                break;
                            case 15://kraken
                                //  c.getPA().startTeleport(2278, 10016, 0, "minigames", false);
                                tile = new Tile(2278,10016,0);
                                break;
                            case 16://vorkath
                                //  c.getPA().startTeleport(2272, 4050, 0, "minigames", false);
                                tile = new Tile(2272,4050,0);
                                break;
                            case 17://zulrah
                                //c.getPA().startTeleport(2202, 3056, 0, "minigames", false);
                                tile = new Tile(2202, 3056, 0);
                                break;
                            case 18://barrelchest
                                //   c.getPA().startTeleport(1243,3497, 0, "minigames", false);
                                tile = new Tile(3804,2844, 0);
                                break;
                            case 49://kalphite queen
                                tile = new Tile(3226,3109,0);
                                //          c.getPA().startTeleport(3226,3109, 0, "minigames", false);
                                break;
                            case 53://alchemical hydra
                                tile = new Tile(1354,10258, 0);
                                //c.getPA().startTeleport(3669,3219, 0, "minigames", false);
                                break;

                        }
                        break;
                }
                if(tile != null)
                    Teleports.minigameTeleport(player, tile);



                break;
        }
    }
}
