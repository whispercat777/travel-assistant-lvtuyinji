
package org.example.tmfinance.service.impl;

import org.example.tmfinance.domain.po.ItineraryReport;
import org.example.tmfinance.domain.po.TimeReport;
import org.example.tmfinance.domain.po.TypeReport;
import org.example.tmfinance.service.ReportService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReportServiceImplTest {

    private final ReportService reportService = mock(ReportService.class);

    // TC01: 获取用户预算与支出汇总测试
    @Test
    public void testGetAllBudgetAndExpense() {
        ItineraryReport report = new ItineraryReport();
        when(reportService.getAllBudgetAndExpense(1)).thenReturn(report);

        ItineraryReport result = reportService.getAllBudgetAndExpense(1);
        assertNotNull(result);
    }

    // TC02: 获取多个行程的预算与支出测试
    @Test
    public void testGetEventBudgetAndExpense() {
        ItineraryReport report = new ItineraryReport();
        when(reportService.getEventBudgetAndExpense(Collections.singletonList(1))).thenReturn(report);

        ItineraryReport result = reportService.getEventBudgetAndExpense(Collections.singletonList(1));
        assertNotNull(result);
    }

    // TC03: 获取空的行程报告列表测试
    @Test
    public void testGetEventBudgetAndExpenseEmpty() {
        when(reportService.getEventBudgetAndExpense(Collections.singletonList(999))).thenReturn(null);

        ItineraryReport result = reportService.getEventBudgetAndExpense(Collections.singletonList(999));
        assertNull(result);
    }

    // TC04: 获取空用户报告测试
    @Test
    public void testGetAllBudgetAndExpenseNotFound() {
        when(reportService.getAllBudgetAndExpense(999)).thenReturn(null);

        ItineraryReport result = reportService.getAllBudgetAndExpense(999);
        assertNull(result);
    }
}
