package org.hello.chapter05.item27.unchecked;

import java.util.HashSet;
import java.util.Set;

public class SetExample {
    public static void main(String[] args) {
        Set<String> stringSet = new HashSet<>();
        Set rawSet = stringSet;
    }
}
