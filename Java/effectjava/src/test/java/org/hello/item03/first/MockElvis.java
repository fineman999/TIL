package org.hello.item03.first;

import org.hello.chapter01.item03.first.IElvis;

public class MockElvis implements IElvis {
    @Override
    public void leaveTheBuilding() {

    }

    @Override
    public void sing() {
        System.out.println("MockElvis is singing");
    }
}
