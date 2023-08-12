package org.hello.chapter01.item02.builderpattern;

import java.time.LocalDate;

public class TourDirector {

    private final TourPlanBuilder tourPlanBuilder;

    public TourDirector(TourPlanBuilder tourPlanBuilder) {
        this.tourPlanBuilder = tourPlanBuilder;
    }

    public TourPlan cancunTrip() {
        return tourPlanBuilder.title("다낭 여행")
                .nightsAndDays(2, 3)
                .startDate(LocalDate.of(2020, 12, 9))
                .whereToStay("호텔")
                .addPlan(1, "다낭공항 출발")
                .addPlan(2, "호텔 체크인")
                .getPlan();
    }

    public TourPlan jejuTrip() {
        return tourPlanBuilder.title("제주도 여행")
                .nightsAndDays(2, 3)
                .startDate(LocalDate.of(2020, 12, 9))
                .whereToStay("호텔")
                .addPlan(1, "제주공항 출발")
                .addPlan(2, "호텔 체크인")
                .getPlan();
    }
}
