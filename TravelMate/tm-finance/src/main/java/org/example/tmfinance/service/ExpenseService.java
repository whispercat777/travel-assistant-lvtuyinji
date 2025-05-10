package org.example.tmfinance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.tmfinance.domain.po.Expense;
import org.example.tmfinance.domain.po.TimeReport;
import org.example.tmfinance.domain.po.TypeReport;

import java.util.List;

public interface ExpenseService extends IService<Expense> {
    Integer addExpense(Expense expense);
    Integer countRecordsByEveID(Integer eveID);
    Integer modifyExpense(Expense expense);
    List<Expense> getEventExpense(Integer eveID);
    TimeReport getTimeExpense(String startTime, String endTime, Integer userID);
    Boolean deleteExpenseByEveID(Integer eveID);
    TimeReport getAllExpense(Integer userID);
    TypeReport getTypeExpense(List<Integer>types, Integer userID);

}
