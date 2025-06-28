package caseStudy3;

import java.util.concurrent.*;

public class FlightBookingSystem {
    private static final int AGENCY_COUNT = 3;

    private static final CyclicBarrier barrier = new CyclicBarrier(AGENCY_COUNT,()->{
        System.out.println("All agencies have selected seats. Confirming bokings...");

        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            System.out.println("Bookings confirmed!\n");
        }
    });

    public static void main(String[] args) {
        System.out.println("Starting flight booking system...\n");
        for(int i=1; i<= AGENCY_COUNT; i++){
            new Thread(new BookingAgency("Agency-" +i)).start();
        }
    }

    static class BookingAgency implements Runnable{
        private final String agencyName;

        public BookingAgency(String agencyName){
            this.agencyName = agencyName;
        }

        @Override
        public void run() {
            try{
                System.out.println(agencyName + "is selecting seats...");
                Thread.sleep((int) (Math.random() * 3000));
                System.out.println(agencyName + "finished selecting seats. Waiting for others...");
                barrier.await();
                System.out.println(agencyName +" proceeds after confirmation.");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
