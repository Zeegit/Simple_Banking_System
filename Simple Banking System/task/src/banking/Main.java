package banking;

import banking.dbService.DBException;
import banking.dbService.Database;

import java.math.BigDecimal;
import java.sql.DatabaseMetaData;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws DBException {

        String dbName = "card.s3db.";
        for (int i = 0; i < args.length; i++) {
            if ("-fileName ".equals(args[i]) && i + 1 < args.length) {
                dbName = args[i + 1];
            }
        }

        Database database = new Database(dbName);
        database.createTable();

        // if (!database.isConnected()) {}

        Dialog dialog = new Dialog(database);
        dialog.execute();

    }
}
