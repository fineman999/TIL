package org.hello.item05;

import java.util.List;

public class SpellChecker {
    private static final DefaultDirectory dictionary = new DefaultDirectory();

    private SpellChecker() {
    }

    public static boolean isValid(String word) {
        return dictionary.contains(word);
    }

    public static List<String> suggestions(String typo) {
        return dictionary.closeWordsTo(typo);
    }

}
