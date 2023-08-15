package org.hello.chapter04.item20.multipleinheritance;

public abstract class AbstractFlyable implements Flyable {
    @Override
    public void fly() {
        System.out.println("너랑 딱 붙어 있을게!");
    }
}
