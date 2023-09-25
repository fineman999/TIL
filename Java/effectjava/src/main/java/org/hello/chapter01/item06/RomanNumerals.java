package org.hello.chapter01.item06;

import java.util.regex.Pattern;

public class RomanNumerals {

    // 성능을 훨씬 더 끌어올릴 수 있다.
    public static boolean isRomanNumeralSlow(String s) {
        return s.matches("^(?=.)M*(C[MD]|D?C{0,3})" + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
    }

    // 값비싼 객체를 재사용해 성능을 개선한다.
    private static final Pattern ROMAN =
            Pattern.compile("^(?=.)M*(C[MD]|D?C{0,3})" + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");

    public static boolean isRomanNumeralFast(String s) {
        return ROMAN.matcher(s).matches();
    }
}
