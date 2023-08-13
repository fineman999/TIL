package org.hello.chapter02.item14;

import java.util.Comparator;
import java.util.Objects;

public class PhoneNumber implements Comparable<PhoneNumber> {
    private final short areaCode, prefix, lineNum;

    public PhoneNumber(int areaCode, int prefix, int lineNum) {
        this.areaCode = rangeCheck(areaCode, 999, "지역코드");
        this.prefix = rangeCheck(prefix, 999, "프리픽스");
        this.lineNum = rangeCheck(lineNum, 9999, "가입자 번호");
    }

    private static short rangeCheck(int val, int max, String arg) {
        if (val < 0 || val > max) {
            throw new IllegalArgumentException(arg + ": " + val);
        }
        return (short) val;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return areaCode == that.areaCode && prefix == that.prefix && lineNum == that.lineNum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(areaCode, prefix, lineNum);
    }

//    @Override
//    public int compareTo(PhoneNumber o) {
//        int result = Short.compare(areaCode, o.areaCode);
//        if (result == 0) {
//            result = Short.compare(prefix, o.prefix);
//            if (result == 0) {
//                result = Short.compare(lineNum, o.lineNum);
//            }
//        }
//        return result;
//    }

    // 코드 14-3 비교자 생성 메서드를 활용한 비교자 (92쪽)
    private static final Comparator<PhoneNumber> COMPARATOR =
            Comparator.comparingInt(PhoneNumber::getAreaCode)
                    .thenComparingInt(PhoneNumber::getPrefix)
                    .thenComparingInt(PhoneNumber::getLineNum);

    @Override
    public int compareTo(PhoneNumber o) {
        return COMPARATOR.compare(this, o);
    }

    public short getAreaCode() {
        return areaCode;
    }

    public short getPrefix() {
        return prefix;
    }

    public short getLineNum() {
        return lineNum;
    }
}
