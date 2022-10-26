package com.ferox.game.content.packet_actions.interactions.container;

import com.ferox.game.content.duel.Dueling;
import com.ferox.game.content.gambling.GamblingSession;
import com.ferox.game.content.syntax.impl.LootingBagX;
import com.ferox.game.content.trade.Trading;
import com.ferox.game.content.tradingpost.TradingPost;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.PlayerStatus;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.container.shop.Shop;
import com.ferox.game.world.items.container.shop.ShopUtility;
import com.ferox.net.packet.interaction.PacketInteractionManager;

import static com.ferox.game.world.InterfaceConstants.*;

/**
 * @author PVE
 * @Since augustus 26, 2020
 */
public class FourthContainerAction {

    public static void fourthAction(Player player, int interfaceId, int slot, int id) {
        var count = player.inventory().count(id);
        Item item = new Item(id, count);
        if(PacketInteractionManager.checkItemContainerActionInteraction(player, item, slot, interfaceId, 4)) {
            return;
        }

        if(player.getRunePouch().removeFromPouch(interfaceId, id, slot,4)) {
            return;
        }

        if (TradingPost.handleSellingItem(player, interfaceId, id, count))
            return;

        if(player.getRunePouch().moveToRunePouch(interfaceId, id, slot,4)) {
            return;
        }

        /* Looting bag */
        if (interfaceId == LOOTING_BAG_BANK_CONTAINER_ID) {
            Item lootingBagItem = player.getLootingBag().get(slot);
            if (lootingBagItem == null) {
                return;
            }
            boolean banking = player.getAttribOr(AttributeKey.BANKING, false);

            if (banking) {
                player.setEnterSyntax(new LootingBagX(lootingBagItem.getId(), slot, true));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to deposit?");
            }
        }

        if (interfaceId == LOOTING_BAG_DEPOSIT_CONTAINER_ID) {
            Item lootingBagItem = player.inventory().get(slot);
            if (lootingBagItem == null) {
                return;
            }

            player.setEnterSyntax(new LootingBagX(lootingBagItem.getId(), slot, false));
            player.getPacketSender().sendEnterAmountPrompt("How many would you like to deposit?");
        }

        if (interfaceId == WITHDRAW_BANK) {
            player.getBank().withdraw(id, slot, Integer.MAX_VALUE);
        }

        if (interfaceId == INVENTORY_STORE) {
            boolean banking = player.getAttribOr(AttributeKey.BANKING, false);
            boolean priceChecking = player.getAttribOr(AttributeKey.PRICE_CHECKING, false);
            boolean depositBoxing = player.getAttribOr(AttributeKey.DEPOSIT_BOXING, false);
            boolean goodiebagChecking = player.getAttribOr(AttributeKey.GOODIE_BAG_CHECKING, false);

            if (priceChecking) {
                player.getPriceChecker().deposit(slot, Integer.MAX_VALUE);
            }
            if (goodiebagChecking) {
                player.getGoodieBag().deposit(slot, Integer.MAX_VALUE);
            }
            if (depositBoxing) {
                player.getDepositBox().deposit(slot, Integer.MAX_VALUE);
            }
            if (banking) {
                player.getBank().deposit(slot, Integer.MAX_VALUE);
            }
        }

        if (interfaceId == PRICE_CHECKER_DISPLAY_ID) {
            boolean priceChecking = player.getAttribOr(AttributeKey.PRICE_CHECKING, false);
            if (priceChecking) {
                int amount_to_withdraw = player.getPriceChecker().count(id);
                player.getPriceChecker().withdraw(id, amount_to_withdraw);
                return;
            }
        }
        if (interfaceId == GOODIE_BAG_DISPLAY_ID) {
            boolean goodiebagChecking = player.getAttribOr(AttributeKey.GOODIE_BAG_CHECKING, false);
            if (goodiebagChecking) {
                int amount_to_withdraw = player.getPriceChecker().count(id);
                player.getGoodieBag().withdraw(id, amount_to_withdraw);
                return;
            }
        }

        if (interfaceId == ShopUtility.ITEM_CHILD_ID || interfaceId == ShopUtility.SLAYER_BUY_ITEM_CHILD_ID) {
            Shop.exchange(player, id, slot, 4, true);
        }

        if (interfaceId == SHOP_INVENTORY) {
            Shop.exchange(player, id, slot, 4, false);
        }

        // Withdrawing items from duel
        if (interfaceId == Dueling.MAIN_INTERFACE_CONTAINER) {
            if (player.getStatus() == PlayerStatus.DUELING) {
                player.getDueling().handleItem(id, player.getDueling().getContainer().count(id), slot,
                    player.getDueling().getContainer(), player.inventory());
            }
        }

        // Withdrawing items from gamble
        if (interfaceId == GamblingSession.MY_ITEMS_OFFERED || interfaceId == GamblingSession.OPPONENT_ITEMS_OFFERED) {
            if (player.getStatus() == PlayerStatus.GAMBLING) {
                player.getGamblingSession().handleItem(id, player.getGamblingSession().getContainer().count(id), slot, player.getGamblingSession().getContainer(), player.inventory());;
            }
        }

        if (interfaceId == REMOVE_INVENTORY_ITEM) {// Duel/Trade inventory
            boolean priceChecking = player.getAttribOr(AttributeKey.PRICE_CHECKING, false);
            boolean depositBoxing = player.getAttribOr(AttributeKey.DEPOSIT_BOXING, false);
            boolean goodiebagChecking = player.getAttribOr(AttributeKey.GOODIE_BAG_CHECKING, false);

            if (priceChecking) {
                player.getPriceChecker().deposit(slot, player.inventory().count(id));
            } else if(depositBoxing){
                player.getDepositBox().deposit(slot, player.inventory().count(id));
            } else if(goodiebagChecking){
                player.getGoodieBag().deposit(slot, player.inventory().count(id));

            } else if (player.getStatus() == PlayerStatus.TRADING) {
                player.getTrading().handleItem(id, player.inventory().count(id), slot,
                    player.inventory(), player.getTrading().getContainer());
            } else if (player.getStatus() == PlayerStatus.DUELING) {
                player.getDueling().handleItem(id, player.inventory().count(id), slot,
                    player.inventory(), player.getDueling().getContainer());
            } else if (player.getStatus() == PlayerStatus.GAMBLING) {
                player.getGamblingSession().handleItem(id, player.inventory().count(id), slot, player.inventory(), player.getGamblingSession().getContainer());
            }
        }

        if (interfaceId == Trading.CONTAINER_INTERFACE_ID) {
            if (player.getStatus() == PlayerStatus.TRADING) {
                player.getTrading().handleItem(id, player.getTrading().getContainer().count(id), slot,
                    player.getTrading().getContainer(), player.inventory());
            }
        }

        if (interfaceId == 48542) {
            player.getPriceChecker().withdraw(id, player.getPriceChecker().count(id));
        }

    }
}
