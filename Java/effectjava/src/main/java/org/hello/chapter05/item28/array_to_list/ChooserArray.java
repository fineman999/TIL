package org.hello.chapter05.item28.array_to_list;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

// 배열 기반 Chooser
public class ChooserArray {
    private final Object[] choiceList;

    public ChooserArray(Collection choiceList) {
        this.choiceList = choiceList.toArray();
    }

    public Object choose() {
        Random rmd = ThreadLocalRandom.current();
        return choiceList[rmd.nextInt(choiceList.length)];
    }

    public static void main(String[] args) {
        List<Integer> integerList = List.of(1, 2, 3, 4, 5, 6);
        ChooserArray chooserArray = new ChooserArray(integerList);

        for (int i = 0; i < 10; i++) {
            Number choose = (Number) chooserArray.choose(); // ClassCastException
            System.out.println(choose);
        }
    }
}
