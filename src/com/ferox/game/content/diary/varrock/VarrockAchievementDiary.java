package com.ferox.game.content.diary.varrock;


import com.ferox.game.content.diary.StatefulAchievementDiary;
import com.ferox.game.world.entity.mob.player.Player;

import java.util.EnumSet;
import java.util.Set;

import static com.ferox.game.content.diary.varrock.VarrockDiaryEntry.*;


public final class VarrockAchievementDiary extends StatefulAchievementDiary<VarrockDiaryEntry> {

    public static final Set<VarrockDiaryEntry> EASY_TASKS = EnumSet.of(VARROCK_ROOFTOP,SMITH_STEEL_KNIFES,FILL_VIAL,EARTH_RUNES,TELEPORT_ESSENCE_VAR,MINE_ESSENCE,TEA_STALL,BECOME_A_DANCER,MINE_IRON);

    public static final Set<VarrockDiaryEntry> MEDIUM_TASKS = EnumSet.of(CHAMPIONS_GUILD,DRESS_FOR_SUCESS,APOTHECARY_STRENGTH,PURCHASE_KITTEN,POTION_DECANT);

    public static final Set<VarrockDiaryEntry> HARD_TASKS = EnumSet.of(PRAY_WITH_SMITE,OBSTACLE_PIPE,YEWS_AND_BURN,COOK_LOBSTER);

    public static final Set<VarrockDiaryEntry> ELITE_TASKS = EnumSet.of(SUPER_COMBAT,SMITH_RUNE_KNIFES,ALOT_OF_EARTH);

    public static final String NAME = "Varrock area";
    public double easy_total = EASY_TASKS.size();
    public double med_total= MEDIUM_TASKS.size();
    public double hard_total = HARD_TASKS.size();
    public double elite_total = ELITE_TASKS.size();

    public double totalcompleted = easy_total + med_total + hard_total + elite_total;

    public double easy_done(){
        return amountOfDiaryDone(EASY_TASKS);
    }
    public double med_done(){
        return amountOfDiaryDone(MEDIUM_TASKS);
    }
    public double hard_done(){
        return amountOfDiaryDone(HARD_TASKS);
    }
    public double elite_done(){
        return amountOfDiaryDone(ELITE_TASKS);
    }

    public double easyDone(){
        return (easy_done()/easy_total)*100.0D;
    }
    public double medDone(){
        return (med_done()/med_total)*100.0D;
    }
    public double hardDone(){
        return (hard_done()/hard_total)*100.0D;
    }
    public double eliteDone(){
        return (elite_done()/elite_total)*100.0D;
    }
    public double totalDone() {
        return easy_done() + med_done() + hard_done() + elite_done();
    }


    //updates the entire ardougne diary: progress bar, name, amount completed
    public void updateDiary() {
        int diaryId = 29541; //+2 = name, +3 = amount, +4 = easy , +5 = med, +6 = hard, +7 elite
        player.getPacketSender().sendFrame126((hasDoneAll() ? "@gre@Varrock" : "@yel@Varrock"), diaryId+2);
        player.getPacketSender().sendFrame126((hasDoneAll() ? "@gre@"+(int)totalDone()+"/"+(int)totalcompleted+"" : "@yel@"+(int)totalDone()+"/"+(int)totalcompleted+""), diaryId+3);
        player.getPacketSender().sendFrame77(diaryId+4, (int) easyDone());
        player.getPacketSender().sendFrame77(diaryId+5, (int) medDone());
        player.getPacketSender().sendFrame77(diaryId+6, (int) hardDone());
        player.getPacketSender().sendFrame77(diaryId+7, (int) eliteDone());
        player.varrock_diary_done = (int) (easy_done() + med_done() + hard_done() + elite_done());
        player.varrock_diary_total = (int)totalcompleted;
    }

    public VarrockAchievementDiary(Player player) {
        super(NAME, player);
    }

