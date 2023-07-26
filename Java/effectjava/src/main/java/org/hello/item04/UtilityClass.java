package org.hello.item04;

public class UtilityClass {

    /**
     * 이 클래스는 인스턴스를 만들 수 없습니다.
     */
    private UtilityClass() {
        throw new AssertionError(); // 인스턴스 생성 방지
    }

    public static String hello() {
        return "hello";
    }
}
