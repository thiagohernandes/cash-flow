package com.cashflow.controller;

import com.cashflow.domain.request.CashFlowRequest;
import com.cashflow.domain.request.CashFlowSearchRequest;
import com.cashflow.domain.response.CashFlowDailyCondensedResponse;
import com.cashflow.domain.response.CashFlowReleaseResponse;
import com.cashflow.domain.response.CashFlowResponse;
import com.cashflow.exception.handler.HandlerValidationException;
import com.cashflow.service.CashFlowService;
import com.cashflow.type.SearchSortType;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cash-flow")
@RequiredArgsConstructor
public class CashFlowController {

    private final CashFlowService cashFlowService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public CashFlowResponse save(@RequestBody CashFlowRequest cashFlowRequest)
        throws HandlerValidationException {
        return cashFlowService.save(cashFlowRequest);
    }

    @GetMapping("/daily-condensed/{initialDate}/{finalDate}")
    @ResponseStatus(HttpStatus.OK)
    public CashFlowDailyCondensedResponse dailyCondensedByDate(
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate initialDate,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate finalDate
    ) {
        return cashFlowService.dailyCondensed(initialDate, finalDate);
    }

    @GetMapping("/financial-releases")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CashFlowReleaseResponse> financialReleases(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate initialDate,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate finalDate,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "ASC") SearchSortType sort,
        @RequestParam(defaultValue = "CREDIT") String type
    ) {
        final CashFlowSearchRequest request = CashFlowSearchRequest.builder()
            .sort(sort)
            .page(page)
            .size(size)
            .initialDate(initialDate)
            .finalDate(finalDate)
            .type(type)
            .build();

        return cashFlowService.financialReleases(request);
    }
}
