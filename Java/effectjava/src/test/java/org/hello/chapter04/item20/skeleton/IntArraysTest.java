package org.hello.chapter04.item20.skeleton;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.hello.chapter04.item20.skeleton.IntArrays.intArrayAsList;
import static org.junit.jupiter.api.Assertions.*;

class IntArraysTest {

    @Test
    void test() {
        int[] a = new int[10];
        for (int i = 0; i < 10; i++) {
            a[i] = i;
        }

        List<Integer> list = intArrayAsList(a);
        Collections.shuffle(list);

        System.out.println(list);


    }
}