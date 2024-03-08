package io.chan.bookrentalservice.domain.model.feature;

import io.chan.bookrentalservice.domain.model.vo.LateFee;

public class LateFeeFixture {
    public static LateFee createLateFee() {
        return LateFee.createLateFee(100L);
    }
}
