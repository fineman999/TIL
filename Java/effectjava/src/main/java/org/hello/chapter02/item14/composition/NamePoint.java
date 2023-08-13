package org.hello.chapter02.item14.composition;

import org.hello.chapter02.item14.inheritance.Point;

public class NamePoint implements Comparable<NamePoint> {
    private final Point point;
    private final String name;

    public NamePoint(int x, int y, String name) {
        this.point = new Point(x, y);
        this.name = name;
    }

    public Point asPoint() {
        return point;
    }

    @Override
    public int compareTo(NamePoint o) {
        int result = point.compareTo(o.point);
        if (result == 0) {
            result = name.compareTo(o.name);
        }
        return result;
    }
}
