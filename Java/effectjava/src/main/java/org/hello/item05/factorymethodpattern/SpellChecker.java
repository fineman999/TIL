package org.hello.item05.factorymethodpattern;

import org.hello.item05.dependencyinjection.Dictionary;


public class SpellChecker {

    private final Dictionary dictionary;

    public SpellChecker(DictionaryFactory dictionaryFactory) {
        this.dictionary = dictionaryFactory.getDictionary();
    }
}
