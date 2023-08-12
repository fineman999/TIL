package org.hello.chapter01.item05.dependencyinjection;

import java.util.List;
import java.util.function.Supplier;

public class SpellChecker {
    private final Dictionary dictionary;

    public SpellChecker(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public SpellChecker(DictionaryFactory dictionaryFactory) {
        this.dictionary = DictionaryFactory.get();
    }

    public SpellChecker(Supplier<? extends Dictionary> directorySupplier) {
        this.dictionary = directorySupplier.get();
    }
    public boolean isValid(String word) {
        // TODO 여기 SpellChecker 코드
        return dictionary.contains(word);
    }

    public List<String> suggestions(String typo) {
        // TODO 여기 SpellChecker 코드
        return dictionary.closeWordsTo(typo);
    }
}
