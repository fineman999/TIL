package org.hello.chapter04.item18.callback;

public class BobFunctionWrapper implements FunctionCall {
    private final BobFunction bobFunction;

    public BobFunctionWrapper(BobFunction bobFunction) {
        this.bobFunction = bobFunction;
    }

    @Override
    public void call() {
        this.bobFunction.call();
        System.out.println("커피도 마셔야지...");
    }

    @Override
    public void run() {
        this.bobFunction.run();
    }
}
