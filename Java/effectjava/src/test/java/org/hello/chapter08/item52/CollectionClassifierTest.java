package org.hello.chapter08.item52;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;

class CollectionClassifierTest {

    @Test
    @DisplayName("컬렉션 분류기 - 오류! - 이 프로그램은 무엇이 출력될까?.")
    void test() {
        Collection<?>[] collections = {
                new java.util.HashSet<String>(),
                new java.util.ArrayList<String>(),
                new java.util.HashMap<String, String>().values()
        };

        for (Collection<?> c : collections) {
            System.out.println(CollectionClassifier.classify(c));
        }
    }

    @Test
    @DisplayName("컬렉션 분류기 - instanceof로 명시적으로 검사하기")
    void test2() {
        Collection<?>[] collections = {
                new java.util.HashSet<String>(),
                new java.util.ArrayList<String>(),
                new java.util.HashMap<String, String>().values()
        };

        for (Collection<?> c : collections) {
            System.out.println(NewCollectionClassifier.classify(c));
        }
    }
}