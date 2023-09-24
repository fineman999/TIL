package org.example.create_object.builder;

import org.example.create_object.builder._01_before.TourPlan;
import org.example.create_object.builder._02_after.DefaultTourBuilder;
import org.example.create_object.builder._02_after.TourDirector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(tourPlan.getTitle()).isEqualTo("제주도 여행");
    }

    @Test
    @DisplayName("TourPlanBuilder를 사용하여 객체를 생성 - 빌더 패턴")
    void test1() {
        TourPlan tourPlan = new DefaultTourBuilder()
                .title("제주도 여행")
                .nightsAndDays(2, 3)
                .startDate("2021-08-01")
                .whereToStay("호텔")
                .addPlan(0, "제주공항에서 렌트카를 빌린다.")
                .addPlan(0, "저녁 6시에 호텔에 체크인한다.")
                .addPlan(1, "제주시에 있는 미술관을 방문한다.")
                .addPlan(1, "점심은 제주시에서 먹는다.")
                .addPlan(2, "제주공항으로 간다.")
                .getPlan();

        assertThat(tourPlan.getTitle()).isEqualTo("제주도 여행");
    }

    @Test
    @DisplayName("TourDirector을 사용한 객체 생성 - 빌더 패턴")
    void test2() {
        TourDirector tourDirector = new TourDirector(new DefaultTourBuilder());

        TourPlan tourPlan = tourDirector.createJejuTourPlan();

        assertThat(tourPlan.getTitle()).isEqualTo("제주도 여행");
    }
}