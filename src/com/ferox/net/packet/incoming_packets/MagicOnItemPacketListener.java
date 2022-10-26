package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.fs.ItemDefinition;
import com.ferox.game.GameConstants;
import com.ferox.game.content.skill.impl.magic.JewelleryEnchantment;
import com.ferox.game.content.skill.impl.smithing.Bar;
import com.ferox.game.task.impl.DistancedActionTask;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.magic.MagicClickSpells;
import com.ferox.game.world.entity.combat.magic.Spell;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.player.MagicSpellbook;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.RequiredItem;
import com.ferox.game.world.items.ground.GroundItem;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.ClientToServerPackets;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;
import com.ferox.util.Color;

import java.util.Arrays;
import java.util.Optional;

import static com.ferox.util.CustomItemIdentifiers.WILDERNESS_KEY;
import static com.ferox.util.ItemIdentifiers.BLOOD_MONEY;
import static com.ferox.util.ItemIdentifiers.COINS_995;

/**
 * Handles the packet for using magic spells on items ingame.
 *
 * @author Professor Oak
 */
public class MagicOnItemPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (packet.getOpcode() == ClientToServerPackets.MAGIC_ON_ITEM_OPCODE) {
            final int slot = packet.readShort();
            final int itemId = packet.readShortA();
            final int childId = packet.readShort();
            final int spellId = packet.readShortA();

            if (player.locked()) {
                return;
            }

            // Some checks
            if (slot < 0 || slot > 27) {
                return;
            }
            Item item = player.inventory().get(slot);
            Item itemminusone = new Item(item.getId()-1);
            if (item == null || item.getId() != itemId) {
                return;
            }

            final Optional<MagicClickSpells.MagicSpells> magicSpell = MagicClickSpells.MagicSpells.find(spellId);

            if (magicSpell.isEmpty()) {
                return;
            }

            player.debugMessage("[MagicOnItemPacket] spell=" + magicSpell + " itemId=" + itemId + " slot=" + slot + " childId=" + childId);

