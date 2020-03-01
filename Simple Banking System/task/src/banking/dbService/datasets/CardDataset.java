package banking.dbService.datasets;

public class CardDataset {
    private int id;
    private String number;
    private String pin;
    private int balance;

    public CardDataset() {
    }

    public CardDataset(int id, String number, String pin, int balance) {
        this.id = id;
        this.number = number;
        this.pin = pin;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }
}
