package org.hello.item02.builderpattern;

import org.hello.chapter01.item02.builderpattern.DefaultTourBuilder;
import org.hello.chapter01.item02.builderpattern.TourDirector;
import org.hello.chapter01.item02.builderpattern.TourPlan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class BuilderPatternTest {

    @Test
    @DisplayName("Director를 이용해 빌더 패턴을 테스트한다.")
    void test() {
        TourDirector director = new TourDirector(new DefaultTourBuilder());
        TourPlan cancunTrip = director.cancunTrip();
        TourPlan jejuTrip = director.jejuTrip();

        assertAll(
                () -> assertThat(cancunTrip.toString())
                        .isEqualTo("TourPlan{title='다낭 여행', startDate=2020-12-09, nights=2, days=3," +
                                " whereToStay='호텔', plans=[DetailPlan{day=1, plan='다낭공항 출발'}, " +
                                "DetailPlan{day=2, plan='호텔 체크인'}, DetailPlan{day=1, plan='제주공항 출발'}, " +
                                "DetailPlan{day=2, plan='호텔 체크인'}]}"),
                () -> assertThat(jejuTrip.toString())
                        .isEqualTo("TourPlan{title='제주도 여행', startDate=2020-12-09, nights=2, days=3," +
                                " whereToStay='호텔', plans=[DetailPlan{day=1, plan='다낭공항 출발'}, " +
                                "DetailPlan{day=2, plan='호텔 체크인'}, DetailPlan{day=1, plan='제주공항 출발'}," +
                                " DetailPlan{day=2, plan='호텔 체크인'}]}")
        );
    }
}