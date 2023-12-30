package bank.entities.bank;

public class BranchBank extends BaseBank {
    private final static int CAPACITY = 25;
    public BranchBank(String name) {
        super(name, CAPACITY);
    }
}
