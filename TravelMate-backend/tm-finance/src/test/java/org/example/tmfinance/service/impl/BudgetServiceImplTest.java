package org.example.tmfinance.service.impl;

import org.example.tmfinance.domain.po.Budget;
import org.example.tmfinance.service.BudgetService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BudgetServiceImplTest {

    private final BudgetService budgetService = mock(BudgetService.class);

    // TC01: 添加预算测试
    @Test
    public void testAddBudget() {
        Budget budget = new Budget();
        budget.setEveID(101);
        budget.setMoney(1000.0f);

        when(budgetService.addBudget(budget)).thenReturn(1);

        int result = budgetService.addBudget(budget);
        assertEquals(1, result);
    }

    // TC02: 修改预算测试
    @Test
    public void testModifyBudget() {
        Budget budget = new Budget();
        budget.setId(1);
        budget.setMoney(2000.0f);

        when(budgetService.modifyBudget(budget)).thenReturn(1);

        int result = budgetService.modifyBudget(budget);
        assertEquals(1, result);
    }

    // TC03: 删除预算测试
    @Test
    public void testRemoveBudgetById() {
        when(budgetService.removeById(1)).thenReturn(true);

        boolean result = budgetService.removeById(1);
        assertTrue(result);
    }

    // TC04: 查询事件预算列表测试
    @Test
    public void testGetEventBudget() {
        List<Budget> budgets = Collections.singletonList(new Budget());

        when(budgetService.getEventBudget(101)).thenReturn(budgets);

        List<Budget> result = budgetService.getEventBudget(101);
        assertFalse(result.isEmpty());
    }

    // TC05: 删除事件预算测试
    @Test
    public void testDeleteBudgetByEveID() {
        when(budgetService.deleteBudgetByEveID(101)).thenReturn(true);

        boolean result = budgetService.deleteBudgetByEveID(101);
        assertTrue(result);
    }
}
