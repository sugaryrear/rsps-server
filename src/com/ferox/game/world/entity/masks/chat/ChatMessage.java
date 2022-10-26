package com.ferox.game.world.entity.masks.chat;

/**
 * Represents a player's chat message.
 * 
 * @author relex lawl
 */

public class ChatMessage {
    /**
     * The color of the message.
     */
    private int colour;
    /**
     * The effects of the message.
     */
    private int effects;
    /**
     * The actual text of the message.
     */
    private byte[] text;

    /**
     * The Message constructor.
     *
     * @param colour  The color the message will have, done through color(#):
     * @param effects The effect the message will have, done through effect(#):
     * @param text    The actual message it will have.
     */
    public ChatMessage(int colour, int effects, byte[] text) {
        this.colour = colour;
        this.effects = effects;
        this.text = text;
    }

    /**
     * Gets the message's chat color.
     *
     * @return colour.
     */
    public int getColour() {
        return colour;
    }

    /**
     * Gets the message's chat effect.
     *
     * @return effects.
     */
    public int getEffects() {
        return effects;
    }

    /**
     * Gets the message's actual text in byte form.
     *
     * @return text.
     */
    public byte[] getText() {
        return text;
    }
}
