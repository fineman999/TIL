package io.chan.accountservice.domain;

public enum BankCode {
    NONGHYUP("농협", "0011"),
    KOOKMIN("국민은행", "0004"),
    WOORI("우리은행", "0020"),
    SHINHAN("신한은행", "0088"),
    KIUP("기업은행", "0003"),
    HANA("하나은행", "0081"),
    SC("SC은행", "0023"),
    SANUP("산업은행", "0002"),
    CITI("씨티은행", "0027"),
    DAEGU("대구은행", "0031"),
    BUSAN("부산은행", "0032"),
    KYOUNGNAM("경남은행", "0039"),
    KWANGJU("광주은행", "0034"),
    JEONBUK("전북은행", "0037"),
    JEJU("제주은행", "0035"),
    POST("우체국", "0071"),
    SAEMAUL("새마을금고", "0045"),
    SHINHYUP("신협", "0048"),
    SUHYUP("수협", "0007");

    private final String name;
    private final String code;

    BankCode(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public static String getCodeByName(String name) {
        for (BankCode bankCode : BankCode.values()) {
            if (bankCode.getName().equals(name)) {
                return bankCode.getCode();
            }
        }
        return null;
    }
}
