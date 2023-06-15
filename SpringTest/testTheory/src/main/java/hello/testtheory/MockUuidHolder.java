package hello.testtheory;

public class MockUuidHolder implements UuidHolder{

    public final String uuidString;

    public MockUuidHolder(String uuidString) {
        this.uuidString = uuidString;
    }

    @Override
    public String randomUuid() {
        return uuidString;
    }
}
