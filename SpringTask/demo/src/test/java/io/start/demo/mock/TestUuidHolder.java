package io.start.demo.mock;

import io.start.demo.common.service.port.UuidHolder;
import lombok.Setter;

@Setter
public class TestUuidHolder implements UuidHolder {

    private String uuid;

    public TestUuidHolder(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String random() {
        return uuid;
    }
}
