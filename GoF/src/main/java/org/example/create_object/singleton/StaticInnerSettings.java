package org.example.create_object.singleton;

public class StaticInnerSettings {
    private StaticInnerSettings() {
    }

    private static class SingletonHolder {
        private static final StaticInnerSettings INSTANCE = new StaticInnerSettings();
    }

    public static StaticInnerSettings getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
