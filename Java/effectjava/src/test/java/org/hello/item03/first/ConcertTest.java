package org.hello.item03.first;

import org.hello.chapter01.item03.first.Concert;
import org.hello.chapter01.item03.first.Elvis;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ConcertTest {

    @Test
    @DisplayName("Concert의 perform 메서드를 테스트한다.")
    void perform() {

        Concert concert = new Concert(Elvis.INSTANCE);
        concert.perform();

        assertThat(concert.isLightsOn()).isTrue();
        assertThat(concert.isMainStateOpen()).isTrue();
    }

    @Test
    @DisplayName("대역 Elvis를 이용해 Concert의 perform 메서드를 테스트한다.")
    void performFake() {

            Concert concert = new Concert(new MockElvis());
            concert.perform();

            assertThat(concert.isLightsOn()).isTrue();
            assertThat(concert.isMainStateOpen()).isTrue();
    }
}