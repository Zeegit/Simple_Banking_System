package banking.dbService;

import banking.dbService.dao.CardDAO;
import banking.dbService.datasets.CardDataset;

import java.sql.*;

public class Database {
    private final Connection connection;
    String url;

    public Database(String dbName) {
        this.url = "jdbc:sqlite:" + dbName;
        this.connection = createConnection();
    }

    public Connection createConnection() {
        try {
            Connection connection = DriverManager.getConnection(url);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    public CardDataset getCard(String number, String pin) throws DBException {
        try {
            return (new CardDAO(connection).getCard(number, pin));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void createTable() throws DBException {
        try {
            connection.setAutoCommit(false);
            CardDAO dao = new CardDAO(connection);
            dao.createTable();
        } catch (SQLException e) {
        try {
            connection.rollback();
        } catch (SQLException ignore) {
        }
        throw new DBException(e);
    } finally {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException ignore) {
        }
    }
    }

    public int addCard(String number, String pin, int balance) throws DBException {
        try {
            connection.setAutoCommit(false);
            CardDAO dao = new CardDAO(connection);
            dao.createTable();
            dao.insertCard(number, pin, balance);
            connection.commit();
            return dao.getCardId(number);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    public void deleteCard(String number, String pin) throws DBException {
        try {
            connection.setAutoCommit(false);
            CardDAO dao = new CardDAO(connection);
            dao.createTable();
            dao.deleteCard(number, pin);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    public void addIncome(String number, String pin, int sum) throws DBException {
        try {
            connection.setAutoCommit(false);
            CardDAO dao = new CardDAO(connection);
            dao.createTable();
            dao.addIncome(number, pin, sum);
            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    public void doTransfer(String number, String toNumber, int sum) throws DBException {
        try {
            connection.setAutoCommit(false);
            CardDAO dao = new CardDAO(connection);
            dao.createTable();
            dao.addIncome(number, "", -sum);
            dao.addIncome(toNumber, "", sum);
            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    public CardDataset getCardByNumberAndPin(String number, String pin) throws DBException {
        try {
            return (new CardDAO(connection).getCardByNumberAndPin(number, pin));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}
