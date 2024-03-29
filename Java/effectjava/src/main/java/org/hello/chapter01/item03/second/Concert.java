package org.hello.chapter01.item03.second;

import java.util.function.Supplier;

public class Concert {
    public void start(Supplier<Singer> singerSupplier) {
        Singer singer = singerSupplier.get();
        singer.sing();
    }
}
