package org.hello.chapter04.item24.staticmemberclass;

public class OutterClass {
    private static int a = 10;
    private int b = 20;

    public static class StaticMemberClass {
        public void print() {
            System.out.println(a);
            // System.out.println(b); // 컴파일 에러
        }
    }

    public static void main(String[] args) {
        StaticMemberClass staticMemberClass = new StaticMemberClass();
        staticMemberClass.print();
    }
}
