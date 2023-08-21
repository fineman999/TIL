package org.hello.chapter07.item43;

import io.micrometer.observation.Observation;

import java.time.Instant;
import java.util.function.Function;

public class LimitedInstanceClass {
    public static void main(String[] args) {

        Function<Instant, Boolean> isAfter = Instant.now()::isAfter;
        boolean result = isAfter.apply(Instant.now());
        System.out.println(result);
    }
}
