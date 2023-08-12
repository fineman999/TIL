package org.hello.item01.flyweightpattern.after;

import org.hello.chapter01.item01.flyweightpattern.after.Character;
import org.hello.chapter01.item01.flyweightpattern.after.FontFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FontTest {
    @Test
    @DisplayName("플라이웨이트 패턴 적용 테스트")
    void cache() {
        FontFactory fontFactory = new FontFactory();
        Character c1 = new Character('a', "red", fontFactory.getFont("nanum:12"));
        Character c2 = new Character('e', "red", fontFactory.getFont("nanum:12"));
        Character c3 = new Character('l', "red", fontFactory.getFont("nanum:12"));
    }
}