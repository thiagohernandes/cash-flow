package com.cashflow.validator;

import com.cashflow.domain.request.CashFlowRequest;
import com.cashflow.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class CashFlowValidator {

    private final ValidatorUtil validatorUtil;

    public void validator(final CashFlowRequest cashFlowRequest) {
        if (Objects.isNull(cashFlowRequest.getValue())) {
            validatorUtil.validate("O valor de lançamento não pode ser nulo!");
        }
        if (Objects.isNull(cashFlowRequest.getDate())) {
            validatorUtil.validate("A data de lançamento não pode ser nula!");
        }
        if (Objects.isNull(cashFlowRequest.getType())) {
            validatorUtil.validate("O tipo de lançamento não pode ser nulo!");
        }
    }
}
