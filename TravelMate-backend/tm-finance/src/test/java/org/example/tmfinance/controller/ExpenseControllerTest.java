package org.example.tmfinance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.tmfinance.domain.po.Expense;
import org.example.tmfinance.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExpenseController.class)
public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpenseService expenseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddExpense() throws Exception {
        Expense expense = new Expense();
        expense.setEveID(10);
        expense.setType(1);
        expense.setTime(LocalDateTime.now());
        expense.setMoney(100.0f);
        expense.setName("Test Expense");

        when(expenseService.addExpense(expense)).thenReturn(1);

        mockMvc.perform(post("/expense/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expense)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1));
    }

    @Test
    public void testCountRecordsByEveID() throws Exception {
        when(expenseService.countRecordsByEveID(10)).thenReturn(5);

        mockMvc.perform(get("/expense/eventID/number").param("eveID", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(5));
    }

    @Test
    public void testModifyExpense() throws Exception {
        Expense expense = new Expense();
        expense.setId(1);
        expense.setEveID(10);
        expense.setType(2);
        expense.setTime(LocalDateTime.now());
        expense.setMoney(200.0f);
        expense.setName("Updated Expense");

        when(expenseService.modifyExpense(expense)).thenReturn(1);

        mockMvc.perform(put("/expense/modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expense)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1));
    }

    @Test
    public void testDeleteExpense() throws Exception {
        when(expenseService.removeById(1)).thenReturn(true);

        mockMvc.perform(delete("/expense/delete").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    public void testGetEventExpense() throws Exception {
        when(expenseService.getEventExpense(100)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/expense/event").param("eveID", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testDeleteExpenseByEveID() throws Exception {
        when(expenseService.deleteExpenseByEveID(100)).thenReturn(true);

        mockMvc.perform(delete("/expense/deletebyeveid").param("eveID", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));
    }
}
