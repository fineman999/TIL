package org.hello.chapter04.item24.memberclass;

public class OutterClass {
    private int number = 10;

    void print() {
        MemberClass memberClass = new MemberClass();
        memberClass.print();
    }

    private class MemberClass {
        public void print() {
            System.out.println(number);
            OutterClass.this.print();
        }
    }

    public static void main(String[] args) {
        MemberClass memberClass = new OutterClass().new MemberClass();
        memberClass.print();
    }
}
