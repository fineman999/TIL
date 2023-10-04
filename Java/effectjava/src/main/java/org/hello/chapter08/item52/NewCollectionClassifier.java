package org.hello.chapter08.item52;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class NewCollectionClassifier {
    public static String classify(Collection<?> c) {
        return c instanceof Set ? "집합" :
                c instanceof List ? "리스트" : "그 외";
    }
}
