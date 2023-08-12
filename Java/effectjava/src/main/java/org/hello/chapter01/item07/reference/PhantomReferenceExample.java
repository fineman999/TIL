package org.hello.chapter01.item07.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

public class PhantomReferenceExample {
    public static void main(String[] args) throws InterruptedException {
        BigObject strong = new BigObject();
        ReferenceQueue<BigObject> rq = new ReferenceQueue<>();

        PhantomReference<BigObject> phantom = new PhantomReference<>(strong, rq);
        strong = null;

        System.gc();
        Thread.sleep(3000L);

        System.out.println("phantom" + phantom.isEnqueued());

        Reference<? extends BigObject> reference = rq.poll();
        reference.clear();
    }
}
