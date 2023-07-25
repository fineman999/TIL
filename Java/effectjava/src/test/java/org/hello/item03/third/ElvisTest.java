package org.hello.item03.third;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ElvisTest {

    @Test
    @DisplayName("열거 타입으로 싱글턴을 만들 수 있다")
    void create() {
        Elvis elvis = Elvis.INSTANCE;
        elvis.leaveTheBuilding();
        assertAll(
                ()-> assertThat(elvis).isEqualTo(Elvis.INSTANCE),
                ()-> assertThat(elvis).isSameAs(Elvis.INSTANCE)
        );
    }

    @Test
    @DisplayName("새로운 생성자 생성시 예외가 발생한다")
    void exception() {

        assertThatThrownBy(
                Elvis.class::getDeclaredConstructor
        ).isInstanceOf(NoSuchMethodException.class);
    }

    @Test
    @DisplayName("역직렬화를 해도 동일한 인스턴스를 얻을 수 있다.")
    void serialization() {

       try (ObjectOutput out = new ObjectOutputStream(new FileOutputStream("elvis.ser"))) {
           out.writeObject(Elvis.INSTANCE);
       } catch (IOException e) {
           e.printStackTrace();
       }

       try (ObjectInput in = new ObjectInputStream(new FileInputStream("elvis.ser"))) {
           Elvis elvis = (Elvis) in.readObject();
           assertThat(elvis).isSameAs(Elvis.INSTANCE);
       } catch (IOException | ClassNotFoundException e) {
           e.printStackTrace();
       }
    }
}