package org.hello.chapter05.item27.suppress;

public class ListExample {
    private int size;
    private Object[] elements;

    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            // This cast is correct because the array we're creating
            // is of the same type as the one passed in, which is T[].
            @SuppressWarnings("unchecked")
            T[] result = (T[]) java.util.Arrays.copyOf(elements, size, a.getClass());
            return result;
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}
