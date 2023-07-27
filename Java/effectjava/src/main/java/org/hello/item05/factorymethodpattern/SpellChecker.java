package org.hello.item05.factorymethodpattern;

import org.hello.item05.dependencyinjection.Directory;


public class SpellChecker {

    private final Directory dictionary;

    public SpellChecker(DictionaryFactory dictionaryFactory) {
        this.dictionary = dictionaryFactory.getDictionary();
    }
}
