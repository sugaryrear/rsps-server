package com.ferox.fs;

import com.ferox.game.GameConstants;
import com.ferox.game.world.definition.BloodMoneyPrices;
import com.ferox.game.world.items.Item;
import com.ferox.io.RSBuffer;
import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.ItemIdentifiers;
import io.netty.buffer.Unpooled;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;

/**
 * Created by Bart Pelle on 10/4/2014.
 */
public class ItemDefinition implements Definition {

    public boolean isNote() {
        return notelink != -1 && noteModel != -1;
    }

    public int resizey;
    public int xan2d;
    public int cost = 1;
    public int inventoryModel;
    public int resizez;
    public short[] recol_s;
    public short[] recol_d;
    public String name = "null";
    public int zoom2d = 2000;
    public int yan2d;
    public int zan2d;
    public int yof2d;
    private boolean stackable;
    public int[] countco;
    public boolean members = false;
    public String[] options = new String[5];
    public String[] ioptions = new String[5];
    public int maleModel0;
    public int maleModel1;
    public short[] retex_s;
    public short[] retex_d;
    public int femaleModel1;
    public int maleModel2;
    public int xof2d;
    public int manhead;
    public int manhead2;
    public int womanhead;
    public int womanhead2;
    public int[] countobj;
    public int femaleModel2;
    public int notelink;
    public int femaleModel0;
    public int resizex;
    public int noteModel;
    public int ambient;
    public int contrast;
    public int team;
    public boolean grandexchange;
    public boolean unprotectable;
    public boolean dummyitem;
    public int placeheld = -1;
    public int pheld14401 = -1;
    public int shiftClickDropType = -1;
    private int op139 = -1;
    private int op140 = -1;

    public int id;

    // our fields: optimized speed so you dont need 1k loops
    public boolean isCrystal;
    public boolean tradeable_special_items;
    public boolean changes;
    public boolean autoKeptOnDeath;
    public BloodMoneyPrices bm;
    public boolean pvpAllowed = false;

    public ItemDefinition(int id, byte[] data) {
        this.id = id;

        if (data != null && data.length > 0)
            decode(new RSBuffer(Unpooled.wrappedBuffer(data)));
        custom();
    }

    void decode(RSBuffer buffer) {
        while (true) {
            int op = buffer.readUByte();
            if (op == 0)
                break;
            decode(buffer, op);
        }
        postDecode(id);
    }
    public static final int[][] custom_prices = new int[][]{
        {19625, 500},   {8008, 500},   {8007, 500},  {8009, 500},  {8010, 500}
    };


