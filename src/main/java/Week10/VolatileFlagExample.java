package Week10;

public class VolatileFlagExample {
    private static volatile boolean running = true;

    public static void main(String[] args) {
        Thread worker = new Thread(() -> {
            System.out.println("Worker thread started...");
            while (running) {
                // Simulate work
            }
            System.out.println("Worker thread stopped.");
        });

        worker.start();

        try {
            Thread.sleep(2000); // Main thread waits for 2 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        running = false; // Stop the worker thread
    }
}
