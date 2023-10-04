package org.hello.chapter08.item50;

import java.util.Date;

public class DefendPeriod {
    private final Date start;
    private final Date end;

    public DefendPeriod(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());

        if (this.start.compareTo(this.end) > 0) {
            throw new IllegalArgumentException(
                    this.start + "가 " + this.end + "보다 늦다.");
        }
    }

    public Date start() {
        return new Date(start.getTime());
    }

    public Date end() {
        return new Date(end.getTime());
    }
}
