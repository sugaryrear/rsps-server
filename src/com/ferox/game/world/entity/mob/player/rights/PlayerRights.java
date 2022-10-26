package com.ferox.game.world.entity.mob.player.rights;

import com.ferox.GameServer;
import com.ferox.game.world.entity.mob.player.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * The rights of a player determines their authority. Every right can be viewed
 * with a name and a value. The value is used to separate each right from one
 * another.
 * 
 * @author Jason
 * @author Patrick van Elderen | zondag 14 juli 2019 (CEST) : 13:25
 * @see <a href="https://github.com/Patrick9-10-1995">Github profile</a>
 *
 */
public enum PlayerRights {

    PLAYER("Player", -1, -1, 0),

    MODERATOR("Moderator", 494, 1, 3),

    ADMINISTRATOR("Administrator", 495, 2, 4),

    OWNER("Owner", 496, 3, 6),

    DEVELOPER("Developer", 497, 4, 5),

    BRONZE_YOUTUBER("Bronze Youtuber", 1087, 8, 1),

    IRON_MAN("Iron Man", 502, 9, 0),

    ULTIMATE_IRON_MAN("Ultimate Iron Man", 503, 10, 0),

    HARDCORE_IRON_MAN("Hardcore Iron Man", 504, 11, 0),

    SUPPORT("Support Team", 505, 12, 2),

    SILVER_YOUTUBER("Silver Youtuber", 1088, 18, 1),

    GOLD_YOUTUBER("Gold Youtuber", 1089, 19, 1),

    ELITE_IRON_MAN("Elite Iron Man", 1769, 16, 0),

    GROUP_IRON_MAN("Group Iron Man", 1770, 17, 0),

    DARK_LORD("Dark Lord", 1838, 18, 0),

    SECURITY_MODERATOR("Security Mod", 1861, 1, 3),

    EVENT_MANAGER("Event Manager", 468, 1, 2),
    ;

    private final String name;

    private final int spriteId;

    private final int right;

    /**
     * The value of the right. The higher the value, the more permissions the player has.
     */
    private final int rightValue;

    PlayerRights(String name, int spriteId, int right, int rightValue) {
        this.name = name;
        this.spriteId = spriteId;
        this.right = right;
        this.rightValue = rightValue;
    }

    @Override
    public String toString() {
        return "PlayerRights{" + "name='" + name + '\'' + '}';
    }

    /**
     * A {@link Set} of all {@link PlayerRights} elements that cannot be directly
     * modified.
     */
    private static final Set<PlayerRights> RIGHTS = Collections.unmodifiableSet(EnumSet.allOf(PlayerRights.class));

    /**
     * Returns a {@link PlayerRights} object for the value.
     *
     * @param value the right level
     * @return the rights object
     */
    public static PlayerRights get(int value) {
        return RIGHTS.stream().filter(element -> element.rightValue == value).findFirst().orElse(PLAYER);
    }

    public final String getName() {
        return name;
    }

    public final int getSpriteId() {
        return spriteId;
    }

    public final int getRight() {
        return right;
    }

    public final int getRightValue() {
        return rightValue;
    }

    public boolean isSupportOrGreater(Player player) {
        return getRightValue() >= SUPPORT.getRightValue() || isOwner(player);
    }

    public boolean isModeratorOrGreater(Player player) {
        return getRightValue() >= MODERATOR.getRightValue() || isOwner(player);
    }

    public boolean isEventManagerOrGreater(Player player) {
        return getRightValue() >= EVENT_MANAGER.getRightValue() || isOwner(player);
    }

    public boolean isAdminOrGreater(Player player) {
        return getRightValue() >= ADMINISTRATOR.getRightValue() || isOwner(player);
    }

    /** Checks if the player is a developer or greater. */
    public boolean isDeveloperOrGreater(Player player) {
        return getRightValue() >= DEVELOPER.getRightValue() || isOwner(player);
    }

    public boolean isServerSupport() {
        return getRightValue() == SUPPORT.getRightValue();
    }

    public boolean isModerator() {
        return getRightValue() == MODERATOR.getRightValue();
    }

    public boolean isAdmin(Player player) {
        return getRightValue() == ADMINISTRATOR.getRightValue() || isOwner(player);
    }

    public boolean isOwner(Player player) {
        return getRightValue() >= OWNER.getRightValue() || Arrays.stream(getOwners()).anyMatch(username -> username.equalsIgnoreCase(player.getUsername()));
    }

    public String[] getOwners() {
        if(!GameServer.properties().production) {
            return new String[]{"patrick", "matthew", "test", "sugary"};
        } else {
            return new String[]{"patrick", "matthew"};
        }
    }

    public final boolean greater(PlayerRights other) {
        return getRightValue() > other.getRightValue();
    }

    public final boolean less(PlayerRights other) {
        return getRightValue() < other.getRightValue();
    }

    public boolean isStaffMember(Player player) {
        return isServerSupport() || isModerator() || isAdminOrGreater(player);
    }

    public boolean isStaffMemberOrYoutuber(Player player) {
        return isServerSupport() || isModerator() || isAdminOrGreater(player)|| isYoutuber(player);
    }

    public boolean isYoutuber(Player player) {
        return player.getPlayerRights().equals(BRONZE_YOUTUBER) || player.getPlayerRights().equals(SILVER_YOUTUBER) || player.getPlayerRights().equals(GOLD_YOUTUBER);
    }

    /** Gets the crown display. */
    public static String getCrown(Player player) {
        return player.getPlayerRights().equals(PLAYER) ? "" : "<img=" + (player.getPlayerRights().getSpriteId()) + ">";
    }
}
