package hello.testdriven.mock;

import hello.testdriven.common.service.port.UuidHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestUuidHolder implements UuidHolder {

    private final String uuid;
    @Override
    public String random() {
        return uuid;
    }
}
