package org.hello.chapter01.item05.dependencyinjection;

import org.hello.chapter01.item05.DefaultDictionary;

public class DictionaryFactory {
    public static Dictionary get() {
        return new DefaultDictionary();
    }
}
