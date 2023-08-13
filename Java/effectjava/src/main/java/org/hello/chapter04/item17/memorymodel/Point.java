package org.hello.chapter04.item17.memorymodel;

public class Point {
    private final int x;
    private final int y;

    public Point() {
        this.x  = 1;
        this.y  = 2;
    }

    public static void main(String[] args) {
        // 실행 순서
        // 1. Object p = new Point();
        // 2. point = (Point) p;
        // 3. point.x = 1;
        // 4. point.y = 2;
        Point point = new Point();
    }
}
