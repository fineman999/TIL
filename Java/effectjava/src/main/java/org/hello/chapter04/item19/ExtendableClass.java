package org.hello.chapter04.item19;

/**
 * 상속용 클래스
 */
public class ExtendableClass {

    /**
     * 상속용 메서드
     *
     * @implSpec
     * System.out.println를 실행한다.
     */
    protected void doSomething() {
        System.out.println("상속용 메서드");
    }
}
