package org.hello.chapter04.item18.callback;

public class Service {
    public void run(BobFunction bobFunction) {
        System.out.println("서비스를 실행합니다.");
        bobFunction.call();
    }

    public static void main(String[] args) {
        Service service = new Service();
        BobFunction bobFunction = new BobFunction(service);
        BobFunctionWrapper bobFunctionWrapper = new BobFunctionWrapper(bobFunction);
        bobFunctionWrapper.run();
    }
}
