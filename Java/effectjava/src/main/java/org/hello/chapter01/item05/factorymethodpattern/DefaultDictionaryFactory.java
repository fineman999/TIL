package org.hello.chapter01.item05.factorymethodpattern;

import org.hello.chapter01.item05.DefaultDictionary;
import org.hello.chapter01.item05.dependencyinjection.Dictionary;

public class DefaultDictionaryFactory implements DictionaryFactory {
    @Override
    public Dictionary getDictionary() {
        return new DefaultDictionary();
    }
}
