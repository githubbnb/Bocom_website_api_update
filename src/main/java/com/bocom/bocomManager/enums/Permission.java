package com.bocom.bocomManager.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    PROMOTION_READ("promotion::read"),
    PROMOTION_UPDATE("promotion::update"),
    PROMOTION_DELETE("promotion::delete"),
    PROMOTION_CREATE("promotion::create"),

    ACTUALITY_READ("actuality::read"),
    ACTUALITY_UPDATE("actuality::update"),
    ACTUALITY_DELETE("actuality::delete"),
    ACTUALITY_CREATE("actuality::create"),

    FILE_ADD("file::add"),
    FILE_DELETE("file::delete"),

    USER_READ("user::read"),
    USER_UPDATE("user::update"),
    USER_DELETE("user::delete"),
    USER_CREATE("user::create");

    @Getter
    private final String permission;
}