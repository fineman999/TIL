package org.example.exam09.atomic;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceExample {
    public static void main(String[] args) {

        User user1 = new User("홍길동", 20);
        User user2 = new User("본드", 30);

        AtomicReference<User> atomicReference = new AtomicReference<>(user1);

        Thread t1 = new Thread(() -> {
            User updateUser = new User("만달레이언", 40);
            boolean success = atomicReference.compareAndSet(user1, updateUser);
            if (success) {
                System.out.println("user1을 " + updateUser + "로 변경했습니다.");
            } else {
                System.out.println("user1을 " + updateUser + "로 변경하지 못했습니다.");
            }
        });

        Thread t2 = new Thread(() -> {
            User updateUser = new User("데이빗", 50);
            boolean success = atomicReference.compareAndSet(user2, updateUser);
            if (success) {
                System.out.println("user2을 " + updateUser + "로 변경했습니다.");
            } else {
                System.out.println("user2을 " + updateUser + "로 변경하지 못했습니다.");
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("최종 결과: " + atomicReference.get());



    }

    static class User {
        private final String name;
        private final int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
