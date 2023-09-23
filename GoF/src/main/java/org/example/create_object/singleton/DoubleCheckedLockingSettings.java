package org.example.create_object.singleton;

public class DoubleCheckedLockingSettings {
    private volatile static DoubleCheckedLockingSettings INSTANCE;

    private DoubleCheckedLockingSettings() {
    }

    public static DoubleCheckedLockingSettings getInstance() {
        if (INSTANCE == null) {
            synchronized (DoubleCheckedLockingSettings.class) {
                if (INSTANCE == null) INSTANCE = new DoubleCheckedLockingSettings();
            }
        }
        return INSTANCE;
    }
}
