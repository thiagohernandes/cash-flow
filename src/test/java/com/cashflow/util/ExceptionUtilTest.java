package com.cashflow.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ExceptionUtilTest {

    @InjectMocks
    private ExceptionUtil exceptionUtil;

    @Test
    void shouldMessageExceptionTest() {
        assertThrows(RuntimeException.class, () -> exceptionUtil.message("Erro ocorrido!"));
    }

}
