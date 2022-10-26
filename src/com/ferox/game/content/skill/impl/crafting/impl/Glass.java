package com.ferox.game.content.skill.impl.crafting.impl;

import com.ferox.fs.ItemDefinition;
import com.ferox.game.action.Action;
import com.ferox.game.action.policy.WalkablePolicy;
import com.ferox.game.content.syntax.EnterSyntax;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.Utils;

import java.util.Arrays;
import java.util.Optional;

import static com.ferox.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * juni 16, 2020
 */
public class Glass {

    /**
     * Holds all the glass data.
     */
    public enum GlassData {
        VIAL(ItemIdentifiers.VIAL, 33, 35.0D, ItemIdentifiers.MOLTEN_GLASS),
        LIGHT_ORB(ItemIdentifiers.LIGHT_ORB, 87, 70.0D, ItemIdentifiers.MOLTEN_GLASS),
        BEER_GLASS(ItemIdentifiers.BEER_GLASS, 1, 17.5D, ItemIdentifiers.MOLTEN_GLASS),
        CANDLE_LANTERN(ItemIdentifiers.CANDLE_LANTERN, 4, 19.0D, ItemIdentifiers.MOLTEN_GLASS),
        OIL_LAMP(ItemIdentifiers.OIL_LAMP, 12, 25.0D, ItemIdentifiers.MOLTEN_GLASS),
        FISHBOWL(ItemIdentifiers.FISHBOWL, 42, 42.5D, ItemIdentifiers.MOLTEN_GLASS),
        LANTERN_LENS(ItemIdentifiers.LANTERN_LENS, 49, 55.0D, ItemIdentifiers.MOLTEN_GLASS),
        MOLTEN_GLASS(ItemIdentifiers.MOLTEN_GLASS, 1, 20.0D, SODA_ASH),
        UNPOWERED_ORB(ItemIdentifiers.UNPOWERED_ORB, 46, 52.5D, ItemIdentifiers.MOLTEN_GLASS);

        /**
         * The glass product.
         */
        private final int product;

        /**
         * The level required.
         */
        private final int level;

        /**
         * The experienced rewarded.
         */
        private final double experience;

        /**
         * The material required.
         */
        private final int material;

        /**
         * Constructs a new <code>GlassData</code>.
         */
        GlassData(int product, int level, double experience, int material) {
            this.product = product;
            this.level = level;
            this.experience = experience;
            this.material = material;
        }

        /**
         * Gets the glass data based on the material item.
         */
        public static Optional<GlassData> forGlass(int item) {
            return Arrays.stream(values()).filter(g -> g.material == item).findFirst();
        }
    }

    /**
     * Handles opening the glass crafting interface.
     */
    public static void open(Player player) {
        player.getInterfaceManager().open(11462);
    }

    /**
     * Handles clicking on the interface.
     */
    public static boolean click(Player player, int button) {
        switch (button) {
            /** Vial */
            case 11474:
                craft(player, GlassData.VIAL, 1);
                return true;
            case 11473:
                craft(player, GlassData.VIAL, 5);
                return true;
            case 11472:
                craft(player, GlassData.VIAL, 10);
                return true;
            case 11471:
                player.setEnterSyntax(new EnterSyntax() {
                    @Override
                    public void handleSyntax(Player player, long input) {
                        craft(player, GlassData.VIAL, (int) input);
                    }
                });
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to make?");
                return true;
            /** Orb */
            case 12396:
                craft(player, GlassData.UNPOWERED_ORB, 1);
                return true;
            case 12395:
                craft(player, GlassData.UNPOWERED_ORB, 5);
                return true;
            case 12394:
                craft(player, GlassData.UNPOWERED_ORB, 10);
                return true;
            case 11475:
                player.setEnterSyntax(new EnterSyntax() {
                    @Override
                    public void handleSyntax(Player player, long input) {
                        craft(player, GlassData.UNPOWERED_ORB, (int) input);
                    }
                });
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to make?");
                return true;
            /** Beer glass */
            case 12400:
                craft(player, GlassData.BEER_GLASS, 1);
                return true;
            case 12399:
                craft(player, GlassData.BEER_GLASS, 5);
                return true;
            case 12398:
                craft(player, GlassData.BEER_GLASS, 10);
                return true;
            case 12397:
                player.setEnterSyntax(new EnterSyntax() {
                    @Override
                    public void handleSyntax(Player player, long input) {
                        craft(player, GlassData.BEER_GLASS, (int) input);
                    }
                });
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to make?");
                return true;
            /** Candle lantern */
            case 12404:
                craft(player, GlassData.CANDLE_LANTERN, 1);
                return true;
            case 12403:
                craft(player, GlassData.CANDLE_LANTERN, 5);
                return true;
            case 12402:
                craft(player, GlassData.CANDLE_LANTERN, 10);
                return true;
            case 12401:
                player.setEnterSyntax(new EnterSyntax() {
                    @Override
                    public void handleSyntax(Player player, long input) {
                        craft(player, GlassData.CANDLE_LANTERN, (int) input);
                    }
                });
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to make?");
                return true;
            /** Oil lamp */
            case 12408:
                craft(player, GlassData.OIL_LAMP, 1);
                return true;
            case 12407:
                craft(player, GlassData.OIL_LAMP, 5);
                return true;
            case 12406:
                craft(player, GlassData.OIL_LAMP, 10);
                return true;
            case 12405:
                player.setEnterSyntax(new EnterSyntax() {
                    @Override
                    public void handleSyntax(Player player, long input) {
                        craft(player, GlassData.OIL_LAMP, (int) input);
                    }
                });
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to make?");
                return true;
            /** Fishbowl */
            case 6203:
                craft(player, GlassData.FISHBOWL, 1);
                return true;
            case 6202:
                craft(player, GlassData.FISHBOWL, 5);
                return true;
            case 6201:
                craft(player, GlassData.FISHBOWL, 10);
                return true;
            case 6200:
                player.setEnterSyntax(new EnterSyntax() {
                    @Override
                    public void handleSyntax(Player player, long input) {
                        craft(player, GlassData.FISHBOWL, (int) input);
                    }
                });
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to make?");
                return true;
            /** Lantern lens */
            case 12412:
                craft(player, GlassData.LANTERN_LENS, 1);
                return true;
            case 12411:
                craft(player, GlassData.LANTERN_LENS, 5);
                return true;
            case 12410:
                craft(player, GlassData.LANTERN_LENS, 10);
                return true;
            case 12409:
                player.setEnterSyntax(new EnterSyntax() {
                    @Override
                    public void handleSyntax(Player player, long input) {
                        craft(player, GlassData.LANTERN_LENS, (int) input);
                    }
                });
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to make?");
                return true;
        }
        return false;
    }