    void custom() {
        if (id == ItemIdentifiers.TOXIC_BLOWPIPE || id == ItemIdentifiers.SERPENTINE_HELM || id == ItemIdentifiers.TRIDENT_OF_THE_SWAMP || id == ItemIdentifiers.TOXIC_STAFF_OF_THE_DEAD
            || id == ItemIdentifiers.TOME_OF_FIRE || id == ItemIdentifiers.SCYTHE_OF_VITUR || id == ItemIdentifiers.SANGUINESTI_STAFF || id == ItemIdentifiers.CRAWS_BOW
            || id == ItemIdentifiers.VIGGORAS_CHAINMACE || id == ItemIdentifiers.THAMMARONS_SCEPTRE || id == ItemIdentifiers.TRIDENT_OF_THE_SEAS || id == ItemIdentifiers.MAGMA_HELM
            || id == ItemIdentifiers.TANZANITE_HELM || id == ItemIdentifiers.DRAGONFIRE_SHIELD || id == ItemIdentifiers.DRAGONFIRE_WARD || id == ItemIdentifiers.ANCIENT_WYVERN_SHIELD
            || id == ItemIdentifiers.ABYSSAL_TENTACLE || id == BARRELCHEST_ANCHOR || id == ItemIdentifiers.SARADOMINS_BLESSED_SWORD) {
            ioptions = new String[]{null, "Wield", null, null, "Drop"};
        }
        for (int i = 0; i < custom_prices.length; i++)
            if ((custom_prices[i][0]) == id)
                cost = custom_prices[i][1];
        boolean replace_drop_with_destroy = Arrays.stream(Item.AUTO_KEPT_LIST).anyMatch(auto_kept_id -> auto_kept_id == id);

        if (replace_drop_with_destroy) {
            ioptions = new String[]{null, null, null, null, "Destroy"};
        }

        int[] untradeables_with_destroy = new int[]{
            VOLATILE_NIGHTMARE_STAFF,
            HARMONISED_NIGHTMARE_STAFF,
            ELDRITCH_NIGHTMARE_STAFF,
        };

        if (IntStream.of(untradeables_with_destroy).anyMatch(untradeable -> id == untradeable)) {
            ioptions = new String[]{null, null, null, null, "Destroy"};
        }

        if (name.contains("slayer helmet") || name.contains("Slayer helmet")) {
            ioptions = new String[]{null, "Wear", null, null, "Drop"};
        }
        if (id == 4502) {
          cost = 100;
        }
        if (id == 4675) {
            cost = 2000;
        }
        if (id == 23413) {
            cost = 100;
        }
        //Bounty hunter emblem hardcoding.
        if (id == 12746 || (id >= 12748 && id <= 12756)) {
            unprotectable = true;
        } else if (id == 4564) {
            stackable = true;
        } else if (id == ANCIENT_GODSWORD) {
            name = "Ancient godsword";
        } else if (id == FROZEN_KEY_26356) {
            name = "Frozen key";
        } else if (id == ZARYTE_CROSSBOW) {
            name = "Zaryte crossbow";
            cost =450000000;
        } else if (id == ZARYTE_VAMBRACES) {
            name = "Zaryte vambraces";
            grandexchange = true;
        } else if (id == NIHIL_HORN) {
            name = "Nihil horn";
            grandexchange = true;
        } else if (id == ANCIENT_HILT) {
            name = "Ancient hilt";
            grandexchange = true;

        } else if (id == NIHIL_SHARD) {
            name = "Nihil shard";
            grandexchange = true;
            stackable = true;
        } else if (id == TORVA_FULL_HELM_DAMAGED) {
            name = "Torva full helm (damaged)";
        } else if (id == TORVA_PLATEBODY_DAMAGED) {
            name = "Torva platebody(damaged)";
        } else if (id == TORVA_PLATELEGS_DAMAGED) {
            name = "Torva platelegs (damaged)";
        } else if (id == 30244) {
            name = "Revenant mystery box";
        } else if (id == NEXLING) {
            name = "Nexling";
        } else if (id == BANDOSIAN_COMPONENTS) {
            name = "Bandosian component";
//        } else if (id == 30224) {
//            name = "Grim h'ween mask";
//        } else if (id == 30225) {
//            name = "Grim partyhat";
//        } else if (id == 30226) {
//            name = "Grim scythe";
//        } else if (id == 30227) {
//            name = "H'ween mystery chest";
//        } else if (id == 30228) {
//            name = "Haunted hellhound pet";
//        } else if (id == 30229) {
//            name = "H'ween armadyl godsword";
//        } else if (id == 30230) {
//            name = "H'ween blowpipe";
//        } else if (id == 30231) {
//            name = "H'ween dragon claws";
//        } else if (id == 30232) {
//            name = "H'ween craw's bow";
//        } else if (id == 30233) {
//            name = "H'ween chainmace";
//        } else if (id == 30234) {
//            name = "H'ween granite maul";
//        } else if (id == 30239) {
//            name = "Haunted sled";
//        } else if (id == 30240) {
//            name = "Haunted crossbow";
//        } else if (id == 30241) {
//            name = "Haunted dragonfire shield";
        } else if (id == 30222) {
            name = "Mystery ticket";
//            stackable = true;
        } else if (id == 30223) {
            name = "Blood money casket (100-250k)";
//        } else if (id == 30315) {
//            name = "Darklord bow";
//        } else if (id == 30309) {
//            name = "Darklord sword";
//        } else if (id == 30312) {
//            name = "Darklord staff";
//        } else if (id == 16475) {
//            name = "Activity casket";
        } else if (id == 3269) {
            name = "Slayer key";
            stackable = true;
        } else if (id == -1) {
            name = "Slayer key";
        } else if(id == 3325) {
            notelink = -1;
            noteModel = -1;
//        } else if(id == 28013) {
//            name = "Veteran partyhat";
//            ioptions = new String[]{null, "Wear", null, null, "Destroy"};
//        } else if(id == 28014) {
//            name = "Veteran halloween mask";
//            ioptions = new String[]{null, "Wear", null, null, "Destroy"};
//        } else if(id == 28015) {
//            name = "Veteran santa hat";
//            ioptions = new String[]{null, "Wear", null, null, "Destroy"};
//        } else if(id == 14479) {
//            name = "Beginner weapon pack";
//            ioptions = new String[]{"Open", null, null, null, "Destroy"};
//        } else if(id == 14486) {
//            name = "Beginner dragon claws";
//            ioptions = new String[]{null, "Wield", null, null, "Destroy"};
//        } else if(id == 14487) {
//            name = "Beginner AGS";
//            ioptions = new String[]{null, "Wield", null, null, "Destroy"};
//        } else if(id == 14488) {
//            name = "Beginner chainmace";
//            ioptions = new String[]{null, "Wield", null, null, "Destroy"};                                        `
//        } else if(id == 14489) {
//            name = "Beginner craw's bow";
//            ioptions = new String[]{null, "Wield", null, null, "Destroy"};
//        } else if(id == 28663) {
//            name = "Zriawk pet";
        } else if(id == 786) {
            name = "Gambler scroll";
        } else if(id == 24937) {
            name = "Fawkes pet";
        } else if(id == 24937) {
            name = "Nexling";
        } else if(id == 24938) {
            name = "Void knight gloves";
        } else if(id == 24939) {
            name = "Void ranger helm";
        } else if(id == 24940) {
            name = "Void mage helm";
        } else if(id == 24941) {
            name = "Void melee helm";
        } else if(id == 24942) {
            name = "Elite void robe";
        } else if(id == 24943) {
            name = "Elite void top";
//        } else if(id == 24949) {
//            name = "Dragon dagger(p++)";
//            stackable = false;
//        } else if(id == 24948) {
//            name = "Abyssal tentacle";
//        } else if(id == 24947) {
//            name = "Spiked manacles";
//        } else if(id == 24946) {
//            name = "Fremennik kilt";
//        } else if(id == 24945) {
//            name = "Partyhat & specs";
//        } else if(id == 24944) {
//            name = "Granite maul";
        } else if(id == 15304) {
            name = "Ring of vigour";
        } else if(id == 26500) {
            name = "Dark bandos chestplate";
        } else if(id == 26501) {
            name = "Dark bandos tassets";
        } else if(id == 26502) {
            name = "Dark armadyl helmet";
        } else if(id == 26503) {
            name = "Dark armadyl chestplate";
        } else if(id == 26504) {
            name = "Dark armadyl chainskirt";
        } else if(id == 27004) {
            name = "Blood money pet";
        } else if(id == 27005) {
            name = "Ring of elysian";
//        } else if(id == 27006) {
//            name = "Toxic staff of the dead (c)";
//        } else if(id == 27000) {
//            name = "Kerberos pet";
//        } else if(id == 27001) {
//            name = "Skorpios pet";
//        } else if(id == 27002) {
//            name = "Arachne pet";
//        } else if(id == 27003) {
//            name = "Artio pet";
//        } else if(id == 22517) {
//            name = "Saeldor shard";
        } else if(id == 25887 || id == 25866) {
            name = "Bow of faerdhinen";
        } else if(id == 25881) {
            name = "Blade of saeldor (t)";
//        } else if(id == 25916) {
//            name = "Dragon hunter crossbow (t)";
//        } else if(id == 25936) {
//            name = "Pharaoh's hilt";
//        } else if(id == 24950) {
//            name = "Cyan partyhat";
//        } else if(id == 24951) {
//            name = "Lime partyhat";
//        } else if(id == 24952) {
//            name = "Orange partyhat";
//        } else if(id == 24953) {
//            name = "White h'ween mask";
//        } else if(id == 24954) {
//            name = "Purple h'ween mask";
//        } else if(id == 24955) {
//            name = "Lime green h'ween mask";
//        } else if(id == 24956 || id == 24958) {
//            name = "Dark elder maul";
        } else if(id == 28957) {
            name = "Sanguine twisted bow";
        } else if(id == 24984) {
            name = "Ancient faceguard";
        } else if(id == 26382) {
            name = "Torva full helm";
            cost = 266_000_000;
        } else if(id == 26384) {
            name = "Torva platebody";
            cost = 413_000_000;
        } else if(id == 26386) {
            name = "Torva platelegs";
            cost = 461_000_000;
//        } else if(id == 24985) {
//            name = "Ancient warrior clamp";
//        } else if(id == 24986) {
//            name = "Ancient king black dragon pet";
//        } else if(id == 24987) {
//            name = "Ancient chaos elemental pet";
//        } else if(id == 24988) {
//            name = "Ancient barrelchest pet";
//        } else if(id == 24989) {
//            name = "Dark ancient emblem";
//        } else if(id == 24990) {
//            name = "Dark ancient totem";
//        } else if(id == 24991) {
//            name = "Dark ancient statuette";
//        } else if(id == 24992) {
//            name = "Dark ancient medallion";
//        } else if(id == 24993) {
//            name = "Dark ancient effigy";
//        } else if(id == 24994) {
//            name = "Dark ancient relic";
//        } else if(id == 24995) {
//            name = "Ancient vesta's longsword";
//        } else if(id == 24996) {
//            name = "Ancient statius's warhammer";
        } else if (id == 24999) {
            name = "Blood money casket (5-50k)";
            ioptions = new String[]{"Open", null, null, null, "Drop"};
//        } else if (id == 28000) {
//            name = "Blood firebird pet";
        } else if (id == 28001) {
            name = "Shadow mace";
        } else if (id == 28002) {
            name = "Shadow great helm";
        } else if (id == 28003) {
            name = "Shadow hauberk";
        } else if (id == 28004) {
            name = "Shadow plateskirt";
        } else if (id == 28005) {
            name = "Shadow inquisitor ornament kit";
        } else if (id == 28006) {
            name = "Inquisitor's mace ornament kit";
        } else if (id == 29000) {
            name = "Viggora's chainmace (c)";
        } else if (id == 29001) {
            name = "Craw's bow (c)";
        } else if (id == 29002) {
            name = "Thammaron's sceptre (c)";
        } else if (id == 30180) {
            name = "Pegasian boots (or)";
        } else if (id == 30182) {
            name = "Eternal boots (or)";
        } else if (id == 24780) {
            name = "Amulet of blood fury";
        } else if (id == 25731) {
            name = "Holy sanguinesti staff";
        } else if (id == 25734) {
            name = "Holy ghrazi rapier";
        } else if (id == 25736) {
            name = "Holy scythe of vitur";
        } else if (id == 25739) {
            name = "Sanguine scythe of vitur";
//        } else if (id == 25753) {
//            name = "99 lamp";
//        } else if (id == 17000) {
//            name = GameConstants.SERVER_NAME + " coins";
//            countco = new int[]{2, 3, 4, 5, 25, 100, 250, 1000, 10000, 0};
//            countobj = new int[]{17001, 17002, 17003, 17004, 17005, 17006, 17007, 17008, 17009, 0};
//            stackable = true;
        } else if (id == 23490) {
            name = "Larran's key tier I";
            ioptions = new String[]{null, null, null, null, "Drop"};
            countco = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            countobj = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        } else if (id == 14900) {
            name = "Larran's key tier II";
            stackable = true;
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 14901) {
            name = "Larran's key tier III";
            stackable = true;
            ioptions = new String[]{null, null, null, null, "Drop"};
//        } else if (id == 25902 || id == 25907 || id == 24445 || id == 25913) {
//            name = "Twisted slayer helmet (i)";
//            ioptions = new String[]{null, "Wear", null, null, "Drop"};
//        } else if (id == 12791) {
//            ioptions = new String[]{"Open", null, "Quick-Fill", "Empty", "Drop"};
        } else if (id == 14525) {
            name = "Mystery chest";
//        } else if (id == 20238) {
//            name = "Imbuement scroll";
//            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == BIG_CHEST) {
            name = "Blood money chest";
        } else if (id == 7956) {
            name = "Small blood money casket";
        } else if (id == 13302) {
            name = "Wilderness key";
            ioptions = new String[]{null, null, null, null, "Drop"};
//        } else if (id == 12646) {
//            name = "Niffler pet";
//        } else if (id == 20693) {
//            name = "Fawkes pet";
//        } else if (id == 619) {
//            name = "Vote ticket";
//            stackable = true;
//            ioptions = new String[]{"Convert to Points", null, null, null, "Drop"};
        } else if (id == 13188) {
            name = "Dragon claws (or)";
//        } else if (id == 28007) {
//            name = "Ethereal partyhat";
//        } else if (id == 28008) {
//            name = "Ethereal halloween mask";
//        } else if (id == 28009) {
//            name = "Ethereal santa hat";
        } else if (id == 30074) {
            name = "Lava d'hide coif";
        } else if (id == 30077) {
            name = "Lava d'hide body";
        } else if (id == 30080) {
            name = "Lava d'hide chaps";
        } else if (id == 30183) {
            name = "Twisted bow (i)";
        } else if (id == 30175) {
            name = "Ancestral hat (i)";
        } else if (id == 30177) {
            name = "Ancestral robe top (i)";
        } else if (id == 30179) {
            name = "Ancestral robe bottom (i)";
        } else if (id == 30038) {
            name = "Primordial boots (or)";
        } else if (id == 23650) {
            notelink = -1;
            noteModel = -1;
            name = "Rune pouch (i)";
            ioptions = new String[]{"Open", null, null, "Empty", "Destroy"};
        } else if (id == 4447) {
            name = "Double drops lamp";
        } else if (id == 6199) {
            ioptions = new String[]{"Quick-open", null, "Open", null, null};
        } else if (id == 16451) {
            name = "Weapon Mystery Box";
            stackable = false;
        } else if (id == 16452) {
            name = "Armour Mystery Box";
           stackable = false;
//        } else if (id == 30185) {
//            name = "Donator Mystery Box";
//            stackable = false;
//        } else if (id == 30186) {
//            name = "H'ween Mystery Box";
//            stackable = false;
//        } else if (id == 30242) {
//            name = "H'ween item casket";
        } else if (id == 16454) {
            name = "Legendary Mystery Box";
            stackable = false;
        } else if (id == 16455) {
            name = "Grand Mystery Box";
            stackable = false;
        } else if (id == 16456) {
            name = "Pet Mystery Box";
            stackable = false;
        } else if (id == 16458) {
            name = "Epic Pet Mystery Box";
            stackable = false;
        } else if (id == 16459) {
            name = "Raids Mystery Box";
            stackable = false;
//        } else if (id == 16460) {
//            name = "Zenyte Mystery Box";
//            stackable = false;
//        } else if (id == 16461) {
//            name = "Starter Box";
//            stackable = false;
//        } else if (id == 16462) {
//            name = "Clan Box";
//            stackable = false;
//        } else if (id == 6722) {
//            name = "Zombies champion pet";
        } else if (id == 2866) {
            name = "Earth arrows";
        } else if (id == 4160) {
            name = "Fire arrows";
        } else if (id == 7806) {
            name = "Ancient warrior sword";
        } else if (id == 7807) {
            name = "Ancient warrior axe";
        } else if (id == 7808) {
            name = "Ancient warrior maul";
        } else if (id == 24983) {
            name = "Ancient warrior sword (c)";
        } else if (id == 24981) {
            name = "Ancient warrior axe (c)";
        } else if (id == 24982) {
            name = "Ancient warrior maul (c)";
        } else if (id == 2944) {
            name = "Key of Drops";
        } else if (id == 12773) {
            name = "Lava whip";
        } else if (id == 12774) {
            name = "Frost whip";
        } else if (id == 10586) {
            ioptions = new String[]{null, null, null, null, "Drop"};
            name = "Genie pet";
//        } else if (id == 12102) {
//            name = "Grim Reaper pet";
//        } else if (id == 12081) {
//            name = "Elemental bow";
        } else if (id == 4067) {
            name = "Donator ticket";
            stackable = true;
            grandexchange = false;
        } else if (id == 13190) {
            name = "5$ bond";
//        } else if (id == 8013) {
//            name = "Home teleport";
//        } else if (id == 964) {
//            name = "Vengeance";
//        } else if (id == 18335) {
//            stackable = false;
//            name = "Lava Party hat";
        } else if (id == 16278) {
            stackable = false;
            name = "$10 bond";
        } else if (id == 16263) {
            stackable = false;
            name = "$20 bond";
        } else if (id == 16264) {
            stackable = false;
            name = "$40 bond";
        } else if (id == 16265) {
            stackable = false;
            name = "$50 bond";
        } else if (id == 16266) {
            stackable = false;
            name = "$100 bond";
        } else if (id == 16012) {
            stackable = false;
            name = "Baby Dark Beast pet";
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 16024) {
            stackable = false;
            name = "Baby Abyssal demon pet";
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 15331) {
            stackable = false;
            name = "Ring of manhunting";
            ioptions = new String[]{null, "Wear", null, null, "Drop"};
        } else if (id == 16167) {
            stackable = false;
            name = "Ring of sorcery";
            ioptions = new String[]{null, "Wear", null, null, "Drop"};
        } else if (id == 16168) {
            stackable = false;
            name = "Ring of precision";
            ioptions = new String[]{null, "Wear", null, null, "Drop"};
        } else if (id == 16169) {
            stackable = false;
            name = "Ring of trinity";
            ioptions = new String[]{null, "Wear", null, null, "Drop"};
//        } else if (id == 13215) {
//            name = "Bloody Token";
//            ioptions = new String[]{null, null, null, null, "Drop"};
//        } else if (id == 30235) {
//            name = "H'ween token";
//            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 30297) {
            name = "Corrupted boots";
//        } else if (id == 27644) {
//            name = "Salazar slytherins locket";
//        } else if (id == 28643) {
//            name = "Fenrir greyback Jr. pet";
//        } else if (id == 28642) {
//            name = "Fluffy Jr. pet";
//        } else if (id == 28641) {
//            name = "Talonhawk crossbow";
//        } else if (id == 28640) {
//            name = "Elder wand stick";
//        } else if (id == 28639) {
//            name = "Elder wand handle";
//        } else if (id == 30181) {
//            name = "Elder wand";
//        } else if (id == 10858) {
//            name = "Sword of gryffindor";
//        } else if (id == 30338) {
//            name = "Male centaur pet";
//        } else if (id == 30340) {
//            name = "Female centaur pet";
//        } else if (id == 30342) {
//            name = "Dementor pet";
//        } else if(id == 21291) {
//            name = "Jal-nib-rek pet";
//        } else if(id == 8788) {
//            name = "Corrupting stone";
        } else if(id == 30048) {
            name = "Corrupted ranger gauntlets";
        } else if(id == 32102) {
            name = "Blood Reaper pet";
//        } else if (id == 23757) {
//            name = "Yougnleff pet";
//        } else if (id == 23759) {
//            name = "Corrupted yougnleff pet";
//        } else if (id == 30016) {
//            name = "Founder imp pet";
//        } else if (id == 30018) {
//            name = "Corrupted nechryarch pet";
//        } else if (id == 30033) {
//            name = "Mini necromancer pet";
//        } else if (id == 30044) {
//            name = "Jaltok-jad pet";
//        } else if (id == 30131) {
//            name = "Baby lava dragon pet";
//        } else if (id == 16173) {
//            name = "Jawa pet";
//        } else if (id == 16172) {
//            name = "Baby aragog pet";
//        } else if (id == 16020) {
//            name = "Dharok pet";
//        } else if (id == 22319) {
//            name = "TzRek-Zuk pet";
//        } else if (id == 24491) {
//            name = "Little nightmare pet";
//        } else if (id == 23759) {
//            name = "Corrupted youngllef pet";
//        } else if (id == 30122) {
//            name = "Corrupt totem base";
//        } else if (id == 30123) {
//            name = "Corrupt totem middle";
//        } else if (id == 30124) {
//            name = "Corrupt totem top";
//        } else if (id == 30125) {
//            name = "Corrupt totem";
//        } else if (id == 16005) {
//            name = "Baby Squirt pet";
//            stackable = false;
        } else if (id == 7999) {
            name = "Pet paint (black)";
        } else if (id == 8000) {
            name = "Pet paint (white)";
        } else if (id == 15300) {
            stackable = false;
            name = "Recover special (4)";
        } else if (id == 15301) {
            stackable = false;
            name = "Recover special (3)";
        } else if (id == 15302) {
            stackable = false;
            name = "Recover special (2)";
        } else if (id == 15303) {
            stackable = false;
            name = "Recover special (1)";
//        } else if (id == 23818) {
//            name = "Barrelchest pet";
//            ioptions = new String[]{null, null, null, null, "Drop"};
//            stackable = false;
        } else if (id == 30049) {
            name = "Magma blowpipe";
//        } else if (id == 16171) {
//            name = "Wampa pet";
//            ioptions = new String[]{null, null, null, null, "Drop"};
//            stackable = false;
        } else if (id == 16013) {
            name = "Pet kree'arra (white)";
            stackable = false;
            ioptions = new String[]{null, null, null, "Wipe-off paint", null};
        } else if (id == 16014) {
            name = "Pet zilyana (white)";
            stackable = false;
            ioptions = new String[]{null, null, null, "Wipe-off paint", null};
        } else if (id == 29102) {
            name = "Scythe of vitur kit";
        } else if (id == 29103) {
            name = "Twisted bow kit";
        } else if (id == 16015) {
            name = "Pet general graardor (black)";
            stackable = false;
            ioptions = new String[]{null, null, null, "Wipe-off paint", null};
//        } else if (id == 16016) {
//            name = "Pet k'ril tsutsaroth (black)";
//            stackable = false;
//            ioptions = new String[]{null, null, null, "Wipe-off paint", null};
//        } else if (id == 12873 || id == 12875 || id == 12877 || id == 12879 || id == 12881 || id == 12883) {
//            ioptions = new String[5];
//            ioptions[0] = "Open";
//        } else if (id == 14500) {
//            name = "Rune pouch (i) (broken)";
//            stackable = false;
//            ioptions = new String[]{null, null, null, null, "Destroy"};
//        } else if (id == 14501) {
//            name = "Amulet of fury (or) (broken)";
//            stackable = false;
//            ioptions = new String[]{null, null, null, null, "Destroy"};
//        } else if (id == 14502) {
//            name = "Occult necklace (or) (broken)";
//            stackable = false;
//            ioptions = new String[]{null, null, null, null, "Destroy"};
//        } else if (id == 14503) {
//            name = "Amulet of torture (or) (broken)";
//            stackable = false;
//            ioptions = new String[]{null, null, null, null, "Destroy"};
//        } else if (id == 14504) {
//            name = "Necklace of anguish (or) (broken)";
//            stackable = false;
//            ioptions = new String[]{null, null, null, null, "Destroy"};
//        } else if (id == 14505) {
//            name = "Tormented bracelet (or) (broken)";
//            stackable = false;
//            ioptions = new String[]{null, null, null, null, "Destroy"};
//        } else if (id == 14506) {
//            name = "Dragon defender (t) (broken)";
//        } else if (id == 14507) {
//            name = "Dragon boots (g) (broken)";
//            stackable = false;
//            ioptions = new String[]{null, null, null, null, "Destroy"};
//        } else if (id == ELDER_WAND_HANDLE) {
//            name = "Elder wand handle";
//        } else if (id == ELDER_WAND_STICK) {
//            name = "Elder wand stick";
//        } else if (id == ELDER_WAND) {
//            name = "Elder wand";
//        } else if (id == TALONHAWK_CROSSBOW) {
//            name = "Talonhawk crossbow";
//        } else if (id == SALAZAR_SLYTHERINS_LOCKET) {
//            name = "Salazar slytherins locket";
//        } else if (id == CORRUPTED_BOOTS) {
//            name = "Corrupted boots";
//        } else if (id == FENRIR_GREYBACK_JR) {
//            name = "Fenrir greyback Jr pet";
//        } else if (id == FLUFFY_JR) {
//            name = "Fluffy Jr pet";
//        } else if (id == CENTAUR_MALE) {
//            name = "Centaur male pet";
//        } else if (id == CENTAUR_FEMALE) {
//            name = "Centaur female pet";
//        } else if (id == DEMENTOR_PET) {
//            name = "Dementor pet";
//        } else if (id == FOUNDER_IMP) {
//            name = "Founder imp pet";
//        } else if (id == PET_CORRUPTED_NECHRYARCH) {
//            name = "Corrupted nechryarch pet";
//        } else if (id == MINI_NECROMANCER) {
//            name = "Mini necromancer";
//        } else if (id == JALTOK_JAD) {
//            name = "Jaltok-jad";
//        } else if (id == BABY_LAVA_DRAGON) {
//            name = "Baby lava dragon";
//        } else if (id == JAWA_PET) {
//            name = "Jawa pet";
//        } else if (id == BABY_ARAGOG) {
//            name = "Baby aragog pet";
//        } else if (id == WAMPA) {
//            name = "Wampa pet";
//        } else if (id == DHAROK_PET) {
//            name = "Dharok pet";
//        } else if (id == MYSTERY_CHEST) {
//            name = "Mystery chest";
//        } else if (id == LAVA_DHIDE_COIF) {
//            name = "Lava dhide coif";
//        } else if (id == LAVA_DHIDE_BODY) {
//            name = "Lava dhide body";
//        } else if (id == LAVA_DHIDE_CHAPS) {
//            name = "Lava dhide chaps";
//        } else if (id == TWISTED_BOW_I) {
//            name = "Twisted bow (i)";
//        } else if (id == ANCESTRAL_HAT_I) {
//            name = "Ancestral hat (i)";
//        } else if (id == ANCESTRAL_ROBE_TOP_I) {
//            name = "Ancestral robe top (i)";
//        } else if (id == ANCESTRAL_ROBE_BOTTOM_I) {
//            name = "Ancestral robe bottom (i)";
//        } else if (id == SWORD_OF_GRYFFINDOR) {
//            name = "Sword of gryffindor";
//            ioptions = new String[]{null, "Wield", null, null, "Drop"};
//        } else if (id == VETERAN_PARTYHAT) {
//            name = "Veteran partyhat";
//        } else if (id == VETERAN_HWEEN_MASK) {
//            name = "Veteran h'ween mask";
//        } else if (id == ANCIENT_GODSWORD) {
//            name = "Ancient godsword";
//        } else if (id == VETERAN_SANTA_HAT) {
//            name = "Veteran santa hat";
//        } else if (id == MAGMA_BLOWPIPE) {
//            name = "Magma blowpipe";
//            ioptions = new String[]{null, "Wield", null, null, "Drop"};
//        } else if (id == ELDER_MAUL_21205 || id == ItemIdentifiers.ARMADYL_GODSWORD_OR || id == ItemIdentifiers.BANDOS_GODSWORD_OR || id == ItemIdentifiers.SARADOMIN_GODSWORD_OR || id == ItemIdentifiers.ZAMORAK_GODSWORD_OR || id == ItemIdentifiers.GRANITE_MAUL_12848) {
//            ioptions = new String[]{null, "Wield", null, null, "Drop"};
//        } else if (id == ItemIdentifiers.ATTACKER_ICON || id == ItemIdentifiers.COLLECTOR_ICON || id == ItemIdentifiers.DEFENDER_ICON || id == ItemIdentifiers.HEALER_ICON || id == ItemIdentifiers.AMULET_OF_FURY_OR || id == ItemIdentifiers.OCCULT_NECKLACE_OR || id == ItemIdentifiers.NECKLACE_OF_ANGUISH_OR || id == ItemIdentifiers.AMULET_OF_TORTURE_OR || id == ItemIdentifiers.BERSERKER_NECKLACE_OR || id == ItemIdentifiers.TORMENTED_BRACELET_OR || id == ItemIdentifiers.DRAGON_DEFENDER_T || id == ItemIdentifiers.DRAGON_BOOTS_G) {
//            ioptions = new String[]{null, "Wear", null, null, "Destroy"};
//            stackable = false;
//        }
        } else if (id == 3269) {
            name = "Slayer key";
            stackable = true;
        } else if (id == 23490) {
            name = "Larran's key tier I";
            ioptions = new String[]{null, null, null, null, "Drop"};
            countco = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            countobj = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        } else if (id == 14900) {
            name = "Larran's key tier II";
            stackable = true;
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 14901) {
            name = "Larran's key tier III";
            stackable = true;
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 25902 || id == 25907 || id == 24445 || id == 25913) {
            name = "Twisted slayer helmet (i)";
            ioptions = new String[]{null, "Wear", null, null, "Drop"};
        } else if (id == 13302) {
            name = "Wilderness key";
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 4447) {
            name = "Double drops lamp";
        } else if (id == 30185) {
            name = "Donator Mystery Box";
        } else if (id == 15300) {
            stackable = false;
            name = "Recover special (4)";
        } else if (id == 15301) {
            stackable = false;
            name = "Recover special (3)";
        } else if (id == 15302) {
            stackable = false;
            name = "Recover special (2)";
        } else if (id == 15303) {
            stackable = false;
            name = "Recover special (1)";
        } else if (id == 23818) {
            name = "Barrelchest pet";
            ioptions = new String[]{null, null, null, null, "Drop"};
            stackable = false;
        } else if (id == 14500) {
            name = "Rune pouch (i) (broken)";
            stackable = false;
            ioptions = new String[]{null, null, null, null, "Destroy"};
        } else if (id == 14501) {
            name = "Amulet of fury (or) (broken)";
            stackable = false;
            ioptions = new String[]{null, null, null, null, "Destroy"};
        } else if (id == 14502) {
            name = "Occult necklace (or) (broken)";
            stackable = false;
            ioptions = new String[]{null, null, null, null, "Destroy"};
        } else if (id == 14503) {
            name = "Amulet of torture (or) (broken)";
            stackable = false;
            ioptions = new String[]{null, null, null, null, "Destroy"};
        } else if (id == 14504) {
            name = "Necklace of anguish (or) (broken)";
            stackable = false;
            ioptions = new String[]{null, null, null, null, "Destroy"};
        } else if (id == 14505) {
            name = "Tormented bracelet (or) (broken)";
            stackable = false;
            ioptions = new String[]{null, null, null, null, "Destroy"};
        } else if (id == 14506) {
            name = "Dragon defender (t) (broken)";
        } else if (id == 14507) {
            name = "Dragon boots (g) (broken)";
            stackable = false;
            ioptions = new String[]{null, null, null, null, "Destroy"};
        }
    }

