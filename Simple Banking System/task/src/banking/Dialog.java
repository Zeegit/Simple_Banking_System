package banking;


import banking.dbService.DBException;
import banking.dbService.Database;
import banking.dbService.datasets.CardDataset;

import java.util.*;

public class Dialog {
    Scanner scanner = new Scanner(System.in);
    Database database;

    public Dialog(Database database) {
        this.database = database;

    }

    public void showWelcomeDialog() {
        System.out.println("1. Create account\n2. Log into account\n0. Exit");
    }

    public void execute() throws DBException {

        String action;
        do {
            showWelcomeDialog();
            action = scanner.nextLine();
            switch (action) {
                case "1":
                    createAccount();
                    break;
                case "2":
                    action = checkAccount();
                    break;
            }
        } while (!action.equals("0"));
        System.out.println("\nBye!");
    }

    private String checkAccount() throws DBException {
        System.out.println("\nEnter your card number:");
        String number = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();

        CardDataset card = database.getCardByNumberAndPin(number, pin);

        String retAction = "";
        if (card.getId() != 0) {
            retAction = doCardActions(number, pin);
        } else {
            System.out.println("Wrong card number or PIN!\n");
        }
        return retAction;
    }

    private String doCardActions(String number, String pin) throws DBException {
        System.out.println("\nYou have successfully logged in!\n");
        String action;

        do {
            showBalanceDialog();
            action = scanner.nextLine();
            switch (action) {
                case "1":
                    showBalance(number, pin);
                    break;
                case "2":
                    addIncome(number, pin);
                    break;
                case "3":
                    doTransfer(number, pin);
                    break;
                case "4":
                    closeCard(number, pin);
                    action = "5";
                    break;
            }
        } while (!(action.equals("0") || action.equals("5")));
        System.out.println("\nYou have successfully logged out!\n");
        return action;
    }

    private void doTransfer(String number, String pin) throws DBException {
        System.out.println("\nEnter your card number:");
        String toNumber = scanner.nextLine();

        if (number.equals(toNumber)) {
            System.out.println("You can't transfer money to the same account!\n");
            return;
        }

        if (!checkLuhnAlgorithm(toNumber)) {
            System.out.println("Probably you made mistake in card number. Please try again!\n");
            return;
        }

        CardDataset toCard = database.getCard(number, "");

        if (toCard.getId() == 0) {
            System.out.println("Such a card does not exist.\n");
            return;
        }

        System.out.println("\nEnter sum to transfer:");
        int sum = Integer.parseInt(scanner.nextLine());

        CardDataset card = database.getCard(number, pin);
        if (sum > card.getBalance()) {
            System.out.println("No money\n");
            return;
        }

        database.doTransfer(number, toNumber, sum);

    }

    private void addIncome(String number, String pin) throws DBException {
        System.out.println("\nEnter sum to add:");
        int sum = Integer.parseInt(scanner.nextLine());
        database.addIncome(number, pin, sum);
        System.out.println("");
    }

    private void closeCard(String number, String pin) throws DBException {
        database.deleteCard(number, pin);
    }

    private void showBalance(String number, String pin) throws DBException {
        CardDataset card = database.getCard(number, pin);
        if (card != null) {
            System.out.println("\nBalance: " + card.getBalance());
        } else {
            System.out.println("\nWrong card number or PIN!");
        }
    }

    private void showBalanceDialog() {
        System.out.println("1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit");
    }

    private boolean checkLuhnAlgorithm(String number) {
        int result = 0;
        for (int i = 0; i < number.length(); i++) {
            int digit = Character.getNumericValue(number.charAt(i));
            if (i % 2 == 0) {
                int doubleDigit = digit * 2 > 9 ? digit * 2 - 9 : digit * 2;
                result += doubleDigit;
                continue;
            }
            result += digit;
        }
        return result % 10 == 0;
    }

    private void createAccount() throws DBException {
        String number = "400000" + String.valueOf(generateRandomIntIntRange(100_000_000, 999_999_999));

        int result = 0;
        for (int i = 0; i < number.length(); i++) {
            int digit = Character.getNumericValue(number.charAt(i));
            if (i % 2 == 0) {
                int doubleDigit = digit * 2 > 9 ? digit * 2 - 9 : digit * 2;
                result += doubleDigit;
                continue;
            }
            result += digit;
        }

        int ch = result % 10 == 0 ? 0 : 10 - result % 10;
        number = number.concat(Integer.toString(ch));

        String pin0 = "0000" + String.valueOf(generateRandomIntIntRange(9, 9999));
        String pin = pin0.substring(pin0.length() - 4);

        database.addCard(number, pin, 0);

        System.out.println();
        System.out.println("Your card have been created");
        System.out.println("Your card number:");
        System.out.println(number);
        System.out.println("Your card PIN:");
        System.out.println(pin);
        System.out.println();
    }

    public int generateRandomIntIntRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
