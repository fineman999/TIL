package org.hello.item05;

import org.hello.item05.dependencyinjection.Dictionary;

import java.util.List;

public class MockDictionary implements Dictionary {
    @Override
    public boolean contains(String word) {
        return false;
    }

    @Override
    public List<String> closeWordsTo(String typo) {
        return null;
    }
}
