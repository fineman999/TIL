package org.hello.chapter07.item42;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class MethodReferenceTest {

    @Test
    @DisplayName("메서드 참조를 함수 객체로 사용 - 람다보다 간결하다.(비교자 생성 메서드)")
    void test() {
        Map<Integer, Integer> diary = new HashMap<>();

        // diary.merge(1, 1, (count, incr) -> count + incr);
        diary.merge(1, 1, Integer::sum);
        assertThat(diary.get(1)).isEqualTo(1);

        diary.merge(1, 1, Integer::sum);
        assertThat(diary.get(1)).isEqualTo(2);
    }
}
