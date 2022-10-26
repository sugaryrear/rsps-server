package com.ferox.game.world.entity.dialogue;

import com.ferox.game.world.entity.masks.animations.Animation;

/**
 * Represents the expressions a chat dialogue head can make
 *
 * @author Arithium
 *
 */
public enum Expression {

    NODDING_ONE(554),
    NODDING_TWO(555),
    DULL(560),
    DULL_TWO(562),
    NODDING_THREE(567),
    NODDING_FIVE(568),
    SHAKING_HEAD_ONE(569),
    SHAKING_HEAD_THREE(570),
    NODDING_FOUR(571),
    NODDING(572),
    SHAKING_HEAD_TWO(575),
    H(576),
    H1(577),
    HAPPY(588),
    ANXIOUS(589),
    CALM_TALK(590),
    DEFAULT(591),
    EVIL(592),
    BAD(593),
    WICKED(594),
    ANNOYED(595),
    DISTRESSED(596),
    AFFLICTED(597),
    DRUNK_LEFT(600),
    DRUNK_RIGHT(601),
    NOT_INTERESTED(602),
    SLEEPY(603),
    PLAIN_EVIL(604),
    LAUGH(605),
    SNIGGER(606),
    HAVE_FUN(607),
    GUFFAW(608),
    EVIL_LAUGH_SHORT(609),
    SLIGHTLY_SAD(610),
    SAD(599),
    VERY_SAD(611),
    ON_ONE_HAND(612),
    ALMOST_CRYING(598),
    NEARLY_CRYING(613),
    ANGRY(614),
    FURIOUS(615),
    ENRAGED(616),
    MAD(617),
    OLM(7399),
    OLM_LAUGH(7400),
    PHOENIX(6814);

    /**
     * The DialogueExpression constructor.
     * @param animationId    The id of the animation for said expression.
     */
    private Expression(int animationId) {
        animation = new Animation(animationId);
    }

    /**
     * The animation the dialogue head model will perform.
     */
    private final Animation animation;

    /**
     * Gets the animation for dialogue head model to perform.
     * @return    animation.
     */
    public Animation getAnimation() {
        return animation;
    }
}
