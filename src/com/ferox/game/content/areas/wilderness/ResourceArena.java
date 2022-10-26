package com.ferox.game.content.areas.wilderness;

import com.ferox.GameServer;
import com.ferox.game.content.skill.impl.mining.Mining;
import com.ferox.game.task.Task;
import com.ferox.game.task.TaskManager;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueManager;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.dialogue.Expression;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Area;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.chainedwork.Chain;

import java.util.Arrays;

import static com.ferox.util.ItemIdentifiers.BLOOD_MONEY;
import static com.ferox.util.ItemIdentifiers.COINS_995;
import static com.ferox.util.NpcIdentifiers.PILES;
import static com.ferox.util.NpcIdentifiers.ROCKS_6601;
import static com.ferox.util.ObjectIdentifiers.GATE_26760;

public class ResourceArena extends PacketInteraction {

    private static final Area ARENA_BOUNDARIES = new Area(3174, 3924, 3196, 3944);
    public static int[] ALLOWED_EXCHANGE = new int[] {440, 453, 444, 447, 449, 451, 1515, 1513, 11936, 11934, 2349, 2351, 2353, 2355, 2357, 2359, 2361, 2363, 451, 13439, 10138};

    private void swap(Player player, int original, int result) {
        int currency = GameServer.properties().pvpMode ? BLOOD_MONEY : COINS_995;
        String name = GameServer.properties().pvpMode ? "bm" : "coins";
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Banknote "+player.inventory().count(original) + " "+new Item(original).name(), "Yes - "+player.inventory().count(original) * 50 + " "+name, "Cancel");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(option == 1) {
                    if(!player.inventory().contains(new Item(currency, player.inventory().count(original) * 50))) {
                        DialogueManager.npcChat(player, Expression.VERY_SAD, 13, "Unfortunately, you don't have enough "+name, "right now to do that.");
                    } else {
                        int num = player.inventory().count(original);
                        player.inventory().remove(new Item(currency, player.inventory().count(original) * 50));
                        player.inventory().remove(new Item(original, num));
                        player.inventory().add(new Item(result, num));

                        //TODO OSS has an achievement here

                        player.getDialogueManager().start(new Dialogue() {
                            @Override
                            protected void start(Object... options) {
                                send(DialogueType.ITEM_STATEMENT, new Item(original), "", "Piles converts your items to banknotes.");
                                setPhase(0);
                            }

                            @Override
                            public void next() {
                                if (isPhase(0)) {
                                    stop();
                                }
                            }
                        });
                    }
                } else if(option == 2) {
                    stop();
                }
            }
        });
    }

    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if(npc.id() == ROCKS_6601) {
            var pick = Mining.findPickaxe(player);

            if (pick.isEmpty()) {
                player.message("You do not have a pickaxe which you have the Mining level to use.");
            } else {
                if (player.skills().level(Skills.MINING) < 85) {
                    player.sound(2277, 0);
                    DialogueManager.sendStatement(player,"You need a Mining level of 85 to mine this rock.");
                } else {
                    Chain.bound(null).runFn(1, () -> player.message("You swing your pick at the rock."));

                    TaskManager.submit(player.loopTask = new Task("loop_skill_task_golem", 1) {

                        int internalTimer = 1;
                        @Override
                        protected void execute() {
                            player.animate(pick.get().anim);

                            if (internalTimer-- == 0) {
                                var odds = Mining.chance(player.skills().level(Skills.MINING), Mining.Rock.RUNE, pick.get());
                                var roll = World.getWorld().random(100);
                                //System.out.println("roll = "+roll);
                                //System.out.println("odds: "+odds);

                                if (roll <= odds) {
                                    player.message("You manage to mine some runite.");

                                    player.sound(3600, 0);
                                    player.animate(-1);

                                    player.inventory().addOrDrop(new Item(Mining.Rock.RUNE.ore));
                                    player.skills().addXp(Skills.MINING, Mining.Rock.RUNE.xp);

                                    //TODO achievement here runite golem
                                    World.getWorld().getNpcs().remove(npc);
                                    stop();
                                } else {
                                    internalTimer = 3;
                                }
                            }
                        }

                        @Override
                        public void onStop() {
                            player.animate(-1);
                        }
                    });
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean handleItemOnNpc(Player player, Item item, Npc npc) {
        if (npc.id() == PILES) {
            if (Arrays.stream(ResourceArena.ALLOWED_EXCHANGE).anyMatch(id -> id == item.getId())) {
                swap(player, item.getId(), item.note().getId());
            } else {
                DialogueManager.npcChat(player, Expression.VERY_SAD, PILES, "Sorry, I wasn't expecting anyone to want to convert", "that sort of item, so I haven't any banknotes for it.");
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(obj.getId() == GATE_26760) {
            if (option == 1) {
                player.faceObj(obj);
                String name = GameServer.properties().pvpMode ? "BM" : "coins";
                int itemId = GameServer.properties().pvpMode ? BLOOD_MONEY : COINS_995;
                int amount = GameServer.properties().pvpMode ? 100 : 7500;

                if (player.tile().y == 3945 && player.tile().y > obj.tile().y) {
                    if (player.inventory().count(itemId) < amount) {
                        player.message("You do not have enough "+name+" to enter the Arena.");
                    } else {
                        player.getDialogueManager().start(new Dialogue() {
                            @Override
                            protected void start(Object... parameters) {
                                send(DialogueType.OPTION, "Pay "+amount+" "+name+" to enter?", "Yes", "No");
                                setPhase(0);
                            }

                            @Override
                            protected void select(int option) {
                                if(option == 1) {
                                    if (ObjectManager.exists(1548, new Tile(obj.tile().x, 3945))) {
                                        return;
                                    }
                                    player.lockDelayDamage();
                                    player.inventory().remove(new Item(itemId, amount));
                                    player.message("You pay "+amount+" "+name+" and enter the resource arena.");
                                    GameObject old = new GameObject(obj.getId(), obj.tile(), obj.getType(), obj.getRotation());
                                    GameObject spawned = new GameObject(1548, new Tile(obj.tile().x,3945), obj.getType(),2);
                                    ObjectManager.removeObj(obj);
                                    ObjectManager.addObj(spawned);
                                    player.getMovementQueue().walkTo(new Tile(3184, 3944));
                                    Chain.bound(player).runFn(2, () -> {
                                        ObjectManager.removeObj(spawned);
                                        ObjectManager.addObj(old);
                                        player.unlock();
                                    });
                                    stop();
                                } else if(option == 2) {
                                    stop();
                                }
                            }
                        });
                    }
                } else if (player.tile().y == 3944 && player.tile().y <= obj.tile().y) {
                    if (!player.tile().equals(obj.tile().transform(0, 0, 0))) {
                        player.getMovementQueue().clear();
                        player.getMovementQueue().walkTo(obj.tile().transform(0, 0, 0));
                    }

                    if (ObjectManager.exists(1548, new Tile(obj.tile().x, 3945))) {
                        return true;
                    }

                    GameObject old = new GameObject(obj.getId(), obj.tile(), obj.getType(), obj.getRotation());
                    GameObject spawned = new GameObject(1548, new Tile(obj.tile().x, 3945), obj.getType(), 2);
                    player.lockDelayDamage();
                    ObjectManager.removeObj(old);
                    ObjectManager.addObj(spawned);
                    player.getMovementQueue().walkTo(new Tile(3184,  3945));
                    Chain.bound(player).runFn(2, () -> {
                        ObjectManager.removeObj(spawned);
                        ObjectManager.addObj(old);
                        player.unlock();
                    });
                } else if (player.tile().y == 3944 && player.tile().y <= obj.tile().y) {
                    if (!player.tile().equals(obj.tile().transform(0, 0, 0))) {
                        player.getMovementQueue().clear();
                        player.getMovementQueue().walkTo(obj.tile().transform(0, 0, 0));
                    }

                    if (ObjectManager.exists(1548, new Tile(obj.tile().x, 3945))) {
                        return true;
                    }

                    GameObject old = new GameObject(obj.getId(), obj.tile(), obj.getType(), obj.getRotation());
                    GameObject spawned = new GameObject(1548, new Tile(obj.tile().x, 3945), obj.getType(), 2);

                    player.lockDelayDamage();
                    ObjectManager.removeObj(old);
                    ObjectManager.addObj(spawned);
                    player.getMovementQueue().walkTo(new Tile(3184,  3945));
                    Chain.bound(null).runFn(2, () -> {
                        ObjectManager.removeObj(spawned);
                        ObjectManager.addObj(old);
                        player.unlock();
                    });
                }
            } else if(option == 2) {
                player.faceObj(obj);
                if (player.tile().y == 3945 && player.tile().y > obj.tile().y) {

                    int count = 0;
                    for (Player p : World.getWorld().getPlayers()) {
                        if (p != null && p.tile().inArea(3174, 3924, 3196, 3944))
                            count++;
                    }

                    if (count == 0) {
                        DialogueManager.sendStatement(player, "You peek inside the gate and see no adventurers inside the arena.");
                    } else {
                        DialogueManager.sendStatement(player, "You peek inside the gate and see " + count + " adventurer inside the arena.");
                    }
                } else if (player.tile().y == 3944) {
                    player.message("All you see is the barren wasteland of the Wilderness.");
                }
            }
            return true;
        }
        return false;
    }

}
