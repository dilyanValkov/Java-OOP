package bank.core;
import bank.entities.bank.Bank;
import bank.entities.bank.BranchBank;
import bank.entities.bank.CentralBank;
import bank.entities.client.Adult;
import bank.entities.client.Client;
import bank.entities.client.Student;
import bank.entities.loan.Loan;
import bank.entities.loan.MortgageLoan;
import bank.entities.loan.StudentLoan;
import bank.repositories.LoanRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import static bank.common.ConstantMessages.*;
import static bank.common.ExceptionMessages.INVALID_CLIENT_TYPE;
import static bank.common.ExceptionMessages.NO_LOAN_FOUND;


public class ControllerImpl implements Controller {

    private final LoanRepository loans;
    private final Collection<Bank> banks;

    public ControllerImpl() {
        this.loans = new LoanRepository();
        this.banks = new ArrayList<>();
    }

    @Override
    public String addBank(String type, String name) {
        switch (type) {
            case "CentralBank":
                Bank centralBank = new CentralBank(name);
                banks.add(centralBank);
                break;
            case "BranchBank":
                Bank branchBank = new BranchBank(name);
                banks.add(branchBank);
                break;
            default:
                throw new IllegalArgumentException("Invalid bank type.");
        }
        return String.format("%s is successfully added.", type);
    }
    @Override
    public String addLoan(String type) {
        switch (type) {
            case "StudentLoan":
                Loan studentLoan = new StudentLoan();
                loans.addLoan(studentLoan);
                break;
            case "MortgageLoan":
                Loan mortgageLoan = new MortgageLoan();
                loans.addLoan(mortgageLoan);
                break;
            default:
                throw new IllegalArgumentException("Invalid loan type.");
        }
        return String.format("%s is successfully added.", type);
    }

    @Override
    public String returnedLoan(String bankName, String loanType) {
        Loan loan = loans.findFirst(loanType);
        if (loan == null){
            throw new IllegalArgumentException(String.format(NO_LOAN_FOUND,loanType));
        }
        Bank bank = banks.stream().filter(b ->
                b.getName().equals(bankName)).findFirst().get();
        bank.addLoan(loan);
        this.loans.removeLoan(loan);
        return String.format(SUCCESSFULLY_ADDED_CLIENT_OR_LOAN_TO_BANK,loanType,bankName);
    }

//        for (Bank bank : banks) {
//            if (bank.getName().equals(bankName)) {
//                Loan returnedLoan = loans.findFirst(loanType);
//                if (returnedLoan == null) {
//                    throw new IllegalArgumentException(String.format("Loan of type %s is missing.", loanType));
//                }
//                bank.addLoan(returnedLoan);
//                loans.removeLoan(returnedLoan);
//                break;
//            }
//        }
//
//        return String.format("%s successfully added to %s.", loanType, bankName);


    @Override
    public String addClient(String bankName, String clientType, String clientName, String clientID, double income) {

        Client client = null;
        if (clientType.equals("Adult")){
            client = new Adult(clientName,clientID,income);
        }else if (clientType.equals("Student")){
            client = new Student(clientName,clientID,income);
        }else {
            throw new IllegalArgumentException(INVALID_CLIENT_TYPE);
        }
        Bank bank = banks.stream().filter(bank1 ->
                bank1.getName().equals(bankName)).findFirst().get();

        String output;
        if (!isSuitableBank(clientType,bank)){
            output = UNSUITABLE_BANK;
        }else {
            bank.addClient(client);
            output = String.format(SUCCESSFULLY_ADDED_CLIENT_OR_LOAN_TO_BANK,clientType,bankName);
        }
        return output;

    }
    private boolean isSuitableBank(String clientType, Bank bank) {
        String bankType = bank.getClass().getSimpleName();
        if (clientType.equals("Adult") && bankType.equals("CentralBank")){
            return true;
        } else if (clientType.equals("Student") && bankType.equals("BranchBank")){
            return true;
        }
        return false;
    }

    @Override
    public String finalCalculation(String bankName) {
        double fund = 0;
        for (Bank bank : banks) {
            if (bank.getName().equals(bankName)) {
                double sumIncomeOfClients = bank.getClients().stream().mapToDouble(Client::getIncome).sum();
                double sumAmountOfLoans = bank.getLoans().stream().mapToDouble(Loan::getAmount).sum();
                fund = sumAmountOfLoans + sumIncomeOfClients;
                break;
            }
        }

        return String.format("The funds of bank %s is %.2f.", bankName, fund);
    }

    @Override
    public String getStatistics() {

        StringBuilder sb = new StringBuilder();
        return this.banks.stream()
                .map(Bank::getStatistics)
                .collect(Collectors.joining(System.lineSeparator())).trim();

//        StringBuilder sb = new StringBuilder();
//        for (Bank bank : banks) {
//            sb.append(bank.getStatistics());
//        }
//
//        return sb.toString();
    }
    }

