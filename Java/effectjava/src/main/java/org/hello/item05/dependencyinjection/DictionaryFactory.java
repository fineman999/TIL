package org.hello.item05.dependencyinjection;

import org.hello.item05.DefaultDirectory;

public class DictionaryFactory {
    public static Directory get() {
        return new DefaultDirectory();
    }
}
