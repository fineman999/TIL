package org.hello.chapter05.item31.example;

public class Box<T extends Comparable<T>> implements Comparable<Box<T>> {
    protected final T value;

    public Box(T value) {
        this.value = value;
    }

    // Box<T>가 Box<T>와 비교할 수 있음을 보장한다.
    @SuppressWarnings("unchecked")
    @Override
    public int compareTo(Box o) {
        return value.compareTo((T)o.value);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Box{");
        sb.append("value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
