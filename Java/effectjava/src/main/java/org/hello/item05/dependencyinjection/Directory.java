package org.hello.item05.dependencyinjection;

import java.util.List;

public interface Directory {

    boolean contains(String word);

    List<String> closeWordsTo(String typo);
}
