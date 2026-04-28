package com.goggles.user_service.domain.model2;

public enum Bank {
    KB_KOOKMIN("KB국민은행"),
    SHINHAN("신한은행"),
    WOORI("우리은행"),
    HANA("하나은행"),
    NH_NONGHYUP("NH농협은행"),
    IBK("IBK기업은행"),
    KAKAO("카카오뱅크"),
    TOSS("토스뱅크"),
    K_BANK("케이뱅크"),
    SC("SC제일은행"),
    CITI("씨티은행"),
    DAEGU("대구은행"),
    BUSAN("부산은행"),
    GWANGJU("광주은행"),
    JEONBUK("전북은행"),
    GYEONGNAM("경남은행"),
    JEJU("제주은행"),
    POST("우체국"),
    SAEMAUL("새마을금고"),
    SINHYUP("신협");

    private final String displayName;

    Bank(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}