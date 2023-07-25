package org.hello.item03.first;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ElvisTest {

    @Test
    @DisplayName("Elvis 클래스의 인스턴스가 하나만 생성되는지 테스트한다.")
    void create() {
        Elvis elvis = Elvis.INSTANCE;
        Elvis elvis2 = Elvis.INSTANCE;

        assertAll(
                () -> assertThat(elvis).isEqualTo(elvis2),
                () -> assertThat(elvis).isSameAs(elvis2)
        );
    }

    @Test
    @DisplayName("리플렉션으로 여러번 호출되면 객체가 여러개 생성되는지 테스트한다.")
    void reflection() {


        try {
            Constructor<Elvis> defaultConstructor = Elvis.class.getDeclaredConstructor();

            defaultConstructor.setAccessible(true);
            Elvis elvis1 = defaultConstructor.newInstance();
            Elvis elvis2 = defaultConstructor.newInstance();

            assertAll(
                    () -> assertThat(elvis1).isNotEqualTo(elvis2),
                    () -> assertThat(elvis1).isNotSameAs(elvis2)
            );
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("리플렉션으로 생성자를 새로 생성하려면 에러가 발생한다.")
    void preventReflection() {
        try {
            Constructor<PreventElvis> defaultConstructor = PreventElvis.class.getDeclaredConstructor();

            defaultConstructor.setAccessible(true);

            Assertions.assertThatThrownBy(
                    defaultConstructor::newInstance
            ).isInstanceOf(InvocationTargetException.class);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("역직렬화 할때 새로운 인스턴스가 생길 수 있다.")
    void deserialization() {
        try (ObjectOutput out = new ObjectOutputStream(new FileOutputStream("elvis.obj"))) {
            out.writeObject(Elvis.INSTANCE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectInput in = new ObjectInputStream(new FileInputStream("elvis.obj"))) {
            Elvis elvis = (Elvis) in.readObject();
            assertAll(
                    () -> assertThat(elvis).isNotEqualTo(Elvis.INSTANCE),
                    () -> assertThat(elvis).isNotSameAs(Elvis.INSTANCE)
            );
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("역직렬화 할때 readResolve 메소드를 이용하면 싱글톤을 보장할 수 있다..")
    void deserializationSingle() {
        try (ObjectOutput out = new ObjectOutputStream(new FileOutputStream("serializableElvis.obj"))) {
            out.writeObject(SerializableElvis.INSTANCE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectInput in = new ObjectInputStream(new FileInputStream("serializableElvis.obj"))) {
            SerializableElvis elvis = (SerializableElvis) in.readObject();
            assertAll(
                    () -> assertThat(elvis).isEqualTo(SerializableElvis.INSTANCE),
                    () -> assertThat(elvis).isSameAs(SerializableElvis.INSTANCE)
            );
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}