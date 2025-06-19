package caseStudy2;


import java.util.concurrent.ConcurrentHashMap;

public class modifiedStudentLoginTracker {
    public static void main(String[] args) {
        ConcurrentHashMap<String, Integer> loginCounts = new ConcurrentHashMap<>();
        loginCounts.put("student123", 0);
        loginCounts.put("student456", 0);

        Runnable incrementStudent123 = () -> {
            for (int i = 0; i < 5000; i++) {
                loginCounts.compute("student123", (k, v) -> v + 1);
            }
        };

        Runnable incrementStudent456 = () -> {
            for (int i = 0; i < 5000; i++) {
                loginCounts.compute("student456", (k, v) -> v + 1);
            }
        };

        Thread t1 = new Thread(incrementStudent123);
        Thread t2 = new Thread(incrementStudent123);
        Thread t3 = new Thread(incrementStudent456);
        Thread t4 = new Thread(incrementStudent456);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final login count for student123: " + loginCounts.get("student123"));
        System.out.println("Final login count for student456: " + loginCounts.get("student456"));
    }
}
