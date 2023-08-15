package org.hello.chapter04.item20.multipleinheritance;

public class MyCat extends AbstractCat implements Flyable {
    private final MyFlyable flyable = new MyFlyable();
    @Override
    protected String sound() {
        return "meow";
    }

    @Override
    protected String name() {
        return "유미";
    }


    @Override
    public void fly() {
        flyable.fly();
    }

    private class MyFlyable extends AbstractFlyable {
    }
}
