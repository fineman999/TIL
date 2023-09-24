package org.example.create_object.builder;

import org.example.create_object.builder._01_before.TourPlan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BuilderTest {

    @Test
    @DisplayName("일반적인 방식으로 객체를 생성 - 객체가 불안정한 상태로 생성될 수 있다.")
    void test() {

        TourPlan tourPlan = new TourPlan();
        tourPlan.setTitle("제주도 여행");
        tourPlan.setDays(3);
        tourPlan.setNights(2);
        tourPlan.setStartDate(LocalDate.of(2021, 8, 1));
        tourPlan.setWhereToStay("호텔");
        tourPlan.addPlan(0, "제주공항에서 렌트카를 빌린다.");
        tourPlan.addPlan(0, "저녁 6시에 호텔에 체크인한다.");
        tourPlan.addPlan(1, "제주시에 있는 미술관을 방문한다.");
        tourPlan.addPlan(1, "점심은 제주시에서 먹는다.");
        tourPlan.addPlan(2, "제주공항으로 간다.");

    }
}