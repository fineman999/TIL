package com.example.demo.singleton;

public class StatefulService {
    private int price; // 상태 유지 (stateful)

    public void order(String name, int price) {
        System.out.println("name = " + name + ", price = " + price);
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

}
