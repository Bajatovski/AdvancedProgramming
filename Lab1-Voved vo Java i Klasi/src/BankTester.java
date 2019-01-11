
import java.util.*;
import java.util.stream.Collectors;
import java.text.DecimalFormat;

public class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1)&&!a1.equals(a2)&&!a2.equals(a1)&&!a3.equals(a1)&&!a4.equals(a1)&&!a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1)&&!fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, Integer.parseInt(o));
        }
        return null;
    }


}

class Account {

    private String name;
    private long id;
    private String balance;

    public Account(String newName, String newBalance) {
        name = newName;
        setBalance(newBalance);
        Random random = new Random();
        id = random.nextLong();
    }

    public String getBalance() {
        return balance;
    }

    public static double getDouble(String s) {
        return Double.parseDouble(s.substring(0,s.length()-1));
    }

    public double getDoubleBalance() {
        return Double.parseDouble(balance.substring(0,balance.length()-1));
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setBalance(String newBalance) {
        DecimalFormat pom = new DecimalFormat("#0.00");
        balance = pom.format(getDouble(newBalance));
        balance += "$";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name).append("\nBalance: ").append(balance).append("\n");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((balance == null) ? 0 : balance.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Account other = (Account) obj;
        if (balance == null) {
            if (other.balance != null)
                return false;
        } else if (!balance.equals(other.balance))
            return false;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}

abstract class Transaction {

    private final long fromId;
    private final long toId;
    private String description;
    private String amount;

    public Transaction(long newFromId, long newToId, String newDescription, String newAmount) {
        fromId = newFromId;
        toId = newToId;
        description = newDescription;
        amount = newAmount;
    }

    public String getDescription() {
        return description;
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public String getAmount() {
        return amount;
    }

    public double getDoubleAmount() {
        return Double.parseDouble(amount.substring(0,amount.length()-1));
    }

    public abstract double getProvision();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + (int) (fromId ^ (fromId >>> 32));
        result = prime * result + (int) (toId ^ (toId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transaction other = (Transaction) obj;
        if (amount == null) {
            if (other.amount != null)
                return false;
        } else if (!amount.equals(other.amount))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (fromId != other.fromId)
            return false;
        if (toId != other.toId)
            return false;
        return true;
    }


}

class FlatAmountProvisionTransaction extends Transaction {

    private String flatProvision;

    public FlatAmountProvisionTransaction(long fromId, long toId, String amount, String flatProvision) {
        super(fromId,toId,"FlatAmount",amount);
        this.flatProvision = flatProvision;
    }

    public String getFlatAmount(){
        return flatProvision;
    }

    public double getDoubleFlatAmount() {
        return Double.parseDouble(flatProvision.substring(0,flatProvision.length()-1));
    }

    @Override
    public double getProvision() {
        return getDoubleFlatAmount();
    }

    @Override
    public double getDoubleAmount() {
        return super.getDoubleAmount() + getDoubleFlatAmount();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((flatProvision == null) ? 0 : flatProvision.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        FlatAmountProvisionTransaction other = (FlatAmountProvisionTransaction) obj;
        if (flatProvision == null) {
            if (other.flatProvision != null)
                return false;
        } else if (!flatProvision.equals(other.flatProvision))
            return false;
        return true;
    }


}

class FlatPercentProvisionTransaction extends Transaction {

    private int flatPercent;

    public FlatPercentProvisionTransaction(long fromId, long toId, String amount, int centsPerDollar) {
        super(fromId,toId,"FlatPercent",amount);
        flatPercent = centsPerDollar;
    }

    public int getPercent() {
        return flatPercent;
    }

    @Override
    public double getProvision() {
        return flatPercent / 100.0 * (int)super.getDoubleAmount();
    }

    @Override
    public double getDoubleAmount() {
        return super.getDoubleAmount() + getProvision();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + flatPercent;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        FlatPercentProvisionTransaction other = (FlatPercentProvisionTransaction) obj;
        if (flatPercent != other.flatPercent)
            return false;
        return true;
    }


}

class Bank {

    private String name;
    private Account[] accounts;
    private double totalTransfer;
    private double totalProvision;

    public Bank(String name, Account [] acc) {
        this.name = name;
        accounts = Arrays.copyOf(acc, acc.length);
        totalTransfer = 0;
        totalProvision = 0;
    }

    boolean makeTransaction(Transaction t) {
        int indexFrom = findID(t.getFromId());
        int indexTo = findID(t.getToId());

        if (indexFrom == -1)
            return false;
        if (indexTo == -1)
            return false;


        double balanceFrom = accounts[indexFrom].getDoubleBalance();
        double balanceTo = accounts[indexTo].getDoubleBalance();
        if ( balanceFrom < t.getDoubleAmount())
            return false;


        double totalSum = t.getDoubleAmount();
        double provision = t.getProvision();
        totalTransfer += (totalSum - provision);
        totalProvision += provision;
        balanceFrom -= totalSum;
        if (indexFrom == indexTo) {
            balanceTo -= provision;
        }
        else {
            balanceTo += (totalSum - provision);
        }

        accounts[indexFrom].setBalance(balanceFrom+"$");
        accounts[indexTo].setBalance(balanceTo + "$");
        return true;
    }

    private int findID(long a){
        int index = -1;
        for (int i=0; i<accounts.length; ++i){
            if (a == accounts[i].getId()){
                index = i;
                break;
            }
        }
        return index;
    }

    public String totalProvision() {
        DecimalFormat pom = new DecimalFormat("#0.00");
        return pom.format(totalProvision) + "$";
    }

    public String totalTransfers() {
        DecimalFormat pom = new DecimalFormat("#0.00");
        return pom.format(totalTransfer) + "$";
    }

    public Account[] getAccounts() {
        return Arrays.copyOf(accounts, accounts.length);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: " + name + "\n\n");
        for (Account a: accounts)
            sb.append(a.toString());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(accounts);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        long temp;
        temp = Double.doubleToLongBits(totalProvision);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(totalTransfer);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Bank other = (Bank) obj;
        if (!Arrays.equals(accounts, other.accounts))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (Double.doubleToLongBits(totalProvision) != Double.doubleToLongBits(other.totalProvision))
            return false;
        if (Double.doubleToLongBits(totalTransfer) != Double.doubleToLongBits(other.totalTransfer))
            return false;
        return true;
    }

}