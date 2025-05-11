package Tutorial_8;

public class ThreadComparison {

    private static final int NUM_THREADS = 10;
    private static int counter = 0;

    static class NormalThread extends Thread {
        public void run() {
            for (int i = 0; i < 1000; i++) {
                counter++;
            }
        }
    }

    static class SynchronizedThread extends Thread {
        public void run() {
            synchronized (SynchronizedThread.class) {
                for (int i = 0; i < 1000; i++) {
                    counter++;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];

        // Measure normal thread time
        counter = 0;
        long startTime = System.nanoTime();
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new NormalThread();
            threads[i].start();
        }
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i].join();
        }
        long endTime = System.nanoTime();
        double normalTime = (endTime - startTime) / 1_000_000_000.0;

        counter = 0;
        startTime = System.nanoTime();
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new SynchronizedThread();
            threads[i].start();
        }
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i].join();
        }
        endTime = System.nanoTime();
        double syncTime = (endTime - startTime) / 1_000_000_000.0;

        System.out.printf("Normal thread = %.9f seconds\n", normalTime);
        System.out.printf("Synchronized thread = %.9f seconds\n", syncTime);
    }
}