    void decode(RSBuffer buffer, int code) {
        if (code == 1) {
            inventoryModel = buffer.readUShort();
        } else if (code == 2) {
            name = buffer.readString();
        } else if (code == 4) {
            zoom2d = buffer.readUShort();
        } else if (code == 5) {
            xan2d = buffer.readUShort();
        } else if (code == 6) {
            yan2d = buffer.readUShort();
        } else if (code == 7) {
            xof2d = buffer.readUShort();
            if (xof2d > 0x7FFF) {
                xof2d -= 0x10000;
            }
        } else if (code == 8) {
            yof2d = buffer.readUShort();
            if (yof2d > 0x7FFF) {
                yof2d -= 0x10000;
            }
        } else if (code == 11) {
            stackable = true;
        } else if (code == 12) {
            cost = buffer.readInt();
        } else if (code == 16) {
            members = true;
        } else if (code == 23) {
            maleModel0 = buffer.readUShort();
            buffer.readByte();
        } else if (code == 24) {
            maleModel1 = buffer.readUShort();
        } else if (code == 25) {
            femaleModel0 = buffer.readUShort();
            buffer.readByte();
        } else if (code == 26) {
            femaleModel1 = buffer.readUShort();
        } else if (code >= 30 && code < 35) {
            options[code - 30] = buffer.readString();
            if (options[code - 30].equalsIgnoreCase("null")) {
                options[code - 30] = null;
            }
        } else if (code >= 35 && code < 40) {
            ioptions[code - 35] = buffer.readString();
        } else if (code == 40) {
            int num = buffer.readUByte();
            recol_s = new short[num];
            recol_d = new short[num];

            for (int var4 = 0; var4 < num; ++var4) {
                recol_s[var4] = (short) buffer.readUShort();
                recol_d[var4] = (short) buffer.readUShort();
            }
        } else if (code == 41) {
            int num = buffer.readUByte();
            retex_s = new short[num];
            retex_d = new short[num];

            for (int var4 = 0; var4 < num; ++var4) {
                retex_s[var4] = (short) buffer.readUShort();
                retex_d[var4] = (short) buffer.readUShort();
            }
        } else if (code == 42) {
            shiftClickDropType = buffer.readByte();
        } else if (code == 65) {
            grandexchange = true;
        } else if (code == 78) {
            maleModel2 = buffer.readUShort();
        } else if (code == 79) {
            femaleModel2 = buffer.readUShort();
        } else if (code == 90) {
            manhead = buffer.readUShort();
        } else if (code == 91) {
            womanhead = buffer.readUShort();
        } else if (code == 92) {
            manhead2 = buffer.readUShort();
        } else if (code == 93) {
            womanhead2 = buffer.readUShort();
        } else if (code == 94) {
            buffer.readUShort();
        } else if (code == 95) {
            zan2d = buffer.readUShort();
        } else if (code == 96) {
            dummyitem = buffer.readByte() == 1;
        } else if (code == 97) {
            notelink = buffer.readUShort();
        } else if (code == 98) {
            noteModel = buffer.readUShort();
        } else if (code >= 100 && code < 110) {
            if (countobj == null) {
                countobj = new int[10];
                countco = new int[10];
            }

            countobj[code - 100] = buffer.readUShort();
            countco[code - 100] = buffer.readUShort();
        } else if (code == 110) {
            resizex = buffer.readUShort();
        } else if (code == 111) {
            resizey = buffer.readUShort();
        } else if (code == 112) {
            resizez = buffer.readUShort();
        } else if (code == 113) {
            ambient = buffer.readByte();
        } else if (code == 114) {
            contrast = buffer.readByte() * 5;
        } else if (code == 115) {
            team = buffer.readUByte();
        } else if (code == 139) {
            op139 = buffer.readShort();
        } else if (code == 140) {
            op140 = buffer.readShort();
        } else if (code == 148) {
            placeheld = buffer.readUShort();
        } else if (code == 149) {
            pheld14401 = buffer.readUShort();
        } else if (code == 249) {
            int length = buffer.readUByte();
            int index;
            if (clientScriptData == null) {
                index = method32(length);
                clientScriptData = new HashMap<>(index);
            }
            for (index = 0; index < length; index++) {
                boolean stringData = buffer.readUByte() == 1;
                int key = buffer.readTriByte();
                clientScriptData.put(key, stringData ? buffer.readString() : buffer.readInt());
            }
        } else {
            throw new RuntimeException("cannot parse item definition, missing config code: " + code);
        }
    }


