package bank.entities.client;

public class Student extends BaseClient {
    private final static int INTEREST = 2;
    public Student(String name, String ID, double income) {
        super(name, ID, INTEREST, income);
    }

    @Override
    public void increase() {
          setInterest(getInterest() + 1);
    }
}
