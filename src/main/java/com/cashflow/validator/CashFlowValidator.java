package com.cashflow.validator;

import com.cashflow.domain.request.CashFlowRequest;
import com.cashflow.type.CashFlowType;
import com.cashflow.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class CashFlowValidator {

    private final ExceptionUtil exceptionUtil;

    public void validator(final CashFlowRequest cashFlowRequest) {
        if (Objects.isNull(cashFlowRequest.getValue())) {
            exceptionUtil.message("O valor de lançamento não pode ser nulo!");
        }
        if (BigDecimal.ZERO.compareTo(cashFlowRequest.getValue()) == 0) {
            exceptionUtil.message("O valor de lançamento deve ser maior que zero!");
        }
        if (Objects.isNull(cashFlowRequest.getDate())) {
            exceptionUtil.message("A data de lançamento não pode ser nula!");
        }
        if (Objects.isNull(cashFlowRequest.getType())) {
            exceptionUtil.message("O tipo de lançamento não pode ser nulo!");
        }
        if (cashFlowRequest.getType().toUpperCase() != CashFlowType.DEBIT.name()
            && cashFlowRequest.getType().toUpperCase() != CashFlowType.CREDIT.name()) {
            exceptionUtil.message("O tipo de lançamento deve ser CREDIT ou DEBIT");
        }
    }
}
