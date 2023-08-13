package org.hello.chapter02.item14.inheritance;

public class NamePoint extends Point{
    private final String name;

    public NamePoint(int x, int y, String name) {
        super(x, y);
        this.name = name;
    }

    @Override
    public int compareTo(Point point) {
        if (!(point instanceof NamePoint)) {
            return super.compareTo(point);
        }
        NamePoint namePoint = (NamePoint) point;
        int result = name.compareTo(namePoint.name);
        if (result == 0) {
            result = super.compareTo(namePoint);
        }
        return result;
    }
}
