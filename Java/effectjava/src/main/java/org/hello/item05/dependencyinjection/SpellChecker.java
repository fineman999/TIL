package org.hello.item05.dependencyinjection;

import java.util.List;
import java.util.function.Supplier;

public class SpellChecker {
    private final Directory dictionary;

    public SpellChecker(Directory dictionary) {
        this.dictionary = dictionary;
    }

    public SpellChecker(Supplier<Directory> directorySupplier) {
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
