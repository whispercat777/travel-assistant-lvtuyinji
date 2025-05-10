package org.example.tmfinance.service;

import org.example.tmfinance.domain.po.ItineraryReport;

import java.util.List;

public interface ReportService {
    ItineraryReport getAllBudgetAndExpense(Integer userID);
    ItineraryReport getEventBudgetAndExpense(List<Integer> itiIDs);
}
