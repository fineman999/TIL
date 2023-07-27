package org.hello.item05.dependencyinjection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class SpellCheckerTest {

    @Test
    @DisplayName("Directory 인터페이스를 통해 쉽게 테스트 할 수 있다.")
    void create() {
        SpellChecker spellChecker = new SpellChecker(new Directory() {
            @Override
            public boolean contains(String word) {
                return false;
            }

            @Override
            public List<String> closeWordsTo(String typo) {
                return null;
            }
        });
    }
}