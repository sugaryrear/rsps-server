package com.ferox.game.world.entity.combat.prayer.default_prayer;

import com.ferox.game.content.duel.DuelRule;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.skull.SkullType;
import com.ferox.game.world.entity.combat.skull.Skulling;
import com.ferox.game.world.entity.dialogue.DialogueManager;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.IronMode;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.container.equipment.EquipmentInfo;
import com.ferox.util.timers.TimerKey;
import org.jetbrains.annotations.Nullable;

/**
 * All of the prayers that can be activated and deactivated. This currently only
 * has support for prayers present in the <b>317 protocol</b>.
 *
 * @author Swiffy
 */
public class DefaultPrayers {

    public static boolean overheadPrayerActivated(Player player) {
        return usingPrayer(player, PROTECT_FROM_MAGIC) || usingPrayer(player, PROTECT_FROM_MISSILES) || usingPrayer(player, PROTECT_FROM_MELEE) || usingPrayer(player, RETRIBUTION) || usingPrayer(player, REDEMPTION) || usingPrayer(player, SMITE);
    }

    /**
     * Gets the protecting prayer based on the argued combat type.
     *
     * @param type the combat type.
     * @return the protecting prayer.
     */
    public static int getProtectingPrayer(@Nullable CombatType type) {
        if (type == null) return -1;
        return switch (type) {
            case MELEE -> PROTECT_FROM_MELEE;
            case MAGIC -> PROTECT_FROM_MAGIC;
            case RANGED -> PROTECT_FROM_MISSILES;
        };
    }

    public static boolean usingPrayer(Mob mob, int prayer) {
        if (prayer < 0 || prayer >= mob.getPrayerActive().length)
            return false;
        return mob.getPrayerActive()[prayer];
    }

    /**
     * Activates a prayer with specified <code>buttonId</code>.
     *
     * @param buttonId The button the player is clicking.
     */
    public static boolean togglePrayer(Player player, final int buttonId) {
        DefaultPrayerData defaultPrayerData = DefaultPrayerData.getActionButton().get(buttonId);
        if (defaultPrayerData != null) {
            if (!player.getPrayerActive()[defaultPrayerData.ordinal()])
                activatePrayer(player, defaultPrayerData.ordinal());
            else
                deactivatePrayer(player, defaultPrayerData.ordinal());
            return true;
        }
        return false;
    }

    /**
     * Activates said prayer with specified <code>prayerId</code> and de-activates
     * all non-stackable prayers.
     *
     * @param mob      The player activating prayer.
     * @param prayerId The id of the prayer being turned on, also known as the ordinal in the respective enum.
     */
    public static void activatePrayer(Mob mob, final int prayerId) {

        //Get the prayer data.
        DefaultPrayerData pd = DefaultPrayerData.getPrayerData().get(prayerId);

        //Check if it's availble
        if (pd == null) {
            return;
        }
        //System.out.println("Prayer data is not null.");
        //Check if we're already praying this prayer.
        if (mob.getPrayerActive()[prayerId]) {

            //If we are an npc, make sure our headicon
            //is up to speed.
            if (mob.isNpc()) {
                Npc npc = mob.getAsNpc();
                if (pd.getHint() != -1) {
                    int hintId = getPrayerHeadIcon(mob);
                    if (npc.getPKBotHeadIcon() != hintId) {
                        npc.setPKBotHeadIcon(hintId);
                    }
                }
            }
            return;
        }

        //If we're a player, make sure we can use this prayer.
        if (mob.isPlayer()) {
            Player player = mob.getAsPlayer();
            if (player.skills().level(Skills.PRAYER) <= 0) {
                player.getPacketSender().sendConfig(pd.getConfigId(), 0);
                player.message("You do not have enough Prayer points.");
                return;
            }
            if (!canUse(player, pd, true)) {
                return;
            }
        }

        switch (prayerId) {
            case THICK_SKIN, ROCK_SKIN, STEEL_SKIN -> resetPrayers(mob, DEFENCE_PRAYERS, prayerId);
            case BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH -> {
                resetPrayers(mob, STRENGTH_PRAYERS, prayerId);
                resetPrayers(mob, RANGED_PRAYERS, prayerId);
                resetPrayers(mob, MAGIC_PRAYERS, prayerId);
            }
            case CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES -> {
                resetPrayers(mob, ATTACK_PRAYERS, prayerId);
                resetPrayers(mob, RANGED_PRAYERS, prayerId);
                resetPrayers(mob, MAGIC_PRAYERS, prayerId);
            }
            case SHARP_EYE, HAWK_EYE, EAGLE_EYE, MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT -> {
                resetPrayers(mob, STRENGTH_PRAYERS, prayerId);
                resetPrayers(mob, ATTACK_PRAYERS, prayerId);
                resetPrayers(mob, RANGED_PRAYERS, prayerId);
                resetPrayers(mob, MAGIC_PRAYERS, prayerId);
            }
            case CHIVALRY, PIETY, RIGOUR, AUGURY -> {
                resetPrayers(mob, DEFENCE_PRAYERS, prayerId);
                resetPrayers(mob, STRENGTH_PRAYERS, prayerId);
                resetPrayers(mob, ATTACK_PRAYERS, prayerId);
                resetPrayers(mob, RANGED_PRAYERS, prayerId);
                resetPrayers(mob, MAGIC_PRAYERS, prayerId);
            }
            case PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES, PROTECT_FROM_MELEE -> resetPrayers(mob, OVERHEAD_PRAYERS, prayerId);
            case RETRIBUTION, REDEMPTION, SMITE -> resetPrayers(mob, OVERHEAD_PRAYERS, prayerId);
        }

        // No prayers currently active and we're gonna turn one on, note the tick we're starting prayer.
        if (mob.isPlayer() && hasNoPrayerOn(mob.getAsPlayer())) {
            mob.getAsPlayer().putAttrib(AttributeKey.PRAYER_ON_TICK, World.getWorld().cycleCount());
        }

        mob.setPrayerActive(prayerId, true);

        if (mob.isPlayer()) {
            Player p = mob.getAsPlayer();
            p.getPacketSender().sendConfig(pd.getConfigId(), 1);
            if (pd.getHint() != -1) {
                int hintId = getPrayerHeadIcon(mob);
                p.setHeadHint(hintId);
            }
        } else if (mob.isNpc()) {
            Npc npc = mob.getAsNpc();
            if (pd.getHint() != -1) {
                int hintId = getPrayerHeadIcon(mob);
                if (npc.getPKBotHeadIcon() != hintId) {
                    npc.setPKBotHeadIcon(hintId);
                }
            }
        }
    }

