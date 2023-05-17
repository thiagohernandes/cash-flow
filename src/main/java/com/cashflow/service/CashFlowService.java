package com.cashflow.service;

import com.cashflow.database.repository.CashFlowRepository;
import com.cashflow.domain.request.CashFlowRequest;
import com.cashflow.domain.response.CashFlowDailyCondensedResponse;
import com.cashflow.domain.response.CashFlowResponse;
import com.cashflow.exception.handler.HandlerValidationException;
import com.cashflow.factory.CashFlowFactory;
import com.cashflow.type.CashFlowType;
import com.cashflow.type.DatetimePatternType;
import com.cashflow.util.FormatUtil;
import com.cashflow.validator.CashFlowValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CashFlowService {

    private final FormatUtil formatUtil;
    private final CashFlowValidator cashFlowValidator;
    private final CashFlowFactory cashFlowFactory;
    private final CashFlowRepository cashFlowRepository;
    private final CashBalanceService cashBalanceService;

    @Transactional(propagation = Propagation.REQUIRED)
    public CashFlowResponse save(final CashFlowRequest cashFlowRequest)
        throws HandlerValidationException {
        var input = new BigDecimal("0.0");
        var output = new BigDecimal("0.0");

        log.info("Iniciando validação de lançamento...");

        cashFlowValidator.validator(cashFlowRequest);

        final var cashFlow =  Optional.ofNullable(cashFlowRequest)
            .map(request -> {
                log.info("Lançamento salvo com sucesso!");

                handleTypeCashFlow(cashFlowRequest, input, output);

                return cashFlowFactory.entityToResponse(cashFlowRepository
                    .save(cashFlowFactory.dtoToEntity(request)));
            });

        return cashFlow.orElseThrow(() -> {
            final var msg = "Problemas ao tentar salvar lançamento!";
            log.error(msg);

            throw new RuntimeException(msg);
        });
    }

    public CashFlowDailyCondensedResponse dailyCondensed(final LocalDate initialDate,
                                                         final LocalDate finalDate) {
        final var balance = Optional.ofNullable(cashBalanceService.verifyBalance());

        final var inputDailyCondensed = cashFlowRepository
            .searchDatesType(initialDate, finalDate, CashFlowType.CREDIT.name())
            .stream()
            .map(item -> Objects.isNull(item.getValue()) ? new BigDecimal("0.0")
                : item.getValue())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        final var outputDailyCondensed = cashFlowRepository
            .searchDatesType(initialDate, finalDate, CashFlowType.DEBIT.name())
            .stream()
            .map(item -> Objects.isNull(item.getValue()) ? new BigDecimal("0.0")
                : item.getValue())
            .reduce(BigDecimal.ZERO, BigDecimal::add);;

        if (balance.isPresent()) {
            final var balanceCalc = balance.get();
            final var calculedBalance = balanceCalc.getInput().subtract(balanceCalc.getOutput());

            return CashFlowDailyCondensedResponse.builder()
                .initialDate(formatUtil.formatLocalDateToString(initialDate,
                    DatetimePatternType.DD_MM_YYYY))
                .finalDate(formatUtil.formatLocalDateToString(finalDate,
                    DatetimePatternType.DD_MM_YYYY))
                .balance(calculedBalance)
                .input(inputDailyCondensed)
                .output(outputDailyCondensed)
                .build();
        } else {
            final var calculedDaily = inputDailyCondensed.subtract(outputDailyCondensed);

            return CashFlowDailyCondensedResponse.builder()
                .initialDate(formatUtil.formatLocalDateToString(initialDate,
                    DatetimePatternType.DD_MM_YYYY))
                .finalDate(formatUtil.formatLocalDateToString(finalDate,
                    DatetimePatternType.DD_MM_YYYY))
                .balance(calculedDaily)
                .input(inputDailyCondensed)
                .output(outputDailyCondensed)
                .build();
        }
    }

    private void handleTypeCashFlow(final CashFlowRequest cashFlowRequest,
                                    final BigDecimal input,
                                    final BigDecimal output) {
        if (cashFlowRequest.getType().equals(CashFlowType.CREDIT.name())) {
            cashBalanceService.updateBalance(cashFlowRequest.getValue(), output);
        } else {
            cashBalanceService.updateBalance(input, cashFlowRequest.getValue());
        }
    }
}
