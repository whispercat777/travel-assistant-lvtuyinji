
package org.example.tmfinance.controller;

import org.example.tmfinance.domain.po.ItineraryReport;
import org.example.tmfinance.domain.po.TimeReport;
import org.example.tmfinance.domain.po.TypeReport;
import org.example.tmfinance.service.ExpenseService;
import org.example.tmfinance.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpenseService expenseService;

    @MockBean
    private ReportService reportService;

    @Test
    public void testGetAllExpense() throws Exception {
        TimeReport report = new TimeReport();
        when(expenseService.getAllExpense(1)).thenReturn(report);

        mockMvc.perform(get("/report/getall").param("userID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    public void testGetTypeExpense() throws Exception {
        TypeReport report = new TypeReport();
        when(expenseService.getTypeExpense(Collections.singletonList(1), 1)).thenReturn(report);

        mockMvc.perform(get("/report/type")
                        .param("types", "1")
                        .param("userID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    public void testGetTimeExpense() throws Exception {
        TimeReport report = new TimeReport();
        when(expenseService.getTimeExpense("2025-01-01", "2025-12-31", 1)).thenReturn(report);

        mockMvc.perform(get("/report/time")
                        .param("startTime", "2025-01-01")
                        .param("endTime", "2025-12-31")
                        .param("userID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    public void testGetAllBudgetAndExpense() throws Exception {
        ItineraryReport report = new ItineraryReport();
        when(reportService.getAllBudgetAndExpense(1)).thenReturn(report);

        mockMvc.perform(get("/report/budgetandexpense").param("userID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    public void testGetItiIDsBudgetAndExpense() throws Exception {
        ItineraryReport report = new ItineraryReport();
        when(reportService.getEventBudgetAndExpense(Collections.singletonList(1))).thenReturn(report);

        mockMvc.perform(get("/report/iti").param("itiIDs", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }
}
