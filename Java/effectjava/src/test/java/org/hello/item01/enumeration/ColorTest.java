package org.hello.item01.enumeration;

import org.hello.chapter01.item01.enumeration.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

class ColorTest {
    @Test
    @DisplayName("EnumMap Test")
    void name() {
        EnumMap<Color, String> colorMap = new EnumMap<>(Color.class);

        colorMap.put(Color.RED, "빨강");
        colorMap.put(Color.GREEN, "초록");
        colorMap.put(Color.BLUE, "파랑");

        System.out.println("RED: " + colorMap.get(Color.RED));
        System.out.println("GREEN: " + colorMap.get(Color.GREEN));
        System.out.println("BLUE: " + colorMap.get(Color.BLUE));
    }
}