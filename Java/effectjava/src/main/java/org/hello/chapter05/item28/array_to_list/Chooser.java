package org.hello.chapter05.item28.array_to_list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Chooser<T> {
    private final List<T> choiceList;

    public Chooser(Collection<T> choices) {
        this.choiceList = new ArrayList<>(choices);
    }

    public T choose() {
        Random rmd = ThreadLocalRandom.current();
        return choiceList.get(rmd.nextInt(choiceList.size()));
    }

    public static void main(String[] args) {
        List<Integer> integerList = List.of(1, 2, 3, 4, 5, 6);
        Chooser<Integer> chooser = new Chooser<>(integerList);

        for (int i = 0; i < 10; i++) {
            Number choose = chooser.choose();
            System.out.println(choose);
        }
    }
}
