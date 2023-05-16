package com.cashflow.type;

import lombok.Getter;

@Getter
public enum DatetimePatternType {
    YYYY_MM_DD_HH_MM_SS_SSS("yyyy-MM-dd'T'HH:mm:ss.SSS"),
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
    DD_MM_YYYY("yyyy-MM-dd"),
    YYYY_MM_DD("yyyy-MM-dd");

    private final String pattern;

    DatetimePatternType(final String pattern) {
        this.pattern = pattern;
    }
}
