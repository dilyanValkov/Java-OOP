package bank.entities.loan;

public class StudentLoan extends BaseLoan {

    private final static int INTEREST_RATE = 1;
    private final static double AMOUNT = 10000;
    public StudentLoan() {
        super(INTEREST_RATE, AMOUNT);
    }
}
