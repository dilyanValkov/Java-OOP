package bank.entities.bank;
import bank.common.ExceptionMessages;
import bank.entities.client.Client;
import bank.entities.loan.Loan;
import java.util.ArrayList;
import java.util.Collection;


public abstract class BaseBank implements Bank {

    private String name;

    private int capacity;

    private Collection <Loan> loans;

    private Collection <Client> clients;

    public BaseBank(String name, int capacity) {
        setName(name);
        this.capacity = capacity;
        this.loans = new ArrayList<>();
        this.clients = new ArrayList<>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException(ExceptionMessages.BANK_NAME_CANNOT_BE_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    @Override
    public Collection<Client> getClients() {
        return clients;
    }

    @Override
    public Collection<Loan> getLoans() {
        return loans;
    }

    @Override
    public void addClient(Client client) {
        if(clients.size() >= capacity){
            throw new IllegalStateException(ExceptionMessages.NOT_ENOUGH_CAPACITY_FOR_CLIENT);
        }
        clients.add(client);
    }

    @Override
    public void removeClient(Client client) {
        clients.remove(client);
    }

    @Override
    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    @Override
    public int sumOfInterestRates() {
        return this.loans.stream().mapToInt(Loan::getInterestRate).sum();
    }

    @Override
    public  String getStatistics(){


        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Name: %s, Type: %s\n", name, getClass().getSimpleName()));
        sb.append("Clients: ");


        int index = 0;

        if(clients.isEmpty()){
            sb.append("none");
        }else{
            for (Client client : clients) {
                String clientName = client.getName();
                sb.append(clientName);
                if(index < clients.size() - 1){
                    sb.append(", ");
                    index++;
                }
            }
        }


        sb.append(System.lineSeparator());
        sb.append(String.format("Loans: %d, Sum of interest rates: %d", loans.size(), sumOfInterestRates()));
        sb.append(System.lineSeparator());

        return sb.toString();

    }
}
