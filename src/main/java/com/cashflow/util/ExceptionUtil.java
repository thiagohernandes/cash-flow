package com.cashflow.util;

import org.springframework.stereotype.Component;

@Component
public class ExceptionUtil {

    public void message(final String msg) {
        throw new RuntimeException(msg);
    }
}