    /**
     * Handles crafting the glass.
     */
    public static void craft(Player player, GlassData glass, int amount) {
        if (player.skills().level(Skills.CRAFTING) < glass.level) {
            player.message("You need a crafting level of " + glass.level + " to craft this!");
            return;
        }

        if (!player.inventory().contains(GLASSBLOWING_PIPE) && glass != GlassData.MOLTEN_GLASS) {
            player.message("You need a glassblowing pipe to do this!");
            return;
        }

        if (!player.inventory().contains(glass.material)) {
            ItemDefinition def = World.getWorld().definitions().get(ItemDefinition.class, glass.material);
            String name = def.name;
            player.message("You need " + Utils.getAOrAn(name) + " " + name + " to do this!");
            return;
        }

        player.getInterfaceManager().close();
        player.action.execute(blow(player, glass, amount), true);
    }

    /**
     * Handles blowing the glass data.
     */
    private static Action<Player> blow(Player player, GlassData glass, int amount) {
        return new Action<Player>(player, 3, true) {
            int ticks = 0;

            @Override
            public void execute() {
                boolean moltenGlass = glass == GlassData.MOLTEN_GLASS;

                if (moltenGlass && (!player.inventory().contains(BUCKET_OF_SAND) || !player.inventory().contains(SODA_ASH))) {
                    player.message("You need a bucket of sand and soda ash to make molten glass!");
                    stop();
                    return;
                }

                if (!player.inventory().contains(GLASSBLOWING_PIPE) && !moltenGlass) {
                    player.message("You need a glassblowing pipe to do this!");
                    stop();
                    return;
                }

                if (!player.inventory().contains(glass.material)) {
                    ItemDefinition def = World.getWorld().definitions().get(ItemDefinition.class, glass.material);
                    if (def == null) return;
                    String name = def.name;
                    player.message("You need " + Utils.getAOrAn(name) + " " + name + " to do this!");
                    stop();
                    return;
                }

                player.inventory().remove(glass.material, 1);

                if (moltenGlass) {
                    player.inventory().replace(BUCKET_OF_SAND, BUCKET, true);
                }

                ItemDefinition def = World.getWorld().definitions().get(ItemDefinition.class, glass.material);
                if (def == null) return;
                String name = def.name;
                player.animate(moltenGlass ? 899 : 884);
                player.inventory().add(new Item(glass.product));
                player.skills().addXp(Skills.CRAFTING, glass.experience);
                player.message("You make " + Utils.getAOrAn(name) + " " + name + ".");

                if (++ticks == amount) {
                    stop();
                }
            }

            @Override
            public String getName() {
                return "Glass";
            }

            @Override
            public boolean prioritized() {
                return false;
            }

            @Override
            public WalkablePolicy getWalkablePolicy() {
                return WalkablePolicy.NON_WALKABLE;
            }
        };
    }
}
