package org.example.create_object.builder._02_after;

import org.example.create_object.builder._01_before.TourPlan;

public class TourDirector {
    private TourPlanBuilder tourPlanBuilder;

    public TourDirector(TourPlanBuilder tourPlanBuilder) {
        this.tourPlanBuilder = tourPlanBuilder;
    }

    public TourPlan createJejuTourPlan() {
        return tourPlanBuilder
                .title("제주도 여행")
                .nightsAndDays(3, 4)
                .startDate("2021-07-01")
                .whereToStay("호텔")
                .addPlan(1, "제주공항 -> 호텔")
                .addPlan(2, "관광지1 -> 관광지2")
                .addPlan(3, "관광지3 -> 관광지4")
                .addPlan(4, "호텔 -> 제주공항")
                .getPlan();
    }

}
