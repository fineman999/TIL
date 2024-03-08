package io.chan.bookrentalservice.domain.model.feature;

import io.chan.bookrentalservice.domain.model.vo.IDName;

public class IDNameFixture {
    public static IDName createIDName() {
        return IDName.createIDName("id", "name");
    }
}
