package hello.testtheory;

import java.util.UUID;

public class SystemUuidHolder implements UuidHolder{

    @Override
    public String randomUuid() {
        return UUID.randomUUID().toString();
    }
}
