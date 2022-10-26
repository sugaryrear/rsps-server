package com.ferox.game.content.packet_actions.interactions.items;

import com.ferox.game.content.EffectTimer;
import com.ferox.game.content.areas.dungeons.godwars.FrozenKey;
import com.ferox.game.content.areas.wilderness.content.activity.ActivityRewardsHandler;
import com.ferox.game.content.collection_logs.LogType;
import com.ferox.game.content.consumables.FoodConsumable;
import com.ferox.game.content.consumables.potions.Potions;
import com.ferox.game.content.duel.DuelRule;
import com.ferox.game.content.items.MithrilSeeds;
import com.ferox.game.content.items.Ornatejewelrybox;
import com.ferox.game.content.items.RockCake;
import com.ferox.game.content.items.mystery_box.MboxItem;
import com.ferox.game.content.items.mystery_box.MysteryBox;
import com.ferox.game.content.items.tools.ItemPacks;
import com.ferox.game.content.skill.impl.herblore.Cleaning;
import com.ferox.game.content.skill.impl.hunter.Hunter;
import com.ferox.game.content.skill.impl.hunter.HunterItemPacks;
import com.ferox.game.content.skill.impl.hunter.trap.impl.Birds;
import com.ferox.game.content.skill.impl.hunter.trap.impl.Chinchompas;
import com.ferox.game.content.skill.impl.slayer.content.ImbuedHeart;
import com.ferox.game.content.skill.impl.woodcutting.BirdNest;
import com.ferox.game.content.treasure.TreasureRewardCaskets;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.combat.bountyhunter.dialogue.TeleportToTargetScrollD;
import com.ferox.game.world.entity.mob.player.MagicSpellbook;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.QuestTab;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.net.packet.interaction.PacketInteractionManager;
import com.ferox.util.Color;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.Utils;
import com.ferox.util.timers.TimerKey;

import java.util.Optional;

import static com.ferox.game.world.entity.AttributeKey.VIEWING_RUNE_POUCH_I;
import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;

public class ItemActionOne {

