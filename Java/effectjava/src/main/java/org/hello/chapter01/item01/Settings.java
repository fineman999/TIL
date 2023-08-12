package org.hello.chapter01.item01;

/**
 * 이 클래스의 인스턴스는 #getInstance()를 사용한다.
 * @See #getInstance()
 */
public class Settings {
    private boolean useAutoSteering;
    private boolean useABS;
    private Difficulty difficulty;


    private Settings() {}
    private static final Settings SETTINGS = new Settings();

    public static Settings getInstance() {
        return SETTINGS;
    }

    public static void main(String[] args) {

    }
}
