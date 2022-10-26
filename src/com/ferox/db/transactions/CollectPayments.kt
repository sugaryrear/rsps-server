package com.ferox.db.transactions

import com.ferox.GameServer
import com.ferox.db.makeQuery
import com.ferox.db.onDatabase
import com.ferox.db.query
import com.ferox.game.world.World
import com.ferox.game.world.entity.AttributeKey.TOTAL_PAYMENT_AMOUNT
import com.ferox.game.world.entity.dialogue.DialogueManager
import com.ferox.game.world.entity.dialogue.Expression
import com.ferox.game.world.entity.increment
import com.ferox.game.world.entity.mob.player.Player
import com.ferox.game.world.items.Item
import com.ferox.util.Color
import com.ferox.util.CustomItemIdentifiers.*
import com.ferox.util.NpcIdentifiers.DONATOR_STORE
import java.time.LocalDateTime

object CollectPayments {

    class DonationRow(val itemId: Int, val itemAmt: Int, val rowId: Int)

    fun Player.collectPayments() {
        // get a list of itemNumer, amt, paidAmt
        query<List<DonationRow>> {
            val list = mutableListOf<DonationRow>()
            prepareStatement(
                    connection,
                    "SELECT * FROM payments WHERE lower(player_name)=:user  AND status='Completed' AND lower(claimed)=0"
            ).apply {
                setString("user", username)
                execute()
                while (resultSet.next()) {
                    list.add(
                            DonationRow(
                                    resultSet.getInt("item_number"),
                                    resultSet.getInt("quantity"),
                                    resultSet.getInt("id")
                            )
                    )
                }
            }
            list
        }.onDatabase(GameServer.getDatabaseService()) { list ->
            if (list.isEmpty()) {
                DialogueManager.npcChat(
                        this,
                        Expression.DEFAULT,
                       DONATOR_STORE,
                        "There are no pending purchases to claim.",
                        "Wait a minute and try again, or contact an Administrator."
                )
            }
            list.forEach { row ->

                val item = row.itemId

                // each row is a reward in the table. do checks that we can fit it inside the bank before actually giving.
                var spaceFor = (!bank.contains(item) || bank.count(item) < Int.MAX_VALUE - row.itemAmt)

                // if a purchase is like 2 items in one you'll need a custom check here:
                if (row.itemId == 100_000) { // example purchase product_number
                    // in this example, purchase 100k is actually 2 items, item 1 x5 and item2 x10. here we check bank space.
                    spaceFor = (!bank.contains(100_001) || bank.count(100_001) < Int.MAX_VALUE - 5)
                            && (!bank.contains(100_002) || bank.count(100_002) < Int.MAX_VALUE - 10)
                }

                if (!spaceFor) {
                    // no space. inform user, purchase is NOT set as claimed.
                    message("Your bank was too full. Make some space and reclaim.")
                } else {
                    // now query again, setting claimed after we've confirmed they have space
                    val currentDateTime = LocalDateTime.now()
                    makeQuery {
                        prepareStatement(
                                connection,
                                "UPDATE payments SET claimed='1' WHERE id=:id"
                        ).apply {
                            setInt("id", row.rowId)
//                            setString("date", currentDateTime.toString())
//                            setString("ip", hostAddress ?: "unknown")
                            execute()
                        }
                    }.onDatabase(GameServer.getDatabaseService()) {
                        var paymentAmount = 0.0
                        when (row.itemId) {

                            FIVE_DOLLAR_BOND -> {
                                paymentAmount = 5.0 * row.itemAmt
                                //TOTAL_PAYMENT_AMOUNT.increment(this, 1.0 * paymentAmount)
                            }

                            TEN_DOLLAR_BOND -> {
                                paymentAmount = 10.0 * row.itemAmt
                                //TOTAL_PAYMENT_AMOUNT.increment(this, 1.0 * paymentAmount)

                            }

                            TWENTY_DOLLAR_BOND -> {
                                paymentAmount = 20.0 * row.itemAmt
                               // TOTAL_PAYMENT_AMOUNT.increment(this, 1.0 * paymentAmount)

                            }

                            FORTY_DOLLAR_BOND -> {
                              //  TOTAL_PAYMENT_AMOUNT.increment(this, 1.0 * paymentAmount)

                            }

                            FIFTY_DOLLAR_BOND -> {
                                TOTAL_PAYMENT_AMOUNT.increment(this, 1.0 * paymentAmount)

                            }

                            ONE_HUNDRED_DOLLAR_BOND -> {
                                TOTAL_PAYMENT_AMOUNT.increment(this, 1.0 * paymentAmount)
                            }

                            ARMOUR_MYSTERY_BOX -> {
                                paymentAmount = 1.0 * row.itemAmt
                                TOTAL_PAYMENT_AMOUNT.increment(this, 1.0 * row.itemAmt)
                            }

                            WEAPON_MYSTERY_BOX -> {
                                paymentAmount = 3.0 * row.itemAmt
                                TOTAL_PAYMENT_AMOUNT.increment(this, 3.0 * row.itemAmt)
                            }

                            DONATOR_MYSTERY_BOX -> {
                                paymentAmount = 10.0 * row.itemAmt
                                TOTAL_PAYMENT_AMOUNT.increment(this, 10.0 * row.itemAmt)
                            }

                            LEGENDARY_MYSTERY_BOX -> {
                                paymentAmount = 20.0 * row.itemAmt
                                TOTAL_PAYMENT_AMOUNT.increment(this, 20.0 * row.itemAmt)
                            }

                            EPIC_PET_BOX -> {
                                paymentAmount = 75.0 * row.itemAmt
                                TOTAL_PAYMENT_AMOUNT.increment(this, 75.0 * row.itemAmt)
                            }
                        }

                        if(GameServer.properties().mysteryPromoEnabled) {
                            val mysteryTickets = paymentAmount.toInt() / 10
                            val bloodMoneyCaskets = paymentAmount.toInt() / 50


                            if (mysteryTickets > 0) {
                                inventory.addOrBank(Item(MYSTERY_TICKET, mysteryTickets))
                                World.getWorld().sendWorldMessage("<img=1081>" + username.toString() + " just received <col=" + Color.BLUE.colorValue.toString() + ">" + mysteryTickets + " mystery tickets</col> for donating! Support us at <col=" + Color.BLUE.colorValue.toString() + ">::donate</col>!")
                            }

                            if (bloodMoneyCaskets > 0) {
                                inventory.addOrBank(Item(BLOOD_MONEY_CASKET_PROMO, bloodMoneyCaskets))
                                World.getWorld().sendWorldMessage("<img=1081>" + Color.PURPLE.wrap(username.toString()) + " just received <col=" + Color.BLUE.colorValue.toString() + ">" + bloodMoneyCaskets + " blood money casket (100-250k)</col> for donating! Support us")
                                World.getWorld().sendWorldMessage("at <col=" + Color.BLUE.colorValue.toString() + ">::donate</col>!")
                            }
                        }

                        //Buy two get one free promo
                        if (GameServer.properties().buyTwoGetOneFree) {
                            // Award a 'buy-two-get-one free' special if acceptable.
                            val bonus = row.itemAmt / 2
                            inventory.addOrBank(Item(row.itemId, row.itemAmt + bonus))
                            if (bonus > 0)
                                message("${Color.RED.tag()}You have been rewarded extra items because of our active payment deal.")
                        } else {
                            //No promo active, give the purchase items without bonus
                            inventory.addOrBank(Item(row.itemId, row.itemAmt))
                        }

                        //println(increaseForPromo)

                        if (GameServer.properties().promoEnabled) {
                            this.paymentPromo.checkForPromoReward(paymentAmount)
                        }
                    }
                }
            }
        }
    }
}
