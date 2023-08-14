package org.hello.chapter04.item18.callback;

public class BobFunction implements FunctionCall {
    private final Service service;


    public BobFunction(Service service) {
        this.service = service;
    }

    @Override
    public void call() {
        System.out.println("밥을 먹을까...");
    }

    @Override
    public void run() {
        this.service.run(this);
    }
}
