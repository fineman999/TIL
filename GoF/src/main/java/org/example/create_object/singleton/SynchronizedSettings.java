package org.example.create_object.singleton;

public class SynchronizedSettings {
    private static SynchronizedSettings INSTANCE;

    private SynchronizedSettings() {
    }

    public static synchronized SynchronizedSettings getInstance() {
        if (INSTANCE == null) INSTANCE = new SynchronizedSettings();
        return INSTANCE;
    }
}
