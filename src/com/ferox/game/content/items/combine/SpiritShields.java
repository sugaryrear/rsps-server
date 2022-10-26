package com.ferox.game.content.items.combine;

import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.chainedwork.Chain;
import static com.ferox.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | March, 16, 2021, 15:04
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class SpiritShields extends PacketInteraction {

    private final static int HOLY_ELIXIR = 12833;
    private final static int SPIRIT_SHIELD = 12829;
    private final static int BLESSED_SPIRIT_SHIELD = 12831;

    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == HOLY_ELIXIR && usedWith.getId() == SPIRIT_SHIELD) || (use.getId() == SPIRIT_SHIELD && usedWith.getId() == HOLY_ELIXIR)) {
            if(!player.inventory().containsAll(HOLY_ELIXIR, SPIRIT_SHIELD)) {
                return true;
            }
            player.inventory().remove(new Item(HOLY_ELIXIR), true);
            player.inventory().remove(new Item(SPIRIT_SHIELD), true);
            player.inventory().add(new Item(BLESSED_SPIRIT_SHIELD), true);
            player.getDialogueManager().start(new Dialogue() {
                @Override
                protected void start(Object... parameters) {
                    send(DialogueType.ITEM_STATEMENT, new Item(BLESSED_SPIRIT_SHIELD), "", "The spirit shield glows an eerie holy glow.");
                    setPhase(0);
                }

                @Override
                protected void next() {
                    if (isPhase(0)) {
                        stop();
                    }
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public boolean handleItemOnObject(Player player, Item item, GameObject object) {
        if (object.definition().name.equalsIgnoreCase("anvil")) {
            if (item.getId() == ELYSIAN_SIGIL) {
                if (player.skills().xpLevel(Skills.PRAYER) < 90 || player.skills().xpLevel(Skills.SMITHING) < 85) {
                    player.message("You don't have the skills required to make this. You need 85 Smithing and 90 Prayer.");
                    return true;
                }

                if(!player.inventory().contains(HAMMER)) {
                    player.message("You need an hammer to craft this powerful shield.");
                    return true;
                }

                player.animate(898);
                Chain.bound(player).name("SpiritShieldsAnvilTask").runFn(6, () -> player.animate(898)).then(6, () -> {
                    if (player.inventory().containsAll(BLESSED_SPIRIT_SHIELD, ELYSIAN_SIGIL)) {
                        player.inventory().remove(new Item(BLESSED_SPIRIT_SHIELD),true);
                        player.inventory().remove(new Item(ELYSIAN_SIGIL),true);
                        player.inventory().add(new Item(ELYSIAN_SPIRIT_SHIELD),true);
                    }
                    player.getDialogueManager().start(new Dialogue() {
                        @Override
                        protected void start(Object... parameters) {
                            send(DialogueType.ITEM_STATEMENT, new Item(ELYSIAN_SPIRIT_SHIELD), "", "You successfully combine the Elysian sigil with the shield.");
                            setPhase(0);
                        }

                        @Override
                        protected void next() {
                            if (isPhase(0)) {
                                stop();
                            }
                        }
                    });
                });
                return true;
            }
            if (item.getId() == ARCANE_SIGIL) {
                if (player.skills().xpLevel(Skills.PRAYER) < 90 || player.skills().xpLevel(Skills.SMITHING) < 85) {
                    player.message("You don't have the skills required to make this. You need 85 Smithing and 90 Prayer.");
                    return true;
                }

                if(!player.inventory().contains(HAMMER)) {
                    player.message("You need an hammer to craft this powerful shield.");
                    return true;
                }

                player.animate(898);
                Chain.bound(player).name("SpiritShieldsAnvilTask").runFn(6, () -> player.animate(898)).then(6, () -> {
                    if (player.inventory().containsAll(BLESSED_SPIRIT_SHIELD, ARCANE_SIGIL)) {
                        player.inventory().remove(new Item(BLESSED_SPIRIT_SHIELD),true);
                        player.inventory().remove(new Item(ARCANE_SIGIL),true);
                        player.inventory().add(new Item(ARCANE_SPIRIT_SHIELD),true);
                    }
                    player.getDialogueManager().start(new Dialogue() {
                        @Override
                        protected void start(Object... parameters) {
                            send(DialogueType.ITEM_STATEMENT, new Item(ARCANE_SPIRIT_SHIELD), "", "You successfully combine the Arcane sigil with the shield.");
                            setPhase(0);
                        }

                        @Override
                        protected void next() {
                            if (isPhase(0)) {
                                stop();
                            }
                        }
                    });
                });
                return true;
            }
            if (item.getId() == SPECTRAL_SIGIL) {
                if (player.skills().xpLevel(Skills.PRAYER) < 90 || player.skills().xpLevel(Skills.SMITHING) < 85) {
                    player.message("You don't have the skills required to make this. You need 85 Smithing and 90 Prayer.");
                    return true;
                }

                if(!player.inventory().contains(HAMMER)) {
                    player.message("You need an hammer to craft this powerful shield.");
                    return true;
                }

                player.animate(898);
                Chain.bound(player).name("SpiritShieldsAnvilTask").runFn(6, () -> player.animate(898)).then(6, () -> {
                    if (player.inventory().containsAll(BLESSED_SPIRIT_SHIELD, SPECTRAL_SIGIL)) {
                        player.inventory().remove(new Item(BLESSED_SPIRIT_SHIELD),true);
                        player.inventory().remove(new Item(SPECTRAL_SIGIL),true);
                        player.inventory().add(new Item(SPECTRAL_SPIRIT_SHIELD),true);
                    }
                    player.getDialogueManager().start(new Dialogue() {
                        @Override
                        protected void start(Object... parameters) {
                            send(DialogueType.ITEM_STATEMENT, new Item(SPECTRAL_SPIRIT_SHIELD), "", "You successfully combine the Spectral sigil with the shield.");
                            setPhase(0);
                        }

                        @Override
                        protected void next() {
                            if (isPhase(0)) {
                                stop();
                            }
                        }
                    });
                });
                return true;
            }
        }
        return false;
    }
}
