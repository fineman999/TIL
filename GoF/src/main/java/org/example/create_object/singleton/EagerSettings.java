package org.example.create_object.singleton;

public class EagerSettings {
    private static final EagerSettings INSTANCE = new EagerSettings();

    private EagerSettings() {
    }

    public static EagerSettings getInstance() {
        return INSTANCE;
    }
}
