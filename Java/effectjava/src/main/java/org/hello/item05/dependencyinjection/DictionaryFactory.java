package org.hello.item05.dependencyinjection;

import org.hello.item05.DefaultDictionary;

public class DictionaryFactory {
    public static Dictionary get() {
        return new DefaultDictionary();
    }
}