    public boolean hasCompleted(String difficulty) {
        switch (difficulty) {
            case "EASY":
                return hasDone(EntryDifficulty.EASY);

            case "MEDIUM":
                return hasDone(EntryDifficulty.MEDIUM);

            case "HARD":
                return hasDone(EntryDifficulty.HARD);

            case "ELITE":
                return hasDone(EntryDifficulty.ELITE);
        }
        return hasDone(EntryDifficulty.EASY);
    }
//
//    int REWARD = 13104;
//    public void claimReward() {
//        //EASY
//        if (!hasDone(EntryDifficulty.EASY)) {
//            npcDialogue("Come back when you've completed the easy tasks of this area.");
//            return;
//        } else {
//            if (!hasClaimed(EntryDifficulty.EASY)) {
//                npcDialogue("Nice job, here have the tier 1 reward.");
//                addReward(REWARD);
//                claim(EntryDifficulty.EASY);
//                return;
//            } else {
//                if (getCount(REWARD) == 0 && !hasClaimed(EntryDifficulty.MEDIUM)) {
//                    npcDialogue("Oh, you lost your reward? Don't worry, here you go.");
//                    addReward(REWARD);
//                    return;
//                }
//            }
//        }
//
//        //MEDIUM
//        if (hasDone(EntryDifficulty.EASY) && hasDone(EntryDifficulty.MEDIUM) && hasClaimed(EntryDifficulty.EASY)) {
//            if (hasClaimed(EntryDifficulty.MEDIUM)) {
//                if (getCount(REWARD + 1) == 0) {
//                    if (!hasClaimed(EntryDifficulty.HARD)) {
//                        npcDialogue("Oh, you lost your reward? Don't worry, here you go.");
//                        addReward(REWARD + 1);
//                        return;
//                    }
//                }
//            } else {
//                if (player.getItems().playerHasItem(REWARD)) {
//                    npcDialogue("Nice one, I will upgrade that for you..");
//                    upgradeReward(REWARD, REWARD + 1);
//                    claim(EntryDifficulty.MEDIUM);
//                    return;
//                } else {
//                    npcDialogue("Bring me the previous tier reward and I will upgrade it for you!");
//                    return;
//                }
//            }
//        }
//
//        //HARD
//        if (hasDone(EntryDifficulty.EASY) && hasDone(EntryDifficulty.MEDIUM) && hasDone(EntryDifficulty.HARD) && hasClaimed(EntryDifficulty.MEDIUM)) {
//            if (hasClaimed(EntryDifficulty.HARD)) {
//                if (getCount(REWARD + 2) == 0) {
//                    if (!hasClaimed(EntryDifficulty.ELITE)) {
//                        npcDialogue("Oh, you lost your reward? Don't worry, here you go.");
//                        addReward(REWARD + 2);
//                        return;
//                    }
//                }
//            } else {
//                if (player.getItems().playerHasItem(REWARD + 1)) {
//                    npcDialogue("Nice one, I will upgrade that for you..");
//                    upgradeReward(REWARD + 1, REWARD + 2);
//                    claim(EntryDifficulty.HARD);
//                    return;
//                } else {
//                    npcDialogue("Bring me the previous tier reward and I will upgrade it for you!");
//                    return;
//                }
//            }
//        }
//
//        //ELITE
//        if (hasDone(EntryDifficulty.EASY) && hasDone(EntryDifficulty.MEDIUM) && hasDone(EntryDifficulty.HARD) && hasDone(EntryDifficulty.ELITE) && hasClaimed(EntryDifficulty.HARD)) {
//            if (hasClaimed(EntryDifficulty.ELITE)) {
//                if (getCount(REWARD + 3) == 0) {
//                    npcDialogue("Oh, you lost your reward? Don't worry, here you go.");
//                    addReward(REWARD + 3);
//                    return;
//                }
//            } else {
//                if (player.getItems().playerHasItem(REWARD + 2)) {
//                    npcDialogue("Nice one, I will upgrade that for you..");
//                    upgradeReward(REWARD + 2, REWARD + 3);
//                    claim(EntryDifficulty.ELITE);
//                    return;
//                } else {
//                    npcDialogue("Bring me the previous tier reward and I will upgrade it for you!");
//                    return;
//                }
//            }
//        }
//
//    }
//
//    public void npcDialogue(String dialogue) {
//        player.getDH().sendNpcChat1(dialogue, player.npcType, "Diary Manager");
//        player.nextChat = -1;
//    }
//    public void addReward(int reward) {
//        player.getItems().addItem(reward, 1);
//        player.getDH().sendNpcChat1("Here you go, you've earned it.", player.npcType, "Diary Manager");
//    }
//    public void upgradeReward(int reward, int upgrade) {
//        player.getItems().replaceItem(player, reward, upgrade);
//        player.getDH().sendNpcChat1("Here you go, upgraded and ready.", player.npcType, "Diary Manager");
//    }
//    public int getCount(int id) {
//        return player.getItems().getItemCount(id, false);
//    }

    @Override
    public Set<VarrockDiaryEntry> getEasy() {
        return EASY_TASKS;
    }

    @Override
    public Set<VarrockDiaryEntry> getMedium() {
        return MEDIUM_TASKS;
    }

    @Override
    public Set<VarrockDiaryEntry> getHard() {
        return HARD_TASKS;
    }

    @Override
    public Set<VarrockDiaryEntry> getElite() {
        return ELITE_TASKS;
    }

