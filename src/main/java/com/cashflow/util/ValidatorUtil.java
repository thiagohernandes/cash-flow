package com.cashflow.util;

import org.springframework.stereotype.Component;

@Component
public class ValidatorUtil {

    public void validate(final String msg) {
        throw new RuntimeException(msg);
    }
}
