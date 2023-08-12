package org.hello.chapter01.item03.first;

import java.io.Serializable;

public class Elvis implements IElvis , Serializable {

    /**
     * 싱글톤 오브젝트
     */
    public static final Elvis INSTANCE = new Elvis();

    private Elvis() {}

    @Override
    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm outta here!");
    }

    @Override
    public void sing() {
        System.out.println("Elvis is singing");
    }

}
