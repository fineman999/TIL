package org.hello.chapter05.item31;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Chooser<T> {
    private final List<T> choiceList;
    private final Random rnd;

    // 생성자 매개변수에 한정적 와일드카드 타입 적용
    public Chooser(Collection<? extends T> choiceList) {
        this.choiceList = new ArrayList<>(choiceList);
        this.rnd = new Random();
    }

    public T choose() {
        return choiceList.get(rnd.nextInt(choiceList.size()));
    }

    public static void main(String[] args) {
        List<Integer> intList = List.of(1, 2, 3, 4, 5);
        Chooser<Number> chooser = new Chooser<>(intList);
        for (int i = 0; i < 10; i++) {
            System.out.println(chooser.choose());
        }
    }
}
