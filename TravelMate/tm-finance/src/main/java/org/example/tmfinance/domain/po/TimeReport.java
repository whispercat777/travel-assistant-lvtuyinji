package org.example.tmfinance.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeReport {
    private List<Expense> expenses;
    private Integer totalExpense;
}
