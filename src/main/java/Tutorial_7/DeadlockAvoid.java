package Tutorial_7;


import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

public class DeadlockAvoid implements Runnable {
    private static final ReentrantLock lock1 = new ReentrantLock();
    private static final ReentrantLock lock2 = new ReentrantLock();
    private final Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) {
        Thread myThread1 = new Thread(new DeadlockAvoid(), "thread-1");
        Thread myThread2 = new Thread(new DeadlockAvoid(), "thread-2");
        myThread1.start();
        myThread2.start();
    }

    public void run() {
        for (int i = 0; i < 10000; i++) {
            boolean b = random.nextBoolean();
            if (b) {
                attemptLocks(lock1, lock2);
            } else {
                attemptLocks(lock2, lock1);
            }
        }
    }

    private void attemptLocks(ReentrantLock firstLock, ReentrantLock secondLock) {
        boolean gotFirstLock = false;
        boolean gotSecondLock = false;
        try {
            gotFirstLock = firstLock.tryLock(50, TimeUnit.MILLISECONDS);
            if (gotFirstLock) {
                System.out.println("[" + Thread.currentThread().getName() + "] locked first resource");
                Thread.sleep(1); // short sleep
                gotSecondLock = secondLock.tryLock(50, TimeUnit.MILLISECONDS);
                if (gotSecondLock) {
                    System.out.println("[" + Thread.currentThread().getName() + "] locked second resource");
                    // Critical section
                } else {
                    System.out.println("[" + Thread.currentThread().getName() + "] Could not lock second resource, releasing first lock");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (gotSecondLock) {
                secondLock.unlock();
            }
            if (gotFirstLock) {
                firstLock.unlock();
            }
        }
    }
}