    public static void click(Player player, Item item) {
        int id = item.getId();
        if (PacketInteractionManager.checkItemInteraction(player, item, 1)) {
            return;
        }
        if (TreasureRewardCaskets.openCasket(player, item)) {
            return;
        }
        if(FrozenKey.onItemOption1(player, item)) {
            return;
        }
        if(Potions.onItemOption1(player, item)) {
            return;
        }

        if(BirdNest.onItemOption1(player, item)) {
            return;
        }

        if (FoodConsumable.onItemOption1(player, item)) {
            return;
        }

        if(MithrilSeeds.onItemOption1(player, item)) {
            return;
        }

        if(RockCake.onItemOption1(player, item)) {
            return;
        }

        if(ItemPacks.open(player, item)) {
            return;
        }

        if(Cleaning.onItemOption1(player, item)) {
            return;
        }

        if(HunterItemPacks.onItemOption1(player, item)) {
            return;
        }

        if(id == BLOOD_MONEY_CASKET) {
            int amt = World.getWorld().random(5_000, 50_000);
            var blood_reaper = player.hasPetOut("Blood Reaper pet");
            if(blood_reaper) {
                int extraBM = amt * 10 / 100;
                amt += extraBM;
            }
            player.inventory().remove(new Item(BLOOD_MONEY_CASKET));
            player.inventory().add(new Item(BLOOD_MONEY, amt));
            player.message(Color.PURPLE.wrap("You've received x "+Utils.formatNumber(amt)+" blood money from the casket!"));
            return;
        }
        if(id == 30329) {
       player.getScratchCard().open();
            return;
        }
        if(id == BLOOD_MONEY_CASKET_PROMO) {
            int amt = World.getWorld().random(100_000, 250_000);
            var blood_reaper = player.hasPetOut("Blood Reaper pet");
            if(blood_reaper) {
                int extraBM = amt * 10 / 100;
                amt += extraBM;
            }
            player.inventory().remove(new Item(BLOOD_MONEY_CASKET_PROMO));
            player.inventory().add(new Item(BLOOD_MONEY, amt));
            player.message(Color.PURPLE.wrap("You've received x "+Utils.formatNumber(amt)+" blood money from the casket!"));
            return;
        }

        if(id == TARGET_TELEPORT_SCROLL) {
            boolean alreadyClaimed = player.getAttribOr(AttributeKey.BOUNTY_HUNTER_TARGET_TELEPORT_UNLOCKED, false);
            if (alreadyClaimed) {
                player.message("You already know this spell.");
                return;
            }
            if (player.inventory().contains(TARGET_TELEPORT_SCROLL)) {
                player.getDialogueManager().start(new TeleportToTargetScrollD());
            }
            return;
        }

        if (id == ItemIdentifiers.COLLECTION_LOG) {
            player.getCollectionLog().open(LogType.BOSSES);
            return;
        }

        if(id == ItemIdentifiers.IMBUED_HEART) {
            ImbuedHeart.activate(player);
            return;
        }

        if(id == VOTE_TICKET) {
            if(!player.inventory().contains(VOTE_TICKET)) {
                return;
            }

            if(WildernessArea.inWilderness(player.tile())) {
                player.message("You cannot exchange vote points in the wilderness.");
                return;
            }

            int amount = player.inventory().count(VOTE_TICKET);
            int current = player.getAttribOr(AttributeKey.VOTE_POINS, 0);

            player.putAttrib(AttributeKey.VOTE_POINS, current + amount);
            player.getPacketSender().sendString(QuestTab.InfoTab.VOTE_POINTS.childId, QuestTab.InfoTab.INFO_TAB.get(QuestTab.InfoTab.VOTE_POINTS.childId).fetchLineData(player));
            player.inventory().remove(new Item(VOTE_TICKET, amount), true);
            player.message("You exchange " + Color.BLUE.tag() + "" + Utils.formatNumber(amount) + " vote points</col>.");
            return;
        }

        if (id == VENGEANCE_SKULL) {
            if(player.getSpellbook() != MagicSpellbook.LUNAR) {
                player.message("You can only use the vengeance cast on the lunars spellbook.");
                return;
            }
            boolean hasVengeance = player.getAttribOr(AttributeKey.VENGEANCE_ACTIVE, false);
            if (player.skills().level(Skills.DEFENCE) < 40) {
                player.message("You need 40 Defence to use Vengence.");
            } else if (player.skills().level(Skills.MAGIC) < 94) {
                player.message("Your Magic level is not high enough to use this spell.");
            } else if (hasVengeance) {
                player.message("You already have Vengeance casted.");
            } else if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_MAGIC.ordinal()]) {
                player.message("Magic is disabled for this duel.");
            } else if (!player.getTimers().has(TimerKey.VENGEANCE_COOLDOWN)) {
                if (!player.inventory().contains(964)) {
                    return;
                }
                player.getTimers().register(TimerKey.VENGEANCE_COOLDOWN, 50);
                player.putAttrib(AttributeKey.VENGEANCE_ACTIVE, true);
                player.animate(8316);
                player.graphic(726);
                player.sound(2907);
                player.getPacketSender().sendEffectTimer(30, EffectTimer.VENGEANCE).sendMessage("You now have Vengeance's effect.");
            } else {
                player.message("You can only cast vengeance spells every 30 seconds.");
            }
            return;
        }

        if (id == 10006) {
            Hunter.lay(player, new Birds(player));
            return;
        }

        if (id == 10008) {
            Hunter.lay(player, new Chinchompas(player));
            return;
        }

        /* Looting bag. */
        if (id == 11941 || id == 22586) {
            player.getLootingBag().openAndCloseBag(id);
            return;
        }
        if (id == RUNE_POUCH) {
            player.getRunePouch().open(RUNE_POUCH);
            player.putAttrib(VIEWING_RUNE_POUCH_I,false);
            return;
        }

        if (id == RUNE_POUCH_I) {
            player.getRunePouch().open(RUNE_POUCH_I);
            player.putAttrib(VIEWING_RUNE_POUCH_I,true);
            return;
        }

        if(id == ACTIVITY_CASKET_3) {
            ActivityRewardsHandler.open(player);
            return;
        }

        switch (id) {
            case MYSTERY_BOX, ARMOUR_MYSTERY_BOX, WEAPON_MYSTERY_BOX, DONATOR_MYSTERY_BOX, HWEEN_MYSTERY_BOX, LEGENDARY_MYSTERY_BOX -> {
                if (player.inventory().contains(item)) {
                    player.inventory().remove(new Item(item), true);

                    Optional<MysteryBox> mBox = MysteryBox.getMysteryBox(item.getId());

                    if (mBox.isEmpty()) {
                        return;
                    }

                    player.getMysteryBox().box = mBox.get();
                    MboxItem mboxItem = mBox.get().rollReward(false).copy();
                    player.getMysteryBox().reward = mboxItem;
                    player.getMysteryBox().broadcast = mboxItem.broadcastItem;
                    player.getMysteryBox().reward();
                }
            }
        }
    }
}
