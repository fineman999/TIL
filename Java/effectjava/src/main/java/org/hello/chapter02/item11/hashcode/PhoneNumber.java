package org.hello.chapter02.item11.hashcode;

import java.util.Objects;

public class PhoneNumber {
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
        return (short)val;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PhoneNumber)) {
            return false;
        }
        PhoneNumber pn = (PhoneNumber)o;
        return pn.lineNum == lineNum && pn.prefix == prefix && pn.areaCode == areaCode;
    }

    private volatile int hashCode;

    @Override
    public int hashCode() {
        if (this.hashCode != 0) {
            return hashCode;
        }
        synchronized (this) {
            int result = hashCode;
            if (result == 0) {
                result = Short.hashCode(areaCode);
                result = 31 * result + Short.hashCode(prefix);
                result = 31 * result + Short.hashCode(lineNum);
                hashCode = result;
            }
            return result;
        }
    }

    /**
     * 이 전화번호의 문자열 표현을 반환한다.
     * 이 문자열은 "XXX-YYY-ZZZZ" 형태의 12글자로 구성된다.
     * XXX는 지역코드, YYY는 프리픽스, ZZZZ는 가입자 번호다.
     * 각각의 대문자는 10진수 숫자 하나를 나타낸다.
     * <p>
     *     전화번호의 각 필드가 주어진 자릿수를 만족하지 못하면,
     *     앞에서부터 0으로 채워나간다. 예를 들어 가입자 번호가 123이면
     *     전화번호의 마지막 네 문자는 "0123"이 된다.
 *     </p>
     */
    @Override
    public String toString() {
        return String.format("%03d-%03d-%04d", areaCode, prefix, lineNum);
    }

    public static PhoneNumber of(String phoneNumber) {
        String[] split = phoneNumber.split("-");
        return new PhoneNumber(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }
}
