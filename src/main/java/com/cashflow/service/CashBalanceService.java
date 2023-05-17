package com.cashflow.service;

import com.cashflow.database.entity.CashBalanceEntity;
import com.cashflow.database.repository.CashBalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CashBalanceService {

    private final CashBalanceRepository cashBalanceRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateBalance(final BigDecimal input,
                              final BigDecimal output) {
        log.info("Salvando/atualizando saldo!");

        final var balance = Optional.ofNullable(verifyBalance());

        cashBalanceRepository.deleteAll();

        balance.ifPresentOrElse(value -> {
                cashBalanceRepository.save(CashBalanceEntity.builder()
                        .input(value.getInput().add(input))
                        .output(value.getOutput().add(output))
                    .build());
            },
            () -> {
                cashBalanceRepository.save(CashBalanceEntity.builder()
                    .input(input)
                    .output(output)
                    .build());
        });

        log.info("Saldo atualizado com sucesso!");
    }

    public CashBalanceEntity verifyBalance() {
        return Optional.ofNullable(cashBalanceRepository.findAll()
            .stream().findFirst())
            .get()
            .orElse(null);
    }
}
