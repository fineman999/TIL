package org.hello.chapter04.item18;

import java.util.HashSet;

public class InstrumentedHashSet<E> extends HashSet<E> {
    // 추가된 원소의 수
    private int addCount = 0;

    public InstrumentedHashSet() {
    }

    public InstrumentedHashSet(int initCap, float loadFactor) {
        super(initCap, loadFactor);
    }

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    // 추가된 원소의 수를 반환한다.
    public int getAddCount() {
        return addCount;
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }
}