    /**
     * Checks if the player can use the specified prayer.
     */
    public static boolean canUse(Player player, DefaultPrayerData prayer, boolean msg) {
        if (player.hp() < 0 || player.locked()) {
            player.getPacketSender().sendConfig(prayer.getConfigId(), 0);
            return false;
        }
        if (player.skills().xpLevel(Skills.PRAYER) < (prayer.getRequirement())) {
            if (msg) {
                player.getPacketSender().sendConfig(prayer.getConfigId(), 0);
                player.message("You need a Prayer level of at least " + prayer.getRequirement() + " to use " + prayer.getPrayerName() + ".");
            }
            return false;
        }
        if (prayer == DefaultPrayerData.CHIVALRY && player.skills().xpLevel(Skills.DEFENCE) < 60) {
            if (msg) {
                player.getPacketSender().sendConfig(prayer.getConfigId(), 0);
                player.message("You need a Defence level of at least 60 to use Chivalry.");
            }
            return false;
        }
        if (prayer == DefaultPrayerData.PIETY && player.skills().xpLevel(Skills.DEFENCE) < 70) {
            if (msg) {
                player.getPacketSender().sendConfig(prayer.getConfigId(), 0);
                player.message("You need a Defence level of at least 70 to use Piety.");
            }
            return false;
        }
        if ((prayer == DefaultPrayerData.RIGOUR || prayer == DefaultPrayerData.AUGURY) && player.skills().xpLevel(Skills.DEFENCE) < 70) {
            if (msg) {
                player.getPacketSender().sendConfig(prayer.getConfigId(), 0);
                player.message("You need a Defence level of at least 70 to use that prayer.");
            }
            return false;
        }
        if (prayer == DefaultPrayerData.PROTECT_ITEM) {
            if (player.ironMode() == IronMode.ULTIMATE) {
                if (msg) {
                    player.getPacketSender().sendConfig(prayer.getConfigId(), 0);
                    player.message("As an Ultimate Iron Man, you cannot use the protect item prayer.");
                }
                return false;
            }

            if (Skulling.skulled(player) && player.getSkullType() == SkullType.RED_SKULL) {
                if (msg) {
                    player.getPacketSender().sendConfig(prayer.getConfigId(), 0);
                    DialogueManager.sendStatement(player, "You cannot use the Protect Item prayer with a red skull!");
                }
                return false;
            }
        }

        if (player.getTimers().has(TimerKey.OVERHEADS_BLOCKED)) {
            if (prayer == DefaultPrayerData.PROTECT_FROM_MELEE || prayer == DefaultPrayerData.PROTECT_FROM_MISSILES || prayer == DefaultPrayerData.PROTECT_FROM_MAGIC) {
                if (msg) {
                    player.getPacketSender().sendConfig(prayer.getConfigId(), 0);
                    player.message("You cannot use overhead prayers right now.");
                }
                return false;
            }
        }

        if (player.inActiveTournament() && !player.getParticipatingTournament().getConfig().canUseOverheadPrayers()) {
            boolean overhead = prayer == DefaultPrayerData.PROTECT_FROM_MELEE || prayer == DefaultPrayerData.PROTECT_FROM_MISSILES || prayer == DefaultPrayerData.PROTECT_FROM_MAGIC || prayer == DefaultPrayerData.RETRIBUTION || prayer == DefaultPrayerData.REDEMPTION || prayer == DefaultPrayerData.SMITE;

            if (overhead) {
                if (msg) {
                    player.getPacketSender().sendConfig(prayer.getConfigId(), 0);
                    player.message("Overhead prayers are disabled in this tournament.");
                }
                return false;
            }
        }

        //Prayer locks
        boolean locked = false;

        boolean preserve_unlocked = player.getAttribOr(AttributeKey.PRESERVE, false);
        boolean rigour_unlocked = player.getAttribOr(AttributeKey.RIGOUR, false);
        boolean augury_unlocked = player.getAttribOr(AttributeKey.AUGURY, false);

        if (prayer == DefaultPrayerData.PRESERVE && !preserve_unlocked
            || prayer == DefaultPrayerData.RIGOUR && !rigour_unlocked
            || prayer == DefaultPrayerData.AUGURY && !augury_unlocked) {
            locked = true;
        }

        if(player.inActiveTournament()) {
            locked = false;
        }

        if (locked) {
            if (msg) {
                player.message("You have not unlocked that Prayer yet.");
                player.getPacketSender().sendConfig(prayer.getConfigId(), 0);
            }
            return false;
        }

        //Duel, disabled prayer?
        if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_PRAYER.ordinal()]) {
            if (msg) {
                DialogueManager.sendStatement(player, "Prayer has been disabled in this duel!");
                player.getPacketSender().sendConfig(prayer.getConfigId(), 0);
            }
            return false;
        }

        return true;
    }

    /**
     * Deactivates said prayer with specified <code>prayerId</code>.
     *
     * @param mob      The player deactivating prayer.
     * @param prayerId The id of the prayer being deactivated.
     */
    public static void deactivatePrayer(Mob mob, int prayerId) {
        if (!mob.getPrayerActive()[prayerId]) {
            return;
        }
        DefaultPrayerData pd = DefaultPrayerData.getPrayerData().get(prayerId);
        mob.getPrayerActive()[prayerId] = false;
        if (mob.isPlayer()) {
            Player p = mob.getAsPlayer();
            p.getPacketSender().sendConfig(pd.getConfigId(), 0);
            if (pd.getHint() != -1) {
                int hintId = getPrayerHeadIcon(mob);
                p.setHeadHint(hintId);
            }

            p.getQuickPrayers().checkActive();
        } else if (mob.isNpc()) {
            if (pd.getHint() != -1) {
                int hintId = getPrayerHeadIcon(mob);
                if (mob.getAsNpc().getPKBotHeadIcon() != hintId) {
                    mob.getAsNpc().setPKBotHeadIcon(hintId);
                }
            }
        }
    }

    public static void closeAllPrayers(Mob mob) {
        for (DefaultPrayerData prayer : DefaultPrayerData.values()) {
            mob.getPrayerActive()[prayer.ordinal()] = false;
            mob.setHeadHint(getPrayerHeadIcon(mob));
            if(mob.isPlayer()) {
                mob.getAsPlayer().getPacketSender().sendConfig(prayer.getConfigId(), 0);
            }
        }
        if (mob.isPlayer()) {
            mob.getAsPlayer().getQuickPrayers().setEnabled(false);
            mob.getAsPlayer().getPacketSender().sendQuickPrayersState(false);
        } else if (mob.isNpc()) {
            if (mob.getAsNpc().getPKBotHeadIcon() != -1) {
                mob.getAsNpc().setPKBotHeadIcon(-1);
            }
        }
    }

    /**
     * Gets the player's current head hint if they activate or deactivate
     * a head prayer.
     *
     * @param mob The player to fetch head hint index for.
     * @return The player's current head hint index.
     */
    private static int getPrayerHeadIcon(Mob mob) {
        boolean[] prayers = mob.getPrayerActive();
        if (prayers[PROTECT_FROM_MELEE])
            return 0;
        if (prayers[PROTECT_FROM_MISSILES])
            return 1;
        if (prayers[PROTECT_FROM_MAGIC])
            return 2;
        if (prayers[RETRIBUTION])
            return 3;
        if (prayers[SMITE])
            return 4;
        if (prayers[REDEMPTION])
            return 5;
        return -1;
    }

    public static double compute(Player player) {
        double rate = 0;

        if (usingPrayer(player,THICK_SKIN))
            //12 seconds level 1
            //1188 seconds level 99
            rate += 0.083;
        if (usingPrayer(player,BURST_OF_STRENGTH))
            rate += 0.083;
        if (usingPrayer(player,CLARITY_OF_THOUGHT))
            rate += 0.083;
        if (usingPrayer(player,SHARP_EYE))
            rate += 0.083;
        if (usingPrayer(player,MYSTIC_WILL))
            rate += 0.083;
        if (usingPrayer(player,ROCK_SKIN))
            rate += 0.17;
        if (usingPrayer(player,SUPERHUMAN_STRENGTH))
            rate += 0.17;
        if (usingPrayer(player,IMPROVED_REFLEXES))
            rate += 0.17;
        if (usingPrayer(player,RAPID_RESTORE))
            rate += 0.04;
        if (usingPrayer(player,RAPID_HEAL))
            rate += 0.06;
        if (usingPrayer(player,PROTECT_ITEM))
            rate += 0.06;
        if (usingPrayer(player,HAWK_EYE))
            rate += 0.17;
        if (usingPrayer(player,MYSTIC_LORE))
            rate += 0.17;
        if (usingPrayer(player,STEEL_SKIN))
            rate += 0.33;
        if (usingPrayer(player,ULTIMATE_STRENGTH))
            rate += 0.33;
        if (usingPrayer(player,INCREDIBLE_REFLEXES))
            rate += 0.33;
        if (usingPrayer(player,PROTECT_FROM_MELEE))
            rate += 0.33;
        if (usingPrayer(player,PROTECT_FROM_MAGIC))
            rate += 0.33;
        if (usingPrayer(player,PROTECT_FROM_MISSILES))
            rate += 0.33;
        if (usingPrayer(player,EAGLE_EYE))
            rate += 0.33;
        if (usingPrayer(player,MYSTIC_MIGHT))
            rate += 0.33;
        if (usingPrayer(player,RETRIBUTION))
            rate += 0.08;
        if (usingPrayer(player,REDEMPTION))
            rate += 0.17;
        if (usingPrayer(player,SMITE))
            rate += 0.56;
        if (usingPrayer(player,PRESERVE))
            rate += 0.06;
        if (usingPrayer(player,CHIVALRY))
            rate += 0.56;
        if (usingPrayer(player,PIETY))
            rate += 0.67;
        if (usingPrayer(player,RIGOUR))
            rate += 0.67;
        if (usingPrayer(player,AUGURY))
            rate += 0.67;

        //System.out.println("rate: "+rate);
        return rate;
    }

    public static void onLogin(Player player) {
        player.getTimers().addOrSet(TimerKey.PRAYER_TICK, 1); // Drain 1 tick later.
    }

    public static void drainPrayer(Player player) {
        // Drain this tick, and reset the timer to 1 to drain next tick. Forever and ever and evvvvvvvver
        player.getTimers().extendOrRegister(TimerKey.PRAYER_TICK, 1);

        if (player.getTimers().has(TimerKey.PRAYER_TICK)) {

            // Dont drain if dead, or no prayers on.
            if (player.dead() || hasNoPrayerOn(player) ||
                World.getWorld().cycleCount() <= player.<Integer>getAttribOr(AttributeKey.PRAYER_ON_TICK, 0)) {
                player.putAttrib(AttributeKey.PRAYERINCREMENT, 0D); // reset
                return;
            }
            //player.message(String.format("on:%s now:%s drain:%s", player.<Integer>getAttrib(AttributeKey.PRAYER_ON_TICK), World.getWorld().getElapsedTicks(), player.<Integer>getAttrib(AttributeKey.PRAYERINCREMENT)));
            double drain = compute(player);
            if (drain > 0) {
                int pray = EquipmentInfo.prayerBonuses(player, World.getWorld().equipmentInfo());
                //player.debugMessage(String.format("drain: %f  bonus:%d  saved:%f", drain, pray, pray < 1 ? 0.0 : (drain / (1 + (0.0333 * pray)))));
                if (pray > 0) {
                    // slows the drain rate by 3.33% of the regular drain rate of the Prayer(s)
                    drain /= 1 + (0.0333 * pray);
                }

                if (player.skills().level(Skills.PRAYER) > 0) {
                    boolean inf_pray = player.getAttribOr(AttributeKey.INF_PRAY, false);
                    if (!inf_pray) {
                        double totalDrains = player.getAttribOr(AttributeKey.PRAYERINCREMENT, 0.0D);
                        player.putAttrib(AttributeKey.PRAYERINCREMENT, totalDrains + drain);
                        if (totalDrains > 1.0) {
                            player.putAttrib(AttributeKey.PRAYERINCREMENT, totalDrains - 1);
                            player.skills().setLevel(Skills.PRAYER, Math.max(0, player.skills().level(Skills.PRAYER) - 1));

                        }
                    }
                }

                if (player.skills().level(Skills.PRAYER) < 1) {
                    // Cant get smited when dead dead, you must be smited before like RS.
                    closeAllPrayers(player);
                    player.message("You have run out of prayer points, you must recharge at an altar.");
                }
            }
        }
    }

    public static boolean hasNoPrayerOn(Player player) {
        int prayersOn = 0;
        for (int i = 0; i < player.getPrayerActive().length; i++) {
            if (player.getPrayerActive()[i])
                prayersOn++;
        }
        return prayersOn == 0;
    }

    /**
     * Resets <code> prayers </code> with an exception for <code> prayerID </code>
     *
     * @param prayers  The array of prayers to reset
     * @param prayerID The prayer ID to not turn off (exception)
     */
    public static void resetPrayers(Mob mob, int[] prayers, int prayerID) {
        for (int prayer : prayers) {
            if (prayer != prayerID)
                deactivatePrayer(mob, prayer);
        }
    }

    /**
     * Resets prayers in the array
     */
    public static void resetPrayers(Player player, int[] prayers) {
        for (int prayer : prayers) {
            deactivatePrayer(player, prayer);
        }
    }

    /**
     * Checks if action button ID is a prayer button.
     *
     * @param actionButtonID action button being hit.
     */
    public static boolean isButton(final int actionButtonID) {
        return DefaultPrayerData.getActionButton().containsKey(actionButtonID);
    }

    public static final int THICK_SKIN = 0, BURST_OF_STRENGTH = 1, CLARITY_OF_THOUGHT = 2, SHARP_EYE = 3, MYSTIC_WILL = 4,
        ROCK_SKIN = 5, SUPERHUMAN_STRENGTH = 6, IMPROVED_REFLEXES = 7, RAPID_RESTORE = 8, RAPID_HEAL = 9,
        PROTECT_ITEM = 10, HAWK_EYE = 11, MYSTIC_LORE = 12, STEEL_SKIN = 13, ULTIMATE_STRENGTH = 14,
        INCREDIBLE_REFLEXES = 15, PROTECT_FROM_MAGIC = 16, PROTECT_FROM_MISSILES = 17,
        PROTECT_FROM_MELEE = 18, EAGLE_EYE = 19, MYSTIC_MIGHT = 20, RETRIBUTION = 21, REDEMPTION = 22, SMITE = 23, PRESERVE = 24,
        CHIVALRY = 25, PIETY = 26, RIGOUR = 27, AUGURY = 28;

    /**
     * Contains every prayer that counts as a defense prayer.
     */
    public static final int[] DEFENCE_PRAYERS = {THICK_SKIN, ROCK_SKIN, STEEL_SKIN, CHIVALRY, PIETY, RIGOUR, AUGURY};

    /**
     * Contains every prayer that counts as a strength prayer.
     */
    public static final int[] STRENGTH_PRAYERS = {BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, CHIVALRY, PIETY};

    /**
     * Contains every prayer that counts as an attack prayer.
     */
    public static final int[] ATTACK_PRAYERS = {CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY, PIETY};

    /**
     * Contains every prayer that counts as a ranged prayer.
     */
    public static final int[] RANGED_PRAYERS = {SHARP_EYE, HAWK_EYE, EAGLE_EYE, RIGOUR};

    /**
     * Contains every prayer that counts as a magic prayer.
     */
    public static final int[] MAGIC_PRAYERS = {MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT, AUGURY};

    /**
     * Contains every prayer that counts as an overhead prayer, excluding protect from summoning.
     */
    public static final int[] OVERHEAD_PRAYERS = {PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES, PROTECT_FROM_MELEE, RETRIBUTION, REDEMPTION, SMITE};

    /**
     * Contains every protection prayer
     */
    public static final int[] PROTECTION_PRAYERS = {PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES, PROTECT_FROM_MELEE};
}
