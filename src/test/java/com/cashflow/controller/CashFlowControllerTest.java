package com.cashflow.controller;

import com.cashflow.domain.request.CashFlowRequest;
import com.cashflow.service.CashFlowService;
import com.cashflow.support.SupportTests;
import com.cashflow.type.CashFlowType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CashFlowControllerTest extends SupportTests {

    @Mock
    private CashFlowService cashFlowService;
    @InjectMocks
    private CashFlowController cashFlowController;

    @BeforeEach
    void init() {
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders
            .standaloneSetup(cashFlowController).build();
    }

    @Test
    void shouldGetDailyCondensedByDateTest() throws Exception {
        mockMvc.perform(get("/cash-flow/daily-condensed/2023-05-15/2023-05-17"))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void shouldSaveEntryTest() throws Exception {
        CashFlowRequest request = new CashFlowRequest();
        request.setDate(LocalDate.now());
        request.setType(CashFlowType.CREDIT.name());
        request.setValue(new BigDecimal("25000"));

        mockMvc.perform(post("/cash-flow/save")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());

    }
}