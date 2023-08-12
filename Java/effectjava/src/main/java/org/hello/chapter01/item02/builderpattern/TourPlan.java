package org.hello.chapter01.item02.builderpattern;

import java.time.LocalDate;
import java.util.List;

public class TourPlan {
    private final String title;
    private final LocalDate startDate;
    private final int nights;
    private final int days;
    private final String whereToStay;
    private final List<DetailPlan> plans;


    public TourPlan(String title, LocalDate startDate, int nights, int days, String whereToStay, List<DetailPlan> plans) {
        this.title = title;
        this.startDate = startDate;
        this.nights = nights;
        this.days = days;
        this.whereToStay = whereToStay;
        this.plans = plans;
    }

    @Override
    public String toString() {
        return "TourPlan{" +
                "title='" + title + '\'' +
                ", startDate=" + startDate +
                ", nights=" + nights +
                ", days=" + days +
                ", whereToStay='" + whereToStay + '\'' +
                ", plans=" + plans +
                '}';
    }
}
