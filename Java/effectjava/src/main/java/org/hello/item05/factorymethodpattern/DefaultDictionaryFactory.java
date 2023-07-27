package org.hello.item05.factorymethodpattern;

import org.hello.item05.DefaultDirectory;
import org.hello.item05.dependencyinjection.Directory;

public class DefaultDictionaryFactory implements DictionaryFactory {
    @Override
    public Directory getDictionary() {
        return new DefaultDirectory();
    }
}
