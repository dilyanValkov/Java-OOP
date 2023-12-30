package bank.entities.bank;

public class CentralBank extends BaseBank {
    private final static int CAPACITY = 50;
    public CentralBank(String name) {
        super(name, CAPACITY);
    }
}
