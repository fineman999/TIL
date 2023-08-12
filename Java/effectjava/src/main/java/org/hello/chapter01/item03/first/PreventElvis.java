package org.hello.chapter01.item03.first;

public class PreventElvis {

    /**
     * 싱글톤 오브젝트
     */
    public static final PreventElvis INSTANCE = new PreventElvis();

    private static boolean created;

    private PreventElvis() {
        if (created) {
            throw new UnsupportedOperationException("이 객체는 이미 생성되었습니다.");
        }
        created = true;
    }

    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm outta here!");
    }


    public void sing() {
        System.out.println("Elvis is singing");
    }

}
