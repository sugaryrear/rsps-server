package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.fs.NpcDefinition;
import com.ferox.fs.ObjectDefinition;
import com.ferox.game.GameConstants;
import com.ferox.game.content.*;
import com.ferox.game.content.areas.dungeons.taverley.cerberus.Portcullis;
import com.ferox.game.content.areas.wilderness.content.boss_event.BossEvent;
import com.ferox.game.content.areas.wilderness.content.boss_event.WildernessBossEvent;
import com.ferox.game.content.collection_logs.LogType;
import com.ferox.game.content.diary.ardougne.ArdougneDiaryEntry;
import com.ferox.game.content.item_forging.ItemForgingCategory;
import com.ferox.game.content.items.RingOfWealth;
import com.ferox.game.content.mechanics.Poison;
import com.ferox.game.content.mechanics.referrals.Referrals;
import com.ferox.game.content.minigames.impl.Barrows;
import com.ferox.game.content.new_players.Tutorial;
import com.ferox.game.content.skill.impl.hunter.Hunter;
import com.ferox.game.content.skill.impl.hunter.trap.impl.Chinchompas;
import com.ferox.game.content.teleport.TeleportType;
import com.ferox.game.content.teleport.Teleports;
import com.ferox.game.content.treasure.TreasureRewardCaskets;
import com.ferox.game.task.Task;
import com.ferox.game.task.TaskManager;
import com.ferox.game.task.impl.ForceMovementTask;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.Venom;
import com.ferox.game.world.entity.combat.magic.MagicClickSpells;
import com.ferox.game.world.entity.combat.magic.Spell;
import com.ferox.game.world.entity.combat.magic.spellfilters.SpellType;
import com.ferox.game.world.entity.combat.magic.spellfilters.Spells;
import com.ferox.game.world.entity.combat.method.impl.npcs.inferno.AncestralGlyph;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueManager;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.FaceDirection;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.ForceMovement;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.entity.mob.player.commands.CommandManager;
import com.ferox.game.world.entity.mob.player.save.PlayerSave;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.ground.GroundItem;
import com.ferox.game.world.items.ground.GroundItemHandler;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.MapObjects;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.region.RegionManager;
import com.ferox.game.world.route.Direction;
import com.ferox.net.packet.incoming_packets.MovementPacketListener;
import com.ferox.test.generic.ChainWorkTest;
import com.ferox.util.*;
import com.ferox.util.chainedwork.Chain;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.util.TriConsumer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static com.ferox.util.ItemIdentifiers.*;

