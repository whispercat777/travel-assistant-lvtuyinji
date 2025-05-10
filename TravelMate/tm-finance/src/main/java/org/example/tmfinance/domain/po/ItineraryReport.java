package org.example.tmfinance.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryReport {
    private List<Expense> expenses;
    private List<Budget> budgets;
    private Integer totalExpense;
    private Integer totalBudget;
}
