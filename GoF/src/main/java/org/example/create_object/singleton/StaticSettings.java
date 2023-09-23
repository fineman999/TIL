package org.example.create_object.singleton;

import java.io.Serial;
import java.io.Serializable;

public class StaticSettings implements Serializable {
    private static StaticSettings INSTANCE;

    private StaticSettings() {
    }

    public static StaticSettings getInstance() {
        if (INSTANCE == null) INSTANCE = new StaticSettings();
        return INSTANCE;
    }

    @Serial
    protected Object readResolve() {
        return getInstance();
    }
}
