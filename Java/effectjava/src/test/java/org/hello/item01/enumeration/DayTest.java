package org.hello.item01.enumeration;

import org.hello.chapter01.item01.enumeration.Day;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

class DayTest {

    @Test
    @DisplayName("EnumSet Test")
    void name() {
        EnumSet<Day> workingDays = EnumSet.of(Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY, Day.FRIDAY);

        for (Day day : workingDays) {
            System.out.println(day);
        }
    }
}