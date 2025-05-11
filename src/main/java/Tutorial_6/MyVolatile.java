package Tutorial_6;

import java.util.Scanner;

class MyThread extends Thread {
    private volatile boolean running = true;

    public void run() {
        while (running) {
            System.out.println("Thread is running...");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted.");
            }
        }
        System.out.println("Thread is shutting down.");
    }

    public void shutdown() {
        running = false;
    }
}

public class MyVolatile {
    public static void main(String[] args) {
        MyThread thread = new MyThread();
        thread.start();

        System.out.println("Press ENTER to stop the thread...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        thread.shutdown();
    }
}