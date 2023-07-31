package org.hello.item08;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;

class FinalizerIsBadTest {

    @Test
    @DisplayName("FinalizerIsBadTest")
    void test() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        for (int i = 0; i < 1_000_000; i++) {
            new FinalizerIsBad();

            if (i % 100_000 == 0) {
                Class<?> finalizerClass = Class.forName("java.lang.ref.Finalizer");

                Field queueStaticField = finalizerClass.getDeclaredField("queue");
                queueStaticField.setAccessible(true);
                ReferenceQueue<Object> referenceQueue = (ReferenceQueue) queueStaticField.get(null);
                Field queueLengthField = ReferenceQueue.class.getDeclaredField("queueLength");
                queueLengthField.setAccessible(true);
                int queueLength = (int) queueLengthField.get(referenceQueue);
                System.out.println("There are " + queueLength + " objects in the queue.");
            }

        }
    }
}