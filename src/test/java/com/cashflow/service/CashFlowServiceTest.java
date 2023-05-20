package com.cashflow.service;

import com.cashflow.database.entity.CashBalanceEntity;
import com.cashflow.database.entity.CashFlowEntity;
import com.cashflow.database.repository.CashFlowRepository;
import com.cashflow.domain.request.CashFlowRequest;
import com.cashflow.domain.request.CashFlowSearchRequest;
import com.cashflow.domain.response.CashFlowDailyCondensedResponse;
import com.cashflow.domain.response.CashFlowReleaseResponse;
import com.cashflow.domain.response.CashFlowResponse;
import com.cashflow.exception.handler.HandlerValidationException;
import com.cashflow.factory.CashFlowFactory;
import com.cashflow.support.SupportTests;
import com.cashflow.type.CashFlowType;
import com.cashflow.type.SearchFieldType;
import com.cashflow.type.SearchSortType;
import com.cashflow.util.ExceptionUtil;
import com.cashflow.util.FormatUtil;
import com.cashflow.validator.CashFlowValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
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
    private ExceptionUtil exceptionUtil;
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

    @BeforeEach
    void init() {
        cashFlowEntityCredit.setValue(new BigDecimal("500"));
        cashFlowEntityDebit.setValue(new BigDecimal("100"));
    }

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
    void shouldExceptionOnGetDailyCondensedWithoutBalancePresentTest() {
        when(cashFlowRepository
            .searchDatesType(initialDate, finalDate, CashFlowType.CREDIT.name()))
            .thenReturn(Collections.singletonList(cashFlowEntityCredit));

        when(cashFlowRepository
            .searchDatesType(initialDate, finalDate, CashFlowType.DEBIT.name()))
            .thenReturn(Collections.singletonList(cashFlowEntityDebit));

        assertThrows(RuntimeException.class, () -> cashFlowService
            .dailyCondensed(initialDate.plusDays(4), finalDate));
    }

    @Test
    void shouldGetFinancialReleasesTest() {
        final CashFlowSearchRequest cashFlowSearchRequest = CashFlowSearchRequest.builder()
            .sort(SearchSortType.DESC)
            .size(10)
            .page(0)
            .type("CREDIT")
            .initialDate(LocalDate.now())
            .finalDate(LocalDate.now().plusDays(10))
            .build();

        final Page<CashFlowEntity> page = mock(Page.class);
        final Pageable pageable = PageRequest.of(0, 10,
            Sort.by(Sort.Direction.DESC, SearchFieldType.BY_DATE.getField()));

        when(cashFlowRepository
            .searchDatesTypePageable(cashFlowSearchRequest.getInitialDate(),
                cashFlowSearchRequest.getFinalDate(),
                cashFlowSearchRequest.getType(),
                pageable))
            .thenReturn(page);

        Mono<CashFlowReleaseResponse> releaseResponseMono = cashFlowService
            .financialReleases(cashFlowSearchRequest);

        assertNotNull(releaseResponseMono);
    }

    @Test
    void shouldGetFinancialReleasesEmptyTest() {
        final CashFlowSearchRequest cashFlowSearchRequest = CashFlowSearchRequest.builder()
            .sort(SearchSortType.DESC)
            .size(10)
            .page(0)
            .type("CREDIT")
            .initialDate(LocalDate.now())
            .finalDate(LocalDate.now().plusDays(10))
            .build();

        final Pageable pageable = PageRequest.of(0, 10,
            Sort.by(Sort.Direction.DESC, SearchFieldType.BY_DATE.getField()));

        when(cashFlowRepository
            .searchDatesTypePageable(cashFlowSearchRequest.getInitialDate(),
                cashFlowSearchRequest.getFinalDate(),
                cashFlowSearchRequest.getType(),
                pageable))
            .thenReturn(null);

        Mono<CashFlowReleaseResponse> releaseResponseMono = cashFlowService
            .financialReleases(cashFlowSearchRequest);

        assertNotNull(releaseResponseMono);
    }
}
