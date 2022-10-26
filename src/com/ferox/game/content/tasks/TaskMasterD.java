package com.ferox.game.content.tasks;

import com.ferox.game.content.tasks.impl.Tasks;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.dialogue.Expression;

import static com.ferox.util.NpcIdentifiers.TOWN_CRIER;


/**
 * @author Patrick van Elderen | April, 12, 2021, 12:33
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class TaskMasterD extends Dialogue {

    @Override
    protected void start(Object... parameters) {
        var dailyTaskDate = player.<Integer>getAttribOr(AttributeKey.DAILY_TASK_DATE,0);
        var completedDailyTask = player.<Boolean>getAttribOr(AttributeKey.COMPLETED_DAILY_TASK,false);
        if (dailyTaskDate == TaskMasterManager.getTodayDate() && !player.getTaskMasterManager().hasTask() && completedDailyTask == true) {
            player.message("@red@You have already completed your daily task for today");
            return;
        }
        send(DialogueType.NPC_STATEMENT, TOWN_CRIER, Expression.CALM_TALK, "Hi there " + player.getUsername() + ", would you like a task?");
        setPhase(0);
    }

    @Override
    protected void next() {
        if (isPhase(0)) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes I would like a PVP task.", "Yes I would like a Skilling task.", "Yes I would like a PVM task.", "Nevermind.");
            setPhase(1);
        } else if (isPhase(2)) {
            player.getTaskMasterManager().giveTask(true,false,false);
            send(DialogueType.NPC_STATEMENT, TOWN_CRIER, Expression.CALM_TALK, "You have been given a PvP task.");
            setPhase(6);
        } else if (isPhase(3)) {
            player.getTaskMasterManager().giveTask(false,true,false);
            send(DialogueType.NPC_STATEMENT, TOWN_CRIER, Expression.CALM_TALK, "You have been given a Skilling task.");
            setPhase(6);
        } else if (isPhase(4)) {
            player.getTaskMasterManager().giveTask(false,false,true);
            send(DialogueType.NPC_STATEMENT, TOWN_CRIER, Expression.CALM_TALK, "You have been given a PvM task.");
            setPhase(6);
        } else if (isPhase(5)) {
            stop();
        } else if (isPhase(6)) {
            player.getTaskMasterManager().open();
        } else if (isPhase(7)) {
            send(DialogueType.NPC_STATEMENT, TOWN_CRIER, Expression.CALM_TALK, "Would you like to reset your task for 100,000 gold?");
            setPhase(8);
        } else if (isPhase(8)) {
            player.costBMAction(5000, "Reset your task for 100,000 gp?", () -> {
                player.getTaskMasterManager().resetTask();
                setPhase(9);
            });
        } else if (isPhase(9)) {
            send(DialogueType.NPC_STATEMENT, TOWN_CRIER, Expression.CALM_TALK, "Your task has been reset.");
            setPhase(5);
        }
    }

    @Override
    protected void select(int option) {
        if (isPhase(1)) {
            if (option == 1) {
                if(player.getTaskMasterManager().hasTask()) {
                    send(DialogueType.NPC_STATEMENT, TOWN_CRIER, Expression.CALM_TALK, "You already have a task.");
                    setPhase(7);
                    return;
                }
                send(DialogueType.PLAYER_STATEMENT,Expression.HAPPY, "Yes I would like a PVP task.");
                setPhase(2);
            } else if (option == 2) {
                if(player.getTaskMasterManager().hasTask()) {
                    send(DialogueType.NPC_STATEMENT, TOWN_CRIER, Expression.CALM_TALK, "You already have a task.");
                    setPhase(7);
                    return;
                }
                send(DialogueType.PLAYER_STATEMENT,Expression.HAPPY, "Yes I would like a Skilling task.");
                setPhase(3);
            } else if (option == 3) {
                if(player.getTaskMasterManager().hasTask()) {
                    send(DialogueType.NPC_STATEMENT, TOWN_CRIER, Expression.CALM_TALK, "You already have a task.");
                    setPhase(7);
                    return;
                }
                send(DialogueType.PLAYER_STATEMENT,Expression.HAPPY, "Yes I would like a PVM task.");
                setPhase(4);
            } else if (option == 4) {
                send(DialogueType.PLAYER_STATEMENT,Expression.ANNOYED, "I don't want a task at this time.");
                setPhase(5);
            }
        }
    }
}
