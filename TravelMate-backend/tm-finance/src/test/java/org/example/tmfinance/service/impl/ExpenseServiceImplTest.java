
package org.example.tmfinance.service.impl;

import org.example.tmfinance.domain.po.Expense;
import org.example.tmfinance.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExpenseServiceImplTest {

    private final ExpenseService expenseService = mock(ExpenseService.class);

    // TC01: 添加支出测试1
    @Test
    public void testAddExpense() {
        Expense expense = new Expense();
        expense.setEveID(101);
        expense.setMoney(100.0f);
        expense.setType(1);
        expense.setTime(LocalDateTime.now());
        expense.setName("Test Expense");

        when(expenseService.addExpense(expense)).thenReturn(1);

        int result = expenseService.addExpense(expense);
        assertEquals(1, result);
    }

    // TC02: 修改支出测试
    @Test
    public void testModifyExpense() {
        Expense expense = new Expense();
        expense.setId(1);
        expense.setMoney(200.0f);
        expense.setName("Updated");

        when(expenseService.modifyExpense(expense)).thenReturn(1);

        int result = expenseService.modifyExpense(expense);
        assertEquals(1, result);
    }

    // TC03: 删除支出测试
    @Test
    public void testRemoveExpenseById() {
        when(expenseService.removeById(1)).thenReturn(true);

        boolean result = expenseService.removeById(1);
        assertTrue(result);
    }

    // TC04: 获取事件支出测试
    @Test
    public void testGetEventExpense() {
        List<Expense> expenses = Collections.singletonList(new Expense());

        when(expenseService.getEventExpense(101)).thenReturn(expenses);

        List<Expense> result = expenseService.getEventExpense(101);
        assertFalse(result.isEmpty());
    }

    // TC05: 删除事件支出测试
    @Test
    public void testDeleteExpenseByEveID() {
        when(expenseService.deleteExpenseByEveID(101)).thenReturn(true);

        boolean result = expenseService.deleteExpenseByEveID(101);
        assertTrue(result);
    }

    // TC06: 计数测试
    @Test
    public void testCountRecordsByEveID() {
        when(expenseService.countRecordsByEveID(101)).thenReturn(3);

        int count = expenseService.countRecordsByEveID(101);
        assertEquals(3, count);
    }

    // TC07: 异常情况测试
    @Test
    public void testGetEventExpenseEmpty() {
        when(expenseService.getEventExpense(999)).thenReturn(Collections.emptyList());

        List<Expense> result = expenseService.getEventExpense(999);
        assertTrue(result.isEmpty());
    }
}
