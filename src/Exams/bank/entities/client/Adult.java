package bank.entities.client;

public class Adult extends BaseClient {

    private final static int INTEREST = 4;
    public Adult(String name, String ID, double income) {
        super(name, ID, INTEREST, income);
    }

    @Override
    public void increase() {
       setInterest(getInterest() + 2);
    }
}
