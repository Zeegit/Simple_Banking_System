package banking.dbService.dao;

import banking.dbService.datasets.CardDataset;
import banking.dbService.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;

public class CardDAO {
    private Executor executor;

    public CardDAO(Connection connection) {
        this.executor = new Executor(connection);
    }

    public CardDataset getCard(String number, String pin) throws SQLException {
        return executor.execQuery(
                "select * from card where number='" + number + "'",
                result -> {

                    //if (!result.next()) return new CardDataset();
                    result.next();
                    if (result.isClosed())
                        return new CardDataset();
                    else
                        return new CardDataset(result.getInt("id"), result.getString("number"), result.getString("pin"), result.getInt("balance"));
                });
    }

    public void createTable() throws SQLException {
        executor.execUpdate("CREATE TABLE IF NOT EXISTS card (id INTEGER PRIMARY KEY,number TEXT NOT NULL, pin TEXT NOT NULL, balance INTEGER DEFAULT 0)");
    }

    public void insertCard(String number, String pin, int balance) throws SQLException {
        executor.execUpdate("INSERT INTO card (number, pin, balance) VALUES ('" + number + "','" + pin + "'," + balance + ")");
    }

    public int getCardId(String number) throws SQLException {
        return executor.execQuery("select * from card where number='" + number + "'", result -> {
            result.next();
            return result.getInt(1);
        });
    }

    public void deleteCard(String number, String pin) throws SQLException {
        executor.execUpdate("delete from card where number='" + number + "'");
    }

    public void addIncome(String number, String pin, int sum) throws SQLException {
        executor.execUpdate("update card set balance = balance+" + sum + " where number='" + number + "'");
    }

    public CardDataset getCardByNumberAndPin(String number, String pin) throws SQLException {
        return executor.execQuery(
                "select * from card where number='" + number + "' and pin='" + pin + "'",
                result -> {

                    //if (!result.next()) return new CardDataset();
                    result.next();
                    if (result.isClosed())
                        return new CardDataset();
                    else
                        return new CardDataset(result.getInt("id"), result.getString("number"), result.getString("pin"), result.getInt("balance"));
                });
    }
}
