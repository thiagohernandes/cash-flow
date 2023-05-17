package com.cashflow.service;

import com.cashflow.database.entity.CashBalanceEntity;
import com.cashflow.database.entity.CashFlowEntity;
import com.cashflow.database.repository.CashFlowRepository;
import com.cashflow.domain.request.CashFlowRequest;
import com.cashflow.domain.response.CashFlowDailyCondensedResponse;
import com.cashflow.domain.response.CashFlowResponse;
import com.cashflow.exception.handler.HandlerValidationException;
import com.cashflow.factory.CashFlowFactory;
import com.cashflow.support.SupportTests;
import com.cashflow.type.CashFlowType;
import com.cashflow.util.FormatUtil;
import com.cashflow.validator.CashFlowValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CashFlowServiceTest extends SupportTests {

    private final CashBalanceEntity cashBalanceEntity = generator.nextObject(CashBalanceEntity.class);
    private final CashFlowEntity cashFlowEntityCredit = generator.nextObject(CashFlowEntity.class);
    private final CashFlowEntity cashFlowEntityDebit = generator.nextObject(CashFlowEntity.class);
    private final CashFlowRequest request = generator.nextObject(CashFlowRequest.class);
    private final CashFlowResponse response = generator.nextObject(CashFlowResponse.class);
    private final LocalDate initialDate = LocalDate.now();
    private final LocalDate finalDate = LocalDate.now().plusDays(3);
    @Mock
    private FormatUtil formatUtil;
    @Mock
    private CashFlowValidator cashFlowValidator;
    @Mock
    private CashFlowFactory cashFlowFactory;
    @Mock
    private CashFlowRepository cashFlowRepository;
    @Mock
    private CashBalanceService cashBalanceService;
    @InjectMocks
    private CashFlowService cashFlowService;

    @Test
    void shouldSaveFlowRequestTest() throws HandlerValidationException {
        when(cashFlowFactory.entityToResponse(cashFlowRepository
            .save(cashFlowFactory.dtoToEntity(request))))
            .thenReturn(response);

        CashFlowResponse flowResponse = cashFlowService.save(request);

        assertNotNull(flowResponse);
    }

    @Test
    void shouldExceptionOnSaveFlowRequestTest() throws HandlerValidationException {
        assertThrows(RuntimeException.class, () ->  cashFlowService.save(request));
    }

    @Test
    void shouldGetDailyCondensedWithBalancePresentTest() {
        cashFlowEntityCredit.setValue(new BigDecimal("500"));
        cashFlowEntityDebit.setValue(new BigDecimal("100"));

        when(cashBalanceService.verifyBalance())
            .thenReturn(cashBalanceEntity);

        when(cashFlowRepository
            .searchDatesType(initialDate, finalDate, CashFlowType.CREDIT.name()))
            .thenReturn(Collections.singletonList(cashFlowEntityCredit));

        when(cashFlowRepository
            .searchDatesType(initialDate, finalDate, CashFlowType.DEBIT.name()))
            .thenReturn(Collections.singletonList(cashFlowEntityDebit));

        CashFlowDailyCondensedResponse dailyCondensed =
            cashFlowService.dailyCondensed(initialDate, finalDate);

        assertNotNull(dailyCondensed);
    }

    @Test
    void shouldGetDailyCondensedWithoutBalancePresentTest() {
        cashFlowEntityCredit.setValue(new BigDecimal("500"));
        cashFlowEntityDebit.setValue(new BigDecimal("100"));

        when(cashFlowRepository
            .searchDatesType(initialDate, finalDate, CashFlowType.CREDIT.name()))
            .thenReturn(Collections.singletonList(cashFlowEntityCredit));

        when(cashFlowRepository
            .searchDatesType(initialDate, finalDate, CashFlowType.DEBIT.name()))
            .thenReturn(Collections.singletonList(cashFlowEntityDebit));

        CashFlowDailyCondensedResponse dailyCondensed =
            cashFlowService.dailyCondensed(initialDate, finalDate);

        assertNotNull(dailyCondensed);
    }

}
