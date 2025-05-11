package Tutorial_6;


class MultiplicationTask implements Runnable {
    private final int number;
    private final int thread;

    public MultiplicationTask(int number, int thread) {
        this.number = number;
        this.thread = thread;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println("Thread-" + thread + ": " + number + " * " + i + " = " + (number * i));
        }
    }
}

public class MultiplicationTable {
    public static void main(String[] args) {
        for (int i = 1; i <= 3; i++) {
            Thread thread = new Thread(new MultiplicationTask(i, i - 1));
            thread.start();
        }
    }
}