package org.hello.chapter02.item10;

public class Point {
    private final int x;
    private final int y;


    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        // 1. 반사성
        if (this == o) {
            return true;
        }
        // 2. 올바른 타입 확인
        if (!(o instanceof Point)) {
            return false;
        }
        // 3. 형 변환
        Point p = (Point)o;
        // 4. 핵심적인 필드들이 모두 일치하는지 검사
        return p.x == x && p.y == y;
    }
}

