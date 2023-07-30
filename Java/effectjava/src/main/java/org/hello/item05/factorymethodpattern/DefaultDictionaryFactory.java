package org.hello.item05.factorymethodpattern;

import org.hello.item05.DefaultDictionary;
import org.hello.item05.dependencyinjection.Dictionary;

public class DefaultDictionaryFactory implements DictionaryFactory {
    @Override
    public Dictionary getDictionary() {
        return new DefaultDictionary();
    }
}
