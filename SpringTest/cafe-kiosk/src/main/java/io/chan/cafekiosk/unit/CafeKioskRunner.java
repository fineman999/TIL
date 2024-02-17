package io.chan.cafekiosk.unit;

import io.chan.cafekiosk.unit.beverge.Americano;
import io.chan.cafekiosk.unit.beverge.Beverage;
import io.chan.cafekiosk.unit.beverge.Latte;

import java.util.ArrayList;

public class CafeKioskRunner {
    public static void main(String[] args) {
        final CafeKiosk cafeKiosk = new CafeKiosk(new ArrayList<>());

        final Beverage latte = new Latte();
        cafeKiosk.add(latte);
        System.out.println("라뗴 추가");

        final Beverage americano = new Americano();
        cafeKiosk.add(americano);
        System.out.println("아메리카노 추가");

        System.out.printf("메뉴: %s\n", cafeKiosk.getMenu());
        System.out.printf("총 가격: %d\n", cafeKiosk.getTotalPrice());
    }
}
