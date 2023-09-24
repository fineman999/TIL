package org.example.create_object.builder._02_after;

import org.example.create_object.builder._01_before.TourPlan;

public interface TourPlanBuilder {
    TourPlanBuilder title(String title);

    TourPlanBuilder nightsAndDays(int nights, int days);

    TourPlanBuilder startDate(String startDate);

    TourPlanBuilder whereToStay(String whereToStay);

    TourPlanBuilder addPlan(int day, String plan);

    TourPlan getPlan();
}