    int frameIndex = 0;
    int amount = /*frameIndex == 10 || frameIndex == 16 || frameIndex == 20 ? 2 : */1;
    public void display() {
        Set<VarrockDiaryEntry>  easy = getEasy();
        Set<VarrockDiaryEntry> medium = getMedium();
        Set<VarrockDiaryEntry>   hard = getHard();
        Set<VarrockDiaryEntry> elite = getElite();

        int[] frames = { 8148, 8149, 8150, 8151, 8152,8153, 8154,8155, 8156, 8157, 8158, 8159,8160, 8161, 8162, 8163, 8164,
            8165, 8166, 8167, 8168, 8169, 8170, 8171, 8172, 8173, 8174, 8175, 8176,8177,8178, 8179, 8180, 8181,
            8182, 8183, 8184, 8185, 8186, 8187, 8188, 8189, 8190, 8191, 8192, 8193, 8194 };

        for (int i = 8144; i < 8195; i++) {
            player.getPacketSender().sendFrame126("", i);
        }
        player.getPacketSender().sendFrame126("@dre@Varrock Diary", 8144);
        player.getPacketSender().sendFrame126("", 8145);

        frameIndex = 0;
        player.getPacketSender().sendFrame126(hasCompleted("EASY") ? "@blu@<str=1>Easy</str>" : "@blu@Easy", 8147);
        easy.forEach(entry -> {
            String description = entry.getDescription();

            /* %stage gets the current stage (e.g. 1)
             * %maximumstage gets the maximum stage (e.g. 5)
             * %totalstage gets both of these (e.g. 1/5)
             */
            description = description.replace("%stagej", Integer.toString(getAbsoluteAchievementStage(entry)));
            description = description.replace("%maximumstage", Integer.toString(getMaximum(entry)));
            description = description.replace("%totalstage", (getAbsoluteAchievementStage(entry)) + "/" + getMaximum(entry));

            player.getPacketSender().sendFrame126(hasDone(entry) ? "<str=0>" +description+ "</str>" : description, frames[frameIndex]);
            frameIndex += amount;
        });
        player.getPacketSender().sendFrame126(hasCompleted("MEDIUM") ? "@blu@<str=1>Medium</str>" : "@blu@Medium", 8148+ frameIndex);//8150
        frameIndex++;
        medium.forEach(entry -> {
            String description = entry.getDescription();

            /* %stage gets the current stage (e.g. 1)
             * %maximumstage gets the maximum stage (e.g. 5)
             * %totalstage gets both of these (e.g. 1/5)
             */
            description = description.replace("%stagej", Integer.toString(getAbsoluteAchievementStage(entry)));
            description = description.replace("%maximumstage", Integer.toString(getMaximum(entry)));
            description = description.replace("%totalstage", (getAbsoluteAchievementStage(entry)) + "/" + getMaximum(entry));

            player.getPacketSender().sendFrame126(hasDone(entry) ? "<str=0>" +description+ "</str>" : description, frames[frameIndex]);
            frameIndex += amount;
        });

        player.getPacketSender().sendFrame126(hasCompleted("HARD") ? "@blu@<str=1>Hard</str>" : "@blu@Hard", 8148+frameIndex);//8154
        frameIndex++;
        hard.forEach(entry -> {
            String description = entry.getDescription();

            /* %stage gets the current stage (e.g. 1)
             * %maximumstage gets the maximum stage (e.g. 5)
             * %totalstage gets both of these (e.g. 1/5)
             */
            description = description.replace("%stagej", Integer.toString(getAbsoluteAchievementStage(entry)));
            description = description.replace("%maximumstage", Integer.toString(getMaximum(entry)));
            description = description.replace("%totalstage", (getAbsoluteAchievementStage(entry)) + "/" + getMaximum(entry));

            player.getPacketSender().sendFrame126(hasDone(entry) ? "<str=0>" +description+ "</str>" : description, frames[frameIndex]);
            frameIndex += amount;
        });

        player.getPacketSender().sendFrame126(hasCompleted("ELITE") ? "@blu@<str=1>Elite</str>" : "@blu@Elite", 8148+frameIndex);
        frameIndex++;
        elite.forEach(entry -> {
            String description = entry.getDescription();

            /* %stage gets the current stage (e.g. 1)
             * %maximumstage gets the maximum stage (e.g. 5)
             * %totalstage gets both of these (e.g. 1/5)
             */
            description = description.replace("%stagej", Integer.toString(getAbsoluteAchievementStage(entry)));
            description = description.replace("%maximumstage", Integer.toString(getMaximum(entry)));
            description = description.replace("%totalstage", (getAbsoluteAchievementStage(entry)) + "/" + getMaximum(entry));

            player.getPacketSender().sendFrame126(hasDone(entry) ? "<str=0>" +description+ "</str>" : description, frames[frameIndex]);
            frameIndex += amount;
        });

        player.getInterfaceManager().open(8134);
    }

    @Override
    public int getMaximum(VarrockDiaryEntry achievement) {
        return achievement.getMaximumStages();
    }

}
