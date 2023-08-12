package org.hello.chapter01.item05;

import org.hello.chapter01.item05.dependencyinjection.Dictionary;

import java.util.List;

public class DefaultDictionary implements Dictionary {

    @Override
    public boolean contains(String word) {
        return false;
    }

    @Override
    public List<String> closeWordsTo(String typo) {
        return null;
    }
}
