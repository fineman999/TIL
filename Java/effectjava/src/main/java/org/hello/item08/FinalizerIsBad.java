package org.hello.item08;

public class FinalizerIsBad {
    @Override
    protected void finalize() throws Throwable {
        System.out.println("Finalizer is bad");
    }
}
