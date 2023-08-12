package org.hello.item02.chain.before;

import org.hello.chapter01.item02.chain.before.NutritionFacts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NutritionFactsTest {

    @Test
    @DisplayName("점층적 생성자 패턴 테스트")
    void name() {
        new NutritionFacts(10, 10);
    }
}