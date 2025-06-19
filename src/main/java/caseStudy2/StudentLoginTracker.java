package caseStudy2;


import java.util.concurrent.ConcurrentHashMap;

public class StudentLoginTracker {
    public static void main(String[] args) {
        ConcurrentHashMap<String, Integer> loginCounts = new ConcurrentHashMap<>();
        loginCounts.put("student123", 0);

        Runnable incrementLogin = () -> {
            for (int i = 0; i < 5000; i++) {
                loginCounts.compute("student123", (k, v) -> v + 1);
            }
        };

        Thread t1 = new Thread(incrementLogin);
        Thread t2 = new Thread(incrementLogin);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final login count for student123: " + loginCounts.get("student123"));
    }
}
