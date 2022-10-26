package com.ferox.db.transactions;

import com.ferox.db.VoidDatabaseTransaction;
import com.ferox.db.statement.NamedPreparedStatement;
import com.ferox.game.GameConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class ServerOnlineDatabaseTransaction extends VoidDatabaseTransaction {
    private static final Logger logger = LogManager.getLogger(ServerOnlineDatabaseTransaction.class);
    int id;
  int amount;

    public ServerOnlineDatabaseTransaction(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    @Override
    public void executeVoid(Connection connection) throws SQLException {
        if(!GameConstants.production)
            return;
        try (NamedPreparedStatement statement = prepareStatement(connection,"UPDATE serveronline SET amount = :amount WHERE id = :id")) {
            statement.setInt("id", 1);
            statement.setInt("amount", amount);
            logger.info("Executing query: " + statement.toString());
            statement.executeUpdate();
        }
    }

    @Override
    public void exceptionCaught(Throwable cause) {
      //  logger.error("There was an error updating the hs_users table for Player " + username + ": ");
        logger.catching(cause);
    }
}
