package com.ferox.game;

import com.ferox.GameServer;
import com.ferox.game.world.InterfaceConstants;
import com.ferox.game.world.items.Item;
import com.ferox.util.ItemIdentifiers;

import static com.ferox.util.ItemIdentifiers.*;

/**
 * A class containing different attributes
 * which affect the game in different ways.
 *
 * @author Professor Oak
 */
public class GameConstants {

    public static final String VOTE_URL = "http://righteouspk.com/vote/";
    public static final String WEBSITE_URL = "http://righteouspk.com/store/index.php";
    public static final int[] BLOOD_MONEY_SHOPS = {4,52,18,67,66,51};
 public static boolean production = false;
    /**
     * Starter items for game mode.
     */
    public static final Item[] STARTER_ITEMS = {
        //Iron armour
        new Item(IRON_FULL_HELM), new Item(IRON_PLATEBODY), new Item(IRON_PLATELEGS), new Item(IRON_KITESHIELD),
        //Iron scimitar and rune scimitar
        new Item(IRON_SCIMITAR), new Item(BLACK_SCIMITAR),
        //Regular bow and bronze arrows
        new Item(SHORTBOW), new Item(BRONZE_ARROW, 500),
        //Glory (6) and climbing boots
        new Item(AMULET_OF_POWER), new Item(1061),
        //Standard runes
        new Item(FIRE_RUNE, 100), new Item(WATER_RUNE, 100), new Item(AIR_RUNE, 100), new Item(EARTH_RUNE, 100), new Item(MIND_RUNE, 100),
        //Food (Lobsters, noted)
        new Item(LOBSTER+1, 50),
        new Item(EDGEVILLE_TELEPORT,10),
        new Item(995,50_000),
        new Item(13307,50_000)


    };

    /**
     * The server currency
     */
    public static int CURRENCY = GameServer.properties().pvpMode ? BLOOD_MONEY : ItemIdentifiers.COINS_995;

    public static String CURRENCY_STRING = GameServer.properties().pvpMode ? "Blood Money" : "coins";

    public static final String SERVER_NAME = "Runescape";

    public static final int[] DONATOR_ITEMS = {DARK_CRAB, DARK_CRAB + 1, ItemIdentifiers.ANGLERFISH, ItemIdentifiers.ANGLERFISH + 1, ItemIdentifiers.SUPER_COMBAT_POTION1, ItemIdentifiers.SUPER_COMBAT_POTION2, ItemIdentifiers.SUPER_COMBAT_POTION3, ItemIdentifiers.SUPER_COMBAT_POTION4, ItemIdentifiers.SUPER_COMBAT_POTION1 + 1, ItemIdentifiers.SUPER_COMBAT_POTION2 + 1, ItemIdentifiers.SUPER_COMBAT_POTION3 + 1, ItemIdentifiers.SUPER_COMBAT_POTION4 + 1};

    /**
     * Spawnable Items
     */
    public static final int[] PVP_ALLOWED_SPAWNS = {

    };
    /**
     * minigames/bosses teleport cooldown
     */
    public static final long MINIGAMES_LENGTH = 30_000;//1 minute

    /**
     * home teleport cooldown
     */
    public static final long HOME_TELE_CD = 60_000;//1minute
    /**
     * Holds the array of all the side-bar identification and their
     * corresponding itemcontainer identification.
     */

//    public static final int[][] SIDEBAR_INTERFACE =
//    {
//        {GameConstants.ATTACK_TAB, 2423}, {GameConstants.SKILL_TAB, 10000}, {GameConstants.QUEST_TAB, 10220}, {GameConstants.INVENTORY_TAB, 3213}, {GameConstants.EQUIPMENT_TAB, 1644}, {GameConstants.PRAYER_TAB, 5608}, {GameConstants.MAGIC_TAB, 938}, //Row 1
//
//        {GameConstants.CLAN_TAB, InterfaceConstants.CLAN_CHAT}, {GameConstants.FRIENDS_TAB, 50650}, {GameConstants.IGNORE_TAB, 12650}, {GameConstants.LOGOUT_TAB, 2449}, {GameConstants.WRENCH_TAB, 42500}, {GameConstants.EMOTE_TAB, 147}, {GameConstants.MUSIC_TAB, 962} //Row 2
//    };
    public static final int[][] SIDEBAR_INTERFACE =
        {
            {GameConstants.ATTACK_TAB, 2423}, {GameConstants.SKILL_TAB, 10000}, {GameConstants.QUEST_TAB, 638}, {GameConstants.INVENTORY_TAB, 3213}, {GameConstants.EQUIPMENT_TAB, 1644}, {GameConstants.PRAYER_TAB, 5608}, {GameConstants.MAGIC_TAB, 938}, //Row 1

            {GameConstants.CLAN_TAB, InterfaceConstants.CLAN_CHAT}, {GameConstants.FRIENDS_TAB, 5065}, {GameConstants.IGNORE_TAB, 5715}, {GameConstants.LOGOUT_TAB, 2449}, {GameConstants.WRENCH_TAB, 42500}, {GameConstants.EMOTE_TAB, 147}, {GameConstants.MUSIC_TAB, 962} //Row 2
        };
    /**
     * All the tab identifications
     */
    public static final int ATTACK_TAB = 0, SKILL_TAB = 1, QUEST_TAB = 2, INVENTORY_TAB = 3, EQUIPMENT_TAB = 4,
            PRAYER_TAB = 5, MAGIC_TAB = 6, CLAN_TAB = 7, FRIENDS_TAB = 8, IGNORE_TAB = 9, LOGOUT_TAB = 10,
            WRENCH_TAB = 11, EMOTE_TAB = 12, MUSIC_TAB = 13;

    public static final int PLAYERS_LIMIT = 2047; //This must be capped to 2047 because 11 bits - 1
    public static final int NPCS_LIMIT = 16383; //This must be capped to 16384 because 14 bits - 1

    /**
     * Strings that are classified as bad
     */
    public static final String[] BAD_STRINGS = { };

    public final static int[] TAB_AMOUNT = {48, 58, 0, 0, 0, 0, 0, 0, 0, 0};
    public final static Item[] BANK_ITEMS = {
        new Item(4587, 20000), // Scim

    };
}
