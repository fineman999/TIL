package org.hello.item05;

import org.hello.item05.dependencyinjection.Directory;

import java.util.List;

public class DefaultDirectory implements Directory {

    @Override
    public boolean contains(String word) {
        return false;
    }

    @Override
    public List<String> closeWordsTo(String typo) {
        return null;
    }
}
