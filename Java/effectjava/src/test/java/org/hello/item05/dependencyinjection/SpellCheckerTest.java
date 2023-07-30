package org.hello.item05.dependencyinjection;

import org.hello.item05.DefaultDictionary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

class SpellCheckerTest {

    @Test
    @DisplayName("Directory 인터페이스를 통해 쉽게 테스트 할 수 있다.")
    void create() {
        SpellChecker spellChecker = new SpellChecker(new Dictionary() {
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

    @Test
    @DisplayName("Supplier<Directory> 인터페이스를 통해 쉽게 테스트 할 수 있다.")
    void createSupplier() {
        SpellChecker spellChecker = new SpellChecker(DefaultDictionary::new);
        spellChecker.isValid("hello");
    }

    @Test
    @DisplayName("정적 팩토리 메소드를 통해 쉽게 테스트 할 수 있다.")
    void createStaticFactoryMethod() {
        SpellChecker spellChecker = new SpellChecker(DictionaryFactory::get);
        spellChecker.isValid("hello");
    }

    @Test
    @DisplayName("Supplier<T> 테스트")
    void supplier() {
        Supplier<Integer> randomNumber = () -> (int) (Math.random() * 100);
        System.out.println(randomNumber.get());
        System.out.println("randomNumber.get() = " + randomNumber.get());
    }
}