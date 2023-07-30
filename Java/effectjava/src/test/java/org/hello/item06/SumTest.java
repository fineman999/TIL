package org.hello.item06;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SumTest {

    @Test
    @DisplayName("sum() 메서드 성능 테스트")
    void sum() {
        long start = System.nanoTime();
        long answer = Sum.sumWithAutoBoxing();
        long end = System.nanoTime();

        System.out.println("sumWithAutoBoxing() 메서드 실행 시간: " + (end - start)/1_000_000. + "ms");
        System.out.println("sumWithAutoBoxing() 메서드 실행 결과: " + answer);

        start = System.nanoTime();
        answer = Sum.sum();
        end = System.nanoTime();

        System.out.println("sum() 메서드 실행 시간: " + (end - start)/1_000_000. + "ms");
        System.out.println("sum() 메서드 실행 결과: " + answer);
    }
}