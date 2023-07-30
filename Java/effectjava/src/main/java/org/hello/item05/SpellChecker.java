package org.hello.item05;

import java.util.List;

public class SpellChecker {
    private static final DefaultDictionary dictionary = new DefaultDictionary();

    private SpellChecker() {
    }

    public static boolean isValid(String word) {
        return dictionary.contains(word);
    }

    public static List<String> suggestions(String typo) {
        return dictionary.closeWordsTo(typo);
    }

}
