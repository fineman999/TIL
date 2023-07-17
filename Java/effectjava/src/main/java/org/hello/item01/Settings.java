package org.hello.item01;

public class Settings {
    private boolean useAutoSteering;
    private boolean useABS;
    private Difficulty difficulty;


    private Settings() {
    }

    private static final Settings SETTINGS = new Settings();

    public static Settings newInstance() {
        Boolean.valueOf(true);
        return SETTINGS;
    }
}
