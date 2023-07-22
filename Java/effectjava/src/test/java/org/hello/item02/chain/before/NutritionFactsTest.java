package org.hello.item02.chain.before;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NutritionFactsTest {

    @Test
    @DisplayName("점층적 생성자 패턴 테스트")
    void name() {
        new NutritionFacts(10, 10);
    }
}