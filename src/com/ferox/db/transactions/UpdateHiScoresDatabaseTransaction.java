package com.ferox.db.transactions;

import com.ferox.db.VoidDatabaseTransaction;
import com.ferox.db.statement.NamedPreparedStatement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class UpdateHiScoresDatabaseTransaction extends VoidDatabaseTransaction {
    private static final Logger logger = LogManager.getLogger(UpdateKdrDatabaseTransaction.class);
    private int rights;
private long overall_xp;
private int attack_xp, defence_xp, strength_xp, constitution_xp, ranged_xp, prayer_xp, magic_xp, cooking_xp,
    woodcutting_xp, fletching_xp, fishing_xp, firemaking_xp, crafting_xp, smithing_xp, mining_xp, herblore_xp, agility_xp, thieving_xp,
    slayer_xp, farming_xp, runecrafting_xp, hunter_xp, construction_xp = 0;
   private String username;

    public UpdateHiScoresDatabaseTransaction(String username, int rights, long overall_xp, int attack_xp, int defence_xp,
                                             int strength_xp, int constitution_xp, int ranged_xp, int prayer_xp,
                                             int magic_xp, int cooking_xp, int woodcutting_xp, int fletching_xp, int fishing_xp,
        int firemaking_xp, int crafting_xp, int smithing_xp, int mining_xp, int herblore_xp, int agility_xp,
        int thieving_xp, int slayer_xp, int farming_xp, int runecrafting_xp, int hunter_xp, int construction_xp){
        this.username = username;
        this.rights = rights;
        this.overall_xp = overall_xp;
        this.attack_xp = attack_xp;
        this.defence_xp = defence_xp;
        this.strength_xp = strength_xp;
        this.constitution_xp = constitution_xp;
        this.ranged_xp = ranged_xp;
        this.prayer_xp = prayer_xp;
        this.magic_xp = magic_xp;
        this.cooking_xp = cooking_xp;
        this.woodcutting_xp = woodcutting_xp;
        this.fletching_xp = fletching_xp;
        this.fishing_xp = fishing_xp;
        this.firemaking_xp = firemaking_xp;
        this.crafting_xp = crafting_xp;
        this.smithing_xp = smithing_xp;
        this.mining_xp = mining_xp;
        this.herblore_xp = herblore_xp;
        this.agility_xp = agility_xp;
        this.thieving_xp = thieving_xp;
        this.slayer_xp = slayer_xp;
        this.farming_xp = farming_xp;
        this.runecrafting_xp = runecrafting_xp;
        this.hunter_xp = hunter_xp;
        this.construction_xp = construction_xp;

    }

    @Override
    public void executeVoid(Connection connection) throws SQLException {
        try (NamedPreparedStatement statement = prepareStatement(connection,"DELETE FROM hs_users WHERE lower(username) = :username")) {
            statement.setString("username", username.toLowerCase());
            logger.info("Executing query: " + statement.toString());
            statement.executeUpdate();
        }
        try (NamedPreparedStatement statement = prepareStatement(connection,"INSERT INTO hs_users (username,rights,overall_xp,attack_xp,defence_xp," +
            "strength_xp,constitution_xp,ranged_xp,prayer_xp,magic_xp,cooking_xp,woodcutting_xp,fletching_xp,fishing_xp,firemaking_xp," +
            "crafting_xp,smithing_xp,mining_xp,herblore_xp,agility_xp,thieving_xp,slayer_xp,farming_xp,runecrafting_xp,hunter_xp,construction_xp) VALUES (:username,:rights,:overall_xp,:attack_xp,:defence_xp," +
            ":strength_xp,:constitution_xp,:ranged_xp,:prayer_xp,:magic_xp,:cooking_xp,:woodcutting_xp,:fletching_xp,:fishing_xp,:firemaking_xp,:crafting_xp,:smithing_xp,:mining_xp," +
            ":herblore_xp,:agility_xp,:thieving_xp,:slayer_xp,:farming_xp,:runecrafting_xp,:hunter_xp,:construction_xp)")) {
            statement.setString("username", username.toLowerCase());
            statement.setInt("rights", rights);
            statement.setLong("overall_xp", overall_xp);
            statement.setInt("attack_xp", attack_xp);
            statement.setInt("defence_xp", defence_xp);
            statement.setInt("strength_xp", strength_xp);
            statement.setInt("constitution_xp", constitution_xp);
            statement.setInt("ranged_xp", ranged_xp);
            statement.setInt("prayer_xp", prayer_xp);
            statement.setInt("magic_xp", magic_xp);
            statement.setInt("cooking_xp", cooking_xp);
            statement.setInt("woodcutting_xp", woodcutting_xp);
            statement.setInt("fletching_xp", fletching_xp);
            statement.setInt("fishing_xp", fishing_xp);
            statement.setInt("firemaking_xp", crafting_xp);
            statement.setInt("crafting_xp", crafting_xp);
            statement.setInt("smithing_xp", smithing_xp);
            statement.setInt("mining_xp", mining_xp);
            statement.setInt("herblore_xp", herblore_xp);
            statement.setInt("agility_xp", agility_xp);
            statement.setInt("thieving_xp", thieving_xp);
            statement.setInt("slayer_xp", slayer_xp);
            statement.setInt("farming_xp", farming_xp);
            statement.setInt("runecrafting_xp", runecrafting_xp);
            statement.setInt("hunter_xp", hunter_xp);
            statement.setInt("construction_xp", construction_xp);

            logger.info("Executing query: " + statement.toString());
            statement.executeUpdate();
        }
    }

    @Override
    public void exceptionCaught(Throwable cause) {
        logger.error("There was an error updating the kdr column for Player " + username + ": ");
        logger.catching(cause);
    }
}
