package org.example.exam05.thread_safe;

public class ImmutableExample implements Runnable{

    private ImmutablePerson person;

    public ImmutableExample(ImmutablePerson person) {
        this.person = person;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": " + person.getName() + ", " + person.getAge());
    }

    public static void main(String[] args) {
        ImmutablePerson person = new ImmutablePerson("John", 20);

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new ImmutableExample(person.incrementAge()));
            thread.start();
        }

    }

}

final class ImmutablePerson {
    private final String name;
    private final int age;

    public ImmutablePerson(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public ImmutablePerson incrementAge() {
        return new ImmutablePerson(this.name, this.age + 1);
    }
}