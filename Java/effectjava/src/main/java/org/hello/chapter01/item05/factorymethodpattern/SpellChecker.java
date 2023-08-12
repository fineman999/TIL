package org.hello.chapter01.item05.factorymethodpattern;

import org.hello.chapter01.item05.dependencyinjection.Dictionary;


public class SpellChecker {

    private final Dictionary dictionary;

    public SpellChecker(DictionaryFactory dictionaryFactory) {
        this.dictionary = dictionaryFactory.getDictionary();
    }
}
