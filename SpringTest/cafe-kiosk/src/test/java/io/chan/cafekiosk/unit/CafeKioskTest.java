package io.chan.cafekiosk.unit;

import io.chan.cafekiosk.unit.beverge.Americano;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CafeKioskTest {

    @Test
    void add() {
        final CafeKiosk cafeKiosk = new CafeKiosk(new ArrayList<>());
        cafeKiosk.add(new Americano());

        assertAll(
            () -> assertThat(cafeKiosk.getTotalPrice()).isEqualTo(4000),
            () -> assertThat(cafeKiosk.getMenu()).isEqualTo("아메리카노")
        );
    }
}