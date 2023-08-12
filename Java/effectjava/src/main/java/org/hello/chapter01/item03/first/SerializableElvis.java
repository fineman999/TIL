package org.hello.chapter01.item03.first;

import java.io.Serial;
import java.io.Serializable;

public class SerializableElvis implements Serializable {
    /**
     * 싱글톤 오브젝트
     */
    public static final SerializableElvis INSTANCE = new SerializableElvis();

    private SerializableElvis() {}

    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm outta here!");
    }

    public void sing() {
        System.out.println("Elvis is singing");
    }

    @Serial
    private Object readResolve() {
        return INSTANCE;
    }
}