    void postDecode(int id) {
        if (id == 6808) {
            name = "Scroll of Imbuement";
        }
        bm = new BloodMoneyPrices();
    }

    public int highAlchValue() {
        return (int) (cost * 0.65);
    }

    public static int method32(int var0) {
        --var0;
        var0 |= var0 >>> 1;
        var0 |= var0 >>> 2;
        var0 |= var0 >>> 4;
        var0 |= var0 >>> 8;
        var0 |= var0 >>> 16;
        return var0 + 1;
    }

    public Map<Integer, Object> clientScriptData;

    public boolean stackable() {
        return stackable || noteModel > 0 || id == 13215 || id == 30235;
    }

    public boolean noted() {
        return noteModel > 0;
    }

    @Override
    public String toString() {
        return "ItemDefinition{" +
            "resizey=" + resizey +
            ", xan2d=" + xan2d +
            ", cost=" + cost +
            ", inventoryModel=" + inventoryModel +
            ", resizez=" + resizez +
            ", recol_s=" + Arrays.toString(recol_s) +
            ", recol_d=" + Arrays.toString(recol_d) +
            ", name='" + name + '\'' +
            ", zoom2d=" + zoom2d +
            ", yan2d=" + yan2d +
            ", zan2d=" + zan2d +
            ", yof2d=" + yof2d +
            ", stackable=" + stackable +
            ", countco=" + Arrays.toString(countco) +
            ", members=" + members +
            ", options=" + Arrays.toString(options) +
            ", ioptions=" + Arrays.toString(ioptions) +
            ", maleModel0=" + maleModel0 +
            ", maleModel1=" + maleModel1 +
            ", retex_s=" + Arrays.toString(retex_s) +
            ", retex_d=" + Arrays.toString(retex_d) +
            ", femaleModel1=" + femaleModel1 +
            ", maleModel2=" + maleModel2 +
            ", xof2d=" + xof2d +
            ", manhead=" + manhead +
            ", manhead2=" + manhead2 +
            ", womanhead=" + womanhead +
            ", womanhead2=" + womanhead2 +
            ", countobj=" + Arrays.toString(countobj) +
            ", femaleModel2=" + femaleModel2 +
            ", notelink=" + notelink +
            ", femaleModel0=" + femaleModel0 +
            ", resizex=" + resizex +
            ", noteModel=" + noteModel +
            ", ambient=" + ambient +
            ", contrast=" + contrast +
            ", team=" + team +
            ", grandexchange=" + grandexchange +
            ", unprotectable=" + unprotectable +
            ", dummyitem=" + dummyitem +
            ", placeheld=" + placeheld +
            ", pheld14401=" + pheld14401 +
            ", shiftClickDropType=" + shiftClickDropType +
            ", op139=" + op139 +
            ", op140=" + op140 +
            ", id=" + id +
            ", clientScriptData=" + clientScriptData +
            '}';
    }
}
