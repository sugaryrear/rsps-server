package com.ferox.net.packet;

import com.ferox.GameServer;
import com.ferox.game.GameConstants;
import com.ferox.game.content.EffectTimer;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Entity;
import com.ferox.game.world.entity.masks.animations.Animation;
import com.ferox.game.world.entity.mob.player.InfectionType;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.PlayerInteractingOption;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.ground.GroundItem;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.outgoing.message.ComponentVisibility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class manages making the packets that will be sent (when called upon) onto
 * the associated player's client.
 * <p>
 * This class is used for S2C (Server to Client) packets.
 *
 * @author relex lawl & Gabbe
 */
public final class PacketSender {

    private static final Logger logger = LogManager.getLogger(PacketSender.class);

    public PacketSender sendEntityFeed(String opponent, int HP, int maxHP) {
        PacketBuilder out = new PacketBuilder(175, PacketType.VARIABLE);
        out.putString(opponent == null ? "null" : opponent).putShort(HP).putShort(maxHP);
        player.getSession().write(out);
        return this;
    }

    public PacketSender resetEntityFeed() {
        sendEntityFeed("", 0, 0);
        return this;
    }

    /**
     * Sends some information to the client about screen fading.
     *
     * @param text the text that will be displayed in the center of the screen
     * @param state the state should be either 0, -1, or 1.
     * @param seconds the amount of time in seconds it takes for the fade to transition.
     *            <p>
     *            If the state is -1 then the screen fades from black to transparent. When the state is +1 the screen fades from transparent to black. If the state is 0 all drawing
     *            is stopped.
     */
    public PacketSender sendScreenFade(String text, int state, int seconds) {
        PacketBuilder out = new PacketBuilder(13, PacketType.VARIABLE);
        if (seconds < 1 && state != 0) {
            throw new IllegalArgumentException("The amount of seconds cannot be less than one.");
        }
        out.putString(text);
        out.put(state);
        out.put(seconds);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendProgressBar(int child, int progress) {
        PacketBuilder out = new PacketBuilder(237);
        out.putInt(child);
        out.putShort(progress);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendSound(int id, int type, int delay, int volume) {
        if(player != null && id != -1 && GameServer.properties().soundsEnabled) {
            PacketBuilder out = new PacketBuilder(174);
            out.putShort(id);
            out.put(type);
            out.putShort(delay);
            out.putShort(volume);
            player.getSession().write(out);
        }
        return this;
    }

    public PacketSender setWidgetActive(int child, boolean active) {
        PacketBuilder out = new PacketBuilder(143);
        out.put(active ? 1 : 0);
        out.putShort(child);
        //System.out.println("Widget active: "+active+" child: "+child);
        player.getSession().write(out);
        return this;
    }

    public PacketSender clearClickedText() {
        PacketBuilder out = new PacketBuilder(238);
        player.getSession().write(out);
        return this;
    }
    public PacketSender resetCamera() {
        PacketBuilder out = new PacketBuilder(107);
        player.getSession().write(out);
        return this;
    }
    public void stillCamera(int x, int y, int height, int speed, int angle) {
        PacketBuilder out = new PacketBuilder(177);
        out.put(x/64);
        out.put(y/64);
        out.putShort(height);
        out.put(speed);
        out.put(angle);
        player.getSession().write(out);
    }
    public PacketSender sendTeleportData(int length, boolean recent, int[] sprites, String[] names) {

        PacketBuilder out = new PacketBuilder(192, PacketType.VARIABLE_SHORT);
        out.putShort(length);
        out.put(recent ? 1 : 0);
        for (int i = 0; i < length; i++) {
            out.putShort(sprites[i]);
            out.putString(names[i]);
        }
        player.getSession().write(out);

        return this;
    }

    public PacketSender setClickedText(int interfaceId, boolean state) {
        PacketBuilder out = new PacketBuilder(239);
        out.put(state ? 1 : 0);
        out.putInt(interfaceId);
        player.getSession().write(out);
        return this;
    }

    public PacketSender updateWidgetTooltipText(int id, String text) {
        PacketBuilder out = new PacketBuilder(207, PacketType.VARIABLE);
        out.putShort(id);
        out.putString(text);
        player.getSession().write(out);
        return this;
    }

    public PacketSender changeWidgetText(int id, String text) {
        PacketBuilder out = new PacketBuilder(209, PacketType.VARIABLE);
        out.putShort(id);
        out.putString(text);
        player.getSession().write(out);
        return this;
    }

    public PacketSender updateTab(int value, int id) {
        PacketBuilder out = new PacketBuilder(189);
        out.putShort(value).putShort(id);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendHitPredictor(int hit) {
        PacketBuilder out = new PacketBuilder(135);
        out.putShort(hit);
        //System.out.println("Sending hit "+ hit);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendStamina(boolean active) {
        PacketBuilder out = new PacketBuilder(138);
        out.put(active ? 1 : 0);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendBroadcast(String message) {
        PacketBuilder out = new PacketBuilder(222, PacketType.VARIABLE_SHORT);
        out.putString(message);
        player.getSession().write(out);
        return this;
    }

    public PacketSender mysteryBoxSpinner() {
        PacketBuilder out = new PacketBuilder(11);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendInfection(InfectionType infection) {
        PacketBuilder out = new PacketBuilder(6);

        out.put(infection.ordinal(), ValueType.C);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendScreenMode(int width, int height) {
        PacketBuilder out = new PacketBuilder(128);

        out.putShort(width, ValueType.A, ByteOrder.LITTLE).putInt(height);
        player.getSession().write(out);
        return this;
    }

    /**
     * Changes the main displaying sprite on an interface. The index represents
     * the location of the new sprite in the index of the sprite array.
     *
     * @param componentId the interface
     * @param index       the index in the array
     */
    public PacketSender sendChangeSprite(int componentId, byte index) {
        PacketBuilder out = new PacketBuilder(7);
        out.putInt(componentId);
        out.put(index);
        player.getSession().write(out);
        return this;
    }

    /**
     * Sends information about the player to the client.
     *
     * @return The PacketSender instance.
     */
    public PacketSender sendDetails() {
        PacketBuilder out = new PacketBuilder(249);
        out.put(1, ValueType.A);
        out.putShort(player.getIndex());
        player.getSession().write(out);
        return this;
    }

    /**
     * Sends the map region a player is located in and also
     * sets the player's first step position of said region as their
     * {@code lastKnownRegion}.
     *
     * @return The PacketSender instance.
     */
    public PacketSender sendMapRegion() {
        player.setRegionHeight(player.tile().getLevel());
        //player.setNeedsPlacement(true);//Need to figure out how other rsps do this flag.
        player.setAllowRegionChangePacket(true);
        player.setLastKnownRegion(player.tile().copy());
        PacketBuilder out = new PacketBuilder(73);
        out.putShort(player.tile().getRegionX() + 6, ValueType.A);
        out.putShort(player.tile().getRegionY() + 6);
        player.getSession().write(out);
        return this;
    }

    /**
     * Sends the logout packet for the player.
     *
     * @return The PacketSender instance.
     */
    public PacketSender sendLogout() {
        PacketBuilder out = new PacketBuilder(109);
        player.getSession().write(out);
        return this;
    }

    /**
     * Sets the world's system update time, once timer is 0, everyone will be disconnected.
     *
     * @param time The amount of seconds in which world will be updated in.
     * @return The PacketSender instance.
     */
    public PacketSender sendSystemUpdate(int time) {
        PacketBuilder out = new PacketBuilder(114);
        out.putShort(time, ByteOrder.LITTLE);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendSong(int id) {
        PacketBuilder out = new PacketBuilder(74);
        out.putShort(id, ByteOrder.LITTLE);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendAutocastId(int id) {
        PacketBuilder out = new PacketBuilder(38);
        out.putShort(id);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendEnableNoclip() {
        PacketBuilder out = new PacketBuilder(250);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendURL(String url) {
        PacketBuilder out = new PacketBuilder(251, PacketType.VARIABLE);
        out.putString(url);
        player.getSession().write(out);
        return this;
    }

    /**
     * Sends a game message to a player in the server.
     *
     * @param message The message they will receive in chat box.
     * @return The PacketSender instance.
     */
    public PacketSender sendMessage(String message) {
        if (message.length() > 220) {
            throw new IllegalArgumentException("The message length was too big! "+message);
        }
        PacketBuilder out = new PacketBuilder(253, PacketType.VARIABLE);
        out.putString(message);
        player.getSession().write(out);
        return this;
    }
    public PacketSender sendAlertMessage(String line1, String line2) {

        PacketBuilder out = new PacketBuilder(253, PacketType.VARIABLE);
        out.putString("Alert##"+line1+"##"+line2);
        player.getSession().write(out);
        return this;
    }
    /**
     * Sends a clan message to a player in the server.
     *
     * @param message The message they will receive in chat box.
     * @return The PacketSender instance.
     */
    public PacketSender sendClanMessage(String name, int type, String message) {
        PacketBuilder out = new PacketBuilder(252, PacketType.VARIABLE_SHORT);
        out.put(type);
        out.putString(name);
        out.putString(message);
        player.getSession().write(out);
        return this;
    }

    /**
     * Sends a skill to a client.
     */
    public PacketSender updateSkill(int skill, int level, int xp) {
        PacketBuilder out = new PacketBuilder(134);
        out.put(skill).putInt(xp, ByteOrder.MIDDLE).put(level);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendExpDrop(int skill, int exp, boolean counter) {
        PacketBuilder out = new PacketBuilder(116);
        out.put(skill).putInt(exp).put(counter ? 1 : 0);
        player.getSession().write(out);
        return this;
    }

    /**
     * Sends a configuration button's state.
     *
     * @param id    The id of the configuration button.
     * @param state The state to set it to.
     * @return The PacketSender instance.
     */
    public PacketSender sendConfig(int id, int state) {
        PacketBuilder out = new PacketBuilder(36);
        out.putShort(id, ByteOrder.LITTLE);
        out.put(state);
        player.getSession().write(out);
        return this;
    }

    /**
     * Sends a interface child's toggle.
     *
     * @param id    The id of the child.
     * @param state The state to set it to.
     * @return The PacketSender instance.
     */
    public PacketSender sendToggle(int id, int state) {
        PacketBuilder out = new PacketBuilder(87);
        out.putShort(id, ByteOrder.LITTLE);
        out.putInt(state, ByteOrder.MIDDLE);
        player.getSession().write(out);
        return this;
    }

    /**
     * Sends the state in which the player has their chat options, such as public, private, friends only.
     *
     * @param publicChat  The state of their public chat.
     * @param privateChat The state of their private chat.
     * @param tradeChat   The state of their trade chat.
     * @return The PacketSender instance.
     */
    public PacketSender sendChatOptions(int publicChat, int privateChat, int tradeChat) {
        PacketBuilder out = new PacketBuilder(206);
        out.put(publicChat).put(privateChat).put(tradeChat);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendRunEnergy(int energy) {
        PacketBuilder out = new PacketBuilder(110);
        out.put(energy);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendQuickPrayersState(boolean activated) {
        PacketBuilder out = new PacketBuilder(111);
        out.put(activated ? 1 : 0);
        player.getSession().write(out);
        return this;
    }

    public PacketSender updateSpecialAttackOrb() {
        PacketBuilder out = new PacketBuilder(137);
        out.put(player.getSpecialAttackPercentage());
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendRunStatus() {
        boolean running = player.getAttribOr(AttributeKey.IS_RUNNING, false);
        PacketBuilder out = new PacketBuilder(113);
        out.put(running ? 1 : 0);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendWeight(double weight) {
        PacketBuilder out = new PacketBuilder(240);
        out.putShort((int) weight);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendInterface(int id) {
        //System.out.println("sending interface");
        PacketBuilder out = new PacketBuilder(97);
        out.putInt(id);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendWalkableInterface(int interfaceId) {
        PacketBuilder out = new PacketBuilder(208);
        out.putInt(interfaceId);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendInterfaceDisplayState(int interfaceId, boolean hide) {
        if (player.getPacketDropper().requiresUpdate(171, new ComponentVisibility(hide, interfaceId))) {
            PacketBuilder out = new PacketBuilder(171);
            out.put(hide ? 1 : 0);
            out.putInt(interfaceId);
            player.getSession().write(out);
        }
        return this;
    }

    public PacketSender sendPlayerHeadOnInterface(int id) {
        PacketBuilder out = new PacketBuilder(185);
        out.putShort(id, ValueType.A, ByteOrder.LITTLE);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendNpcHeadOnInterface(int id, int interfaceId) {
        PacketBuilder out = new PacketBuilder(75);
        out.putShort(id, ValueType.A, ByteOrder.LITTLE);
        out.putShort(interfaceId, ValueType.A, ByteOrder.LITTLE);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendEnterAmountPrompt(String title) {
        PacketBuilder out = new PacketBuilder(27, PacketType.VARIABLE);
        out.putString(title);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendEnterInputPrompt(String title) {
        PacketBuilder out = new PacketBuilder(187, PacketType.VARIABLE);
        out.putString(title);
        player.getSession().write(out);
        return this;
    }

    /**
     * Closes a player's client.
     */
    public PacketSender sendExit() {
        PacketBuilder out = new PacketBuilder(62);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendInterfaceComponentMoval(int id, int x, int y) {
        PacketBuilder out = new PacketBuilder(70);
        out.putShort(x).putShort(y).putInt(id);
        //System.out.println("x "+ x +" y "+ y +" id "+id);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendInterfaceAnimation(int interfaceId, Animation animation) {
        PacketBuilder out = new PacketBuilder(200);
        out.putShort(interfaceId);
        out.putShort(animation.getId());
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendInterfaceModel(int widget, int scale, Item item) {
        PacketBuilder out = new PacketBuilder(246);
        out.putShort(widget, ByteOrder.LITTLE);
        out.putShort(scale).putShort(item.getId());
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendInterfaceModel(int widget, int scale, int item) {
        PacketBuilder out = new PacketBuilder(246);
        out.putShort(widget, ByteOrder.LITTLE);
        out.putShort(scale).putShort(item);
        player.getSession().write(out);
        return this;
    }

    /**
     * read the method name
     */

    public PacketSender sendInterfaceSpriteChange(int interfaceID, int spriteID) {

        PacketBuilder out = new PacketBuilder(193);
        out.putInt(interfaceID);
        out.putShort(spriteID);

        player.getSession().write(out);
        return this;
    }

    public PacketSender sendTabInterface(int tabId, int interfaceId) {
        PacketBuilder out = new PacketBuilder(71);
        out.putInt(interfaceId);
        out.put(tabId, ValueType.A);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendTabs() {
        for (int i = 0; i < GameConstants.SIDEBAR_INTERFACE.length; i++) {
            int tab = GameConstants.SIDEBAR_INTERFACE[i][0];
            int id = GameConstants.SIDEBAR_INTERFACE[i][1];
            if (tab == 6) {
                id = player.getSpellbook().getInterfaceId();
                if (id == 29999) {
                    updateTab(2, 0);
                } else if (id == 838) {
                    updateTab(1, 0);
                } else if (id == 938) {
                    updateTab(0, 0);
                } else {
                    logger.error("For some reason, the spellbook interface ID for " + player.getUsername() + " is " + id);
                }
            }
            player.getInterfaceManager().setSidebar(tab, id);
        }
        return this;
    }

    public PacketSender sendWeapon(int weaponId, int ammoId) {
        PacketBuilder out = new PacketBuilder(191);
        out.putShort(weaponId).putShort(ammoId);
        player.getSession().write(out);
        return this;
    }

    public PacketSender setDefensiveAutocastState(int state) {
        PacketBuilder out = new PacketBuilder(188);
        out.putShort(state);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendTab(int id) {
        PacketBuilder out = new PacketBuilder(106);
        out.put(id, ValueType.C);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendChatboxInterface(int id) {
        PacketBuilder out = new PacketBuilder(164);
        out.putShort(id, ByteOrder.LITTLE);
        player.getSession().write(out);
        return this;
    }

    public PacketSender changeMapVisibility(int state) {
        PacketBuilder out = new PacketBuilder(99);
        out.put(state);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendInterfaceRemoval() {
        //System.out.println("sending interface removal");
        //System.out.println("Send interface removal " + Misc.getStackTrace());
        player.getSession().write(new PacketBuilder(219));
        return this;
    }

    public PacketSender closeDialogue() {
        player.getSession().write(new PacketBuilder(220));
        return this;
    }

    public PacketSender sendInterfaceScrollReset(int interfaceId) {
        PacketBuilder out = new PacketBuilder(9);
        out.putInt(interfaceId);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendScrollbarHeight(int interfaceId, int scrollMax) {
        PacketBuilder out = new PacketBuilder(10);
        out.putInt(interfaceId);
        out.putShort(scrollMax);
        //System.out.printf("interfaceId= %d scrollMax=%d %n", interfaceId, scrollMax);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendInterfaceSet(int interfaceId, int sidebarInterfaceId) {
        PacketBuilder out = new PacketBuilder(248);
        out.putInt(interfaceId);
        out.putInt(sidebarInterfaceId);
        player.getSession().write(out);
        return this;
    }
    public PacketSender disableTabs() {
        PacketBuilder out = new PacketBuilder(243);
        out.putInt(1);
        out.putInt(5063);
        player.getSession().write(out);
        return this;
    }
    public PacketSender flashSidebar(int value) {
        PacketBuilder out = new PacketBuilder(24);
        out.put(value);
        player.getSession().write(out);
        return this;
    }
    public PacketSender sendEffectTimer(int delay, EffectTimer e) {

        PacketBuilder out = new PacketBuilder(54);

        out.putShort(delay);
        out.putShort(e.getClientSprite());

        player.getSession().write(out);
        return this;
    }
    public PacketSender disableTabs(int tabs) {

        PacketBuilder out = new PacketBuilder(243);

        out.putShort(tabs);

        player.getSession().write(out);
        return this;
    }
    public PacketSender sendItemOnInterface(int interfaceId, Item... items) {
        PacketBuilder out = new PacketBuilder(53, PacketType.VARIABLE_SHORT);
        out.putInt(interfaceId);
        out.putShort(items.length);
        for (final Item item : items) {
            if (item != null) {
                if (item.getAmount() > 254) {
                    out.put(255).putInt(item.getAmount(), ByteOrder.INVERSE_MIDDLE);
                } else {
                    out.put(item.getAmount());
                }
                out.putShort(item.getId() + 1, ValueType.A, ByteOrder.LITTLE);
            } else {
                out.put(0).putShort(0, ValueType.A, ByteOrder.LITTLE);
            }
        }

        player.getSession().write(out);
        return this;
    }

    public void sendAlertItems(String line1, String line2, Item... items){
        player.RARE.clear();

        player.RARE.addAll(Arrays.asList(items));
        sendAlertItemOnInterface(41001,player.getPossibleRewards());
        sendAlertMessage(line1, line2);
    }
    public PacketSender sendAlertItemOnInterface(int interfaceId, List<Item> items) {
        PacketBuilder out = new PacketBuilder(58, PacketType.VARIABLE_SHORT);
        out.putInt(interfaceId);
        out.putShort(items.size());
        for (final Item item : items) {
            if (item != null) {
                if (item.getAmount() > 254) {
                    out.put(255).putInt(item.getAmount(), ByteOrder.INVERSE_MIDDLE);
                } else {
                    out.put(item.getAmount());
                }
                out.putShort(item.getId() + 1, ValueType.A, ByteOrder.LITTLE);
            } else {
                out.put(0).putShort(0, ValueType.A, ByteOrder.LITTLE);
            }
        }

        player.getSession().write(out);
        return this;
    }
    public PacketSender sendBanktabs() {
        PacketBuilder out = new PacketBuilder(55, PacketType.FIXED);
        if (player.getBank().tabAmounts != null) {
            for (final int amount : player.getBank().tabAmounts) {
                out.put(amount >> 8).putShort(amount & 0xFF);
            }
        } else {
            // just cover the case when the above two for some reason dont trigger
            for (final int amount : new int[10]) {
                out.put(amount >> 8).putShort(amount & 0xFF);
            }
        }
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendItemOnInterface(int interfaceId, List<Item> items) {
        PacketBuilder out = new PacketBuilder(53, PacketType.VARIABLE_SHORT);
        out.putInt(interfaceId).putShort(items.size());
        for (final Item item : items) {
            if (item != null) {
                if (item.getAmount() > 254) {
                    out.put(255).putInt(item.getAmount(), ByteOrder.INVERSE_MIDDLE);
                } else {
                    out.put(item.getAmount());
                }
                out.putShort(item.getId() + 1, ValueType.A, ByteOrder.LITTLE);
            } else {
                out.put(0).putShort(0, ValueType.A, ByteOrder.LITTLE);
            }
        }
        player.getSession().write(out);
        return this;
    }

    /**
     * Sends the given collection of items on the specified frameId.
     *
     * @param interfaceId The client interface frame id that the items are being sent
     *                    to.
     * @param items       The collection of items that is being sent.
     */
    public PacketSender sendItemOnInterface(final int interfaceId, final Collection<Item> items) {
        PacketBuilder out = new PacketBuilder(53, PacketType.VARIABLE_SHORT);
        out.putInt(interfaceId).putShort(items.size());
        for (final Item item : items) {
            if (item != null) {
                if (item.getAmount() > 254) {
                    out.put(255).putInt(item.getAmount(), ByteOrder.INVERSE_MIDDLE);
                } else {
                    out.put(item.getAmount());
                }
                out.putShort(item.getId() + 1, ValueType.A, ByteOrder.LITTLE);
            } else {
                out.put(0).putShort(0, ValueType.A, ByteOrder.LITTLE);
            }
        }
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendItemOnInterfaceSlot(int interfaceId, int item, int amount, int slot) {
        return sendItemOnInterfaceSlot(interfaceId, new Item(item, amount), slot);
    }

    public PacketSender sendItemOnInterfaceSlot(int interfaceId, Item item, int slot) {
        PacketBuilder out = new PacketBuilder(34, PacketType.VARIABLE_SHORT);
        out.putInt(interfaceId);
        out.put(slot);
        if (item == null) {
            out.putShort(0);
            out.put(0);
        } else {
            out.putShort(item.getId() + 1);
            if (item.getAmount() > 254) {
                out.put(255);
                out.putInt(item.getAmount());
            } else {
                out.put(item.getAmount());
            }
        }
        player.getSession().write(out);
        return this;
    }

    public PacketSender clearItemOnInterface(int frame) {
        PacketBuilder out = new PacketBuilder(72);
        out.putShort(frame);
        player.getSession().write(out);
        return this;
    }


    public PacketSender sendInteractionOption(String option, int slot, boolean top) {
        PacketBuilder out = new PacketBuilder(104, PacketType.VARIABLE);
        out.put(slot, ValueType.C);
        out.put(top ? 1 : 0, ValueType.A);
        out.putString(option);
        player.getSession().write(out);
        PlayerInteractingOption interactingOption = PlayerInteractingOption.forName(option);
        player.setPlayerInteractingOption(interactingOption);
        return this;
    }

    public PacketSender sendLobbytimer(int seconds) {
        PacketBuilder out = new PacketBuilder(203);
        out.putShort(seconds);
        player.getSession().write(out);
        return this;
    }

    public void sendMultipleStrings(List<Player.TextData> stringList) {
        List<Player.TextData> filtered = stringList.stream().collect(Collectors.toList());

        PacketBuilder out = new PacketBuilder(129, PacketType.VARIABLE_SHORT);

        out.put(filtered.size());

        filtered.forEach(string -> {
            out.putString(string.text);
            out.putInt(string.id);
        });
        player.getSession().write(out);
    }

    public PacketSender sendString(int id, String string) {
        PacketBuilder out = new PacketBuilder(126, PacketType.VARIABLE_SHORT);
        out.putString(string);
        out.putInt(id);
        player.getSession().write(out);
        //System.out.println("Id being updated: "+id+" vs text: "+string);
        return this;
    }
    public PacketSender sendFrame126(String string, int id) {
        PacketBuilder out = new PacketBuilder(126, PacketType.VARIABLE_SHORT);
        out.putString(string);
        out.putInt(id);
        player.getSession().write(out);
        //System.out.println("Id being updated: "+id+" vs text: "+string);
        return this;
    }
    public PacketSender sendColor(int id, int color) {
        PacketBuilder out = new PacketBuilder(122);
        out.putShort(id);
        out.putInt(color);
        //System.out.printf("id: %d colour: %d%n", id, color);
        player.getSession().write(out);
        return this;
    }

    /**
     * Sends the players rights ordinal to the client.
     *
     * @return The packetsender instance.
     */
    public PacketSender sendRights() {
        PacketBuilder out = new PacketBuilder(127);
        out.put(player.getPlayerRights().ordinal());
        out.put(player.getMemberRights().ordinal());
        player.getSession().write(out);
        return this;
    }

    /**
     * Sends a hint to specified position.
     *
     * @param tile     The position to create the hint.
     * @param tilePosition The position on the square (middle = 2; west = 3; east = 4; south = 5; north = 6)
     * @return The Packet Sender instance.
     */
    public PacketSender sendPositionalHint(Tile tile, int tilePosition) {
        PacketBuilder out = new PacketBuilder(254);
        out.put(tilePosition);
        out.putShort(tile.getX());
        out.putShort(tile.getY());
        out.put(tile.getLevel());
        player.getSession().write(out);
        return this;
    }

    /**
     * Sends a hint above an entity's head.
     *
     * @param entity The target entity to draw hint for.
     * @return The PacketSender instance.
     */
    public PacketSender sendEntityHint(Entity entity) {
        int type = entity instanceof Player ? 10 : 1;
        PacketBuilder out = new PacketBuilder(254);
        out.put(type);
        out.putShort(entity.getIndex());
        out.putInt(0, ByteOrder.TRIPLE_INT);
        player.getSession().write(out);

        // Tell client to prioritize the target.
        if (entity.getIndex() >= 0 && entity.isPlayer()) {
            player.message("prioritizetarget:"+entity.getIndex());
        }
        return this;
    }

    /**
     * Sends a hint removal above an entity's head.
     *
     * @param playerHintRemoval Remove hint from a player or an NPC?
     * @return The PacketSender instance.
     */
    public PacketSender sendEntityHintRemoval(boolean playerHintRemoval) {
        int type = playerHintRemoval ? 10 : 1;
        PacketBuilder out = new PacketBuilder(254);
        out.put(type).putShort(-1);
        out.putInt(0, ByteOrder.TRIPLE_INT);
        player.getSession().write(out);

        // Tell client to prioritize the target.
        player.message("prioritizetarget:-1");
        return this;
    }

    public PacketSender sendMultiIcon(int value) {
        PacketBuilder out = new PacketBuilder(61);
        out.put(value);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendPrivateMessage(Player target, byte[] message, int size) {
        PacketBuilder out = new PacketBuilder(196, PacketType.VARIABLE_SHORT);
        out.putString(target.getUsername());
        out.putInt(target.getRelations().getPrivateMessageId());
        out.put(target.getPlayerRights().ordinal());
        out.put(target.getMemberRights().ordinal());
        out.putBytes(message, size);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendFriendStatus(int status) {
        PacketBuilder out = new PacketBuilder(221);
        out.put(status);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendFriend(String name, int world) {
        // world 0 = offline
        world = world != 0 ? world + 9 : world;
        PacketBuilder out = new PacketBuilder(50, PacketType.VARIABLE);
        out.putNewString(name);
        out.put(world);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendDeleteFriend(String name) {
        PacketBuilder out = new PacketBuilder(51, PacketType.VARIABLE);
        out.putString(name);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendAddIgnore(String name) {
        PacketBuilder out = new PacketBuilder(214, PacketType.VARIABLE);
        out.putString(name);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendDeleteIgnore(String name) {
        PacketBuilder out = new PacketBuilder(215, PacketType.VARIABLE);
        out.putString(name);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendAnimationReset() {
        PacketBuilder out = new PacketBuilder(1);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendTileGraphic(int id, Tile tile, int height, int delay) {
        sendPosition(tile);
        PacketBuilder out = new PacketBuilder(4);

        out.put(0);
        out.putShort(id);
        out.put(height);
        out.putShort(delay);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendObject(GameObject object) {
        sendPosition(object.tile());
        PacketBuilder out = new PacketBuilder(151);
        out.put(0, ValueType.A); //Don't send offset, we don't actually use it client-side cause we sendPosition first.
        out.putShort(object.getId(), ByteOrder.LITTLE);
        out.put((byte) ((object.getType() << 2) + (object.getRotation() & 3)), ValueType.S);
        player.getSession().write(out);
        //System.out.println("Sending object packet. For object: "+object.toString());
        return this;
    }
    public PacketSender sendFrame77(int interfaceid, int completed) {
        PacketBuilder out = new PacketBuilder(77);
        out.putInt(interfaceid);
        out.putInt(completed);
        player.getSession().write(out);
        return this;

    }
    public PacketSender sendObjectRemoval(GameObject object) {
        //System.out.println("Object details: " + object.toString());
        sendPosition(object.tile());
        PacketBuilder out = new PacketBuilder(101);
        out.put((object.getType() << 2) + (object.getRotation() & 3), ValueType.C);
        //System.out.println("Sending value" + (((object.getX() & 0x7) << 4) | (object.getY() & 0x7)));
        out.put(0); //Don't send offset, we don't actually use it client-side cause we sendPosition first.
        player.getSession().write(out);
        //System.out.println("Sending object removal packet. For object: "+object.toString());
        return this;
    }

    public PacketSender sendObjectAnimation(GameObject object, int anim) {
        sendPosition(object.tile());
        PacketBuilder out = new PacketBuilder(160);
        out.put(0, ValueType.S);
        out.put((object.getType() << 2) + (object.getRotation() & 3), ValueType.S);
        out.putShort(anim, ValueType.A);
        player.getSession().write(out);
        return this;
    }

    public PacketSender createGroundItem(GroundItem groundItem) {
        sendPosition(groundItem.getTile());
        PacketBuilder out = new PacketBuilder(44);
        out.putShort(groundItem.getItem().getId(), ValueType.A, ByteOrder.LITTLE);
        //ken comment, also changed this in Client.java opcode == ClientToServerPackets.CREATE_GROUND_ITEMS to allow for interfaces greater than 80000.
        //out.putShort(groundItem.getItem().getAmount());
        out.putInt(groundItem.getItem().getAmount());
        out.putShort(0);
        player.getSession().write(out);
        return this;
    }

    public PacketSender deleteGroundItem(GroundItem groundItem) {
        sendPosition(groundItem.getTile());
        PacketBuilder out = new PacketBuilder(156);
        out.put(0, ValueType.S);
        out.putShort(groundItem.getItem().getId());
        player.getSession().write(out);
        return this;
    }

    public PacketSender updateGroundItemAmount(int oldAmt, GroundItem groundItem) {
        sendPosition(groundItem.getTile());
        PacketBuilder out = new PacketBuilder(84);
        out.put(groundItem.getTile().getLevel(), ValueType.STANDARD);
        out.putShort(groundItem.getItem().getId());
        out.putShort(oldAmt);
        out.putShort(groundItem.getItem().getAmount());
        player.getSession().write(out);
        return this;
    }

    /**
     * Deletes spawns related to regions, such as ground items and objects.
     *
     * @return
     */
    public PacketSender deleteRegionalSpawns() {
        PacketBuilder out = new PacketBuilder(178);
        player.getSession().write(out);
        return this;
    }

    /*
     * Sends the modified trade slot to the interacting player
     */
    public PacketSender sendModified(int slot) {
        PacketBuilder out = new PacketBuilder(180);
        out.put(slot);
        player.getSession().write(out);
        return this;
    }

    public PacketSender sendPosition(final Tile tile) {
        final Tile other = player.getLastKnownRegion();
        PacketBuilder out = new PacketBuilder(85);
        out.put(tile.getY() - 8 * other.getRegionY(), ValueType.C);
        out.put(tile.getX() - 8 * other.getRegionX(), ValueType.C);
        player.getSession().write(out);
        return this;
    }

    public PacketSender(Player player) {
        this.player = player;
    }

    private final Player player;

    /**
     * Sends a projectile to a location.
     *
     * @param tile
     *            The starting location.
     * @param offset
     *            The finishing location.
     * @param id
     *            The graphic id.
     * @param delay
     *            The delay before showing the projectile.
     * @param angle
     *            The angle the projectile is coming from.
     * @param speed
     *            The speed the projectile travels at.
     * @param startHeight
     *            The starting height of the projectile.
     * @param endHeight
     *            The ending height of the projectile.
     * @param lockon
     *            The lockon index of the projectile, so it follows them if they
     *            move.
     * @param slope
     *            The slope at which the projectile moves.
     * @param radius
     *            The radius from the centre of the tile to display the
     *            projectile from.
     * @return The packet sender instance, for chaining.
     */
    public PacketSender sendProjectile(Tile tile, Tile offset, int angle, int speed, int id, int startHeight, int endHeight, int lockon, int delay, int slope, int radius) {
        sendPosition(tile);
        PacketBuilder out = new PacketBuilder(117);
        out.put(angle);
        out.put(offset.getY());
        out.put(offset.getX());
        out.putShort(lockon);
        out.putShort(id);
        out.put(startHeight);
        out.put(endHeight);
        out.putShort(delay);
        out.putShort(speed);
        out.put(slope);
        out.put(radius);
        player.getSession().write(out);
        //System.out.println(String.format("pos %s offset %s angle %d speed %d id %d startHeight %d endHeight %d lockon %d delay %d slope %d radius %d%n", position, offset, angle, speed, id, startHeight, endHeight, lockon, delay, slope, radius));
        return this;
    }

    public PacketSender confirm(int state, int value) {
        PacketBuilder out = new PacketBuilder(213);
        out.putShort(state).putShort(value);
        player.getSession().write(out);
        return this;
    }

    /**
     * Sends packet 79 to set an interface's scroll position.
     *
     * @param childId        The childId of the interface.
     * @param scrollPosition The value of the scroll position.
     */
    public PacketSender setScrollPosition(final int childId, final int scrollPosition, final int scrollMax) {
        PacketBuilder out = new PacketBuilder(79);
        out.putShort(childId);
        out.putShort(scrollPosition);
        out.putShort(scrollMax);
        player.getSession().write(out);
        return this;
    }
}
