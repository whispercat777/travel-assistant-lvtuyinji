package org.example.tmfinance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.tmfinance.domain.po.Budget;
import org.example.tmfinance.service.BudgetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BudgetController.class)
public class BudgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BudgetService budgetService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddBudget() throws Exception {
        Budget budget = new Budget();
        budget.setEveID(101);
        budget.setMoney(1000.0f);

        when(budgetService.addBudget(budget)).thenReturn(1);

        mockMvc.perform(post("/budget/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budget)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1));
    }

    @Test
    public void testModifyBudget() throws Exception {
        Budget budget = new Budget();
        budget.setId(1);
        budget.setEveID(101);
        budget.setMoney(1500.0f);

        when(budgetService.modifyBudget(budget)).thenReturn(1);

        mockMvc.perform(put("/budget/modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budget)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1));
    }

    @Test
    public void testDeleteBudget() throws Exception {
        when(budgetService.removeById(1)).thenReturn(true);

        mockMvc.perform(delete("/budget/delete").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    public void testGetEventBudget() throws Exception {
        when(budgetService.getEventBudget(101)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/budget/event").param("eveID", "101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testDeleteBudgetByEveID() throws Exception {
        when(budgetService.deleteBudgetByEveID(101)).thenReturn(true);

        mockMvc.perform(delete("/budget/deletebyeveid").param("eveID", "101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));
    }
}
