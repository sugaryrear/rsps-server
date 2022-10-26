package com.ferox.game.content.diary;

import com.ferox.game.world.entity.mob.player.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public abstract class AchievementDiary<T extends Enum<T>> {

    /**
     * The name of this {@link AchievementDiary}
     */
    private final String name;

    /**
     * The {@link Player} object the {@link AchievementDiary} is responsible for
     */
    protected final Player player;

    /**
     * A {@link HashSet} representing the various {@link T} achievements the
     * {@link #player} has
     */
    protected final Set<T> achievements = new HashSet<>();

    /**
     * Creates a new {@link AchievementDiary} implementation (should be effectively immutable)
     * @param name
     * 		The name of this {@link AchievementDiary}
     * @param player
     *
     */
    public AchievementDiary(String name, Player player) {
        this.name = name;
        this.player = player;
    }

    public boolean complete(T achievement) {
        boolean success = achievements.add(achievement);
        if (success)
            uponCompletion(achievement);
        return success;
    }

    public final boolean nonNotifyComplete(T achievement) {
        return achievements.add(achievement);
    }

    public final boolean remove(T achievement) {
        return achievements.remove(achievement);
    }

    public void uponCompletion(T achievement) {
        player.message("@mag@Well done! You have completed a task in the "+ getName() +". Your "+(getName().contains("lumbridge") ? "" : "Achievement Diary")+"");
        player.message("@mag@"+(getName().contains("lumbridge") ? "Achievement" : "")+" Diary has been updated.");
        player.updateDiary();
    }

    public String getName() {
        return name;
    }

    public boolean hasDone(T entry) {
        return get(entry).isPresent();
    }

    public boolean hasDone(Set<T> entries) {
        boolean containsAll = true;
        for (T entry : entries) {
            if (!achievements.contains(entry)) {
                containsAll = false;
            }
        }
        return containsAll;
    }
    /**
     * total amount of dairies completed for a selected difficulty
     *
     * */
    public double amountOfDiaryDone(Set<T> entries) {
        double completed = 0;
        for (T entry : entries) {
            if (achievements.contains(entry)) {
                completed++;
            }
        }
        return completed;
    }
    public void forEach(Consumer<T> action) {
        achievements.forEach(entry -> action.accept(entry));
    }

    public Optional<T> get(T entry) {
        return achievements.stream().filter(some -> some.equals(entry))
            .findAny();
    }

    public Set<T> getAchievements() {
        return achievements;
    }

    public Player getPlayer() {
        return player;
    }
}

