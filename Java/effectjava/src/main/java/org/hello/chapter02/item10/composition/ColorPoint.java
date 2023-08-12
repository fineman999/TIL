package org.hello.chapter02.item10.composition;

import org.hello.chapter02.item10.Color;
import org.hello.chapter02.item10.Point;

public class ColorPoint {
    private final Point point;
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        this.point = new Point(x, y);
        this.color = color;
    }

    /**
     * Point 인스턴스로 위임
     * @return
     */
    public Point asPoint() {
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColorPoint)) {
            return false;
        }
        ColorPoint cp = (ColorPoint)o;
        return cp.point.equals(point) && cp.color == color;
    }

    @Override
    public int hashCode() {
        return point.hashCode() * 33 + color.hashCode();
    }
}
