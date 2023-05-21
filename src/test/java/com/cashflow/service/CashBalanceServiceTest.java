package com.cashflow.service;

import com.cashflow.database.entity.CashBalanceEntity;
import com.cashflow.database.repository.CashBalanceRepository;
import com.cashflow.support.SupportTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CashBalanceServiceTest extends SupportTests {

    private final BigDecimal input = new BigDecimal("100");
    private final BigDecimal output = new BigDecimal("50");
    private final CashBalanceEntity entity = generator.nextObject(CashBalanceEntity.class);
    @Mock
    private CashBalanceRepository cashBalanceRepository;
    @InjectMocks
    private CashBalanceService cashBalanceService;

    @Test
    void shouldUpdateBalanceEmptyTest() {
         cashBalanceService.updateBalance(input, output);
    }

    @Test
    void shouldUpdateBalanceInitializedTest() {
        when(cashBalanceRepository.findAll())
            .thenReturn(Collections.singletonList(entity));

        cashBalanceService.updateBalance(input, output);
    }

    @Test
    void shouldVerifyBalanceTest() {
        when(cashBalanceRepository.findAll())
            .thenReturn(Collections.singletonList(entity));

        final var balance = cashBalanceService.verifyBalance();

        assertNotNull(balance);
    }
}
