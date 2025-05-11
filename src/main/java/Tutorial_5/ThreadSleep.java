package Tutorial_5;

public class ThreadSleep {
    public static void main(String[] args) {
        for (int i = 1; i <= 20; i++) {
            Thread t = new Thread(new PrintThread(i));
            t.start();
        }
    }
}

class PrintThread implements Runnable {
    private int threadNumber;

    public PrintThread(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    @Override
    public void run() {
        printLine("One");
        printLine("Two");
        printLine("Three");
        printLine("XXXXXXXXXX");
    }

    public void printLine(String message) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Thread %d : %s%n", threadNumber, message);
    }
}