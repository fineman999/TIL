package org.hello.item06;

public class Deprecation {

    /**
     * @deprecated in favor of {@link #Deprecation(String)}
     */
    @Deprecated(since = "2024-01-01", forRemoval = true)
    public Deprecation() { }

    private String name;

    public Deprecation(String name) {
        this.name = name;
    }
}