public class LazyCommands {
    private static ArrayList<Spells> filteredspells = new ArrayList<>();
    public static void byLazy(String key, TriConsumer<Player,String,String[]> consumer) {
        CommandManager.commands.put(key, new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                consumer.accept(player, command, parts);
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
    }
    public static double easyDone(){
        return (2.0/5.0)*100.0D;
    }

    public static void init() {
        Map<String, Command> commands = CommandManager.commands;
        /*
         * Misc testing commands
         */
        commands.put("ztest", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.message("testing");
                //System.out.println("start");
                ChainWorkTest.test();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("cerbrandomroom", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {

                //System.out.println("start");
                Portcullis.randomroomtest(player);
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });

        commands.put("rstanim", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {

                player.resetAnimation();
                player.getCombat().reset();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("putlevels", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.skills().getPreviousLevels().put(0,player.skills().level(Skills.ATTACK));
                player.skills().getPreviousLevels().put(1,player.skills().level(Skills.DEFENCE));
                player.skills().getPreviousLevels().put(2,player.skills().level(Skills.STRENGTH));
                player.skills().getPreviousLevels().put(3,player.skills().level(Skills.HITPOINTS));
                player.skills().getPreviousLevels().put(4,player.skills().level(Skills.RANGED));
                player.skills().getPreviousLevels().put(5,player.skills().level(Skills.PRAYER));
                player.skills().getPreviousLevels().put(6,player.skills().level(Skills.MAGIC));
            player.message(player.skills().getPreviousLevels().size()+" ");
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("whatitemsingoodiebag", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.getPriceChecker().listofitems();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("givegoodiebag", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.getGoodieBag().disperse();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("opengoodiebag", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.getGoodieBag().open();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("openrandomcard", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.getScratchCard().open();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("openitemforging", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.getItemForgingTable().open(player, ItemForgingCategory.WEAPON);

            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("testcombatspells", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                filteredspells.clear();
              List<Spells> spellstolist =   Spells.getTypeOfSpell(SpellType.COMBAT);

              for(Spells spell : spellstolist){
                  filteredspells.add(spell);
                  player.message("Spell: "+spell.getSpell().name()+" and lvl req: "+spell.getSpell().levelRequired());
              }
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });

        commands.put("testcombatspellslvlfor", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {

                for (Iterator<Spells> itr = filteredspells.iterator(); itr.hasNext();) {
                    Spells childid = itr.next();
                    if (player.skills().level(Skills.MAGIC) < childid.getSpell().levelRequired()) {
                        itr.remove();
                    }
                }
                for(Spells spell : filteredspells){
                    player.message("Spell: "+spell.getSpell().name()+" and lvl req: "+spell.getSpell().levelRequired());
                }
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("testcombatspellsrunesfor", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {

                for (Iterator<Spells> itr = filteredspells.iterator(); itr.hasNext();) {
                    Spells childid = itr.next();
                    if (!childid.checkRunesReq(player,childid.getSpell())) {
                        itr.remove();
                    }
                }
                for(Spells spell : filteredspells){
                    player.message("Spell: "+spell.getSpell().name()+" and you have runes: "+spell.getSpell().itemsRequired(player).toString()+"");
                }
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("testdeposit", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                int id = Integer.parseInt(parts[1]);
                player.getDepositBox().deposit(0, id);
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("mapv", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                int id = Integer.parseInt(parts[1]);
                player.getPacketSender().changeMapVisibility(id);
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("intmove", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                int id = Integer.parseInt(parts[1]);
                int x = Integer.parseInt(parts[2]);
                int y = Integer.parseInt(parts[3]);
                player.getPacketSender().sendInterfaceComponentMoval(id,x,y);
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("camera", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                int id = Integer.parseInt(parts[1]);
                player.getPacketSender().changeMapVisibility(id);
                    player.getPacketSender().stillCamera(      Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), 0, Integer.parseInt(parts[4]));

            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("proj", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                new Projectile(player.tile(), new Tile(x,y), 0, 1482, 150, 20, 20, 10,1).sendProjectile();
//                World.getWorld().getNpcs().filter(Objects::nonNull).min(Comparator.comparingInt(o -> o.tile().distance(player.tile()))).ifPresent(n -> {
//                    n.forceChat("hit me");
//                    new Projectile(player, n, 1482, 65, 150, 20, 20, 1).sendProjectile();
//                });

              //  new Projectile(player.tile(),new Tile(x,y),0,143, 12,50, 50, 10, 50,10,10).sendProjectile();

            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("glyphtest", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                AncestralGlyph glyph = new AncestralGlyph(NpcIdentifiers.ANCESTRAL_GLYPH, new Tile(2496,5099));

                World.getWorld().registerNpc(glyph);
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("collectlog", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.getCollectionLog().open(LogType.BOSSES);
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("gotowildboss", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                if (WildernessBossEvent.getINSTANCE().getActiveNpc().isPresent() && WildernessBossEvent.currentSpawnPos != null) {
                    Tile tile = WildernessBossEvent.currentSpawnPos;
                    if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                        return;
                    }
                    Teleports.basicTeleport(player, tile);
                } else {
                    player.message("The world boss recently died and will respawn shortly.");
                }
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("pker1", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                World.getServerData().getTopPkers().update("test2", 3);
                World.getServerData().setTopPkers(World.getServerData().getTopPkers());
                World.getServerData().processQueue();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("pker2", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                World.getServerData().getTopPkers().update("sugary", 12);
                World.getServerData().setTopPkers(World.getServerData().getTopPkers());
                World.getServerData().processQueue();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("pker3", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                World.getServerData().getTopPkers().update("randomguy", 8);

            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("pker4", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                World.getServerData().getTopPkers().update("idiotguy", 1);

            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("pker5", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                World.getServerData().getTopPkers().update("durial321", 4);

            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("pkerweekly1", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                World.getServerData().getTopPkersWeekly().update(player.getUsername(), 24);
//                World.getServerData().setTopPkers(World.getServerData().getTopPkers());
//                World.getServerData().processQueue();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("addalltoleaderboard", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                World.getServerData().getTopBossPoints().update(player.getUsername(), 12);
                World.getServerData().getTopBossPointsWeekly().update(player.getUsername(), 12);
                World.getServerData().getTopBossPointsDaily().update(player.getUsername(), 12);

                World.getServerData().getTopPkers().update(player.getUsername(), 25);
                World.getServerData().getTopPkersWeekly().update(player.getUsername(), 25);
                World.getServerData().getTopPkersDaily().update(player.getUsername(), 25);

                World.getServerData().getTopXP().update(player.getUsername(), player.skills().getTotalExperience());
                World.getServerData().getTopXPWeekly().update(player.getUsername(), player.skills().getTotalExperience());
                World.getServerData().getTopXPDaily().update(player.getUsername(), player.skills().getTotalExperience());


//                World.getServerData().setTopPkers(World.getServerData().getTopPkers());
//                World.getServerData().processQueue();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("testfw", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                try(FileWriter fw = new FileWriter("data/weeklyrewardswinner/topxprewards.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {
                    out.println("winner: hi");

                } catch (IOException e) {
                    e.printStackTrace();
                }

                try(FileWriter fw = new FileWriter("data/weeklyrewardswinner/toppkersrewards.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {
                    out.println("winner: hi");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try(FileWriter fw = new FileWriter("data/weeklyrewardswinner/topbosspointsrewards.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {
                    out.println("winner: hi");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("pkerdaily1", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                World.getServerData().getTopPkersDaily().update(player.getUsername(), 11);
//                World.getServerData().setTopPkers(World.getServerData().getTopPkers());
//                World.getServerData().processQueue();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("bosspointsdaily1", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                World.getServerData().getTopBossPointsDaily().update(player.getUsername(), 11);
//                World.getServerData().setTopPkers(World.getServerData().getTopPkers());
//                World.getServerData().processQueue();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("xpdaily1", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                World.getServerData().getTopXPDaily().update(player.getUsername(), 11L);
                World.getServerData().getTopBossPointsDaily().update(player.getUsername(), 4);
                World.getServerData().getTopPkersDaily().update(player.getUsername(), 4);
//                World.getServerData().setTopPkers(World.getServerData().getTopPkers());
//                World.getServerData().processQueue();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("bosspointsweekly1", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                World.getServerData().getTopBossPointsWeekly().update(player.getUsername(), 4);
//                World.getServerData().setTopPkers(World.getServerData().getTopPkers());
//                World.getServerData().processQueue();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("bosspointsweekly2", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                World.getServerData().getTopBossPointsWeekly().update("durial321", 4);
//                World.getServerData().setTopPkers(World.getServerData().getTopPkers());
//                World.getServerData().processQueue();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("bossdaily1", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                World.getServerData().getTopBossPointsDaily().update(player.getUsername(), 15);
//                World.getServerData().setTopPkers(World.getServerData().getTopPkers());
//                World.getServerData().processQueue();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("pkersdaily1", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                World.getServerData().getTopPkersDaily().update(player.getUsername(), 15);
//                World.getServerData().setTopPkers(World.getServerData().getTopPkers());
//                World.getServerData().processQueue();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("pkersdaily2", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                World.getServerData().getTopPkersDaily().update("Sugary", 22);
//                World.getServerData().setTopPkers(World.getServerData().getTopPkers());
//                World.getServerData().processQueue();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("pkerweekly2", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                World.getServerData().getTopPkersWeekly().update("durial321", 1);
//                World.getServerData().setTopPkers(World.getServerData().getTopPkers());
//                World.getServerData().processQueue();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });

        commands.put("testhandout", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
             TopPkersWeeklyEvent.handleTopPkersRewards();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("resetdate", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                TopPkersWeeklyEvent.setdate();
                dailyRewardsHandout.setdate();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("testconfig", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                int id = Integer.parseInt(parts[1]);
                int state = Integer.parseInt(parts[2]);
                player.getPacketSender().sendConfig(id, state);
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("opentele", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.setCurrentTabIndex(1);
                player.getTeleportInterface().displayFavorites();
                player.message("resetsidebars##");
                player.getInterfaceManager().open(29050);

            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });

        commands.put("pkerlist", new Command() {

            @Override
            public void execute(Player player, String command, String[] parts) {
                int rank = 0;
                World.getServerData().setTopPkers(World.getServerData().getTopPkers());
                World.getServerData().processQueue();
                TopPkersWeekly wogw = World.getServerData().getTopPkersWeekly();
                Date enddate =  wogw.getDate();
                player.getPacketSender().sendString(50102, "Current week Ends: " + Utils.theEndDate(enddate)+" (in "+Utils.differenceindays(enddate)+" days)");
                player.getPacketSender().sendString(50103, "#1 in each category will receive either $10 or 10M 07 GP!");

                player.getInterfaceManager().open(50190);
                for (int i = 0; i < 10; i++) {
                    player.getPacketSender().sendFrame126("", 50200 + i);
                    player.getPacketSender().sendFrame126("", 50210 + i);
                    player.getPacketSender().sendFrame126("", 50220 + i);
                }
                player.getPacketSender().sendString(50104, "");
                TopPkers toppkers = World.getServerData().getTopPkers();
                ArrayList<Map.Entry<String, Integer>> winners = toppkers.getSortedResults();

                Map<String, Integer> winnerMap = new HashMap<>();
                winners.forEach(entry -> winnerMap.put(entry.getKey(), entry.getValue()));


for(int i = 0 ; i < winners.size(); i++){
    if(player.getUsername().toLowerCase().equals(winners.get(i).getKey())){
      //  player.message("found at index: "+i);
        rank = i + 1;
    }
}

                int killcount = player.getAttribOr(AttributeKey.PLAYER_KILLS, 0);
                player.getPacketSender().sendString(50104, "Your current kills: "+killcount+" / "+(rank == 0? "unranked" : "rank #"+rank+"")+"");


                StringBuilder s = new StringBuilder();

int i = 1;
player.message("amount of ppl: "+winners.size());
                for (Map.Entry<String, Integer> entry : winners) {
                    player.getPacketSender().sendFrame126("#"+i+"", 50199 + i);
                    player.getPacketSender().sendFrame126(entry.getKey(), 50209 + i);
                    player.getPacketSender().sendFrame126(entry.getValue()+"", 50219 + i);
                        i++;
//                    if (entry.getValue() > 0) {
//                        s.append(entry.getKey() + "(" + entry.getValue() + ")  ");
//                }

            }
//                if(s.length() > 0)
//                    player.message("list: "+s.toString());
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("checkincrease", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                TopPkers toppkers = World.getServerData().getTopPkers();
                ArrayList<Map.Entry<String, Integer>> winners = toppkers.getSortedResults();

                Map<String, Integer> winnerMap = new HashMap<>();
                winners.forEach(entry -> winnerMap.put(entry.getKey(), entry.getValue()));

                World.getServerData().getTopPkers().update(player.getUsername().toLowerCase(), 1);//increase by one
                for (Map.Entry<String, Integer> entry : winners) {
                        player.message(entry.getKey()+" : "+entry.getValue());
                }
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("howmanytiles", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.message(MovementPacketListener.steps+"");
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("testspellfilter", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {

                Spell magicspell = MagicClickSpells.getMagicSpellByName(parts[1]);
                player.message(magicspell.levelRequired()+ "");

            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("row", new Command() {
            @Override

            public void execute(Player player, String command, String[] parts) {

              RingOfWealth.displayOptions(player);
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("flashsidebar", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.getDialogueManager().start(new Tutorial());
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("rewardbarrows", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                //player.message("testing");
                //System.out.println("start");
                Barrows.testloot(player);
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("vorkath", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.teleport(2273, 4049);
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        // soz for spam xd
        commands.put("hitt1", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.message("ok");
                player.lockDelayDamage();
                player.hit(player,5);
                player.hit(player,5);
                Chain.bound(player).name("hitt1Task").runFn(5, () -> {
                    player.unlock();
                    player.message("unlock 5t later");
                });
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("wnpc", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
               // String[] args = playerCommand.split(" ");
                int id = Integer.parseInt(parts[1]);
                NpcDefinition def = World.getWorld().definitions().get(NpcDefinition.class, id);
                String name = def.name;
                try(FileWriter fw = new FileWriter("data/map/spawntoadd.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {
                    out.println("//"+name+"\n{ \"id\":"+id+", \"x\":"+player.tile().x+", \"y\": "+player.tile().y+",\"z\":"+player.tile().level+",\"walkRange\":6}, "
                    );

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });

        commands.put("hometel", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                Chain.bound(player).name("HomeTeleTask").runFn(1, () -> {
                        player.animate(4847);
                        player.graphic(800);
                }).then(8, () -> {
                        //player.message("8");
                    player.animate(4850);
                        player.graphic(801,0,0);
                })
                    .then(3, () -> {
                   //     player.message("3");
                        player.animate(4853);
                        player.graphic(802);
                }).then(5, () -> {
                      //  player.message("5");
                        player.animate(4855);
                        player.graphic(803);
                })
                    .then(5, () -> {
                     //   player.message("5 again");
                        player.animate(4857);
                        player.graphic(804);
                    }).then(2, () -> {
                        player.teleport(3222,3222);
                    });
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("hitt2", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.message("ok");
                player.lockNoDamage();
                player.hit(player,5);
                player.hit(player,5);
                Chain.bound(player).name("hitt2Task").runFn(5, () -> {
                    player.unlock();
                    player.message("unlock 5t later - expect damage to be nullified");
                });
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("hitt3", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.message("ok");
                player.lockDamageOk();
                player.hit(player,5);
                player.hit(player,5);
                Chain.bound(player).name("hitt3Task").runFn(5, () -> {
                    player.unlock();
                    player.message("unlock 5t later - expect damage to have appeared instantly, but we cant move");
                });
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("recmd", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                commands.clear();
                CommandManager.loadCmds();
                player.message("Commands have been reloaded.");
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });

        commands.put("waittest", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
             //   commands.clear();
                Chain.bound(player).name("waittest").waitForTile(player.tile().transform(3, 0, 0), () -> {
                    player.message("arrived");
                });
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("huntt1", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                for (int i = 0; i < 100; i++) {
                    Chain.bound(player).name("huntt1").runFn(1 + i, () -> {
                        player.teleport(player.tile().transform(1, 0, 0)); // move right every tick
                        World.getWorld().registerNpc(new Npc(2912, player.tile()));
                    }).then(1, () -> {
                        Hunter.lay(player, new Chinchompas(player));
                    });
                }
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("hit", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.hit(player, Utils.random(40), 1, CombatType.MELEE).checkAccuracy().submit();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isAdminOrGreater(player);
            }
        });

        byLazy("save", (p,cmd,parts) -> {
            World.getWorld().ls.savePlayerAsync(p);
            p.message("Saving " + p.getUsername());
        });

        for (String s : new String[] {"cpa", "clipat", "clippos"})
            byLazy(s, (p,cmd,parts) -> {
                int c = RegionManager.getClipping(p.tile().x, p.tile().y, p.tile().level);

                p.message("cur clip %s = %s", c, World.getWorld().clipstr(c));
                p.message(String.format("%s", World.getWorld().clipstrMethods(p.tile())));
                Debugs.CLIP.debug(p, String.format("%s", World.getWorld().clipstrMethods(p.tile())));
            });
        byLazy("scm", (player,cmd,parts) -> {
            ArrayList<GroundItem> gis = new ArrayList<>();
            int baseitem = 1;
            int radius = parts.length > 1 ? Integer.parseInt(parts[1]) : 4;
            for (int x = player.getX() - radius; x < player.getX() + radius; x++) {
                for (int y = player.getY() - radius; y < player.getY() + radius; y++) {
                    int clip = RegionManager.getClipping(x, y, player.getZ());
                    int item = 0;
                    item = clip==0 ? 227 : baseitem++;
                    Debugs.CLIP.debug(player, String.format("%s is %s %s = %s %s", new Tile(x,y,player.getZ()), item, new Item(item).name(),
                        clip, World.getWorld().clipstr(clip)));
                    if (clip != 0) {
                        GroundItem gi = new GroundItem(new Item(item, 1), Tile.create(x, y, player.tile().level), player);
                        player.getPacketSender().createGroundItem(gi);
                        gis.add(gi);
                    }
                }
            }
            Task.runOnceTask(10, c -> {
                gis.forEach(GroundItemHandler::sendRemoveGroundItem);
            });
        });
        byLazy("rpk", (p,cmd,s) -> {
            // makes a npc index attack us, used for clipping testing
            World.getWorld().getNpcs().get(Integer.parseInt(s[1])).getCombat().attack(p);
        });
        byLazy("odef", (p,cmd,s) -> {
            ObjectDefinition def = World.getWorld().definitions().get(ObjectDefinition.class, Integer.parseInt(s[1]));
            p.message(def.name+" "+def.tall +" "+def.tall);
        });
        byLazy("dprints", (p,cmd,s) -> {
            // usage is ::dprints tftftftf for true/false in order of each Debug enum
            for (int i = 0; i < Math.min(s[1].length(), Debugs.values().length); i++) {
                //noinspection StringOperationCanBeSimplified
                Debugs.values()[i].enabled = s[1].substring(i, i + 1).equals("t");
                //noinspection StringOperationCanBeSimplified
                System.out.println(Debugs.values()[i].name()+" "+Debugs.values()[i].enabled + " by " + s[1].substring(i, i + 1).equals("t"));
            }
        });
        byLazy("dp2", (p,cmd,s) -> {
            // usage is ::dprints tftftftf for true/false in order of each Debug enum
            for (int i = 0; i < s[1].length(); i++) {
                //noinspection StringOperationCanBeSimplified
                int id = Integer.parseInt(s[1].substring(i, i + 1));
                Debugs.values()[id].enabled = !Debugs.values()[id].enabled;
                //noinspection StringOperationCanBeSimplified
                System.out.println(Debugs.values()[id].name()+" "+Debugs.values()[id].enabled);
            }
        });

        commands.put("drop", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                DropsDisplay.start(player);
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });

        commands.put("sidebar", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.getInterfaceManager().setSidebar(GameConstants.LOGOUT_TAB, 46500);
                player.message("sending sidebar");
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        byLazy("testalert", (p,cmd,parts) -> {
            p.getDialogueManager().start(new Dialogue() {
                @Override
                protected void start(Object... parameters) {
                    send(DialogueType.ITEM_STATEMENT, new Item(4151), "", "yo", "");//must be new Item or else you will get error
                    setPhase(0);
                }

                @Override
                protected void next() {
                    if (isPhase(0)) {
                        stop();
                    }
                }
            });
           // p.getPacketSender().sendAlertItems("Congratulations! You can now purchase", "@red@Dragon @lre@weapons from the Heroes guild!", new Item(DRAGON_DAGGER),new Item(DRAGON_MACE), new Item(DRAGON_SCIMITAR));

        });

        byLazy("gfxlength", (p,cmd,parts) -> {
            World.getWorld().tileGraphic(1325, new Tile(p.tile().getX(), p.tile().getY()-1), 0, Integer.parseInt(parts[1]));
        });
        byLazy("testdiary", (p,cmd,parts) -> {
            p.getPacketSender().sendFrame77(29471+4, (int) easyDone());
        });
        byLazy("testdiaryard", (p,cmd,parts) -> {
            p.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.TELEPORT_ARDOUGNE);

        });
        byLazy("up", (p,cmd,parts) -> {
            p.teleport(p.tile().transform(0,0,1));
        });
        byLazy("saveprocess", (p,cmd,parts) -> {
            World.getServerData().setSerializablePairNex(new SerializablePair<String,Long>("sugary", 41000000L));

            World.getServerData().processQueue();
           // TaskManager.submit(new ForceMovementTask(p, 1, new ForceMovement(p.tile().clone(), new Tile(0, 5), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Direction.SOUTH.faceValue)));

        });
        byLazy("down", (p,cmd,parts) -> {
            p.teleport(p.tile().transform(0,0,-1));
        });
        byLazy("openpresets", (p,cmd,parts) -> {
            p.getPresetManager().open();
        });
        byLazy("objsat", (p,cmd,parts) -> {
            int total = MapObjects.mapObjects.size();
            boolean ignoreZ = (parts.length < 2 ? 0 : Integer.parseInt(parts[1])) == 1;
            long hash = MapObjects.getHash(p.getX(), p.getY(), 0);
            ArrayList<GameObject> gameObjects = MapObjects.mapObjects.get(hash);
            if (gameObjects == null)
                gameObjects = new ArrayList<>(0);
            p.message(String.format("total %s objs cached. %s at pos", total, gameObjects.size()));
            System.out.println(Arrays.toString(gameObjects.toArray()));
        });

        byLazy("iot1", (p,cmd,parts) -> {
            // fuck me up fam
            p.putAttrib(AttributeKey.BGS_GFX_GOLD, 1); // expected type = Boolean
            p.requestLogout();
        });
        byLazy("bc1", (p,cmd,parts) -> { // banking case 1
            p.getBank().clear();
            p.getBank().addAll(
                new Item(ItemIdentifiers.RING_OF_SUFFERING, 50),
                new Item(ItemIdentifiers.RING_OF_RECOIL, 50),
                new Item(ItemIdentifiers.RING_OF_SUFFERING_R),
                new Item(ItemIdentifiers.RING_OF_SUFFERING_R)
            );
            p.getBank().tabAmounts = new int[10];
            p.getBank().tabAmounts [0] = 4;
            p.inventory().clear();
            p.inventory().addAll(
                new Item(ItemIdentifiers.FIRE_RUNE, 1000),
                new Item(ItemIdentifiers.NATURE_RUNE, 1000),
                new Item(ItemIdentifiers.RING_OF_RECOIL+1, 100)
            );
        });
        byLazy("ps", (p,cmd,parts) -> { // save .. dont have to relog to refresh json file
            PlayerSave.save(p);
        });
        byLazy("openmaster", (p,cmd,parts) -> {
            int amount = Integer.parseInt(parts[1]);
            p.inventory().add(new Item(19836, amount));
            for (int i = 0; i < amount; i++) {
                TreasureRewardCaskets.openCasket(p, new Item(19836));
            }
        });

        byLazy("obj", (p,cmd,parts) -> {
            int id = Integer.parseInt(parts[1]);
            int type = parts.length >= 3 ? Integer.parseInt(parts[2]) : 10;
            int rot = parts.length >= 4 ? Integer.parseInt(parts[3]) : 0;
            p.getPacketSender().sendObject(new GameObject(id, p.tile().copy(), type, rot));
        });
        byLazy("objexist", (p,cmd,parts) -> {
            if(ObjectManager.exists(31858, new Tile(3126,3635))){
                System.out.println("exists");
            } else {
                System.out.println("null");

            }

        });
        byLazy("ndrop", (p,cmd,parts) -> {
            DropsDisplay.clickActions(p, 35143);
        });
        byLazy("venom", (p,cmd,parts) -> {
            p.venom(p);
        });
        byLazy("curevenom", (p,cmd,parts) -> {
            Venom.cure(2, p);
        });
        byLazy("curepoison", (p,cmd,parts) -> {
            Poison.cure(p);
        });
        byLazy("tpro", ((player, s, strings) -> {
            new Projectile(player.getCentrePosition(), player.tile().transform(3, 3), 0, 1482, 65, 20, 20, 20, 1).sendProjectile();
        }));
        byLazy("tpro2", ((player, s, strings) -> {
            World.getWorld().getNpcs().filter(Objects::nonNull).min(Comparator.comparingInt(o -> o.tile().distance(player.tile()))).ifPresent(n -> {
                n.forceChat("hit me");
                new Projectile(player, n, 1482, 65, 150, 20, 20, 1).sendProjectile();
            });
        }));
        byLazy("askref", (player, s, strings) -> {
            Referrals.INSTANCE.askReferrerName(player);
        });
        byLazy("cia4", (player, s, strings) -> {
            throw new RuntimeException("testy");
        });
    }
}
