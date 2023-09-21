package org.example.create_object.singleton;

public class StaticSettings {
    private static StaticSettings INSTANCE;

    private StaticSettings() {
    }

    public static StaticSettings getInstance() {
        if (INSTANCE == null) INSTANCE = new StaticSettings();
        return INSTANCE;
    }
}
