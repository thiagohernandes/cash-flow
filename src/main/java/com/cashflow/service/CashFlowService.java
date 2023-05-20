package com.cashflow.service;

import com.cashflow.database.entity.CashFlowEntity;
import com.cashflow.database.repository.CashFlowRepository;
import com.cashflow.domain.request.CashFlowRequest;
import com.cashflow.domain.request.CashFlowSearchRequest;
import com.cashflow.domain.response.CashFlowDailyCondensedResponse;
import com.cashflow.domain.response.CashFlowReleaseResponse;
import com.cashflow.domain.response.CashFlowResponse;
import com.cashflow.exception.handler.HandlerValidationException;
import com.cashflow.factory.CashFlowFactory;
import com.cashflow.type.CashFlowType;
import com.cashflow.type.DatetimePatternType;
import com.cashflow.type.SearchFieldType;
import com.cashflow.util.ExceptionUtil;
import com.cashflow.util.FormatUtil;
import com.cashflow.validator.CashFlowValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CashFlowService {

    private final ExceptionUtil exceptionUtil;
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
        log.info("Iniciando geração de condensado...");

        handleDates(initialDate, finalDate);
        final var balance = Optional.ofNullable(cashBalanceService.verifyBalance());

        log.info("Calculando entrada...");
        final var inputDailyCondensed = buiderSumValueCashFlow(cashFlowRepository
            .searchDatesType(initialDate, finalDate, CashFlowType.CREDIT.name()));

        log.info("Calculando saídas...");
        final var outputDailyCondensed = buiderSumValueCashFlow(cashFlowRepository
            .searchDatesType(initialDate, finalDate, CashFlowType.DEBIT.name()));

        if (balance.isPresent()) {
            final var balanceCalc = balance.get();
            final var calculedBalance = balanceCalc.getInput().subtract(balanceCalc.getOutput());

            return builderCashFlowDaily(calculedBalance, inputDailyCondensed,
                outputDailyCondensed, initialDate, finalDate);
        } else {
            final var calculedDaily = inputDailyCondensed.subtract(outputDailyCondensed);

            return builderCashFlowDaily(calculedDaily, inputDailyCondensed,
                outputDailyCondensed, initialDate, finalDate);
        }
    }

    public Mono<CashFlowReleaseResponse> financialReleases(final CashFlowSearchRequest request) {
        log.info("Iniciando geração lançamentos - request: {}", request);

        cashFlowValidator.validatorType(request);
        handleDates(request.getInitialDate(), request.getFinalDate());

        log.info("Paginação...");

        final var pageable = PageRequest.of(request.getPage(), request.getSize(),
            Sort.by(request.getSort().name().equals(Sort.Direction.DESC.name())
                ? Sort.Direction.DESC : Sort.Direction.ASC, SearchFieldType.BY_DATE.getField()));

        final var pageFlowSearch = Optional.ofNullable(cashFlowRepository
            .searchDatesTypePageable(request.getInitialDate(),
                request.getFinalDate(), request.getType(), pageable));

        if (pageFlowSearch.isPresent()) {
            log.info("Gerando releases...");

            return Mono.fromSupplier(() -> {
                final var isLast = pageFlowSearch.get().isLast();
                final var totalPages = pageFlowSearch.get().getTotalPages();
                final var sumAmountType = buiderSumValueCashFlow(pageFlowSearch.get().getContent());

                return CashFlowReleaseResponse.builder()
                        .releases(cashFlowFactory.entityToResponse(pageFlowSearch.get()))
                        .amount(sumAmountType)
                        .type(request.getType())
                        .isLast(isLast)
                        .totalPages(totalPages)
                        .build();
            }).onErrorResume(e -> {
                log.warn("Problemas na geração de solicitação de lançamentos");
                return Mono.empty();
            });
        } else {
            return Mono.fromSupplier(() ->
                CashFlowReleaseResponse.builder().build());
        }
    }

    private BigDecimal buiderSumValueCashFlow(final List<CashFlowEntity> listCashFlowEntity) {
        return listCashFlowEntity.stream()
            .map(item -> Objects.isNull(item.getValue()) ? new BigDecimal("0.0")
                : item.getValue())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private CashFlowDailyCondensedResponse builderCashFlowDaily(final BigDecimal value,
                                                                final BigDecimal input,
                                                                final BigDecimal output,
                                                                final LocalDate initialDate,
                                                                final LocalDate finalDate) {
        return CashFlowDailyCondensedResponse.builder()
            .initialDate(formatUtil.formatLocalDateToString(initialDate,
                DatetimePatternType.DD_MM_YYYY))
            .finalDate(formatUtil.formatLocalDateToString(finalDate,
                DatetimePatternType.DD_MM_YYYY))
            .balance(value)
            .input(input)
            .output(output)
            .build();
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

    private void handleDates(final LocalDate initialDate, final LocalDate finalDate) {
        if (initialDate.isAfter(finalDate)) {
            exceptionUtil.message("A data inicial não pode ser maior que a final!");
        }
    }
}
