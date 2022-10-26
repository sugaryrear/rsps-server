package com.ferox.game.content.packet_actions.interactions.container;

import com.ferox.game.content.duel.Dueling;
import com.ferox.game.content.gambling.GamblingSession;
import com.ferox.game.content.syntax.impl.*;
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
import static com.ferox.game.world.entity.AttributeKey.USING_TRADING_POST;

/**
 * @author PVE
 * @Since augustus 26, 2020
 */
public class FifthContainerAction {

    public static void fifthAction(Player player, int interfaceId, int slot, int id) {
        boolean banking = player.getAttribOr(AttributeKey.BANKING, false);
        boolean priceChecking = player.getAttribOr(AttributeKey.PRICE_CHECKING, false);
        boolean depositBoxing = player.getAttribOr(AttributeKey.DEPOSIT_BOXING, false);

        if(PacketInteractionManager.checkItemContainerActionInteraction(player, new Item(id), slot, interfaceId, 5)) {
            return;
        }

        if(player.getRunePouch().removeFromPouch(interfaceId, id, slot,5)) {
            return;
        }

        if(player.getRunePouch().moveToRunePouch(interfaceId, id, slot,5)) {
            return;
        }

        /* Bank x */
        if (interfaceId == WITHDRAW_BANK) {
            if (banking) {
                player.setEnterSyntax(new BankX(id, slot, false));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to withdraw?");
            }
        }

        if (interfaceId == INVENTORY_STORE) {
            if (priceChecking) {
                player.setEnterSyntax(new PriceCheckX(id, slot, true));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to deposit?");
                /* Bank store x */
            } else if (banking) {
                player.setEnterSyntax(new BankX(id, slot, true));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to deposit?");
            }
        }

        if (interfaceId == PRICE_CHECKER_DISPLAY_ID) {
            player.setEnterSyntax(new PriceCheckX(id, slot, false));
            player.getPacketSender().sendEnterAmountPrompt("How many would you like to withdraw?");
        }
        if (interfaceId == GOODIE_BAG_DISPLAY_ID) {
            player.setEnterSyntax(new PriceCheckX(id, slot, false));
            player.getPacketSender().sendEnterAmountPrompt("How many would you like to withdraw?");
        }
        if (interfaceId == ShopUtility.ITEM_CHILD_ID) {
            Shop.exchange(player, id, slot, 5, true);
        }

        if (interfaceId == SHOP_INVENTORY) {
            Shop.exchange(player, id, slot, 5, false);
        }

        if (interfaceId == REMOVE_INVENTORY_ITEM) { // Duel/Trade inventory

            if (player.<Boolean>getAttribOr(USING_TRADING_POST,false)) {
                TradingPost.handleXOptionInput(player, id, slot);
            } else if (player.getStatus() == PlayerStatus.TRADING) {
                player.setEnterSyntax(new TradeX(id, slot, true));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to offer?");
            } else if (player.getStatus() == PlayerStatus.DUELING) {
                player.setEnterSyntax(new StakeX(id, slot, true));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to offer?");
            } else if (player.getStatus() == PlayerStatus.GAMBLING) {
                player.setEnterSyntax(new GambleX(id, slot, true));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to offer?");
            }
        }

        if (interfaceId == Trading.CONTAINER_INTERFACE_ID) {
            if (player.getStatus() == PlayerStatus.TRADING) {
                player.setEnterSyntax(new TradeX(id, slot, false));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
            }
        }

        if (interfaceId == Dueling.MAIN_INTERFACE_CONTAINER) {
            if (player.getStatus() == PlayerStatus.DUELING) {
                player.setEnterSyntax(new StakeX(id, slot, false));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
            }
        }

        if (interfaceId == GamblingSession.MY_ITEMS_OFFERED || interfaceId == GamblingSession.OPPONENT_ITEMS_OFFERED) {
            if (player.getStatus() == PlayerStatus.GAMBLING) {
                player.setEnterSyntax(new GambleX(id, slot, false));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
            }
        }
    }
}
