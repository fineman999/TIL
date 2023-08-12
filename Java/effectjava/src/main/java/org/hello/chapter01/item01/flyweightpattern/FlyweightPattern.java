package org.hello.chapter01.item01.flyweightpattern;

public class FlyweightPattern {
    public static void main(String[] args) {

        Dog choco = new Dog("choco", 2, "수컷", "진돗개");
        Dog coco = new Dog("coco", 3, "암컷", "시바");

        System.out.println(choco);
        System.out.println(coco);
    }
}
