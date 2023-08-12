package org.hello.chapter01.item05.springioc;

import org.hello.chapter01.item05.dependencyinjection.Dictionary;

import java.util.List;

public class SpringDictionary implements Dictionary {
    @Override
    public boolean contains(String word) {
        System.out.println("SpringDictionary.contains" + word);
        return true;
    }

    @Override
    public List<String> closeWordsTo(String typo) {
        return null;
    }
}
