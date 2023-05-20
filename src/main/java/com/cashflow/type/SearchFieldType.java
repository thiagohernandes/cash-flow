package com.cashflow.type;

import lombok.Getter;

@Getter
public enum SearchFieldType {
    BY_DATE("date");

    private final String field;

    SearchFieldType(String field) {
        this.field = field;
    }
}
