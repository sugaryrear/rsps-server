package com.ferox.game.content.areas.edgevile.dialogue;

import com.ferox.game.GameConstants;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.dialogue.Expression;

import static com.ferox.util.NpcIdentifiers.DONATOR_STORE;

/**
 * @author Patrick van Elderen | April, 22, 2021, 11:21
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class PaymentManagerDialogue extends Dialogue {

    @Override
    protected void start(Object... parameters) {
        send(DialogueType.NPC_STATEMENT, DONATOR_STORE, Expression.HAPPY, "Hey there player!", "Interested in supporting the server?");
        setPhase(0);
    }

    @Override
    protected void next() {
        if(isPhase(0)) {
            send(DialogueType.PLAYER_STATEMENT, Expression.HAPPY, "Uhh what's in it for me?");
            setPhase(1);
        } else if(isPhase(1)) {
            send(DialogueType.NPC_STATEMENT, DONATOR_STORE, Expression.HAPPY, "Well you can gain a pretty unfair advantage", "over others in terms of gear!");
            setPhase(2);
        } else if(isPhase(2)) {
            send(DialogueType.NPC_STATEMENT, DONATOR_STORE, Expression.HAPPY, "What would you like to know?");
            setPhase(3);
        } else if(isPhase(3)) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "How do I donate?", "What sort of things can I donate for?", "What are the different tiers of members?", "How many $ have I spent in total?");
            setPhase(4);
        } else if(isPhase(5)) {
            send(DialogueType.NPC_STATEMENT, DONATOR_STORE, Expression.HAPPY, "Donations are currently being taken through the website", "on the Store page. Simply visit " + GameConstants.WEBSITE_URL + "", "and head to the store.");
            setPhase(6);
        } else if(isPhase(6)) {
            send(DialogueType.NPC_STATEMENT, DONATOR_STORE, Expression.HAPPY, "As it stands, you can make payments through Paypal", "OR through", "payment methods and OSGP or BTC, Only Sugary", "Kudos, and Precise take OSGP and BTC!");
            setPhase(7);
        } else if(isPhase(7)) {
            send(DialogueType.NPC_STATEMENT, DONATOR_STORE, Expression.HAPPY, "Would you like me to open the page for you?");
            setPhase(8);
        } else if(isPhase(8)) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes.", "No.");
            setPhase(9);
        } else if(isPhase(10)) {
            send(DialogueType.NPC_STATEMENT, DONATOR_STORE, Expression.HAPPY, "I've quite a wide selection and I'm always looking to", " expand! Right now, we have bonds, which when opened", "give you membership AND donator tickets for the store.");
            setPhase(11);
        } else if(isPhase(10)) {
            send(DialogueType.NPC_STATEMENT, DONATOR_STORE, Expression.HAPPY, "Would you like to take a look now?");
            setPhase(11);
        } else if(isPhase(11)) {
            send(DialogueType.OPTION, "Would you like to view our online webstore?", "Yes.", "No.");
            setPhase(12);
        } else if(isPhase(13)) {
            send(DialogueType.NPC_STATEMENT, DONATOR_STORE, Expression.HAPPY, "Ahh, yes! You want to know about the different titles.", "Of course, of course.");
            setPhase(14);
        } else if(isPhase(14)) {
            send(DialogueType.NPC_STATEMENT, DONATOR_STORE, Expression.HAPPY, "Right now, there's 5 available tiers, ranging from all", "different amounts. As you spend more, you'll automatically", "increase in title. I am in the highest possible tier,", "but that's to be expected.");
            setPhase(15);
        } else if(isPhase(15)) {
            send(DialogueType.NPC_STATEMENT, DONATOR_STORE, Expression.HAPPY, "There's the standard member, which you'll likely", "see most people have.");
            setPhase(16);
        } else if(isPhase(16)) {
            send(DialogueType.NPC_STATEMENT, DONATOR_STORE, Expression.HAPPY, "Next comes the Super Member, a step above.");
            setPhase(17);
        } else if(isPhase(17)) {
            send(DialogueType.NPC_STATEMENT, DONATOR_STORE, Expression.HAPPY, "Following that, there's the Extreme Members.");
            setPhase(18);
        } else if(isPhase(18)) {
            send(DialogueType.NPC_STATEMENT, DONATOR_STORE, Expression.HAPPY, "Afterward, you'll find the Legendary Members, a growing", "prestigious rank.");
            setPhase(19);
        } else if(isPhase(18)) {
            send(DialogueType.NPC_STATEMENT, DONATOR_STORE, Expression.HAPPY, "And finally, and the highest rank of all, is the V.I.P Members.");
            setPhase(19);
        } else if(isPhase(19)) {
            send(DialogueType.NPC_STATEMENT, DONATOR_STORE, Expression.HAPPY, "There may be more ranks to come, but those are all", "I know of at the moment! As great as I may be,", "the decisions do come down from the top.");
            setPhase(20);
        } else if(isPhase(20)) {
            send(DialogueType.PLAYER_STATEMENT, Expression.HAPPY, "Awesome, thanks!");
            setPhase(21);
        } else if(isPhase(21)) {
            stop();
        } else if(isPhase(22)) {
            send(DialogueType.NPC_STATEMENT, DONATOR_STORE, Expression.HAPPY, "You have spent a total of $" + player.<Double>getAttribOr(AttributeKey.TOTAL_PAYMENT_AMOUNT,0D) + "0.");
            setPhase(21);
        }
    }

    @Override
    protected void select(int option) {
        if(isPhase(4)) {
            if(option == 1) {
                send(DialogueType.PLAYER_STATEMENT, Expression.HAPPY, "How do I donate?");
                setPhase(5);
            }
            if(option == 2) {
                send(DialogueType.PLAYER_STATEMENT, Expression.HAPPY, "What sort of items am I able to donate for?");
                setPhase(10);
            }
            if(option == 3) {
                send(DialogueType.PLAYER_STATEMENT, Expression.HAPPY, "What are the different available tiers of donation?");
                setPhase(13);
            }
            if(option == 4) {
                send(DialogueType.PLAYER_STATEMENT, Expression.HAPPY, "How many $ have I spent in total?");
                setPhase(22);
            }
        } else if(isPhase(9)) {
            if(option == 1) {
                player.getPacketSender().sendURL("http://righteouspk.com/store/index.php");
                stop();
            }
            if(option == 2) {
                stop();
            }
        } else if(isPhase(12)) {
            if(option == 1) {
                player.getPacketSender().sendURL("http://righteouspk.com/store/index.php");
                stop();
            }
            if(option == 2) {
                stop();
            }
        }
    }
}
