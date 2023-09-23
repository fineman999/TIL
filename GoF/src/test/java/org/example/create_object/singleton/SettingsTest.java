package org.example.create_object.singleton;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;

class SettingsTest {


    @Test
    @DisplayName("basicSettings1과 basicSettings2의 인스턴스는 같지 않다.")
    void test() {
        BasicSettings basicSettings1 = new BasicSettings();
        BasicSettings basicSettings2 = new BasicSettings();

        assertThat(basicSettings1).isNotSameAs(basicSettings2);
    }

    @Test
    @DisplayName("staticSettings1과 staticSettings2의 인스턴스는 같다.")
    void test2() {
        StaticSettings staticSettings1 = StaticSettings.getInstance();
        StaticSettings staticSettings2 = StaticSettings.getInstance();

        assertThat(staticSettings1).isSameAs(staticSettings2);
    }

    @Test
    @DisplayName("싱글톤 패턴 구현 깨트리는 방법 - 리플렉션")
    void reflection() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        StaticSettings settings = StaticSettings.getInstance();

        Constructor<StaticSettings> constructor = StaticSettings.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        StaticSettings newInstance = constructor.newInstance();

        assertThat(settings).isNotSameAs(newInstance);
    }

    @Test
    @DisplayName("싱글톤 패턴 구현 깨트리는 방법 - 직렬화")
    void serialization() throws IOException, ClassNotFoundException {
        StaticSettings settings = StaticSettings.getInstance();
        StaticSettings newInstance = null;

        // 직렬화
         try (FileOutputStream fos = new FileOutputStream("settings.bin");
              ObjectOutputStream oos = new ObjectOutputStream(fos)) {
             oos.writeObject(settings);
         }

        // 역직렬화
         try (FileInputStream fis = new FileInputStream("settings.bin");
              ObjectInputStream ois = new ObjectInputStream(fis)) {
             newInstance = (StaticSettings) ois.readObject();
         }

        assertThat(settings).isNotSameAs(newInstance);
    }

    @Test
    @DisplayName("enum 싱글톤 - 역직렬화")
    void enumSingleton() throws IOException, ClassNotFoundException {
        EnumSettings instance1 = EnumSettings.INSTANCE;
        EnumSettings instance2 = null;

        // 직렬화
        try (FileOutputStream fos = new FileOutputStream("settings.bin");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(instance1);
        }

        // 역직렬화
        try (FileInputStream fis = new FileInputStream("settings.bin");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            instance2 = (EnumSettings) ois.readObject();
        }

        assertThat(instance1).isSameAs(instance2);
    }
}

