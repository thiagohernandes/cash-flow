package com.cashflow.validator;

import com.cashflow.domain.request.CashFlowRequest;
import com.cashflow.support.SupportTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CashFlowValidatorTest extends SupportTests {

    private final CashFlowRequest cashFlowRequest =
        generator.nextObject(CashFlowRequest.class);
    @InjectMocks
    private CashFlowValidator cashFlowValidator;

    @Test
    void shouldValidateSuccessTest() {
        cashFlowRequest.setType("CREDIT");
        cashFlowValidator.validator(cashFlowRequest);
    }

    @Test
    void shouldExceptionOnValidateTypeTest() {
        cashFlowRequest.setType("ANYTHING");

        assertThrows(RuntimeException.class, () -> cashFlowValidator.validator(cashFlowRequest));
    }

    @Test
    void shouldExceptionOnValidateValueZeroTest() {
        cashFlowRequest.setValue(new BigDecimal("0.0"));
        assertThrows(RuntimeException.class, () -> cashFlowValidator.validator(cashFlowRequest));
    }
}
