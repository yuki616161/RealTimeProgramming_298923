package Tutorial_11;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

public class BankAccountWithLock {
    private double balance;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public BankAccountWithLock(double initialBalance){
        this.balance = initialBalance;
    }

    // Read balance (shared lock)
    public double getBalance(){
        readLock.lock();
        try{
            System.out.println(Thread.currentThread().getName() + " reads balance: " + balance);
            return balance;
        }finally{
            readLock.unlock();
        }
    }

    // Deposit money (exclusive lock)
    public void deposit(double amount){
        writeLock.lock();
        try{
            System.out.println(Thread.currentThread().getName() + " deposits amount: " + amount);
            balance += amount;
        }finally{
            writeLock.unlock();
        }
    }

    // Withdraw money (exclusive lock)
    public void withdraw(double amount){
        writeLock.lock();
        try{
            if(balance >= amount){
                System.out.println(Thread.currentThread().getName() + " withdraws amount: " + amount);
                balance -= amount;
            }else{
                System.out.println(Thread.currentThread().getName() + " insufficient funds for: " + amount);
            }
        }finally{
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        BankAccountWithLock account = new BankAccountWithLock(1000.0);

        // Repeat the transaction logic for 5 rounds
        for (int i = 1; i <= 5; i++) {
            int round = i; // effectively final copy for lambda usage

            System.out.println("\n--- Transaction Round " + round + " ---");

            Thread reader1   = new Thread(() -> account.getBalance(), "Reader-1");
            Thread depositor = new Thread(() -> account.deposit(100 * round), "Depositor");
            Thread withdrawer= new Thread(() -> account.withdraw(50 * round), "Withdrawer");
            Thread reader2   = new Thread(() -> account.getBalance(), "Reader-2");

            // Start all threads
            reader1.start();
            depositor.start();
            withdrawer.start();
            reader2.start();

            // Wait for all threads to finish
            try {
                reader1.join();
                depositor.join();
                withdrawer.join();
                reader2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Optional delay for readability
            try {
                Thread.sleep(500); // 500ms pause between rounds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\nFinal Balance: " + account.getBalance());
    }
}
