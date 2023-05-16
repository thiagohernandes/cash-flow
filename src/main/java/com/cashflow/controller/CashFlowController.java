package com.cashflow.controller;

import com.cashflow.domain.request.CashFlowRequest;
import com.cashflow.domain.response.CashFlowDailyCondensedResponse;
import com.cashflow.domain.response.CashFlowResponse;
import com.cashflow.exception.handler.HandlerValidationException;
import com.cashflow.service.CashFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
}
