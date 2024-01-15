package org.example.exam05.critical_section;

public class SharedResource {
    private int counter = 0;

    public void increment() {

        for (int i = 0; i < 10000; i++) {
             synchronized (this) {  // entry section

                    // critical section
                 counter++;
                 System.out.println(Thread.currentThread().getName() + ": " + counter);
             } // exit section
        }

        // remainder section
        doOtherWork();
    }

    private void doOtherWork() {
        System.out.println(Thread.currentThread().getName() + ": doOtherWork");
    }
}
