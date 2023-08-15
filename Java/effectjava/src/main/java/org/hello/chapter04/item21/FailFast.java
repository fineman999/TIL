package org.hello.chapter04.item21;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FailFast {

    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);

//        // 이터레이터로 콜렉션을 순회하는 중에 Collection.remove() 메서드를 호출하면
//        for (Integer number : numbers) {
//            System.out.println(number);
//            if (number == 3) {
//                numbers.remove(number);
//            }
//        }
//        // 이터레이터의 remove() 메서드를 사용하면 예외가 발생하지 않는다.
//        for (Iterator<Integer> iterator = numbers.iterator(); iterator.hasNext();) {
//            Integer number = iterator.next();
//            System.out.println(number);
//            if (number == 3) {
//                iterator.remove();
//            }
//        }
//        // 인덱스를 사용하면 예외가 발생하지 않는다.
//         for (int i = 0; i < numbers.size(); i++) {
//             System.out.println(numbers.get(i));
//             if (numbers.get(i) == 3) {
//                 numbers.remove(numbers.get(i));
//             }
//         }

        // removeIf() 메서드를 사용하면 예외가 발생하지 않는다.
        numbers.removeIf(number -> number == 3);
    }
}