            if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
                player.getBankPin().openIfNot();
                return;
            }

            if (player.askForAccountPin()) {
                player.sendAccountPinMessage();
                return;
            }

            if (!player.dead()) {
                player.stopActions(false); // Seems like this does not cancel moving on rs? I can alch while I run.

                //Do actions...
                final MagicClickSpells.MagicSpells magicSpell2 = magicSpell.get();
                final Spell spell = magicSpell2.getSpell();
                int itemValue = item.definition(World.getWorld()).highAlchValue();
           //    ItemDefinition def = World.getWorld().definitions().get(ItemDefinition.class, item.getId());
                if(item.noted()){
                    itemValue = itemminusone.definition(World.getWorld()).highAlchValue();
                }


                switch (magicSpell2) {
                    case SUPERHEAT_ITEM:
                        if (player.getSpellbook() != MagicSpellbook.NORMAL)
                            return;
                        if (!player.getClickDelay().elapsed(500)) {
                            return;
                        }

                        Optional<Bar> data = Bar.getDefinitionByItem(item.getId());

                        if (data.isEmpty()) {
                            player.message("You can not super heat this item!");
                            return;
                        }

                        for (RequiredItem requiredItem : data.get().getOres()) {
                            if (!player.inventory().containsAll(requiredItem.getItem())) {
                                player.message("You do not contain the required items to super heat!");
                                return;
                            }
                        }

                        if (player.skills().xpLevel(Skills.SMITHING) < data.get().getLevelReq()) {
                            player.message("You need a smithing level of " + data.get().getLevelReq() + " to do super heat this item!");
                            return;
                        }

                        player.animate(722);
                        player.graphic(148);
                        player.getPacketSender().sendTab(6);
                        for (RequiredItem requiredItem : data.get().getOres()) {
                            player.inventory().removeAll(requiredItem.getItem());
                        }
                        player.inventory().addAll(new Item(data.get().getBar()));
                        player.skills().addXp(Skills.MAGIC, spell.baseExperience(), true);
                        player.skills().addXp(Skills.SMITHING, data.get().getXpReward(), true);
                        player.getClickDelay().reset();
                        return;
                    case ENCHANT_SAPPHIRE:
                    case ENCHANT_DIAMOND:
                    case ENCHANT_EMERALD:
                    case ENCHANT_ONYX:
                    case ENCHANT_ZENYTE:
                    case ENCHANT_DRAGONSTONE:
                    case ENCHANT_RUBY_TOPAZ:
                        if (JewelleryEnchantment.check(player, itemId, spellId)) {
                            if (!spell.canCast(player, null, spell.deleteRunes())) {
                                return;
                            }
                            JewelleryEnchantment.enchantItem(player, itemId);
                        }
                        return;
                    case LOW_ALCHEMY:
                        if (!item.rawtradable() || item.getId() == BLOOD_MONEY || item.getId() == COINS_995) {
                            player.message("You can't alch that item.");
                            return;
                        }

                        Item finalItem1 = item;
                        if (Arrays.stream(GameConstants.DONATOR_ITEMS).anyMatch(donator_item -> donator_item == finalItem1.getId())) {
                            player.message("You cannot alch that item.");
                            return;
                        }

                        if (!spell.canCast(player, null, spell.deleteRunes())) {
                            return;
                        }

                        int coinAmountToGive = (int) Math.floor(itemValue * 0.15);

//                        if (item.getValue() == 0) {
//                            coinAmountToGive = 0;
//                        }

                        spell.startCast(player, null);

                        item = new Item(item.getId(), 1);

                        player.inventory().remove(item, slot);
                     //   if (!GameServer.properties().pvpMode)
                            player.inventory().add(COINS_995, coinAmountToGive);
                        return;
                    case HIGH_ALCHEMY:
                        if (!item.rawtradable() || item.getId() == BLOOD_MONEY || item.getId() == COINS_995) {
                            player.message("You can't alch that item.");
                            return;
                        }

                        Item finalItem = item;
                        if (Arrays.stream(GameConstants.DONATOR_ITEMS).anyMatch(donator_item -> donator_item == finalItem.getId())) {
                            player.message("You cannot alch that item.");
                            return;
                        }

                        if (!spell.canCast(player, null, spell.deleteRunes())) {
                            return;
                        }

                        coinAmountToGive = (int) Math.floor(itemValue * 0.25);


                        if (item.getValue() == 0) {
                            coinAmountToGive = 0;
                        }

                        spell.startCast(player, null);

                        item = new Item(item.getId(), 1);

                        player.inventory().remove(item, slot);


                            player.inventory().add(COINS_995, coinAmountToGive);
                        return;
                }
            }
        }

        if (packet.getOpcode() == ClientToServerPackets.MAGIC_ON_GROUND_ITEM_OPCODE) {
            final int groundItemY = packet.readLEShort();
            final int groundItemId = packet.readUnsignedShort();
            final int groundItemX = packet.readLEShort();
            final int spellId = packet.readShortA();

            if (!player.locked() && !player.dead()) {
                Tile tile = new Tile(groundItemX, groundItemY, player.tile().level);
                final Optional<MagicClickSpells.MagicSpells> magicSpell = MagicClickSpells.MagicSpells.find(spellId);
                final MagicClickSpells.MagicSpells magicSpell2 = magicSpell.get();
                final Spell spell = magicSpell2.getSpell();
                if (magicSpell.isEmpty()) {
                    return;
                }

                Optional<GroundItem> groundItem = Optional.of(new GroundItem(new Item(groundItemId), tile, player));
                player.putAttrib(AttributeKey.INTERACTED_GROUNDITEM, groundItem.get());
                player.putAttrib(AttributeKey.INTERACTION_OPTION, 4);
                player.face(tile);
          //      player.message(groundItemX+" "+groundItemY);

                switch (magicSpell2) {
                    case TELEKINETIC_GRAB:
                        if (!spell.canCast(player, null, spell.deleteRunes())) {
                            return;
                        }


                        if (player.tile().isWithinDistance(tile, 5)) {
                            player.getMovementQueue().reset();
                            player.lock();
                            player.animate(710);

                            player.graphic(142,50,0);




                            player.runFn(3, () -> {
                                new Projectile(player.tile(), new Tile(groundItemX,groundItemY), 0, 143, 30, 2, 20, 0,1).sendProjectile();

                            });


                            player.runFn(4, () -> {
                                World.getWorld().tileGraphic(144, tile, 0, 0);
                                PickupItemPacketListener.pickup(player, new Item(groundItemId), tile, true);
                        player.unlock();
                            });
                        } else {
                            player.setDistancedTask(new DistancedActionTask() {

                                @Override
                                public void onReach() {
                                    player.lock();
                                    player.getMovementQueue().reset();
                                    player.animate(710);

                                    player.graphic(142,50,0);




                                    player.runFn(3, () -> {
                                        new Projectile(player.tile(), new Tile(groundItemX,groundItemY), 0, 143, 30, 2, 20, 0,1).sendProjectile();

                                    });


                                    player.runFn(4, () -> {
                                        World.getWorld().tileGraphic(144, tile, 0, 0);
                                        PickupItemPacketListener.pickup(player, new Item(groundItemId), tile, true);
                                        player.unlock();
                                    });
                                    stop();
                                }

                                @Override
                                public boolean reached() {
                                    return player.tile().isWithinDistance(tile, 5);
                                }
                            });
                        }

                       // player.message("here");
                        break;
                }
            }
        }
    }
}
