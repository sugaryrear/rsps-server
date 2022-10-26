package com.ferox.game.content.diary;


import com.ferox.game.content.diary.ardougne.ArdougneAchievementDiary;
import com.ferox.game.content.diary.desert.DesertAchievementDiary;
import com.ferox.game.content.diary.falador.FaladorAchievementDiary;
import com.ferox.game.content.diary.fremennik.FremennikAchievementDiary;
import com.ferox.game.content.diary.kandarin.KandarinAchievementDiary;
import com.ferox.game.content.diary.karamja.KaramjaAchievementDiary;
import com.ferox.game.content.diary.kourend.KourendAchievementDiary;
import com.ferox.game.content.diary.lumbridge_draynor.LumbridgeDraynorAchievementDiary;
import com.ferox.game.content.diary.morytania.MorytaniaAchievementDiary;
import com.ferox.game.content.diary.varrock.VarrockAchievementDiary;
import com.ferox.game.content.diary.western_provinces.WesternAchievementDiary;
import com.ferox.game.content.diary.wilderness.WildernessAchievementDiary;
import com.ferox.game.world.entity.mob.player.Player;

public final class AchievementDiaryManager {

    private final Player player;

    private final VarrockAchievementDiary varrockDiary;
    private final ArdougneAchievementDiary ardougneDiary;
    private final FaladorAchievementDiary faladorDiary;
    private final LumbridgeDraynorAchievementDiary lumbridgeDraynorDiary;
    private final KaramjaAchievementDiary karamjaDiary;
    private final WildernessAchievementDiary wildernessDiary;
    private final MorytaniaAchievementDiary morytaniaDiary;
    private final KandarinAchievementDiary kandarinDiary;
    private final FremennikAchievementDiary fremennikDiary;
    private final WesternAchievementDiary westernDiary;
    private final DesertAchievementDiary desertDiary;
    private final KourendAchievementDiary kourendDiary;
    public AchievementDiaryManager(Player player) {
        this.player = player;

        varrockDiary = new VarrockAchievementDiary(player);
        ardougneDiary = new ArdougneAchievementDiary(player);
        faladorDiary = new FaladorAchievementDiary(player);
        lumbridgeDraynorDiary = new LumbridgeDraynorAchievementDiary(player);
        karamjaDiary = new KaramjaAchievementDiary(player);
        wildernessDiary = new WildernessAchievementDiary(player);
        morytaniaDiary = new MorytaniaAchievementDiary(player);
        kandarinDiary = new KandarinAchievementDiary(player);
        fremennikDiary = new FremennikAchievementDiary(player);
        westernDiary = new WesternAchievementDiary(player);
        desertDiary = new DesertAchievementDiary(player);
        kourendDiary = new KourendAchievementDiary(player);
    }

    public VarrockAchievementDiary getVarrockDiary() {
        return varrockDiary;
    }

    public ArdougneAchievementDiary getArdougneDiary() {
        return ardougneDiary;
    }

    public FaladorAchievementDiary getFaladorDiary() {
        return faladorDiary;
    }

    public LumbridgeDraynorAchievementDiary getLumbridgeDraynorDiary() {
        return lumbridgeDraynorDiary;
    }

    public KaramjaAchievementDiary getKaramjaDiary() {
        return karamjaDiary;
    }

    public WildernessAchievementDiary getWildernessDiary() {
        return wildernessDiary;
    }

    public MorytaniaAchievementDiary getMorytaniaDiary() {
        return morytaniaDiary;
    }

    public KandarinAchievementDiary getKandarinDiary() {
        return kandarinDiary;
    }

    public FremennikAchievementDiary getFremennikDiary() {
        return fremennikDiary;
    }

    public WesternAchievementDiary getWesternDiary() {
        return westernDiary;
    }

    public DesertAchievementDiary getDesertDiary() {
        return desertDiary;
    }
    public KourendAchievementDiary getKourendDiary() {
        return kourendDiary;
    }

    public Player getPlayer() {
        return player;
    }
}
