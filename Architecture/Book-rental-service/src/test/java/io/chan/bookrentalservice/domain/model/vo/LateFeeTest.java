package io.chan.bookrentalservice.domain.model.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LateFeeTest {

    @DisplayName("LateFee에서 포인트를 추가한다")
    @Test
    void addPoint() {
        // given
        final Long point = 100L;
        final Long addPoint = 50L;
        final LateFee lateFee = LateFee.createLateFee(point);

        // when
        final LateFee addedLateFee = lateFee.addPoint(addPoint);

        // then
        assertThat(addedLateFee.getPoint()).isEqualTo(point + addPoint);
    }

    @DisplayName("LateFee에서 포인트를 차감한다")
    @Test
    void subtractPoint() {
        // given
        final Long point = 100L;
        final Long subtractPoint = 50L;
        final LateFee lateFee = LateFee.createLateFee(point);

        // when
        final LateFee subtractedLateFee = lateFee.subtractPoint(subtractPoint);

        // then
        assertThat(subtractedLateFee.getPoint()).isEqualTo(point - subtractPoint);
    }

    @DisplayName("LateFee에서 차감한 포인트가 음수가 되면 IllegalArgumentException을 던진다")
    @Test
    void subtractPointWithNegativePoint() {
        // given
        final Long point = 100L;
        final Long subtractPoint = 150L;
        final LateFee lateFee = LateFee.createLateFee(point);

        // when & then
        assertThatThrownBy(() -> lateFee.subtractPoint(subtractPoint))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The point cannot be negative.");
    }

    @DisplayName("LateFee을 생성한다")
    @Test
    void createLateFee() {
        // given
        final Long point = 100L;

        // when
        final LateFee lateFee = LateFee.createLateFee(point);

        // then
        assertThat(lateFee.getPoint()).isEqualTo(point);
    }
}